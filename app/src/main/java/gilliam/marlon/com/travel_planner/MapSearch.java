package gilliam.marlon.com.travel_planner;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapSearch extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private UiSettings mUiSettings;
    Address address;
    String location;
    int PLACE_PICKER_REQUEST = 1;

    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mUiSettings = mMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void invokeSearch(View view)
    {
        address = null;
        EditText editSearch = (EditText) findViewById(R.id.editSearch);
        location = editSearch.getText().toString();
        List<Address> addressList = null;

        if (location != null)
        {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);
            }catch (IOException e){
                e.printStackTrace();
            }

            address = addressList.get(0);
            LatLng latLang= new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLang).title("Marker"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLang));
        }
    }

    public void invokeAddLocation(View view)
    {
        String latitude = null;
        String longitude = null;
        Double lat;
        Double lng;
        LatLng latLong = null;
        if (address != null)
        {
            latLong = new LatLng(address.getLatitude(), address.getLongitude());
            lat = latLong.latitude;
            lng = latLong.longitude;
            latitude = lat.toString();
            longitude = lng.toString();
        }
        Intent addLocation = new Intent(MapSearch.this, Location.class);
        addLocation.putExtra("lat", latitude);
        addLocation.putExtra("lng", longitude);
        addLocation.putExtra("NAME", location);
        addLocation.putExtra("mode", "add");
        startActivity(addLocation);
    }
}
