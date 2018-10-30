package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;

/**
 * Created by alan fabricio on 22/03/2018.
 */

public class Adaptador_mostrarusuarios extends BaseAdapter{

   private List<Fuente_mostrarUsuarios> usuarios;
    private Context context;

    //constructor

    public Adaptador_mostrarusuarios(List<Fuente_mostrarUsuarios> usuarios, Context context) {
        this.usuarios = usuarios;
        this.context = context;
    }

    @Override
    public int getCount() {
        return usuarios.size() ;
    }

    @Override
    public Object getItem(int i) {
        return usuarios.get(i);
    }


    @Override
    public long getItemId(int i) {
        usuarios.get(i).getId();
        return i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v=view;
               v = View.inflate(context,R.layout.activity_item_list_view_mostrar_usuarios,null);

        TextView textid = (TextView)v.findViewById(R.id.id_mostrarusuarios);
        TextView textnombre = (TextView)v.findViewById(R.id.mostrarusuarios);
        TextView texdescripcion = (TextView)v.findViewById(R.id.descripcionmostrarusuarios);
        CircleImageView imageView = v.findViewById(R.id.iconoUsuario);
        textid.setText(""+usuarios.get(i).getId());
        textnombre.setText(usuarios.get(i).getUsuario());
        texdescripcion.setText(usuarios.get(i).getDescripcion());
        if(texdescripcion.getText().toString().equals("Administrador")){
            Picasso.get().load(R.drawable.imgadministrador).into(imageView);
        }else {
            Picasso.get().load(R.drawable.imgclientes).into(imageView);
        }
        return v;
    }
}
