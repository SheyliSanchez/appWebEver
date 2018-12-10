package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin.AdministracionDePerfilesAdmin;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Base64;

import com.bumptech.glide.Glide;

//import org.apache.http.entity.mime.MultipartEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.mime.HttpMultipartMode;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;
import cz.msebera.android.httpclient.entity.mime.content.FileBody;
import cz.msebera.android.httpclient.entity.mime.content.StringBody;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;
import de.hdodenhof.circleimageview.CircleImageView;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVABessy.Ingresar_Ubicacion;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVASheyli.FuncionCerrarSesion;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVASheyli.ipLocalhost;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.Login;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.SharedPrefManager;

public class NuevoPerfil extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1 ;
    CircleImageView imagenOrg;
    Bitmap imagenBitmap;
    FloatingActionButton botonGuardar;
    FloatingActionButton botonFoto;
    TextInputEditText etnombreeorganizacion, etnumerofijo, etnumerocel, etdireccion, etemail, etdescripcion;
    TextView etlatitud, etlongitud;
    Spinner spcategorias, spregiones, spusuario;
    ArrayList<ModeloSpinner> listaCategorias, listaRegiones, listaUsuarios;
    boolean editarFoto = false;

    int id_categoria, id_region, id_usuario;

    private static final int PICK_IMAGE = 100;
    Uri imageUri;

    String encodeImagen;


    ipLocalhost ip = new ipLocalhost();
    FuncionCerrarSesion cs = new FuncionCerrarSesion();
    //


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        //
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        botonFoto = findViewById(R.id.botonFoto);
        imagenOrg = findViewById(R.id.imagenDeOrganizacion);
        botonGuardar= findViewById(R.id.botonGuardar);
        etnombreeorganizacion = findViewById(R.id.etnombreeorganizacion);
        etnumerofijo = findViewById(R.id.etnumerofijo);
        etnumerocel = findViewById(R.id.etnumerocel);
        etdireccion = findViewById(R.id.etdireccion);
        etemail = findViewById(R.id.etemail);
        etdescripcion = findViewById(R.id.etdescripcion);
        etlatitud = findViewById(R.id.etlatitud);
        etlongitud = findViewById(R.id.etlongitud);

        spcategorias = findViewById(R.id.spinercategoriaPerfil);
        spregiones = findViewById(R.id.spinerregionPerfil);
        spusuario= findViewById(R.id.spinerusuariosPerfil);
        spusuario.setVisibility(View.VISIBLE);
        TextView titleUsuario = findViewById(R.id.tvus);
        titleUsuario.setVisibility(View.VISIBLE);

        listaCategorias=new ArrayList<ModeloSpinner>();
        listaRegiones=new ArrayList<ModeloSpinner>();
        listaUsuarios=new ArrayList<ModeloSpinner>();


        new llenarSpinnersNuevoPerfil().execute();

        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCheck1 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {


        }


        spcategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_categoria = listaCategorias.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spregiones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_region = listaRegiones.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spusuario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_usuario = listaUsuarios.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        botonFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarFoto=true;
                requestRead();
            }
        });

        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validar();
                if (etnombreeorganizacion.getError()==null &&
                        etnumerofijo.getError()==null &&
                        etnumerocel.getError()==null &&
                        etdireccion.getError()==null &&
                        etemail.getError()==null &&
                        etdescripcion.getError()==null &&
                        etlatitud.getError()==null &&
                        etlongitud.getError()==null) {
                    botonGuardar.setClickable(false);
                    Toast.makeText(getApplicationContext(),"Procesando... Espere",Toast.LENGTH_SHORT).show();
                    new crearPerfil().execute();
                }


            }
        });


    }

    /**********************************************************************************************
     *                         MÉTODO PARA RECORTAR PESO DE LA IMAGEN SELECCIONADA
     **********************************************************************************************/
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public  void  guardarUbicacionOrganizacion(View view){

        Intent ubicacion1 = new Intent(getApplicationContext(),Ingresar_Ubicacion.class);
        startActivityForResult(ubicacion1,1);
    }

    public void requestRead() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            openGallery();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                // Permission Denied
                Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void openGallery(){
        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(gallery,  "Seleccione una imagen"),PICK_IMAGE);
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
    /**********************************************************************************************
     *            MÉTODO QUE EJECUTA CCIONES A PARTIR DE RESULTADOS DE ACTIVITIES
     **********************************************************************************************/
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE && data.getData()!=null){

            try {
                imageUri = data.getData();
                imagenBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                imagenOrg.setImageBitmap(imagenBitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                String latitud = data.getStringExtra("latitud");
                String longitud = data.getStringExtra("longitud");
                etlatitud.setText(latitud);
                etlongitud.setText(longitud);

            }
        }
    }
    private String getPath(Uri uri){
        Cursor cursor = getContentResolver().query(uri,null,null,null,null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id=document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();

        cursor=getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID+" = ?", new String[]{document_id},null
        );
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }

    private void validar(){
        //id.setError(null);
        etnombreeorganizacion.setError(null);
        etnumerofijo.setError(null);
        etnumerocel.setError(null);
        etdireccion.setError(null);
        etemail.setError(null);
        etdescripcion.setError(null);
        etlatitud.setError(null);
        etlongitud.setError(null);

        // String idd = id.getText().toString();
        String nomborg = etnombreeorganizacion.getText().toString();
        String numtel = etnumerofijo.getText().toString();
        String numcel = etnumerocel.getText().toString();
        String direccion = etdireccion.getText().toString();
        String desc = etdescripcion.getText().toString();
        String lati = etlatitud.getText().toString();
        String longitud = etlongitud.getText().toString();
        String mail = etemail.getText().toString();

        if(TextUtils.isEmpty(mail)){

        }else{
            if(!mail.contains("@") || !mail.contains(".")){
                etemail.setError(getString(R.string.error_mailnovalido));
                etemail.requestFocus();
                return;
            }
        }

        if(TextUtils.isEmpty(nomborg) || nomborg.startsWith(" ")){
            etnombreeorganizacion.setError(getString(R.string.errNombreOrg));
            etnombreeorganizacion.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(numtel)){
            if(TextUtils.isEmpty(numcel)){
                etnumerofijo.setError(getString(R.string.errNumero));
                etnumerofijo.requestFocus();
                return;
            }
        }else{
            if(numtel.length()<8 || !numtel.startsWith("2") || numtel.length()>8){
                etnumerofijo.setError(getString(R.string.error_numnovalido));
                etnumerofijo.requestFocus();
                return;
            }
        }

        if(TextUtils.isEmpty(numcel)){
            if(TextUtils.isEmpty(numtel)){
                etnumerocel.setError(getString(R.string.errNumero));
                etnumerocel.requestFocus();
                return;
            }
        }else{
            if(numcel.length()<8 || numcel.length()>8){
                etnumerocel.setError(getString(R.string.error_numnovalido));
                etnumerocel.requestFocus();
                return;
            }
        }

        if(TextUtils.isEmpty(direccion) || direccion.startsWith(" ")){
            etdireccion.setError(getString(R.string.errDir));
            etdireccion.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(desc) || desc.startsWith(" ")){
            etdescripcion.setError(getString(R.string.errDesc));
            etdescripcion.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(lati)){
            etlatitud.setError(getString(R.string.errLat));
            etlatitud.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(longitud)){
            etlongitud.setError(getString(R.string.errLong));
            etlongitud.requestFocus();
            return;
        }

    }

    private class llenarSpinnersNuevoPerfil extends AsyncTask<String, Integer, Integer> {
        private llenarSpinnersNuevoPerfil(){}
        int resul;

        @Override
        protected Integer doInBackground(String... strings) {

            try {
                HttpClient httpclient;
                HttpClient httpclient1;
                HttpClient httpclient2;
                HttpPost httppost;
                HttpGet regiones= new HttpGet(ip.getIp()+"regiones");
                HttpGet categorias= new HttpGet(ip.getIp()+"categorias");
                ArrayList<NameValuePair> parametros;
                httpclient = new DefaultHttpClient();
                httpclient1 = new DefaultHttpClient();
                httpclient2 = new DefaultHttpClient();
                httppost = new HttpPost(ip.getIp()+"todosUsuarios");
                parametros = new ArrayList<NameValuePair>();
                parametros.add(new BasicNameValuePair("ste","1"));
                parametros.add(new BasicNameValuePair("Authorization",SharedPrefManager.getInstance(NuevoPerfil.this).getUSUARIO_LOGUEADO().getToken()));

                httppost.setEntity(new UrlEncodedFormEntity(parametros, "UTF-8"));

                HttpResponse regionesResp=httpclient.execute(regiones);
                HttpResponse categoriasResp=httpclient1.execute(categorias);
                HttpResponse usuariosResp=httpclient2.execute(httppost);

                if(regionesResp.getStatusLine().getStatusCode()==200) {
                    JSONObject regionesWS = new JSONObject(EntityUtils.toString(regionesResp.getEntity()));
                    JSONArray jsonArrayRegion = regionesWS.getJSONArray("content");
                    //array regiones
                    for (int i = 0; i < jsonArrayRegion.length(); i++) {
                        listaRegiones.add(new ModeloSpinner(jsonArrayRegion.getJSONObject(i).getString("nombre_region"), Integer.parseInt(jsonArrayRegion.getJSONObject(i).getString("id_region")))
                        );
                    }
                }

                if(usuariosResp.getStatusLine().getStatusCode()==200){
                    JSONObject jsonObject = new JSONObject(EntityUtils.toString(usuariosResp.getEntity()));
                    JSONArray usuariosWS = jsonObject.getJSONArray("content");
                    //array usuarios
                    for (int i=0;i<usuariosWS.length();i++){
                        listaUsuarios.add(new ModeloSpinner(usuariosWS.getJSONObject(i).getString("nombre_usuario"),Integer.parseInt(usuariosWS.getJSONObject(i).getString("id_usuario"))));
                    }
                }

                if(categoriasResp.getStatusLine().getStatusCode()==200){
                    JSONObject categoriasWS = new JSONObject(EntityUtils.toString(categoriasResp.getEntity()));
                    JSONArray jsonArrayCategoria = categoriasWS.getJSONArray("content");
                    //array categorias
                    for (int i=0;i<jsonArrayCategoria.length();i++){
                        listaCategorias.add(new ModeloSpinner(jsonArrayCategoria.getJSONObject(i).getString("nombre_categoria"), Integer.parseInt(jsonArrayCategoria.getJSONObject(i).getString("id_categoria"))));
                    }
                }

                if(regionesResp.getStatusLine().getStatusCode()==200 && usuariosResp.getStatusLine().getStatusCode()==200 && categoriasResp.getStatusLine().getStatusCode()==200){
                    resul = 200;
                }else if(regionesResp.getStatusLine().getStatusCode()==500 && usuariosResp.getStatusLine().getStatusCode()==500 && categoriasResp.getStatusLine().getStatusCode()==500){
                    resul=500;
                }else if(usuariosResp.getStatusLine().getStatusCode()==401 ){
                    resul=401;
                }



            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);

            }
            return resul;

        }

        protected void onPostExecute(Integer result) {
            if (result==200) {
                AdaptadorPersonalizadoSpinner adaptadorCategorias = new AdaptadorPersonalizadoSpinner(NuevoPerfil.this,R.layout.plantilla_spiners_personalizados_id_nombre,R.id.item_id_spinner,listaCategorias);
                AdaptadorPersonalizadoSpinner adaptadorRegiones = new AdaptadorPersonalizadoSpinner(NuevoPerfil.this,R.layout.plantilla_spiners_personalizados_id_nombre,R.id.item_id_spinner,listaRegiones);
                AdaptadorPersonalizadoSpinner adaptadorUsuarios = new AdaptadorPersonalizadoSpinner(NuevoPerfil.this,R.layout.plantilla_spiners_personalizados_id_nombre,R.id.item_id_spinner,listaUsuarios);
                spcategorias.setAdapter(adaptadorCategorias);
                spregiones.setAdapter(adaptadorRegiones);
                spusuario.setAdapter(adaptadorUsuarios);

            }else if(result==500) {
                Toast.makeText(getApplicationContext(), "Ocurrió un error con la base de datos", Toast.LENGTH_SHORT).show();
            }else if(result==401) {
                Intent data = new Intent();
                data.putExtra("msg","Token de autenticación inválido o expirado, por favor inicie sesión nuevamente");
                data.putExtra("ste",401);
                setResult(AdministracionDePerfiles.RESULT_CANCELED, data);
                finish();
            } else  {
                Toast.makeText(getApplicationContext(), "Problemas de conexión", Toast.LENGTH_SHORT).show();
            }
        }


    }

    private class crearPerfil extends AsyncTask<String, Integer, Integer> {
        private crearPerfil(){}
        int ResponseEstado;

        @Override
        protected Integer doInBackground(String... strings) {

            try {

                HttpClient httpclient;
                HttpPost httppost;
                MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
                multipartEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(ip.getIp()+"crearPerfil");

                multipartEntity.addPart("nomborg_rec", new StringBody(etnombreeorganizacion.getText().toString()));
                multipartEntity.addPart("numtel_rec",new StringBody(etnumerofijo.getText().toString()));
                multipartEntity.addPart("numcel_rec",new StringBody(etnumerocel.getText().toString()));
                multipartEntity.addPart("direccion_rec",new StringBody(etdireccion.getText().toString()));
                multipartEntity.addPart("email_rec",new StringBody(etemail.getText().toString()));
                multipartEntity.addPart("desc_rec",new StringBody(etdescripcion.getText().toString()));
                multipartEntity.addPart("lat_rec",new StringBody(etlatitud.getText().toString()));
                multipartEntity.addPart("longitud_rec",new StringBody(etlongitud.getText().toString()));
                multipartEntity.addPart("id_categoria",new StringBody(String.valueOf(id_categoria)));
                multipartEntity.addPart("id_region",new StringBody(String.valueOf(id_region)));
                multipartEntity.addPart("id_usuario",new StringBody(String.valueOf(id_usuario)));
                multipartEntity.addPart("id_estado",new StringBody(String.valueOf(2)));
                multipartEntity.addPart("Authorization", new StringBody(SharedPrefManager.getInstance(NuevoPerfil.this).getUSUARIO_LOGUEADO().getToken()));

                if(editarFoto){
                    File img=new File(getPath(imageUri));
                    multipartEntity.addPart("imagen", new FileBody(img));
                }


                HttpEntity entity = multipartEntity.build();
                httppost.setEntity(entity);
                HttpResponse httpResponse=httpclient.execute(httppost);
                ResponseEstado = httpResponse.getStatusLine().getStatusCode();

            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
            }
            return ResponseEstado;

        }


        protected void onPostExecute(Integer responseEstado) {
            if (responseEstado==200) {
                Toast.makeText(getApplicationContext(),"Perfil Creado Correctamente",Toast.LENGTH_SHORT).show();
                Intent data = new Intent();
                setResult(AdministracionDePerfiles.RESULT_OK, data);
                finish();

            }else if(responseEstado==500){
                Toast.makeText(getApplicationContext(),"Ocurrió un error en la base de datos",Toast.LENGTH_SHORT).show();
            }else if(responseEstado==401){
                Intent data = new Intent();
                data.putExtra("msg","Token de autenticación inválido o expirado, por favor inicie sesión nuevamente");
                data.putExtra("ste",401);
                setResult(AdministracionDePerfiles.RESULT_CANCELED, data);
                botonGuardar.setClickable(true);
                finish();
            }else {
                Toast.makeText(getApplicationContext(), "Problemas de conexión", Toast.LENGTH_SHORT).show();
                botonGuardar.setClickable(true);
            }
        }
    }
}