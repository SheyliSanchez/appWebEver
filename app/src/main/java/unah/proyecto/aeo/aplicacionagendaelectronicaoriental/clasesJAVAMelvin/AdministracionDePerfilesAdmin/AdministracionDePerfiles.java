package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin.AdministracionDePerfilesAdmin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
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
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.Editar_Usuarios;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.Panel_de_Control;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVASheyli.FuncionCerrarSesion;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVASheyli.ipLocalhost;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.AcercaDe;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.FormularioRegistroLogin;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.Login;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.PanelDeControlUsuarios;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.SharedPrefManager;

public class AdministracionDePerfiles extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //Declaración de atributos
    ArrayList<Fuente_mostrarPerfiles> mostrar_perfiles;
    private ListView lista;
    private int perfilselecionado = -1;
    int id_contacto;
    String nombre_organizacion, imagen, usuariopropietario;
    public AdaptadorMostrarPerfiles adaptadorMostrarPerfiles;
    private  Object mActionMode;
    int idperf;
    ProgressBar barra;
    FloatingActionButton botonNuevoPerfil;
    int id_usuario_resibido_usuario;
    int id_usu=-1;
    private static final int PASAR_A_EDITAR = 100;
    private static final int PASAR_A_NUEVO = 200;
    public static Handler h;

    ipLocalhost ip = new ipLocalhost();
    FuncionCerrarSesion cs = new FuncionCerrarSesion();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administracion_de_perfiles);


        //inicialización de componentes gráficos
        barra = findViewById(R.id.progressBarPerfiles);
        lista = (ListView) findViewById(R.id.listviewperfiles);
        mostrar_perfiles= new ArrayList<Fuente_mostrarPerfiles>();

        h = new Handler() {

            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch(msg.what) {

                    case 0:
                        finish();
                        break;

                }
            }

        };

        //Ejecución de la clase AsyncTask llenarLista
        new llenarLista().execute();

        //Se establece el listener al mantener presionado un item del listview
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //se inicializa la variable con la posición del item seleccionado del listview
                perfilselecionado = position;
                Fuente_mostrarPerfiles per = mostrar_perfiles.get(perfilselecionado);
                Intent in = new Intent(getApplicationContext(), EditarPerfil.class);
                in.putExtra("id",per.getId());
                startActivityForResult(in,PASAR_A_EDITAR);
            }
        });


        //se establece el listener del boton agregar perfil
        botonNuevoPerfil = (FloatingActionButton) findViewById(R.id.botonNuevoPerfil);
        botonNuevoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent i= new Intent(getApplicationContext(), NuevoPerfil.class);
                startActivityForResult(i,PASAR_A_NUEVO);
            }
        });

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
    protected void onStart() {
        super.onStart();
        if (SharedPrefManager.getInstance(this).estaLogueado()){


        }else{
            startActivity(new Intent(this, Login.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK)) ;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PASAR_A_EDITAR){
            if(resultCode==RESULT_OK){
                if(data.getExtras().getString("msg").equals("update")){
                    Toast.makeText(getApplicationContext(),"Perfil actualizado correctamente",Toast.LENGTH_SHORT).show();
                }else if(data.getExtras().getString("msg").equals("delete")){
                    Toast.makeText(getApplicationContext(),"Perfil eliminado correctamente",Toast.LENGTH_SHORT).show();
                }

                mostrar_perfiles.clear();
                new llenarLista().execute();
                adaptadorMostrarPerfiles.notifyDataSetChanged();
            }else if(resultCode == RESULT_CANCELED){
                Toast.makeText(getApplicationContext(),data.getExtras().getString("msg"),Toast.LENGTH_SHORT).show();
                    cs.cerrarsesion();

                    SharedPrefManager.getInstance(getApplicationContext()).limpiar();
                    startActivity(new Intent(AdministracionDePerfiles.this, Login.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK));

            }

        }else if(requestCode == PASAR_A_NUEVO ){
            if(resultCode==RESULT_OK){
                mostrar_perfiles.clear();
                new llenarLista().execute();
                adaptadorMostrarPerfiles.notifyDataSetChanged();
            }else if(resultCode==RESULT_CANCELED){
                Toast.makeText(getApplicationContext(),data.getExtras().getString("msg"),Toast.LENGTH_SHORT).show();
                cs.cerrarsesion();

                SharedPrefManager.getInstance(getApplicationContext()).limpiar();
                startActivity(new Intent(AdministracionDePerfiles.this, Login.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }

        }
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
            //envia intent ala actividad editar perfil
            if (getIntent().getExtras()!=null){
                id_usuario_resibido_usuario  = getIntent().getExtras().getInt("usuario_ingreso");
                i.putExtra("id",id_usuario_resibido_usuario);
            }
            startActivity(i);
        } else if (id == R.id.menusolicitudesrechazadas) {
            Intent i = new Intent(getApplicationContext(),SolicitudesRechazadas.class);
            //envia intent ala actividad editar perfil
            if (getIntent().getExtras()!=null){
                id_usuario_resibido_usuario  = getIntent().getExtras().getInt("usuario_ingreso");
                i.putExtra("id",id_usuario_resibido_usuario);
            }
            startActivity(i);
        } else if (id == R.id.menuperfileliminados) {
            Intent i = new Intent(getApplicationContext(),PerfilesEliminados.class);
            //envia intent ala actividad editar perfil
            if (getIntent().getExtras()!=null){
                id_usuario_resibido_usuario  = getIntent().getExtras().getInt("usuario_ingreso");
                i.putExtra("id",id_usuario_resibido_usuario);
            }
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
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

        }else if (id == R.id.cerrarsecion){
            cs.cerrarsesion();
            SharedPrefManager.getInstance(AdministracionDePerfiles.this).limpiar();
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


    //clase que se conecta al webservice y trae los registros solicitados
    private class llenarLista extends AsyncTask<String, Integer, Integer> {
        private llenarLista(){}
        //variable booleana para controlar el resultado de las ejecuciones
        int ResponseEstado;

        @Override
        protected Integer doInBackground(String... strings) {

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(ip.getIp()+"listarPerfiles?ste=2");
                HttpResponse httpResponse=httpClient.execute(httpGet);
                ResponseEstado = httpResponse.getStatusLine().getStatusCode();

                JSONObject jsonObject = new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
                JSONArray respJSON = jsonObject.getJSONArray("content");
                //recorre el array para asignar los resultados a las variables
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
            //muestra el progreso en la barra de progreso
            barra.setProgress(values[0]);

        }

        protected void onPostExecute(Integer responseEstado) {

            if (responseEstado==200) {
                //hace invisible la barra
                barra.setVisibility(View.INVISIBLE);
                //inicializa el adaptador
                adaptadorMostrarPerfiles = new AdaptadorMostrarPerfiles( mostrar_perfiles,getApplicationContext());
                //establece el adaptador al listview
                lista.setAdapter(adaptadorMostrarPerfiles);
                return;
            }else if(responseEstado==500){
                Toast.makeText(getApplicationContext(),"Ocurrió un error en la base de datos",Toast.LENGTH_SHORT).show();
            }else if(responseEstado==401){
                Toast.makeText(getApplicationContext(), "Token de autenticación inválido o expirado, por favor inicie sesión nuevamente", Toast.LENGTH_SHORT).show();
                cs.cerrarsesion();
                SharedPrefManager.getInstance(getApplicationContext()).limpiar();
                startActivity(new Intent(AdministracionDePerfiles.this, Login.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }else{
                //muestra mensaje si se produce un error al ejercutar la consulta al webservice
                Toast.makeText(getApplicationContext(), "Problemas de conexión", Toast.LENGTH_SHORT).show();
            }

        }


    }

    //metodo para eliminar un perfil

    public void removerperfil(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar Perfil");
        String fmt= getResources().getString(R.string.eliminarPerfil);
        builder.setMessage(String.format(fmt,mostrar_perfiles.get(pos).getPerfil()));
        builder.setPositiveButton(R.string.eliminar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //  llama a la clase que borra el perfil de la base de datos remota


                new eliminarPerfil().execute();

            }
        });

        builder.setNegativeButton(R.string.canselar,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


            }
        });
        builder.create().show();

    }

    //clase AsyncTask que se conecta al webservice que ejecuta la consulta para borrar el perfil

    private class eliminarPerfil extends AsyncTask<String, Integer, Boolean> {
        private eliminarPerfil(){}
        boolean resul = true;

        @Override
        protected Boolean doInBackground(String... strings) {

            try {
                //Se obtiene el id del perfil que se va a eliminar
                Fuente_mostrarPerfiles perf = mostrar_perfiles.get(perfilselecionado);
                idperf=perf.getId();
                //se ejecuta la consulta al webservice y se pasa el id del perfil seleccionado

                HttpClient httpclient;
                HttpPost httppost;
                ArrayList<NameValuePair> parametros;
                httpclient = new DefaultHttpClient();
                httppost = new HttpPost("http://aeo.web-hn.com/WebServices/eliminarPerfil.php");
                parametros = new ArrayList<NameValuePair>();
                parametros.add(new BasicNameValuePair("cto",String.valueOf(idperf)) );
                //parametros.add(new BasicNameValuePair("tkn",SharedPrefManager.getInstance(AdministracionDePerfiles.this).getUSUARIO_LOGUEADO().getToken()));
                httppost.setEntity(new UrlEncodedFormEntity(parametros, "UTF-8"));
                httpclient.execute(httppost);

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
                Toast.makeText(getApplicationContext(),"Perfil Eliminado",Toast.LENGTH_SHORT).show();
                mostrar_perfiles.removeAll(mostrar_perfiles);
                new llenarLista().execute();
                adaptadorMostrarPerfiles.notifyDataSetChanged();
            }else {
                Toast.makeText(getApplicationContext(), "Problemas de conexión", Toast.LENGTH_SHORT).show();
            }
        }


    }
}