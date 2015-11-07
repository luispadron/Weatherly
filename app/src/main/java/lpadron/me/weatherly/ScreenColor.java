package lpadron.me.weatherly;

public class ScreenColor {
    private long time;
    private String timeZone;


    ScreenColor(long time, String timeZone) {
        this.time = time;
        this.timeZone = timeZone;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}
