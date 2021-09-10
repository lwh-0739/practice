package l.w.h.hutool;

import org.ansj.domain.Result;
import org.ansj.recognition.Recognition;

/**
 * @author lwh
 * @date 2021/4/26 10:51
 **/
public class RecognitionImpl implements Recognition {

    @Override
    public void recognition(Result result) {
        result.getTerms().removeIf(term -> "".equals(String.valueOf(term).trim()));
    }

}
