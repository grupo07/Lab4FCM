package co.edu.udea.compumovil.gr07.lab4fcm.UI;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.format.DateFormat;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import java.util.Calendar;
import java.util.Date;


/**
 * Created by grupo07 on 18/10/2016.
 */

public class Usos {

    private static final int LONGITUD_PASS_MIN = 8;


    public static boolean validarCampoVacio(EditText campo, Context appContext) {
        String valorcampo = campo.getText().toString();
        if (valorcampo.isEmpty()) {
            campo.setError(appContext.getString(R.string.mensaje_error_campoVacio));
            return false;
        }
        return true;
    }

    public static boolean validarCampoCorreo(EditText campo, Context appContext) {
        String valor = campo.getText().toString();
        if (!Patterns.EMAIL_ADDRESS.matcher(valor).matches()) {
            campo.setError(appContext.getString(R.string.mensaje_error_formatoInvalido_correo));
            return false;
        }
        return true;
    }

    public static boolean validarContraseñaRepetida(EditText contra, EditText repContra, Context appContext) {
        String contraseña = contra.getText().toString();
        String repContraseña = repContra.getText().toString();
        if (contraseña.length() < LONGITUD_PASS_MIN) {
            contra.setError(appContext.getString(R.string.mensaje_error_longitudMinima));
            return false;
        } else {
            if (!contraseña.equals(repContraseña)) {
                repContra.setError(appContext.getString(R.string.mensaje_error_NoCoinciden_contraseña));
                return false;
            }
        }
        return true;
    }

    public static boolean validarContraseña(EditText contra, Context appContext) {
        String contraseña = contra.getText().toString();
        if (contraseña.length() < LONGITUD_PASS_MIN) {
            contra.setError(appContext.getString(R.string.mensaje_error_longitudMinima));
            return false;
        }
        return true;
    }

    public static String obtenerFechaActual(Context contextoApp) {
        Calendar calendario = Calendar.getInstance();
        Date fecha = calendario.getTime();
        java.text.DateFormat fechaFormat = DateFormat.getDateFormat(contextoApp);
        java.text.DateFormat tiempoFormat = DateFormat.getTimeFormat(contextoApp);
        String fechanueva = fechaFormat.format(fecha) + " " + tiempoFormat.format(fecha);
        return fechanueva;
    }

    public static void validarConexionFirebase(@NonNull Task<AuthResult> task, Context appContext, EditText clave) {
        try {
            throw task.getException();
        } catch (FirebaseAuthInvalidUserException e) {
            Toast.makeText(appContext, R.string.mensaje_error_usuarioInvalido, Toast.LENGTH_SHORT).show();
        } catch (FirebaseAuthInvalidCredentialsException e) {
            if (e.getErrorCode().equals(MainActivity.PASSWORD_FIREBASE_ERROR)) {
                clave.setError(appContext.getString(R.string.mensaje_error_contraseñaIncorrecta));
                clave.requestFocus();
            }
        } catch (Exception e) {
        }
    }
}
