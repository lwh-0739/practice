package l.w.h.rxjavapractice.help;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author lwh
 * @since 2022/1/26 16:15
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    private String uid;

    private String name;

    private String classUid;

    private Integer age;

    private List<SourceScore> sourceScoreList;

}
