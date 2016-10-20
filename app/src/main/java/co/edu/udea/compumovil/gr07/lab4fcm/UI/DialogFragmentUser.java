package co.edu.udea.compumovil.gr07.lab4fcm.UI;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import co.edu.udea.compumovil.gr07.lab4fcm.Intefaces.dialogEvent;
import co.edu.udea.compumovil.gr07.lab4fcm.R;

/**
 * Created by grupo07 on 18/10/2016.
 */

public class DialogFragmentUser extends DialogFragment {

    private dialogEvent listener;

    public DialogFragmentUser() {
        super();
    }

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        return crearDialogLogin();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (dialogEvent) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    context.toString() +
                            " no implementó DialogEvent");

        }
    }

    public android.app.Dialog crearDialogLogin() {
        AlertDialog.Builder frame = new AlertDialog.Builder(getActivity());
        LayoutInflater inflador = getActivity().getLayoutInflater();
        final View vista = inflador.inflate(R.layout.dialog_login, null);
        frame.setView(vista);
        Button iniciarSesion = (Button) vista.findViewById(R.id.login_btn_iniciar_id);

        iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText correo = (EditText) vista.findViewById(R.id.login_correo_id);
                EditText clave = (EditText) vista.findViewById(R.id.login_clave_id);
                listener.iniciarSesion(correo, clave);
            }
        });

        Button crearCuenta = (Button) vista.findViewById(R.id.login_btn_crear_cuenta_id);
        crearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.crearCuenta();

            }
        });
        return frame.create();
    }
}

