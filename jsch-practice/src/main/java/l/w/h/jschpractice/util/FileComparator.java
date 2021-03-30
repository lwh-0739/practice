package l.w.h.jschpractice.util;

import lombok.Builder;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;

/**
 * @author lwh
 * @date 2021/3/30 9:47
 **/
@Builder
public class FileComparator implements Comparator<File>, Serializable {

    @Override
    public int compare(File o1, File o2) {
        boolean o1IsFile = o1.isFile();
        boolean o2IsFile = o2.isFile();
        if (o1IsFile == o2IsFile){
            return o1.getName().compareTo(o2.getName());
        }
        if (o1IsFile){
            return 1;
        }
        return -1;
    }

}
