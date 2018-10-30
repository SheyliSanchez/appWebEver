package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by melvinrivera on 19/4/18.
 */

public class CategoriasContract {
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

    public static final String PATH_CATEGORIAS = "categorias-path";

    public static class CategoriasEntry implements BaseColumns {
        /** The content URI to access the perfil data in the provider */

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CATEGORIAS).build();
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_CATEGORIAS;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_CATEGORIAS;


        /** Name of database table for perfiles */
        public final static String TABLE_NAME="categorias";

        public final static String _ID=BaseColumns._ID;

        public static final String COLUMN_ID_CATEGORIA="id_categoria";
        public static final String COLUMN_NOMBRE_CATEGORIA="nombre_categoria";
        public static final String COLUMN_IMAGEN_CATEGORIA="imagen_categoria";
        public static final String COLUMN_CANTIDAD="count";

    }
}
