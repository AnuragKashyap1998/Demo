package anurag.myappdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class PlaceDetailActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMapLongClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerDragListener {
    TextView name, place, type, description;
    ListView guidelist;
    ViewPager viewPager;
    DatabaseHelper gdb;
    private GoogleMap mMap;
    Places_DatabaseHelper pdb;
    Double longitude, latitude;


    //Google ApiClient
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placedetail);
        name = (TextView) findViewById(R.id.tv_name_placedetail);
        place = (TextView) findViewById(R.id.tv_place_placedetail);
        type = (TextView) findViewById(R.id.tv_type_placedetail);
        description = (TextView) findViewById(R.id.tv_description_placedetail);
        guidelist = (ListView) findViewById(R.id.lv_guide_placedetail);
        viewPager = (ViewPager) findViewById(R.id.viewpager);


        gdb = new DatabaseHelper(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Places details = (Places) this.getIntent().getSerializableExtra("PLACESDETAILS");

            name.setText(details.getName());
            place.setText(details.getPlace());
            description.setText(details.getDescription());
            type.setText(details.getType());
            try {
                latitude = Double.parseDouble(details.getLatitude());
                longitude = Double.parseDouble(details.getLongitude());
            } catch (Exception e) {

                latitude = 28.612875;
                longitude = 77.229310;
            }
            ImageAdapter adapter = new ImageAdapter(this, new String[]{details.getUrl1(), details.getUrl2(), details.getUrl3()});
            viewPager.setAdapter(adapter);
            //location.setText(details.getLanguage());
            Toast.makeText(getApplicationContext(), details.getName(), Toast.LENGTH_SHORT).show();

            //Glide.with(this).load("https://image.tmdb.org/t/p/w500"+details.getPoster_path()).into(image);
        } else {
            Toast.makeText(getApplicationContext(), "ERRor", Toast.LENGTH_SHORT).show();

        }

        //map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

        //Initializing googleapi client
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


//ListView
        final ArrayList<Guide> guidedetail = gdb.getAllGuide();
        final MyListAdapter adapter = new MyListAdapter(this, guidedetail);
        guidelist.setAdapter(adapter);
        guidelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String value = adapter.getItem(i).getName();
                Toast.makeText(getApplicationContext(), "Thanks for Selecting Mr."+guidedetail.get(i).getName(), Toast.LENGTH_SHORT).show();
                final int pos = i;
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(PlaceDetailActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.cutom_dialog_guide_display, null);
                final EditText name = (EditText) mView.findViewById(R.id.et_name_dialog_gd);
                final EditText area = (EditText) mView.findViewById(R.id.et_area_dialog_gd);
                final EditText price = (EditText) mView.findViewById(R.id.et_price_dialog_gd);
                final EditText mobile = (EditText) mView.findViewById(R.id.et_mobile_gd);
                final EditText regno = (EditText) mView.findViewById(R.id.et_reg_dialog_gd);
                final RatingBar rating = (RatingBar) mView.findViewById(R.id.rat_dialog_gd);


                name.setText(guidedetail.get(i).getName().toString());
                area.setText(guidedetail.get(i).getArea().toString());
                price.setText(guidedetail.get(pos).getPackage_price().toString());
                mobile.setText(guidedetail.get(pos).getMobile_no().toString());
                regno.setText(guidedetail.get(pos).getRegno().toString());
                rating.setRating(guidedetail.get(pos).getRating());


                Button mmsg = (Button) mView.findViewById(R.id.btn_msg_dialog_gd);
                Button mcall = (Button) mView.findViewById(R.id.btn_call_dialog_gd);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                mcall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + Uri.encode(mobile.getText().toString().trim())));
                        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        startActivity(callIntent);


                    }
                });
                mmsg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, guidedetail.get(pos).getName());
                        startActivity(intent);


                    }
                });
            }
        });


    }

    //Getting current location
    private void getCurrentLocation() {
        mMap.clear();
        //Creating a location object
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            //Getting longitude and latitude
            longitude = location.getLongitude();
            latitude = location.getLatitude();

            //moving the map to location
            moveMap();
        }
    }

    //Function to move the map
    private void moveMap() {
        //String to display current latitude and longitude
        String msg = latitude + ", "+longitude;

        //Creating a LatLng Object to store Coordinates
        LatLng latLng = new LatLng(latitude, longitude);

        //Adding marker to map
        mMap.addMarker(new MarkerOptions()
                .position(latLng) //setting position
                .draggable(true) //Making the marker draggable
                .title("Current Location")); //Adding a title

        //Moving the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        //Animating the camera
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        //Displaying current coordinates in toast
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latLng = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMapLongClickListener(this);
    }

    @Override
    public void onConnected(Bundle bundle) {
        getCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        //Clearing all the markers
        mMap.clear();

        //Adding a new marker to the current pressed position
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .draggable(true));
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        //Getting the coordinates
        latitude = marker.getPosition().latitude;
        longitude = marker.getPosition().longitude;

        //Moving the map
        moveMap();
    }

}
