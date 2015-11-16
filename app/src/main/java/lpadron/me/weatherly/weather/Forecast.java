package lpadron.me.weatherly.weather;

import java.util.ArrayList;

/**
 * Created by luispadron on 11/15/15.
 */
public class Forecast {
    private Currently currently;
    private ArrayList<Daily> dailyWeatherList = new ArrayList();
    private ArrayList<Hourly> hourlyWeatherList = new ArrayList();

    public Currently getCurrently() {
        return currently;
    }

    public void setCurrently(Currently currently) {
        this.currently = currently;
    }

    public ArrayList<Daily> getDailyWeatherList() {
        return dailyWeatherList;
    }

    public void setDailyWeatherList(ArrayList<Daily> dailyWeatherList) {
        this.dailyWeatherList = dailyWeatherList;
    }

    public ArrayList<Hourly> getHourlyWeatherList() {
        return hourlyWeatherList;
    }

    public void setHourlyWeatherList(ArrayList<Hourly> hourlyWeatherList) {
        this.hourlyWeatherList = hourlyWeatherList;
    }
}
