package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.web;

import org.json.JSONObject;

/**
 * Created by melvinrivera on 21/4/18.
 */

public class RegionParser {
    public static Region parse(JSONObject jsonRegion) {
        final String BGS_id_region  = "id_region";
        final String BGS_nombre_region ="nombre_region";


        Region region = new Region();
        region.setID_REGION(jsonRegion.optString(BGS_id_region));
        region.setNOMBRE_REGION(jsonRegion.optString(BGS_nombre_region));

        return region;
    }
}
