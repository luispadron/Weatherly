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

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Double getPercip() {
        return percip;
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
