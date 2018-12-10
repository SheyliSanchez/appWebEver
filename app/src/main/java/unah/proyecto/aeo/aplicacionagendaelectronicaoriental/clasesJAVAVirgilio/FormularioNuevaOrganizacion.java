package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio;

import android.Manifest;
import android.app.ProgressDialog;
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
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.ActivityCategorias;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.Panel_de_Control;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVABessy.Ingresar_Ubicacion;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin.AdministracionDePerfilesAdmin.AdaptadorPersonalizadoSpinner;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin.AdministracionDePerfilesAdmin.AdministracionDePerfiles;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin.AdministracionDePerfilesAdmin.ModeloSpinner;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin.AdministracionDePerfilesAdmin.NuevoPerfil;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVASheyli.FuncionCerrarSesion;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVASheyli.ipLocalhost;


public class FormularioNuevaOrganizacion extends AppCompatActivity {
    EditText nombreOrganizacion;
    EditText telefonoFijo;
    EditText telefonoCelular;
    EditText direccionOrganizacion;
    EditText emailOrganizacion;
    EditText descrpcionOrganizacion;
    FloatingActionButton guardar;
    FloatingActionButton imageButton;
    CircleImageView imagenOrganizacion;
    Button ubicacion;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1 ;
    Bitmap imagenBitmap;
    boolean editarFoto = false;
    ArrayList<ModeloSpinner> listaCategorias, listaRegiones, listaUsuarios;
    private Spinner  spinnerCategorias,spinnerRgiones;
    int id_categoria, id_region, id_usuario;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    String encodeImagen,cantidadDigitos;
    TextView lo,lat;
    //preferencias
    ipLocalhost ip = new ipLocalhost();
    FuncionCerrarSesion cs = new FuncionCerrarSesion();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_registro_nueva_organizacion);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        imageButton = findViewById(R.id.imagenOrganizacionUsuario);
        imagenOrganizacion = (CircleImageView) findViewById(R.id.imagenDeOrganizacion);
        guardar = (FloatingActionButton) findViewById(R.id.btnGuardar);
        nombreOrganizacion = (EditText) findViewById(R.id.txtNombreOrganizacion);
        telefonoFijo = (EditText) findViewById(R.id.txtNumeroTelefonoFijo);
        telefonoCelular = (EditText) findViewById(R.id.txtNumeroTelefonoCelular);
        direccionOrganizacion = (EditText) findViewById(R.id.txtDireccion);
        emailOrganizacion = (EditText) findViewById(R.id.txtEmail);
        descrpcionOrganizacion = (EditText) findViewById(R.id.txtDescripcion);
        lat =findViewById(R.id.latitiResibida);
        lo =findViewById(R.id.longitudResibida);
        spinnerCategorias = (Spinner) findViewById(R.id.spinercategoriaOrganizacion);
        spinnerRgiones = (Spinner) findViewById(R.id.spinerregionOrganizacion);

        ubicacion = (Button) findViewById(R.id.btnUbicacionOrganizacion);
        //Ingresar la ubicacion
        ubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ubicacion1 = new Intent(getApplicationContext(),Ingresar_Ubicacion.class);
                startActivityForResult(ubicacion1,1);
            }
        });

        listaCategorias=new ArrayList<ModeloSpinner>();
        listaRegiones=new ArrayList<ModeloSpinner>();

        new llenarSpinnersNuevoPerfil().execute();
        if (SharedPrefManager.getInstance(getApplicationContext()).getUSUARIO_LOGUEADO().getId_logueado()!=-2){
            id_usuario = SharedPrefManager.getInstance(getApplicationContext()).getUSUARIO_LOGUEADO().getId_logueado();
        }

        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCheck1 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
        }

        spinnerCategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_categoria = listaCategorias.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerRgiones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_region = listaRegiones.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarFoto=true;
                requestRead();
            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validar();
                if (nombreOrganizacion.getError()==null &&
                        telefonoFijo.getError()==null &&
                        telefonoCelular.getError()==null &&
                        direccionOrganizacion.getError()==null &&
                        emailOrganizacion.getError()==null &&
                        descrpcionOrganizacion.getError()==null &&
                        lat.getError()==null &&
                        lo.getError()==null) {
                    guardar.setClickable(false);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE && data.getData()!=null){

            try {
                imageUri = data.getData();
                imagenBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                imagenOrganizacion.setImageBitmap(imagenBitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                String latitud = data.getStringExtra("latitud");
                String longitud = data.getStringExtra("longitud");
                lat.setText(latitud);
                lo.setText(longitud);

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
        nombreOrganizacion.setError(null);
        telefonoFijo.setError(null);
        telefonoCelular.setError(null);
        direccionOrganizacion.setError(null);
        emailOrganizacion.setError(null);
        descrpcionOrganizacion.setError(null);
        lat.setError(null);
        lo.setError(null);

        // String idd = id.getText().toString();
        String nomborg = nombreOrganizacion.getText().toString();
        String numtel = telefonoFijo.getText().toString();
        String numcel = telefonoCelular.getText().toString();
        String direccion = direccionOrganizacion.getText().toString();
        String desc = descrpcionOrganizacion.getText().toString();
        String lati = lat.getText().toString();
        String longitud = lo.getText().toString();
        String mail = emailOrganizacion.getText().toString();

        if(TextUtils.isEmpty(mail)){

        }else{
            if(!mail.contains("@") || !mail.contains(".")){
                emailOrganizacion.setError(getString(R.string.error_mailnovalido));
                emailOrganizacion.requestFocus();
                return;
            }
        }

        if(TextUtils.isEmpty(nomborg) || nomborg.startsWith(" ")){
            nombreOrganizacion.setError(getString(R.string.errNombreOrg));
            nombreOrganizacion.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(numtel)){
            if(TextUtils.isEmpty(numcel)){
                telefonoFijo.setError(getString(R.string.errNumero));
                telefonoFijo.requestFocus();
                return;
            }
        }else{
            if(numtel.length()<8 || !numtel.startsWith("2") || numtel.length()>8){
                telefonoFijo.setError(getString(R.string.error_numnovalido));
                telefonoFijo.requestFocus();
                return;
            }
        }

        if(TextUtils.isEmpty(numcel)){
            if(TextUtils.isEmpty(numtel)){
                telefonoCelular.setError(getString(R.string.errNumero));
                telefonoCelular.requestFocus();
                return;
            }
        }else{
            if(numcel.length()<8 || numcel.length()>8){
                telefonoCelular.setError(getString(R.string.error_numnovalido));
                telefonoCelular.requestFocus();
                return;
            }
        }

        if(TextUtils.isEmpty(direccion) || direccion.startsWith(" ")){
            direccionOrganizacion.setError(getString(R.string.errDir));
            direccionOrganizacion.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(desc) || desc.startsWith(" ")){
            descrpcionOrganizacion.setError(getString(R.string.errDesc));
            descrpcionOrganizacion.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(lati)){
            lat.setError(getString(R.string.errLat));
            lat.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(longitud)){
            lo.setError(getString(R.string.errLong));
            lo.requestFocus();
            return;
        }

    }

    private class llenarSpinnersNuevoPerfil extends AsyncTask<String, Integer, Boolean> {
        private llenarSpinnersNuevoPerfil(){}
        boolean resul = true;

        @Override
        protected Boolean doInBackground(String... strings) {

            try {

                JSONObject regionesWS = new JSONObject(EntityUtils.toString(new DefaultHttpClient().execute(new HttpGet(ip.getIp()+"regiones")).getEntity()));
                JSONArray jsonArrayRegion = regionesWS.getJSONArray("content");

                JSONObject categoriasWS = new JSONObject(EntityUtils.toString(new DefaultHttpClient().execute(new HttpGet(ip.getIp()+"categorias")).getEntity()));
                JSONArray jsonArrayCategoria = categoriasWS.getJSONArray("content");

                for (int i = 0; i < jsonArrayRegion.length(); i++) {
                    listaRegiones.add(new ModeloSpinner(jsonArrayRegion.getJSONObject(i).getString("nombre_region"),Integer.parseInt(jsonArrayRegion.getJSONObject(i).getString("id_region")))
                    );                }

                for (int i=0;i<jsonArrayCategoria.length();i++){
                    listaCategorias.add(new ModeloSpinner(jsonArrayCategoria.getJSONObject(i).getString("nombre_categoria"), Integer.parseInt(jsonArrayCategoria.getJSONObject(i).getString("id_categoria"))));
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
                AdaptadorPersonalizadoSpinner adaptadorCategorias = new AdaptadorPersonalizadoSpinner(FormularioNuevaOrganizacion.this,R.layout.plantilla_spiners_personalizados_id_nombre,R.id.item_id_spinner,listaCategorias);
                AdaptadorPersonalizadoSpinner adaptadorRegiones = new AdaptadorPersonalizadoSpinner(FormularioNuevaOrganizacion.this,R.layout.plantilla_spiners_personalizados_id_nombre,R.id.item_id_spinner,listaRegiones);
                spinnerCategorias.setAdapter(adaptadorCategorias);
                spinnerRgiones.setAdapter(adaptadorRegiones);

            }else {
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
                httppost = new HttpPost(ip.getIp() + "crearPerfil");
                multipartEntity.addPart("nomborg_rec", new StringBody(nombreOrganizacion.getText().toString()));
                multipartEntity.addPart("numtel_rec", new StringBody(telefonoFijo.getText().toString()));
                multipartEntity.addPart("numcel_rec", new StringBody(telefonoCelular.getText().toString()));
                multipartEntity.addPart("direccion_rec", new StringBody(direccionOrganizacion.getText().toString()));
                multipartEntity.addPart("email_rec", new StringBody(emailOrganizacion.getText().toString()));
                multipartEntity.addPart("desc_rec", new StringBody(descrpcionOrganizacion.getText().toString()));
                multipartEntity.addPart("lat_rec", new StringBody(lat.getText().toString()));
                multipartEntity.addPart("longitud_rec", new StringBody(lo.getText().toString()));
                multipartEntity.addPart("id_categoria", new StringBody(String.valueOf(id_categoria)));
                multipartEntity.addPart("id_region", new StringBody(String.valueOf(id_region)));
                multipartEntity.addPart("id_usuario", new StringBody(String.valueOf(id_usuario)));
                multipartEntity.addPart("id_estado", new StringBody(String.valueOf(1)));
                multipartEntity.addPart("Authorization", new StringBody(SharedPrefManager.getInstance(FormularioNuevaOrganizacion.this).getUSUARIO_LOGUEADO().getToken()));

                if(editarFoto){
                    File img = new File(getPath(imageUri));
                    multipartEntity.addPart("imagen", new FileBody(img));
                }
                HttpEntity entity = multipartEntity.build();
                httppost.setEntity(entity);
                HttpResponse httpResponse=httpclient.execute(httppost);
                ResponseEstado = httpResponse.getStatusLine().getStatusCode();
               //httpclient.execute(httppost);

                //resul = true;
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                //resul = false;
            }
            //return resul;
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
               // guardar.setClickable(true);
            }else if(responseEstado==401){
                Intent data = new Intent();
                data.putExtra("msg","Token de autenticación inválido o expirado, por favor inicie sesión nuevamente");
                setResult(PanelDeControlUsuarios.RESULT_CANCELED, data);
                finish();
            }else {
                Toast.makeText(getApplicationContext(), "Problemas de conexión", Toast.LENGTH_SHORT).show();
                guardar.setClickable(true);
            }
        }

    }
}