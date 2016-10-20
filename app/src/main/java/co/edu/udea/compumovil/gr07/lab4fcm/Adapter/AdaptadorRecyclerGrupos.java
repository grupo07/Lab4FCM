package co.edu.udea.compumovil.gr07.lab4fcm.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import co.edu.udea.compumovil.gr07.lab4fcm.R;
import co.edu.udea.compumovil.gr07.lab4fcm.UI.Chat;
/**
 * Created by grupo07 on 18/10/2016.
 */

public class AdaptadorRecyclerGrupos extends RecyclerView.Adapter<AdaptadorRecyclerGrupos.HolderGrupos> {
    ArrayList<String> grupos;

    public AdaptadorRecyclerGrupos(ArrayList<String> grupos) {
        this.grupos = grupos;
    }

    @Override
    public HolderGrupos onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_grupos, parent, false);
        return new HolderGrupos(parent.getContext(), vista);
    }

    @Override
    public void onBindViewHolder(HolderGrupos holder, int position) {
        holder.getGrupo().setText(grupos.get(position));
    }

    @Override
    public int getItemCount() {
        return grupos.size();
    }

    public static class HolderGrupos extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView grupo;
        private Context contextoApp;

        public Context getContextoApp() {
            return contextoApp;
        }

        public HolderGrupos(Context contextoApp, View itemView) {
            super(itemView);
            grupo = (TextView) itemView.findViewById(R.id.grupos_txt_grupo_id);
            itemView.setOnClickListener(this);
            this.contextoApp = contextoApp;
        }

        public TextView getGrupo() {
            return grupo;
        }


        @Override
        public void onClick(View view) {
            Toast.makeText(contextoApp, grupo.getText(), Toast.LENGTH_SHORT).show();
            Intent abrirChat = new Intent(this.getContextoApp(), Chat.class);
            abrirChat.putExtra(Chat.CHAT_GRUPO, grupo.getText().toString());
            contextoApp.startActivity(abrirChat);
        }
    }
}
