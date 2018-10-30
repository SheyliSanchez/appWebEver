package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.web;

import org.json.JSONObject;

/**
 * Created by melvinrivera on 19/4/18.
 */

public class PerfilParser {
    public static Perfil parse(JSONObject jsonPerfil) {
        final String BGS_id_organizacion  = "id_contacto";
        final String BGS_nombre_organizacion ="nombre_organizacion";
        final String BGS_numero_telefono ="numero_fijo";
        final String BGS_numero_celular ="numero_movil";
        final String BGS_direccion ="direccion";
        final String BGS_e_mail ="e_mail";
        final String BGS_descripcion ="descripcion_organizacion";
        final String BGS_categoria ="id_categoria";
        final String BGS_region ="nombre_region";
        final String BGS_imagen_path ="imagen";
        final String BGS_id_region ="id_region";
        final String BGS_latitud ="latitud";
        final String BGS_longitud ="longitud";
        final String BGS_estado ="id_estado";
        Perfil perfil = new Perfil();
        perfil.setPERFILID(jsonPerfil.optString(BGS_id_organizacion));
        perfil.setNOMBRE(jsonPerfil.optString(BGS_nombre_organizacion));
        perfil.setNUMERO_TELEFONO(jsonPerfil.optString(BGS_numero_telefono));
        perfil.setNUMERO_CELULAR(jsonPerfil.optString(BGS_numero_celular));
        perfil.setDIRECCION(jsonPerfil.optString(BGS_direccion));
        perfil.setE_MAIL(jsonPerfil.optString(BGS_e_mail));
        perfil.setDESCRIPCION(jsonPerfil.optString(BGS_descripcion));
        perfil.setCATEGORIA(jsonPerfil.optString(BGS_categoria));
        perfil.setNOMBRE_REGION(jsonPerfil.optString(BGS_region));
        perfil.setIMAGEN_PATH(jsonPerfil.optString(BGS_imagen_path));
        perfil.setID_REGION(jsonPerfil.optString(BGS_id_region));
        perfil.setLATITUD(jsonPerfil.optString(BGS_latitud));
        perfil.setLONGITUD(jsonPerfil.optString(BGS_longitud));
        perfil.setESTADO(jsonPerfil.optString(BGS_estado));
        return perfil;
    }
}
