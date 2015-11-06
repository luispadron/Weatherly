package lpadron.me.weatherly;

import android.app.FragmentManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    private Weather weather = new Weather();
    private SwipeRefreshLayout swipeRefreshLayout;

    /* Butter knife references */
    @Bind(R.id.tempLabel) TextView tempLabel;
    @Bind(R.id.timeLabel) TextView timeLabel;
    @Bind(R.id.weatherIcon) ImageView iconView;
    @Bind(R.id.locationLabel) TextView locationLabel;
    @Bind(R.id.greetingLabel) TextView greetingLabel;
    @Bind(R.id.humidityValue) TextView humidityLabel;
    @Bind(R.id.percipValue) TextView percipLabel;
    @Bind(R.id.progressBar) ProgressBar progressBar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Butter knife creates the variables */
        ButterKnife.bind(this);
        progressBar.setVisibility(View.INVISIBLE);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(this);
        
        getForecast();
    }

    private void getForecast() {
        String apiKey = "a45f738558f376111212234d47a716f6";
        double latitude = 28.537448;
        double longitude = -81.379026;

        getForecast(latitude,longitude);
    }

    private void getForecast(double latitude, double longitude) {
        String apiKey = "a45f738558f376111212234d47a716f6";
        String finalUrl = "https://api.forecast.io/forecast/" + apiKey + "/" + latitude + ","
                + longitude;

        if (isNetwork()){
            toggleProgressBar();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(finalUrl).build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleProgressBar();
                        }
                    });
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        /* If we can connect and retrieve */
                        if (response.isSuccessful()) {
                            weather = getCurrentWeather(jsonData);
                            /* When user click the refresh button
                             * recheck the forcast.io data for new one
                             * Also runs on start up */
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateData();
                                }
                            });

                        } else {
                            reportHttpError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "EXCEPTION CAUGHT:  ", e);
                    } catch (JSONException e) {
                        Log.e(TAG, "EXCEPTION CAUGHT:  ", e);
                    }
                }
            });
        }else {
            reportNetworkError();
        }
    }

    private void toggleProgressBar() {
        if (progressBar.getVisibility() == View.INVISIBLE){
            progressBar.setVisibility(View.VISIBLE);
        }else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void updateData() {
        tempLabel.setText(weather.getTemp() + "");
        timeLabel.setText(weather.getFormattedTime());
        Greeting greeting = new Greeting(weather.getTime(), weather.getTimeZone());
        greetingLabel.setText(greeting.getCorrectGreeting());
        humidityLabel.setText(weather.getHumidity() + "%");
        percipLabel.setText(weather.getPercip() + "%");
        Drawable drawable = getResources().getDrawable(weather.getIconId());
        iconView.setImageDrawable(drawable);
    }

    private Weather getCurrentWeather(String data) throws JSONException {
        JSONObject baseData = new JSONObject(data);
        String timeZone = baseData.getString("timezone");

        /* Get Current Weather data and create Weather object with data */
        JSONObject currentData = baseData.getJSONObject("currently");

        Weather currentWeather = new Weather();

        currentWeather.setHumidity(currentData.getDouble("humidity"));
        currentWeather.setIcon(currentData.getString("icon"));
        currentWeather.setPercip(currentData.getDouble("precipProbability"));
        currentWeather.setSummary(currentData.getString("summary"));
        currentWeather.setTime(currentData.getLong("time"));
        currentWeather.setTemp(currentData.getDouble("temperature"));
        currentWeather.setTimeZone(timeZone);

        return currentWeather;
    }

    private boolean isNetwork() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }

        return isAvailable;
    }

    private void reportHttpError() {
        ReportHttpErrorFragment error = new ReportHttpErrorFragment();
        error.show(getFragmentManager(), "http error");
    }
    private void reportNetworkError(){
        ReportNetworkErrorFragment error = new ReportNetworkErrorFragment();
        error.show(getFragmentManager(), "network error");
    }

    @Override public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getForecast();
                updateData();
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 500);
    }
}
