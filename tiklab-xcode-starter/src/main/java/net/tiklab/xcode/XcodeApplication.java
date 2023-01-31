package net.tiklab.xcode;


import net.tiklab.utils.property.PropertyAndYamlSourceFactory;
import net.tiklab.xcode.until.CodeFileUntil;
import net.tiklab.xcode.until.CodeUntil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;
import java.text.FieldPosition;

@SpringBootApplication
@PropertySource(value = {"classpath:application.yaml"}, factory = PropertyAndYamlSourceFactory.class)
@EnableScheduling
@EnableXcode
public class XcodeApplication {

	public static void main(String[] args) {

		SpringApplication.run(XcodeApplication.class, args);

		CodeFileUntil.createDirectory(CodeUntil.defaultPath());
	}

}
