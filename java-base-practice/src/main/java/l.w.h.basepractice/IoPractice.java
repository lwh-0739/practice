package l.w.h.basepractice;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author lwh
 * @since 2022/3/7 13:59
 **/
public class IoPractice {

    /**
     * IO字节、字符流测试
     */
    public static void main(String[] args) throws IOException {
        // bufferReaderTest();
        bufferWriteTest();
        // byteInputStreamTest();
        // byteOutputTest();
    }

    private final static String TEST_PATH = "./tmp/test.txt";

    /**
     * 字节流测试
     */
    private static void byteInputStreamTest() throws IOException {
        File file = new File("./pom.xml");
        if (file.exists()){
            try(FileInputStream fileInputStream = new FileInputStream(file)) {
                System.out.println(fileInputStream.available());
            }
        }else {
            System.out.println("文件不存在！");
        }
    }

    private static void byteOutputTest() throws IOException {
        File file = new File(TEST_PATH);
        if (!file.exists()){
            System.out.println(file.createNewFile());
        }
        try(FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write("testest".getBytes(StandardCharsets.UTF_8));
        }
    }

    /**
     * 字符流测试
     * @throws IOException IO异常
     */
    private static void bufferReaderTest() throws IOException {
        File file = new File("./pom.xml");
        if (file.exists()){
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
                String line;
                while (null != (line = bufferedReader.readLine())) {
                    System.out.println(line);
                }
            }
        }else {
            System.out.println("文件不存在！");
        }
    }

    private static void bufferWriteTest() throws IOException {
        File file = new File(TEST_PATH);
        if (!file.exists()){
            System.out.println(file.createNewFile());
        }
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.write("test");
        }
    }

}
