package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin.HerramientaBusquedaAvanzada;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin.PerfilesBreves.PerfilBreve;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVASheyli.ipLocalhost;

/**
 * Created by melvinrivera on 22/2/18.
 */

public class AdaptadorParaBusquedaAvanzada extends RecyclerView.Adapter<ViewHolderPerfilBreve> {
    /**********************************************************************************************
     *                                       DECLARACIÓN DE VARIABLES
     **********************************************************************************************/

    List<PerfilBreve> listaObjetos;
    private  String BASE_URL=new ipLocalhost().getBASE_URL();
    /**********************************************************************************************
     *                                       CONSTRUCTOR
     **********************************************************************************************/

    public AdaptadorParaBusquedaAvanzada(List<PerfilBreve> listaObjetos) {
        this.listaObjetos = listaObjetos;
    }

    /**********************************************************************************************
     *      CREACIÓN DEL VIEWHOLDER QUE INFLA LA PLANTILLA DE LOS ELEMENTOS
     **********************************************************************************************/

    @Override
    public ViewHolderPerfilBreve onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_item_lista_contactos,parent,false);
        return new ViewHolderPerfilBreve(vista);
    }

    /**********************************************************************************************
     * SETEA LOS ELEMENTOS SEGÚN LA LISTA OBTENIDA
     **********************************************************************************************/
    @Override
    public void onBindViewHolder(ViewHolderPerfilBreve holder, int position) {
        holder.nombrePerfilBreve.setText(listaObjetos.get(position).getNombre().toString());
        holder.direccionPerfilBreve.setText(listaObjetos.get(position).getDireccion().toString());
        holder.numeroTelefonoPerfilBreve.setText(listaObjetos.get(position).getNumeroTelefono().toString());
        holder.id_perfilBreve.setText(""+listaObjetos.get(position).getId());
        if(!listaObjetos.get(position).getImagen().equals("null")){
            Glide.with(holder.context).
                    load(BASE_URL + listaObjetos.get(position).getImagen()).
                    placeholder(R.drawable.wait).
                    into(holder.imagenPerfilBreve);
        }else{
            Glide.with(holder.context).
                    load(R.drawable.iconocontactowhite).
                    placeholder(R.drawable.wait).
                    into(holder.imagenPerfilBreve);
        }
        holder.setOnClickListener();
    }

    @Override
    public int getItemCount() {
        return listaObjetos.size();
    }

    /**********************************************************************************************
     *    SETEA EL FILTRO DEL ARRAYLIST
     **********************************************************************************************/

    public void setFilter(ArrayList<PerfilBreve>  newList){
        listaObjetos = new ArrayList<PerfilBreve>();
        listaObjetos.addAll(newList);
        notifyDataSetChanged();
    }
}