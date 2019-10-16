package com.epam.esm.init;

import com.epam.esm.config.RepositoryConfig;
import com.epam.esm.config.ServiceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    Class[] sources = {Application.class, RepositoryConfig.class, ServiceConfig.class};
    ConfigurableApplicationContext context = SpringApplication.run(sources, args);
    context.getBean(DispatcherServlet.class).setThrowExceptionIfNoHandlerFound(true);
  }
}
