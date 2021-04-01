package l.w.h.jschpractice.util;

import cn.zhgtv.common.result.BaseResponse;
import l.w.h.jschpractice.exception.ExistedException;
import l.w.h.jschpractice.exception.IoException;
import l.w.h.jschpractice.exception.NotFoundException;
import l.w.h.jschpractice.exception.ParameterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author lwh
 * @date 2021/3/26 17:33
 **/
public class Util {

    public static Boolean osIsLinux;

    public static String encoding;

    public static final String WIN = "win";

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    /**
     * 查看文件内容的最大长度：8M
     */
    public static final Integer OPEN_FILE_MAX = 8388608;

    /**
     * 错误日志   共用
     */
    public static final Logger errorLog  = LoggerFactory.getLogger("errorLog");

    /**
     * 发送成功请求
     */
    public static <T> BaseResponse<T> sendSuccessResponse(T data){
        return BaseResponse.<T>builder()
                .code(BaseResponse.DEFAULT_SUCCESS_CODE)
                .message(BaseResponse.DEFAULT_SUCCESS_MESSAGE)
                .result(data).build();
    }

    /**
     * 释放资源
     * @param reader 字符输入流
     */
    private static void close(Reader reader){
        if (reader != null){
            try {
                reader.close();
            } catch (IOException e) {
                errorLog.error("释放IO资源失败！");
            }
        }
    }

    /**
     * 释放资源
     * @param writer 字符输出流
     */
    public static void close(Writer writer){
        if (writer != null){
            try {
                writer.close();
            } catch (IOException e) {
                errorLog.error("释放IO资源失败！");
            }
        }
    }

    /**
     * 释放资源
     * @param inputStream 字节输入流
     */
    private static void close(InputStream inputStream){
        if (inputStream != null){
            try {
                inputStream.close();
            } catch (IOException e) {
                errorLog.error("释放IO资源失败！");
            }
        }
    }

    /**
     * 释放资源
     * @param outputStream 字节输出流
     */
    private static void close(OutputStream outputStream){
        if (outputStream != null){
            try {
                outputStream.close();
            } catch (IOException e) {
                errorLog.error("释放IO资源失败！");
            }
        }
    }

    private static final Locale LOCALE = Locale.CHINA;

    /**
     * 判断string是否包含anotherString
     * ignoreCase：是否忽略大小写
     */
    public static Boolean stringContainAnotherString(String string,String anotherString,Boolean ignoreCase){
        if (string == null || anotherString == null){
            return false;
        }
        if (ignoreCase){
            string = string.toLowerCase(LOCALE);
            anotherString = anotherString.toLowerCase(LOCALE);
        }
        return string.contains(anotherString);
    }

    /**
     * 判断字符串是否为null或""
     * @param string 要进行判断的字符串
     * @param errorPre 错误前缀
     */
    public static void processString(String string,String errorPre){
        if (string == null || "".equals(string)){
            errorLog.error(errorPre + " 不能为null或\"\"");
            throw new ParameterException(errorPre + " 不能为null或\"\"");
        }
    }

    public static final char LINUX_SEPARATOR = '/';
    private static final char WIN_SEPARATOR = '\\';

    /**
     * 处理传入的路径
     * @param path 路径
     * @return 修改后的路径
     */
    private static String processPath(String path){
        char lastChar = path.charAt(path.length() - 1);
        if (LINUX_SEPARATOR != (lastChar) && WIN_SEPARATOR != lastChar){
            path = path + File.separator;
        }
        return path;
    }

    /**
     * 判断传入的路径格式是否正确
     * @param path 路径
     * @param errorPre 错误前缀信息
     */
    private static void processPath2(String path, String errorPre){
        processString(path,errorPre);
        boolean flag = true;
        if (osIsLinux){
            if (path.indexOf(WIN_SEPARATOR) != -1){
                flag = false;
            }
        }else {
            if (path.indexOf(LINUX_SEPARATOR) != -1){
                flag = false;
            }
        }
        if (!flag){
            Util.errorLog.error(errorPre + "路径格式有误！");
            throw new ParameterException("路径格式有误！");
        }
    }

    /**
     * 处理路径
     * @param path 路径
     * @param errorPre 错误前缀
     * @return 若path为null或""，返回当前目录"./"，否则验证路径是否正确，且补充路径最后的分隔符
     */
    public static String processPath3(String path,String errorPre){
        if (path == null || "".equals(path)){
            path = "./";
        }else {
            processPath2(path,errorPre);
            path = Util.processPath(path);
        }
        return path;
    }

    /**
     * 处理文件名称
     * @param fileName 文件（目录）名称
     * @param errorPre 错误前缀
     * @return fileName
     */
    public static String processFileName(String fileName,String errorPre){
        if (fileName == null || "".equals(fileName)){
            errorLog.error(errorPre + "文件（目录）名称不能为null或\"\"");
            throw new ParameterException("文件（目录）名称不能为null或\"\"");
        }
        int start = 0;
        int lastIndex = fileName.length() - 1;
        int end = lastIndex;
        if (osIsLinux){
            if (fileName.charAt(0) == LINUX_SEPARATOR){
                start++;
            }
            if (fileName.charAt(lastIndex) == LINUX_SEPARATOR){
                end--;
            }
        }else {
            if (fileName.charAt(0) == WIN_SEPARATOR){
                start++;
            }
            if (fileName.charAt(lastIndex) == WIN_SEPARATOR){
                end--;
            }
        }
        if (!(start == 0 && end == lastIndex)){
            fileName = fileName.substring(start,end);
        }
        return fileName;
    }

    /**
     * 删除指定文件或路径下的所有文件
     * @param file 文件、目录
     */
    public static void deleteByPath(File file){
        boolean delete;
        if (file.isFile()){
            delete = file.delete();
        }else {
            File[] fileList = file.listFiles();
            if (fileList != null && fileList.length > 0){
                for (File f :fileList
                ) {
                    deleteByPath(f);
                }
            }else {
                delete = file.delete();
            }
        }
    }

    /**
     * 创建目录
     * @param path 路径 默认为path不为null或""，且符合系统路径格式
     */
    public static void createPath(String path){
        File file = new File(path);
        if (!file.exists()){
            try {
                boolean mkdirs = file.mkdirs();
            } catch (Exception e) {
                errorLog.error("创建" + path + " 失败！");
                throw new IoException(e.getMessage());
            }
        }
    }

    /**
     * 具体的下载实现（单个文件）
     * @param path 完整路径（即：path + fileName）
     * @param response 响应对象
     * 注：此时path对应的文件是已存在的，不需再进行额外的验证
     */
    public static void download(String path, HttpServletResponse response){
        File file = new File(path);
        String name = file.getName();
        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            byte[] buffer = new byte[bufferedInputStream.available()];
            int read = bufferedInputStream.read(buffer);
            response.reset();
            response.addHeader("Content-Disposition",
                    "attachment;filename=" + new String(name.getBytes(), StandardCharsets.ISO_8859_1));
            response.addHeader("Content-Length", "" + file.length());
            response.setContentType("application/octet-stream");
            bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());
            bufferedOutputStream.write(buffer);
            bufferedOutputStream.flush();
        } catch (Exception e) {
            errorLog.error("下载文件：" + path + " 失败！");
            throw new IoException("下载文件：" + path + " 失败！");
        } finally {
            close(fileInputStream);
            close(bufferedInputStream);
            close(bufferedOutputStream);
        }
    }

    /**
     * 压缩目录，并返回压缩后的与目录同名zip文件
     * @param sourcePath 要压缩的目录（最后一个字符不是分隔符）
     * @param zipPath 压缩的文件位置（sourcePath.zip）
     * @return 压缩文件file
     */
    public static File createZip(String sourcePath,String zipPath){
        File file = new File(zipPath);
        if (file.exists()){
            errorLog.error(zipPath + " 已存在！不能下载！");
            throw new ExistedException(zipPath + " 已存在！不能下载！");
        }
        File folder = new File(sourcePath);
        boolean flag = true;
        if (!folder.exists()){
            flag = false;
        }else {
            String[] list = folder.list();
            if (list == null || list.length == 0){
                flag = false;
            }
        }
        if (!flag){
            errorLog.error(sourcePath + " 目录不存在或该目录下无文件！");
            throw new NotFoundException(sourcePath + " 目录不存在或该目录下无文件！");
        }
        FileOutputStream fileOutputStream = null;
        ZipOutputStream zipOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            zipOutputStream = new ZipOutputStream(fileOutputStream);
            writeZip(folder,"",zipOutputStream);
        } catch (Exception e) {
            errorLog.error(sourcePath + " 目录压缩失败：" + e.getMessage());
            throw new IoException(sourcePath + " 目录压缩失败：" + e.getMessage());
        } finally {
            Util.close(zipOutputStream);
            Util.close(fileOutputStream);
        }
        return file;
    }

    /**
     * 具体的压缩过程
     * @param file 文件、目录文件
     * @param parentPath 上一级目录
     * @param zipOutputStream 字节输出流
     */
    private static void writeZip(File file,String parentPath,ZipOutputStream zipOutputStream){
        if (!file.exists()){
            errorLog.error(file.getAbsolutePath() + " 不存在！");
            throw new NotFoundException(file.getAbsolutePath() + " 不存在！");
        }
        if (file.isDirectory()){
            parentPath += file.getName() + File.separator;
            File[] fileList = file.listFiles();
            if (fileList != null && fileList.length != 0){
                for (File f:fileList
                ) {
                    writeZip(f,parentPath,zipOutputStream);
                }
            }
        }else {
            FileInputStream fileInputStream = null;
            BufferedInputStream bufferedInputStream = null;
            try {
                fileInputStream = new FileInputStream(file);
                bufferedInputStream = new BufferedInputStream(fileInputStream);
                ZipEntry ze = new ZipEntry(parentPath + file.getName());
                zipOutputStream.putNextEntry(ze);
                int size;
                byte[] buffer = new byte[1024];
                while ((size = bufferedInputStream.read(buffer)) > 0){
                    zipOutputStream.write(buffer,0,size);
                    zipOutputStream.flush();
                }
            }catch (Exception e){
                errorLog.error(file.getAbsolutePath() + " 读取失败！或压缩写入过程出现问题！");
                throw new IoException(file.getAbsolutePath() + " 读取失败！或压缩写入过程出现问题！");
            }finally {
                Util.close(bufferedInputStream);
                Util.close(fileInputStream);
            }
        }
    }

    /**
     * 自调单位转换
     * @param size 文件大小
     * @return size
     */
    public static String getSize(long size) {
        DecimalFormat df = new DecimalFormat("0.00");
        if (size >> 40 >= 1){
            return df.format((float) size / 1024 / 1024 / 1024 / 1024) + " TB";
        }else if (size >> 30 >= 1){
            return df.format((float) size / 1024 / 1024 / 1024) + " GB";
        }else if (size >> 20 >= 1){
            return df.format((float) size / 1024 / 1024) + " MB";
        }else if (size >> 10 >= 1){
            return df.format((float) size / 1024) + " KB";
        }
        return df.format((float) size) + " B ";
    }

    /**
     * 获取磁盘使用情况
     * @return String
     */
    public static String getDriverInfo() {
        StringBuilder stringBuilder = new StringBuilder();
        File[] roots = File.listRoots();
        for (File file : roots) {
            stringBuilder.append(file.getPath())
                    .append(" (")
                    .append(getSize(file.getFreeSpace()))
                    .append(" Free，Total ")
                    .append(getSize(file.getTotalSpace()))
                    .append(")")
                    .append(LINE_SEPARATOR);
        }
        return stringBuilder.toString();
    }

    public static final String Y = "Y";
    public static final String N = "N";
    public static final String A = "A";

    /**
     * 判断一个文件是否包含指定后缀
     * @param file 文件 存在，不为null
     * @param subfix 指定后缀   不为null
     * @return true：包含  false：不包含
     */
    private static boolean fileEndWithSubfix(File file,String subfix){
        return stringContainAnotherString(file.getName(),subfix,true);
    }

    /**
     * 判断一个文件是否以指定后缀列表中的后缀结束
     * @param file 文件 存在，不为null
     * @param subfixList 后缀列表   不为null，且个数不为0
     * @return true：包含  false：不包含
     */
    public static boolean fileEndWithInSubfixList(File file, List<String> subfixList){
        for (String subfix:subfixList
        ) {
            if (fileEndWithSubfix(file,subfix)){
                return true;
            }
        }
        return false;
    }

    /**
     * 读取指定文件的内容
     * @param file 文件 不为null，且可读
     * @param errorPre 错误前缀
     * @return 文件内容
     */
    public static String readFile(File file,String errorPre){
        BufferedReader bufferedReader = null;
        FileReader fileReader = null;
        StringBuilder content = new StringBuilder();
        try {
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null){
                content.append(line).append(Util.LINE_SEPARATOR);
            }
        } catch (Exception e) {
            Util.errorLog.error(errorPre + file.getAbsolutePath() + " 文件读取失败！" + e.getMessage());
            throw new IoException(file.getAbsolutePath() + " 文件读取失败！" + e.getMessage());
        } finally {
            Util.close(bufferedReader);
            Util.close(fileReader);
        }
        return content.toString();
    }

    /**
     * 比较时间大小
     * @param time 时间
     * @param anotherTime 另一时间
     * @return 时间 - 另一时间
     */
    public static int timeIsMoreThanAnotherTime(String time,String anotherTime){
        return stringToDate(time).compareTo(stringToDate(anotherTime));
    }

    /**
     * 字符串转时间
     * @param dateString 时间字符串   格式：yyyy-MM-dd
     * @return Date
     */
    private static Date stringToDate(String dateString){
        Date date;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            Util.errorLog.error("字符串转Date失败！dateString = " + dateString);
            throw new ParameterException("字符串转Date失败！dateString = " + dateString);
        }
        return date;
    }

    /**
     * 获取客户端的IP地址
     */
    public static String getIpAdrress(HttpServletRequest request) {
        if (request == null){
            Util.errorLog.error("不能获取到request！");
            return null;
        }
        String unKnown = "unKnown";
        String xip = request.getHeader("X-Real-IP");
        String xFor = request.getHeader("X-Forwarded-For");
        if(!StringUtils.isEmpty(xFor) && !unKnown.equalsIgnoreCase(xFor)){
            /*
             * 多次反向代理后会有多个ip值，第一个ip才是真实ip
             */
            int index = xFor.indexOf(",");
            if(index != -1){
                return xFor.substring(0,index);
            }else{
                return xFor;
            }
        }
        xFor = xip;
        if(!StringUtils.isEmpty(xFor) && !unKnown.equalsIgnoreCase(xFor)){
            return xFor;
        }
        if (StringUtils.isEmpty(xFor) || unKnown.equalsIgnoreCase(xFor)) {
            xFor = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(xFor) || unKnown.equalsIgnoreCase(xFor)) {
            xFor = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(xFor) || unKnown.equalsIgnoreCase(xFor)) {
            xFor = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isEmpty(xFor) || unKnown.equalsIgnoreCase(xFor)) {
            xFor = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isEmpty(xFor) || unKnown.equalsIgnoreCase(xFor)) {
            xFor = request.getRemoteAddr();
        }
        return xFor;
    }

}
