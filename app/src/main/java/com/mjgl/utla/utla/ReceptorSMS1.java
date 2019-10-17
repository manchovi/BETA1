package com.mjgl.utla.utla;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class ReceptorSMS1 extends BroadcastReceiver {
    private String num;
    private String UBIC="+50379663757";
    private String numero="";
    String coordenadas = "";
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        if(bundle !=null){
            Object[] sms = (Object[]) bundle.get("pdus");
            for (int i=0; i<sms.length;i++){
                SmsMessage mensajes = SmsMessage.createFromPdu((byte[]) (sms[i]));
                numero = mensajes.getDisplayOriginatingAddress();
                coordenadas = mensajes.getMessageBody().toString();
            }

            if(numero.equals(UBIC)){
                Toast.makeText(context,"Posición recibida de: "+ numero +": "+coordenadas,Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context," SMS desconocido: "+ numero +": "+coordenadas,Toast.LENGTH_SHORT).show();
            }

        }
    }
}
