package io.mse.doggodate.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonPointStyle;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONObject;

import java.util.Iterator;
import java.util.Objects;

import io.mse.doggodate.MainActivity;
import io.mse.doggodate.R;
import io.mse.doggodate.databinding.MapsFragmentBinding;
import io.mse.doggodate.entity.DoggoZone;
import io.mse.doggodate.helpers.HelperViewModel;
import io.mse.doggodate.viewmodel.MapViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    private GoogleMap mMap;
    private Context context;
    private TextView zoneName;
    private ImageButton favoritesBtn;
    private ImageButton goToDoggoZone;
    private DoggoZone park1;
    private SlidingUpPanelLayout slider;
    private TextView parkArea;
    private TextView fence;
    private TextView type;
    private TextView doggosJoining;
    private Fragment thisF;
    private Observer<DoggoZone> selectedDoggoZone;
    private View view;
    private MainActivity mainActivity;
    private MapViewModel mapViewModel;
    private GeoJsonLayer geoJsonLayer;
    private HelperViewModel helperViewModel;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapViewModel = ViewModelProviders.of(this).get(MapViewModel.class);
        helperViewModel = ViewModelProviders.of(getActivity()).get(HelperViewModel.class);
        thisF = this;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient((MainActivity) getActivity());
        getCurrentLocation();
    }

    private void getCurrentLocation() {
        if (((MainActivity)getActivity()).checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ((MainActivity)getActivity()).checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ((MainActivity)getActivity()).requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
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
        Log.i("MAP","on create map view");
        final MapsFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.maps_fragment, container, false);
        context = container.getContext();
        view = binding.getRoot();
        selectedDoggoZone = new Observer<DoggoZone>() {
            @Override
            public void onChanged(DoggoZone doggoZone) {
                binding.setSelectedDoggoZone(doggoZone);
                helperViewModel.setSelectedDoggoZone(doggoZone);
            }
        };

        mainActivity = (MainActivity)getActivity();
        assert mainActivity != null;
        Objects.requireNonNull(mainActivity.getSupportActionBar()).setTitle("DoggoZones");
        mainActivity.invalidateOptionsMenu();
        park1 = ((MainActivity)getActivity()).getPark1();

        slider = (SlidingUpPanelLayout) view.findViewById(R.id.slider);
        //slider.onRestoreInstanceState(null);
        slider.setPanelHeight(0);
        //slider.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        Log.i("MAP","PAN Steate " + slider.getPanelState());
        zoneName = (TextView) view.findViewById(R.id.zone_name);
        favoritesBtn = (ImageButton) view.findViewById(R.id.add_favorites);
        parkArea = (TextView)view.findViewById(R.id.area);
        fence= (TextView)view.findViewById(R.id.fence);
        type=(TextView)view.findViewById(R.id.type);
        doggosJoining=(TextView)view.findViewById(R.id.doggos_joining);
        goToDoggoZone = (ImageButton) view.findViewById(R.id.join_zone);


        return view;
    }

    public void setFavorites(){
        favoritesBtn.setImageResource(R.drawable.heart);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        LatLng myLocation = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(myLocation).icon(bitmapDescriptorFromVector(getContext(),R.drawable.park_fav,R.drawable.dog_white));
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation,15));

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



        mapViewModel.getFeatures().observe(this, new Observer<JSONObject>() {
            @Override
            public void onChanged(JSONObject featuresResponse) {
                if (featuresResponse != null) {
                    Log.i("Map", "Adds Park Layer to Map");
                    geoJsonLayer = new GeoJsonLayer(mMap,featuresResponse);
                    Iterator<GeoJsonFeature> featureIterator = geoJsonLayer.getFeatures().iterator();
                    while (featureIterator.hasNext()) {
                        GeoJsonFeature geoJsonFeature = featureIterator.next();
                        GeoJsonPointStyle style = new GeoJsonPointStyle();
                        style.setIcon(bitmapDescriptorFromVector(getContext(), R.drawable.park,R.drawable.tree_outline));
                        geoJsonFeature.setPointStyle(style);

                    }

                    geoJsonLayer.setOnFeatureClickListener(new GeoJsonLayer.GeoJsonOnFeatureClickListener() {
                        @Override
                        public void onFeatureClick(Feature feature) {
                            slider.setPanelHeight(180);
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

                            selectedDoggoZone.onChanged(doggoZone);

                            MapFirestoreCallback mapFirestoreCallback = new MapFirestoreCallback() {
                                @Override
                                public void onDataRetrieved(@Nullable DoggoZone doggoZone) {
                                    selectedDoggoZone.onChanged(doggoZone);
                                    helperViewModel.setSelectedDoggoZone(doggoZone);


                                }
                            };
                            mapViewModel.getSelectedDoggoZone(doggoZone,mapFirestoreCallback).observe(thisF, selectedDoggoZone);

                            doggosJoining.setText("8 others are joining");


                            goToDoggoZone.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    navigateToDoggoZone(v);
                                }
                            });

                        }
                    });



                    geoJsonLayer.addLayerToMap();
                }
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE:
                if(grantResults.length>1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getCurrentLocation();
                }
                break;
        }
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


}
