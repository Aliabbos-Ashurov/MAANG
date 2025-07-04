package com.abbos.maang.core.protocol;


import com.abbos.maang.annotation.Example;
import com.google.errorprone.annotations.DoNotCall;
import com.google.errorprone.annotations.Immutable;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-04-28
 */
@Immutable
@Example("http")
public final class HttpProtocol {

    @Test
    @DoNotCall
    void sample() {
        try (HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build()) {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://jsonplaceholder.typicode.com/users"))
                    .GET()
                    .timeout(Duration.ofSeconds(1))
                    .build();

            HttpResponse<String> response = httpClient
                    .send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            System.out.println(response.statusCode());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
