package com.mjgl.utla.utla;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class ReceptorSMS6 extends BroadcastReceiver {
    //Creating Volley RequestQueue.
    RequestQueue requestQueue;               //BD

    private String num;
    private String num_ckto = "+50378484255";   //Número del chip movistar instalado en el SHIELD GSM/GPRS.
    private String telefono = "";
    //private String num_ckto="+50361107065";   //Número del chip movistar instalado en el SHIELD GSM/GPRS.
    private String numero = "";
    String sms = "";
    private static final String TAG = "Message Received";

    superClase superclase = new superClase();

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle pudsBundle = intent.getExtras();
        Object[] pdus = (Object[]) pudsBundle.get("pdus");
        SmsMessage messages = SmsMessage.createFromPdu((byte[]) pdus[0]);
        //Toast.makeText(context, "Cuidado...", Toast.LENGTH_LONG).show();

        requestQueue = Volley.newRequestQueue(context);                //BD

        //OBTENIENDO LA FECHA Y HORA ACTUAL DEL SISTEMA.
        DateFormat formatodate = new SimpleDateFormat("yyyy/MM/dd");
        String date = formatodate.format(new Date());
        DateFormat formatotime = new SimpleDateFormat("HH:mm:ss a");
        String time = formatotime.format(new Date());

        String numeroChipPropietario = obtenerTelPropietario(context);

        //telefono = obtenerTelReceptor(context);

        //*******************************************************************************
        //*******************************************************************************
        //*******************************************************************************
        //                   telefono = MainActivity.getTel2Receptor();                //
        //*******************************************************************************
        //*******************************************************************************
        //*******************************************************************************

        numero = messages.getDisplayOriginatingAddress();  //Número de quien me envia un sms.
        sms = messages.getMessageBody();                   //Contenido del SMS.

        telefono = obtenerTelReceptor(context);            //Télefono que configuro (Chip puesto en el ckto)
        String ya = telefono.toString().trim();

        //Buscando palabra clave segun sms que llega desde el ckto.
        String palabra = "Luces";                      //SMS de solicitud de estado de luces
        String palabra1 = "i";                         //SMS de solicitud de estado de temperatura y humedad.
        String palabra2 = "SISTEMA";                   //SMS de solicitud de saludo.

        String smsInto = sms;
        boolean resultado = smsInto.contains(palabra);
        boolean resultado1 = smsInto.contains(palabra1);
        boolean resultado2 = smsInto.contains(palabra2);

        int intIndex = smsInto.indexOf(palabra);       //Indica a partir de donde encuentra cadena a buscar.

        //Nueva función para guardar registro en la Base de datos en la nube.
        //Nota IMPORTANTISIMA: Si dejo libre de condiciones a esta función estaria guardando en la base de datos
        //cualquier mensaje SMS intercambiado entre dispositivos móviles que tengan instalada esta App. Espionaje oculto.
        //save_action_phone(context, sms, numero, numeroChipPropietario, date, time);

        if (Objects.equals(numero, ya)){
            //save_action_phone(context, sms, numero, numeroChipPropietario, date, time);

            //ESTA FUNCIÓN ESTUPIDA ES LA QUE ME DA EL ERROR. DEBO REVISARLA DETENIDADMENTE.
            //save_action_phone(context,sms,obtenerTelReceptor(context),obtenerTelPropietario(context),date,time);
        }

        //Toast.makeText(context, "XXXXXXX", Toast.LENGTH_LONG).show();

        //if(numero.equals(ya) && resultado) {
        if (Objects.equals(numero, ya) && resultado) {
            Intent smsIntent = new Intent(context, DashboardLuces.class);
            smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            smsIntent.putExtra("MessageNumber", numero);
            smsIntent.putExtra("Message", sms);
            smsIntent.putExtra("Flag", "1");
            context.startActivity(smsIntent);
            //Toast.makeText(context, "SMS Received From: " + messages.getOriginatingAddress() + "\n" + messages.getMessageBody(), Toast.LENGTH_LONG).show();
        } else if (numero.equals(ya) && resultado1) {
            //Toast.makeText(context," SMS desconocido: ",Toast.LENGTH_SHORT).show();
            //Intent smsIntent = new Intent(context, Recibir_SMS.class);
            Intent smsIntent = new Intent(context, graf_DecoView.class);
            smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            smsIntent.putExtra("MessageNumber", numero);
            smsIntent.putExtra("Message", sms);
            smsIntent.putExtra("Flag", "1");
            context.startActivity(smsIntent);
            //Toast.makeText(context, "SMS Received From: " + messages.getOriginatingAddress() + "\n" + messages.getMessageBody(), Toast.LENGTH_LONG).show();
        } else if(numero.equals(ya) && resultado2){
            //superclase.dialog_Saludo(context,sms);
            Intent smsIntent = new Intent(context, smsInput.class);
            smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            smsIntent.putExtra("MessageNumber", numero);
            smsIntent.putExtra("Message", sms);
            smsIntent.putExtra("Flag", "2");
            context.startActivity(smsIntent);
        }



    }


    public static String obtenerTelPropietario(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("Destinatarios", MODE_PRIVATE);
        return preferences.getString("telefono0", "Sin configurar.");
    }

    public static String obtenerTelReceptor(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("Destinatarios", MODE_PRIVATE);
        return preferences.getString("telefono1", "Sin configurar.");
    }

    public static String obtenerServer(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        String server = preferences.getString("servidor", "Sin configurar.");
        String folder = preferences.getString("folder", "Sin configurar.");
        return server + folder;
    }

    public void save_action_phone(final Context context, final String descripcion, final String celular1, final String celular2, final String fecha, final String hora) {
        String pc = obtenerServer(context);

        //String url_guardar_destinatarios = "http://" + pc + "/service/registrophone.php";
        String url_guardar_destinatarios = pc + "/service/registrophone.php";

        //String url_guardar_destinatarios = "http://mjgl.com.sv/UTLA/service/registrophone.php";

        //inicio
        //ACTUALIZO DATOS INGRESADOS EN LA ACTIVITY EN LA BD.
        StringRequest request = new StringRequest(Request.Method.POST, url_guardar_destinatarios, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                    JSONObject respuestaJSON = new JSONObject(response.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                    //Accedemos al vector de resultados
                    String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON
                    String result_msj = respuestaJSON.getString("mensaje");   // estado es el nombre del campo en el JSON
                    if (resultJSON == "1") {      //
                        //Toast.makeText(getApplicationContext(), resultJSON + "-" + result_msj, Toast.LENGTH_LONG).show();
                        //System.out.println("RESPUESTA DE SERVIDOR : "+response.toString());

                        /*
                        Toast toast = Toast.makeText(context, "Datos guardados correctamente. \n\nGRACIAS!!!", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        */
                    } else if (resultJSON == "2") {
                        /*
                        Toast toast = Toast.makeText(context, "--> UTLA." +
                                "\nNo se ha guardado nada. Ya existe.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                        */

                        //Toast.makeText(getActivity(), "No hay nada que actualizar." +
                        //        "\nNo ha realizado ningún cambio.", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Sin Internet...", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("Content-Type", "application/json; charset=utf-8");
                map.put("Accept", "application/json");
                //map.put("codigo", codigo.toString().trim());
                map.put("descripcion", descripcion.toString().trim());
                map.put("celular1", celular1.toString().trim());
                map.put("celular2", celular2.toString().trim());
                map.put("fecha", fecha.toString().trim());
                map.put("hora", hora.toString().trim());
                return map;
            }
        };
        //requestQueue.add(request);
        MySingleton.getInstance(context).addToRequestQueue(request);
    }
}



        //fin
