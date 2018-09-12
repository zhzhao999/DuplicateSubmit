/**
 * https://www.zhzhao.top
 */
package top.zhzhao.duplicatesubmit.annotation;

import java.lang.annotation.*;

/**
 * 防止表单重复提交注解
 *@author zhzhao
 *@version $ Id: DuplicateSubmitToken.java,V 0.1 2018/8/22 16:44 zhzhao Exp $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface DuplicateSubmitToken {
    /**
     * 一次请求完成之前防止重复提交
     */
    public static final int REQUEST = 1;
    /**
     * 一次会话中防止重复提交
     */
    public static final int SESSION = 2;

    /**
     * 防止重复提交类型，默认：一次请求完成之前防止重复提交
     */
    int type() default REQUEST;
}
