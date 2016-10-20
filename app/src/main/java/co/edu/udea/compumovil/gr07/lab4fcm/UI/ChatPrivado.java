package co.edu.udea.compumovil.gr07.lab4fcm.UI;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import co.edu.udea.compumovil.gr07.lab4fcm.Adapter.AdaptadorRecyclerChat;
import co.edu.udea.compumovil.gr07.lab4fcm.Intefaces.conexionInterface;
import co.edu.udea.compumovil.gr07.lab4fcm.Intefaces.Mensaje;
import co.edu.udea.compumovil.gr07.lab4fcm.Intefaces.chatComponet;
import co.edu.udea.compumovil.gr07.lab4fcm.R;

/**
 * Created by grupo07 on 18/10/2016.
 */
public class ChatPrivado extends AppCompatActivity implements conexionInterface {

    private static final String TAG = "ChatPivate";
    public static final String CHAT_PRIVATE_USERID = "chatPrivateUser";
    public static final String CHAT_PRIVATE_USER = "usuario";
    private Toolbar miToolbar;
    private RecyclerView recycler;
    private boolean isConnected;
    private AdaptadorRecyclerChat adaptadorRecycler;
    private RecyclerView.LayoutManager lManager;
    private FirebaseDatabase miBD;
    private DatabaseReference root, miembros, chatPrivadoGrupo, chatPrivado;
    private String usuarioActivo, usuarioActivoID;
    private ArrayList<chatComponet> miembrosChats;
    private String emisor, receptor;
    private String usuarioExternoID;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        miToolbar = (Toolbar) findViewById(R.id.chat_toolbar);
        setSupportActionBar(miToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        usuarioExternoID = getIntent().getStringExtra(CHAT_PRIVATE_USERID);
        String usuario = getIntent().getStringExtra(CHAT_PRIVATE_USER);
        TextView textToolbar = (TextView) miToolbar.findViewById(R.id.toolbar_subtitle);
        textToolbar.setText(usuario);
        if (user != null) {
            usuarioActivo = user.getDisplayName();
            usuarioActivoID = user.getUid();
        }
        miBD = FirebaseDatabase.getInstance();
        root = miBD.getReference().getRoot();
        miembros = root.child("miembros");
        chatPrivadoGrupo = root.child("chat-privado");
        chatPrivado = null;
        ArrayList<Mensaje> messages = new ArrayList<>();
        miembrosChats = new ArrayList<>();
        miembros.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator i = dataSnapshot.getChildren().iterator();
                while (i.hasNext()) {
                    DataSnapshot temp = ((DataSnapshot) i.next());
                    chatComponet nuevo = temp.getValue(chatComponet.class);
                    nuevo.setCodigoChat(temp.getKey());
                    miembrosChats.add(nuevo);
                }

                if (miembrosChats.isEmpty()) {
                    Map<String, Object> map = new HashMap<>();
                    String nuevoChat = chatPrivadoGrupo.push().getKey();
                    map.put(nuevoChat, "");
                    chatPrivadoGrupo.updateChildren(map);
                    Map<String, Object> mapMiembros = new HashMap<>();
                    chatComponet miembrosNuevos = new chatComponet(usuarioActivoID, usuarioExternoID);
                    mapMiembros.put(nuevoChat, miembrosNuevos);
                    miembros.updateChildren(mapMiembros);
                } else {
                    for (chatComponet datos : miembrosChats) {
                        if (usuarioExternoID.equals(datos.getUsuarioExternoID()) || usuarioExternoID.equals(datos.getUsuarioActivoID())) {
                            if (usuarioActivoID.equals(datos.getUsuarioExternoID()) || usuarioActivoID.equals(datos.getUsuarioActivoID())) {
                                chatPrivado = chatPrivadoGrupo.child(datos.getCodigoChat());
                                break;
                            }
                        }
                    }
                    if (chatPrivado == null) {
                        Map<String, Object> map = new HashMap<>();
                        String nuevoChat = chatPrivadoGrupo.push().getKey();
                        map.put(nuevoChat, "");
                        chatPrivadoGrupo.updateChildren(map);
                        Map<String, Object> mapMiembros = new HashMap<>();
                        chatComponet miembrosNuevos = new chatComponet(usuarioActivoID, usuarioExternoID);
                        mapMiembros.put(nuevoChat, miembrosNuevos);
                        miembros.updateChildren(mapMiembros);
                        chatPrivado = chatPrivadoGrupo.child(nuevoChat);
                        iniciarEventoChat(chatPrivado);
                    }
                    iniciarEventoChat(chatPrivado);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        recycler = (RecyclerView) findViewById(R.id.chat_recyclerview_id);

        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);

        adaptadorRecycler = new AdaptadorRecyclerChat(messages);
        recycler.setAdapter(adaptadorRecycler);
    }


    public void iniciarEventoChat(DatabaseReference chatPrivado) {
        chatPrivado.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    Mensaje nuevoMensaje = dataSnapshot.getValue(Mensaje.class);
                    adaptadorRecycler.rellenarAdapter(nuevoMensaje);
                    recycler.scrollToPosition(adaptadorRecycler.getItemCount() - 1);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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
    }


    @Override
    protected void onStart() {
        super.onStart();


    }

    public void enviarMensaje(View v) {
        TextView mensaje = (TextView) findViewById(R.id.chat_txt_mensaje_id);
        if (!mensaje.getText().toString().isEmpty()) {
            Map<String, Object> nuevoMensaje = new HashMap<>();
            String fecha = Usos.obtenerFechaActual(getApplicationContext());
            nuevoMensaje.put("mensaje", mensaje.getText().toString());
            nuevoMensaje.put("usuario", usuarioActivo);
            nuevoMensaje.put("fecha", fecha);
            nuevoMensaje.put("usuarioID", usuarioActivoID);
            String key = chatPrivado.push().getKey();
            DatabaseReference mss = chatPrivado.child(key);
            mss.updateChildren(nuevoMensaje);
            mensaje.setText("");

        }
    }

    @Override
    public void mostrarMensajeConexion() {
        validarConexion();
    }

    public void validarConexion() {
        ConnectivityManager conexion = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo infoConexion = conexion.getActiveNetworkInfo();
        FloatingActionButton btnSend = (FloatingActionButton) findViewById(R.id.chat_btn_enviar_id);
        if (infoConexion != null) {
            isConnected = infoConexion.isConnected();
        } else {
            isConnected = false;
        }
        final Snackbar sinConexion = Snackbar.make(findViewById(R.id.activity_chat), R.string.mensaje_error_conexion, Snackbar.LENGTH_INDEFINITE);
        if (!isConnected) {
            btnSend.setEnabled(false);
            sinConexion.setAction(R.string.login_snackbar_action, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sinConexion.dismiss();
                }
            }).show();
        } else {
            btnSend.setEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

