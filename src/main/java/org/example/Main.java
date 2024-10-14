package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.stream.Stream;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static final String NASA_URL="https://api.nasa.gov/planetary/apod?api_key=HWECsXvxvr2qCun7Mb78fonScKFkfJawenJxLugm";
    public static ObjectMapper nasa_mapper= new ObjectMapper();
    public static void main(String[] args) throws IOException {
        NASAnswer nAnswer = nasa_mapper.readValue(httpRequest(NASA_URL),NASAnswer.class);
        String hdUrl = nAnswer.getHdurl();
        try(FileOutputStream fOs = new FileOutputStream(hdUrl.substring(hdUrl.lastIndexOf("/")+1))){
            fOs.write(httpRequest(hdUrl));
        }

    }

    public static byte[] httpRequest (String url) throws IOException {
        try (CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                return response.getEntity().getContent().readAllBytes();
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return new byte[0];
    }
}