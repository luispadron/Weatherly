package lpadron.me.weatherly.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import lpadron.me.weatherly.R;
import lpadron.me.weatherly.adapters.HourlyAdapter;
import lpadron.me.weatherly.weather.Hourly;

public class HourlyForecastActivity extends AppCompatActivity {

    private Hourly[] hours;

    @Bind(R.id.recyclerView) RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_forecast);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.HOURLY_FORECAST);
        hours = Arrays.copyOf(parcelables, parcelables.length, Hourly[].class);

        HourlyAdapter adapter = new HourlyAdapter(this, hours);
        recyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

}
