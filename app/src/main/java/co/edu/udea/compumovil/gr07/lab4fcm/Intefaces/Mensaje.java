package co.edu.udea.compumovil.gr07.lab4fcm.Intefaces;

/**
 * Created by grupo07 on 18/10/2016.
 */

public class Mensaje {

    public static final int DESTINO = 0;
    public static final int ORIGEN = 1;


    private String mensaje, usuario, fecha;
    private String usuarioID;

    public Mensaje(String mensaje, String usuario, String fecha, String usuarioID) {
        this.mensaje = mensaje;
        this.usuario = usuario;
        this.fecha = fecha;
        this.usuarioID = usuarioID;
    }

    public Mensaje() {

    }

    public String getUsuarioID() {
        return usuarioID;
    }

    public void setUsuarioID(String usuarioID) {
        this.usuarioID = usuarioID;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

}
