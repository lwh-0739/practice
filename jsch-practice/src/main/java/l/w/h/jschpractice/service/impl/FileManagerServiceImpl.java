package l.w.h.jschpractice.service.impl;

import l.w.h.jschpractice.bean.SystemInfo;
import l.w.h.jschpractice.exception.*;
import l.w.h.jschpractice.service.FileManagerService;
import l.w.h.jschpractice.util.FileComparator;
import l.w.h.jschpractice.util.Util;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author lwh
 * @date 2021/3/26 17:28
 **/
@Service
public class FileManagerServiceImpl implements FileManagerService {

    /**
     * 获取指定路径下的文件列表
     * @param path 路径
     * @return 文件列表
     */
    @Override
    public List<File> findFileList(String path,Boolean all) {
        String errorPre = "获取指定路径下的文件列表：";
        File file = new File(path = Util.processPath3(path,errorPre));
        if (!file.exists()){
            Util.errorLog.error("查找指定路径下的目录：path = " + path + " 不存在！");
            throw new ParameterException("path = " + path + " 不存在！");
        }
        if (file.isFile()){
            Util.errorLog.error("查找指定路径下的目录：path = " + path + " 不是路径！");
            throw new ParameterException("path = " + path + " 不是路径！");
        }
        List<File> returnFileList = new ArrayList<>();
        File[] fileList = file.listFiles(pathname -> {
            if (all == null || !all){
                return !pathname.isHidden();
            }
            return true;
        });
        if (fileList != null && fileList.length > 0){
            returnFileList.addAll(Arrays.asList(fileList));
        }
        returnFileList.sort(FileComparator.builder().build());
        return returnFileList;
    }

    /**
     * 创建文件或目录
     * @param path 路径
     * @param fileName 文件、目录名称
     * @param isFile true：创建文件 false：创建目录
     * @return 是否成功
     */
    @Override
    public String createFileOrFolder(String path, String fileName, Boolean isFile) {
        if (isFile == null){
            Util.errorLog.error("未指定创建的是文件还是目录！");
            throw new ParameterException("未指定创建的是文件还是目录！");
        }
        String type = (isFile ? "文件" : "目录");
        String errorPre = "创建" + type + "：";
        fileName = Util.processFileName(fileName,errorPre);
        File file = new File(Util.processPath3(path,errorPre) + fileName);
        if (file.exists()){
            Util.errorLog.error(errorPre + "fileName = " + fileName + " 的文件或目录已存在！");
            throw new ExistedException("fileName = " + fileName + " 的文件或目录已存在！");
        }
        try {
            boolean isCreate;
            if (isFile){
                isCreate = file.createNewFile();
            }else {
                isCreate = file.mkdir();
            }
        }catch (Exception e){
            Util.errorLog.error("创建" + type + "：" + fileName + " 失败！");
            throw new IoException(e.getMessage());
        }
        return "创建" + type + "成功！";
    }

    /**
     * 重命名
     * @param path 文件或目录所在的路径
     * @param fileName 要进行重命名的文件或目录
     * @param newName 新的名称
     * @return 是否成功
     */
    @Override
    public String rename(String path, String fileName, String newName) {
        String errorPre = "重命名操作：";
        fileName = Util.processFileName(fileName,errorPre);
        /*
         * 内部类访问的变量是使用final修饰的，也可不显式的使用final声明，即Effectively final，不可对变量重新赋值
         * String newName1 相当于 final String newName1
         */
        String newName1 = Util.processFileName(newName,errorPre + "新");
        File file = new File(path = Util.processPath3(path,errorPre),fileName);
        if (!file.exists()){
            Util.errorLog.error(errorPre + path + fileName + " 文件（目录）不存在！");
            throw new NotFoundException(path + fileName + " 文件（目录）不存在！");
        }
        if (!fileName.equals(newName1)){
            String[] list = new File(path).list((dir, name) -> name.equals(newName1));
            if (list != null && list.length > 0){
                Util.errorLog.error(errorPre + path + " 下已存在newName = " + newName1 + " 的文件或目录！");
                throw new ExistedException(path + " 下已存在newName = " + newName1 + " 的文件或目录！");
            }
            File f = new File(path,newName1);
            boolean renameTo = file.renameTo(f);
        }
        return "重命名成功！";
    }

    /**
     * 删除指定路径下的文件或目录
     * @param path 路径
     * @param fileName 文件或目录名称
     * @return 是否成功
     */
    @Override
    public String delete(String path, String fileName) {
        String errorPre = "删除指定路径下的文件或目录：";
        fileName = Util.processFileName(fileName,errorPre);
        File file = new File(path = Util.processPath3(path,errorPre),fileName);
        if (!file.exists()){
            Util.errorLog.error(errorPre + path + fileName + " 文件（目录）不存在！");
            throw new NotFoundException(path + fileName + " 文件（目录）不存在！");
        }
        Util.deleteByPath(file);
        return "删除成功！";
    }

    /**
     * 打开指定文件（获取指定文件的内容）
     * @param path 路径
     * @param fileName 文件名称
     * @return 文件内容
     */
    @Override
    public String openFile(String path, String fileName) {
        String errorPre = "打开指定文件（获取指定文件的内容）：";
        fileName = Util.processFileName(fileName,errorPre);
        File file = new File(path = Util.processPath3(path,errorPre),fileName);
        if (!file.exists()){
            Util.errorLog.error(errorPre + path + fileName + " 文件不存在！");
            throw new NotFoundException(path + fileName + " 文件不存在！");
        }
        if (file.isDirectory()){
            Util.errorLog.error(errorPre + path + fileName + " 是目录！不能查看内容！");
            throw new LogicException(path + fileName + " 是目录！不能查看内容！");
        }
        if (!file.canRead()){
            Util.errorLog.error(errorPre + path + fileName + " 文件不能读取！权限为不可读！");
            throw new PermissionException(path + fileName + " 文件不能读取！权限为不可读！");
        }
        if (file.length() > Util.OPEN_FILE_MAX){
            Util.errorLog.error(errorPre + "文件：" + path + fileName + " 大小超出限制，请使用ssh客户端查看！");
            throw new LogicException(path + fileName + " 大小超出限制，请使用ssh客户端查看！");
        }
        return Util.readFile(file,errorPre);
    }

    /**
     * 修改文件内容
     * @param path 路径
     * @param fileName 文件名称
     * @param content 新的文件内容
     * @return 是否成功
     */
    @Override
    public String updateFile(String path, String fileName, String content) {
        String errorPre = "修改文件内容：";
        fileName = Util.processFileName(fileName,errorPre);
        Util.processString(content,errorPre + "新的文件内容");
        if (content.length() > Util.OPEN_FILE_MAX){
            Util.errorLog.error(errorPre + "文件：" + path + fileName + " 大小超出限制，请使用ssh客户端更新内容！");
            throw new LogicException(path + fileName + " 大小超出限制，请使用ssh客户端更新内容！");
        }
        File file = new File(path = Util.processPath3(path,errorPre),fileName);
        if (!file.exists()){
            Util.errorLog.error(errorPre + path + fileName + " 文件不存在！");
            throw new NotFoundException(path + fileName + " 文件不存在！");
        }
        if (file.isDirectory()){
            Util.errorLog.error(errorPre + path + fileName + " 是目录！不能修改内容！");
            throw new LogicException(path + fileName + " 是目录！不能修改内容！");
        }
        if (!file.canWrite()){
            Util.errorLog.error(errorPre + path + fileName + " 文件不能写入！权限为不可写入！");
            throw new PermissionException(path + fileName + " 文件不能写入！权限为不可写入！");
        }
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.flush();
        } catch (Exception e) {
            Util.errorLog.error(errorPre + path + fileName + " 文件写入失败！" + e.getMessage());
            throw new IoException(path + fileName + " 文件写入失败！" + e.getMessage());
        } finally {
            Util.close(fileWriter);
        }
        return "修改成功！";
    }

    /**
     * 上传文件（夹）
     * @param multipartFiles 文件（夹）
     * @return 是否成功
     */
    @Override
    public String upload(String path, MultipartFile[] multipartFiles) {
        String errorPre = "上传文件（夹）：";
        if (multipartFiles == null || multipartFiles.length == 0){
            Util.errorLog.error(errorPre + "请选择要上传的文件（夹）！");
            throw new ParameterException("请选择要上传的文件（夹）！");
        }
        File file = new File(path = Util.processPath3(path,errorPre));
        if (!file.exists()){
            Util.errorLog.error(errorPre + path + " 路径不存在！");
            throw new NotFoundException(path + " 路径不存在！");
        }
        /*
         * 验证当前路径下是否存在同名的文件或目录，若存在，则不进行上传
         */
        String[] list = file.list();
        boolean listIsNull = list == null || list.length == 0;
        if (!listIsNull){
            List<String> pathFileList = Arrays.asList(list);
            for (MultipartFile multipartFile:multipartFiles
            ) {
                String originalFilename = multipartFile.getOriginalFilename();
                if (originalFilename != null && !"".equals(originalFilename)){
                    if (Util.stringContainAnotherString(originalFilename,Util.LINUX_SEPARATOR + "",false)){
                        /*
                         * 目录
                         * 判断目录（若存在多级目录，只判断第一级目录）是否存在，若存在，不可上传
                         */
                        originalFilename = originalFilename.substring(0, originalFilename.indexOf(Util.LINUX_SEPARATOR));
                    }
                    if (pathFileList.contains(originalFilename)){
                        Util.errorLog.error(errorPre + path  + "下存在相同名称的文件或目录：" + originalFilename + "！不能上传！");
                        throw new ExistedException(path  + "下存在相同名称的文件或目录：" + originalFilename + "！不能上传！请先进行删除！");
                    }
                }
            }
        }
        /*
         * 上传
         */
        String tmpPath;
        for (MultipartFile multipartFile:multipartFiles
        ) {
            String originalFilename = multipartFile.getOriginalFilename();
            if (originalFilename != null && !"".equals(originalFilename)){
                if (Util.stringContainAnotherString(originalFilename,Util.LINUX_SEPARATOR + "",false)){
                    tmpPath = path + originalFilename.substring(0, originalFilename.lastIndexOf(Util.LINUX_SEPARATOR));
                    Util.createPath(tmpPath);
                }
                File tmpFile = new File(path = file.getAbsolutePath(),originalFilename);
                try {
                    /*
                     * 此时不可使用相对路径，此时的相对路径已定位在流的缓存位置
                     */
                    multipartFile.transferTo(tmpFile);
                } catch (IOException e) {
                    Util.errorLog.error(errorPre + "上传文件 " + path + originalFilename + "出现IO异常！" + e.getMessage());
                    throw new IoException("上传文件 " + path + originalFilename + "出现IO异常！" + e.getMessage());
                }
            }
        }
        return "上传文件成功！";
    }

    /**
     * 下载文件
     * @param response response
     * @param path 路径
     * @param fileName 文件名称
     */
    @Override
    public void download(HttpServletResponse response, String path, String fileName) {
        String errorPre = "下载文件：";
        Util.processPath3(path,errorPre);
        fileName = Util.processFileName(fileName,errorPre);
        File file = new File(path,fileName);
        if (!file.exists()){
            Util.errorLog.error(errorPre + path + fileName + " 文件（目录）不存在！");
            throw new NotFoundException(path + fileName + " 文件（目录）不存在！");
        }
        if (file.isDirectory()){
            /*
             * 先压缩再下载
             */
            file = Util.createZip(file.getAbsolutePath(), file.getAbsolutePath() + ".zip");
        }
        Util.download(file.getAbsolutePath(),response);
    }

    /**
     * 获取系统信息
     * @param request request
     * @return SystemInfo
     */
    @Override
    public SystemInfo env(HttpServletRequest request) {
        String errorPre = "获取系统信息：";
        String serverIp = null;
        try {
            serverIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            Util.errorLog.error(errorPre + "无法获取服务器端的ip！");
        }
        Map<String, String> env = System.getenv();
        return SystemInfo
                .builder()
                .osName(System.getProperty("os.name"))
                .osVersion(System.getProperty("os.version"))
                .osEncoding(System.getProperty("sun.jnu.encoding"))
                .computerName(env.get("COMPUTERNAME"))
                .availableProcessors(Runtime.getRuntime().availableProcessors())
                .ip(serverIp)
                .systemDriver(env.get("SystemDrive"))
                .driverInfo(Util.getDriverInfo())
                .userCountry(System.getProperty("user.country"))
                .userTimezone(System.getProperty("user.timezone"))
                .currentDir(System.getProperty("user.dir"))
                .userHome(System.getProperty("user.home"))
                .userDomain(env.get("USERDOMAIN"))
                .userName(env.get("USERNAME"))
                .userDnsDomain(env.get("USERDNSDOMAIN"))
                .publicDir(env.get("ALLUSERSPROFILE"))
                .temp(env.get("TEMP"))
                .fileEncoding(System.getProperty("file.encoding"))
                .programFiles(env.get("ProgramFiles"))
                .appData(env.get("APPDATA"))
                .systemRoot(env.get("SystemRoot"))
                .console(env.get("ComSpec"))
                .fileExecutable(env.get("PATHEXT"))
                .protocol(request.getProtocol())
                .jdkVersion(System.getProperty("java.version"))
                .jdkHome(System.getProperty("java.home"))
                .jvmVersion(System.getProperty("java.vm.specification.version"))
                .jvmName(System.getProperty("java.vm.name"))
                .classPath(System.getProperty("java.class.path"))
                .javaLibraryPath(System.getProperty("java.library.path"))
                .javaTmpdir(System.getProperty("java.io.tmpdir"))
                .compiler(System.getProperty("java.compiler"))
                .javaExtDirs(System.getProperty("java.ext.dirs"))
                .remoteAddr(Util.getIpAdrress(request))
                .remoteHost(request.getRemoteHost())
                .scheme(request.getScheme())
                .secure(request.isSecure()?"是":"否")
                .build();
    }

    /**
     * 查询文件
     * @param path 目录路径 默认当前目录
     * @param keyWord 查询内容
     * @param subList 文件后缀列表 默认全部
     * @param byName Y：通过文件名称查询   N：通过文件内容查询   A：通过文件名称以及内容查询   默认Y
     * @param ignoreCase 是否忽略大小写 true：忽略大小写   false：不忽略大小写   默认false
     * @return 满足条件的文件绝对路径列表
     */
    @Override
    public List<String> searchFile(String path, String keyWord, List<String> subList, String byName, Boolean ignoreCase) {
        String errorPre = "查询文件：";
        Util.processString(keyWord,errorPre + "content");
        byName = byName == null  ? Util.Y : byName;
        if (!Util.Y.equalsIgnoreCase(byName) && !Util.N.equalsIgnoreCase(byName) && !Util.A.equalsIgnoreCase(byName)){
            Util.errorLog.error(errorPre + "byName属性值有误：" + byName);
            throw new ParameterException("byName属性值有误：" + byName);
        }
        path = Util.processPath3(path,errorPre);
        ignoreCase = ignoreCase == null ? false : ignoreCase;
        List<String> pathList = new ArrayList<>();
        searchFile(pathList,path, keyWord, subList, byName, ignoreCase,errorPre);
        return pathList;
    }

    /**
     * 具体的查询操作
     * @param pathList 文件路径列表（不为null）
     * @param path 目录路径 默认当前目录
     * @param keyWord 查询内容
     * @param subList 文件后缀列表 默认全部
     * @param byName Y：通过文件名称查询   N：通过文件内容查询   A：通过文件名称以及内容查询   默认Y
     * @param ignoreCase 是否忽略大小写 true：忽略大小写   false：不忽略大小写   默认false
     */
    private void searchFile(List<String> pathList,String path, String keyWord, List<String> subList, String byName, Boolean ignoreCase,String errorPre){
        boolean allType = false;
        if (subList == null || subList.size() == 0){
            allType = true;
        }
        File file = new File(path);
        if (!file.exists() || file.isFile()){
            Util.errorLog.error(errorPre + path + "不存在或不是目录！");
            throw new NotFoundException(path + "不存在或不是目录！");
        }
        File[] fileList = file.listFiles();
        if (fileList != null && fileList.length > 0){
            for (File f :fileList
            ) {
                if (f.isDirectory()){
                    searchFile(pathList, f.getAbsolutePath(), keyWord, subList, byName, ignoreCase, errorPre);
                }else {
                    if (!allType && !Util.fileEndWithInSubfixList(f,subList)){
                        continue;
                    }
                    String name = f.getName();
                    String content = null;
                    boolean byNameIsn = Util.N.equalsIgnoreCase(byName);
                    if (byNameIsn || Util.A.equalsIgnoreCase(byName)){
                        if (!f.canRead()){
                            Util.errorLog.error(errorPre + "查询过程中出现异常，无法进行查询：" + f.getAbsolutePath() + " 无读权限！");
                            throw new PermissionException("查询过程中出现异常，无法进行查询：" + f.getAbsolutePath() + " 无读权限！");
                        }
                        content = Util.readFile(f,errorPre);
                        if (byNameIsn){
                            name = null;
                        }
                    }
                    if (Util.stringContainAnotherString(name,keyWord,ignoreCase) ||
                            Util.stringContainAnotherString(content,keyWord,ignoreCase)
                    ){
                        pathList.add(f.getAbsolutePath());
                    }
                }
            }
        }
    }

}
