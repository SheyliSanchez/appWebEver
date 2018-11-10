package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.ActivityCategorias;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.Panel_de_Control;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin.AdministracionDePerfilesAdmin.EditarPerfil;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVASheyli.ipLocalhost;

public class EditarUsuario extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private int usuarioEditar;
    private EditText nombreusuario,nombrepropio,correo;
    String nombreusuariobar,nombrepropiobar,contrasenabar;
    Button bottonvalidar;
    String nombre_usuario,nombre_propio,corre_o;

    ipLocalhost ip = new ipLocalhost();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_usuario);

        bottonvalidar = (Button)findViewById(R.id.editar);


            usuarioEditar = SharedPrefManager.getInstance(this).getUSUARIO_LOGUEADO().getId_logueado();


        bottonvalidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new actualizarUsuarios().execute();


            }
        });

        nombreusuario = (EditText)findViewById(R.id.EditUsuario);
        nombrepropio = (EditText)findViewById(R.id.EditNombre);
        correo= (EditText)findViewById(R.id.Editcorreo);


        // reflejarCampos();
        //SE OPTIENEN LOS DATOS DEL SERVER Y SE PASAN A VARIABLES
        new llenarlosEditTextdelServer().execute();
        nombreusuariobar=nombreusuario.getText().toString();
        nombrepropiobar=nombrepropio.getText().toString();
        contrasenabar=correo.getText().toString();


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

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.borrar_perfil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.eliminarPerfil) {
            new eliminarUsuario().execute();

        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
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
            SharedPrefManager.getInstance(this).limpiar();
            startActivity(new Intent(this, Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();

        }else if (id == R.id.ediciondeCuenta){

        }else if(id == R.id.panelControlUsuario){
            Intent intent = new Intent(this,PanelDeControlUsuarios.class);
            startActivity(intent);
            finish();

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void validar(){

        //id.setError(null);
        nombreusuario.setError(null);
        nombrepropio.setError(null);
        correo.setError(null);


        String nombusus = nombreusuario.getText().toString();
        String nomb = nombrepropio.getText().toString();
        String cor = correo.getText().toString();


        if(TextUtils.isEmpty(nombusus) || nombusus.startsWith(" ")){
            nombreusuario.setError(getString(R.string.error_nombre_usuario));
            nombreusuario.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(nomb) || nomb.startsWith(" ")){
            nombrepropio.setError(getString(R.string.error_nombre));
            nombrepropio.requestFocus();
            return;

        }if(TextUtils.isEmpty(cor) || cor.startsWith(" ")){
        }else{
            if(!correo.getText().toString().contains("@") && !correo.getText().toString().contains(".")){
                correo.setError(getString(R.string.error_contrasena));
                correo.requestFocus();
                return;
            }

        }

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

    //ACTUALIZACION DE UN USUARIO DESDE EL WEB SERVER
    private class actualizarUsuarios extends AsyncTask<String, Integer, Boolean> {
        private actualizarUsuarios(){}
        boolean resul = true;

        @Override
        protected Boolean doInBackground(String... strings) {

            try {
                HttpClient httpclient;
                HttpPost httppost;
                ArrayList<NameValuePair> parametros;
                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(ip.getIp()+"actualizarUsuario");
                parametros = new ArrayList<NameValuePair>();
                parametros.add(new BasicNameValuePair("usuario",String.valueOf(usuarioEditar)));
                parametros.add(new BasicNameValuePair("usuarionombre",nombreusuario.getText().toString()));
                parametros.add(new BasicNameValuePair("usuariopropio",nombrepropio.getText().toString()));
                parametros.add(new BasicNameValuePair("usuarioemail",correo.getText().toString()));
               // parametros.add(new BasicNameValuePair("tkn", SharedPrefManager.getInstance(getApplicationContext()).getUSUARIO_LOGUEADO().getToken()));

                httppost.setEntity(new UrlEncodedFormEntity(parametros, "UTF-8"));

                httpclient.execute(httppost);


                resul = true;
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }
            return resul;

        }

        protected void onPostExecute(Boolean result) {
            validar();


            if (resul) {
                if (nombreusuario.getError()==null && nombrepropio.getError()==null && correo.getError()==null){
                    Toast.makeText(getApplicationContext(),"Usuario Realizado Correctamente",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditarUsuario.this,PanelDeControlUsuarios.class);
                    startActivity(intent);
                    finish();
                }

            }else {
                Toast.makeText(getApplicationContext(), "Problemas de conexión", Toast.LENGTH_SHORT).show();
            }
        }


    }
    //METODO PARA LLENAR CUANDO SE ACTUALIZARON LOS USUARIOS DESDE EL WEB SERVER.
    private class llenarlosEditTextdelServer extends AsyncTask<String, Integer, Boolean> {
        private llenarlosEditTextdelServer(){}
        boolean resul = true;

        @Override
        protected Boolean doInBackground(String... strings) {

            try {
                HttpClient httpclient;
                HttpPost httppost;
                ArrayList<NameValuePair> parametros;
                httpclient = new DefaultHttpClient();
                httppost = new HttpPost("http://aeo.web-hn.com/WebServices/Mostar_Los_Usuarios_Editados.php");
                parametros = new ArrayList<NameValuePair>();
                parametros.add(new BasicNameValuePair("usuario",String.valueOf(usuarioEditar) ));
                //parametros.add(new BasicNameValuePair("tkn",SharedPrefManager.getInstance(EditarUsuario.this).getUSUARIO_LOGUEADO().getToken()));

                httppost.setEntity(new UrlEncodedFormEntity(parametros, "UTF-8"));
                JSONArray respJSON = new JSONArray(EntityUtils.toString(( httpclient.execute(httppost)).getEntity()));
               // JSONArray respJSON = new JSONArray(EntityUtils.toString(new DefaultHttpClient().execute(new HttpPost("http://aeo.web-hn.com/WebServices/Mostar_Los_Usuarios_Editados.php?usuario="+usuarioEditar)).getEntity()));
                for (int i = 0; i < respJSON.length(); i++) {
                    nombre_usuario = respJSON.getJSONObject(i).getString("nombre_usuario");
                    nombre_propio = respJSON.getJSONObject(i).getString("nombre_propio");
                    corre_o = respJSON.getJSONObject(i).getString("correo");
                }

                resul = true;
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }
            return resul;

        }

        protected void onPostExecute(Boolean result) {

            if (resul) {
                nombreusuario.setText(nombre_usuario);
                nombrepropio.setText(nombre_propio);
                correo.setText(corre_o);
            }else {
                Toast.makeText(getApplicationContext(), "Problemas de conexión", Toast.LENGTH_SHORT).show();
            }
        }


    }

    private class eliminarUsuario extends AsyncTask<String, Integer, Boolean> {
        private eliminarUsuario(){}
        boolean resul = true;

        @Override
        protected Boolean doInBackground(String... strings) {

            try {


                HttpClient httpclient;
                HttpPost httppost;
                ArrayList<NameValuePair> parametros;
                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(ip.getIp()+"eliminarUsuario");
                parametros = new ArrayList<NameValuePair>();
                parametros.add(new BasicNameValuePair("usuario",String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getUSUARIO_LOGUEADO().getId_logueado())) );
                //parametros.add(new BasicNameValuePair("tkn",SharedPrefManager.getInstance(getApplicationContext()).getUSUARIO_LOGUEADO().getToken()));
                httppost.setEntity(new UrlEncodedFormEntity(parametros, "UTF-8"));
                httpclient.execute(httppost);

                //se ejecuta la consulta al webservice y se pasa el id del perfil seleccionado
                //EntityUtils.toString(new DefaultHttpClient().execute(new HttpPost("http://aeo.web-hn.com/WebServices/eliminarPerfil.php?cto="+idperf)).getEntity());
                resul = true;
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }
            return resul;

        }

        protected void onPostExecute(Boolean result) {

            if (resul) {
                //barra de progreso
                //fin de barra de progreso
                Toast.makeText(getApplicationContext(),"Usuario Eliminado",Toast.LENGTH_SHORT).show();
                SharedPrefManager.getInstance(getApplicationContext()).limpiar();
                startActivity(new Intent(getApplicationContext(), Login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();
            }else {
                Toast.makeText(getApplicationContext(), "Problemas de conexión", Toast.LENGTH_SHORT).show();
            }
        }

    }
}

