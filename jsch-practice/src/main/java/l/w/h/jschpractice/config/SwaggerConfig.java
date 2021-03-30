package l.w.h.jschpractice.config;

import l.w.h.jschpractice.config.property.SwaggerInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lwh
 * @date 2021/3/30 11:06
 **/
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Resource
    private SwaggerInfo swaggerInfo;

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(swaggerInfo.getTitle())
                .description(swaggerInfo.getDescription())
                .version(swaggerInfo.getVersion())
                .contact(new Contact(swaggerInfo.getContactName(),swaggerInfo.getContactUrl(),swaggerInfo.getContactEmail()))
                .license(swaggerInfo.getLicense())
                .licenseUrl(swaggerInfo.getLicenseUrl())
                .build();
    }

    @Bean
    public Docket api() {
        List<ResponseMessage> responseMessageList = globalResponseMessageList();
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(swaggerInfo.getGroupName())
                // .globalOperationParameters(globalParameterList())
                .apiInfo(this.apiInfo())
                .enable(swaggerInfo.getEnable())
                .useDefaultResponseMessages(false)
//                设置全局状态码
                .globalResponseMessage(RequestMethod.GET, responseMessageList)
                .globalResponseMessage(RequestMethod.POST, responseMessageList)
                .globalResponseMessage(RequestMethod.PUT, responseMessageList)
                .globalResponseMessage(RequestMethod.DELETE, responseMessageList)
                .select()
                .apis(RequestHandlerSelectors.basePackage(swaggerInfo.getBasePackage()))
                .paths(PathSelectors.any())
                .build();
    }

    private List<ResponseMessage> globalResponseMessageList(){
        List<ResponseMessage> responseMessageList = new ArrayList<>();
        responseMessageList.add(new ResponseMessageBuilder().code(200).message("请求成功！").build());
        responseMessageList.add(new ResponseMessageBuilder().code(-3000).message("系统异常").build());
        responseMessageList.add(new ResponseMessageBuilder().code(-3001).message("参数异常").build());
        responseMessageList.add(new ResponseMessageBuilder().code(-3002).message("已存在异常").build());
        responseMessageList.add(new ResponseMessageBuilder().code(-3003).message("IO异常").build());
        responseMessageList.add(new ResponseMessageBuilder().code(-3004).message("不存在异常").build());
        responseMessageList.add(new ResponseMessageBuilder().code(-3005).message("逻辑异常").build());
        responseMessageList.add(new ResponseMessageBuilder().code(-3006).message("权限异常").build());
        return responseMessageList;
    }

}
