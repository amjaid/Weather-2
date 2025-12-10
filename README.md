🛠️ Prerequisites
Java JDK 21 or later

Apache Maven 3.9+

WeatherAPI.com account (free tier: 1M calls/month)

Git (optional, for version control)


Configure Application
Edit src/main/resources/application.yml:

yaml
app:
  weatherapi:
    api-key: "YOUR_API_KEY_HERE"        # ← Replace with your API key
    base-url: http://api.weatherapi.com/v1Get API Key
Visit WeatherAPI.com

Sign up for a free account

Get your API key from the dashboard

Free tier includes 1,000,000 calls/month

