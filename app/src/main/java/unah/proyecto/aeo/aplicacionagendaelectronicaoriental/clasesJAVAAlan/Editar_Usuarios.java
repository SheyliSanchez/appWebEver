package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVASheyli.FuncionCerrarSesion;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVASheyli.ipLocalhost;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.EditarUsuario;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.FormularioRegistroLogin;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.Login;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.SharedPrefManager;


/**
 * Created by alan fabricio on 15/03/2018.
 */


public class Editar_Usuarios extends AppCompatActivity {

    private int usuarioEditar;
    private EditText nombreusuario,nombrepropio,correo;
    String nombreusuariobar,nombrepropiobar,correobar;
    Button bottonvalidar;
    String nombre_usuario,nombre_propio,correoo;


    ipLocalhost ip = new ipLocalhost();
    FuncionCerrarSesion cs = new FuncionCerrarSesion();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_usuarios);

        bottonvalidar = (Button)findViewById(R.id.editar);
//
        //RECIVIMOS EL ID QUE VIENE DE LA CLASE MOSTRAR USUARIOS.
        Bundle extras = this.getIntent().getExtras();
        if(extras!=null) {
            usuarioEditar = extras.getInt("id");
        }
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
        correobar=correo.getText().toString();

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
            removerusuario();

        }

        return super.onOptionsItemSelected(item);
    }

    //METODO PARA ELIMINAR USUARIO  ATRAVES  DE UN CUADRO DE DIALOGO
    public void removerusuario() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.eliminar_usuario);
        String fmt= getResources().getString(R.string.mensaje_para_eliminar);
        builder.setMessage(String.format(fmt,String.valueOf(nombreusuariobar)));
        builder.setPositiveButton(R.string.eliminar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //  usuarios.remove(pos);


                if(!nombreusuario.getText().toString().contains("Admin")){
                    if (usuarioEditar == SharedPrefManager.getInstance(Editar_Usuarios.this).getUSUARIO_LOGUEADO().getId_logueado()){


                        //PASAMOS EL NOMBRE DE LA CLASE QUE EJECUTA LA SENTENCIA SQL DEL WEB SERVISE
                        new eliminarUsuario().execute();
                       // Intent intent = new Intent(Editar_Usuarios.this,ActivityCategorias.class);
                        cs.cerrarsesion();
                        startActivity(new Intent(getBaseContext(), ActivityCategorias.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                        SharedPrefManager.getInstance(Editar_Usuarios.this).limpiar();

                        //setResult(Mostrar_Usuarios.RESULT_OK,intent);
                        //startActivity(intent);
                        //finish();


                    }else{
                        new eliminarUsuario().execute();
                    }

                }else  {

                    Toast.makeText(getApplicationContext(),"No se puede eliminar el usuario Administrador",Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton(R.string.canselar,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


            }
        });
        builder.create().show();

    }

    //ELIMINAR UN USUARIO DESDE EL WEB SERVISE.
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
                httppost.setHeader("Authorization",SharedPrefManager.getInstance(Editar_Usuarios.this).getUSUARIO_LOGUEADO().getToken());
                parametros.add(new BasicNameValuePair("usuario",String.valueOf(usuarioEditar)));
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

            if (resul) {

                Toast.makeText(getApplicationContext(),"Usuario Eliminado",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                setResult(Mostrar_Usuarios.RESULT_OK,intent);
                finish();

            }else {
                Toast.makeText(getApplicationContext(), "Problemas de conexión", Toast.LENGTH_SHORT).show();
            }
        }


    }



    private void validar(){


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
                httppost.setHeader("Authorization",SharedPrefManager.getInstance(Editar_Usuarios.this).getUSUARIO_LOGUEADO().getToken());
                parametros = new ArrayList<NameValuePair>();
                parametros.add(new BasicNameValuePair("usuario",String.valueOf(usuarioEditar)));
                parametros.add(new BasicNameValuePair("usuarionombre",nombreusuario.getText().toString()));
                parametros.add(new BasicNameValuePair("usuariopropio",nombrepropio.getText().toString()));
                parametros.add(new BasicNameValuePair("usuarioemail",correo.getText().toString()));

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
                    Intent intent = new Intent();
                    setResult(Mostrar_Usuarios.RESULT_OK,intent);
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
                httppost = new HttpPost(ip.getIp()+"obtenerUsuario");
                httppost.setHeader("Authorization",SharedPrefManager.getInstance(Editar_Usuarios.this).getUSUARIO_LOGUEADO().getToken());
                parametros = new ArrayList<NameValuePair>();
                parametros.add(new BasicNameValuePair("usuario",String.valueOf(usuarioEditar) ));
                httppost.setEntity(new UrlEncodedFormEntity(parametros, "UTF-8"));

                JSONObject respJSON = new JSONObject(EntityUtils.toString(( httpclient.execute(httppost)).getEntity()));
                JSONArray jsonArray = respJSON.getJSONArray("content");

                for (int i = 0; i < jsonArray.length(); i++) {
                    nombre_usuario = jsonArray.getJSONObject(i).getString("nombre_usuario");
                    nombre_propio = jsonArray.getJSONObject(i).getString("nombre_propio");
                    correoo = jsonArray.getJSONObject(i).getString("correo");
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
                correo.setText(correoo);
            }else {
                Toast.makeText(getApplicationContext(), "Problemas de conexión", Toast.LENGTH_SHORT).show();
            }
        }


    }

}

