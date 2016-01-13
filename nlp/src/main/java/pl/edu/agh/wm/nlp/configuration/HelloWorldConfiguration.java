package pl.edu.agh.wm.nlp.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "pl.edu.agh.wm.nlp")
public class HelloWorldConfiguration {
	

}