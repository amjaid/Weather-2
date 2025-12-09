package com.example.weather.controller;

import com.example.weather.model.WeatherResult;
import com.example.weather.service.WeatherService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Controller
@Validated
public class WeatherController {
    
    @Autowired
    private WeatherService weatherService;
    
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("popularCities", weatherService.getPopularCities());
        return "index";
    }
    
    @PostMapping("/search")
    public String searchWeather(
            @RequestParam @NotBlank(message = "City name is required") String city,
            Model model) {
        
        try {
            WeatherResult result = weatherService.getWeatherData(city);
            
            // Calculate day/night status
            LocalTime currentTime = result.getCurrentTime().toLocalTime();
            LocalTime sunrise = result.getSunrise().toLocalTime();
            LocalTime sunset = result.getSunset().toLocalTime();
            
            boolean isDaytime = currentTime.isAfter(sunrise) && currentTime.isBefore(sunset);
            String timeOfDay = isDaytime ? "day" : "night";
            String greeting = isDaytime ? "Good Day!" : "Good Evening!";
            
            model.addAttribute("weather", result);
            model.addAttribute("timeOfDay", timeOfDay);
            model.addAttribute("greeting", greeting);
            model.addAttribute("isDaytime", isDaytime);
            model.addAttribute("popularCities", weatherService.getPopularCities());
            
            return "result";
            
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("popularCities", weatherService.getPopularCities());
            return "index";
        }
    }
}