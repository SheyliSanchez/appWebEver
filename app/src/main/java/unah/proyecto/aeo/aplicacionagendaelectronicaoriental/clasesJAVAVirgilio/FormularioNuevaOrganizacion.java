package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
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
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVASheyli.ipLocalhost;


public class FormularioNuevaOrganizacion extends AppCompatActivity {

    EditText nombreOrganizacion;
    EditText telefonoFijo;
    EditText telefonoCelular;
    EditText direccionOrganizacion;
    EditText emailOrganizacion;
    EditText descrpcionOrganizacion;
    EditText latitudOrganizacion;
    EditText longitudOrganizacion;
    //ImageView imagenOrganizacion,ubicacionOrganizacion;
    FloatingActionButton guardar;
    FloatingActionButton imageButton;
    CircleImageView imagenOrganizacion;
    //int id_usuario;
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

    int id_usu=-1;
    //


    String correoIgual;
    //

    ipLocalhost ip = new ipLocalhost();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_registro_nueva_organizacion);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        nombreOrganizacion = (EditText) findViewById(R.id.txtNombreOrganizacion);
        telefonoFijo = (EditText) findViewById(R.id.txtNumeroTelefonoFijo);
        telefonoCelular = (EditText) findViewById(R.id.txtNumeroTelefonoCelular);
        direccionOrganizacion = (EditText) findViewById(R.id.txtDireccion);
        emailOrganizacion = (EditText) findViewById(R.id.txtEmail);
        descrpcionOrganizacion = (EditText) findViewById(R.id.txtDescripcion);
        latitudOrganizacion = (EditText) findViewById(R.id.txtlatitudOrganizacion);
        imageButton = findViewById(R.id.imagenOrganizacionUsuario);
        lat =findViewById(R.id.latitiResibida);
        lo =findViewById(R.id.longitudResibida);
        longitudOrganizacion = (EditText) findViewById(R.id.txtlongitudOrganizacion);
        imagenOrganizacion = (CircleImageView) findViewById(R.id.imagenDeOrganizacion);
        guardar = (FloatingActionButton) findViewById(R.id.btnGuardar);
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

        //llenado de spiner categorias y regiones
        listaCategorias=new ArrayList<ModeloSpinner>();
        listaRegiones=new ArrayList<ModeloSpinner>();
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
        //
        new llenarSpinnersNuevoPerfil().execute();
        if (SharedPrefManager.getInstance(getApplicationContext()).getUSUARIO_LOGUEADO().getId_logueado()!=-2){
            id_usuario = SharedPrefManager.getInstance(getApplicationContext()).getUSUARIO_LOGUEADO().getId_logueado();
        }

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
                if(editarFoto==true){
                    imagenBitmap = ((BitmapDrawable)imagenOrganizacion.getDrawable()).getBitmap();
                    new AsyncTask<Void, Void, String>(){
                        @Override
                        protected String doInBackground(Void... voids) {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            imagenBitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                            byte b []= baos.toByteArray();

                            encodeImagen = Base64.encodeToString(b,Base64.DEFAULT);

                            return null;
                        }
                    }.execute();
                }

                validar();
                if (nombreOrganizacion.getError()==null &&
                        telefonoFijo.getError()==null &&
                        telefonoCelular.getError()==null &&
                        direccionOrganizacion.getError()==null &&
                        emailOrganizacion.getError()==null &&
                        descrpcionOrganizacion.getError()==null &&
                        latitudOrganizacion.getError()==null &&
                        longitudOrganizacion.getError()==null) {
                    guardar.setClickable(false);

                    final ProgressDialog progressDialog = new ProgressDialog(FormularioNuevaOrganizacion.this);
                    progressDialog.setTitle("Procesando...");
                    progressDialog.setMessage("Por favor espere");
                    progressDialog.setCancelable(false);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    //Toast.makeText(getApplicationContext(),"Procesando... Por favor espere",Toast.LENGTH_SHORT).show();

                    progressDialog.show();
                    new crearPerfil().execute();

                }


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

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){

            try {
                Uri imageUri = data.getData();
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                selectedImage = getResizedBitmap(selectedImage, 500);// 400 is for example, replace with desired size

                imagenOrganizacion.setImageBitmap(selectedImage);


            } catch (FileNotFoundException e) {
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
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
//9873298475923487858342905
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }




    private void validar(){
        nombreOrganizacion.setError(null);
        telefonoFijo.setError(null);
        telefonoCelular.setError(null);
        direccionOrganizacion.setError(null);
        emailOrganizacion.setError(null);
        descrpcionOrganizacion.setError(null);
        lat.setError(null);
        lo.setError(null);

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
            if(!mail.contains("@") && !mail.contains(".") ){
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

    private class crearPerfil extends AsyncTask<String, Integer, Boolean> {
        private crearPerfil(){}
        boolean resul = true;

        @Override
        protected Boolean doInBackground(String... strings) {

            try {
                HttpClient httpclient;
                HttpPost httppost;
                ArrayList<NameValuePair> parametros;
                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(ip.getIp()+"crearPerfil");
                httppost.setHeader("Authorization",SharedPrefManager.getInstance(FormularioNuevaOrganizacion.this).getUSUARIO_LOGUEADO().getToken());
                parametros = new ArrayList<NameValuePair>();
                parametros.add(new BasicNameValuePair("nomborg_rec",nombreOrganizacion.getText().toString()));
                parametros.add(new BasicNameValuePair("numtel_rec",telefonoFijo.getText().toString()));
                parametros.add(new BasicNameValuePair("numcel_rec",telefonoCelular.getText().toString()));
                parametros.add(new BasicNameValuePair("direccion_rec",direccionOrganizacion.getText().toString()));
                parametros.add(new BasicNameValuePair("desc_rec",descrpcionOrganizacion.getText().toString()));
                parametros.add(new BasicNameValuePair("email_rec",emailOrganizacion.getText().toString()));
                parametros.add(new BasicNameValuePair("lat_rec",lat.getText().toString()));
                parametros.add(new BasicNameValuePair("longitud_rec",lo.getText().toString()));
                parametros.add(new BasicNameValuePair("id_categoria",String.valueOf(id_categoria)));
                parametros.add(new BasicNameValuePair("id_region",String.valueOf(id_region)));
                parametros.add(new BasicNameValuePair("id_usuario",String.valueOf(id_usuario)));
                parametros.add(new BasicNameValuePair("id_estado",String.valueOf(1)));

                if(editarFoto==true){
                    parametros.add(new BasicNameValuePair("imagen",encodeImagen));
                    parametros.add(new BasicNameValuePair("nombre_imagen",nombreOrganizacion.getText().toString().replace(" ","_") +".jpg"));
                }
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
                validar();
                if (nombreOrganizacion.getError()==null && telefonoFijo.getError()==null && telefonoCelular.getError()==null && direccionOrganizacion.getError()==null && emailOrganizacion.getError()==null && descrpcionOrganizacion.getError()==null && lat.getError()==null && lo.getError()==null){
                    Toast.makeText(getApplicationContext(),"Perfil Creado Correctamente",Toast.LENGTH_SHORT).show();
                    setResult(PanelDeControlUsuarios.RESULT_OK);
                    finish();
                }

            }else {
                Toast.makeText(getApplicationContext(), "Problemas de conexión", Toast.LENGTH_SHORT).show();
                guardar.setClickable(true);
            }
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


    private class validarCorreoDiferente extends AsyncTask<String, Integer, Boolean> {
        private validarCorreoDiferente() {
        }

        boolean resul = true;

        @Override
        protected Boolean doInBackground(String... strings) {
            String correoTraido;

            try {
                // Parseamos la respuesta obtenida del servidor a un objeto JSON
                JSONObject jsonObject = new JSONObject(EntityUtils.toString(new DefaultHttpClient().execute(new HttpPost("http://aeo.web-hn.com/verCorreoOrganizacion.php?id_correo="+emailOrganizacion.getText().toString())).getEntity()));
                JSONArray jsonArray = jsonObject.getJSONArray("datos");
                for (int i = 0; i < jsonArray.length(); i++) {

                    correoTraido = jsonArray.getJSONObject(i).getString("e_mail");
                    if (jsonArray.getJSONObject(i).getString("e_mail").isEmpty()) {


                        if (correoTraido.equals(emailOrganizacion.getText().toString())) {
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
                    emailOrganizacion.setError("correo ya existe");
                    emailOrganizacion.requestFocus();
                }else {

                    validar();

                    if (nombreOrganizacion.getError() == null && telefonoFijo.getError() == null && telefonoCelular.getError() == null && direccionOrganizacion.getError() == null && emailOrganizacion.getError() == null && descrpcionOrganizacion.getError() == null && latitudOrganizacion.getError() == null && longitudOrganizacion.getError() == null) {

                    }
                }

            }else {
                Toast.makeText(getApplicationContext(), "Problemas de conexión", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
