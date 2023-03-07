package scau.lcimp.lcimps.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfiguration {

    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        //noinspection deprecation
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(
                        new ApiInfoBuilder()
                                .title("直播带货信息管理平台服务端API")
                                .contact("Hastur kiki")
                                .description("直播带货信息管理平台系统的API文档")
                                .termsOfServiceUrl("http://localhost:8080/")
                                .version("1.0")
                                .build()
                )
                .groupName("1.0 版本")    //分组名称
                .select()
                .apis(RequestHandlerSelectors.basePackage("scau.lcimp.lcimps.controller"))  //这里指定Controller扫描包路径
                .paths(PathSelectors.any())
                .build();
    }
}
