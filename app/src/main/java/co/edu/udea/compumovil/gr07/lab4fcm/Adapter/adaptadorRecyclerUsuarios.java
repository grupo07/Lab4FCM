package co.edu.udea.compumovil.gr07.lab4fcm.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import co.edu.udea.compumovil.gr07.lab4fcm.Intefaces.UsuarioInfo;
import co.edu.udea.compumovil.gr07.lab4fcm.R;
import co.edu.udea.compumovil.gr07.lab4fcm.UI.ChatPrivado;

/**
 * Created by grupo07 on 18/10/2016.
 */

public class adaptadorRecyclerUsuarios extends RecyclerView.Adapter<adaptadorRecyclerUsuarios.holderUsuarios> {
    private List<UsuarioInfo> usuariosRegistrados;

    public adaptadorRecyclerUsuarios(List<UsuarioInfo> usuarios) {
        usuariosRegistrados = usuarios;
    }

    @Override
    public holderUsuarios onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.carview_usuarios, parent, false);
        return new holderUsuarios(parent.getContext(), vista);
    }

    @Override
    public void onBindViewHolder(holderUsuarios holder, int position) {
        holder.getUsuario().setText(usuariosRegistrados.get(position).getNombre());
        holder.setUserID(usuariosRegistrados.get(position).getUsuarioID());
        int estado = usuariosRegistrados.get(position).getEstado();
        if (estado == UsuarioInfo.ESTADO_CONECTADO) {
            holder.getEstado().setText(holder.getContextoApp().getString(R.string.estado_conectado));
            holder.getEstado().setTextColor(holder.getContextoApp().getResources().getColor(R.color.conectado));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.getImgEstado().setImageDrawable(holder.getContextoApp().getDrawable(R.drawable.ic_estado_usuario_conexion_conectado));
            } else {
                holder.getImgEstado().setImageDrawable(holder.getContextoApp().getResources().getDrawable(R.drawable.ic_estado_usuario_conexion_conectado));
            }
        }
        if (estado == UsuarioInfo.ESTADO_DESCONECTADO) {
            holder.getEstado().setText(holder.getContextoApp().getString(R.string.estado_desconectado));
            holder.getEstado().setTextColor(holder.getContextoApp().getResources().getColor(R.color.desconectado));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.getImgEstado().setImageDrawable(holder.getContextoApp().getDrawable(R.drawable.ic_estado_usuario_conexion_desconectado));
            } else {
                holder.getImgEstado().setImageDrawable(holder.getContextoApp().getResources().getDrawable(R.drawable.ic_estado_usuario_conexion_desconectado));
            }
        }
    }

    public void rellenarAdapter(UsuarioInfo datos) {
        usuariosRegistrados.add(datos);
        notifyItemInserted(getItemCount() - 1);
    }

    @Override
    public int getItemCount() {
        return usuariosRegistrados.size();
    }

    public static class holderUsuarios extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView usuario;
        private TextView estado;
        private ImageView imgEstado;
        private String userID;
        private Context contextoApp;

        public String getUserID() {
            return userID;
        }

        public void setUserID(String userID) {
            this.userID = userID;
        }

        public holderUsuarios(Context contextoApp, View itemView) {
            super(itemView);
            usuario = (TextView) itemView.findViewById(R.id.usuarios_cardview_Nombre_id);
            estado = (TextView) itemView.findViewById(R.id.usuarios_cardview_estado_id);
            imgEstado = (ImageView) itemView.findViewById(R.id.usuarios_cardview_img_estado_id);
            this.contextoApp = contextoApp;
            itemView.setOnClickListener(this);
        }

        public TextView getUsuario() {
            return usuario;
        }

        public TextView getEstado() {
            return estado;
        }

        public ImageView getImgEstado() {
            return imgEstado;
        }

        public Context getContextoApp() {
            return contextoApp;
        }

        @Override
        public void onClick(View view) {
            //Toast.makeText(contextoApp, getUserID(), Toast.LENGTH_SHORT).show();
            Intent chatPrivado = new Intent(contextoApp, ChatPrivado.class);
            chatPrivado.putExtra(ChatPrivado.CHAT_PRIVATE_USERID, getUserID());
            chatPrivado.putExtra(ChatPrivado.CHAT_PRIVATE_USER, usuario.getText().toString());
            contextoApp.startActivity(chatPrivado);
        }
    }
}
