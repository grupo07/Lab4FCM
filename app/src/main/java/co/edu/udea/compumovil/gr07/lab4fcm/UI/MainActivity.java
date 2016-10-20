package co.edu.udea.compumovil.gr07.lab4fcm.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import co.edu.udea.compumovil.gr07.lab4fcm.Intefaces.NetworkChangeReceiver;
import co.edu.udea.compumovil.gr07.lab4fcm.Intefaces.conexionInterface;
import co.edu.udea.compumovil.gr07.lab4fcm.Intefaces.dialogEvent;

/**
 * Created by grupo07 on 18/10/2016.
 */
public class MainActivity extends AppCompatActivity implements dialogEvent, conexionInterface {

    private DialogFragmentUser login;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean isConnected;
    private BroadcastReceiver conexionReceiver;
    private GoogleApiClient mgoogleAccount;
    private static final String TAG = "MainActivity";
    public static final String PASSWORD_FIREBASE_ERROR = "ERROR_WRONG_PASSWORD";
    private static final int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        conexionReceiver = new NetworkChangeReceiver();
        SignInButton googleBtn = (SignInButton) findViewById(R.id.login_btn_googleSignIn_id);
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        validarConexion();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.token_google)).requestEmail()
                .build();

        mgoogleAccount = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.e(TAG, "onConnectionFailed: " + connectionResult.getErrorMessage());
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser usuario = firebaseAuth.getCurrentUser();
                if (usuario != null) {
                    Intent inicio = new Intent(getApplicationContext(), SesionActiva.class);
                    startActivity(inicio);
                    finish();
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void abrirDialogLogin(View v) {
        if (login == null) {
            login = new DialogFragmentUser();
            login.show(getSupportFragmentManager(), "Login");
            return;
        }
        login.show(getSupportFragmentManager(), "Login");
    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mgoogleAccount);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void fireBaseAuthConGoogle(GoogleSignInAccount cuenta) {
        AuthCredential credencial = GoogleAuthProvider.getCredential(cuenta.getIdToken(), null);
        mAuth.signInWithCredential(credencial).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.mensaje_success_login, Toast.LENGTH_SHORT).show();
                    Intent inicio = new Intent(getApplicationContext(), SesionActiva.class);
                    startActivity(inicio);
                    if (login != null && login.isVisible()) {
                        login.dismiss();
                    }
                    finish();
                } else {
                    EditText clave = (EditText) findViewById(R.id.login_clave_id);
                    Usos.validarConexionFirebase(task, getApplicationContext(), clave);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (data != null) {
                GoogleSignInResult resultado = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (resultado.isSuccess()) {
                    GoogleSignInAccount cuenta = resultado.getSignInAccount();
                    fireBaseAuthConGoogle(cuenta);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        validarConexion();
        NetworkChangeReceiver.registrarReceiver(this);
    }

    @Override
    public void iniciarSesion(EditText correo, final EditText clave) {
        String correoValor = correo.getText().toString();
        String claveValor = clave.getText().toString();
        if (Usos.validarCampoVacio(correo, getApplicationContext()) & Usos.validarCampoVacio(clave, getApplicationContext())) {
            if (Usos.validarCampoCorreo(correo, getApplicationContext()) & Usos.validarContraseña(clave, getApplicationContext())) {
                mAuth.signInWithEmailAndPassword(correoValor, claveValor).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), R.string.mensaje_success_login, Toast.LENGTH_SHORT).show();
                            Intent inicio = new Intent(getApplicationContext(), SesionActiva.class);
                            startActivity(inicio);
                            login.dismiss();
                            finish();
                        } else {
                            Usos.validarConexionFirebase(task, getApplicationContext(), clave);
                        }
                    }
                });
            }
        } else {
            if (login != null && login.getView() != null) {
                Snackbar.make(login.getView(), R.string.mensaje_error_camposVacios, Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void crearCuenta() {
        login.dismiss();
        Intent crear = new Intent(this, CrearCuenta.class);
        startActivity(crear);
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
        Button iniciar = (Button) findViewById(R.id.login_btn_iniciarCorreoYContra_id);
        SignInButton iniciarGoogle = (SignInButton) findViewById(R.id.login_btn_googleSignIn_id);
        final Snackbar sinConexion = Snackbar.make(findViewById(R.id.activity_main), R.string.mensaje_error_conexion, Snackbar.LENGTH_INDEFINITE);
        if (!isConnected) {
            iniciar.setEnabled(false);
            iniciarGoogle.setEnabled(false);
            sinConexion.setAction(R.string.login_snackbar_action, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sinConexion.dismiss();
                }
            }).show();
        } else {
            iniciar.setEnabled(true);
            iniciarGoogle.setEnabled(true);
        }
    }
}
