package lpadron.me.weatherly.weather;

/**
 * Created by luispadron on 11/15/15.
 */
public class Daily {

    private long time;
    private Double tempHigh;
    private Double tempLow;
    private String icon;
    private String summary;
    private String timeZone;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Double getTempHigh() {
        return tempHigh;
    }

    public void setTempHigh(Double temp) {
        this.tempHigh = temp;
    }

    public Double getTempLow() {
        return tempLow;
    }

    public void setTempLow(Double tempLow) {
        this.tempLow = tempLow;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}
