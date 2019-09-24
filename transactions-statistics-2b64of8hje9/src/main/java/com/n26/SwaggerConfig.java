/**
 * 
 */
package com.n26;

import static springfox.documentation.builders.RequestHandlerSelectors.withClassAnnotation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.annotations.Api;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * @author neelam
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket api() {
		final ApiInfo apiInfo = new ApiInfoBuilder().title("Repayment plan / Annuity loan")
				.description("Here, calculate a repayment plan for an annuity loan.").build();
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo).select().apis(withClassAnnotation(Api.class))
				.paths(PathSelectors.any()).build();
	}

}
