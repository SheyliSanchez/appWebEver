package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin.AdministracionDePerfilesAdmin;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.ActivityCategorias;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.Panel_de_Control;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVASheyli.FuncionCerrarSesion;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVASheyli.ipLocalhost;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.AcercaDe;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.Login;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.PanelDeControlUsuarios;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.SharedPrefManager;

public class SolicitudesRechazadas extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ArrayList<Fuente_mostrarPerfiles> mostrar_perfiles;
    private ListView lista;
    AdaptadorMostrarPerfiles adaptadorMostrarPerfiles;
    ProgressBar barra;
    int id_contacto;
    String nombre_organizacion, imagen, usuariopropietario;
    int id_usuario_resibido_usuario;
    int id_usu=-1;

    ipLocalhost ip = new ipLocalhost();

    FuncionCerrarSesion cs = new FuncionCerrarSesion();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevas_solicitudes);




        barra = findViewById(R.id.progressBarPerfilesPendientes);
        mostrar_perfiles= new ArrayList<Fuente_mostrarPerfiles>();
        lista = (ListView) findViewById(R.id.listviewperfilesPendientes);


        new llenarListaRechazadas().execute();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.principaldos) {
            startActivity(new Intent(getBaseContext(), ActivityCategorias.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
            finish();

        } else if (id == R.id.acercadeinfodos) {
            Intent intent = new Intent(this,AcercaDe.class);
            startActivity(intent);
            finish();

        }else if (id ==R.id.cerrarsecion){
            cs.cerrarsesion();
            SharedPrefManager.getInstance(getApplicationContext()).limpiar();
            startActivity(new Intent(this,Login.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK));

        }else if (id == R.id.panelControl){
            Intent intent = new Intent(this, Panel_de_Control.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_administracion_de_perfiles_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.menusolicitudesnuevas) {
            Intent i = new Intent(getApplicationContext(),NuevasSolicitudes.class);
            startActivity(i);
            finish();

        } else if (id == R.id.menusolicitudesrechazadas) {

        } else if (id == R.id.menuperfileliminados) {
            Intent i = new Intent(getApplicationContext(),PerfilesEliminados.class);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (SharedPrefManager.getInstance(this).estaLogueado()){


        }else{
            startActivity(new Intent(this, Login.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK)) ;
        }
    }

    public static boolean compruebaConexion(Context context) {

        boolean connected = false;

        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Recupera todas las redes (tanto móviles como wifi)
        NetworkInfo[] redes = connec.getAllNetworkInfo();

        for (int i = 0; i < redes.length; i++) {
            // Si alguna red tiene conexión, se devuelve true
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                connected = true;
            }
        }
        return connected;
    }

    private class llenarListaRechazadas extends AsyncTask<String, Integer, Integer> {
        private llenarListaRechazadas(){}
        int ResponseEstado;

        @Override
        protected Integer doInBackground(String... strings) {

            try {

                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(ip.getIp()+"listarPerfiles?ste=3");
                HttpResponse httpResponse=httpClient.execute(httpGet);
                ResponseEstado = httpResponse.getStatusLine().getStatusCode();

                JSONObject jsonObject = new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
                JSONArray respJSON = jsonObject.getJSONArray("content");

                for (int i = 0; i < respJSON.length(); i++) {
                    id_contacto = respJSON.getJSONObject(i).getInt("id_contacto");
                    nombre_organizacion = respJSON.getJSONObject(i).getString("nombre_organizacion");
                    imagen = respJSON.getJSONObject(i).getString("imagen");
                    usuariopropietario = respJSON.getJSONObject(i).getString("nombre_usuario");
                    mostrar_perfiles.add(new Fuente_mostrarPerfiles(id_contacto, nombre_organizacion,imagen, usuariopropietario));

                }

            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);

            }
            return ResponseEstado;

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            barra.setProgress(values[0]);
        }

        protected void onPostExecute(Integer responseEstado) {

            if (responseEstado==200) {
                barra.setVisibility(View.INVISIBLE);
                adaptadorMostrarPerfiles = new AdaptadorMostrarPerfiles(mostrar_perfiles,getApplicationContext());
                lista.setAdapter(adaptadorMostrarPerfiles);
            }else if(responseEstado==500){
                Toast.makeText(getApplicationContext(),"Ocurrió un error en la base de datos",Toast.LENGTH_SHORT).show();
            }else if(responseEstado==401){
                Toast.makeText(getApplicationContext(), "Token de autenticación inválido o expirado, por favor inicie sesión nuevamente", Toast.LENGTH_SHORT).show();
                cs.cerrarsesion();
                barra.setVisibility(View.INVISIBLE);
                SharedPrefManager.getInstance(getApplicationContext()).limpiar();
                startActivity(new Intent(SolicitudesRechazadas.this, Login.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }else {
                barra.setVisibility(View.INVISIBLE);
                if(compruebaConexion(getApplicationContext())){
                    Toast.makeText(getApplicationContext(), "No hay solicitudes rechazadas", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Problemas de conexión", Toast.LENGTH_SHORT).show();
                }
            }

        }


    }
}
