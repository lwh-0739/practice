package l.w.h.jschpractice.exception;

/**
 * @author lwh
 * @date 2021/3/26 17:40
 **/
public class ExistedException extends BaseException {

    private static final int DEFAULT_ERROR_CODE = -3002;
    private static final String DEFAULT_ERROR_MESSAGE = "已存在异常";

    public ExistedException(){
        super(DEFAULT_ERROR_MESSAGE,DEFAULT_ERROR_CODE);
    }

    public ExistedException(String message){
        super(message,DEFAULT_ERROR_CODE);
    }

    public ExistedException(String message,int errorCode){
        super(message,errorCode);
    }

    public static int getDefaultErrorCode() {
        return DEFAULT_ERROR_CODE;
    }

    public static String getDefaultErrorMessage() {
        return DEFAULT_ERROR_MESSAGE;
    }

}
