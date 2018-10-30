package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVABessy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin.AdministracionDePerfilesAdmin.EditarPerfil;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin.AdministracionDePerfilesAdmin.NuevoPerfil;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.EditarPerfilOrganizacion;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.FormularioNuevaOrganizacion;

public class Ingresar_Ubicacion extends AppCompatActivity implements GoogleMap.OnMarkerDragListener,OnMapReadyCallback {
    private GoogleMap mMap;
    private Marker marcas;
    double latitud;
    double longitud;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar__ubicacion);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        LatLng danli;
        Bundle extras = getIntent().getExtras();
        if (getIntent().getExtras()!=null){
           double latitud = getIntent().getExtras().getDouble("latitud");
           double longitud = getIntent().getExtras().getDouble("longitud");
           danli= new  LatLng(latitud, longitud);

        }else {

        danli = new LatLng(14.041458, -86.568061);
        }

            marcas = googleMap.addMarker(new MarkerOptions().position(danli).title("Danlí").draggable(true));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(danli, 16));
            googleMap.setOnMarkerDragListener(this);
            mMap.getUiSettings().setZoomControlsEnabled(true);

    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        if (marker.equals(marcas)) {
            Toast.makeText(this, "Ubicar", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        if (marker.equals(marcas)) {
            String nuevoTitulo = String.format(Locale.getDefault(), getString(R.string.detalle_de_marcas),
                    marker.getPosition().latitude, marker.getPosition().longitude);
            setTitle(nuevoTitulo);
        }
    }
    @Override
    public void onMarkerDragEnd(Marker marker) {
        if (marker.equals(marcas)) {
            Toast.makeText(this, "Ubicacion Exitosa.", Toast.LENGTH_SHORT).show();

        }
    }


    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.enviar_ubicacion, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.send) {
            String la, lo;
            la = Double.toString(marcas.getPosition().latitude);
            lo = Double.toString(marcas.getPosition().longitude);

            Intent data = new Intent();
            data.putExtra("latitud", la);
            data.putExtra("longitud", lo);
            setResult(FormularioNuevaOrganizacion.RESULT_OK, data);
            finish();
            return true;

        }else if(id==R.id.send) {

            String la, lo;
            la = Double.toString(marcas.getPosition().latitude);
            lo = Double.toString(marcas.getPosition().longitude);

            Intent data = new Intent();
            data.putExtra("latitud", la);
            data.putExtra("longitud", lo);
            setResult(NuevoPerfil.RESULT_OK, data);
            finish();

            return true;
        }else if(id==R.id.send) {

            String la, lo;
            la = Double.toString(marcas.getPosition().latitude);
            lo = Double.toString(marcas.getPosition().longitude);

            Intent data = new Intent();
            data.putExtra("latitud", la);
            data.putExtra("longitud", lo);
            setResult(EditarPerfilOrganizacion.RESULT_OK, data);
            finish();

            return true;
        }else if(id==R.id.send) {

            String la, lo;
            la = Double.toString(marcas.getPosition().latitude);
            lo = Double.toString(marcas.getPosition().longitude);

            Intent data = new Intent();
            data.putExtra("latitud", la);
            data.putExtra("longitud", lo);
            setResult(EditarPerfil.RESULT_OK, data);
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}