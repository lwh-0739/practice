package l.w.h.commonresult.response;

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
public class BaseResponse<T> {

    private Integer code;

    private String message;

    private T data;

}
