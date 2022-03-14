package l.w.h.commonresult.util;

import l.w.h.commonresult.exception.BaseException;
import l.w.h.commonresult.exception.CustomException;
import l.w.h.commonresult.response.BaseResponse;

/**
 * @author lwh
 * @since 2022/3/14
 */
public class CommonUtil {

    /**
     * 获取成功返回对象
     */
    public static <T> BaseResponse<T> gainSuccessResponse(T data){
        return gainSuccessResponse(CommonConstant.DEFAULT_RESULT_CODE,CommonConstant.SUCCESS_MESSAGE,data);
    }

    /**
     * 获取成功返回对象
     */
    public static <T> BaseResponse<T> gainSuccessResponse(Integer code,T data){
        return gainSuccessResponse(code,CommonConstant.SUCCESS_MESSAGE,data);
    }

    /**
     * 获取成功返回对象
     */
    public static <T> BaseResponse<T> gainSuccessResponse(Integer code,String message,T data){
        return BaseResponse
                .<T>builder()
                .code(code)
                .message(message)
                .data(data)
                .build();
    }

    /**
     * 获取失败返回对象
     */
    private static BaseException gainErrorResponse(Integer errorCode, String message){
        return BaseException
                .builder()
                .errorCode(errorCode)
                .message(message)
                .build();
    }

    /**
     * 抛系统异常
     */
    public static void throwBaseException(String message) throws BaseException {
        throwException(CustomException.BASE_EXCEPTION.getErrorCode(), whetherUseDefault(message) ? CustomException.BASE_EXCEPTION.getMessage() : message);
    }

    /**
     * 抛参数异常
     */
    public static void throwParameterException(String message) throws BaseException {
        throwException(CustomException.PARAMETER_EXCEPTION.getErrorCode(), whetherUseDefault(message) ? CustomException.BASE_EXCEPTION.getMessage() : message);
    }

    /**
     * 抛参数异常
     */
    public static void throwExistedException(String message) throws BaseException {
        throwException(CustomException.EXISTED_EXCEPTION.getErrorCode(), whetherUseDefault(message) ? CustomException.BASE_EXCEPTION.getMessage() : message);
    }

    /**
     * 抛IO异常
     */
    public static void throwIoException(String message) throws BaseException {
        throwException(CustomException.IO_EXCEPTION.getErrorCode(), whetherUseDefault(message) ? CustomException.BASE_EXCEPTION.getMessage() : message);
    }

    /**
     * 抛不存在异常
     */
    public static void throwNotFoundException(String message) throws BaseException {
        throwException(CustomException.NOT_FOUND_EXCEPTION.getErrorCode(), whetherUseDefault(message) ? CustomException.BASE_EXCEPTION.getMessage() : message);
    }

    /**
     * 抛逻辑异常
     */
    public static void throwLogicException(String message) throws BaseException {
        throwException(CustomException.LOGIC_EXCEPTION.getErrorCode(), whetherUseDefault(message) ? CustomException.BASE_EXCEPTION.getMessage() : message);
    }

    /**
     * 抛权限异常
     */
    public static void throwPermissionException(String message) throws BaseException {
        throwException(CustomException.PERMISSION_EXCEPTION.getErrorCode(), whetherUseDefault(message) ? CustomException.BASE_EXCEPTION.getMessage() : message);
    }

    /**
     * 是否使用默认的信息
     */
    private static boolean whetherUseDefault(String message){
        return null == message || "".equals(message);
    }

    /**
     * 抛异常信息
     */
    public static void throwException(Integer errorCode, String customDescription) throws BaseException {
        throw gainErrorResponse(errorCode,customDescription);
    }

}
