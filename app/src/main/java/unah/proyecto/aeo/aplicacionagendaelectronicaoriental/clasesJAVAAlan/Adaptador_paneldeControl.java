package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;

/**
 * Created by alan fabricio on 12/03/2018.
 */

public class Adaptador_paneldeControl extends ArrayAdapter<Fuente_Panel_de_control> {
    private Fuente_Panel_de_control[] fuente_panel_de_control;
    Context context;

    public Adaptador_paneldeControl(Context context, Fuente_Panel_de_control[] fuente_panel_de_controls) {
        super(context,-1,fuente_panel_de_controls);
        this.context = context;
        this.fuente_panel_de_control = fuente_panel_de_controls;
    }



    public View getView(int posicion, View convertview, ViewGroup viewGroup) {
        LayoutInflater inflador = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View fila = inflador.inflate(R.layout.activity_item_list_view__panelde__control,viewGroup,false);
        ImageView imagen = (ImageView) fila.findViewById(R.id.imagepaneldecontrol);
        TextView texto = (TextView)fila.findViewById(R.id.textopaneldecontrol);
        TextView descripcion = (TextView)fila.findViewById(R.id.descripcionpaneldecontrol);


        imagen.setImageResource(fuente_panel_de_control[posicion].getImagenPaneldeControl());
        texto.setText(fuente_panel_de_control[posicion].getTitulo());
        descripcion.setText(fuente_panel_de_control[posicion].getDescripcion());
        return fila;
    }


}
