package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio;

/**
 * Created by Usuario on 19/04/2018.
 */

public class EntidadOrganizacion {

    private int id;
    private String imagen;
    private String titulo, estado;

    public EntidadOrganizacion(int id, String titulo, String estado,String imagen) {
        this.id = id;
        this.titulo = titulo;
        this.estado = estado;
        this.imagen=imagen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
