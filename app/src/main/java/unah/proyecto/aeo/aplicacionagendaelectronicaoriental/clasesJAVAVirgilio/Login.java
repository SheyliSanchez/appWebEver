package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
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
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVASheyli.ipLocalhost;

public class Login extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public EditText usuario, contrasena;
    public Button button;
    private int contador = 0;
    private int id_usuario;
    private int rol;
    private int estado_usuario;

    Context context=this;

    ipLocalhost ip  = new ipLocalhost();


    private CircularProgressButton acceder;
    String nada,dato,tkasig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usuario = (EditText) findViewById(R.id.usuario_login);
        contrasena = (EditText) findViewById(R.id.contrasena_login);
        //Preferencias de imgadministrador y usuario

        acceder = (CircularProgressButton) findViewById(R.id.ingresar_login);
        acceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceder.startAnimation();

                if (usuario.getText().toString().isEmpty() || contrasena.getText().toString().isEmpty()) {

                    validar();
                    acceder.revertAnimation();
                    acceder.stopAnimation();
                } else {
                    new LoginValidadoWeb().execute();//si existe el usuario y la contraseña son correctas el accedera


                }//fin else
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //muestra el menu lateral
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            startActivity(new Intent(getBaseContext(), ActivityCategorias.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
            finish();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.principaldos) {
           startActivity(new Intent(this,ActivityCategorias.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
           finish();
        } else if (id == R.id.acercadeinfodos) {
            Intent intent = new Intent(this, AcercaDe.class);
            startActivity(intent);

        } else if (id == R.id.login) {


        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Metodo para ingresar al formulario de registrar una nueva cuenta de usuario
    public void Formulario_Registrarse_login(View v) {   //metodo que habre el formulario para registrarse
        Intent intent = new Intent(this, FormularioRegistroUsuario.class);
        usuario.setText("");
        contrasena.setText("");
        startActivity(intent);
    }


    //METODO DE VERIFICADE DESDE EL SERVIDOR
    @SuppressLint("StaticFieldLeak")
    private class LoginValidadoWeb extends AsyncTask<String, Integer, Boolean> {
        private LoginValidadoWeb() {
        }

        boolean resul = true;

        @Override
        protected Boolean doInBackground(String... strings) {



            try {
                String usu=usuario.getText().toString();
                String pas=contrasena.getText().toString();
               HttpClient httpclient;
                HttpPost httppost;
                ArrayList<NameValuePair> parametros;
                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(ip.getIp()+"loginUsuario");
                parametros = new ArrayList<NameValuePair>();
                parametros.add(new BasicNameValuePair("nombre_usuario",usu));
                parametros.add(new BasicNameValuePair("contrasena",pas));
                httppost.setEntity(new UrlEncodedFormEntity(parametros, "UTF-8"));

                JSONObject credencial = new JSONObject(EntityUtils.toString(httpclient.execute(httppost).getEntity()));
                JSONObject credencialArray = credencial.getJSONObject("content");


                    rol = credencialArray.getInt("rol");
                    id_usuario = credencialArray.getInt("idUrs");
                    estado_usuario = credencialArray.getInt("ste");
                    tkasig = credencialArray.getString("tkn");


                if (id_usuario != 0 && rol != 0 && estado_usuario != 0) {
                    resul = true;
                } else {
                        resul = false;
                }
                if (id_usuario==0){
                    resul = false;
                }
                if (credencial.equals("Credenciales incorrectos")){
                    nada="nada";
                }

            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }


            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (resul) {

                SharedPrefManager.getInstance(context).guardarUsuario(
                        new ModeloUsuarioLogueado(
                                id_usuario,
                                rol,
                                estado_usuario,
                                tkasig
                        )
                );


                acceder.stopAnimation();
                acceder.revertAnimation();

                 if (rol == 1 && estado_usuario ==1) {
                    Intent intent = new Intent(Login.this,Panel_de_Control.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();

                } else if (rol ==2 && estado_usuario ==1){

                    Intent intent = new Intent(Login.this,PanelDeControlUsuarios.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }

            } else {
                acceder.stopAnimation();
                acceder.revertAnimation();

                if (id_usuario==0){
                    contador=contador+1;
                    if (contador ==3){
                        usuario.setText("");
                        contrasena.setText("");
                        Toast.makeText(getApplicationContext(), "Limite de intentos agotados", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        acceder.stopAnimation();
                        acceder.revertAnimation();
                        Toast.makeText(getApplicationContext(), "Usuario y/o Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                    }
                   // Toast.makeText(getApplicationContext(),"Usuario y/o Contraseña incorrecta 1",Toast.LENGTH_LONG).show();
                }else  if (id_usuario!=0&& rol==1&&estado_usuario==2){
                    contador=contador+1;
                    if (contador ==3){
                        usuario.setText("");
                        contrasena.setText("");
                        Toast.makeText(getApplicationContext(), "Limite de intentos agotados", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        acceder.stopAnimation();
                        acceder.revertAnimation();
                        Toast.makeText(getApplicationContext(), "Usuario y/o Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                    }

                }else if (id_usuario!=0&& rol==2&&estado_usuario==2){
                    contador=contador+1;
                    if (contador ==3){
                        usuario.setText("");
                        contrasena.setText("");
                        Toast.makeText(getApplicationContext(), "Limite de intentos agotados", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        acceder.stopAnimation();
                        acceder.revertAnimation();
                        Toast.makeText(getApplicationContext(), "Usuario y/o Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                    }

                } else if (nada.equals(nada)) {
                    //Toast.makeText(getApplicationContext(), "Usuario y/o Contraseña incorrecta ", Toast.LENGTH_LONG).show();
                }if (compruebaConexion()==false){
                    Toast.makeText(getApplicationContext(), "Problemas de conexion ", Toast.LENGTH_LONG).show();
                    acceder.stopAnimation();
                    acceder.revertAnimation();
                }

            }//fin de onPostExecute

        }//fin boolean result
        public boolean compruebaConexion() {

            Runtime runtime = Runtime.getRuntime();
            try {
                Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
                int     exitValue = ipProcess.waitFor();
                return (exitValue == 0);
            } catch (IOException e)          { e.printStackTrace(); }
            catch (InterruptedException e) { e.printStackTrace(); }
            return false;
        }

    }//fin boolean

    private void validar(){

        usuario.setError(null);
        contrasena.setError(null);

        String us = usuario.getText().toString();
        String cont = contrasena.getText().toString();


        if(TextUtils.isEmpty(us)){
            usuario.setError(getString(R.string.error_usuario));
            usuario.requestFocus();
            return;
        }if(TextUtils.isEmpty(cont)){
            contrasena.setError(getString(R.string.error_contrasenaingresada));
            contrasena.requestFocus();
            return;

        }


    }


}

