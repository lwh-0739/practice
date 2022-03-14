package l.w.h.commonresult.exception;

import lombok.*;

/**
 * @author lwh
 * @since 2022/3/14
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseException extends RuntimeException {

    private Integer errorCode;

    private String message;

}
