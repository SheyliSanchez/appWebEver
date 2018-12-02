package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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

public class AdaptadorOrganizacion extends BaseAdapter {
    private List<EntidadOrganizacion> listaobjetos;
    private Context context;
    LayoutInflater lsinflater;
    private  String BASE_URL=new ipLocalhost().getIp().substring(0,19);
    //constructor


    public AdaptadorOrganizacion(List<EntidadOrganizacion> listaobjetos, Context context) {
        this.listaobjetos = listaobjetos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listaobjetos.size();
    }

    @Override
    public Object getItem(int position) {
        return listaobjetos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listaobjetos.get(position).getId();
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        View v=view;

        v = View.inflate(context, R.layout.item_perfil_empresa,null);

        //TextView textid = (TextView)v.findViewById(R.id.id);
        TextView nombre = (TextView)v.findViewById(R.id.nombreOrganizacionRegistrada);
        TextView estado = (TextView)v.findViewById(R.id.estadoOrganizacionRegistrada);
        CircleImageView Imagen = v.findViewById(R.id.imagenOrganizacionRegistrada);


       // textid.setText(""+perfiles.get(i).getId());
        nombre.setText(listaobjetos.get(i).getTitulo().toString());
        estado.setText(listaobjetos.get(i).getEstado().toString());
        if (listaobjetos.get(i).getImagen().equals("null")){
            Glide.with(context).load(R.drawable.iconocontactowhite).into(Imagen);

        }else {
            Picasso.get().load(BASE_URL+listaobjetos.get(i).getImagen()).
            placeholder(R.drawable.wait).
                    into(Imagen);
        }

        return v;
    }
}