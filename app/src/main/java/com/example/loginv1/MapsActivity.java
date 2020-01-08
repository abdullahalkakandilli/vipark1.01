package com.example.loginv1;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = "    ";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private GoogleMap mMap;
    ArrayList<Integer> ids;

    BitmapDescriptorFactory bitmapDescriptorFactory;
    private StorageReference mStorageRef;
    ConstraintLayout info_window_park_markers;
    TextView info_of_park_name;
    Button check_rezervation;
    private static final String FINE_LOCATION= Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION=Manifest.permission.ACCESS_COARSE_LOCATION;
    private Boolean mLocationPermissionGranted =false;
    private static  final int LOCATION_PERMISSION_REQUEST_CODE =1234;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private EditText mSearchText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mSearchText=(EditText) findViewById(R.id.input_search);
        mSearchText.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        getLocationPermission();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        info_window_park_markers = (ConstraintLayout) findViewById(R.id.info_window_park_markers);
        info_of_park_name = (TextView) findViewById(R.id.info_of_park_name);
        check_rezervation = (Button) findViewById(R.id.check_rezervation);
        getDeviceLocation();
    }
    private void init(){
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_SEARCH || actionId==EditorInfo.IME_ACTION_DONE || event.getAction()==KeyEvent.ACTION_DOWN || event.getAction()==KeyEvent.KEYCODE_ENTER){
                    geoLocate();

                }
                return false;
            }
        });

    }
    private void geoLocate(){
        String searchString = mSearchText.getText().toString();
        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address> list = new ArrayList<>();
        try{
            list =geocoder.getFromLocationName(searchString,1);
        }catch (IOException e){
            Log.e(TAG,"geolocate: "+e.getMessage());

        }
        if(list.size()>0){

            Address address = list.get(0);
            moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),15f);

        }
    }
    /*
    @Override
    protected void onResume() {
        if(mLocationPermissionGranted){
            //getDeviceLocation();
        }
        super.onResume();
    }*/
    private void initMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity.this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        info_window_park_markers.setVisibility(View.INVISIBLE);



        Log.d("test", "asdasd");
        //(document.getData().get("logo_icon_id").toString())

        db.collection("parks")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (final QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getData().get("price").toString().equals("10")){

                                    mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(document.getData().get("x")

                                            .toString()), Double.parseDouble(document.getData().get("y")

                                            .toString()))).title(document.getData().get("name").toString())

                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.bjk)).snippet(document.getData().get("rezervation_able").toString()));}

                                else{

                                    mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(document.getData().get("x")

                                            .toString()), Double.parseDouble(document.getData().get("y")

                                            .toString()))).title(document.getData().get("name").toString())

                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.fsh)).snippet(document.getData().get("rezervation_able").toString()));}

                                /*info_of_park_name.setText(document.get("name").toString());
                                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                    @Override
                                    public boolean onMarkerClick(Marker marker) {
                                        Toast.makeText(MapsActivity.this,"hellö",Toast.LENGTH_SHORT).show();
                                        if(document.getData().get("rezervation_able").equals(true)){
                                            check_rezervation.setClickable(true);
                                        }else{
                                            check_rezervation.setClickable(false);
                                        }
                                        info_window_park_markers.setVisibility(View.VISIBLE);
                                        return true;
                                    }
                                });*/


                                /*mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                    @Override
                                    public void onInfoWindowClick(Marker marker) {

                                        Toast.makeText(MapsActivity.this,"hellö",Toast.LENGTH_SHORT).show();
                                        if(document.getData().get("rezervation_able").toString().equals("1")){
                                            Toast.makeText(MapsActivity.this,"button enable",Toast.LENGTH_SHORT).show();
                                            //info_of_park_name.setText(marker.getTitle()); info window artık yok. popUp var
                                            //check_rezervation.setEnabled(true); info windowdaki button- artık gereksiz
                                        }else{
                                            Toast.makeText(MapsActivity.this,"button enable false",Toast.LENGTH_SHORT).show();
                                            //check_rezervation.setEnabled(false);
                                        }

                                        Intent intent = new Intent(MapsActivity.this,popUp.class);
                                        startActivity(intent);

                                    }
                                });*/

                                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                    @Override
                                    public boolean onMarkerClick(Marker marker) {
                                        if(marker.getSnippet().equals("1")||marker.getSnippet().equals("Rezervasyon yapılabilir")){
                                            marker.setSnippet("Rezervasyon yapılabilir");
                                            Toast.makeText(MapsActivity.this,"button enable",Toast.LENGTH_SHORT).show();

                                        }else if(marker.getSnippet().equals("0")||marker.getSnippet().equals("Rezervasyon yapılamaz")){
                                            marker.setSnippet("Rezervasyon yapılamaz");
                                            Toast.makeText(MapsActivity.this,"sadasdsads",Toast.LENGTH_SHORT).show();

                                        }
                                        Intent intent = new Intent(MapsActivity.this,popUp.class);
                                        String parkName = marker.getTitle();
                                        intent.putExtra("park_name",parkName);
                                        startActivity(intent);

                                        return false;
                                    }
                                });
                            }

                        } else {
                            Log.w("test", "Error getting documents.", task.getException());
                        }
                    }
                });

        mMap.setMyLocationEnabled(true);
        init();

    }

    private void getLocationPermission(){
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                mLocationPermissionGranted =true;
                initMap();
            }

        }
        else{
            ActivityCompat.requestPermissions(this,permissions,LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
    private void getDeviceLocation(){

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            Task location = mFusedLocationProviderClient.getLastLocation();
            ((Task) location).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){

                        Location currentLocation = (Location) task.getResult();
                        moveCamera(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),15f);
                    }else{

                    }
                }
            });
        }catch(SecurityException e){

        }

    }
    private void moveCamera(LatLng latLng , float zoom){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted=false;
        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length>0){
                    for(int i =0;i<grantResults.length;i++){
                        if(grantResults[i]!=PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionGranted=false;
                            return;
                        }
                    }
                    mLocationPermissionGranted=true;
                    initMap();
                }
            }
        }
    }


    public void check_rezervation(View view){
        Intent intent = new Intent(MapsActivity.this,rezervation_activity_with_wheel_time_picker.class);
        startActivity(intent);
    }


    /*ublic void download_file() throws IOException {
        final File localFile = File.createTempFile("images", "jpg");
        mStorageRef.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Successfully downloaded data to local file
                        // ...
                        localFile.getAbsoluteFile();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle failed download
                // ...
            }
        });
    }*/

}
