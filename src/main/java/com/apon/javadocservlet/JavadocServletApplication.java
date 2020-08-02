package com.apon.javadocservlet;

import com.apon.javadocservlet.controllers.UrlUtil;
import com.apon.javadocservlet.repository.ArtifactStorage;
import com.apon.javadocservlet.repository.impl.mavencentral.MavenCentralRepository;
import com.apon.javadocservlet.client.WebserviceClient;
import com.apon.javadocservlet.zip.ZipCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.servlet.ServletContext;

@SpringBootApplication
public class JavadocServletApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavadocServletApplication.class, args);
    }

    @Bean
    public ArtifactStorage getArtifactStorage() {
        return new MavenCentralRepository(new WebserviceClient());
    }

    @Bean
    public ZipCache getZipCache(ArtifactStorage artifactStorage) {
        return new ZipCache(artifactStorage);
    }

    @Bean
    // Name without get is more logical in Thymeleaf code.
    public UrlUtil urlUtil(ServletContext servletContext) {
        return new UrlUtil(servletContext);
    }
}
