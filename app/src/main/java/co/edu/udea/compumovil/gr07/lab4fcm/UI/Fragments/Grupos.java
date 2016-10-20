package co.edu.udea.compumovil.gr07.lab4fcm.UI.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import co.edu.udea.compumovil.gr07.lab4fcm.Adapter.AdaptadorRecyclerGrupos;
import co.edu.udea.compumovil.gr07.lab4fcm.R;
/**
 * Created by grupo07 on 18/10/2016.
 */
public class Grupos extends Fragment implements View.OnClickListener {

    //private ListView miListView;
    private RecyclerView recycler;
    private RecyclerView.Adapter adaptadorRecycler;
    private RecyclerView.LayoutManager lManager;
    private FirebaseDatabase miBD;
    private DatabaseReference root, grupos;
    private String nuevoGrupo;
    private ValueEventListener evento;

    public Grupos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contenedor = inflater.inflate(R.layout.fragment_grupos, container, false);
        FloatingActionButton addGrupo = (FloatingActionButton) contenedor.findViewById(R.id.grupos_floatingbtn_addgrupo_id);
        addGrupo.setOnClickListener(this);

        nuevoGrupo = "";
        miBD = FirebaseDatabase.getInstance();
        root = miBD.getReference().getRoot();
        grupos = root.child("grupos");
        final ArrayList<String> gruposList = new ArrayList<>();
        recycler = (RecyclerView) contenedor.findViewById(R.id.grupos_listView_id);

        lManager = new LinearLayoutManager(this.getContext());
        recycler.setLayoutManager(lManager);

        evento = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator i = dataSnapshot.getChildren().iterator();
                Set<String> grupos = new HashSet<String>();
                while (i.hasNext()) {
                    grupos.add(((DataSnapshot) i.next()).getKey());
                }
                gruposList.clear();
                gruposList.addAll(grupos);

                adaptadorRecycler.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        adaptadorRecycler = new AdaptadorRecyclerGrupos(gruposList);
        recycler.setAdapter(adaptadorRecycler);


        return contenedor;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (evento != null) {
            grupos.removeEventListener(evento);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        grupos.addValueEventListener(evento);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.grupos_floatingbtn_addgrupo_id:
                agregarGrupo().show();
                nuevoGrupo = "";
                break;
        }
    }

    public AlertDialog.Builder agregarGrupo() {
        AlertDialog.Builder grupo = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.titulo_dialog_grupo)
                .setMessage(R.string.mensajes_dialog_nuevoGrupo);
        final EditText txtGrupo = new EditText(this.getContext());
        txtGrupo.setMaxLines(1);
        grupo.setView(txtGrupo)
                .setPositiveButton(R.string.dialog_positiveOption, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        nuevoGrupo = txtGrupo.getText().toString();
                        Map<String, Object> map = new HashMap<>();
                        map.put(nuevoGrupo, "");
                        grupos.updateChildren(map);
                        Toast.makeText(getContext(), nuevoGrupo, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.dialog_negativeOption, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create();
        return grupo;
    }
}
