package com.ephraim.me.dublinbikeadvanced;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TrackingActivity extends AppCompatActivity {

    private Button button, button2;
    private TextView textView;
    private LocationListener locationListener;
    private LocationManager locationManager;

    ArrayList<Double> pLat = new ArrayList<>();
    ArrayList<Double> pLng = new ArrayList<>();
    public static ArrayList<String> pos = new ArrayList<>();

    Button btnLoc, btnStopLoc;
    Location location = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLoc = (Button) findViewById(R.id.btnGetLoc);
        btnStopLoc = (Button) findViewById(R.id.btnStopLoc);
        btnStopLoc.setVisibility(btnStopLoc.INVISIBLE);




        textView = (TextView) findViewById(R.id.textView);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                textView.append("\n " + location.getLatitude() +" " + location.getLongitude());
                double lat = location.getLatitude();
                double lng = location.getLongitude();
                pos.add(lat + " " + lng);
                System.out.println(pos);



            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);

            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 10);
            return;
        } else {
            start();
            stop();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    start();
                stop();
                return;
        }
    }

    private void start() {
        btnLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos.clear();
                //locationManager.requestLocationUpdates("gps", 1, 1, locationListener);
                Toast.makeText(TrackingActivity.this, "Getting GPS coordinates", Toast.LENGTH_LONG).show();
                locationManager.requestSingleUpdate("gps", locationListener, Looper.getMainLooper());
                btnLoc.setVisibility(btnLoc.INVISIBLE);
                btnStopLoc.setVisibility(btnStopLoc.VISIBLE);
            }
        });



    }

    public void stop(){
        btnStopLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager.requestSingleUpdate("gps", locationListener, Looper.getMainLooper());
                Toast.makeText(TrackingActivity.this, "Getting GPS coordinates", Toast.LENGTH_LONG).show();
                btnStopLoc.setVisibility(btnStopLoc.INVISIBLE);
                btnLoc.setVisibility(btnLoc.VISIBLE);
            }
        });

    }
}