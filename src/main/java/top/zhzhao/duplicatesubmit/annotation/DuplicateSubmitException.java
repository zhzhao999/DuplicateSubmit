/**
 * https://www.zhzhao.top
 */
package top.zhzhao.duplicatesubmit.annotation;

/**
 *重复提交异常
 *@author zhzhao
 *@version $ Id: DuplicateSubmitException.java,V 0.1 2018/8/22 16:56 zhzhao Exp $
 */
public class DuplicateSubmitException extends RuntimeException {

    public DuplicateSubmitException(String msg) {
        super(msg);
    }

    public DuplicateSubmitException(String msg, Throwable cause){
        super(msg,cause);
    }
}
