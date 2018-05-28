package com.antonysohal.wb;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTest {

    @Autowired
    private WebTestClient webClient;

    @Test
    public void test_walthamstow() {
        webClient.get().uri(uriBuilder ->  uriBuilder.path("/venues/searchAndRecommend")
                        .queryParam("search", "walthamstow")
                        .build()).accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.locationName").exists()
                .jsonPath("$.venues[*]").isArray();
    }


    @Test
    public void test_non_existing_place() {
        webClient.get().uri(uriBuilder ->  uriBuilder.path("/venues/searchAndRecommend")
                .queryParam("search", "dkfjdf")
                .build()).accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().is5xxServerError();
    }

}