//Designed and Lisenced by Gurubalan.H, thoroughly for educational purposes.
package a1.latitudeandlongitude;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import a1.test.R;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_LOCATION_SETTINGS_REQUEST_CODE = 33;
    private static final int REQUEST_PERMISSIONS_LAST_LOCATION_REQUEST_CODE = 34;
    private static final int REQUEST_PERMISSIONS_CURRENT_LOCATION_REQUEST_CODE = 35;
    private static final int REQUEST_LOCATION = 1;
    private FusedLocationProviderClient mFusedLocationClient;
    protected Location mLastLocation;
    private TextView AccuracyTextView;
    private TextView LatitudeTextView;
    private TextView LongitudeTextView;
    private TextView AltitudeTextView;
    private TextView SpeedTextView;
    private TextView BearingTextView;
    private TextView DateTextView;
    private TextView TimeTextView24;
    private TextView TimeTextView12;
    private TextView LastLatitudeTextView;
    private TextView LastLongitudeTextView;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private SimpleDateFormat simpleDateFormat1;
    private LocationManager locationManager;
    private String Date;
    private String Time24;
    private String Time12;
    private float s=34;
    LocationRequest locationRequest;
    Location currentLocation = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        LatitudeTextView = (TextView) findViewById((R.id.LatitudeText));
        LongitudeTextView=(TextView)findViewById(R.id.LongitudeText);
        AltitudeTextView =(TextView)findViewById(R.id.AltitudeText);
        AccuracyTextView=(TextView)findViewById(R.id.AccuracyTextView);
        SpeedTextView=(TextView)findViewById(R.id.SpeedTextView);
        BearingTextView=(TextView)findViewById(R.id.BearingTextView);
        LastLatitudeTextView=(TextView)findViewById(R.id.LastLatitudeText);
        LastLongitudeTextView=(TextView)findViewById(R.id.LastLongitudeText);
        DateTextView=(TextView)findViewById(R.id.DateText);
        TimeTextView24 =(TextView)findViewById(R.id.TimeText24);
        TimeTextView12=(TextView)findViewById(R.id.TimeText12);
        calendar=Calendar.getInstance();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        checkForLocationRequest();
        checkForLocationSettings();
        getDateTime();
        callCurrentLocation();
        callLastKnownLocation();
        getAltitude();
        getAccuracy();
        getSpeed();
        getBearing();
    }
    @Override
    protected void onResume() {
        super.onResume();
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int result = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS && result != ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED) {
            showSnackbar("Are you running in Emulator? Try a real device.");
        }
    }
    @Override
    public void onStart() {
        super.onStart();
    }
    public void callLastKnownLocation() {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    )
            {
                requestPermissions(REQUEST_PERMISSIONS_LAST_LOCATION_REQUEST_CODE);
                return;
            }
            getLastLocation();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void getDateTime()
    {
        Date= DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        Time24=R.string.TwentyFourHour+DateFormat.getTimeInstance(DateFormat.FULL).format(calendar.getTime());
        Time12=R.string.TwelveHour+DateFormat.getTimeInstance().format(calendar.getTime());
        DateTextView.setText(Date);
        TimeTextView24.setText(Time24);
        TimeTextView12.setText(Time12);

    }
    public void getSpeed()
    {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        }
        else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                if (location.hasSpeed())
                    location.setSpeed(s);
                    String speed="Speed: "+location.getSpeed();
                    SpeedTextView.setText(speed);
                } else {
                    SpeedTextView.setText(R.string.ServiceError);
                }

            }
        }
    public void getBearing()
    {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        }
        else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                if (location.hasBearing()) {
                    String Bearing = "Bearing: " + location.getBearing();
                    BearingTextView.setText(Bearing);
                } else {
                    BearingTextView.setText(R.string.ServiceError);
                }

            }
        }
    }
    public void getAccuracy()
    {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        }
        else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                if (location.hasAccuracy()) {
                    String Accuracy = "Accuracy: " + location.getAccuracy();
                    AccuracyTextView.setText(Accuracy);
                } else {
                    AccuracyTextView.setText(R.string.ServiceError);
                }

            }
        }
    }
    public void getAltitude()
    {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        }
        else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                if (location.hasAltitude()) {
                    String Altitude = "Altitude: " + location.getAltitude();
                    AltitudeTextView.setText(Altitude);
                } else {
                    AltitudeTextView.setText(R.string.ServiceError);
                }

            }
        }
    }
    public void callCurrentLocation() {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    )
            {
                requestPermissions(REQUEST_PERMISSIONS_CURRENT_LOCATION_REQUEST_CODE);
                return;
            }
            mFusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    currentLocation=(Location) locationResult.getLastLocation();
                    String Latitude="Latitude:" + currentLocation.getLatitude();
                    String Longitude="Longitude:" + currentLocation.getLongitude();
                    LatitudeTextView.setText(Latitude);
                    LongitudeTextView.setText(Longitude);
                }
            }, Looper.myLooper());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();
                            String Latitude="Latitude is " + mLastLocation.getLatitude();
                            String Longitude="Longitude is " + mLastLocation.getLongitude();
                            LastLatitudeTextView.setText(Latitude);
                            LastLongitudeTextView.setText(Longitude);
                        } else {
                            showSnackbar("No Last known location found. Try current location.");
                        }
                    }
                });
    }
    private void showSnackbar(final String text) {
        View container = findViewById(R.id.container);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
    }
    private void startLocationPermissionRequest(int requestCode) {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, requestCode);
    }
    private void requestPermissions(final int requestCode) {
        boolean shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (shouldProvideRationale) {
            startLocationPermissionRequest(requestCode);
        } else {
            startLocationPermissionRequest(requestCode);
        }}
    public void checkForLocationRequest(){
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(100);
        locationRequest.setFastestInterval(100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }
    public void checkForLocationSettings() {
        try {
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
            builder.addLocationRequest(locationRequest);
            SettingsClient settingsClient = LocationServices.getSettingsClient(MainActivity.this);
            settingsClient.checkLocationSettings(builder.build())
                    .addOnSuccessListener(MainActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
                        @Override
                        public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        }
                    })
                    .addOnFailureListener(MainActivity.this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            int statusCode = ((ApiException) e).getStatusCode();
                            switch (statusCode) {
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    try {
                                        ResolvableApiException rae = (ResolvableApiException) e;
                                        rae.startResolutionForResult(MainActivity.this, REQUEST_PERMISSIONS_LOCATION_SETTINGS_REQUEST_CODE);
                                    } catch (IntentSender.SendIntentException sie) {
                                        sie.printStackTrace();
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    Toast.makeText(MainActivity.this, "This feature isn't available in your device.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS_LAST_LOCATION_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
        if (requestCode == REQUEST_PERMISSIONS_CURRENT_LOCATION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callCurrentLocation();
            }
        }
    }


}
