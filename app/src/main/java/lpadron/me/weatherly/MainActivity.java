package lpadron.me.weatherly;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationListener;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.jar.Manifest;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends Activity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, LocationListener{

    public static final String TAG = MainActivity.class.getSimpleName();
    private Weather weather = new Weather();
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
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
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
    protected void onStart() {
        super.onStart();
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }

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
        stopLocationUpdates();
        if (mGoogleApiClient.isConnected()) {
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
    }

    @Override
    public void onLocationChanged(Location location) {
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        getForecast();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

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
            refreshImageView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }else {
            progressBar.setVisibility(View.INVISIBLE);
            refreshImageView.setVisibility(View.VISIBLE);
        }
    }

    private void updateData() {
        /* Set the background color */
        RelativeLayout screen = (RelativeLayout) findViewById(R.id.screen);
        ScreenColor screenColor = new ScreenColor(weather.getTime(), weather.getTimeZone());
        screen.setBackgroundColor(getResources().getColor(screenColor.getCorrectColor()));
        /* Set the temperature and time labels */
        tempLabel.setText(weather.getTemp() + "");
        timeLabel.setText(weather.getFormattedTime());
        /* Get/set the correct greeting based on time of day */
        Greeting greeting = new Greeting(weather.getTime(), weather.getTimeZone(),
                weather.getTemp(), weather.getPercip());
        greetingLabel.setText(greeting.getCorrectGreeting());
        /* Set the current humidity and percipitation labels */
        humidityLabel.setText(weather.getHumidity() + "%");
        percipLabel.setText(weather.getPercip() + "%");
        /* Get and set the correct weather icon */
        Drawable drawable = getResources().getDrawable(weather.getIconId());
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
}
