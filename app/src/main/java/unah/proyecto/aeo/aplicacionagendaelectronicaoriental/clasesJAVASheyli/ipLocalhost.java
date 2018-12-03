package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVASheyli;

public class ipLocalhost {

    private static final String ip = "http://192.168.0.105:8090/WebEver/public/";
    private static final  String BASE_URL=ip.substring(0,24);
    public String getIp() {
        return ip;
    }
    public String getBASE_URL() {
        return BASE_URL;
    }

}
