package l.w.h.jschpractice.exception;

/**
 * @author lwh
 * @date 2021/3/26 17:39
 **/
public class ParameterException extends BaseException {

    private static final int DEFAULT_ERROR_CODE = -3001;
    private static final String DEFAULT_ERROR_MESSAGE = "参数异常";

    public ParameterException(){
        super(DEFAULT_ERROR_MESSAGE,DEFAULT_ERROR_CODE);
    }

    public ParameterException(String message){
        super(message,DEFAULT_ERROR_CODE);
    }

    public ParameterException(String message,int errorCode){
        super(message,errorCode);
    }

    public static int getDefaultErrorCode() {
        return DEFAULT_ERROR_CODE;
    }

    public static String getDefaultErrorMessage() {
        return DEFAULT_ERROR_MESSAGE;
    }

}
