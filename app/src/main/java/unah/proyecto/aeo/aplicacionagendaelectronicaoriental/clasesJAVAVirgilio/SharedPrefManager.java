package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio;


/**
 * Created by alan fabricio on 22/04/2018.
 */
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager{
    private static final String SHARED_PREF_NAME = "DatosLogueo";
    private ModeloUsuarioLogueado USUARIO_LOGUEADO;

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public void guardarUsuario(ModeloUsuarioLogueado logueado){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("id_logueado", logueado.getId_logueado());
        editor.putInt("rol_logueado", logueado.getRol_logueado());
        editor.putInt("estado_logueado", logueado.getEstado_logueado());
        editor.apply();
    }

    public boolean estaLogueado(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getInt("estado_logueado", -2) != -2;
    }

    public ModeloUsuarioLogueado getUSUARIO_LOGUEADO(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        USUARIO_LOGUEADO = new ModeloUsuarioLogueado(
               sharedPreferences.getInt("id_logueado",-2),
               sharedPreferences.getInt("rol_logueado",-2),
                sharedPreferences.getInt("estado_logueado",-2)
        );

        return  USUARIO_LOGUEADO;
    }

    public void limpiar(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}