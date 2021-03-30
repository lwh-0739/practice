package l.w.h.jschpractice.exception;

/**
 * @author lwh
 * @date 2021/3/30 10:05
 **/
public class LogicException extends BaseException {

    private static final int DEFAULT_ERROR_CODE = -3005;
    private static final String DEFAULT_ERROR_MESSAGE = "逻辑异常";

    public LogicException(){
        super(DEFAULT_ERROR_MESSAGE,DEFAULT_ERROR_CODE);
    }

    public LogicException(String message){
        super(message,DEFAULT_ERROR_CODE);
    }

    public LogicException(String message,int errorCode){
        super(message,errorCode);
    }

    public static int getDefaultErrorCode() {
        return DEFAULT_ERROR_CODE;
    }

    public static String getDefaultErrorMessage() {
        return DEFAULT_ERROR_MESSAGE;
    }

}
