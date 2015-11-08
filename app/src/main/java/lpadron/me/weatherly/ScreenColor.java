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
        /* Get the correct date/time */
        Date datetime = new Date (this.time * 1000);
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone(timeZone));
        cal.setTime(datetime);
        /* Figure out the correct background color based on time of day */
        int color = R.color.defaultColor;
        int hour = cal.get(Calendar.HOUR_OF_DAY);

          /* Hour is in military time */
        if (hour == 0){
            color = R.color.earlyEvening;
        } else if (hour >= 1 && hour < 5){
            //late evening is 1 am - 4 am
            color = R.color.lateEvening;
        } else if (hour >= 5 && hour < 9) {
            //early morning from 5 am - 8 am
            color = R.color.earlyMorning;
        }else if (hour >= 9 && hour < 12) {
            //late morning from 9 am - 11 am
            color = R.color.lateMorning;
        }else if (hour >= 12 && hour < 15) {
            //early afternoon from 12 pm - 2 pm
            color = R.color.earlyAfternoon;
        }else if (hour >= 15 && hour < 18) {
            //late afternoon from 3 pm - 5 pm
            color = R.color.lateAfternoon;
        }else {
            //early evening from 6 pm - 11 pm
            color = R.color.earlyEvening;
        }

        return color;
    }
}
