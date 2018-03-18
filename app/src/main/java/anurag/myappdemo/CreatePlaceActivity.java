package anurag.myappdemo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class CreatePlaceActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    EditText name,place,type,description,location,url1,url2,url3;
    Button save;
    private GoogleApiClient googleApiClient;
    private int PLACE_PICKER_REQUEST = 1;
    ImageButton add_guide,add_location;
    Places_DatabaseHelper db;
    DatabaseHelper gdb;
    private GoogleMap mMap;
    String  latitude="28.612875",   longitude="77.229310";;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_place);
        name= (EditText) findViewById(R.id.et_name_createplace);
        place= (EditText) findViewById(R.id.et_place_createplace);
        type= (EditText) findViewById(R.id.et_type_createplace);
        description= (EditText) findViewById(R.id.et_description_createplace);
        url1= (EditText) findViewById(R.id.et_url1_createplace);
        url2= (EditText) findViewById(R.id.et_url2_createplace);
        url3= (EditText) findViewById(R.id.et_url3_createplace);
        //location=findViewById();
        add_guide= (ImageButton) findViewById(R.id.ib_add_guide_createplace);
        add_location= (ImageButton) findViewById(R.id.ib_get_location_createplace);
        save= (Button) findViewById(R.id.btn_save_createplace);

        add_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                Intent intent;
                try {
                    intent = builder.build(CreatePlaceActivity.this);
                    startActivityForResult(intent,PLACE_PICKER_REQUEST);
                }
                catch (GooglePlayServicesRepairableException e)
                {
                    e.printStackTrace();
                }
                catch (GooglePlayServicesNotAvailableException e)
                {
                    e.printStackTrace();
                }

            }
        });
        //map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Initializing googleapi client
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        db=new Places_DatabaseHelper(this);
        gdb=new DatabaseHelper(this);
        //Dialog box appears when Add guide button clicks
        add_guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(CreatePlaceActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.custom_dialog_guide_add, null);
                final EditText mguidename = (EditText) mView.findViewById(R.id.et_name_dialog_ga);
                final EditText mguidearea = (EditText) mView.findViewById(R.id.et_area_dialog_ga);
                final RatingBar mrating = (RatingBar) mView.findViewById(R.id.rat_dialog_ga);
                final EditText mmobileno = (EditText) mView.findViewById(R.id.et_mobile_dialog_ga);
                final EditText mprice = (EditText) mView.findViewById(R.id.et_price_dialog_ga);
                final EditText mregno=(EditText) mView.findViewById(R.id.et_reg_dialog_ga);
                Button madd = (Button) mView.findViewById(R.id.btn_add_dialog_ga);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                madd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!mguidename.getText().toString().isEmpty() && !mguidearea.getText().toString().isEmpty()){
                            Guide guide=new Guide(mregno.getText().toString(),mguidename.getText().toString(),
                                    mmobileno.getText().toString(),
                                    mguidearea.getText().toString(),
                                    mprice.getText().toString(),
                                    mrating.getRating());
                            // Inserting Contacts
                            boolean res=gdb.addGuide(guide);

                         /*  String key = dbr.child("teamData").push().getKey();
                           Map<String, Object> postValues = db.toMap(team);
                           Map<String, Object> childUpdates = new HashMap<>();
                           childUpdates.put("/teamData/" + key, postValues);
*/

                            if(res) {
                                Toast.makeText(CreatePlaceActivity.this,
                                        "Guide Added Successfully",
                                        Toast.LENGTH_SHORT).show();
                            }
                            else{ Toast.makeText(CreatePlaceActivity.this,
                                    "Guide Added failed",
                                    Toast.LENGTH_SHORT).show();

                            }

                            dialog.dismiss();
                        }else{
                            Toast.makeText(CreatePlaceActivity.this,
                                    "CANCEL",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString()!=null&&place.getText().toString()!=null)
                {
                    Places places = new Places();
                    places.setName(name.getText().toString());
                    places.setPlace(place.getText().toString());
                    places.setType(type.getText().toString());
                    places.setDescription(description.getText().toString());
                    places.setUrl1((url1.getText().toString()));
                    places.setUrl2((url2.getText().toString()));
                    places.setUrl3((url3.getText().toString()));
                    places.setLatitude(latitude);
                    places.setLongitude(longitude);
                    //place.setLocation();

                    Boolean res=db.addPlaces(places);
                    if(res) {
                        Toast.makeText(CreatePlaceActivity.this,
                                "Place Added Successfully",
                                Toast.LENGTH_SHORT).show();
                    }
                    else{ Toast.makeText(CreatePlaceActivity.this,
                            "Place Added failed",
                            Toast.LENGTH_SHORT).show();
                    }



                    finish();

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PLACE_PICKER_REQUEST)
        {
            if(resultCode==RESULT_OK)
            {
                Place place = PlacePicker.getPlace(data,this);
                String address = String.format("%s",place.getAddress());
                try {
                    latitude = String.valueOf(place.getLatLng().latitude);
                    longitude = String.valueOf(place.getLatLng().longitude);
                }
                catch (Exception e)
                {
                    latitude="28.612875";
                    longitude="77.229310";
                }
                Toast.makeText(getApplicationContext(),latitude+"\n"+longitude,Toast.LENGTH_SHORT).show();
                this.place.setText(address);
            }
            else {
                Toast.makeText(getApplicationContext(),"ERROR",Toast.LENGTH_SHORT).show();

            }
        }
    }

    //Getting current location
    private void getCurrentLocation() {
        mMap.clear();
        //Creating a location object
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            longitude = Double.toString(location.getLongitude());
            latitude = Double.toString(location.getLatitude());

            //moving the map to location
            moveMap();
        }
    }

    //Function to move the map
    private void moveMap() {
        //String to display current latitude and longitude
        String msg = latitude + ", "+longitude;

        //Creating a LatLng Object to store Coordinates
        LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));

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
        // Add a marker in Sydney and move the camera
        LatLng Point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        mMap.addMarker(new
                MarkerOptions().position(Point).title("Place_Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Point));
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
