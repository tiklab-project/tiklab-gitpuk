package io.tiklab.xcode;


import io.tiklab.core.property.PropertyAndYamlSourceFactory;
import io.tiklab.xcode.starter.annotation.EnableXcode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@PropertySource(value = {"classpath:application.yaml"}, factory = PropertyAndYamlSourceFactory.class)
@EnableXcode
@ServletComponentScan
public class XcodeApplication {

	public static void main(String[] args) {

		SpringApplication.run(XcodeApplication.class, args);

	}



}
