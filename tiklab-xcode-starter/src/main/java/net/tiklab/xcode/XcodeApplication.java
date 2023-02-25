package net.tiklab.xcode;


import net.tiklab.utils.property.PropertyAndYamlSourceFactory;
import net.tiklab.xcode.until.RepositoryFileUntil;
import net.tiklab.xcode.until.RepositoryUntil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@PropertySource(value = {"classpath:application.yaml"}, factory = PropertyAndYamlSourceFactory.class)
@EnableScheduling
@EnableXcode
@ServletComponentScan
public class XcodeApplication {

	public static void main(String[] args) {

		SpringApplication.run(XcodeApplication.class, args);

		RepositoryFileUntil.createDirectory(RepositoryUntil.defaultPath());
	}



}
