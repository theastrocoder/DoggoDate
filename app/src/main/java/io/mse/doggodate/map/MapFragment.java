package io.mse.doggodate.map;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.data.Feature;
import com.google.maps.android.data.Geometry;
import com.google.maps.android.data.Layer;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonPoint;
import com.google.maps.android.data.geojson.GeoJsonPointStyle;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

import io.mse.doggodate.main.MainActivity;
import io.mse.doggodate.R;
import io.mse.doggodate.databinding.MapsFragmentBinding;
import io.mse.doggodate.doggozone.DoggoZoneViewModel;
import io.mse.doggodate.entity.Doggo;
import io.mse.doggodate.entity.DoggoZone;
import io.mse.doggodate.main.MainActivityViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, MapFirestoreCallback, GoogleMap.OnMyLocationButtonClickListener {

    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    private static final String TAG = "MapFragment";
    private GoogleMap mMap;
    private Context context;
    private TextView doggosJoining;
    private View view;
    private MainActivity mainActivity;
    private MapViewModel mapViewModel;
    private GeoJsonLayer geoJsonLayer;
    private DoggoZoneViewModel doggoZoneViewModel;
    private Doggo activeDoggo;
    private MapsFragmentBinding binding;
    private JSONObject JSONFile;
    private GeoJsonPointStyle style;
    private DoggoZone selectedDoggoZoneWithId;
    private JSONObject jsonObjectFeature;
    private Feature selectedFeature;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient((MainActivity) getActivity());
        getCurrentLocation();
    }


    private void getCurrentLocation() {
        if (((MainActivity) getActivity()).checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ((MainActivity) getActivity()).checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ((MainActivity) getActivity()).requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                    SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                            .findFragmentById(R.id.map);
                    assert mapFragment != null;
                      mapFragment.getMapAsync(MapFragment.this);

                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("MAP", "on create map view");
        mapViewModel = ViewModelProviders.of(this).get(MapViewModel.class);
        doggoZoneViewModel = ViewModelProviders.of(getActivity()).get(DoggoZoneViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.maps_fragment, container, false);
        final MainActivityViewModel main = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
        main.getLoggedInDoggo().observe(getViewLifecycleOwner(), new Observer<Doggo>() {
            @Override
            public void onChanged(Doggo doggo) {
                Log.i(TAG, "Active doggo is " + doggo);
                activeDoggo = doggo;
            }
        });
        context = container.getContext();
        view = binding.getRoot();
        final Observer<DoggoZone> selectedDoggoZoneObserver = new Observer<DoggoZone>() {
            @Override
            public void onChanged(DoggoZone doggoZone) {
                Log.i(TAG, "On doggoZone changed " + doggoZone);
                binding.setSelectedDoggoZone(doggoZone);
                if (doggoZone.isFavorite()) {
                    binding.addFavorites.setImageResource(R.drawable.heart);
                } else {
                    binding.addFavorites.setImageResource(R.drawable.heart_outline);
                }
            }
        };
        doggoZoneViewModel.getSelectedDoggoZone().observe(getViewLifecycleOwner(), selectedDoggoZoneObserver);
        doggoZoneViewModel.setMapFirestoreCallback(this);
        doggoZoneViewModel.getSelectedDoggoZoneWithID().observe(getViewLifecycleOwner(), new Observer<DoggoZone>() {
            @Override
            public void onChanged(DoggoZone doggoZone) {
                selectedDoggoZoneWithId = doggoZone;
                doggoZoneViewModel.setSelectedDoggoZone(doggoZone);
            }
        });
        mainActivity = (MainActivity) getActivity();
        assert mainActivity != null;
        Objects.requireNonNull(mainActivity.getSupportActionBar()).setTitle("DoggoZones");
        mainActivity.invalidateOptionsMenu();

        binding.slider.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        binding.addFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedDoggoZoneWithId.isFavorite()) {
                    removeFromFavorites(jsonObjectFeature, v, (GeoJsonFeature) selectedFeature);
                } else {
                    addToFavorites(jsonObjectFeature, v, (GeoJsonFeature) selectedFeature);
                }
            }
        });

        binding.joinZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToDoggoZone(v);
            }
        });
        doggosJoining = (TextView) view.findViewById(R.id.doggos_joining);

        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        final LatLng myLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLng(myLocation));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 17));

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.

            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            context, R.raw.mapstyle));

            if (!success) {
                Log.e("MapsFragment", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {

            Log.e("MapsFragment", "Style parsing failed.");
        }

        mapViewModel.getFeatures().observe(getViewLifecycleOwner(), new Observer<JSONObject>() {
            @Override
            public void onChanged(final JSONObject featuresResponse) {
                if (featuresResponse != null) {
                    JSONFile = featuresResponse;
                    Log.i(TAG, "Adds Park Layer to Map");
                    geoJsonLayer = new GeoJsonLayer(mMap, featuresResponse);

                    //Doggo
                    GeoJsonPoint p = new GeoJsonPoint(myLocation);
                    HashMap<String, String> m = new HashMap<>();
                    m.put("doggo", activeDoggo.getName());
                    GeoJsonFeature doggo = new GeoJsonFeature(p, "myDoggo", m, null);
                    GeoJsonPointStyle doggoStyle = new GeoJsonPointStyle();
                    doggoStyle.setIcon(bitmapDescriptorFromVector(getContext(), R.drawable.park_fav, R.drawable.dog_white));
                    doggoStyle.setTitle("My Doggo");
                    doggo.setPointStyle(doggoStyle);

                    Iterator<GeoJsonFeature> featureIterator = geoJsonLayer.getFeatures().iterator();
                    while (featureIterator.hasNext()) {
                        GeoJsonFeature geoJsonFeature = featureIterator.next();
                        style = new GeoJsonPointStyle();

                        if (isFavorite(geoJsonFeature)) {
                            style.setIcon(bitmapDescriptorFromVector(getContext(), R.drawable.park_fav, R.drawable.tree_outline));
                        } else {
                            style.setIcon(bitmapDescriptorFromVector(getContext(), R.drawable.park, R.drawable.tree_outline));
                        }
                        geoJsonFeature.setPointStyle(style);

                    }

                    geoJsonLayer.addFeature(doggo);
                    geoJsonLayer.addLayerToMap();

                    geoJsonLayer.setOnFeatureClickListener(onFeatureClickListener);
                }
            }
        });
        if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
        }
        mMap.setMyLocationEnabled(true);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG,"YESSS ");
                getCurrentLocation();
                final LatLng myLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 17));

            } else {
                Log.d(TAG,"NOOOOpe");
                // Permission was denied. Display an error message.
            }
        }
    }

    private Layer.OnFeatureClickListener onFeatureClickListener = new Layer.OnFeatureClickListener() {
        @Override
        public void onFeatureClick(final Feature feature) {
            selectedFeature = feature;
            if(feature.hasProperty("doggo")){
                binding.slider.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            }else {
                String name = feature.getProperty("PARK");
                String area = feature.getProperty("FLAECHE");
                String fenceS = feature.getProperty("EINFRIEDUNG");
                String typ = feature.getProperty("TYP");
                final DoggoZone doggoZone = new DoggoZone(
                        name,
                        area,
                        fenceS,
                        typ,
                        false);

                doggoZoneViewModel.setSelectedDoggoZone(doggoZone);
                binding.slider.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                if (isFavorite(feature)) {
                    binding.addFavorites.setImageResource(R.drawable.heart);
                } else {
                    binding.addFavorites.setImageResource(R.drawable.heart_outline);
                }
                doggoZoneViewModel.createNewDoggoZone(doggoZone);
                doggoZoneViewModel.loadDoggoZoneFromFirestore(doggoZone);
                doggosJoining.setText("8 others are joining");
                jsonObjectFeature = getJSONObject(feature);
            }

        }
    };


    private boolean isFavorite(Feature feature){

        if(feature.getProperty("fav")!=null){
            if(feature.getProperty("fav").equals("true")){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }

    private JSONObject getJSONObject(Feature feature){
        Log.i(TAG,"Creating a JSONObject of feature");
        JSONObject featureObj = JSONFile;
        try {
            JSONArray a = JSONFile.optJSONArray("features");
            JSONObject obA = a.toJSONObject(a);
            Iterator<String> i = obA.keys();
            int k = 0;
            while (i.hasNext()){
                String s = i.next();
                JSONObject ob = new JSONObject(s);
                if(ob.get("id").equals(feature.getId())){
                    featureObj =(JSONObject)JSONFile.getJSONArray("features").get(k);
                }
                k++;
            }
        } catch (JSONException e) {
            Log.i(TAG,"Failed to get JSONObject of feature " + e.getLocalizedMessage());
        }
        return featureObj;
    }


    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int pin,int icon) {
        float dpsLeft = 8;
        float dpsTop = 5;
        float dm = getResources().getDisplayMetrics().density;
        Drawable background = ContextCompat.getDrawable(context, pin);
        assert background != null;
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(context, icon);
        assert vectorDrawable != null;
        vectorDrawable.setBounds((int)(dpsLeft * dm), (int)(dpsTop * dm), vectorDrawable.getIntrinsicWidth()+(int)(dpsLeft * dm), vectorDrawable.getIntrinsicHeight()+(int)(dpsTop * dm));

        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);

    }

    private void navigateToDoggoZone(View v){
        NavController navController = Navigation.findNavController(v);
        navController.navigate(R.id.toDoggoZone);


    }

    @Override
    public void onDoggoZoneCreated(DoggoZone doggoZone) {
        Log.i(TAG,"DoggoZone created callback");
        doggoZoneViewModel.loadDoggoZoneFromFirestore(doggoZone);

    }

    private void addToFavorites(final JSONObject feature, final View view, final GeoJsonFeature geoJsonFeature) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setMessage("Sie können DoggoZone to Favorites hinzufügen");
        alertDialogBuilder.setTitle("In Favorites hinzufügen ?");

        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(view.getContext(), "In Favorites hinzugefügt", Toast.LENGTH_LONG).show();
                binding.addFavorites.setImageResource(R.drawable.heart);
                try {
                    feature.getJSONObject("properties").put("fav",true);
                    selectedDoggoZoneWithId.setFavorite(true);
                    doggoZoneViewModel.updateDoggoZone(selectedDoggoZoneWithId,activeDoggo,JSONFile);
                    style.setIcon(bitmapDescriptorFromVector(getContext(),R.drawable.park_fav,R.drawable.tree_outline));
                    geoJsonFeature.setPointStyle(style);

                } catch (JSONException e) {
                    Log.i(TAG,"Failed to add fav property to JSON file " + e.getLocalizedMessage());
                }
            }
        });

        alertDialogBuilder.setNegativeButton("Nein", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(view.getContext(), "Hinzufügen abgelehnt!", Toast.LENGTH_LONG).show();

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void removeFromFavorites(final JSONObject feature, final View view, final GeoJsonFeature geoJsonFeature) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setMessage("DoggoZone wird aus Favorites entfernt");
        alertDialogBuilder.setTitle("DoggoZone aus Favorites entfernen? ");

        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(view.getContext(), "Aus Favorites entfernt", Toast.LENGTH_LONG).show();
                binding.addFavorites.setImageResource(R.drawable.heart_outline);
                try {
                    feature.getJSONObject("properties").put("fav",false);

                    selectedDoggoZoneWithId.setFavorite(false);
                    doggoZoneViewModel.updateDoggoZone(selectedDoggoZoneWithId,activeDoggo,JSONFile);
                    style.setIcon(bitmapDescriptorFromVector(getContext(),R.drawable.park,R.drawable.tree_outline));
                    geoJsonFeature.setPointStyle(style);

                } catch (JSONException e) {
                    Log.i(TAG,"Failed to add fav property to JSON file " + e.getLocalizedMessage());
                }
            }
        });

        alertDialogBuilder.setNegativeButton("Nein", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(view.getContext(), "Entfernen abgelehnt!", Toast.LENGTH_LONG).show();

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Log.i(TAG,"HII LOC");
        return false;
    }
}
