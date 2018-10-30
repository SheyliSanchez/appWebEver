package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;

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

import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.Mostrar_Usuarios;

public class FormularioRegistroLogin extends AppCompatActivity {
    private EditText id,nombrepropio_isertar,nombreusuario_insertar,correo_insertar,contrasena_insertar,contrasenarepetir,rol_insertar,estado_del_usuario;
    String nombreusuariobar;
    String nombrepropiobar;
    String correobar;
    String contrasenabar;
    String contrasenarepetirbar;

    int id_rol;
    private Spinner  tipousu;
    private AsyncHttpClient cliente;
    Button bottonvalidar;
    ArrayList lista = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_registro_login);


        //flecha atras
        android.support.v7.app.ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //fin de flecha

        bottonvalidar = (Button)findViewById(R.id.registrar_usuario);
        bottonvalidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //EJECUTAMOS LAS CLASE DE INSERCION DE UN USUARIO.
                validar();
                if (nombreusuario_insertar.getError()==null && nombrepropio_isertar.getError()==null && correo_insertar.getError()==null && contrasena_insertar.getError()==null && contrasenarepetir.getError()==null){


                    new insertarUsuarios().execute();
               // validar();
                }
            }
        });

        tipousu=(Spinner)findViewById(R.id.tipousuario);
        nombrepropio_isertar = (EditText) findViewById(R.id.txtnombre_registro_login);
        nombreusuario_insertar = (EditText) findViewById(R.id.txtnombreUsuario_registro_login);
        correo_insertar = (EditText) findViewById(R.id.txtcorreo_registro_login);
        contrasena_insertar = (EditText) findViewById(R.id.txtcontrasena_registro_login);
        contrasenarepetir = (EditText) findViewById(R.id.txtcontrasenarepetir_registro_login);
        rol_insertar = (EditText) findViewById(R.id.txtrol_registro_login);
        estado_del_usuario = (EditText) findViewById(R.id.txtestado_registro_login);




//EJECUTAMOS LA CLASE DE LLENADO DE EL SPINNER
     new llenarEspinner().execute();
     tipousu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
         @Override
         public void onItemSelected(AdapterView<?> adapterView, View view, int posicion, long l) {
             switch (posicion) {
                 case 0:
                     id_rol =1;
                     break;
                 case  1:
                     id_rol = 2;
                     break;

             }
         }

         @Override
         public void onNothingSelected(AdapterView<?> adapterView) {

         }
     });

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

    // public void registrar_usuario_login (View v){
    private void validar(){
        //id.setError(null);
        nombrepropio_isertar.setError(null);
        nombreusuario_insertar.setError(null);
        correo_insertar.setError(null);
        contrasena_insertar.setError(null);
        contrasenarepetir.setError(null);



//VARIABLES QUE SE USAN EN LA CONEXION DE LA BASE DE DATOS LOCAL.
        // String idd = id.getText().toString();
        String nombusus = nombrepropio_isertar.getText().toString();
        String nomb = nombreusuario_insertar.getText().toString();
        String cor = correo_insertar.getText().toString();
        String cont = contrasena_insertar.getText().toString();
        String contr = contrasenarepetir.getText().toString();

        


//server
//RECIVIMOS LOS USUARIOS QUE VIENEN DEL SERVIDOR Y LOS ADAPTAMOS ALOS COMPONENTES VISIALUES
        nombreusuariobar=nombreusuario_insertar.getText().toString();
        nombrepropiobar=nombrepropio_isertar.getText().toString();
        correobar=correo_insertar.getText().toString();
        contrasenabar=contrasena_insertar.getText().toString();
        contrasenarepetirbar=contrasenarepetir.getText().toString();


        if(TextUtils.isEmpty(nombusus) || nombusus.startsWith(" ")){
            nombrepropio_isertar.setError(getString(R.string.error_nombre));
            nombrepropio_isertar.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(nomb) || nomb.startsWith(" ")){
            nombreusuario_insertar.setError(getString(R.string.error_nombre_usuario));
            nombreusuario_insertar.requestFocus();
            return;

        }if(TextUtils.isEmpty(cor) || cor.startsWith(" ")){
            correo_insertar.setError(getString(R.string.error_correo));
            correo_insertar.requestFocus();
            return;
        }else{
            if(!correo_insertar.getText().toString().contains("@") || !correo_insertar.getText().toString().contains(".")){
                correo_insertar.setError(getString(R.string.error_mailnovalido));
                correo_insertar.requestFocus();
                return;
            }

        }

        if(TextUtils.isEmpty(cont) || cont.startsWith(" ")){
            contrasena_insertar.setError(getString(R.string.error_contrasena));
            contrasena_insertar.requestFocus();
            return;
        }else{
            if(!TextUtils.isEmpty(contr) && !contr.equals(cont)){
                contrasenarepetir.setError(getString(R.string.error_contrasenavalidada));
                contrasenarepetir.requestFocus();
                return;
            }
        }

        if(TextUtils.isEmpty(contr) || contr.startsWith(" ")){
            contrasenarepetir.setError(getString(R.string.error_contrasena1));
            contrasenarepetir.requestFocus();
            return;
        }else{
            if(!TextUtils.isEmpty(cont) && !cont.equals(contr)){
                contrasenarepetir.setError(getString(R.string.error_contrasenavalidada));
                contrasenarepetir.requestFocus();
                return;
            }
        }

    }







    //METODO PARA LLENAR EL ESPINNER DESDE EL WEB SERVER
    private class llenarEspinner extends AsyncTask<String, Integer, Boolean> {
        private llenarEspinner(){}
        boolean resul = true;

        @Override
        protected Boolean doInBackground(String... strings) {

            try {
                JSONArray respJSON = new JSONArray(EntityUtils.toString(new DefaultHttpClient().execute(new HttpPost("http://aeo.web-hn.com/WebServices/consultar_los_roles.php")).getEntity()));

                for (int i = 0; i < respJSON.length(); i++) {
                    String roles ;
                    roles=respJSON.getJSONObject(i).getString("descripcion_rol");

                    lista.add(roles);
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
                ArrayAdapter a = new ArrayAdapter<>(getApplicationContext(),R.layout.adaptacion_spinner,lista);
                tipousu.setAdapter(a);

            }else {
                Toast.makeText(getApplicationContext(), "Problemas de conexión", Toast.LENGTH_SHORT).show();
            }
        }


    }

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
                httppost = new HttpPost("http://aeo.web-hn.com/WebServices/insercion_de_usuario.php");
                parametros = new ArrayList<NameValuePair>();
                parametros.add(new BasicNameValuePair("usuarionombre",nombreusuario_insertar.getText().toString()));
                parametros.add(new BasicNameValuePair("usuariopropio",nombrepropio_isertar.getText().toString()));
                parametros.add(new BasicNameValuePair("usuarioemail",correo_insertar.getText().toString()));
                parametros.add(new BasicNameValuePair("usariopassword",contrasena_insertar.getText().toString()));
                parametros.add(new BasicNameValuePair("usuariosroles",String.valueOf(id_rol)));
                parametros.add(new BasicNameValuePair("tkn",SharedPrefManager.getInstance(FormularioRegistroLogin.this).getUSUARIO_LOGUEADO().getToken()));


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
                //VALIDACION DE QUE LOS CAMPOS NO ESTEN VACIOS ANTES DE INGRESAR UN USUARIO
                   Toast.makeText(getApplicationContext(),"Usuario agregado Correctamente",Toast.LENGTH_SHORT).show();

                    Intent data = new Intent();
                    setResult(Mostrar_Usuarios.RESULT_OK, data);
                    finish();




            }else {
                Toast.makeText(getApplicationContext(), "Problemas de conexión", Toast.LENGTH_SHORT).show();
            }
        }



}

 }







