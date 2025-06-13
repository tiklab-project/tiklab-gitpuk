package io.tiklab.gitpuk.starter;


import io.tiklab.gitpuk.starter.annotation.EnableGitPuk;
import io.tiklab.toolkit.property.PropertyAndYamlSourceFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@PropertySource(value = {"classpath:application.yaml"}, factory = PropertyAndYamlSourceFactory.class)
@EnableGitPuk
@ServletComponentScan("io.tiklab.gitpuk")
public class GitPukApplication {

	public static void main(String[] args) {

		SpringApplication.run(GitPukApplication.class, args);
	}

}
