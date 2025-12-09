package com.example.weather.service;

import com.example.weather.dto.WeatherApiResponse;
import com.example.weather.model.WeatherResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Service
public class WeatherService {
    
    @Value("${app.weatherapi.api-key:demo-key}")
    private String apiKey;
    
    @Value("${app.weatherapi.base-url:http://api.weatherapi.com/v1}")
    private String baseUrl;
    
    @Autowired
    private RestTemplate restTemplate;
    
    public WeatherResult getWeatherData(String city) {
        try {
            // Clean and format the city name
            String formattedCity = formatCityName(city);
            
            WeatherApiResponse response = getWeatherDataFromApi(formattedCity);
            
            if (response == null || response.getLocation() == null) {
                // Try with just the first word if multi-word fails
                if (city.contains(" ")) {
                    String firstWord = city.split(" ")[0];
                    response = getWeatherDataFromApi(firstWord);
                }
                
                if (response == null || response.getLocation() == null) {
                    throw new RuntimeException("City not found. Try 'Kuala Lumpur, Malaysia' format");
                }
            }
            
            return createWeatherResult(response);
            
        } catch (HttpClientErrorException e) {
            // Handle specific HTTP errors
            if (e.getStatusCode().value() == 400) {
                throw new RuntimeException("Invalid city name. Try format: 'City, Country' (e.g., 'Kuala Lumpur, Malaysia')");
            } else if (e.getStatusCode().value() == 401) {
                throw new RuntimeException("Invalid API key. Please check your WeatherAPI key.");
            } else if (e.getStatusCode().value() == 403) {
                throw new RuntimeException("API limit reached or subscription expired.");
            }
            throw new RuntimeException("Error from WeatherAPI: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error fetching weather data: " + e.getMessage());
        }
    }
    
    private String formatCityName(String city) {
        // Remove extra whitespace
        city = city.trim().replaceAll("\\s+", " ");
        
        // Handle common multi-word city patterns
        if (city.toLowerCase().contains("kuala lumpur")) {
            return "Kuala Lumpur";
        } else if (city.toLowerCase().contains("new york")) {
            return "New York";
        } else if (city.toLowerCase().contains("los angeles")) {
            return "Los Angeles";
        } else if (city.toLowerCase().contains("hong kong")) {
            return "Hong Kong";
        } else if (city.toLowerCase().contains("san francisco")) {
            return "San Francisco";
        } else if (city.toLowerCase().contains("rio de janeiro")) {
            return "Rio de Janeiro";
        }
        
        // Try to add country if not specified
        if (!city.contains(",")) {
            // Try to guess country for major cities
            String lowerCity = city.toLowerCase();
            if (lowerCity.contains("london")) {
                return city + ", UK";
            } else if (lowerCity.contains("paris") || lowerCity.contains("berlin") || 
                      lowerCity.contains("rome") || lowerCity.contains("madrid")) {
                return city + ", France";
            } else if (lowerCity.contains("tokyo") || lowerCity.contains("osaka") || 
                      lowerCity.contains("kyoto")) {
                return city + ", Japan";
            } else if (lowerCity.contains("sydney") || lowerCity.contains("melbourne")) {
                return city + ", Australia";
            } else if (lowerCity.contains("mumbai") || lowerCity.contains("delhi") || 
                      lowerCity.contains("bangalore")) {
                return city + ", India";
            }
        }
        
        return city;
    }
    
    private WeatherApiResponse getWeatherDataFromApi(String city) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/forecast.json")
                .queryParam("key", apiKey)
                .queryParam("q", city)
                .queryParam("days", 1)
                .queryParam("aqi", "no")
                .queryParam("alerts", "no")
                .toUriString();
        
        System.out.println("Calling WeatherAPI with: " + url.replace(apiKey, "***"));
        
        ResponseEntity<WeatherApiResponse> response = 
            restTemplate.getForEntity(url, WeatherApiResponse.class);
        
        return response.getBody();
    }
    
    private WeatherResult createWeatherResult(WeatherApiResponse response) {
        WeatherResult result = new WeatherResult();
        result.setCity(response.getLocation().getName());
        result.setCountry(response.getLocation().getCountry());
        result.setState(response.getLocation().getRegion());
        
        // Set sunrise and sunset times
        if (response.getForecast() != null && 
            response.getForecast().getForecastday() != null && 
            response.getForecast().getForecastday().length > 0) {
            
            String sunrise = response.getForecast().getForecastday()[0].getAstro().getSunrise();
            String sunset = response.getForecast().getForecastday()[0].getAstro().getSunset();
            
            result.setSunrise(parseAstroTime(sunrise));
            result.setSunset(parseAstroTime(sunset));
        }
        
        // Set weather information
        if (response.getCurrent() != null) {
            result.setTemperature(response.getCurrent().getTempC());
            result.setFeelsLike(response.getCurrent().getFeelslikeC());
            result.setHumidity(response.getCurrent().getHumidity());
            
            if (response.getCurrent().getCondition() != null) {
                result.setWeatherCondition(response.getCurrent().getCondition().getText());
                result.setWeatherDescription(response.getCurrent().getCondition().getText());
            }
        }
        
        // Parse local time from response
        if (response.getLocation().getLocaltime() != null) {
            try {
                // Parse "2023-12-09 14:30" format
                String[] parts = response.getLocation().getLocaltime().split(" ");
                if (parts.length >= 2) {
                    String timePart = parts[1];
                    LocalTime localTime = LocalTime.parse(timePart);
                    result.setCurrentTime(LocalDateTime.now().with(localTime));
                }
            } catch (Exception e) {
                result.setCurrentTime(LocalDateTime.now());
            }
        } else {
            result.setCurrentTime(LocalDateTime.now());
        }
        
        // Set timezone
        result.setTimezone("Local Time");
        
        return result;
    }
    
    private LocalDateTime parseAstroTime(String astroTime) {
        // Parse times like "06:45 AM" or "06:45"
        try {
            if (astroTime.contains("AM") || astroTime.contains("PM")) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
                LocalTime time = LocalTime.parse(astroTime, formatter);
                return LocalDateTime.now().with(time);
            } else {
                // 24-hour format
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                LocalTime time = LocalTime.parse(astroTime, formatter);
                return LocalDateTime.now().with(time);
            }
        } catch (Exception e) {
            return LocalDateTime.now();
        }
    }
    
    public List<String> getPopularCities() {
        return Arrays.asList(
            "New York, USA", "London, UK", "Tokyo, Japan", "Paris, France", "Sydney, Australia",
            "Mumbai, India", "Dubai, UAE", "Singapore", "Beijing, China", "Moscow, Russia",
            "Cairo, Egypt", "Rio de Janeiro, Brazil", "Toronto, Canada", "Berlin, Germany", "Rome, Italy",
            "Kuala Lumpur, Malaysia", "Bangkok, Thailand", "Seoul, South Korea"
        );
    }
}