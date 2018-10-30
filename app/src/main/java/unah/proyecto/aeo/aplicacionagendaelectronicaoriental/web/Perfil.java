package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.web;

/**
 * Created by melvinrivera on 17/4/18.
 */

public class Perfil {
    private String PERFILID;
    private  String NOMBRE;
    private  String NUMERO_TELEFONO;
    private  String NUMERO_CELULAR;
    private  String DIRECCION;
    private  String E_MAIL;
    private  String DESCRIPCION;
    private  String CATEGORIA;
    private  String IMAGEN_PATH;
    private  String NOMBRE_REGION;
    private  String LATITUD;
    private  String LONGITUD;
    private  String ESTADO;
    private  String ID_REGION;


    public Perfil() {

    }

    public String getPERFILID() {
        return PERFILID;
    }

    public void setPERFILID(String PERFILID) {
        this.PERFILID = PERFILID;
    }

    public String getNOMBRE() {
        return NOMBRE;
    }

    public void setNOMBRE(String NOMBRE) {
        this.NOMBRE = NOMBRE;
    }

    public String getNUMERO_TELEFONO() {
        return NUMERO_TELEFONO;
    }

    public void setNUMERO_TELEFONO(String NUMERO_TELEFONO) {
        this.NUMERO_TELEFONO = NUMERO_TELEFONO;
    }

    public String getNUMERO_CELULAR() {
        return NUMERO_CELULAR;
    }

    public void setNUMERO_CELULAR(String NUMERO_CELULAR) {
        this.NUMERO_CELULAR = NUMERO_CELULAR;
    }

    public String getDIRECCION() {
        return DIRECCION;
    }

    public void setDIRECCION(String DIRECCION) {
        this.DIRECCION = DIRECCION;
    }

    public String getE_MAIL() {
        return E_MAIL;
    }

    public void setE_MAIL(String e_MAIL) {
        E_MAIL = e_MAIL;
    }

    public String getDESCRIPCION() {
        return DESCRIPCION;
    }

    public void setDESCRIPCION(String DESCRIPCION) {
        this.DESCRIPCION = DESCRIPCION;
    }

    public String getCATEGORIA() {
        return CATEGORIA;
    }

    public void setCATEGORIA(String CATEGORIA) {
        this.CATEGORIA = CATEGORIA;
    }

    public String getIMAGEN_PATH() {
        return IMAGEN_PATH;
    }

    public void setIMAGEN_PATH(String IMAGEN_PATH) {
        this.IMAGEN_PATH = IMAGEN_PATH;
    }

    public String getNOMBRE_REGION() {
        return NOMBRE_REGION;
    }

    public void setNOMBRE_REGION(String NOMBRE_REGION) {
        this.NOMBRE_REGION = NOMBRE_REGION;
    }

    public String getLATITUD() {
        return LATITUD;
    }

    public void setLATITUD(String LATITUD) {
        this.LATITUD = LATITUD;
    }

    public String getLONGITUD() {
        return LONGITUD;
    }

    public void setLONGITUD(String LONGITUD) {
        this.LONGITUD = LONGITUD;
    }

    public String getESTADO() {
        return ESTADO;
    }

    public void setESTADO(String ESTADO) {
        this.ESTADO = ESTADO;
    }

    public String getID_REGION() {
        return ID_REGION;
    }

    public void setID_REGION(String ID_REGION) {
        this.ID_REGION = ID_REGION;
    }
}
