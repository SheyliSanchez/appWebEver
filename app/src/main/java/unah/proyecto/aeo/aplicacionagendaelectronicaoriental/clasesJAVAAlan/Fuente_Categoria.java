package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.provider.CategoriasContract;

/**
 * Created by alan fabricio on 21/02/2018.
 */


public class Fuente_Categoria {
    String Titulo;
    String imagen;
    int cantidad;
    int id;




    public Fuente_Categoria(Cursor cursor) {
        this.Titulo = cursor.getString(cursor.getColumnIndex(CategoriasContract.CategoriasEntry.COLUMN_NOMBRE_CATEGORIA));;
        this.imagen = cursor.getString(cursor.getColumnIndex(CategoriasContract.CategoriasEntry.COLUMN_IMAGEN_CATEGORIA));;
        this.cantidad = cursor.getInt(cursor.getColumnIndex(CategoriasContract.CategoriasEntry.COLUMN_CANTIDAD));
        this.id= cursor.getInt(cursor.getColumnIndex(CategoriasContract.CategoriasEntry.COLUMN_ID_CATEGORIA));

    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

