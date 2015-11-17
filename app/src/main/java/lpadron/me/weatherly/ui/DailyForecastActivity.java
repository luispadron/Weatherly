package lpadron.me.weatherly.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.RelativeLayout;

import java.util.Arrays;

import lpadron.me.weatherly.R;
import lpadron.me.weatherly.adapters.DayAdapter;
import lpadron.me.weatherly.weather.Daily;

public class DailyForecastActivity extends ListActivity {

    private Daily[] dailyWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast);
        
        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.DAILY_FORECAST);
        dailyWeather = Arrays.copyOf(parcelables, parcelables.length, Daily[].class);

        DayAdapter adapter = new DayAdapter(this, dailyWeather);
        setListAdapter(adapter);
    }

}
