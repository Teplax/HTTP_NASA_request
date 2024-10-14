package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.FileOutputStream;
import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static final String NASA_URL="https://api.nasa.gov/planetary/apod?api_key=HWECsXvxvr2qCun7Mb78fonScKFkfJawenJxLugm";
    //создаём экземпляр мапера
    public static ObjectMapper nasa_mapper= new ObjectMapper();
    public static void main(String[] args) throws IOException {
        //получаем ответ сервера и мапим его в объект
        NASAnswer nAnswer = nasa_mapper.readValue(httpRequest(NASA_URL),NASAnswer.class);
        //получаем из объекта строку с адресом изображения
        String hdUrl = nAnswer.getHdurl();
        //создам поток для записи в файл
        try(FileOutputStream fOs = new FileOutputStream(hdUrl.substring(hdUrl.lastIndexOf("/")+1))){
            //записываем полученный от сервера массив байт в файл
            fOs.write(httpRequest(hdUrl));
        }

    }

    //метод, возвращающий массив байт в результате запроса к удалённому сервису.
    // В качестве аргумента принимает URL сервера
    public static byte[] httpRequest (String url) throws IOException {
        //создаём экземпляр http-клиента через билдер с настройками
        try (CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build()) {
            //создаём http-запрос по переданному адресу сервера
            HttpGet request = new HttpGet(url);
            //создаём экземпляр ответа сервера
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                //и возвращаем массив байт
                return response.getEntity().getContent().readAllBytes();
            }
            catch (Exception e){
                //в случае ошибки -выводим её сообщение в консоль и возвращаем пустой массив байт
                System.out.println(e.getMessage());
            }
        }
        return new byte[0];
    }
}