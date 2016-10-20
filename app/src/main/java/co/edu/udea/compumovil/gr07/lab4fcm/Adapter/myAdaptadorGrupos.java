package co.edu.udea.compumovil.gr07.lab4fcm.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import co.edu.udea.compumovil.gr07.lab4fcm.R;

/**
 * Created by grupo07 on 18/10/2016.
 */

public class myAdaptadorGrupos extends ArrayAdapter<String> implements View.OnClickListener {
    private final Context context;
    private String[] valores;

    public myAdaptadorGrupos(Context context, String[] valores) {
        super(context, R.layout.lista_grupos, valores);
        this.context = context;
        this.valores = valores;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflador = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vista = inflador.inflate(R.layout.lista_grupos, parent, false);
        TextView nombreGrupo = (TextView) vista.findViewById(R.id.grupos_txt_grupo_id);
        nombreGrupo.setText(valores[position]);
        vista.setOnClickListener(this);
        return vista;
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(context, view.toString(), Toast.LENGTH_SHORT).show();
    }
}
