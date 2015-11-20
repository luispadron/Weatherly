package lpadron.me.weatherly.ui;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import lpadron.me.weatherly.R;

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
        /* Get the correct date/time */
        Date datetime = new Date (this.time * 1000);
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone(timeZone));
        cal.setTime(datetime);
        /* Figure out the correct background color based on time of day */
        int color = R.color.defaultColor;
        int hour = cal.get(Calendar.HOUR_OF_DAY);

          /* Hour is in military time
          sets the color variable depending on time of day starts at 5 am -->  */
        switch (hour) {
            case 0:
            case 1:
                color = R.color.evening4;
                break;
            case 2:
            case 3:
                color = R.color.evening3;
                break;
            case 4:
                color = R.color.evening1;
                break;
            case 5:
            case 6:
                color = R.color.morning1;
                break;
            case 7:
            case 8:
                color = R.color.morning2;
                break;
            case 9:
            case 10:
                color = R.color.morning3;
                break;
            case 11:
                color = R.color.morning4;
                break;
            case 12:
            case 13:
                color = R.color.afternoon1;
                break;
            case 14:
                color = R.color.afternoon2;
                break;
            case 15:
                color = R.color.afternoon3;
                break;
            case 16:
            case 17:
                color = R.color.afternoon4;
                break;
            case 18:
            case 19:
                color = R.color.evening1;
                break;
            case 20:
            case 21:
                color = R.color.evening2;
                break;
            case 22:
            case 23:
                color = R.color.evening3;

        }
        return color;
    }
}
