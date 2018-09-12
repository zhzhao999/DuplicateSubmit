/**
 * https://www.zhzhao.top
 */
package top.zhzhao.duplicatesubmit.annotation;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.UUID;

/**
 * 防止表单重复提交拦截器
 *@author zhzhao
 *@version $ Id: DuplicateSubmitAspect.java,V 0.1 2018/8/22 16:47 zhzhao Exp $
 */
@Aspect
@Component
@Slf4j
public class DuplicateSubmitAspect {
    public static final String  DUPLICATE_TOKEN_KEY = "duplicate_token_key";

    @Pointcut("execution(public * top.zhzhao.duplicatesubmit..*(..))")
    public void webLog() {
    }

    @Before("webLog() && @annotation(token)")
    public void before(final JoinPoint joinPoint, DuplicateSubmitToken token){
        if (token!=null){
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String appId = getAppId(request);
            String key = getDuplicateTokenKey(joinPoint,appId);
            Object duplicateValue = request.getSession().getAttribute(key);
            if (null == duplicateValue){
                request.getSession().setAttribute(key,"true");
                log.info("token-key = " + key);
            }else {
                throw new DuplicateSubmitException("表单重复提交");
            }

        }
    }

    @AfterReturning("webLog() && @annotation(token)")
    public void doAfterReturning(JoinPoint joinPoint,DuplicateSubmitToken token) {
        // 处理完请求，返回内容
        if (token!=null){
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String appId = getAppId(request);
            String key = getDuplicateTokenKey(joinPoint,appId);
            Object duplicateValue = request.getSession().getAttribute(key);
            if (null != duplicateValue && token.type() == DuplicateSubmitToken.REQUEST){
                HttpSession session = request.getSession(false);
                session.removeAttribute(key);
            }
        }
    }

    /**
     * 异常处理
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(pointcut = "webLog()&& @annotation(token)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e, DuplicateSubmitToken token) {
        //处理重复提交本身之外的异常
        if (null != token && e instanceof DuplicateSubmitException == false){
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String appId = getAppId(request);
            String key=getDuplicateTokenKey(joinPoint,appId);
            Object t = request.getSession().getAttribute(key);
            if (null != t){
                //方法执行完毕移除请求重复标记
                request.getSession(false).removeAttribute(key);
            }
        }
    }

    /**
     * 获取验证重复提交的Key
     * @param joinPoint
     * @return
     */
    public String getDuplicateTokenKey(JoinPoint joinPoint,String appId) {
        String methodName = joinPoint.getSignature().getName();
        StringBuilder key = new StringBuilder(DUPLICATE_TOKEN_KEY);
        key.append(",").append(methodName).append(",").append(appId);
        return key.toString();
    }

    /**
     * 获取RESTFUL风格url中的appId
     * @return appId
     */
    public String getAppId(HttpServletRequest request){
        HashMap<String,String> attribute = (HashMap)request
                .getAttribute("org.springframework.web.servlet.View.pathVariables");
        String appId = attribute.get("appId");
        if (StringUtils.isBlank(appId)){
            throw new RuntimeException("表单验证异常，未查询到应用ID");
        }
        return appId;
    }
}
