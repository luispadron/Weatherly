package lpadron.me.weatherly.weather;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import lpadron.me.weatherly.ui.ScreenColor;

public class Daily implements Parcelable{

    private long time;
    private Double latitude;
    private Double longitude;
    private Double tempHigh;
    private Double tempLow;
    private String icon;
    private String summary;
    private String timeZone;

    public Daily() {

    }

    private Daily(Parcel in) {
        time = in.readLong();
        summary = in.readString();
        tempHigh = in.readDouble();
        tempLow = in.readDouble();
        icon = in.readString();
        timeZone = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getTempHigh() {
        return (int) Math.round(tempHigh);
    }

    public void setTempHigh(Double temp) {
        this.tempHigh = temp;
    }

    public int getTempLow() {
        return (int) Math.round(tempLow);
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

    public int getIconId() {
        return Forecast.getIconId(icon);
    }

    public String getDayOfTheWeek() {
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        formatter.setTimeZone(TimeZone.getTimeZone(timeZone));
        Date dateTime = new Date(time * 1000);

        return formatter.format(dateTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(time);
        dest.writeString(summary);
        dest.writeDouble(tempHigh);
        dest.writeDouble(tempLow);
        dest.writeString(icon);
        dest.writeString(timeZone);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    public static final Creator<Daily> CREATOR = new Creator<Daily>() {
        @Override
        public Daily createFromParcel(Parcel source) {
            return new Daily(source);
        }

        @Override
        public Daily[] newArray(int size) {
            return new Daily[size];
        }
    };
}

