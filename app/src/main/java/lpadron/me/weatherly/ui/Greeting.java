package lpadron.me.weatherly.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Greeting {
    private long time;
    private int temp;
    private int percip;
    private String correctGreeting;
    private String timeZone;
    private ArrayList<String> greetings = new ArrayList();

    public Greeting(long time, String timeZone, int temp, int percip){
        this.time = time;
        this.timeZone = timeZone;
        this.temp = temp;
        this.percip = percip;
        this.greetings.add("Good morning, ");
        this.greetings.add("Good afternoon, ");
        this.greetings.add("Good evening, ");
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
            /* Determine temp for greeting */
            if (temp < 30 ) {
                correctGreeting = addToGreeting(0, 1);
            } else if (temp >= 30 && temp <= 59) {
               correctGreeting = addToGreeting(0, 2);
            }  else if (temp >= 60 && temp <= 85 ) {
                correctGreeting = addToGreeting(0, 3);
            } else if (temp >= 86) {
                correctGreeting = addToGreeting(0, 4);
            }
        }else if (hour > 11 && hour < 17) {
            /* Determine temp for greeting */
            if (temp < 30 ) {
                correctGreeting = addToGreeting(1,1);
            } else if (temp >= 30 && temp <= 59) {
               correctGreeting =  addToGreeting(1,2);
            }  else if (temp >= 60 && temp <= 85 ) {
                correctGreeting = addToGreeting(1,3);
            } else if (temp >= 86) {
                correctGreeting = addToGreeting(1,4);
            }
        }else {
               /* Determine temp for greeting */
            if (temp < 30 ) {
                correctGreeting = addToGreeting(2,1);
            } else if (temp >= 30 && temp <= 59) {
               correctGreeting = addToGreeting(2,2);
            }  else if (temp >= 60 && temp <= 85 ) {
               correctGreeting = addToGreeting(2,3);
            } else if (temp >= 86) {
               correctGreeting = addToGreeting(2,4);
            }
        }
        /* Determine if it's going to rain/snow */
        if (percip > 60) {
            correctGreeting = correctGreeting.concat("\nIt's also looking like it might rain.");
        }
        return correctGreeting;
    }

    private String addToGreeting(int element, int choice) {
        String result = "";
        switch (choice) {
            case 1:
                result =greetings.get(element).concat("it's really cold outside.");
                break;
            case 2:
                result = greetings.get(element).concat("it's a bit chilly outside.");
                break;
            case 3:
                result = greetings.get(element).concat("it's feeling rather nice out.");
                break;
            case 4:
                result = greetings.get(element).concat("it's really hot outside.");
                break;
        }
        return result;
    }
}
