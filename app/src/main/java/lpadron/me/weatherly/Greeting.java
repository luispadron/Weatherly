package lpadron.me.weatherly;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Greeting {
    private long time;
    private String timeZone;
    private ArrayList<String> greetings = new ArrayList();

    public Greeting(long time, String timeZone){
        this.time = time;
        this.timeZone = timeZone;
        this.greetings.add("Good morning " + "Luis,");
        this.greetings.add("Good afternoon " + "Luis,");
        this.greetings.add("Good evening " + "Luis,");
    }

    public ArrayList<String> getGreetings() {
        return greetings;
    }

    public void setGreetings(ArrayList<String> greetings) {
        this.greetings = greetings;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getCorrectGreeting() {
        Date datetime = new Date (this.time * 1000);
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone(timeZone));
        cal.setTime(datetime);

        int hour = cal.get(Calendar.HOUR_OF_DAY);

        if (hour  >= 5 && hour < 11){
            return greetings.get(0);
        }else if (hour > 11 && hour < 17) {
            return greetings.get(1);
        }else {
            return greetings.get(2);
        }
    }
}
