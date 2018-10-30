package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.web;

import org.json.JSONObject;

/**
 * Created by melvinrivera on 19/4/18.
 */

public class CategoriaParser {
    public static Categoria parse(JSONObject jsonCategoria) {
        final String BGS_id_categoria  = "id_categoria";
        final String BGS_nombre_categoria ="nombre_categoria";
        final String BGS_imagen_categoria ="imagen_categoria";
        final String BGS_cantidad ="coun";

        Categoria categoria = new Categoria();
        categoria.setID_CATEGORIA(jsonCategoria.optString(BGS_id_categoria));
        categoria.setNOMBRE_CATEGORIA(jsonCategoria.optString(BGS_nombre_categoria));
        categoria.setIMAGEN_CATEGORIA(jsonCategoria.optString(BGS_imagen_categoria));
        categoria.setCANTIDAD(jsonCategoria.optString(BGS_cantidad));

        return categoria;
    }
}
