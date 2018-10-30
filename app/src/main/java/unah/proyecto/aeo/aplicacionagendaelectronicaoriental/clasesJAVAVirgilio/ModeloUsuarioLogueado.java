package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio;

public class ModeloUsuarioLogueado {
    private int id_logueado;
    private int rol_logueado;
    private String token;

    public ModeloUsuarioLogueado(int id_logueado, int rol_logueado, String token) {
        this.id_logueado = id_logueado;
        this.rol_logueado = rol_logueado;
        this.token = token;
    }

    public int getId_logueado() {
        return id_logueado;
    }

    public void setId_logueado(int id_logueado) {
        this.id_logueado = id_logueado;
    }

    public int getRol_logueado() {
        return rol_logueado;
    }

    public void setRol_logueado(int rol_logueado) {
        this.rol_logueado = rol_logueado;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
