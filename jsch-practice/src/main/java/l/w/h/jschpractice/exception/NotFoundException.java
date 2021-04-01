package l.w.h.jschpractice.exception;

/**
 * @author lwh
 * @date 2021/3/26 17:42
 **/
public class NotFoundException extends BaseException {

    private static final int DEFAULT_ERROR_CODE = -3004;
    private static final String DEFAULT_ERROR_MESSAGE = "不存在异常";

    public NotFoundException(){
        super(DEFAULT_ERROR_MESSAGE,DEFAULT_ERROR_CODE);
    }

    public NotFoundException(String message){
        super(message,DEFAULT_ERROR_CODE);
    }

    public NotFoundException(String message,int errorCode){
        super(message,errorCode);
    }

    public static int getDefaultErrorCode() {
        return DEFAULT_ERROR_CODE;
    }

    public static String getDefaultErrorMessage() {
        return DEFAULT_ERROR_MESSAGE;
    }

}
