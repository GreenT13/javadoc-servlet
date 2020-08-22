package com.apon.javadocservlet.repository.impl;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

class WebserviceClientTest {

    private WebserviceClient createWebserviceClient(RestTemplate restTemplate) {
        return new WebserviceClient(() -> restTemplate);
    }

    private <T> HttpEntity<T> createHttpEntity(String mediaType) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Accept", mediaType);
        return new HttpEntity<>(httpHeaders);
    }

    private <T> ResponseEntity<T> createResponseEntity(T body) {
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @Test
    public void testGet() {
        // Given
        String url = "http://localhost:8080/";
        Map<String, String> queryParam = Map.of("key", "value");
        String response = "response";
        HttpEntity<String> httpEntity = createHttpEntity(MediaType.APPLICATION_JSON_VALUE);

        RestTemplate restTemplate = mock(RestTemplate.class);
        doReturn(createResponseEntity(response)).when(restTemplate).exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(String.class));

        // When
        WebserviceClient webserviceClient = createWebserviceClient(restTemplate);
        String actualResponse = webserviceClient.get(url, queryParam, String.class);

        // Then
        assertThat(actualResponse, equalTo(response));
        verify(restTemplate).exchange("http://localhost:8080/?key=value", HttpMethod.GET, httpEntity, String.class);
    }

    @Test
    public void testGetJar() {
        // Given
        String url = "http://localhost:8080/";
        Map<String, String> queryParam = Map.of("key", "value");
        byte[] response = new byte[]{1, 2, 3};
        HttpEntity<String> httpEntity = createHttpEntity("application/java-archive");

        RestTemplate restTemplate = mock(RestTemplate.class);
        doReturn(createResponseEntity(response)).when(restTemplate).exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(byte[].class));

        // When
        WebserviceClient webserviceClient = createWebserviceClient(restTemplate);
        byte[] actualResponse = webserviceClient.getJar(url, queryParam);

        // Then
        assertThat(actualResponse, equalTo(response));
        verify(restTemplate).exchange("http://localhost:8080/?key=value", HttpMethod.GET, httpEntity, byte[].class);
    }
}
