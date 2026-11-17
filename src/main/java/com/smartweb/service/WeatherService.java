package com.smartweb.service;

import com.smartweb.model.WeatherData;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeatherService {

    private final HttpClient client = HttpClient.newHttpClient();

    //public String getWeather(String city)
    public WeatherData getWeather(String city)
            throws IOException, InterruptedException {

        // 1. Encoder le nom de la ville
        String encodedCity = URLEncoder.encode(
                city, StandardCharsets.UTF_8);

        // 2. Rechercher les coordonnées géographiques
        String geoUrl =
                "https://geocoding-api.open-meteo.com/v1/search"
                + "?name=" + encodedCity
                + "&count=1&language=fr&format=json";

        HttpRequest geoRequest = HttpRequest.newBuilder()
                .uri(URI.create(geoUrl))
                .GET()
                .build();

        HttpResponse<String> geoResponse = client.send(
                geoRequest,
                HttpResponse.BodyHandlers.ofString()
        );

        if (geoResponse.statusCode() != 200) {
            throw new IOException("Erreur de géolocalisation.");
        }

        String geoJson = geoResponse.body();
        String cityName = extractText(geoJson, "name");
        String region = extractText(geoJson, "admin1");
        String country = extractText(geoJson, "country");

        // 3. Extraire latitude et longitude
        double latitude = extractCoordinate(geoJson, "latitude");
        double longitude = extractCoordinate(geoJson, "longitude");
        System.out.println("Informations géographiques : " + geoJson);

        // 4. Construire l'URL météo
        String weatherUrl =
                "https://api.open-meteo.com/v1/forecast"
                + "?latitude=" + latitude
                + "&longitude=" + longitude
                + "&current=temperature_2m,relative_humidity_2m,"
                + "apparent_temperature,weather_code,wind_speed_10m";

        HttpRequest weatherRequest = HttpRequest.newBuilder()
                .uri(URI.create(weatherUrl))
                .GET()
                .build();

        // 5. Récupérer les données météo
        HttpResponse<String> weatherResponse = client.send(
                weatherRequest,
                HttpResponse.BodyHandlers.ofString()
        );

        if (weatherResponse.statusCode() != 200) {
            throw new IOException("Erreur de récupération météo.");
        }

       // return weatherResponse.body();
        return new WeatherData(
        	    cityName,
        	    region,
        	    country,
        	    weatherResponse.body()
        	);
    }

    private double extractCoordinate(String json, String coordinate)
            throws IOException {

        Pattern pattern = Pattern.compile(
                "\"" + coordinate + "\"\\s*:\\s*(-?\\d+(?:\\.\\d+)?)"
        );

        Matcher matcher = pattern.matcher(json);

        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1));
        }

        throw new IOException("Coordonnées introuvables.");
    }
    private String extractText(String json, String field)
            throws IOException {

        Pattern pattern = Pattern.compile(
            "\"" + field + "\"\\s*:\\s*\"([^\"]*)\""
        );

        Matcher matcher = pattern.matcher(json);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return "";
    }
}