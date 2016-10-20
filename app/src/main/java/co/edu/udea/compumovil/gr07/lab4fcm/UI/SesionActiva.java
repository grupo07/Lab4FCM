package co.edu.udea.compumovil.gr07.lab4fcm.UI;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import co.edu.udea.compumovil.gr07.lab4fcm.Adapter.ViewPageAdapater;
import co.edu.udea.compumovil.gr07.lab4fcm.Intefaces.NetworkChangeReceiver;
import co.edu.udea.compumovil.gr07.lab4fcm.Intefaces.conexionInterface;
import co.edu.udea.compumovil.gr07.lab4fcm.Intefaces.UsuarioInfo;
import co.edu.udea.compumovil.gr07.lab4fcm.R;
import co.edu.udea.compumovil.gr07.lab4fcm.UI.Fragments.Grupos;
import co.edu.udea.compumovil.gr07.lab4fcm.UI.Fragments.Usuarios;
/**
 * Created by grupo07 on 18/10/2016.
 */
public class SesionActiva extends AppCompatActivity implements conexionInterface {

    private static final String TAG = "SesionActiva";

    private Toolbar miToolbar;
    private TabLayout miTabLayout;
    private ViewPager miViewPager;

    private boolean isConnected;
    private FirebaseAuth mAuth;
    private FirebaseUser usuarioActivo;
    private DatabaseReference myRef;

    @Override
    protected void onStart() {
        super.onStart();
        NetworkChangeReceiver.registrarReceiver(this);
        validarConexion();
    }

    @Override
    protected void onStop() {
        addUser(1);
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesion_activa);

        miToolbar = (Toolbar) findViewById(R.id.sesionActiva_toolbar);
        setSupportActionBar(miToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        miViewPager = (ViewPager) findViewById(R.id.sesionActiva_viewPager);
        configurarViewPager(miViewPager);

        miTabLayout = (TabLayout) findViewById(R.id.sesionActiva_TabLayout);
        miTabLayout.setupWithViewPager(miViewPager);

        mAuth = FirebaseAuth.getInstance();
        usuarioActivo = mAuth.getCurrentUser();

        addUser(0);
    }

    private void addUser(int estado) {
        myRef = FirebaseDatabase.getInstance().getReference();
        UsuarioInfo usuarioInfo = new UsuarioInfo();
        usuarioInfo.setNombre(usuarioActivo.getDisplayName());
        usuarioInfo.setEmail(usuarioActivo.getEmail());
        usuarioInfo.setEstado(estado);
        usuarioInfo.setUsuarioID(usuarioActivo.getUid());
        myRef.child(UsuarioInfo.CHILD).child(usuarioActivo.getUid()).setValue(usuarioInfo);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mostrarDialogoCerrarSesion().show();
    }

    public void configurarViewPager(ViewPager vP) {
        ViewPageAdapater adaptador = new ViewPageAdapater(getSupportFragmentManager());
        adaptador.agregarFragment(new Grupos(), getString(R.string.titulo_fragment_grupos));
        adaptador.agregarFragment(new Usuarios(), getString(R.string.titulo_fragment_usuarios));
        vP.setAdapter(adaptador);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_options_cerrarSesion:
                mostrarDialogoCerrarSesion().show();
                break;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void mostrarMensajeConexion() {
        validarConexion();
    }

    public void validarConexion() {
        ConnectivityManager conexion = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo infoConexion = conexion.getActiveNetworkInfo();

        if (infoConexion != null) {
            isConnected = infoConexion.isConnected();
        } else {
            isConnected = false;
        }
        final Snackbar sinConexion = Snackbar.make(findViewById(R.id.activity_sesion_activa), R.string.mensaje_error_conexion, Snackbar.LENGTH_INDEFINITE);
        if (!isConnected) {
            sinConexion.setAction(R.string.login_snackbar_action, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sinConexion.dismiss();
                }
            }).show();
        } else {
        }
    }

    public AlertDialog.Builder mostrarDialogoCerrarSesion() {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle(R.string.sesionActiva_titutlo_dialogo)
                .setMessage(R.string.sesionActiva_mensaje_dialogo)
                .setPositiveButton(R.string.sesionActiva_positiveOption_dialogo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        addUser(1);
                        Intent cerrar = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(cerrar);
                        mAuth.signOut();
                        finish();
                    }
                })
                .setNegativeButton(R.string.sesionActiva_negativeOption_dialogo, null)
                .create();
        return dialogo;
    }
}
