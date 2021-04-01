package l.w.h.jschpractice.service;

import l.w.h.jschpractice.bean.SystemInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

/**
 * @author lwh
 * @date 2021/3/26 17:28
 **/
public interface FileManagerService {

    /**
     * 获取指定路径下的文件列表
     * @param path 路径
     * @param all true：显示全部  false：显示未隐藏的文件（夹） 默认：false
     * @return 文件列表
     */
    List<File> findFileList(String path,Boolean all);

    /**
     * 创建文件或目录
     * @param path 路径
     * @param fileName 文件、目录名称
     * @param isFile true：创建文件 false：创建目录
     * @return 是否成功
     */
    String createFileOrFolder(String path,String fileName,Boolean isFile);

    /**
     * 重命名
     * @param path 文件或目录所在的路径
     * @param fileName 要进行重命名的文件或目录
     * @param newName 新的名称
     * @return 是否成功
     */
    String rename(String path,String fileName,String newName);

    /**
     * 删除指定路径下的文件或目录
     * @param path 路径
     * @param fileName 文件或目录名称
     * @return 是否成功
     */
    String delete(String path,String fileName);

    /**
     * 打开指定文件（获取指定文件的内容）
     * @param path 路径
     * @param fileName 文件名称
     * @return 文件内容
     */
    String openFile(String path,String fileName);

    /**
     * 修改文件内容
     * @param path 路径
     * @param fileName 文件名称
     * @param content 新的文件内容
     * @return 是否成功
     */
    String updateFile(String path,String fileName,String content);

    /**
     * 上传文件（夹）
     * @param multipartFiles 文件（夹）
     * @return 是否成功
     */
    String upload(String path, MultipartFile[] multipartFiles);

    /**
     * 下载文件
     * @param response response
     * @param path 路径
     * @param fileName 文件名称
     */
    void download(HttpServletResponse response, String path, String fileName);

    /**
     * 获取系统信息
     * @param request request
     * @return SystemInfo
     */
    SystemInfo env(HttpServletRequest request);

    /**
     * 查询文件
     * @param path 目录路径 默认当前目录
     * @param keyWord 查询内容
     * @param subList 文件后缀列表 默认全部
     * @param byName Y：通过文件名称查询   N：通过文件内容查询   A：通过文件名称以及内容查询   默认Y
     * @param ignoreCase 是否忽略大小写
     * @return 满足条件的文件绝对路径列表
     */
    List<String> searchFile(String path,String keyWord,List<String> subList,String byName,Boolean ignoreCase);

}
