package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

public class FormularioRegistroUsuario extends AppCompatActivity {
    private EditText nombrepropio_isertar_usuario,nombreusuario_insertar_usuario,correo_insertar_usario,contrasena_insertar_usuario,confirmarcontrasena;
    String nombreusuariobar_usuario,nombrepropiobar_usuario,contrasenabar_usuario,correobar,respuesta1bar_usuario,respuesta2bar_usuario,respuesta3bar_usuario;
    int rol_insertar_usuario,estado_insertar_usuario;
    private Button insertar_usuario;
    private EditText respuesta1,respuesta2,respuesta3;
    String correoIgual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_registro_usuario);

        nombreusuario_insertar_usuario = (EditText) findViewById(R.id.txtnombreUsuario_registro_login_usuario);
        nombrepropio_isertar_usuario = (EditText) findViewById(R.id.txtnombre_registro_login_usuario);
        contrasena_insertar_usuario = (EditText) findViewById(R.id.txtcontrasena_registro_login_usuario);
        confirmarcontrasena = (EditText) findViewById(R.id.txtcontrasenarecuperacion_registro_login_usuario);

        correo_insertar_usario=(EditText) findViewById(R.id.txtcorreo_registro_login_usuario);
        rol_insertar_usuario=2;


        insertar_usuario = (Button) findViewById(R.id.registrar_usuario_usuario);
        insertar_usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //EJECUTAMOS LAS CLASE DE INSERCION DE UN USUARIO.
                validar();
                if (nombreusuario_insertar_usuario.getError()==null && nombrepropio_isertar_usuario.getError()==null &&correo_insertar_usario.getError()==null && contrasena_insertar_usuario.getError()==null && confirmarcontrasena.getError()==null){

                    new insertarUsuarios().execute();

                }
            }
        });
    }

    // public void registrar_usuario_login (View v){
    private void validar(){
        //id.setError(null);
        nombrepropio_isertar_usuario.setError(null);
        nombreusuario_insertar_usuario.setError(null);
        contrasena_insertar_usuario.setError(null);
        confirmarcontrasena.setError(null);
        correo_insertar_usario.setError(null);


//VARIABLES QUE SE USAN EN LA CONEXION DE LA BASE DE DATOS LOCAL.
        // String idd = id.getText().toString();
        String nombusus = nombrepropio_isertar_usuario.getText().toString();
        String nomb = nombreusuario_insertar_usuario.getText().toString();
        String cont = contrasena_insertar_usuario.getText().toString();
        String cor = correo_insertar_usario.getText().toString();
        String contr = confirmarcontrasena.getText().toString();



        if(TextUtils.isEmpty(nombusus) || nombusus.startsWith(" ")){
            nombrepropio_isertar_usuario.setError(getString(R.string.error_nombre));
            nombrepropio_isertar_usuario.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(nomb) || nomb.startsWith(" ")){
            nombreusuario_insertar_usuario.setError(getString(R.string.error_nombre_usuario));
            nombreusuario_insertar_usuario.requestFocus();
            return;

        }if(TextUtils.isEmpty(cor) || cor.startsWith(" ")){
            correo_insertar_usario.setError(getString(R.string.error_correo));
            correo_insertar_usario.requestFocus();
            return;
        }else{
            if(!correo_insertar_usario.getText().toString().contains("@") || !correo_insertar_usario.getText().toString().contains(".")){
                correo_insertar_usario.setError(getString(R.string.error_mailnovalido));
                correo_insertar_usario.requestFocus();
                return;
            }

        }

        if(TextUtils.isEmpty(cont) || cont.startsWith(" ")){
            contrasena_insertar_usuario.setError(getString(R.string.error_contrasena));
            contrasena_insertar_usuario.requestFocus();
            return;
        }else{
            if(!TextUtils.isEmpty(contr) && !contr.equals(cont)){
                confirmarcontrasena.setError(getString(R.string.error_contrasenavalidada));
                confirmarcontrasena.requestFocus();
                return;
            }
        }

        if(TextUtils.isEmpty(contr) || contr.startsWith(" ")){
            confirmarcontrasena.setError(getString(R.string.error_contrasena1));
            confirmarcontrasena.requestFocus();
            return;
        }else{
            if(!TextUtils.isEmpty(cont) && !cont.equals(contr)){
                confirmarcontrasena.setError(getString(R.string.error_contrasenavalidada));
                confirmarcontrasena.requestFocus();
                return;
            }
        }

    }

    //METODO PARA INSERTAR USUARIOS DIRECTAMENTE DESDE EL SERVER.
    private class insertarUsuarios extends AsyncTask<String, Integer, Boolean> {
        private insertarUsuarios(){}

        boolean resul = true;

        @Override
        protected Boolean doInBackground(String... strings) {

            try {
                HttpClient httpclient;
                HttpPost httppost;
                ArrayList<NameValuePair> parametros;
                httpclient = new DefaultHttpClient();
                httppost = new HttpPost("http://aeo.web-hn.com/WebServices/insertarUsuarioCliente.php");
                parametros = new ArrayList<NameValuePair>();
                parametros.add(new BasicNameValuePair("usuarionombre",nombreusuario_insertar_usuario.getText().toString()));
                parametros.add(new BasicNameValuePair("usuariopropio",nombrepropio_isertar_usuario.getText().toString()));
                parametros.add(new BasicNameValuePair("usuarioemail",correo_insertar_usario.getText().toString()));
                parametros.add(new BasicNameValuePair("usariopassword",contrasena_insertar_usuario.getText().toString()));


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
                //VALIDACION DE QUE LOS CAMPOS NO ESTEN VACIOS ANTES DE INGRESAR UN USUARIO

                    Toast.makeText(getApplicationContext(),"Usuario agregado Correctamente",Toast.LENGTH_SHORT).show();
                    finish();



            }else {
                Toast.makeText(getApplicationContext(), "Problemas de conexión", Toast.LENGTH_SHORT).show();
            }
        }


    }

    //valida que el correo sea diferente

    private class validarCorreoDiferente extends AsyncTask<String, Integer, Boolean> {
        private validarCorreoDiferente() {
        }

        boolean resul = true;

        @Override
        protected Boolean doInBackground(String... strings) {
            String correoTraido;

            try {
                // Parseamos la respuesta obtenida del servidor a un objeto JSON
                JSONObject jsonObject = new JSONObject(EntityUtils.toString(new DefaultHttpClient().execute(new HttpPost("http://aeo.web-hn.com/WebServices/verCorreoOrganizacion.php?id_correo="+correo_insertar_usario.getText().toString())).getEntity()));
                JSONArray jsonArray = jsonObject.getJSONArray("datos");
                for (int i = 0; i < jsonArray.length(); i++) {

                    correoTraido = jsonArray.getJSONObject(i).getString("e_mail");
                    if (jsonArray.getJSONObject(i).getString("e_mail").isEmpty()) {


                        if (correoTraido.equals(correo_insertar_usario.getText().toString())) {
                            correoIgual = "igual";
                        } else {
                            correoIgual = "diferente";

                        }
                    }else {
                        correoIgual = "diferente";
                    }



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
                if (correoIgual.equals("igual")){
                    correo_insertar_usario.setError(null);
                    contrasena_insertar_usuario.setError("correo ya existe");
                    correo_insertar_usario.requestFocus();
                }else {

                    validar();

                    if (nombreusuario_insertar_usuario.getError()==null && nombrepropio_isertar_usuario.getError()==null && contrasena_insertar_usuario.getError()==null && respuesta1.getError()==null && respuesta2.getError()==null && respuesta3.getError()==null){

                    }
                }

            }else {
                Toast.makeText(getApplicationContext(), "Problemas de conexión", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

}
