package co.edu.udea.compumovil.gr07.lab4fcm.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import co.edu.udea.compumovil.gr07.lab4fcm.Intefaces.Mensaje;
import co.edu.udea.compumovil.gr07.lab4fcm.R;

/**
 * Created by grupo07 on 18/10/2016.
 */

public class AdaptadorRecyclerChat extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Mensaje> mensajes;
    String usuarioActivoID;

    public AdaptadorRecyclerChat(List<Mensaje> mensajes) {
        this.mensajes = mensajes;
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            usuarioActivoID = user.getUid();
        }
        user = null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflador = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder holder;
        if (viewType == Mensaje.ORIGEN) {
            View vista = inflador.inflate(R.layout.cardview_chat_origen, parent, false);
            holder = new HolderOrigen(vista, parent.getContext());
        } else {
            View vista = inflador.inflate(R.layout.cardview_chat_destino, parent, false);
            holder = new HolderDestino(vista, parent.getContext());
        }
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        if (mensajes.get(position).getUsuarioID().equalsIgnoreCase(usuarioActivoID)) {
            return Mensaje.ORIGEN;
        } else {
            return Mensaje.DESTINO;
        }
    }

    public void rellenarAdapter(Mensaje datos) {
        mensajes.add(datos);
        notifyItemInserted(getItemCount() - 1);
    }

    public void rellenarPrimerVez(List<Mensaje> mensajes) {
        mensajes.clear();
        mensajes.addAll(mensajes);

        notifyItemInserted(getItemCount() - 1);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (mensajes.get(position).getUsuarioID().equalsIgnoreCase(usuarioActivoID)) {
            HolderOrigen origen = (HolderOrigen) holder;
            Mensaje datos = mensajes.get(position);
            origen.getMensaje().setText(datos.getMensaje());
            origen.getFecha().setText(datos.getFecha());
        } else {
            HolderDestino destino = (HolderDestino) holder;
            Mensaje datos = mensajes.get(position);
            destino.getMensaje().setText(datos.getMensaje());
            destino.getUsuario().setText(datos.getUsuario());
            destino.getFecha().setText(datos.getFecha());
        }

    }

    @Override
    public int getItemCount() {
        return mensajes.size();
    }

    public static class HolderDestino extends RecyclerView.ViewHolder {
        Context contextoApp;
        private TextView mensaje, fecha, usuario;

        public TextView getMensaje() {
            return mensaje;
        }

        public TextView getFecha() {
            return fecha;
        }

        public TextView getUsuario() {
            return usuario;
        }

        public HolderDestino(View itemView, Context contextoApp) {
            super(itemView);
            mensaje = (TextView) itemView.findViewById(R.id.chat_destino_mensaje_id);
            fecha = (TextView) itemView.findViewById(R.id.chat_destino_fecha_id);
            usuario = (TextView) itemView.findViewById(R.id.chat_destino_usuario_id);
            this.contextoApp = contextoApp;
        }

    }

    public static class HolderOrigen extends RecyclerView.ViewHolder {
        Context contextoApp;
        private TextView mensaje, fecha;

        public TextView getMensaje() {
            return mensaje;
        }

        public TextView getFecha() {
            return fecha;
        }

        public HolderOrigen(View itemView, Context contextoApp) {
            super(itemView);
            this.contextoApp = contextoApp;
            mensaje = (TextView) itemView.findViewById(R.id.chat_origen_mensaje_id);
            fecha = (TextView) itemView.findViewById(R.id.chat_origen_fecha_id);
        }
    }
}
