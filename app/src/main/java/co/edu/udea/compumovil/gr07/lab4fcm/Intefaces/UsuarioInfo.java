package co.edu.udea.compumovil.gr07.lab4fcm.Intefaces;

/**
 * Created by grupo07 on 18/10/2016.
 */
public class UsuarioInfo {

    public static  String CHILD = "Users";
    public static final int ESTADO_CONECTADO = 0;
    public static final int ESTADO_DESCONECTADO = 1;

    private String nombre;
    private int estado;
    private String email;
    private String usuarioID;

    public String getUsuarioID() {
        return usuarioID;
    }

    public void setUsuarioID(String usuarioID) {
        this.usuarioID = usuarioID;
    }

    public UsuarioInfo() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
