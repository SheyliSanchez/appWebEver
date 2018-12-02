package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin.AdministracionDePerfilesAdmin;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVASheyli.ipLocalhost;

/**
 * Created by melvinrivera on 3/4/18.
 */

public class AdaptadorMostrarPerfiles extends BaseAdapter {
    private List<Fuente_mostrarPerfiles> perfiles;
    private Context context;
    private  String BASE_URL=new ipLocalhost().getIp().substring(0,19);

    //constructor

    public AdaptadorMostrarPerfiles(List<Fuente_mostrarPerfiles> perfiles, Context context) {
        this.perfiles = perfiles;
        this.context = context;

    }


    public int getCount() {
        return perfiles.size() ;
    }


    public Object getItem(int i) {
        return perfiles.get(i);
    }



    public long getItemId(int i) {
        perfiles.get(i).getId();
        return i;
    }

    //METODO PARA SETEAR LOS COMPONENTES VISUALES
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v=view;
        v = View.inflate(context, R.layout.activity_item_listview_mostrar_perfiles,null);

        TextView textid = (TextView)v.findViewById(R.id.id_mostrarperfiles);
        TextView textnombre = (TextView)v.findViewById(R.id.mostrarperfiles);
        TextView textusuario = (TextView)v.findViewById(R.id.uspropietario);

        CircleImageView icono = v.findViewById(R.id.iconoDeContactoEnAdministrador);

        if(!perfiles.get(i).getImagen().equals("null")){
            Picasso.get().load(BASE_URL+perfiles.get(i).getImagen()).
                    placeholder(R.drawable.wait).
            into(icono);
        }else {
            Picasso.get().load(R.drawable.iconocontactowhite).
                    into(icono);
        }

        textid.setText(""+perfiles.get(i).getId());
        textnombre.setText(perfiles.get(i).getPerfil());
        textusuario.setText(perfiles.get(i).getUsuario());

        return v;
    }
}
