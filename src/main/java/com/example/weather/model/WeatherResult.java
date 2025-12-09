package com.example.weather.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class WeatherResult {
    private String city;
    private String country;
    private String state;
    private LocalDateTime sunrise;
    private LocalDateTime sunset;
    private String weatherCondition;
    private String weatherDescription;
    private Double temperature;
    private Double feelsLike;
    private Integer humidity;
    private String timezone;
    private LocalDateTime currentTime;
    
    public WeatherResult() {}
    
    public WeatherResult(String city, String country, Long sunriseTimestamp, Long sunsetTimestamp, 
                        String weatherCondition, String weatherDescription, Double temperature, 
                        Double feelsLike, Integer humidity, Integer timezoneOffset) {
        this.city = city;
        this.country = country;
        
        // Convert Unix timestamps to LocalDateTime using timezone offset
        this.sunrise = convertTimestamp(sunriseTimestamp, timezoneOffset);
        this.sunset = convertTimestamp(sunsetTimestamp, timezoneOffset);
        
        this.weatherCondition = weatherCondition;
        this.weatherDescription = weatherDescription;
        this.temperature = temperature;
        this.feelsLike = feelsLike;
        this.humidity = humidity;
        this.timezone = formatTimezone(timezoneOffset);
        this.currentTime = LocalDateTime.now(ZoneId.of(this.timezone));
    }
    
    private LocalDateTime convertTimestamp(Long timestamp, Integer timezoneOffset) {
        // Convert Unix timestamp to LocalDateTime considering timezone offset
        return LocalDateTime.ofInstant(
            Instant.ofEpochSecond(timestamp + timezoneOffset),
            ZoneId.of("UTC")
        );
    }
    
    private String formatTimezone(Integer timezoneOffset) {
        int hours = timezoneOffset / 3600;
        int minutes = Math.abs((timezoneOffset % 3600) / 60);
        return String.format("UTC%+03d:%02d", hours, minutes);
    }
    
    public String getFormattedSunrise() {
        return formatTime(sunrise);
    }
    
    public String getFormattedSunset() {
        return formatTime(sunset);
    }
    
    public String getFormattedCurrentTime() {
        return formatTime(currentTime);
    }
    
    private String formatTime(LocalDateTime time) {
        if (time == null) return "N/A";
        return time.format(DateTimeFormatter.ofPattern("hh:mm:ss a"));
    }
    
    public String getTemperatureCelsius() {
        return String.format("%.1f°C", temperature);
    }
    
    public String getFeelsLikeCelsius() {
        return String.format("%.1f°C", feelsLike);
    }
    
    public String getTemperatureFahrenheit() {
        return String.format("%.1f°F", (temperature) * 9/5 + 32);
    }
    
    public String getFeelsLikeFahrenheit() {
        return String.format("%.1f°F", (feelsLike) * 9/5 + 32);
    }
    
    // Getters and Setters
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    
    public LocalDateTime getSunrise() { return sunrise; }
    public void setSunrise(LocalDateTime sunrise) { this.sunrise = sunrise; }
    
    public LocalDateTime getSunset() { return sunset; }
    public void setSunset(LocalDateTime sunset) { this.sunset = sunset; }
    
    public String getWeatherCondition() { return weatherCondition; }
    public void setWeatherCondition(String weatherCondition) { this.weatherCondition = weatherCondition; }
    
    public String getWeatherDescription() { return weatherDescription; }
    public void setWeatherDescription(String weatherDescription) { this.weatherDescription = weatherDescription; }
    
    public Double getTemperature() { return temperature; }
    public void setTemperature(Double temperature) { this.temperature = temperature; }
    
    public Double getFeelsLike() { return feelsLike; }
    public void setFeelsLike(Double feelsLike) { this.feelsLike = feelsLike; }
    
    public Integer getHumidity() { return humidity; }
    public void setHumidity(Integer humidity) { this.humidity = humidity; }
    
    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
    
    public LocalDateTime getCurrentTime() { return currentTime; }
    public void setCurrentTime(LocalDateTime currentTime) { this.currentTime = currentTime; }
}