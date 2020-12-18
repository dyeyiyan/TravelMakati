package com.thatchosenone.travelmakati.fragments;


import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thatchosenone.travelmakati.R;
import com.thatchosenone.travelmakati.connection.ConnectivityReceiver;
import com.thatchosenone.travelmakati.connection.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, ConnectivityReceiver.ConnectivityReceiverListener {

    private static final String TAG = "MapFragment";
    private static final String COMMON_TAG = "MapFragment";

    private View mapFragment;

    private MapView mapView;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationRequest mLocationRequest;
    private LocationManager mLocationManager;
    private LocationListener listener;
    private long UPDATE_INTERVAL = 2000;
    private long FASTEST_INTERVAL = 5000;
    private LocationManager locationManager;
    private LatLng latLng;
    private boolean isPermission;
    private Context context;

    Button btnSort;
    Button btnCategories;
    Button btnBarangay;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, lDRMap, ratingRef;

    String currentUser;


    Spinner sSort;
    String sortItem;

    Spinner sMap;
    String mapItem;

    String name, latitude, longitude, categories, address, price, treviews, trates;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // mapFragment = inflater.inflate(R.layout.fragment_map, container, false);

//        btnSort = mapFragment.findViewById(R.id.fm_btn_sort);
//        btnBarangay = mapFragment.findViewById(R.id.fm_btn_barangay);
//        btnCategories = mapFragment.findViewById(R.id.fm_btn_category);

//        btnSort.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SortByDialog bottomSheetSort = new SortByDialog();
//                //bottomSheetSort.SortByListener(this);
//                bottomSheetSort.show(getChildFragmentManager(), "exampleBottomSheet");
//
//            }
//        });
//
//        btnCategories.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CategoriesDialog bottomSheetCategories = new CategoriesDialog();
//                //bottomSheetSort.SortByListener(this);
//                bottomSheetCategories.show(getChildFragmentManager(), "exampleBottomSheet");
//
//            }
//        });
//
//        btnBarangay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                BarangayDialog bottomSheetBarangay = new BarangayDialog();
//                //bottomSheetSort.SortByListener(this);
//                bottomSheetBarangay.show(getChildFragmentManager(), "exampleBottomSheet");
//
//            }
//        });


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            checkUserPermission();
//        }

//            Bundle mapViewBundle = null;
//            if (savedInstanceState != null) {
//                mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
//            }
//
//            mapView = mapFragment.findViewById(R.id.maps_fm);
//            mapView.onCreate(mapViewBundle);
//            mapView.getMapAsync(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        //Path of table of users
        databaseReference = firebaseDatabase.getReference("Users");
        lDRMap = firebaseDatabase.getReference("Leisure");
        ratingRef = firebaseDatabase.getReference("Ratings");

//        sSort = mapFragment.findViewById(R.id.fm_s_category);
//        sMap = mapFragment.findViewById(R.id.fm_s_map_type);
//
//
//        SupportMapFragment mapFragment1 = (SupportMapFragment) getChildFragmentManager()
//                .findFragmentById(R.id.maps_fm);
//        mapFragment1.getMapAsync(this);

//        if (requestSinglePermission()) {
//
//            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
//                    .addConnectionCallbacks(this)
//                    .addOnConnectionFailedListener(this)
//                    .addApi(LocationServices.API)
//                    .build();
//            mLocationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
//            checkLocation();
//
//        }
        checkConnection();
        return mapFragment;
    }


//    private boolean checkLocation() {
//        if (!isLocationEnabled()) {
//            showAlert();
//        }
//        return isLocationEnabled();
//
//    }
//
//    private void showAlert() {
//        final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
//        dialog.setTitle("Enable Location")
//                .setMessage("Your Location Settings is set to 'Off.'\n Please enable to use this app")
//                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent myIntent = new Intent((Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                        startActivity(myIntent);
//                    }
//                })
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//
//        dialog.show();
//    }

//    private boolean isLocationEnabled() {
//        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
//        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
//                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//    }
//
//    private boolean requestSinglePermission() {
//        Dexter.withActivity(getActivity())
//                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
//                .withListener(new PermissionListener() {
//                    @Override
//                    public void onPermissionGranted(PermissionGrantedResponse response) {
//                        isPermission = true;
//                    }
//
//                    @Override
//                    public void onPermissionDenied(PermissionDeniedResponse response) {
//                        //check for permanent denial of permission
//                        if (response.isPermanentlyDenied()) {
//                            isPermission = false;
//                        }
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
//
//                    }
//                }).check();
//        return isPermission;
//    }
//
//    private void startLocationUpdates() {
//        mLocationRequest = LocationRequest.create()
//                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//                .setInterval(UPDATE_INTERVAL)
//                .setFastestInterval(FASTEST_INTERVAL);
//        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//
//        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
//    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        List<String> sort = new ArrayList<>();
        sort.add("Parks");
        sort.add("Museums");
        sort.add("Malls");
        sort.add("Hotels");
        sort.add("Nightlife");
        sort.add("Restaurant");
        sort.add("Art Galleries");
        sort.add("Cafe / Tea Shop");
        //Style and populate the spinner
        ArrayAdapter<String> arrayAdapter;
        arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, sort);

        //Dropdown layout stylea
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Attaching array adapter to spinner
        sSort.setAdapter(arrayAdapter);

        sSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortItem = parent.getItemAtPosition(position).toString();
                loadMap(sortItem, mMap);
                // Show selected item
                //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        List<String> type = new ArrayList<>();
        type.add("Normal");
        type.add("Hybrid");
        type.add("Satellite");
        type.add("Terrain");
        //Style and populate the spinner
        ArrayAdapter<String> arrayAdapters;
        arrayAdapters = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, type);

        //Dropdown layout stylea
        arrayAdapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Attaching array adapter to spinner
        sMap.setAdapter(arrayAdapters);

        sMap.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mapItem = parent.getItemAtPosition(position).toString();
                if (mapItem.equals("Normal")) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
                if (mapItem.equals("Hybrid")) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                }
                if (mapItem.equals("Satellite")) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                }
                if (mapItem.equals("Terrain")) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                }
                //loadMap(sortItem, mMap);
                // Show selected item
                //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });


        //Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        //Declare HashMap to store mapping of marker to Activity


//        //add infowindows method
//        if (mMap != null) {
//            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
//                @Override
//                public View getInfoWindow(Marker marker) {
//                    return null;
//                }
//
//                @Override
//                public View getInfoContents(Marker marker) {
//                    View view = getLayoutInflater().inflate(R.layout.row_leisure_map, null);
//                    final TextView lname = (TextView) view.findViewById(R.id.rli_tv_lname);
//                    final TextView lcat = (TextView) view.findViewById(R.id.rli_tv_lcategory);
//                    final TextView laddress = (TextView) view.findViewById(R.id.rli_tv_laddress);
//                    final TextView lprice = (TextView) view.findViewById(R.id.rli_tv_lprice);
//                    final MaterialRatingBar materialRatingBar = view.findViewById(R.id.rli_mrb_lrating);
//
//                    String actionId = value.get(marker.getId());
//                    lDRMap.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
//
//                                String luid = "" + ds.child("luid").getValue();
//
//                                name = "" + ds.child("name").getValue();
//                                categories = "" + ds.child("category").getValue();
//                                address = "" + ds.child("street").getValue();
//                                price = "" + ds.child("price").getValue();
//                                trates = "" + ds.child("total_rate").getValue();
//
//                                float nltotal = Float.parseFloat(trates);
//                                materialRatingBar.setRating(nltotal);
//
//                                lname.setText(name);
//                                lcat.setText(categories);
//                                laddress.setText(address);
//                                lprice.setText(price);
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//
//                    return view;
//                }
//
//            });
//        }
    }

    private void loadMap(String sortItem, GoogleMap mMap) {
        mMap.clear();

//        lDRMap.orderByChild("category").equalTo(sortItem).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    final Map<String, String> value = (Map<String, String>) ds.getValue();
//
//                    String getname = value.get("name");
//                    String getslat = value.get("latitude");
//                    String getslong = value.get("longitude");
//                    String getAddress = value.get("street");
////                    name = "" + ds.child("name").getValue();
////                    latitude = "" + ds.child("latitude").getValue();
////                    longitude = "" + ds.child("longitude").getValue();
//
//                    Double lat = Double.parseDouble(getslat);
//                    Double lon = Double.parseDouble(getslong);
//                    LatLng latLng = new LatLng(lat, lon);
//
//                    if(sortItem.equals("Cafe / Tea Shop")){
//                        Drawable myDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.parks_m, null);
//                        Bitmap markerBitmap = ((BitmapDrawable) myDrawable).getBitmap();
//
//                        mMap.addMarker(new MarkerOptions().position(latLng)
//                                .title(getname)
//                                .snippet(getAddress)
//                                .icon(BitmapDescriptorFactory.fromBitmap(markerBitmap)));
//
//                        //mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_default_img)));
//                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f));
//                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//                    }
//
//
//
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


    }
//        Query query = databaseReference.orderByChild("uid").equalTo(firebaseUser.getUid());
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    String curCity = "";
//                    if (ds.hasChild("latitude") && ds.hasChild("longitude")) {
//                        String name = "" + ds.child("name").getValue();
//                        String latitude = ""+ ds.child("latitude").getValue();
//                        String longitude = "" +ds.child("longitude").getValue();
//                        Double lat = Double.parseDouble(latitude);
//                        Double lon = Double.parseDouble(longitude);
//                        LatLng latLng = new LatLng(lat, lon);
//                        List<Address> addresses;
//                        try {
//                            addresses = geocoder.getFromLocation(lat, lon, 1);
//                            if (addresses != null && addresses.size() > 0) {
//                                curCity = addresses.get(0).getLocality();
//                                mMap.addMarker(new MarkerOptions().position(latLng)
//                                        .title(curCity)
//                                        .snippet(name)
//                                        .icon(BitmapDescriptorFactory.fromBitmap(markerBitmap)));
//                                //mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_default_img)));
//                                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f));
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    } else {
//                        // mMap.addMarker(new MarkerOptions().position(new LatLng(14.554592,121.0156802)));
//                        //mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_default_img)));
//                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(14.554592, 121.0156802), 14f));
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        startLocationUpdates();
//        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        if (mLocation == null) {
//            startLocationUpdates();
//        } else {
//            Toast.makeText(getContext(), "Location not delete", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
////
////        String msg = "Updated Location" +
////                Double.toString(location.getLatitude()) + "," + Double.toString(location.getLongitude());
////        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
//        HashMap<String, Object> result = new HashMap<>();
//        result.put("latitude", "" + Location.convert(location.getLatitude(), Location.FORMAT_DEGREES));
//        result.put("longitude", "" + Location.convert(location.getLatitude(), Location.FORMAT_DEGREES));
//        databaseReference.child(firebaseUser.getUid()).updateChildren(result);
//    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(COMMON_TAG, "MapFragment onSaveInstanceState");
    }

    //
    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
//        if (mGoogleApiClient == null) {
//            mGoogleApiClient.disconnect();
//        }
    }

//    @Override
//    public void onRadioBarangay(String text) {
//        btnBarangay.setText(text);
//    }
//
//    @Override
//    public void onRadioCategories(String text) {
//        btnCategories.setText(text);
//    }
//
//    @Override
//    public void onRadioSort(String text) {
//        btnSort.setText(text);
//    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {
        if (isConnected) {

        } else {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_frame, new InternetConnection());
            fragmentTransaction.commit();
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    /*Inflate option menu*/
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflating menu
        inflater.inflate(R.menu.main_menu, menu);
//        menu.findItem(R.id.add_post_mm).setVisible(false);
//        menu.findItem(R.id.search_all_mm).setVisible(false);

//        MenuItem searchAll = menu.findItem(R.id.add_search_mm);
//        Intent intent = new Intent(getContext(), SearchAllLeisure.class);
//        searchAll.setIntent(intent);

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkChange(boolean inConnected) {
        showSnack(inConnected);
    }


}
