package lpadron.me.weatherly;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Weather {
    private long time;
    private Double temp;
    private Double humidity;
    private Double percip;
    private String icon;
    private String summary;
    private String timeZone;

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getIcon() {
        return icon;
    }

    public int getIconId() {
        int iconId = R.drawable.clear_day;

        /* Set icon to appropriate weather data */
        switch(this.icon){
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

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public long getTime() {
        return time;
    }

    public String getFormattedTime() {
        /* Format the time and return as string
         * Time will be formatted into hours:minutes am/pm */
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone(this.timeZone));
        Date dateFromTime = new Date (this.time * 1000);
        String formattedTime = formatter.format(dateFromTime);

        return formattedTime;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getTemp() {
        /* Round down for purpose of UI */
        return (int) Math.round(temp);
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public int getHumidity() {
        Double humidityPercentage = humidity * 100;
        return (int) Math.round(humidityPercentage);
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public int getPercip() {
        Double precipPercentage = percip * 100;
        return (int) Math.round(precipPercentage);
    }

    public void setPercip(Double percip) {
        this.percip = percip;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
