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
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.ActivityCategorias;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.Editar_Usuarios;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.Panel_de_Control;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin.AdministracionDePerfilesAdmin.EditarPerfil;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVASheyli.FuncionCerrarSesion;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVASheyli.ipLocalhost;

public class EditarUsuario extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private int usuarioEditar;
    private EditText nombreusuario,nombrepropio,correo;
    String nombreusuariobar,nombrepropiobar,contrasenabar;
    Button bottonvalidar;
    String nombre_usuario,nombre_propio,corre_o;

    ipLocalhost ip = new ipLocalhost();
    FuncionCerrarSesion cs = new FuncionCerrarSesion();
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
            cs.cerrarsesion();
            startActivity(new Intent(this, Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            SharedPrefManager.getInstance(this).limpiar();
            //finish();

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
    private class actualizarUsuarios extends AsyncTask<String, Integer, Integer> {
        private actualizarUsuarios(){}
        int ResponseEstado;

        @Override
        protected Integer doInBackground(String... strings) {

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
                parametros.add(new BasicNameValuePair("Authorization",SharedPrefManager.getInstance(EditarUsuario.this).getUSUARIO_LOGUEADO().getToken()));
                httppost.setEntity(new UrlEncodedFormEntity(parametros, "UTF-8"));
                HttpResponse httpResponse=httpclient.execute(httppost);
                ResponseEstado = httpResponse.getStatusLine().getStatusCode();

            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);

            }
            return ResponseEstado;

        }

        protected void onPostExecute(Integer responseEstado) {
            validar();


            if (responseEstado==200) {
                if (nombreusuario.getError()==null && nombrepropio.getError()==null && correo.getError()==null){
                    Toast.makeText(getApplicationContext(),"Usuario Realizado Correctamente",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditarUsuario.this,PanelDeControlUsuarios.class);
                    startActivity(intent);
                    finish();
                }

            }else if(responseEstado==500){
                Toast.makeText(getApplicationContext(), "Ocurrió un error en la base de datos", Toast.LENGTH_SHORT).show();
            }else if(responseEstado==401){
                Toast.makeText(getApplicationContext(), "Token de autenticación inválido o expirado, por favor inicie sesión nuevamente", Toast.LENGTH_SHORT).show();
                cs.cerrarsesion();
                SharedPrefManager.getInstance(getApplicationContext()).limpiar();
                startActivity(new Intent(EditarUsuario.this, Login.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }else if(responseEstado==400){
                Toast.makeText(getApplicationContext(), "El email ya existe.", Toast.LENGTH_SHORT).show();
            }else if(responseEstado==403){
                Toast.makeText(getApplicationContext(), "Este nombre de usuario ya existe.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Problemas de conexión", Toast.LENGTH_SHORT).show();
            }
        }


    }
    //METODO PARA LLENAR CUANDO SE ACTUALIZARON LOS USUARIOS DESDE EL WEB SERVER.
    private class llenarlosEditTextdelServer extends AsyncTask<String, Integer, Integer> {
        private llenarlosEditTextdelServer(){}
        int ResponseEstado;

        @Override
        protected Integer doInBackground(String... strings) {

            try {
                HttpClient httpclient;
                HttpPost httppost;
                ArrayList<NameValuePair> parametros;
                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(ip.getIp()+"obtenerUsuario");
                parametros = new ArrayList<NameValuePair>();
                parametros.add(new BasicNameValuePair("usuario",String.valueOf(usuarioEditar) ));
                parametros.add(new BasicNameValuePair("Authorization",SharedPrefManager.getInstance(EditarUsuario.this).getUSUARIO_LOGUEADO().getToken()));
                httppost.setEntity(new UrlEncodedFormEntity(parametros, "UTF-8"));
                HttpResponse httpResponse=httpclient.execute(httppost);
                ResponseEstado = httpResponse.getStatusLine().getStatusCode();


                JSONObject jsonObject = new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
                JSONArray respJSON = jsonObject.getJSONArray("content");

                for (int i = 0; i < respJSON.length(); i++) {
                    nombre_usuario = respJSON.getJSONObject(i).getString("nombre_usuario");
                    nombre_propio = respJSON.getJSONObject(i).getString("nombre_propio");
                    corre_o = respJSON.getJSONObject(i).getString("correo");
                }

                //resul = true;
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
               // resul = false;
            }
            return ResponseEstado;

        }

        protected void onPostExecute(Integer responseEstado) {

            if (responseEstado==200) {
                nombreusuario.setText(nombre_usuario);
                nombrepropio.setText(nombre_propio);
                correo.setText(corre_o);
            }else if(responseEstado==500){
                Toast.makeText(getApplicationContext(), "Ocurrió un error en la base de datos", Toast.LENGTH_SHORT).show();
            }else if(responseEstado==401){
                Toast.makeText(getApplicationContext(), "Token de autenticación inválido o expirado, por favor inicie sesión nuevamente", Toast.LENGTH_SHORT).show();
                cs.cerrarsesion();
                SharedPrefManager.getInstance(getApplicationContext()).limpiar();
                startActivity(new Intent(EditarUsuario.this, Login.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }else {
                Toast.makeText(getApplicationContext(), "Problemas de conexión", Toast.LENGTH_SHORT).show();
            }
        }


    }

    private class eliminarUsuario extends AsyncTask<String, Integer, Integer> {
        private eliminarUsuario(){}
        int ResponseEstado;

        @Override
        protected Integer doInBackground(String... strings) {

            try {


                HttpClient httpclient;
                HttpPost httppost;
                ArrayList<NameValuePair> parametros;
                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(ip.getIp()+"eliminarUsuario");
                parametros = new ArrayList<NameValuePair>();
                parametros.add(new BasicNameValuePair("usuario",String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getUSUARIO_LOGUEADO().getId_logueado())) );
                parametros.add(new BasicNameValuePair("Authorization",SharedPrefManager.getInstance(EditarUsuario.this).getUSUARIO_LOGUEADO().getToken()));
                httppost.setEntity(new UrlEncodedFormEntity(parametros, "UTF-8"));
                HttpResponse httpResponse=httpclient.execute(httppost);
                ResponseEstado = httpResponse.getStatusLine().getStatusCode();
                //se ejecuta la consulta al webservice y se pasa el id del perfil seleccionado
                //EntityUtils.toString(new DefaultHttpClient().execute(new HttpPost("http://aeo.web-hn.com/WebServices/eliminarPerfil.php?cto="+idperf)).getEntity());

            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);

            }
            return ResponseEstado;

        }

        protected void onPostExecute(Integer responseEstado) {

            if (responseEstado==200) {
                Toast.makeText(getApplicationContext(),"Usuario Eliminado",Toast.LENGTH_SHORT).show();
                SharedPrefManager.getInstance(getApplicationContext()).limpiar();
                startActivity(new Intent(getApplicationContext(), Login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();
            }else if(responseEstado==500){
                Toast.makeText(getApplicationContext(), "Ocurrió un error en la base de datos", Toast.LENGTH_SHORT).show();
            }else if(responseEstado==401){
                Toast.makeText(getApplicationContext(), "Token de autenticación inválido o expirado, por favor inicie sesión nuevamente", Toast.LENGTH_SHORT).show();
                cs.cerrarsesion();
                SharedPrefManager.getInstance(getApplicationContext()).limpiar();
                startActivity(new Intent(EditarUsuario.this, Login.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }else {
                Toast.makeText(getApplicationContext(), "Problemas de conexión", Toast.LENGTH_SHORT).show();
            }
        }

    }
}

