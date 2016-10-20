package co.edu.udea.compumovil.gr07.lab4fcm.UI;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import co.edu.udea.compumovil.gr07.lab4fcm.Intefaces.NetworkChangeReceiver;
import co.edu.udea.compumovil.gr07.lab4fcm.Intefaces.conexionInterface;
import co.edu.udea.compumovil.gr07.lab4fcm.R;

import static co.edu.udea.compumovil.gr07.lab4fcm.UI.Usos.validarCampoCorreo;
import static co.edu.udea.compumovil.gr07.lab4fcm.UI.Usos.validarCampoVacio;
import static co.edu.udea.compumovil.gr07.lab4fcm.UI.Usos.validarConexionFirebase;
import static co.edu.udea.compumovil.gr07.lab4fcm.UI.Usos.validarContraseñaRepetida;
/**
 * Created by grupo07 on 18/10/2016.
 */
public class CrearCuenta extends AppCompatActivity implements conexionInterface {

    private static final String TAG = "CrearCuenta";
    private boolean isConnected;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);

        validarConexion();
        mAuth = FirebaseAuth.getInstance();

    }


    public void crearCuenta(View v) {
        final EditText nombreEdt = (EditText) findViewById(R.id.crearCuenta_edt_nombreUsuario_id);
        EditText correoEdt = (EditText) findViewById(R.id.crearCuenta_edt_correo_id);
        final EditText claveEdt = (EditText) findViewById(R.id.crearCuenta_edt_clave_id);
        EditText confirmClaveEdt = (EditText) findViewById(R.id.crearCuenta_edt_claveRep_id);
        final View vista = findViewById(R.id.activity_crear_cuenta);

        if (validarCampoVacio(nombreEdt, getApplicationContext()) & validarCampoVacio(correoEdt, getApplicationContext())
                & validarCampoVacio(claveEdt, getApplicationContext()) & validarCampoVacio(confirmClaveEdt, getApplicationContext())) {
            if (validarCampoCorreo(correoEdt, getApplicationContext()) &
                    validarContraseñaRepetida(claveEdt, confirmClaveEdt, getApplicationContext())) {
                final String correoTxt = correoEdt.getText().toString();
                final String claveTxt = claveEdt.getText().toString();
                mAuth.createUserWithEmailAndPassword(correoTxt, claveTxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mAuth.signInWithEmailAndPassword(correoTxt, claveTxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        UserProfileChangeRequest cambios = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(nombreEdt.getText().toString()).build();
                                        FirebaseUser usuario = mAuth.getCurrentUser();

                                        if (usuario != null) {
                                            usuario.updateProfile(cambios);
                                        }
                                        finish();
                                        mAuth.signOut();
                                    }
                                }
                            });
                        } else {
                            validarConexionFirebase(task, getApplicationContext(), claveEdt);
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthUserCollisionException e) {
                                Snackbar.make(vista, R.string.mensaje_error_usuarioExistente, Snackbar.LENGTH_SHORT).show();
                            } catch (Exception e) {

                            }
                        }
                    }
                });
            }
        } else {
            Snackbar.make(vista, R.string.mensaje_error_camposVacios, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        validarConexion();
        NetworkChangeReceiver.registrarReceiver(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
        Button crear = (Button) findViewById(R.id.crearCuenta_btn_crear_id);
        final Snackbar sinConexion = Snackbar.make(findViewById(R.id.activity_crear_cuenta), R.string.mensaje_error_conexion, Snackbar.LENGTH_INDEFINITE);
        if (!isConnected) {
            crear.setEnabled(false);
            sinConexion.setAction(R.string.login_snackbar_action, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sinConexion.dismiss();
                }
            }).show();
        } else {
            crear.setEnabled(true);
        }
    }
}
