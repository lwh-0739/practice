package l.w.h.commonresult.exception;

import lombok.Getter;

/**
 * @author lwh
 * @since 2022/3/14
 */
@Getter
public enum CustomException {

    /**
     * 自定义异常
     */
    BASE_EXCEPTION(-3000,"服务器内部错误"),
    PARAMETER_EXCEPTION(-3001,"参数异常"),
    EXISTED_EXCEPTION(-3002,"已存在异常"),
    IO_EXCEPTION(-3003,"IO异常"),
    NOT_FOUND_EXCEPTION(-3004,"不存在异常"),
    LOGIC_EXCEPTION(-3005,"逻辑异常"),
    PERMISSION_EXCEPTION(-3006,"权限异常");


    private final Integer errorCode;

    private final String message;

    CustomException(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

}
