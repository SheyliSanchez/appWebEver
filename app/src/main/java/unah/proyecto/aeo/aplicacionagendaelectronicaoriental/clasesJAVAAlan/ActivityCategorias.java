package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.WindowManager;

import java.io.IOException;

import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin.HerramientaBusquedaAvanzada.BusquedaAvanzada;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.AcercaDe;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.EditarUsuario;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.Login;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.PanelDeControlUsuarios;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.SharedPrefManager;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.provider.CategoriasContract;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.sync.SyncAdapter;

public class ActivityCategorias extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        SearchView.OnQueryTextListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    Adaptador_Categoria adaptadorCategoria;
    RecyclerView contenedor;
    private static final int CATEGORIA_LOADER = 0;

    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_categorias);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

         navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        contenedor = (RecyclerView) findViewById(R.id.contenedor);
        contenedor.setHasFixedSize(true);


        adaptadorCategoria = new Adaptador_Categoria(ActivityCategorias.this, null);


        if (getRotation(getApplicationContext()) == "vertical") {
            LinearLayoutManager layout = new LinearLayoutManager(getApplicationContext());
            layout.setOrientation(LinearLayoutManager.VERTICAL);
            contenedor.setLayoutManager(layout);
        } else {

            contenedor.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        }
        contenedor.setAdapter(adaptadorCategoria);


        SyncAdapter.initializeSyncAdapter(this);

        getLoaderManager().initLoader(CATEGORIA_LOADER,null,this);


    }


    public String getRotation(Context context) {
        final int rotation = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation();
        switch (rotation) {
            case Surface.ROTATION_0:
                return "vertical";
            case Surface.ROTATION_90:
                return "horizontal";
            case Surface.ROTATION_180:
                return "vertical inversa";
            default:
                return "horizontal inversa";
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lista_de_contactos, menu);
        MenuItem menuItem = menu.findItem(R.id.accion_buscar);
        MenuItem itemBusquedaAvanzada = menu.findItem(R.id.accion_buscarAvanzado);
        //Establecimeinto del SearchView para filtrar por nombre, numero de telefono o region
        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Intent para pasar a la activity de búsqueda avanzada
            case R.id.accion_buscarAvanzado:
                Intent aBusquedaAvanzada = new Intent(getApplicationContext(), BusquedaAvanzada.class);
                startActivity(aBusquedaAvanzada);
                break;
            case R.id.sincronizar:
                if( compruebaConexion()){
                    Snackbar.make(findViewById(R.id.drawer_layout),"Actualizando datos...",Snackbar.LENGTH_SHORT).show();
                    SyncAdapter.syncImmediately(this);
                }else{
                    Snackbar.make(findViewById(R.id.drawer_layout),"Actualmente no cuentas con conexión a internet",Snackbar.LENGTH_SHORT).show();

                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.principaldos) {


        } else if (id == R.id.acercadeinfodos) {
            Intent intent = new Intent(this,AcercaDe.class);
            startActivity(intent);

        }else if (id == R.id.login) {

                Intent intent = new Intent(ActivityCategorias.this,Login.class);
                startActivity(intent);

        }else if (id == R.id.cerrarsecion){
            SharedPrefManager.getInstance(getApplicationContext()).limpiar();
            startActivity(new Intent(this,Login.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK));
        }else if(id == R.id.panelControl){
            Intent intent = new Intent(this,Panel_de_Control.class);
            startActivity(intent);

        }else if(id == R.id.panelControlUsuario){
            Intent intent = new Intent(this,PanelDeControlUsuarios.class);
            startActivity(intent);

        }else if (id == R.id.ediciondeCuenta){
            Intent intent = new Intent(this,EditarUsuario.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                CategoriasContract.CategoriasEntry.COLUMN_ID_CATEGORIA,
                CategoriasContract.CategoriasEntry.COLUMN_NOMBRE_CATEGORIA,
                CategoriasContract.CategoriasEntry.COLUMN_CANTIDAD,
                CategoriasContract.CategoriasEntry.COLUMN_IMAGEN_CATEGORIA
        };



        return new CursorLoader(this,
                CategoriasContract.CategoriasEntry.CONTENT_URI,
                projection,
                null,
                null,
                CategoriasContract.CategoriasEntry.COLUMN_ID_CATEGORIA);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adaptadorCategoria.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adaptadorCategoria.swapCursor(null);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        Cursor contacts = getListOfContacts(query);

       Adaptador_Categoria adaptador_categoria1 = new Adaptador_Categoria(this,contacts);

        contenedor.setAdapter(adaptador_categoria1);
        return true;
    }

    public Cursor getListOfContacts(String searchText) {

        Cursor cur = null;
        ContentResolver cr = getContentResolver();

        String[] projection = {
                CategoriasContract.CategoriasEntry.COLUMN_ID_CATEGORIA,
                CategoriasContract.CategoriasEntry.COLUMN_NOMBRE_CATEGORIA,
                CategoriasContract.CategoriasEntry.COLUMN_CANTIDAD,
                CategoriasContract.CategoriasEntry.COLUMN_IMAGEN_CATEGORIA
        };

        Uri uri = CategoriasContract.CategoriasEntry.CONTENT_URI;

        String selection = CategoriasContract.CategoriasEntry.COLUMN_NOMBRE_CATEGORIA + " LIKE ?";
        String[] selectionArgs = new String[]{"%"+searchText+"%"};

        cur = cr.query(uri, projection, selection, selectionArgs, CategoriasContract.CategoriasEntry.COLUMN_ID_CATEGORIA);

        return cur;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Cursor contacts = getListOfContacts(newText);


        Adaptador_Categoria adaptador_categoria1 = new Adaptador_Categoria(this,contacts);

        contenedor.setAdapter(adaptador_categoria1);
        return true;
    }

    public boolean compruebaConexion() {

        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -w 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }
}