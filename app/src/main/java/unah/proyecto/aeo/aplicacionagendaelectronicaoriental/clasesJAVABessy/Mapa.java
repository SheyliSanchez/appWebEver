package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVABessy;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;

public class Mapa extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double x;
    double y;
    String n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle extras = getIntent().getExtras();
        if (extras!=null){
            x = extras.getDouble("latitud");
            y = extras.getDouble("longitud");
            n = extras.getString("nombre");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(googleMap.MAP_TYPE_HYBRID);

        LatLng coordenadas = new LatLng(x,y);
        mMap.addMarker(new MarkerOptions().position(coordenadas).title(n));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordenadas,16));
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }
}
