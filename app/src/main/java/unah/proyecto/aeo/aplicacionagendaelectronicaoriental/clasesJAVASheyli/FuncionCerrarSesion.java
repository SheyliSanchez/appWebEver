package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVASheyli;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import org.json.JSONObject;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;


public class FuncionCerrarSesion {

    ipLocalhost ip = new  ipLocalhost();

public   void  cerrarsesion() {
     new CerrarSesion().execute();
}



    //METODO DE VERIFICADE DESDE EL SERVIDOR
    @SuppressLint("StaticFieldLeak")
    private class  CerrarSesion extends AsyncTask<String, Integer, Boolean> {
        private CerrarSesion() {
        }

        boolean resul = true;

        @Override
        protected Boolean doInBackground(String... strings) {

            try {

               // JSONObject jsonObject = new JSONObject());

                EntityUtils.toString(new DefaultHttpClient().execute(new HttpGet(ip.getIp()+"cerrarSession")).getEntity());

            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }


            return resul;
        }

        protected void onPostExecute(Boolean result) {


        }//fin de onPostExecute
    }

}
