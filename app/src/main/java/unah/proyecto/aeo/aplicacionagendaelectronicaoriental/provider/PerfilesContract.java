
package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by melvinrivera on 14/4/18.
 */

public class PerfilesContract {
    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String  CONTENT_AUTHORITY = "unah.proyecto.aeo.aplicacionagendaelectronicaoriental";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+ CONTENT_AUTHORITY);

    public static final String PATH_PERFILES = "perfiles-path";

    public static class ContactosEntry implements BaseColumns{
        /** The content URI to access the perfil data in the provider */

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PERFILES).build();
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_PERFILES;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_PERFILES;


        /** Name of database table for perfiles */
        public final static String TABLE_NAME="perfiles";

        public final static String _ID=BaseColumns._ID;


        public static final String COLUMN_PERFILID="id_contacto";
        public static final String COLUMN_NOMBRE="nombre_organizacion";
        public static final String COLUMN_NUMERO_TELEFONO="numero_fijo";
        public static final String COLUMN_NUMERO_CELULAR="numero_movil";
        public static final String COLUMN_DIRECCION="direccion";
        public static final String COLUMN_E_MAIL="e_mail";
        public static final String COLUMN_DESCRIPCION="descripcion_organizacion";
        public static final String COLUMN_CATEGORIA="id_categoria";
        public static final String COLUMN_IMAGEN_PATH="imagen";
        public static final String COLUMN_NOMBRE_REGION="nombre_region";
        public static final String COLUMN_LATITUD="latitud";
        public static final String COLUMN_LONGITUD="longitud";
        public static final String COLUMN_ESTADO="id_estado";
        public static final String COLUMN_ID_REGION="id_region";




    }

}
