package scpp.globaleye.com.scppclient.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;


import scpp.globaleye.com.scppclient.R;

public class MapsActivity extends FragmentActivity implements LocationListener {

    GoogleMap googleMap;
    double start_lat,stop_lat,start_lng,stop_lng,lat,lng;
    Criteria criteria;
    LocationManager locationManager;
    String provider;
    Location location;
    Button start_btn,stop_btn;
    TextView tv;
    float[] distance = new float[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //System.out.println("on createteeeeeeeeeeeeeee");

        /*SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        googleMap = fm.getMap();
        getLocation();

        //////button
        start_btn = (Button) findViewById(R.id.button1);
        stop_btn = (Button) findViewById(R.id.button2);
        stop_btn.setEnabled(false);

        start_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                //System.out.println("startttttttttttttttttttttttttbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
                if(tv != null){
                    tv.setText("");
                }

                stop_btn.setEnabled(true);
                start_btn.setEnabled(false);
                getStarrtLoc(location);

            }
        });

        stop_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                //System.out.println("stoppppppppppppppppppppppppppbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
                start_btn.setEnabled(true);
                stop_btn.setEnabled(false);
                getStoptLoc(location);
            }
        });*/

    }
    @Override
    public void onLocationChanged(Location loc) {

        location = loc;
        //System.out.println("changggggggggggggggggggggggggg"+loc.getLatitude()+loc.getLongitude());
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public Location getLocation(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //return;
            Toast.makeText(this,"NO PERMISSION", Toast.LENGTH_LONG).show();
        }
        // Enabling MyLocation Layer of Google Map
        googleMap.setMyLocationEnabled(true);

        // Getting LocationManager object from System Service LOCATION_SERVICE
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        criteria = new Criteria();

        // Getting the name of the best provider
        provider = locationManager.getBestProvider(criteria, true);

        // Getting Current Location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //return;
            Toast.makeText(this,"NO PERMISSION", Toast.LENGTH_LONG).show();
        }
        location = locationManager.getLastKnownLocation(provider);

        if(location!=null){
            Toast.makeText(this,"GPS FIXED", Toast.LENGTH_LONG).show();
            //onLocationChanged(location);
            //return location;
            lat=location.getLatitude();
            lng=location.getLongitude();
        }
        locationManager.requestLocationUpdates(provider,0,0, this);
        //System.out.println("llllllllllllllllllllllllllllllllllllll"+lat+lng);
        return location;
    }

    public void getStarrtLoc(Location start_loc){
        start_lat = start_loc.getLatitude();
        start_lng = start_loc.getLongitude();
        //System.out.println("startttttttttttttttttttttttttttttttttt"+start_lat+"ffffffffff"+start_lng);

    }

    public void getStoptLoc(Location stop_loc){

        stop_lat = stop_loc.getLatitude();
        stop_lng = stop_loc.getLongitude();
        //System.out.println("stppppppppppppppppppppppppppppppppppppp"+stop_lat+"ffffffffff"+stop_lng);
        calcDistance();
        tv = (TextView) findViewById(R.id.textView);
        tv.setText("Distance :"+distance[0]);
        //System.out.println("dddddddddd"+distance[0]);
    }

    public void calcDistance(){
        Location.distanceBetween(start_lat,start_lng,stop_lat,stop_lng,distance);
    }

    @Override
    public void onBackPressed() {
          super.onBackPressed();
          MapsActivity.this.finish();
          this.overridePendingTransition(R.anim.stay_in, R.anim.bottom_out);
    }
}
