package com.harmonycloud.stuck.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2 implements WebMvcConfigurer {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.harmonycloud.stuck.web"))
            .paths(PathSelectors.any())
            .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("Harmonycloud Stuck Demo")
            .description(getDesc())
            .version("1.0")
            .build();
    }
    
    private String getDesc() {
        String titleSplit = "  \n";
        String nextLine = "\n";

        StringBuilder builder = new StringBuilder();
        builder.append("数据库锁表卡顿").append(titleSplit)
                .append("1. [准备] 数据库锁表 lock tables weather write").append(nextLine)
                .append("2. [测试] 访问/db/query接口").append(nextLine)
                .append("3. [结束] 数据库释放锁 unlock tables").append(nextLine)
                .append(nextLine)
            .append("数据库连接池连接耗尽访问超时(30s)场景").append(titleSplit)
                .append("1. [准备] 访问/db/getConnections接口").append(nextLine)
                .append("2. [测试] 访问/db/query接口").append(nextLine)
                .append("3. [结束] 访问/db/releaseConnections接口").append(nextLine)
                .append(nextLine)
            .append("线程死锁场景 死锁后无法恢复").append(titleSplit)
                .append("1. [准备] 访问/dead/prepare接口").append(nextLine)
                .append("2. [测试] 访问/dead/lock接口").append(nextLine)
                .append("3. [结束] 无法结束，只能重启应用").append(nextLine)
                .append(nextLine)
            .append("CPU飙高(线程死循环导致100%) 场景").append(titleSplit)
                .append("1. [测试] 访问/cpu/hot接口").append(nextLine)
                .append("2. [结束] 访问/cpu/cool接口").append(nextLine)
                .append(nextLine)
            .append("IO阻塞 场景").append(titleSplit)
                .append("1. [测试] 访问/io/block接口").append(nextLine)
                .append("2. [测试] 控制台输入值eg. aaa").append(nextLine)
                .append(nextLine)
            .append("请求长等待 场景").append(titleSplit)
                .append("1. [测试] 访问/sleep/{second}接口").append(nextLine)
                .append(nextLine)
            .append("同步块锁等待 场景").append(titleSplit)
                .append("1. [测试] 访问/sync-lock/{second}接口").append(nextLine)
                .append("2. [测试] 再次访问/sync-lock/{second}接口").append(nextLine)
                .append(nextLine)
            .append("重锁等待 场景").append(titleSplit)
                .append("1. [测试] 访问/reetrant-lock/{second}接口").append(nextLine)
                .append("2. [测试] 再次访问/reetrant-lock/{second}接口").append(nextLine)
                .append(nextLine)
            .append("OOM场景").append(titleSplit)
                .append("1. [测试] 多次调用/oom/heap/{size}接口直到日志输出heap oom").append(nextLine)
                .append("2. [结束] OOM后无法恢复，但此时接口还可以接收请求，但OOM恢复只能重启应用").append(nextLine)
                .append(nextLine)
            .append("Join场景 https://blog.51cto.com/zhangfengzhe/1704416").append(titleSplit)
                .append("1. [测试] 调用/join/exception接口会阻塞接口").append(nextLine)
                .append("2. [结束] 无法结束").append(nextLine)
                .append(nextLine);
        return builder.toString();
    }
}