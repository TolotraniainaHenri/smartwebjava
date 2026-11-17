package com.smartweb.model;

public class WeatherData {

    private String city;
    private String region;
    private String country;
    private String weatherJson;

    public WeatherData(String city, String region,
                       String country, String weatherJson) {
        this.city = city;
        this.region = region;
        this.country = country;
        this.weatherJson = weatherJson;
    }

    public String getCity() {
        return city;
    }

    public String getRegion() {
        return region;
    }

    public String getCountry() {
        return country;
    }

    public String getWeatherJson() {
        return weatherJson;
    }
}