package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin.PerfilesBreves.ListaDeContactos;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin.AdministracionDePerfilesAdmin.AdministracionDePerfiles;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVASheyli.FuncionCerrarSesion;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.AcercaDe;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.Login;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.PanelDeControlUsuarios;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.SharedPrefManager;

public class Panel_de_Control extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    ListView lista;

    private DrawerLayout mdrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private int id_usuario_resibido;
    private SharedPreferences.Editor editor_2;
    Context context=this;
    public static Handler h;

    FuncionCerrarSesion cs = new FuncionCerrarSesion();

    public Fuente_Panel_de_control[] fuente_panel_de_control;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_panel_de_control);



        lista=(ListView)findViewById(R.id.listViewPneldeControl);
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



        Fuente_Panel_de_control fuente_panel_de_control[]={
                new Fuente_Panel_de_control(R.drawable.administracioncuenta,"Administración de  Cuenta","Crear, editar y eliminar cuentas de usuario",0),
                new Fuente_Panel_de_control(R.drawable.administracionperfil,"Administración de Perfil","Crear, editar y eliminar perfiles de contacto / aceptar y rechazar solicitudes",0)
                // new Fuente_Panel_de_control(R.drawable.celular,"Solicitudes Nuevas",0),
                // new Fuente_Panel_de_control(R.drawable.celular,"Solicitudes Aprovadas",0),
                // new Fuente_Panel_de_control(R.drawable.celular,"Solicitudes Eliminadas",0)

        };
        final Adaptador_paneldeControl adapter  = new Adaptador_paneldeControl(this,fuente_panel_de_control);
        lista.setAdapter(adapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if(position==0){
                    Intent intent = new Intent(view.getContext(),Mostrar_Usuarios.class);
                    startActivity(intent);
                }else if(position==1){
                    Intent intent = new Intent(view.getContext(),AdministracionDePerfiles.class);
                    startActivity(intent);
                }else if(position==2){
                    Intent intent = new Intent(view.getContext(),ListaDeContactos.class);
                    startActivity(intent);

                }

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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }
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

    @SuppressWarnings("StatementWithEmptyBody")
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

        }else if (id ==R.id.cerrarsecion){
            cs.cerrarsesion();
            SharedPrefManager.getInstance(this).limpiar();
            startActivity(new Intent(this, Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
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



}




