package co.edu.udea.compumovil.gr07.lab4fcm.Intefaces;

/**
 * Created by grupo07 on 18/10/2016.
 */
public class chatComponet {

    private String usuarioActivoID, usuarioExternoID;
    private String codigoChat;

    public String getCodigoChat() {
        return codigoChat;
    }

    public chatComponet() {
    }

    public void setCodigoChat(String codigoChat) {
        this.codigoChat = codigoChat;
    }

    public String getUsuarioActivoID() {
        return usuarioActivoID;
    }

    public void setUsuarioActivoID(String usuarioActivoID) {
        this.usuarioActivoID = usuarioActivoID;
    }

    public String getUsuarioExternoID() {
        return usuarioExternoID;
    }

    public void setUsuarioExternoID(String usuarioExternoID) {
        this.usuarioExternoID = usuarioExternoID;
    }

    public chatComponet(String usuarioActivoID, String usuarioExternoID) {

        this.usuarioActivoID = usuarioActivoID;
        this.usuarioExternoID = usuarioExternoID;
    }
}
