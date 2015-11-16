package lpadron.me.weatherly.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationListener;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import lpadron.me.weatherly.R;
import lpadron.me.weatherly.weather.Currently;
import lpadron.me.weatherly.weather.Daily;
import lpadron.me.weatherly.weather.Forecast;
import lpadron.me.weatherly.weather.Hourly;

public class MainActivity extends Activity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, LocationListener{

    public static final String TAG = MainActivity.class.getSimpleName();
    private Forecast forecast;
    private GoogleApiClient mGoogleApiClient;
    private Location location;
    private LocationRequest locationRequest;
    private Context context = this;
    private double latitude;
    private double longitude;

    /* Butter knife references */
    @Bind(R.id.tempLabel) TextView tempLabel;
    @Bind(R.id.timeLabel) TextView timeLabel;
    @Bind(R.id.weatherIcon) ImageView iconView;
    @Bind(R.id.locationLabel) TextView locationLabel;
    @Bind(R.id.greetingLabel) TextView greetingLabel;
    @Bind(R.id.humidityValue) TextView humidityLabel;
    @Bind(R.id.percipValue) TextView percipLabel;
    @Bind(R.id.refreshImageView) ImageView refreshImageView;
    @Bind(R.id.progressBar) ProgressBar progressBar;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /* Butter knife creates the variables */
        ButterKnife.bind(this);

        /* Startup location services */
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_LOW_POWER)
                .setSmallestDisplacement(2000) //minimum distance of 5,000 meters before checking
                .setInterval(1 * 10000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        progressBar.setVisibility(View.INVISIBLE);

        refreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForecast();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /* Location related methods */

    @Override
    public void onConnected(Bundle bundle) {
        location = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        LocationServices
                .FusedLocationApi
                .requestLocationUpdates(mGoogleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("SUSPENDING", "CONNECTION SUSPENDED");
    }

    @Override
    public void onLocationChanged(Location location) {
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        getForecast();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        locationLabel.setText("Unable to get location.");
        greetingLabel.setText("Network error");
        reportNetworkError();
    }

    protected void stopLocationUpdates() {
        Log.i("STOPPING" , "STOPPING LOCATION UPDATES");
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    /* Application methods */

    private void getForecast() {
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
                            forecast = parseForecastInfo(jsonData);
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
            refreshImageView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }else {
            progressBar.setVisibility(View.INVISIBLE);
            refreshImageView.setVisibility(View.VISIBLE);
        }
    }
    private Forecast parseForecastInfo(String json) throws JSONException {
        Forecast forecast = new Forecast();

        forecast.setCurrently(getCurrentlyWeather(json));
        forecast.setDailyWeatherList(getDailyWeather(json));
        forecast.setHourlyWeatherList(getHourlyWeather(json));

        return forecast;
    }


    private ArrayList<Daily> getDailyWeather(String json) throws JSONException {
        JSONObject baseData = new JSONObject(json);
        ArrayList<Daily> dailyList = new ArrayList<>();
        String timeZone = baseData.getString("timezone");
        //Get the hourly JSON object
        JSONObject daily = baseData.getJSONObject("daily");
        //Get the data array from the JSON hourly object
        JSONArray dailyData = daily.getJSONArray("data");

        for (int i = 0; i < daily.length(); i++) {
           /* Get a single json object from the json array
            * and get all the required information, save it into an
             * hour object, then save that object into the list*/
            JSONObject jsonObj = dailyData.getJSONObject(i);
            Daily day = new Daily();

            day.setIcon(jsonObj.getString("icon"));
            day.setSummary(jsonObj.getString("summary"));
            day.setTime(jsonObj.getLong("time"));
            day.setTempHigh(jsonObj.getDouble("temperatureMax"));
            day.setTempLow(jsonObj.getDouble("temperatureMin"));
            day.setTimeZone(timeZone);

            dailyList.add(day);
        }
        return dailyList;
    }

    private ArrayList<Hourly> getHourlyWeather(String json) throws JSONException {
        ArrayList<Hourly> hourlyList = new ArrayList<>();
        JSONObject baseData = new JSONObject(json);
        //Get timezon from JSON
        String timeZone = baseData.getString("timezone");
        //Get the hourly JSON object
        JSONObject hourly = baseData.getJSONObject("hourly");
        //Get the data array from the JSON hourly object
        JSONArray hourlyData = hourly.getJSONArray("data");

        for (int i = 0; i < hourly.length(); i++) {
           /* Get a single json object from the json array
            * and get all the required information, save it into an
             * hour object, then save that object into the list*/
            JSONObject jsonObj = hourlyData.getJSONObject(i);
            Hourly hour = new Hourly();

            hour.setIcon(jsonObj.getString("icon"));
            hour.setSummary(jsonObj.getString("summary"));
            hour.setTime(jsonObj.getLong("time"));
            hour.setTemp(jsonObj.getDouble("temperature"));
            hour.setTimeZone(timeZone);

            hourlyList.add(hour);
        }
        return hourlyList;
    }

    private Currently getCurrentlyWeather(String json) throws JSONException {
        JSONObject baseData = new JSONObject(json);
        String timeZone = baseData.getString("timezone");

        /* Get Current Weather data and create Weather object with data */
        JSONObject currentData = baseData.getJSONObject("currently");

        Currently currently = new Currently();

        currently.setHumidity(currentData.getDouble("humidity"));
        currently.setIcon(currentData.getString("icon"));
        currently.setPercip(currentData.getDouble("precipProbability"));
        currently.setSummary(currentData.getString("summary"));
        currently.setTime(currentData.getLong("time"));
        currently.setTemp(currentData.getDouble("temperature"));
        currently.setTimeZone(timeZone);

        return currently;
    }

    private void updateData() {
        Currently currently = forecast.getCurrently();
        /* Set the background color */
        RelativeLayout screen = (RelativeLayout) findViewById(R.id.screen);
        ScreenColor screenColor = new ScreenColor(currently.getTime(), currently.getTimeZone());
        screen.setBackgroundColor(getResources().getColor(screenColor.getCorrectColor()));
        /* Set the temperature and time labels */
        tempLabel.setText(currently.getTemp() + "");
        timeLabel.setText(currently.getFormattedTime());
        /* Get/set the correct greeting based on time of day */
        Greeting greeting = new Greeting(currently.getTime(), currently.getTimeZone(),
                currently.getTemp(), currently.getPercip());
        greetingLabel.setText(greeting.getCorrectGreeting());
        /* Set the current humidity and percipitation labels */
        humidityLabel.setText(currently.getHumidity() + "%");
        percipLabel.setText(currently.getPercip() + "%");
        /* Get and set the correct weather icon */
        Drawable drawable = getResources().getDrawable(currently.getIconId());
        iconView.setImageDrawable(drawable);
        /* Get the users city name */
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.size() > 0)
            if (addresses.get(0).getLocality() != null && addresses.get(0).getAdminArea() == null) {
                //In case we cant find the users state/country, this happens a bit in places outside
                //the US & Canada
                //We will only display the city if we have it.
                locationLabel.setText(addresses.get(0).getLocality());
            } else if (addresses.get(0).getLocality() != null && addresses.get(0).getAdminArea() != null) {
                //If we have both the city and state/country
                //display both to the user
                locationLabel.setText(addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea());
            } else if (addresses.get(0).getLocality() == null && addresses.get(0).getAdminArea() != null) {
                //If we dont have the city but we have the country/state
                locationLabel.setText(addresses.get(0).getAdminArea());
            }
            else {
                //In case we cant find the users city and country?
                //We leave the location label blank.
                locationLabel.setText("");
            }
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
}