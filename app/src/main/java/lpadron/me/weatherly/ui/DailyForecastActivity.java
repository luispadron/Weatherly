package lpadron.me.weatherly.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.TextView;

import java.util.Arrays;

import lpadron.me.weatherly.R;
import lpadron.me.weatherly.adapters.DailyAdapter;
import lpadron.me.weatherly.weather.Daily;
import lpadron.me.weatherly.weather.UsersLocation;

public class DailyForecastActivity extends ListActivity {

    private Daily[] dailyWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast);
        
        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.DAILY_FORECAST);
        dailyWeather = Arrays.copyOf(parcelables, parcelables.length, Daily[].class);
        /* Set the city/state at bottom of screen */
        TextView dailyLocationLabel = (TextView) findViewById(R.id.dailyLocationLabel);
        dailyLocationLabel.setText(
                UsersLocation.getUsersLocation(
                        dailyWeather[0].getLatitude(),
                        dailyWeather[0].getLongitude(),
                        this));

        DailyAdapter adapter = new DailyAdapter(this, dailyWeather);
        setListAdapter(adapter);
    }

}
