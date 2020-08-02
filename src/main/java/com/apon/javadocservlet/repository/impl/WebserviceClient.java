package com.apon.javadocservlet.repository.impl;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.Map;

/**
 * Class for executing webservice calls.
 */
public class WebserviceClient {

    /**
     * Creates a URI from the supplied parameters. The URI will not be encoded, since {@link RestTemplate} functions
     * will encode the URI again. To avoid encoding twice, we do not encode here.
     * @param url         The URL
     * @param queryParams The query parameters
     * @return Unencoded URI
     */
    private String createUriNotEncoded(String url, Map<String, String> queryParams) {
        // Create url with query params.
        final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        queryParams.forEach(builder::queryParam);

        return builder.build(false).toUriString();
    }

    /**
     * Executes a JSON GET request and returns the response object.
     * @param url         The URL
     * @param queryParams The query parameters
     * @param resultClass The class of the response object type
     * @param <T>         The response object type
     * @return Instance of the response object type
     */
    public <T> T get(String url, Map<String, String> queryParams, Class<T> resultClass) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        String uri = createUriNotEncoded(url, queryParams);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<T> response = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(httpHeaders), resultClass);
        return response.getBody();
    }

    /**
     * Returns the byte content of a jar from an URL.
     * @param url         The url
     * @param queryParams The query parameters
     * @return Byte content of the jar
     */
    public byte[] getJar(String url, Map<String, String> queryParams) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Accept", "application/java-archive");

        String uri = createUriNotEncoded(url, queryParams);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<byte[]> response = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(httpHeaders), byte[].class);

        return response.getBody();
    }
}
