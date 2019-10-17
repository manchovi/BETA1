package com.mjgl.utla.utla;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;
//Funciona perfectamente para recibir el SMS y mostrarlo en un TextView, dentro de un activity
//Recibir_SMS. Esta versión captura cualquier SMS de entrada y lo muestra en la activity.
public class ReceptorSMS3 extends BroadcastReceiver {
    private String num;
    private String UBIC="+50361107065";
    private String numero="";
    String coordenadas = "";

    private static final String TAG = "Message Received";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle pudsBundle = intent.getExtras();
        Object[] pdus = (Object[])pudsBundle.get("pdus");
        SmsMessage messages = SmsMessage.createFromPdu((byte[])pdus[0]);
        Intent smsIntent = new Intent(context, Recibir_SMS.class);
        smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        smsIntent.putExtra("MessageNumber", messages.getOriginatingAddress());
        smsIntent.putExtra("Message", messages.getMessageBody());
        context.startActivity(smsIntent);
        Toast.makeText(context, "SMS Received From: " + messages.getOriginatingAddress()+"\n"+messages.getMessageBody(),Toast.LENGTH_LONG).show();
    }

}
