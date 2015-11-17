package lpadron.me.weatherly.weather;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Hourly implements Parcelable {

    private long time;
    private int color;
    private Double latitude;
    private Double longitude;
    private Double temp;
    private String icon;
    private String summary;
    private String timeZone;

    public Hourly() {

    }

    private Hourly(Parcel in) {
        time = in.readLong();
        temp = in.readDouble();
        summary = in.readString();
        icon = in.readString();
        timeZone = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        color = in.readInt();
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getTemp() {
        return (int) Math.round(temp);
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getIconId() {
        return Forecast.getIconId(icon);
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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getHour() {
        SimpleDateFormat formatter = new SimpleDateFormat("h a");
        formatter.setTimeZone(TimeZone.getTimeZone(timeZone));
        Date date = new Date(time * 1000);
        return formatter.format(date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(time);
        dest.writeDouble(temp);
        dest.writeString(summary);
        dest.writeString(icon);
        dest.writeString(timeZone);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeInt(color);
    }

    public static final Creator<Hourly> CREATOR = new Creator<Hourly>() {
        @Override
        public Hourly createFromParcel(Parcel source) {
            return new Hourly(source);
        }

        @Override
        public Hourly[] newArray(int size) {
            return new Hourly[size];
        }
    };
}
