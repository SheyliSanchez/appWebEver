package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.HttpGet;

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
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVASheyli.ipLocalhost;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.FormularioRegistroLogin;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.Login;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.SharedPrefManager;

/**
 * Created by alan fabricio on 14/03/2018.
 */

public class Mostrar_Usuarios extends AppCompatActivity {

    ArrayList<Fuente_mostrarUsuarios> mostrar_usuarios;
    private ListView lista;
    private int usuarioselecionado = -1;
    int id_usuario;
    String nombre_usuario,descripcion_rol;
    Adaptador_mostrarusuarios adaptador;
    ProgressBar barra;

    ipLocalhost ip = new ipLocalhost();

    public  void onCreate(Bundle b){
        super.onCreate(b);

        mostrar_usuarios= new ArrayList<Fuente_mostrarUsuarios>();

        setContentView(R.layout.mostrar_usuario);


       new ArrayList<>();
        //flecha atras
        android.support.v7.app.ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //fin de flecha
        lista=(ListView)findViewById(R.id.idmostrar_usuario);
        barra = findViewById(R.id.progressBarUsuarios);


        //LLENAMOS LA LISTA QUEVIENE DESDE EL SERVIDOR.
        new llenarLista().execute();
       // llenarLista();
        onclick();

        FloatingActionButton floatingActionButton = (FloatingActionButton)findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Mostrar_Usuarios.this, FormularioRegistroLogin.class);
                startActivityForResult(intent,600);

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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( resultCode == RESULT_OK){
            mostrar_usuarios.clear();
            new llenarLista().execute();
            adaptador.notifyDataSetChanged();
        }
    }
    public void onclick(){
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                usuarioselecionado = position;
                Fuente_mostrarUsuarios usu = mostrar_usuarios.get(usuarioselecionado);
                Intent in = new Intent(Mostrar_Usuarios.this,Editar_Usuarios.class);
                in.putExtra("id",usu.getId());
                startActivityForResult(in, new Integer(100));
            }
        });
    }



    //LLENAR EL LIST VIEW DESDE EL WEB SERVER.
    private class llenarLista extends AsyncTask<String, Integer, Boolean> {
        private llenarLista(){}
        boolean resul = true;

        @Override
        protected Boolean doInBackground(String... strings) {

            try {
                HttpClient httpclient;
                HttpPost httppost;
                ArrayList<NameValuePair> parametros=new ArrayList<NameValuePair>();
                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(ip.getIp()+"todosUsuarios");
                parametros.add(new BasicNameValuePair("Authorization",SharedPrefManager.getInstance(Mostrar_Usuarios.this).getUSUARIO_LOGUEADO().getToken()));
                httppost.setEntity(new UrlEncodedFormEntity(parametros, "UTF-8"));
                JSONObject jsonObject = new JSONObject(EntityUtils.toString(httpclient.execute(httppost).getEntity()));
                JSONArray jsonArray = jsonObject.getJSONArray("content");

                for (int i = 0; i < jsonArray.length(); i++) {
                    id_usuario = jsonArray.getJSONObject(i).getInt("id_usuario");
                    nombre_usuario = jsonArray.getJSONObject(i).getString("nombre_usuario");
                    descripcion_rol = jsonArray.getJSONObject(i).getString("descripcion_rol");

                    mostrar_usuarios.add(new Fuente_mostrarUsuarios(id_usuario, nombre_usuario,descripcion_rol));

                }
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }
            return Boolean.valueOf(resul);

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            barra.setProgress(values[0]);
        }

        protected void onPostExecute(Boolean result) {

            if (result.booleanValue()) {
                barra.setVisibility(View.INVISIBLE);
                adaptador = new Adaptador_mostrarusuarios( mostrar_usuarios,getApplicationContext());
                lista.setAdapter(adaptador);
                return;
            }
            Toast.makeText(getApplicationContext(), "Problemas de conexión", Toast.LENGTH_SHORT).show();
        }


    }
}

