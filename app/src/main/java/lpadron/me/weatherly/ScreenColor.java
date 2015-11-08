package lpadron.me.weatherly;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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

    public int getCorrectColor() {
        Date datetime = new Date (this.time * 1000);
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone(timeZone));
        cal.setTime(datetime);

        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int result = R.color.defaultColor;

        return result;
    }
}
