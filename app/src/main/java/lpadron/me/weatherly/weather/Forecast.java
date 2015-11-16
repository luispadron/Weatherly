package lpadron.me.weatherly.weather;


import lpadron.me.weatherly.R;

/**
 * Created by luispadron on 11/15/15.
 */
public class Forecast {
    private Currently currently;
    private Daily[] dailyWeather;
    private Hourly[] hourlyWeather;

    public Currently getCurrently() {
        return currently;
    }

    public void setCurrently(Currently currently) {
        this.currently = currently;
    }

    public Daily[] getDailyWeather() {
        return dailyWeather;
    }

    public void setDailyWeather(Daily[] dailyWeather) {
        this.dailyWeather = dailyWeather;
    }

    public Hourly[] getHourlyWeather() {
        return hourlyWeather;
    }

    public void setHourlyWeather(Hourly[] hourlyWeather) {
        this.hourlyWeather = hourlyWeather;
    }

    public static int getIconId(String iconString) {
        int iconId = R.drawable.clear_day;

        /* Set icon to appropriate weather data */
        switch(iconString){
            case "clear-day":
                iconId = R.drawable.clear_day;
                break;
            case "clear-night":
                iconId = R.drawable.clear_night;
                break;
            case "cloudy":
                iconId = R.drawable.cloudy;
                break;
            case "cloudy-night":
                iconId = R.drawable.clear_night;
                break;
            case "fog":
                iconId = R.drawable.fog;
                break;
            case "partly-cloudy":
                iconId = R.drawable.partly_cloudy;
                break;
            case "rain":
                iconId = R.drawable.rain;
                break;
            case "sleet":
                iconId = R.drawable.sleet;
                break;
            case "snow":
                iconId = R.drawable.snow;
                break;
            case "sunny":
                iconId = R.drawable.sunny;
                break;
            case "wind":
                iconId = R.drawable.wind;
                break;
        }
        return iconId;
    }
}
