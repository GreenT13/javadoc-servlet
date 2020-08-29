package com.apon.javadocservlet;

import com.apon.javadocservlet.controllers.UrlUtil;
import com.apon.javadocservlet.repository.ArtifactStorage;
import com.apon.javadocservlet.repository.impl.mavencentral.MavenCentralRepository;
import com.apon.javadocservlet.repository.impl.WebserviceClient;
import com.apon.javadocservlet.zip.ZipCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.CacheControl;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletContext;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class JavadocServletApplication {
    /** Maximum size of cache for storing processed zip in bytes. Current value: 100MB. */
    public static final long MAX_STORAGE_SIZE_ZIP_FILES_IN_BYTES = 100_000_000;

    /** Maximum number of checksums (32 bytes each) of zips we store in cache. */
    public static final long MAX_NUMBER_OF_ZIP_CHECKSUMS_IN_CACHE = 100_000;

    /** Cache control settings for returned files in the downloaded javadoc artifacts. */
    public static final CacheControl CACHE_CONTROL = CacheControl.maxAge(365, TimeUnit.DAYS);

    public static void main(String[] args) {
        SpringApplication.run(JavadocServletApplication.class, args);
    }

    @Bean
    public ArtifactStorage getArtifactStorage() {
        return new MavenCentralRepository(new WebserviceClient(RestTemplate::new));
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
