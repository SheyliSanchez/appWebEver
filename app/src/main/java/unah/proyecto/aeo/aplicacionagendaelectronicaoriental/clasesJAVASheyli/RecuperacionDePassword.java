package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVASheyli;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;

public class RecuperacionDePassword extends AppCompatActivity {
    private EditText p1,p2,p3,idUsuario;
    private Button recuperarContrasena;
    String usuario,contra,res1,res2,res3;

//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperacion_de_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        idUsuario = (EditText) findViewById(R.id.idUsuario);
        p1 = (EditText) findViewById(R.id.pregunta1);
        p2 = (EditText) findViewById(R.id.pregunta2);
        p3 = (EditText) findViewById(R.id.pregunta3);
        recuperarContrasena = (Button) findViewById(R.id.recuperar);

        recuperarContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new recuperacionPassword().execute();
            }

        });
    }

    private void validar(){

        //id.setError(null);
        idUsuario.setError(null);
        p1.setError(null);
        p2.setError(null);
        p3.setError(null);


        // String idd = id.getText().toString();
        String u = idUsuario.getText().toString().trim();
        String a = p1.getText().toString().trim();
        String b = p2.getText().toString().trim();
        String c = p3.getText().toString().trim();

        if(TextUtils.isEmpty(u)){
            idUsuario.setError(getString(R.string.respuestaVacia));
            idUsuario.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(a)){
            p1.setError(getString(R.string.respuestaVacia));
            p1.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(b)){
            p2.setError(getString(R.string.respuestaVacia));
            p2.requestFocus();
            return;

        }if(TextUtils.isEmpty(c)){
            p3.setError(getString(R.string.respuestaVacia));
            p3.requestFocus();
            return;
        }
    }

    @Override
    public void invalidateOptionsMenu() {

    }

    private class recuperacionPassword extends AsyncTask<String, Integer, Boolean> {
        private recuperacionPassword(){}
        boolean resul = true;

        @Override
        protected Boolean doInBackground(String... strings) {
            String nombre = idUsuario.getText().toString();
            try{
                // Parseamos la respuesta obtenida del servidor a un objeto JSON
                JSONObject jsonObject = new JSONObject(EntityUtils.toString(new DefaultHttpClient().execute(new HttpPost("http://aeo.web-hn.com/recuperacion_contrasenia.php?nombre_usuario=" + nombre)).getEntity()));
                JSONArray jsonArray = jsonObject.getJSONArray("password");
                for(int i = 0; i < jsonArray.length(); i++) {

                    usuario = jsonArray.getJSONObject(i).getString("nombre_usuario");
                    contra = jsonArray.getJSONObject(i).getString("contrasena");
                    res1 = jsonArray.getJSONObject(i).getString("respuesta_uno");
                    res2 = jsonArray.getJSONObject(i).getString("respuesta_dos");
                    res3= jsonArray.getJSONObject(i).getString("respuesta_tres");

                    resul  = true;
                }
            }catch (Exception ex){
                ex.printStackTrace();
                resul = false;
            }

            return resul;
        }
        protected void onPostExecute(Boolean result) {
            if (result)
            {
                validar();
                String user = idUsuario.getText().toString().trim();
                String ps1 = p1.getText().toString().trim();
                String ps2 = p2.getText().toString().trim();
                String ps3 = p3.getText().toString().trim();

                if (idUsuario.getError()==null && p1.getError()==null && p2.getError()==null && p3.getError()==null)
                {

                    if ((user.equals(usuario))&&(ps1.equals(res1)) && (ps2.equals(res2)) && (ps3.equals(res3)))
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RecuperacionDePassword.this);
                        builder.setIcon(R.drawable.llave).setTitle("Recuperación Exitosa!").
                                setMessage("Tu contraseña es:\n" + contra).
                                setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        idUsuario.setText(null);
                                        p1.setText(null);
                                        p2.setText(null);
                                        p3.setText(null);
                                    }
                                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                idUsuario.setText(null);
                                p1.setText(null);
                                p2.setText(null);
                                p3.setText(null);
                                dialogInterface.dismiss();
                            }
                        });

                        AlertDialog alertDialog=builder.create();
                        alertDialog.show();
                    }else//respuestas diferentes
                    {
                        Toast.makeText(getApplicationContext(), "Las respuestas son incorrectas", Toast.LENGTH_SHORT).show();
                    }
                }
            }else {
                Toast.makeText(getApplicationContext(), "Revisa tu conexión o tus respuestas!", Toast.LENGTH_SHORT).show();
            }//fin de else fallo conexion
        }//fin de onPostExecute
    }
}
