package io.mse.doggodate.map;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.util.MapUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.GeoPoint;
import com.google.gson.JsonObject;
import com.google.maps.android.data.Feature;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonPoint;
import com.google.maps.android.data.geojson.GeoJsonPointStyle;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import io.mse.doggodate.databinding.MapsFragmentBinding;
import io.mse.doggodate.entity.DoggoZone;
import io.mse.doggodate.MainActivity;
import io.mse.doggodate.R;
import io.mse.doggodate.rest.DogZoneAPI;
import io.mse.doggodate.viewmodel.MapViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback{

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
    private DoggoZone selectedDoggoZone;
    private View view;
    MainActivity mainActivity;
    private MapViewModel mapViewModel;
    private GeoJsonLayer geoJsonLayer;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapViewModel = ViewModelProviders.of(this).get(MapViewModel.class);
        thisF = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final MapsFragmentBinding root = DataBindingUtil.inflate(inflater, R.layout.maps_fragment, container, false);
        context = container.getContext();
        view = root.getRoot();
       // slider.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        mainActivity = (MainActivity)getActivity();
        assert mainActivity != null;
        Objects.requireNonNull(mainActivity.getSupportActionBar()).setTitle("DoggoZones");
        mainActivity.invalidateOptionsMenu();
        park1 = ((MainActivity)getActivity()).getPark1();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        slider = (SlidingUpPanelLayout) view.findViewById(R.id.slider);
        slider.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
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
                        style.setIcon(bitmapDescriptorFromVector(getContext(), R.drawable.park));
                        geoJsonFeature.setPointStyle(style);

                    }

                    geoJsonLayer.setOnFeatureClickListener(new GeoJsonLayer.GeoJsonOnFeatureClickListener() {
                        @Override
                        public void onFeatureClick(Feature feature) {
                            slider.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                            Log.i("MAPFR","COR " + feature);
                            GeoPoint location = new GeoPoint(48.23426714947363,16.329941187195445);
                            String name = feature.getProperty("PARK");
                            String area = feature.getProperty("FLAECHE");
                            String fenceS = feature.getProperty("EINFRIEDUNG");
                            String typ = feature.getProperty("TYP");
                            DoggoZone doggoZone = new DoggoZone(location,
                                    name,
                                    area,
                                    fenceS,
                                    typ);

                            mapViewModel.getSelectedDoggoZone(doggoZone).observe(thisF, new Observer<DoggoZone>() {
                                @Override
                                public void onChanged(DoggoZone doggoZone) {
                                    Log.i("MAP","SLECETED " + doggoZone.getName());
                                    selectedDoggoZone = doggoZone;
                                }

                            });

                            zoneName.setText(name);
                            parkArea.setText("Fläche : " + area);
                            fence.setText("Einfriedung : " + fenceS);
                            type.setText("Typ : " + typ);
                            doggosJoining.setText("8 others are joining");


                            goToDoggoZone.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    NavController navController = Navigation.findNavController(v);
                                    navController.navigate(R.id.toDoggoZone);
                                }
                            });

                        }
                    });



                    geoJsonLayer.addLayerToMap();
                }
            }
        });



        LatLng park1Pos = new LatLng(park1.getLocation().getLatitude(),park1.getLocation().getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(park1Pos,15));

    }


    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int id) {
        float dpsLeft = 8;
        float dpsTop = 5;
        float dm = getResources().getDisplayMetrics().density;
        Drawable background = ContextCompat.getDrawable(context, id);
        assert background != null;
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(context, R.drawable.tree_outline);
        assert vectorDrawable != null;
        vectorDrawable.setBounds((int)(dpsLeft * dm), (int)(dpsTop * dm), vectorDrawable.getIntrinsicWidth()+(int)(dpsLeft * dm), vectorDrawable.getIntrinsicHeight()+(int)(dpsTop * dm));

        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);

    }


}
