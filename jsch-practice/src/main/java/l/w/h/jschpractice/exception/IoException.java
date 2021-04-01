package l.w.h.jschpractice.exception;

/**
 * @author lwh
 * @date 2021/3/26 17:41
 **/
public class IoException extends BaseException {

    private static final int DEFAULT_ERROR_CODE = -3003;
    private static final String DEFAULT_ERROR_MESSAGE = "IO异常";

    public IoException(){
        super(DEFAULT_ERROR_MESSAGE,DEFAULT_ERROR_CODE);
    }

    public IoException(String message){
        super(message,DEFAULT_ERROR_CODE);
    }

    public IoException(String message,int errorCode){
        super(message,errorCode);
    }

    public static int getDefaultErrorCode() {
        return DEFAULT_ERROR_CODE;
    }

    public static String getDefaultErrorMessage() {
        return DEFAULT_ERROR_MESSAGE;
    }

}
