package co.edu.udea.compumovil.gr07.lab4fcm.Intefaces;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import co.edu.udea.compumovil.gr07.lab4fcm.Intefaces.conexionInterface;
/**
 * Created by grupo07 on 18/10/2016.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {
    private static conexionInterface conex;
    private static final String TAG = "NetworkChangeReceiver";

    public NetworkChangeReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (conex != null) {
            conex.mostrarMensajeConexion();
        } else {
            Log.e(TAG, "onReceive: No registrado");
        }
    }

    public static void registrarReceiver(conexionInterface receiver) {
        conex = receiver;
    }

}
