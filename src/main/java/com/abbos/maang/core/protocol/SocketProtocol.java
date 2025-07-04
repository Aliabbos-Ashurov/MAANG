package com.abbos.maang.core.protocol;

import com.abbos.maang.annotation.Tag;
import com.google.errorprone.annotations.DoNotCall;
import org.junit.jupiter.api.Test;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * @author Aliabbos Ashurov
 * @since 2025-06-16
 */
@Tag("socket")
public class SocketProtocol {

    @Test
    @DoNotCall
    void sslSocketFactory() throws IOException {
        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        try (SSLSocket socket = (SSLSocket) factory.createSocket("jsonplaceholder.typicode.com", 443)) {
            socket.startHandshake();

            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Send raw HTTP request
            writer.print("GET /posts HTTP/1.1\r\n");
            writer.print("Host: jsonplaceholder.typicode.com\r\n");
            writer.print("Connection: close\r\n");
            writer.print("\r\n");
            writer.flush();

            // Read response
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
}
