package io.mse.doggodate;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.ToolbarWidgetWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Context context;
    private TextView zoneName;
    private ImageButton favoritesBtn;
    private DoggoZone park1;
    private DoggoZone park2;
    private DoggoZone park3;
    private DoggoZone park4;
    private DoggoZone park5;
    private DoggoZone park6;
    private SlidingUpPanelLayout slider;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();
        View view = inflater.inflate(R.layout.maps_fragment, container, false);
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
        mMap.addMarker(new MarkerOptions().position(park1Pos).title(park1.getName()).icon(bitmapDescriptorFromVector(getContext(),R.drawable.park))).setTag(park1);
        mMap.addMarker(new MarkerOptions().position(park2Pos).title(park2.getName()).icon(bitmapDescriptorFromVector(getContext(),R.drawable.park))).setTag(park2);
        mMap.addMarker(new MarkerOptions().position(park3Pos).title(park3.getName()).icon(bitmapDescriptorFromVector(getContext(), R.drawable.park_fav))).setTag(park3);
        mMap.addMarker(new MarkerOptions().position(park4Pos).title(park4.getName()).icon(bitmapDescriptorFromVector(getContext(),R.drawable.park))).setTag(park4);
        mMap.addMarker(new MarkerOptions().position(park5Pos).title(park5.getName()).icon(bitmapDescriptorFromVector(getContext(), R.drawable.park_fav))).setTag(park5);
        mMap.addMarker(new MarkerOptions().position(park6Pos).title(park6.getName()).icon(bitmapDescriptorFromVector(getContext(), R.drawable.park_fav))).setTag(park6);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(park1Pos,12));

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.i("MapFragment", "Marker title is " + marker.getTitle());
        zoneName.setText(marker.getTitle());
        slider.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        ((MainActivity)getActivity()).setSelectedDoggoZone((DoggoZone) marker.getTag());
        return true;
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Drawable background = ContextCompat.getDrawable(context, id);
            background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
            Drawable vectorDrawable = ContextCompat.getDrawable(context, R.drawable.ic_pets_black_24dp);
            vectorDrawable.setBounds(20, 10, vectorDrawable.getIntrinsicWidth()+20, vectorDrawable.getIntrinsicHeight()+10);

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
