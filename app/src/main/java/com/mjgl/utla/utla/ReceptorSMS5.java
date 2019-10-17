package com.mjgl.utla.utla;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class ReceptorSMS5 extends BroadcastReceiver {
    private String num;

    //private String num_ckto="+50378484255";   //Número del chip movistar instalado en el SHIELD GSM/GPRS.
    private String num_ckto="+50361107065";   //Número del chip movistar instalado en el SHIELD GSM/GPRS.

    private String numero="";
    String sms = "";

    private static final String TAG = "Message Received";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle pudsBundle = intent.getExtras();
        Object[] pdus = (Object[])pudsBundle.get("pdus");
        SmsMessage messages = SmsMessage.createFromPdu((byte[])pdus[0]);

        //SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        //Log.e("hi", settings.getString("username", ""));

        numero = messages.getDisplayOriginatingAddress();  //Número de quien me envia un sms.
        sms = messages.getMessageBody();                   //Contenido del SMS.

        //Buscando palabra clave segun sms que llega desde el ckto.
        String palabra = "Luces";   //SMS de solicitud de estado de luces
        String palabra1 = "Estado"; //SMS de solicitud de estado de temperatura y humedad.
        String smsInto = sms;
        boolean resultado = smsInto.contains(palabra);
        boolean resultado1 = smsInto.contains(palabra1);
        int intIndex = smsInto.indexOf(palabra);       //Indica a partir de donde encuentra cadena a buscar.

        if(numero.equals(num_ckto) && resultado) {
            Intent smsIntent = new Intent(context, DashboardLuces.class);
            smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            smsIntent.putExtra("MessageNumber", numero);
            smsIntent.putExtra("Message", sms);
            smsIntent.putExtra("Flag", "1");
            context.startActivity(smsIntent);
            //Toast.makeText(context, "SMS Received From: " + messages.getOriginatingAddress() + "\n" + messages.getMessageBody(), Toast.LENGTH_LONG).show();
        }else if(numero.equals(num_ckto) && resultado1){
            //Toast.makeText(context," SMS desconocido: ",Toast.LENGTH_SHORT).show();
            //Intent smsIntent = new Intent(context, Recibir_SMS.class);
            Intent smsIntent = new Intent(context, graficaBarrasTH.class);
            smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            smsIntent.putExtra("MessageNumber", numero);
            smsIntent.putExtra("Message", sms);
            smsIntent.putExtra("Flag", "1");
            context.startActivity(smsIntent);
            //Toast.makeText(context, "SMS Received From: " + messages.getOriginatingAddress() + "\n" + messages.getMessageBody(), Toast.LENGTH_LONG).show();
        }
    }

}
