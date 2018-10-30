package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin.HerramientaBusquedaAvanzada;

        import android.content.Context;
        import android.content.Intent;
        import android.net.Uri;
        import android.support.v7.widget.RecyclerView;
        import android.view.View;
        import android.widget.ImageView;
        import android.widget.TextView;

        import de.hdodenhof.circleimageview.CircleImageView;
        import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;
        import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVASheyli.PerfilDeLaOrganizacion;


/**
 * Created by melvinrivera on 21/2/18.
 */

public class ViewHolderPerfilBreve extends RecyclerView.ViewHolder  implements View.OnClickListener{
    CircleImageView imagenPerfilBreve;
    TextView nombrePerfilBreve;
    TextView numeroTelefonoPerfilBreve;
    TextView direccionPerfilBreve;
    TextView id_perfilBreve;
    Context context;
    int position;


    public ViewHolderPerfilBreve(View itemView) {
        super(itemView);
        context = itemView.getContext();
        imagenPerfilBreve =  itemView.findViewById(R.id.imagen_organizacion);
        nombrePerfilBreve = (TextView) itemView.findViewById(R.id.nombre_organizacion);
        numeroTelefonoPerfilBreve = (TextView) itemView.findViewById(R.id.numero_telefono_organizacion);
        direccionPerfilBreve = (TextView) itemView.findViewById(R.id.direccion_organizacion);
        id_perfilBreve = (TextView) itemView.findViewById(R.id.id_Deorganizacion);
    }

    public void onClick(View v) {
        intentPasarPerfilCompleto();
    }

    void setOnClickListener() {
        imagenPerfilBreve.setOnClickListener(this);
        nombrePerfilBreve.setOnClickListener(this);
        numeroTelefonoPerfilBreve.setOnClickListener(this);
        direccionPerfilBreve.setOnClickListener(this);
    }

    public void intentPasarPerfilCompleto(){

        Intent intent = new Intent(context,PerfilDeLaOrganizacion.class);
        intent.putExtra("id_organizacion",Integer.valueOf(id_perfilBreve.getText().toString()));
        context.startActivity(intent);

    }
}

