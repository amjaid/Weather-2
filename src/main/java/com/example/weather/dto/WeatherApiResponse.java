package com.example.weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherApiResponse {
    
    @JsonProperty("location")
    private Location location;
    
    @JsonProperty("current")
    private Current current;
    
    @JsonProperty("forecast")
    private Forecast forecast;
    
    // Getters and Setters
    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }
    
    public Current getCurrent() { return current; }
    public void setCurrent(Current current) { this.current = current; }
    
    public Forecast getForecast() { return forecast; }
    public void setForecast(Forecast forecast) { this.forecast = forecast; }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Location {
        @JsonProperty("name")
        private String name;
        
        @JsonProperty("region")
        private String region;
        
        @JsonProperty("country")
        private String country;
        
        @JsonProperty("lat")
        private Double lat;
        
        @JsonProperty("lon")
        private Double lon;
        
        @JsonProperty("localtime")
        private String localtime;
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getRegion() { return region; }
        public void setRegion(String region) { this.region = region; }
        
        public String getCountry() { return country; }
        public void setCountry(String country) { this.country = country; }
        
        public Double getLat() { return lat; }
        public void setLat(Double lat) { this.lat = lat; }
        
        public Double getLon() { return lon; }
        public void setLon(Double lon) { this.lon = lon; }
        
        public String getLocaltime() { return localtime; }
        public void setLocaltime(String localtime) { this.localtime = localtime; }
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Current {
        @JsonProperty("temp_c")
        private Double tempC;
        
        @JsonProperty("temp_f")
        private Double tempF;
        
        @JsonProperty("condition")
        private Condition condition;
        
        @JsonProperty("humidity")
        private Integer humidity;
        
        @JsonProperty("feelslike_c")
        private Double feelslikeC;
        
        public Double getTempC() { return tempC; }
        public void setTempC(Double tempC) { this.tempC = tempC; }
        
        public Double getTempF() { return tempF; }
        public void setTempF(Double tempF) { this.tempF = tempF; }
        
        public Condition getCondition() { return condition; }
        public void setCondition(Condition condition) { this.condition = condition; }
        
        public Integer getHumidity() { return humidity; }
        public void setHumidity(Integer humidity) { this.humidity = humidity; }
        
        public Double getFeelslikeC() { return feelslikeC; }
        public void setFeelslikeC(Double feelslikeC) { this.feelslikeC = feelslikeC; }
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Condition {
        @JsonProperty("text")
        private String text;
        
        @JsonProperty("icon")
        private String icon;
        
        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
        
        public String getIcon() { return icon; }
        public void setIcon(String icon) { this.icon = icon; }
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Forecast {
        @JsonProperty("forecastday")
        private ForecastDay[] forecastday;
        
        public ForecastDay[] getForecastday() { return forecastday; }
        public void setForecastday(ForecastDay[] forecastday) { this.forecastday = forecastday; }
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ForecastDay {
        @JsonProperty("astro")
        private Astro astro;
        
        public Astro getAstro() { return astro; }
        public void setAstro(Astro astro) { this.astro = astro; }
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Astro {
        @JsonProperty("sunrise")
        private String sunrise;
        
        @JsonProperty("sunset")
        private String sunset;
        
        public String getSunrise() { return sunrise; }
        public void setSunrise(String sunrise) { this.sunrise = sunrise; }
        
        public String getSunset() { return sunset; }
        public void setSunset(String sunset) { this.sunset = sunset; }
    }
}