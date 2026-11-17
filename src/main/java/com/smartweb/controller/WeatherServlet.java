package com.smartweb.controller;

import java.io.IOException;

import com.smartweb.service.WeatherService;
import com.smartweb.model.WeatherData;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/weather")
public class WeatherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private WeatherService weatherService;

    @Override
    public void init() throws ServletException {
        weatherService = new WeatherService();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
                         throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String city = request.getParameter("city");

        if (city == null || city.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(
                "{\"error\":\"Veuillez saisir une ville.\"}"
            );
            return;
        }

        try {
        	
           // String weatherData = weatherService.getWeather(city.trim());

           // response.getWriter().write(weatherData);
        	WeatherData weatherData = weatherService.getWeather(city.trim());

        	String json = "{"
        	        + "\"city\":\"" + weatherData.getCity() + "\","
        	        + "\"region\":\"" + weatherData.getRegion() + "\","
        	        + "\"country\":\"" + weatherData.getCountry() + "\","
        	        + "\"weather\":" + weatherData.getWeatherJson()
        	        + "}";

        	response.getWriter().write(json);

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(
                "{\"error\":\"La requête météo a été interrompue.\"}"
            );

        } catch (IOException e) {

            response.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
            response.getWriter().write(
                "{\"error\":\"Impossible de récupérer les données météo.\"}"
            );

            log("Erreur lors de la récupération météo", e);
        }
    }
}