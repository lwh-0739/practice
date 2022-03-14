package l.w.h.jschpractice.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import l.w.h.commonresult.response.BaseResponse;
import l.w.h.jschpractice.bean.SystemInfo;
import l.w.h.jschpractice.config.MvcConfig;
import l.w.h.jschpractice.service.FileManagerService;
import l.w.h.jschpractice.util.Util;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

/**
 * @author lwh
 * @date 2021/3/26 17:25
 **/
@RestController
@RequestMapping(MvcConfig.PATH_PRE + "/file")
public class FileManagerController {

    @Resource
    private FileManagerService fileManagerService;

    @ApiOperation(value = "获取文件列表",notes = "获取文件列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path",value = "文件目录（默认当前目录）",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "all",value = "true：显示全部  false：显示未隐藏的文件（夹） 默认：false",paramType = "query",dataType = "boolean")
    })
    @GetMapping("/list")
    public BaseResponse<List<File>> findFileList(
            @RequestParam(required = false) String path,
            @RequestParam(required = false) Boolean all
    ){
        List<File> fileList = fileManagerService.findFileList(path,all);
        return Util.sendSuccessResponse(fileList);
    }

    @ApiOperation(value = "创建文件或目录",notes = "创建文件或目录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path",value = "路径（默认当前目录）",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "fileName",value = "文件、目录名称（注：不能出现名称相同的文件或目录）",required = true,paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "isFile",value = "true：创建文件 false：创建目录",required = true,paramType = "query",dataType = "boolean")
    })
    @PostMapping("")
    public BaseResponse<String> createFileOrFolder(
            @RequestParam(required = false) String path,
            @RequestParam String fileName,
            @RequestParam Boolean isFile
    ){
        String result = fileManagerService.createFileOrFolder(path, fileName, isFile);
        return Util.sendSuccessResponse(result);
    }

    @ApiOperation(value = "对文件、目录重命名",notes = "对文件、目录重命名")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path",value = "目录、文件所在的目录（默认当前目录）",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "fileName",value = "要进行重命名的文件或目录名称",required = true,paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "newName",value = "新名称（注：不能出现名称相同的文件或目录）",required = true,paramType = "query",dataType = "String")
    })
    @PutMapping("/rename")
    public BaseResponse<String> rename(
            @RequestParam(required = false) String path,
            @RequestParam String fileName,
            @RequestParam String newName
    ){
        String result = fileManagerService.rename(path, fileName, newName);
        return Util.sendSuccessResponse(result);
    }

    @ApiOperation(value = "删除文件或目录",notes = "删除文件或目录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path",value = "目录、文件所在的目录（默认当前目录）",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "fileName",value = "要删除的文件或目录名称",required = true,paramType = "query",dataType = "String")
    })
    @DeleteMapping("")
    public BaseResponse<String> delete(
            @RequestParam(required = false) String path,
            @RequestParam String fileName
    ){
        String result = fileManagerService.delete(path, fileName);
        return Util.sendSuccessResponse(result);
    }

    @ApiOperation(value = "打开指定文件（获取指定文件的内容）",notes = "打开指定文件（获取指定文件的内容）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path",value = "文件所在的目录（默认当前目录）",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "fileName",value = "要查看的文件名称",required = true,paramType = "query",dataType = "String")
    })
    @GetMapping("")
    public BaseResponse<String> openFile(
            @RequestParam(required = false) String path,
            @RequestParam String fileName
    ){
        String content = fileManagerService.openFile(path, fileName);
        return Util.sendSuccessResponse(content);
    }

    @ApiOperation(value = "将内容输入到指定文件（修改文件内容）",notes = "将内容输入到指定文件（修改文件内容）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path",value = "文件所在的目录（默认当前目录）",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "fileName",value = "文件名称",required = true,paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "content",value = "新的文件内容",required = true,paramType = "query",dataType = "String")
    })
    @PutMapping("")
    public BaseResponse<String> updateFile(
            @RequestParam(required = false) String path,
            @RequestParam String fileName,
            @RequestParam String content
    ){
        String result = fileManagerService.updateFile(path, fileName, content);
        return Util.sendSuccessResponse(result);
    }

    @ApiOperation(value = "上传文件（夹）",notes = "上传文件（夹）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path",value = "文件所在的目录（默认当前目录）",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "multipartFiles",value = "文件（夹）",paramType = "form",dataType = "File",allowMultiple = true)
    })
    @PostMapping("/upload")
    public BaseResponse<String> upload(
            @RequestParam(required = false) String path,
            @RequestParam(required = false) MultipartFile[] multipartFiles
    ){
        String result = fileManagerService.upload(path, multipartFiles);
        return Util.sendSuccessResponse(result);
    }

    @ApiOperation(value = "下载文件（目录）",notes = "下载文件（目录）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path",value = "文件所在的目录（默认当前目录）",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "fileName",value = "文件（或目录）名称",required = true,paramType = "query",dataType = "String")
    })
    @PostMapping("/download")
    public void download(
            HttpServletResponse response,
            @RequestParam(required = false) String path,
            @RequestParam String fileName
    ){
        fileManagerService.download(response, path, fileName);
    }

    @ApiOperation(value = "获取系统信息",notes = "获取系统信息")
    @GetMapping("/env")
    public BaseResponse<SystemInfo> env(
            HttpServletRequest request
    ){
        SystemInfo systemInfo = fileManagerService.env(request);
        return Util.sendSuccessResponse(systemInfo);
    }
    
    @ApiOperation(value = "查询文件",notes = "查询文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path",value = "查询路径（默认当前目录）",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "keyWord",value = "查询内容",required = true,paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "subList",value = "查询文件的后缀（格式：1,1,1） 默认所有类型的文件",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "byName",value = "Y：通过文件名称查询   N：通过文件内容查询   A：通过文件名称以及内容查询   默认Y",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "ignoreCase",value = "true：忽略大小写   false：不忽略大小写   默认false",paramType = "query",dataType = "boolean")
    })
    @GetMapping("/search")
    public BaseResponse<List<String>> searchFile(
            @RequestParam(required = false) String path,
            @RequestParam String keyWord,
            @RequestParam(required = false) List<String> subList,
            @RequestParam(required = false) String byName,
            @RequestParam(required = false) Boolean ignoreCase
    ){
        List<String> pathList = fileManagerService.searchFile(path, keyWord, subList, byName, ignoreCase);
        return Util.sendSuccessResponse(pathList);
    }
    
}
