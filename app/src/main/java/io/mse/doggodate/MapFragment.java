package io.mse.doggodate;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Arrays;
import java.util.List;

import io.mse.doggodate.Entity.DoggoZone;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Context context;
    private TextView zoneName;
    private Button joinBtn;
    private DoggoZone park1;
    private DoggoZone park2;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();
        View view = inflater.inflate(R.layout.activity_maps, container, false);
        park1 = ((MainActivity)getActivity()).getPark1();
        park2  = ((MainActivity)getActivity()).getPark2();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        zoneName = (TextView) view.findViewById(R.id.zone_name);
        joinBtn = (Button) view.findViewById(R.id.join_zone);
        joinBtn.setText("Join");
        return view;
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
        mMap.addMarker(new MarkerOptions().position(park1Pos).title(park1.getName()));
        mMap.addMarker(new MarkerOptions().position(park2Pos).title(park2.getName()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(park1Pos,17));

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.i("onMarkrerClick", "Marker title is " + marker.getTitle());
        zoneName.setText(marker.getTitle());
        return true;
    }
}
