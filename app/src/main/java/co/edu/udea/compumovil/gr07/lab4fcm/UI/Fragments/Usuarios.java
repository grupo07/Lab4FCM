package co.edu.udea.compumovil.gr07.lab4fcm.UI.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr07.lab4fcm.Adapter.adaptadorRecyclerUsuarios;
import co.edu.udea.compumovil.gr07.lab4fcm.Intefaces.UsuarioInfo;
import co.edu.udea.compumovil.gr07.lab4fcm.R;

/**
 * Created by grupo07 on 18/10/2016.
 */

/**
 * A simple {@link Fragment} subclass.
 */
public class Usuarios extends Fragment {
    private RecyclerView recycler;
    private adaptadorRecyclerUsuarios adaptadorRecycler;
    private RecyclerView.LayoutManager lManager;
    private DatabaseReference myRef;
    private DatabaseReference usuarioRef;
    private List<UsuarioInfo> users;
    private FirebaseAuth mAuth;
    private View contenedor;

    public Usuarios() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contenedor = inflater.inflate(R.layout.fragment_usuarios, container, false);

        myRef = FirebaseDatabase.getInstance().getReference();
        usuarioRef = myRef.child(UsuarioInfo.CHILD);

        return contenedor;
    }

    @Override
    public void onStart() {
        lManager = new LinearLayoutManager(this.getContext());
        recycler = (RecyclerView) contenedor.findViewById(R.id.usuarios_recyclerView_id);
        users = new ArrayList<>();
        adaptadorRecycler = new adaptadorRecyclerUsuarios(users);
        recycler.setLayoutManager(lManager);
        recycler.setAdapter(adaptadorRecycler);
        usuarioRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    mAuth = FirebaseAuth.getInstance();
                    FirebaseUser usuarioActivo = mAuth.getCurrentUser();
                    UsuarioInfo usuarioTemp = dataSnapshot.getValue(UsuarioInfo.class);
                    if (!usuarioTemp.getUsuarioID().equals(usuarioActivo.getUid())) {
                        adaptadorRecycler.rellenarAdapter(usuarioTemp);
                        recycler.scrollToPosition(adaptadorRecycler.getItemCount());
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot.exists()) {
                    mAuth = FirebaseAuth.getInstance();
                    FirebaseUser usuarioActivo = mAuth.getCurrentUser();
                    UsuarioInfo usuarioTemp = dataSnapshot.getValue(UsuarioInfo.class);
                    if (!usuarioTemp.getUsuarioID().equals(usuarioActivo.getUid())) {
                        adaptadorRecycler.rellenarAdapter(usuarioTemp);
                        recycler.scrollToPosition(adaptadorRecycler.getItemCount());
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        super.onStart();
    }

}
