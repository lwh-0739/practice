package l.w.h.jschpractice.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lwh
 * @date 2021/3/30 10:44
 **/
@Getter
@Setter
@Builder
@ApiModel(value = "系统信息",description = "系统信息")
public class SystemInfo {

    /*******服务端系统信息*******/
    @ApiModelProperty(name = "osName",value = "系统名称")
    private String osName;

    @ApiModelProperty(name = "osVersion",value = "系统版本")
    private String osVersion;

    @ApiModelProperty(name = "osEncoding",value = "系统编码")
    private String osEncoding;

    @ApiModelProperty(name = "computerName",value = "计算机名称")
    private String computerName;

    @ApiModelProperty(name = "availableProcessors",value = "CPU核心数")
    private int availableProcessors;

    @ApiModelProperty(name = "ip",value = "IP")
    private String ip;

    @ApiModelProperty(name = "systemDriver",value = "系统盘符")
    private String systemDriver;

    @ApiModelProperty(name = "driverInfo",value = "磁盘信息")
    private String driverInfo;

    @ApiModelProperty(name = "userCountry",value = "国家")
    private String userCountry;

    @ApiModelProperty(name = "userTimezone",value = "时区")
    private String userTimezone;

    @ApiModelProperty(name = "currentDir",value = "当前目录")
    private String currentDir;

    @ApiModelProperty(name = "userHome",value = "用户目录")
    private String userHome;

    @ApiModelProperty(name = "userDomain",value = "账户的域名称")
    private String userDomain;

    @ApiModelProperty(name = "userName",value = "登录用户名")
    private String userName;

    @ApiModelProperty(name = "userDnsDomain",value = "用户域")
    private String userDnsDomain;

    @ApiModelProperty(name = "publicDir",value = "用户公共目录")
    private String publicDir;

    @ApiModelProperty(name = "temp",value = "用户临时目录")
    private String temp;

    @ApiModelProperty(name = "fileEncoding",value = "文件系统编码")
    private String fileEncoding;

    @ApiModelProperty(name = "programFiles",value = "默认程序目录")
    private String programFiles;

    @ApiModelProperty(name = "appData",value = "应用程序数据目录")
    private String appData;

    @ApiModelProperty(name = "systemRoot",value = "系统启动目录")
    private String systemRoot;

    @ApiModelProperty(name = "console",value = "控制台")
    private String console;

    @ApiModelProperty(name = "fileExecutable",value = "可执行后缀")
    private String fileExecutable;

    @ApiModelProperty(name = "protocol",value = "网络协议")
    private String protocol;

    /*******JDK*******/
    @ApiModelProperty(name = "jdkVersion",value = "JDK版本")
    private String jdkVersion;

    @ApiModelProperty(name = "jdkHome",value = "JDK环境目录")
    private String jdkHome;

    @ApiModelProperty(name = "jvmVersion",value = "JVM版本")
    private String jvmVersion;

    @ApiModelProperty(name = "jvmName",value = "JVM名称")
    private String jvmName;

    @ApiModelProperty(name = "classPath",value = "JAVA类路径")
    private String classPath;

    @ApiModelProperty(name = "javaLibraryPath",value = "JAVA载入库搜索路径")
    private String javaLibraryPath;

    @ApiModelProperty(name = "javaTmpdir",value = "JAVA临时目录")
    private String javaTmpdir;

    @ApiModelProperty(name = "compiler",value = "JIT编译器名")
    private String compiler;

    @ApiModelProperty(name = "javaExtDirs",value = "扩展目录路径")
    private String javaExtDirs;

    /*******客户端*******/
    @ApiModelProperty(name = "remoteAddr",value = "客户端IP")
    private String remoteAddr;

    @ApiModelProperty(name = "remoteHost",value = "客户端host")
    private String remoteHost;

    @ApiModelProperty(name = "scheme",value = "请求方式")
    private String scheme;

    @ApiModelProperty(name = "secure",value = "是否应用安全套接字层")
    private String secure;

}
