package io.thoughtware.gittork.starter;


import io.thoughtware.gittork.starter.annotation.EnableGitTork;
import io.thoughtware.core.property.PropertyAndYamlSourceFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@PropertySource(value = {"classpath:application.yaml"}, factory = PropertyAndYamlSourceFactory.class)
@EnableGitTork
@ServletComponentScan("io.thoughtware.gittork")
public class GitTorkApplication {

	public static void main(String[] args) {

		SpringApplication.run(GitTorkApplication.class, args);

	}

}
