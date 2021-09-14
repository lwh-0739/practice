import org.junit.Test;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author lwh
 * @date 2021/9/9 16:17
 **/
public class StreamPractice {

    @Test
    public void streamTest(){
        // mapTest();
        // floatMapTest();
        // peekTest();
        // mapToIntTest();
        // GenericObjectPool genericObjectPool = new GenericObjectPool();
    }

    /**
     * mapToInt测试
     */
    private void mapToIntTest(){
        Stream.of("one", "two", "three", "four")
                .mapToInt(String::length)
                .forEach(System.out::println);
    }

    /**
     * peek测试
     */
    private void peekTest(){
        Stream.of("one", "two", "three", "four")
                .filter(e -> e.length() > 3)
                .peek(e -> System.out.println("Filtered value: " + e))
                .filter(e -> e.length() > 4)
                // .map(String::toUpperCase)
                .peek(e -> System.out.println("Mapped value: " + e))
                .collect(Collectors.toList()).forEach(System.out::println);
    }

    /**
     * map测试
     */
    private void mapTest(){
        String[] strings = new String[]{"1","2","2","3","4"};
        Arrays.stream(strings).map(s -> s + s).distinct().collect(Collectors.toList()).forEach(System.out::println);
    }

    /**
     * floatMap测试
     */
    private void floatMapTest(){
        String[] strings1 = new String[]{"1","2","2","3","4"};
        String[] strings2 = new String[]{"11","22","22","33","44"};
        Stream.of(strings1,strings2).flatMap(e -> Arrays.stream(e).map(n -> n + n)).collect(Collectors.toList()).forEach(System.out::println);
    }

}
