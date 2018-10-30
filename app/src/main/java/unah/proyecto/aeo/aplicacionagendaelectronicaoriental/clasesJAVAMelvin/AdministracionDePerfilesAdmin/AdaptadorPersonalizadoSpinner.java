package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin.AdministracionDePerfilesAdmin;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;

/**
 * Created by melvinrivera on 9/4/18.
 */

public class AdaptadorPersonalizadoSpinner extends ArrayAdapter<ModeloSpinner> {
    int groupId;
    Activity context;
    ArrayList<ModeloSpinner>  list;
    LayoutInflater  inflater;

    public AdaptadorPersonalizadoSpinner(Activity context, int  groupId, int id, ArrayList<ModeloSpinner> list){
        super(context,id,list);
        this.list = list;
        inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groupId=groupId;

    }

    public View getView(int position, View convertView, ViewGroup parent){
        View itemView = inflater.inflate(groupId,parent,false);
        TextView nombre = (TextView)itemView.findViewById(R.id.item_nombre_spinner);
        TextView id =(TextView)itemView.findViewById(R.id.item_id_spinner);

        nombre.setText(list.get(position).getNombre());
        id.setText(""+list.get(position).getId());

        return itemView;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent){
        return  getView(position,convertView, parent);
    }
}
