package lpadron.me.weatherly.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String day = dailyWeather[position].getDayOfTheWeek();
        String condition = dailyWeather[position].getSummary();
        String highTemp = dailyWeather[position].getTempHigh() + "";
        String lowTemp = dailyWeather[position].getTempLow() + "";

        String message = String.format("%s\n%s\nHigh: %s\nLow: %s", day,condition,highTemp,lowTemp);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

    }
}
