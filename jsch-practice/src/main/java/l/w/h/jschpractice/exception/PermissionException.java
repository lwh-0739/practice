package l.w.h.jschpractice.exception;

/**
 * @author lwh
 * @date 2021/3/30 10:06
 **/
public class PermissionException extends BaseException {

    private static final int DEFAULT_ERROR_CODE = -3006;
    private static final String DEFAULT_ERROR_MESSAGE = "权限异常";

    public PermissionException(){
        super(DEFAULT_ERROR_MESSAGE,DEFAULT_ERROR_CODE);
    }

    public PermissionException(String message){
        super(message,DEFAULT_ERROR_CODE);
    }

    public PermissionException(String message,int errorCode){
        super(message,errorCode);
    }

    public static int getDefaultErrorCode() {
        return DEFAULT_ERROR_CODE;
    }

    public static String getDefaultErrorMessage() {
        return DEFAULT_ERROR_MESSAGE;
    }

}
