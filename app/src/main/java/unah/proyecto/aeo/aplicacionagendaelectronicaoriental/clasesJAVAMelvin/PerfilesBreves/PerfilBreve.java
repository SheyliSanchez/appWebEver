package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin.PerfilesBreves;

import android.database.Cursor;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.provider.*;

/**
 * Created by melvinrivera on 21/2/18.
 */

public class PerfilBreve {
    /**********************************************************************************************
     *                                       DECLARACIÓN DE VARIABLES
     **********************************************************************************************/
    public String nombre;
    public String imagen;
    public String numeroTelefono;
    public String direccion;
    public int id;

    /**********************************************************************************************
     *                                      CONSTRUCTOR
     **********************************************************************************************/

    public PerfilBreve(Cursor cursor) {
        this.nombre = cursor.getString(cursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_NOMBRE));
        this.imagen = cursor.getString(cursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_IMAGEN_PATH));
        if(cursor.getString(cursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_NUMERO_TELEFONO)).isEmpty()){
            this.numeroTelefono = cursor.getString(cursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_NUMERO_CELULAR));
        }else{
            this.numeroTelefono = cursor.getString(cursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_NUMERO_TELEFONO));
        }

        this.direccion = cursor.getString(cursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_NOMBRE_REGION));
        this.id = cursor.getInt(cursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_PERFILID));
    }
    /**********************************************************************************************
     *                                      CONSTRUCTOR SIN PARAMETROS
     **********************************************************************************************/

    public PerfilBreve(){

    }

    /**********************************************************************************************
     *                                  GETTERS Y SETTERS
     **********************************************************************************************/

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}