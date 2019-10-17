package com.mjgl.utla.utla;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class ReceptorSMS4 extends BroadcastReceiver {
    private String num;
    private String num_ckto="+50378484255";   //Número del chip movistar instalado en el SHIELD GSM/GPRS.
    private String numero="";
    String sms = "";

    private static final String TAG = "Message Received";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle pudsBundle = intent.getExtras();
        Object[] pdus = (Object[])pudsBundle.get("pdus");
        SmsMessage messages = SmsMessage.createFromPdu((byte[])pdus[0]);

        numero = messages.getDisplayOriginatingAddress();  //Número de quien me envia un sms.
        sms = messages.getMessageBody();                   //Contenido del SMS.

        if(numero.equals(num_ckto)) {
            Intent smsIntent = new Intent(context, Recibir_SMS.class);
            smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            smsIntent.putExtra("MessageNumber", numero);
            smsIntent.putExtra("Message", sms);
            context.startActivity(smsIntent);
            Toast.makeText(context, "SMS Received From: " + messages.getOriginatingAddress() + "\n" + messages.getMessageBody(), Toast.LENGTH_LONG).show();
        }else{
            //Toast.makeText(context," SMS desconocido: ",Toast.LENGTH_SHORT).show();
        }
    }

}
