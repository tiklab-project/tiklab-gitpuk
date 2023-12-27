package io.thoughtware.gittok.starter;


import io.thoughtware.gittok.starter.annotation.EnableGitTok;
import io.thoughtware.core.property.PropertyAndYamlSourceFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@PropertySource(value = {"classpath:application.yaml"}, factory = PropertyAndYamlSourceFactory.class)
@EnableGitTok
@ServletComponentScan("io.thoughtware.gittok")
public class GitTokApplication {

	public static void main(String[] args) {

		SpringApplication.run(GitTokApplication.class, args);

	}

}
