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
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import io.mse.doggodate.Entity.DoggoZone;
import io.mse.doggodate.MainActivity;
import io.mse.doggodate.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Context context;
    private TextView zoneName;
    private ImageButton favoritesBtn;
    private ImageButton goToDoggoZone;
    private DoggoZone park1;
    private DoggoZone park2;
    private DoggoZone park3;
    private DoggoZone park4;
    private DoggoZone park5;
    private DoggoZone park6;
    private SlidingUpPanelLayout slider;
    private TextView parkArea;
    private TextView fence;
    private TextView type;
    private TextView doggosJoining;
    private DoggoZone selectedDoggoZone;
    private View view;
    MainActivity mainActivity;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();
        view = inflater.inflate(R.layout.maps_fragment, container, false);
        mainActivity = (MainActivity)getActivity();
        mainActivity.getSupportActionBar().setTitle("DoggoZones");


        park1 = ((MainActivity)getActivity()).getPark1();
        park2  = ((MainActivity)getActivity()).getPark2();
        park3  = ((MainActivity)getActivity()).getPark3();
        park4  = ((MainActivity)getActivity()).getPark4();
        park5  = ((MainActivity)getActivity()).getPark5();
        park6  = ((MainActivity)getActivity()).getPark6();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
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

        mMap.setOnMarkerClickListener(this);
        LatLng park1Pos = new LatLng(park1.getLatitude(),park1.getLongitude());
        LatLng park2Pos = new LatLng(park2.getLatitude(),park2.getLongitude());
        LatLng park3Pos = new LatLng(park3.getLatitude(),park3.getLongitude());
        LatLng park4Pos = new LatLng(park4.getLatitude(),park4.getLongitude());
        LatLng park5Pos = new LatLng(park5.getLatitude(),park5.getLongitude());
        LatLng park6Pos = new LatLng(park6.getLatitude(),park5.getLongitude());
        mMap.addMarker(new MarkerOptions().position(park1Pos).icon(bitmapDescriptorFromVector(getContext(),R.drawable.park))).setTag(park1);
        mMap.addMarker(new MarkerOptions().position(park2Pos).icon(bitmapDescriptorFromVector(getContext(),R.drawable.park))).setTag(park2);
        mMap.addMarker(new MarkerOptions().position(park3Pos).icon(bitmapDescriptorFromVector(getContext(), R.drawable.park_fav))).setTag(park3);
        mMap.addMarker(new MarkerOptions().position(park4Pos).icon(bitmapDescriptorFromVector(getContext(),R.drawable.park))).setTag(park4);
        mMap.addMarker(new MarkerOptions().position(park5Pos).icon(bitmapDescriptorFromVector(getContext(), R.drawable.park_fav))).setTag(park5);
        mMap.addMarker(new MarkerOptions().position(park6Pos).icon(bitmapDescriptorFromVector(getContext(), R.drawable.park_fav))).setTag(park6);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(park1Pos,12));

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.i("MapFragment", "Marker title is " + marker.getTitle());
        slider.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        selectedDoggoZone = (DoggoZone) marker.getTag();
        ((MainActivity)getActivity()).setSelectedDoggoZone(selectedDoggoZone);
        zoneName.setText(selectedDoggoZone.getName());
        parkArea.setText("Area: " + selectedDoggoZone.getSurface() + "m2");
        fence.setText("Fence: Yes"  );
        type.setText("Type: Dog Zone");
        doggosJoining.setText("8 others are joining");
        goToDoggoZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.toDoggoZone);
            }
        });
        return false;
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float dpsLeft = 8;
            float dpsTop = 5;
            float dm = getResources().getDisplayMetrics().density;
            Drawable background = ContextCompat.getDrawable(context, id);
            background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
            Drawable vectorDrawable = ContextCompat.getDrawable(context, R.drawable.tree_outline);
            vectorDrawable.setBounds((int)(dpsLeft * dm), (int)(dpsTop * dm), vectorDrawable.getIntrinsicWidth()+(int)(dpsLeft * dm), vectorDrawable.getIntrinsicHeight()+(int)(dpsTop * dm));

            Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            background.draw(canvas);
            vectorDrawable.draw(canvas);

            return BitmapDescriptorFactory.fromBitmap(bitmap);

        } else {
            return BitmapDescriptorFactory.fromResource(id);
        }
    }


}
