package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVASheyli;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.ActivityCategorias;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.Panel_de_Control;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVABessy.Mapa;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.AcercaDe;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.EditarUsuario;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.Login;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.PanelDeControlUsuarios;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.SharedPrefManager;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.provider.AEODbHelper;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.provider.PerfilesContract;

public class PerfilDeLaOrganizacion extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ImageView organizacion;
    private TextView nombre, direccion, telefono, email, descripcion, movil;
    AEODbHelper conn;
    int id_organizacion;
    String nombreOrganizacion;
    double x, y;
    FloatingActionButton ubicacion;
    String organizacionP, nombreP, direccionP, telefonoP, emailP, descripcionP, movilP;
    boolean imagenContacto;



    int id_usu=-1;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_de_la_organizacion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id_organizacion = extras.getInt("id_organizacion");
            nombreOrganizacion = extras.getString("nombre_organizacion");
            toolbar.setTitle(nombreOrganizacion);
        }
        setSupportActionBar(toolbar);
        conn = new AEODbHelper(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

         navigationView = (NavigationView) findViewById(R.id.nav_view);
         navigationView.setNavigationItemSelectedListener(this);


        //recuperacion de variables
        organizacion =  findViewById(R.id.imagenOrganizacion);
        nombre = (TextView) findViewById(R.id.n);
        direccion = (TextView) findViewById(R.id.d);
        telefono = (TextView) findViewById(R.id.t);
        movil = (TextView) findViewById(R.id.tcelular);
        email = (TextView) findViewById(R.id.e);
        descripcion = (TextView) findViewById(R.id.descripcion);
        ubicacion = (FloatingActionButton) findViewById(R.id.fab);



        String[] projection = {
                PerfilesContract.ContactosEntry.COLUMN_IMAGEN_PATH,
                PerfilesContract.ContactosEntry.COLUMN_NOMBRE,
                PerfilesContract.ContactosEntry.COLUMN_NUMERO_TELEFONO,
                PerfilesContract.ContactosEntry.COLUMN_NUMERO_CELULAR,
                PerfilesContract.ContactosEntry.COLUMN_E_MAIL,
                PerfilesContract.ContactosEntry.COLUMN_DIRECCION,
                PerfilesContract.ContactosEntry.COLUMN_DESCRIPCION,
                PerfilesContract.ContactosEntry.COLUMN_LATITUD,
                PerfilesContract.ContactosEntry.COLUMN_LONGITUD
        };

        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor = db.query(PerfilesContract.ContactosEntry.TABLE_NAME,
                projection,
                PerfilesContract.ContactosEntry.COLUMN_PERFILID + " = ?",
                new String[]{String.valueOf(id_organizacion)},
                null,
                null,
                null);


        while (cursor.moveToNext()) {
            if (!cursor.getString(cursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_IMAGEN_PATH)).isEmpty()) {
                Picasso.get().
                        load(cursor.getString(cursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_IMAGEN_PATH))).
                        into(organizacion);
            } else {
                Picasso.get().
                        load(R.drawable.iconocontactowhite).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).
                into(organizacion);
            }

            nombre.setText(cursor.getString(cursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_NOMBRE)));
            if (cursor.getString(cursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_NUMERO_TELEFONO)).isEmpty()) {
                telefono.setText("No disponible");
            } else {
                telefono.setText(cursor.getString(cursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_NUMERO_TELEFONO)));
            }

            if (cursor.getString(cursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_NUMERO_CELULAR)).isEmpty()) {
                movil.setText("No disponible");
            } else {
                movil.setText(cursor.getString(cursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_NUMERO_CELULAR)));
            }

            if (cursor.getString(cursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_E_MAIL)).isEmpty()) {
                email.setText("No disponible");
            } else {
                email.setText(cursor.getString(cursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_E_MAIL)));
            }

            if (cursor.getString(cursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_DIRECCION)).isEmpty()) {
                direccion.setText("Ninguna");
            } else {
                direccion.setText(cursor.getString(cursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_DIRECCION)));
            }

            if (cursor.getString(cursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_DESCRIPCION)).isEmpty()) {
                descripcion.setText("No disponible");
            } else {
                descripcion.setText(cursor.getString(cursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_DESCRIPCION)));
            }

            x = Double.valueOf(cursor.getString(cursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_LATITUD)));
            y = Double.valueOf(cursor.getString(cursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_LONGITUD)));

        }
        ubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ubicacion1 = new Intent(getApplicationContext(), Mapa.class);
                ubicacion1.putExtra("latitud", x);
                ubicacion1.putExtra("longitud", y);
                ubicacion1.putExtra("nombre", nombre.getText().toString());
                startActivity(ubicacion1);
            }
        });
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
    protected void onStart() {
        super.onStart();
        if(SharedPrefManager.getInstance(getApplicationContext()).estaLogueado()){
            int rol = SharedPrefManager.getInstance(getApplicationContext()).getUSUARIO_LOGUEADO().getRol_logueado();
            if ( rol ==2){
                navigationView.getMenu().clear();
                navigationView.inflateMenu(R.menu.menu_cliente);

            }else if(rol == 1){
                navigationView.getMenu().clear();
                navigationView.inflateMenu(R.menu.menu_admin);

            }

        }else{
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.navigation_view);

        }
    }


   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.perfil_de_la_organizacion, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.llamar) {
            String p;
            Intent i = new Intent(Intent.ACTION_DIAL);
            if(telefono.getText().toString().equals("No disponible")){
                p = "tel:" + movil.getText().toString();

            }else{
                p= "tel:" + telefono.getText().toString();
            }
            i.setData(Uri.parse(p));
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.principaldos) {
            // Handle the camera action
            startActivity(new Intent(getBaseContext(), ActivityCategorias.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
            finish();
        } else if (id == R.id.acercadeinfodos) {
            Intent intent = new Intent(this, AcercaDe.class);
            startActivity(intent);

        } else if (id == R.id.login) {

            Intent intent = new Intent(this, Login.class);
            startActivity(intent);

        }else if (id == R.id.cerrarsecion){
            SharedPrefManager.getInstance(getApplicationContext()).limpiar();
            startActivity(new Intent(this,Login.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK));
        }else if (id == R.id.ediciondeCuenta){
            Intent intent = new Intent(this,EditarUsuario.class);
            startActivity(intent);

        }else if(id == R.id.panelControl){
            Intent intent = new Intent(this,Panel_de_Control.class);
            startActivity(intent);

        }else if(id == R.id.panelControlUsuario){
            Intent intent = new Intent(this,PanelDeControlUsuarios.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

