package com.mjgl.utla.utla;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class ReceptorSMS extends BroadcastReceiver {
    private int num;
    private int UBIC=615202383;
    private String numero="0";

    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.i("ReceptorSMS", "SMS recibido");
        //Bundle bundle = intent.getExtras();
        Bundle b = intent.getExtras();

        SmsMessage[] mensajes = null;
        String cadena = null;
        //if(bundle !=null){
        if (b != null) {
            Object[] objetos = (Object[])b.get("pdus");
            //Object[] pdus = (Object[]) b.get("pdus");
            //Object[] sms = (Object[]) bundle.get("pdus");
            mensajes = new SmsMessage[objetos.length];
            for (int i = 0; i < mensajes.length; i++) {
                mensajes[i] = SmsMessage.createFromPdu((byte[]) objetos[i]);
                cadena = "Número origen: " + mensajes[i].getOriginatingAddress();
                cadena += "\nSMS Contenido: ";
                cadena += mensajes[i].getMessageBody().toString();
                //cadena += "\n";
            }

            Toast.makeText(context,cadena,Toast.LENGTH_LONG).show();

            /*
            SmsMessage[] mensajes = new SmsMessage[pdus.length];
            for (int i = 0; i < mensajes.length; i++) {
                mensajes[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                String idMensaje = mensajes[i].getOriginatingAddress();
                String textoMensaje = mensajes[i].getMessageBody();
                Log.i("ReceptorSMS", "Remitente: " + idMensaje);
                Log.i("ReceptorSMS", "Mensaje: " + textoMensaje);
            }
            */

            /*
                for (int i=0; i<sms.length;i++){
                SmsMessage mensajes = SmsMessage.createFromPdu((byte[]) (sms[i]));
                String numero = mensajes.getDisplayOriginatingAddress();
                String coordenadas = mensajes.getMessageBody().toString();
                num = Integer.parseInt(numero);
                if(num == UBIC ){
                    Toast.makeText(context,"Posición recibida de: "+ numero +": "+coordenadas,Toast.LENGTH_SHORT).show();
                //Guardar en fichero cuando funcione...
                }
             */
        }
    }
}
