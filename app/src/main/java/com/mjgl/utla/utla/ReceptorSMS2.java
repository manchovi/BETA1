package com.mjgl.utla.utla;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsMessage;
import android.widget.Toast;

//OJO: Esta versión no me ha funcionado aun por la adición del dialog alert.
//Pendiente solventar esto.

public class ReceptorSMS2 extends BroadcastReceiver {
    AlertDialog.Builder dialogo;
    private String num;
    //private String UBIC="+50379663757";
    private String UBIC="+50361107065";
    private String numero="";
    String coordenadas = "";
    @Override
    public void onReceive(final Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        if(bundle !=null){
            Object[] sms = (Object[]) bundle.get("pdus");
            for (int i=0; i<sms.length;i++){
                SmsMessage mensajes = SmsMessage.createFromPdu((byte[]) (sms[i]));
                numero = mensajes.getDisplayOriginatingAddress();
                coordenadas = mensajes.getMessageBody().toString();
            }

            if(numero.equals(UBIC)){
                //Toast.makeText(context,"Posición recibida de: "+ numero +": "+coordenadas,Toast.LENGTH_SHORT).show();

                String mensaje = "SMS Entrante\n" +
                        " Número de origen: " + numero +
                        "\nContenido SMS: " + coordenadas + "\n";
                dialogo = new AlertDialog.Builder(context);
                dialogo.setIcon(R.drawable.ic_sms);
                //dialog.setTitle("COCESNA.");
                dialogo.setTitle("SMS CIRCUITO");
                dialogo.setMessage(mensaje);
                dialogo.setCancelable(false);
                dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo, int id) {

                    }
                });
                dialogo.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo, int id) {
                        //Toast toast = Toast.makeText(getApplicationContext(), "Operación Cancelada correctamente.", Toast.LENGTH_SHORT);
                        //toast.setGravity(Gravity.CENTER, 0, 0);
                        //toast.show();
                        Toast.makeText(context,"Operación Cancelada.",Toast.LENGTH_LONG).show();
                    }
                });
                dialogo.show();

            }else{
                //Toast.makeText(context," SMS desconocido: "+ numero +": "+coordenadas,Toast.LENGTH_SHORT).show();
            }

        }
    }
}
