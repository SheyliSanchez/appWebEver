package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan;

import android.widget.ImageView;

/**
 * Created by alan fabricio on 12/03/2018.
 */

public class Fuente_Panel_de_control {
    private int imagenPaneldeControl;
    private String titulo;
    private String descripcion;
    private int id;

    public Fuente_Panel_de_control(int imagenPaneldeControl, String titulo, String descripcion,int id) {
        this.imagenPaneldeControl = imagenPaneldeControl;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.id=id;
    }


    public int getImagenPaneldeControl() {
        return imagenPaneldeControl;
    }

    public void setImagenPaneldeControl(int imagenPaneldeControl) {
        this.imagenPaneldeControl = imagenPaneldeControl;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
