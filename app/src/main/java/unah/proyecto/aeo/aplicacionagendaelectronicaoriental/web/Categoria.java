package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.web;

/**
 * Created by melvinrivera on 19/4/18.
 */

public class Categoria {
    private String ID_CATEGORIA, NOMBRE_CATEGORIA, IMAGEN_CATEGORIA, CANTIDAD;

    public Categoria() {

    }

    public String getCANTIDAD() {
        return CANTIDAD;
    }

    public void setCANTIDAD(String CANTIDAD) {
        this.CANTIDAD = CANTIDAD;
    }

    public String getID_CATEGORIA() {
        return ID_CATEGORIA;
    }

    public void setID_CATEGORIA(String ID_CATEGORIA) {
        this.ID_CATEGORIA = ID_CATEGORIA;
    }

    public String getNOMBRE_CATEGORIA() {
        return NOMBRE_CATEGORIA;
    }

    public void setNOMBRE_CATEGORIA(String NOMBRE_CATEGORIA) {
        this.NOMBRE_CATEGORIA = NOMBRE_CATEGORIA;
    }

    public String getIMAGEN_CATEGORIA() {
        return IMAGEN_CATEGORIA;
    }

    public void setIMAGEN_CATEGORIA(String IMAGEN_CATEGORIA) {
        this.IMAGEN_CATEGORIA = IMAGEN_CATEGORIA;
    }
}
