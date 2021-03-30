package l.w.h.jschpractice.exception;

import cn.zhgtv.common.exception.ZhgtvException;

/**
 * @author lwh
 * @date 2021/3/26 17:34
 **/
public class BaseException extends ZhgtvException {

    public static final int DEFAULT_ERROR_CODE = -3000;
    public static final String DEFAULT_ERROR_MESSAGE = "系统异常";

    public BaseException(){
        super(DEFAULT_ERROR_MESSAGE,DEFAULT_ERROR_CODE);
    }

    public BaseException(String message){
        super(message,DEFAULT_ERROR_CODE);
    }

    public BaseException(String message,int errorCode){
        super(message,errorCode);
    }

    public static int getDefaultErrorCode() {
        return DEFAULT_ERROR_CODE;
    }

    public static String getDefaultErrorMessage() {
        return DEFAULT_ERROR_MESSAGE;
    }

}
