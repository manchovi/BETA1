package com.mjgl.utla.utla;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//import static android.content.Context.MODE_PRIVATE;

//public class superClase extends MainActivity{
public class superClase{

    AlertDialog.Builder dialogo, dialogo1;
    private ProgressDialog pd;
    //Variable para crear mis propios cuadros de dialogo.
    Dialog myDialog;
    int estadoAll=0;
    int estadof1=0;int estadof2=0;int estadof3=0;int estadof4=0;
    int estadof5=0;int estadof6=0;int estadof7=0;int estadof8=0;

    ToggleButton sw0,sw1,sw2,sw3,sw4,sw5,sw6,sw7,sw8;
    ImageView f_all,f1,f2,f3,f4,f5,f6,f7,f8;
    /*
    public superClase (Context context){
        super(context);
    }
    */

    /*Funciones para enviar SMS con temperatura y humedad relativa a un destinario*/
    public void confirmar(final Context context){
        //android.app.AlertDialog.Builder dialogo1 = new android.app.AlertDialog.Builder(getContext());
        dialogo1 = new AlertDialog.Builder(context);
        dialogo1.setIcon(R.drawable.ic_sms);
        dialogo1.setTitle("Advertencia.");
        dialogo1.setMessage("¿Realmente desea compartir los datos con el phone registrado?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                EnviarSMS(context);
                /*Toast toast = Toast.makeText(context, "Probando esto...", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();*/
            }
        });
        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                Toast toast = Toast.makeText(context, "PROCESO CANCELADO CORRECTAMENTE.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
        dialogo1.show();
    }

    public String infoSharedPreferences(Context context){
        //Buscando datos en archivo credenciales.xml
        SharedPreferences preferences = context.getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        String server = preferences.getString("servidor","Sin configurar aun.");
        String folder = preferences.getString("folder","Sin configurar aun.");
        return server + folder;
    }

    public String infoConfDestinatarioTel2(Context context){
        SharedPreferences preferences = context.getSharedPreferences("Destinatarios", Context.MODE_PRIVATE);
        String t2 = preferences.getString("telefono2","Sin configurar.");
        return t2;
    }

    public void EnviarSMS(final Context context){
        pd = new ProgressDialog(context);
        pd.setMessage("Procesando, por favor espere...");

        pd.show();
        String pc = infoSharedPreferences(context);
        String url = pc + "/grafica5.php";
        //String url = pc + "/service/grafica5.php";

        //String url = "http://" + pc + "/service/grafica5.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @SuppressLint("ResourceType")
                    @Override
                    public void onResponse(String response) {
                        Log.d("string",response);
                        try {
                            //System.out.println("RESPUESTA DE SERVIDOR : "+response);
                            //Toast.makeText(grafica5.this, response, Toast.LENGTH_LONG).show();
                            JSONArray jsonarray = new JSONArray(response);
                            String cadena="";

                            for(int i=0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                double temperatura = jsonobject.getDouble("temperatura");          //Valores de la base de datos.
                                double humedad = jsonobject.getDouble("humedad");
                                int luz1 = jsonobject.getInt("luz1");
                                int luz2 = jsonobject.getInt("luz2");
                                int luz3 = jsonobject.getInt("luz3");
                                int luz4 = jsonobject.getInt("luz4");
                                int luz5 = jsonobject.getInt("luz5");
                                int luz6 = jsonobject.getInt("luz6");
                                int luz7 = jsonobject.getInt("luz7");
                                int luz8 = jsonobject.getInt("luz8");

                                String l1="",l2="",l3="",l4="",l5="",l6="",l7="",l8="";
                                if(luz1 == 1){
                                    l1="Encendido";
                                }else{
                                    l1="Apagado";
                                }

                                if(luz2 == 1){
                                    l2="Encendido";
                                }else{
                                    l2="Apagado";
                                }

                                if(luz3 == 1){
                                    l3="Encendido";
                                }else{
                                    l3="Apagado";
                                }

                                if(luz4 == 1){
                                    l4="Encendido";
                                }else{
                                    l4="Apagado";
                                }

                                if(luz5 == 1){
                                    l5="Encendido";
                                }else{
                                    l5="Apagado";
                                }

                                if(luz6 == 1){
                                    l6="Encendido";
                                }else{
                                    l6="Apagado";
                                }

                                if(luz7 == 1){
                                    l7="Encendido";
                                }else{
                                    l7="Apagado";
                                }

                                if(luz8 == 1){
                                    l8="Encendido";
                                }else{
                                    l8="Apagado";
                                }

                                String fecha = jsonobject.getString("fecha").trim();
                                String hora = jsonobject.getString("hora").trim();

                                //OBTENIENDO LA FECHA Y HORA ACTUAL DEL SISTEMA.
                                DateFormat formatodate= new SimpleDateFormat("yyyy/MM/dd");
                                String date= formatodate.format(new Date());

                                DateFormat formatotime= new SimpleDateFormat("HH:mm:ss a");
                                String time= formatotime.format(new Date());
                                /*String datosCompletos = "SMS UTLA.\n" +
                                        "Temperatura: " + temperatura + " °C\n" +
                                        "Humedad: " + humedad + " RH\n" +
                                        "Fecha: "  + fecha + "\n" +
                                        "Hora: " + hora + "\n" +
                                        "by system UTLA";*/
                                String datosCompletos="SMS BY SYSTEM UTLA.\n\n" +
                                        "Estado de Variables del Sistema.\n\n"+
                                        "Temperatura: " + temperatura + " °C\n" +
                                        "Humedad Relativa: " + humedad + " % [R.H.]\n" +
                                        "Luminaria # 1: " + l1 + "\n" +
                                        "Luminaria # 2: " + l2 + "\n" +
                                        "Luminaria # 3: " + l3 + "\n" +
                                        "Luminaria # 4: " + l4 + "\n" +
                                        "Luminaria # 5: " + l5 + "\n" +
                                        "Luminaria # 6: " + l6 + "\n" +
                                        "Luminaria # 7: " + l7 + "\n" +
                                        "Luminaria # 8: " + l8 + "\n\n" +
                                        "Fecha: " + fecha + "\n" +
                                        "Hora: " + hora + "\n\n" +
                                        "-------------------------------------------" + "\n" +
                                        "sms send: " + date + "\n\t\t\t\t\t\t\t\t" +
                                        time + "\n" +
                                        "-------------------------------------------" + "\n" +
                                        "UTLA  2018~2019";
                                try{
                                    SmsManager sms = SmsManager.getDefault();
                                    //sms.sendTextMessage(numTel, null, datosCompletos, null,null);  //FUNCION LIMITADO A MENOS CARACTERES POR SMS
                                    ArrayList msgTexts = sms.divideMessage(datosCompletos);
                                    sms.sendMultipartTextMessage(infoConfDestinatarioTel2(context), null, msgTexts, null,null);
                                    //Toast.makeText(getApplicationContext(), "Mensaje Enviado.", Toast.LENGTH_LONG).show();
                                    Toast toast = Toast.makeText(context, "MENSAJE ENVIADO A MÓVIL: " + infoConfDestinatarioTel2(context), Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                }
                                catch (Exception e){
                                    Toast.makeText(context, "Mensaje no enviado, datos incorrectos." + e.getMessage().toString(), Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pd.hide();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error != null){
                            Toast.makeText(context, "Algo salió mal.", Toast.LENGTH_LONG).show();
                            pd.hide();
                        }
                    }
                }
        );
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }
    /*FIN*/

    public String getTelPropietario(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("Destinatarios", Context.MODE_PRIVATE);
        return preferences.getString("telefono0", "Sin configurar.");
    }

    public String getTelReceptor(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("Destinatarios", Context.MODE_PRIVATE);
        return preferences.getString("telefono1", "Sin configurar.");
    }

    public static String obtenerTelPropietario(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("Destinatarios", Context.MODE_PRIVATE);
        return preferences.getString("telefono0", "Sin configurar.");
    }

    public static String obtenerTelReceptor(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("Destinatarios", Context.MODE_PRIVATE);
        return preferences.getString("telefono1", "Sin configurar.");
    }

    public static String obtenerServer(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        String server = preferences.getString("servidor", "Sin configurar.");
        String folder = preferences.getString("folder", "Sin configurar.");
        return server + folder;
    }

    public String getDate(){
        //Función para obtener la fecha.
        DateFormat formatodate = new SimpleDateFormat("yyyy/MM/dd");
        String date = formatodate.format(new Date());
        return date;
    }

    public String getTime(){
        //Función para obtener la hora.
        DateFormat formatotime = new SimpleDateFormat("HH:mm:ss a");
        String time = formatotime.format(new Date());
        return time;
    }


    public void save_action_phone(final Context context, final String descripcion, final String celular1, final String celular2, final String fecha, final String hora) {
        String pc = obtenerServer(context);
        String url_guardar_destinatarios = pc + "/registrophone.php";
        //String url_guardar_destinatarios = pc + "/service/registrophone.php";

        //String url_guardar_destinatarios = "http://" + pc + "/service/registrophone.php";
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


    public void about(final Context context){
        myDialog = new Dialog(context);
        myDialog.setContentView(R.layout.acercade);
        myDialog.setTitle("Creador");
        myDialog.setCancelable(false);
        Button btnclose = (Button)myDialog.findViewById(R.id.btnclose);
        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        myDialog.show();
    }

    //FUNCIONES PARA ENVIO DE SMS: CONTROL MULTIPLE DE LUMINARIAS.
    //getTelCkto(), obtenerTelPropietario(), getDate(), getTime()

    public void onoffMultipleSMS(final Context context) {
        myDialog = new Dialog(context);
        myDialog.setContentView(R.layout.onoffcombinado);
        myDialog.setTitle("App Creado por,");
        myDialog.setCancelable(false);
        //Button btnclose = (Button)myDialog.findViewById(R.id.btnclose);
        f_all = (ImageView)myDialog.findViewById(R.id.ivFocoAll);
        f1 = (ImageView)myDialog.findViewById(R.id.ivFoco1);
        f2 = (ImageView)myDialog.findViewById(R.id.ivFoco2);
        f3 = (ImageView)myDialog.findViewById(R.id.ivFoco3);
        f4 = (ImageView)myDialog.findViewById(R.id.ivFoco4);
        f5 = (ImageView)myDialog.findViewById(R.id.ivFoco5);
        f6 = (ImageView)myDialog.findViewById(R.id.ivFoco6);
        f7 = (ImageView)myDialog.findViewById(R.id.ivFoco7);
        f8 = (ImageView)myDialog.findViewById(R.id.ivFoco8);

        //f_all.setImageResource(R.drawable.ic_luz11);
        //ToggleButton sw0 = (ToggleButton)myDialog.findViewById(R.id.ToggleButton0);
        sw0 = (ToggleButton) myDialog.findViewById(R.id.ToggleButton0);
        sw1 = (ToggleButton) myDialog.findViewById(R.id.ToggleButton1);
        sw2 = (ToggleButton) myDialog.findViewById(R.id.ToggleButton2);
        sw3 = (ToggleButton) myDialog.findViewById(R.id.ToggleButton3);
        sw4 = (ToggleButton) myDialog.findViewById(R.id.ToggleButton4);
        sw5 = (ToggleButton) myDialog.findViewById(R.id.ToggleButton5);
        sw6 = (ToggleButton) myDialog.findViewById(R.id.ToggleButton6);
        sw7 = (ToggleButton) myDialog.findViewById(R.id.ToggleButton7);
        sw8 = (ToggleButton) myDialog.findViewById(R.id.ToggleButton8);

        Button button = (Button)myDialog.findViewById(R.id.btnAplicar);
        TextView txtclose = (TextView)myDialog.findViewById(R.id.txtclose);

        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

        sw0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sw0.isChecked()) {
                    estadoAll = 1;
                    estadof1=1;estadof2=1;estadof3=1;estadof4=1;
                    estadof5=1;estadof6=1;estadof7=1;estadof8=1;
                    //sw0.setChecked(true);
                    sw1.setChecked(true);sw2.setChecked(true);sw3.setChecked(true);
                    sw4.setChecked(true);sw5.setChecked(true);sw6.setChecked(true);
                    sw7.setChecked(true);sw8.setChecked(true);

                    //@drawable/foco1off
                    //f1.setImageResource(R.drawable.ic_luz11);

                    f_all.setImageResource(R.drawable.foco1on);
                    f1.setImageResource(R.drawable.foco1on);
                    f2.setImageResource(R.drawable.foco1on);
                    f3.setImageResource(R.drawable.foco1on);
                    f4.setImageResource(R.drawable.foco1on);
                    f5.setImageResource(R.drawable.foco1on);
                    f6.setImageResource(R.drawable.foco1on);
                    f7.setImageResource(R.drawable.foco1on);
                    f8.setImageResource(R.drawable.foco1on);

                    //Toast.makeText(DashboardLuces.this, "" + estadoAll, Toast.LENGTH_SHORT).show();
                } else {
                    estadoAll = 0;
                    estadof1=0;estadof2=0;estadof3=0;estadof4=0;
                    estadof5=0;estadof6=0;estadof7=0;estadof8=0;
                    //sw0.setChecked(true);
                    sw1.setChecked(false);sw2.setChecked(false);sw3.setChecked(false);
                    sw4.setChecked(false);sw5.setChecked(false);sw6.setChecked(false);
                    sw7.setChecked(false);sw8.setChecked(false);

                    f_all.setImageResource(R.drawable.foco1off);
                    f1.setImageResource(R.drawable.foco1off);
                    f2.setImageResource(R.drawable.foco1off);
                    f3.setImageResource(R.drawable.foco1off);
                    f4.setImageResource(R.drawable.foco1off);
                    f5.setImageResource(R.drawable.foco1off);
                    f6.setImageResource(R.drawable.foco1off);
                    f7.setImageResource(R.drawable.foco1off);
                    f8.setImageResource(R.drawable.foco1off);
                }
            }
        });

        sw1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sw1.isChecked()) {
                    estadof1 = 1;
                    f1.setImageResource(R.drawable.foco1on);
                } else {
                    estadof1 = 0;
                    f1.setImageResource(R.drawable.foco1off);
                }
            }
        });

        sw2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sw2.isChecked()) {
                    estadof2 = 1;
                    f2.setImageResource(R.drawable.foco1on);
                } else {
                    estadof2 = 0;
                    f2.setImageResource(R.drawable.foco1off);
                }
            }
        });

        sw3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sw3.isChecked()) {
                    estadof3 = 1;
                    f3.setImageResource(R.drawable.foco1on);
                } else {
                    estadof3 = 0;
                    f3.setImageResource(R.drawable.foco1off);
                }
            }
        });

        sw4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sw4.isChecked()) {
                    estadof4 = 1;
                    f4.setImageResource(R.drawable.foco1on);
                } else {
                    estadof4 = 0;
                    f4.setImageResource(R.drawable.foco1off);
                }
            }
        });

        sw5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sw5.isChecked()) {
                    estadof5 = 1;
                    f5.setImageResource(R.drawable.foco1on);
                } else {
                    estadof5 = 0;
                    f5.setImageResource(R.drawable.foco1off);
                }

            }
        });

        sw6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sw6.isChecked()) {
                    estadof6 = 1;
                    f6.setImageResource(R.drawable.foco1on);
                } else {
                    estadof6 = 0;
                    f6.setImageResource(R.drawable.foco1off);
                }
            }
        });

        sw7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sw7.isChecked()) {
                    estadof7 = 1;
                    f7.setImageResource(R.drawable.foco1on);
                } else {
                    estadof7 = 0;
                    f7.setImageResource(R.drawable.foco1off);
                }
            }
        });

        sw8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sw8.isChecked()) {
                    estadof8 = 1;
                    f8.setImageResource(R.drawable.foco1on);
                } else {
                    estadof8 = 0;
                    f8.setImageResource(R.drawable.foco1off);
                }
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(estadoAll==1){

                }else{

                }*/

                //cero
                if(estadof1==0 && estadof2==0 && estadof3==0 && estadof4==0 && estadof5==0 && estadof6==0 && estadof7==0 && estadof8==0){
                    //universal("utla00000000");
                    //setStatusDefault();
                    Toast.makeText(context, "No ha especificado un cambio válido.\n\n" +
                            "Debe especificar al menos un cambio de estado de luminaria para que se envie" +
                            " la orden al sistema de control.", Toast.LENGTH_SHORT).show();
                }

                //uno
                if(estadof8==0 && estadof7==0 && estadof6==0 && estadof5==0 && estadof4==0 && estadof3==0 && estadof2==0 && estadof1==1){
                    universal(context,"utla00000001");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-0-0-0-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //dos
                if(estadof8==0 && estadof7==0 && estadof6==0 && estadof5==0 && estadof4==0 && estadof3==0 && estadof2==1 && estadof1==0){
                    universal(context,"utla00000010");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-0-0-0-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //tres
                if(estadof8==0 && estadof7==0 && estadof6==0 && estadof5==0 && estadof4==0 && estadof3==0 && estadof2==1 && estadof1==1){
                    universal(context,"utla00000011");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-0-0-0-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //cuatro
                if(estadof8==0 && estadof7==0 && estadof6==0 && estadof5==0 && estadof4==0 && estadof3==1 && estadof2==0 && estadof1==0){
                    universal(context,"utla00000100");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-0-0-1-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //cinco
                if(estadof8==0 && estadof7==0 && estadof6==0 && estadof5==0 && estadof4==0 && estadof3==1 && estadof2==0 && estadof1==1){
                    universal(context,"utla00000101");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-0-0-1-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //seis
                if(estadof8==0 && estadof7==0 && estadof6==0 && estadof5==0 && estadof4==0 && estadof3==1 && estadof2==1 && estadof1==0){
                    universal(context,"utla00000110");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-0-0-1-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //siete
                if(estadof8==0 && estadof7==0 && estadof6==0 && estadof5==0 && estadof4==0 && estadof3==1 && estadof2==1 && estadof1==1){
                    universal(context,"utla00000111");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-0-0-1-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //ocho
                if(estadof8==0 && estadof7==0 && estadof6==0 && estadof5==0 && estadof4==1 && estadof3==0 && estadof2==0 && estadof1==0){
                    universal(context,"utla00001000");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-0-1-0-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //nueve
                if(estadof8==0 && estadof7==0 && estadof6==0 && estadof5==0 && estadof4==1 && estadof3==0 && estadof2==0 && estadof1==1){
                    universal(context,"utla00001001");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-0-1-0-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //dies
                if(estadof8==0 && estadof7==0 && estadof6==0 && estadof5==0 && estadof4==1 && estadof3==0 && estadof2==1 && estadof1==0){
                    universal(context,"utla00001010");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-0-1-0-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //11
                if(estadof8==0 && estadof7==0 && estadof6==0 && estadof5==0 && estadof4==1 && estadof3==0 && estadof2==1 && estadof1==1){
                    universal(context,"utla00001011");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-0-1-0-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //12
                if(estadof8==0 && estadof7==0 && estadof6==0 && estadof5==0 && estadof4==1 && estadof3==1 && estadof2==0 && estadof1==0){
                    universal(context,"utla00001100");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-0-1-1-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //13
                if(estadof8==0 && estadof7==0 && estadof6==0 && estadof5==0 && estadof4==1 && estadof3==1 && estadof2==0 && estadof1==1){
                    universal(context,"utla00001101");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-0-1-1-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //14
                if(estadof8==0 && estadof7==0 && estadof6==0 && estadof5==0 && estadof4==1 && estadof3==1 && estadof2==1 && estadof1==0){
                    universal(context,"utla00001110");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-0-1-1-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //15
                if(estadof8==0 && estadof7==0 && estadof6==0 && estadof5==0 && estadof4==1 && estadof3==1 && estadof2==1 && estadof1==1){
                    universal(context,"utla00001111");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-0-1-1-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //16
                if(estadof8==0 && estadof7==0 && estadof6==0 && estadof5==1 && estadof4==0 && estadof3==0 && estadof2==0 && estadof1==0){
                    universal(context,"utla00010000");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-1-0-0-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //17
                if(estadof8==0 && estadof7==0 && estadof6==0 && estadof5==1 && estadof4==0 && estadof3==0 && estadof2==0 && estadof1==1){
                    universal(context,"utla00010001");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-1-0-0-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //18
                if(estadof8==0 && estadof7==0 && estadof6==0 && estadof5==1 && estadof4==0 && estadof3==0 && estadof2==1 && estadof1==0){
                    universal(context,"utla00010010");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-1-0-0-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //19
                if(estadof8==0 && estadof7==0 && estadof6==0 && estadof5==1 && estadof4==0 && estadof3==0 && estadof2==1 && estadof1==1){
                    universal(context,"utla00010011");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-1-0-0-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //20
                if(estadof8==0 && estadof7==0 && estadof6==0 && estadof5==1 && estadof4==0 && estadof3==1 && estadof2==0 && estadof1==0){
                    universal(context,"utla00010100");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-1-0-1-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //21
                if(estadof8==0 && estadof7==0 && estadof6==0 && estadof5==1 && estadof4==0 && estadof3==1 && estadof2==0 && estadof1==1){
                    universal(context,"utla00010101");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-1-0-1-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //22
                if(estadof8==0 && estadof7==0 && estadof6==0 && estadof5==1 && estadof4==0 && estadof3==1 && estadof2==1 && estadof1==0){
                    universal(context,"utla00010110");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-1-0-1-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //23
                if(estadof8==0 && estadof7==0 && estadof6==0 && estadof5==1 && estadof4==0 && estadof3==1 && estadof2==1 && estadof1==1){
                    universal(context,"utla00010111");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-1-0-1-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //24
                if(estadof8==0 && estadof7==0 && estadof6==0 && estadof5==1 && estadof4==1 && estadof3==0 && estadof2==0 && estadof1==0){
                    universal(context,"utla00011000");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-1-1-0-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //25
                if(estadof8==0 && estadof7==0 && estadof6==0 && estadof5==1 && estadof4==1 && estadof3==0 && estadof2==0 && estadof1==1){
                    universal(context,"utla00011001");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-1-1-0-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //26
                if(estadof8==0 && estadof7==0 && estadof6==0 && estadof5==1 && estadof4==1 && estadof3==0 && estadof2==1 && estadof1==0){
                    universal(context,"utla00011010");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-1-1-0-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //27
                if(estadof8==0 && estadof7==0 && estadof6==0 && estadof5==1 && estadof4==1 && estadof3==0 && estadof2==1 && estadof1==1){
                    universal(context,"utla00011011");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-1-1-0-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //28
                if(estadof8==0 && estadof7==0 && estadof6==0 && estadof5==1 && estadof4==1 && estadof3==1 && estadof2==0 && estadof1==0){
                    universal(context,"utla00011100");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-1-1-1-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //29
                if(estadof8==0 && estadof7==0 && estadof6==0 && estadof5==1 && estadof4==1 && estadof3==1 && estadof2==0 && estadof1==1){
                    universal(context,"utla00011101");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-1-1-1-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //30
                if(estadof8==0 && estadof7==0 && estadof6==0 && estadof5==1 && estadof4==1 && estadof3==1 && estadof2==1 && estadof1==0){
                    universal(context,"utla00011110");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-1-1-1-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //31
                if(estadof8==0 && estadof7==0 && estadof6==0 && estadof5==1 && estadof4==1 && estadof3==1 && estadof2==1 && estadof1==1){
                    universal(context,"utla00011111");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-1-1-1-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //32
                if(estadof8==0 && estadof7==0 && estadof6==1 && estadof5==0 && estadof4==0 && estadof3==0 && estadof2==0 && estadof1==0){
                    universal(context,"utla00100000");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-0-0-0-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //33
                if(estadof8==0 && estadof7==0 && estadof6==1 && estadof5==0 && estadof4==0 && estadof3==0 && estadof2==0 && estadof1==1){
                    universal(context,"utla00100001");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-0-0-0-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //34
                if(estadof8==0 && estadof7==0 && estadof6==1 && estadof5==0 && estadof4==0 && estadof3==0 && estadof2==1 && estadof1==0){
                    universal(context,"utla00100010");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-0-0-0-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //35
                if(estadof8==0 && estadof7==0 && estadof6==1 && estadof5==0 && estadof4==0 && estadof3==0 && estadof2==1 && estadof1==1){
                    universal(context,"utla00100011");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-0-0-0-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //36
                if(estadof8==0 && estadof7==0 && estadof6==1 && estadof5==0 && estadof4==0 && estadof3==1 && estadof2==0 && estadof1==0){
                    universal(context,"utla00100100");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-0-0-1-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //37
                if(estadof8==0 && estadof7==0 && estadof6==1 && estadof5==0 && estadof4==0 && estadof3==1 && estadof2==0 && estadof1==1){
                    universal(context,"utla00100101");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-0-0-1-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //38
                if(estadof8==0 && estadof7==0 && estadof6==1 && estadof5==0 && estadof4==0 && estadof3==1 && estadof2==1 && estadof1==0){
                    universal(context,"utla00100110");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-0-0-1-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //39
                if(estadof8==0 && estadof7==0 && estadof6==1 && estadof5==0 && estadof4==0 && estadof3==1 && estadof2==1 && estadof1==1){
                    universal(context,"utla00100111");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-0-0-1-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //40
                if(estadof8==0 && estadof7==0 && estadof6==1 && estadof5==0 && estadof4==1 && estadof3==0 && estadof2==0 && estadof1==0){
                    universal(context,"utla00101000");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-0-1-0-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //41
                if(estadof8==0 && estadof7==0 && estadof6==1 && estadof5==0 && estadof4==1 && estadof3==0 && estadof2==0 && estadof1==1){
                    universal(context,"utla00101001");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-0-1-0-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //42
                if(estadof8==0 && estadof7==0 && estadof6==1 && estadof5==0 && estadof4==1 && estadof3==0 && estadof2==1 && estadof1==0){
                    universal(context,"utla00101010");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-0-1-0-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //43
                if(estadof8==0 && estadof7==0 && estadof6==1 && estadof5==0 && estadof4==1 && estadof3==0 && estadof2==1 && estadof1==1){
                    universal(context,"utla00101011");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-0-1-0-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //44
                if(estadof8==0 && estadof7==0 && estadof6==1 && estadof5==0 && estadof4==1 && estadof3==1 && estadof2==0 && estadof1==0){
                    universal(context,"utla00101100");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-0-1-1-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //45
                if(estadof8==0 && estadof7==0 && estadof6==1 && estadof5==0 && estadof4==1 && estadof3==1 && estadof2==0 && estadof1==1){
                    universal(context,"utla00101101");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-0-1-1-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //46
                if(estadof8==0 && estadof7==0 && estadof6==1 && estadof5==0 && estadof4==1 && estadof3==1 && estadof2==1 && estadof1==0){
                    universal(context,"utla00101110");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-0-1-1-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //47
                if(estadof8==0 && estadof7==0 && estadof6==1 && estadof5==0 && estadof4==1 && estadof3==1 && estadof2==1 && estadof1==1){
                    universal(context,"utla00101111");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-0-1-1-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //48
                if(estadof8==0 && estadof7==0 && estadof6==1 && estadof5==1 && estadof4==0 && estadof3==0 && estadof2==0 && estadof1==0){
                    universal(context,"utla00110000");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-1-0-0-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //49
                if(estadof8==0 && estadof7==0 && estadof6==1 && estadof5==1 && estadof4==0 && estadof3==0 && estadof2==0 && estadof1==1){
                    universal(context,"utla00110001");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-1-0-0-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //50
                if(estadof8==0 && estadof7==0 && estadof6==1 && estadof5==1 && estadof4==0 && estadof3==0 && estadof2==1 && estadof1==0){
                    universal(context,"utla00110010");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-1-0-0-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //51
                if(estadof8==0 && estadof7==0 && estadof6==1 && estadof5==1 && estadof4==0 && estadof3==0 && estadof2==1 && estadof1==1){
                    universal(context,"utla00110011");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-1-0-0-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //52
                if(estadof8==0 && estadof7==0 && estadof6==1 && estadof5==1 && estadof4==0 && estadof3==1 && estadof2==0 && estadof1==0){
                    universal(context,"utla00110100");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-1-0-1-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //53
                if(estadof8==0 && estadof7==0 && estadof6==1 && estadof5==1 && estadof4==0 && estadof3==1 && estadof2==0 && estadof1==1){
                    universal(context,"utla00110101");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-1-0-1-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //54
                if(estadof8==0 && estadof7==0 && estadof6==1 && estadof5==1 && estadof4==0 && estadof3==1 && estadof2==1 && estadof1==0){
                    universal(context,"utla00110110");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-1-0-1-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //55
                if(estadof8==0 && estadof7==0 && estadof6==1 && estadof5==1 && estadof4==0 && estadof3==1 && estadof2==1 && estadof1==1){
                    universal(context,"utla00110111");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-1-0-1-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //56
                if(estadof8==0 && estadof7==0 && estadof6==1 && estadof5==1 && estadof4==1 && estadof3==0 && estadof2==0 && estadof1==0){
                    universal(context,"utla00111000");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-1-1-0-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //57
                if(estadof8==0 && estadof7==0 && estadof6==1 && estadof5==1 && estadof4==1 && estadof3==0 && estadof2==0 && estadof1==1){
                    universal(context,"utla00111001");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-1-1-0-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //58
                if(estadof8==0 && estadof7==0 && estadof6==1 && estadof5==1 && estadof4==1 && estadof3==0 && estadof2==1 && estadof1==0){
                    universal(context,"utla00111010");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-1-1-0-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //59
                if(estadof8==0 && estadof7==0 && estadof6==1 && estadof5==1 && estadof4==1 && estadof3==0 && estadof2==1 && estadof1==1){
                    universal(context,"utla00111011");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-1-1-0-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //60
                if(estadof8==0 && estadof7==0 && estadof6==1 && estadof5==1 && estadof4==1 && estadof3==1 && estadof2==0 && estadof1==0){
                    universal(context,"utla00111100");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-1-1-1-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //61
                if(estadof8==0 && estadof7==0 && estadof6==1 && estadof5==1 && estadof4==1 && estadof3==1 && estadof2==0 && estadof1==1){
                    universal(context,"utla00111101");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-1-1-1-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //62
                if(estadof8==0 && estadof7==0 && estadof6==1 && estadof5==1 && estadof4==1 && estadof3==1 && estadof2==1 && estadof1==0){
                    universal(context,"utla00111110");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-1-1-1-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //63
                if(estadof8==0 && estadof7==0 && estadof6==1 && estadof5==1 && estadof4==1 && estadof3==1 && estadof2==1 && estadof1==1){
                    universal(context,"utla00111111");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-1-1-1-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //64
                if(estadof8==0 && estadof7==1 && estadof6==0 && estadof5==0 && estadof4==0 && estadof3==0 && estadof2==0 && estadof1==0){
                    universal(context,"utla01000000");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-0-0-0-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //65
                if(estadof8==0 && estadof7==1 && estadof6==0 && estadof5==0 && estadof4==0 && estadof3==0 && estadof2==0 && estadof1==1){
                    universal(context,"utla01000001");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-0-0-0-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //66
                if(estadof8==0 && estadof7==1 && estadof6==0 && estadof5==0 && estadof4==0 && estadof3==0 && estadof2==1 && estadof1==0){
                    universal(context,"utla01000010");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-0-0-0-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //67
                if(estadof8==0 && estadof7==1 && estadof6==0 && estadof5==0 && estadof4==0 && estadof3==0 && estadof2==1 && estadof1==1){
                    universal(context,"utla01000011");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-0-0-0-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //68
                if(estadof8==0 && estadof7==1 && estadof6==0 && estadof5==0 && estadof4==0 && estadof3==1 && estadof2==0 && estadof1==0){
                    universal(context,"utla01000100");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-0-0-1-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //69
                if(estadof8==0 && estadof7==1 && estadof6==0 && estadof5==0 && estadof4==0 && estadof3==1 && estadof2==0 && estadof1==1){
                    universal(context,"utla01000101");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-0-0-1-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //70
                if(estadof8==0 && estadof7==1 && estadof6==0 && estadof5==0 && estadof4==0 && estadof3==1 && estadof2==1 && estadof1==0){
                    universal(context,"utla01000110");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-0-0-1-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //71
                if(estadof8==0 && estadof7==1 && estadof6==0 && estadof5==0 && estadof4==0 && estadof3==1 && estadof2==1 && estadof1==1){
                    universal(context,"utla01000111");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-0-0-1-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //72
                if(estadof8==0 && estadof7==1 && estadof6==0 && estadof5==0 && estadof4==1 && estadof3==0 && estadof2==0 && estadof1==0){
                    universal(context,"utla01001000");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-0-1-0-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //73
                if(estadof8==0 && estadof7==1 && estadof6==0 && estadof5==0 && estadof4==1 && estadof3==0 && estadof2==0 && estadof1==1){
                    universal(context,"utla01001001");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-0-1-0-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //74
                if(estadof8==0 && estadof7==1 && estadof6==0 && estadof5==0 && estadof4==1 && estadof3==0 && estadof2==1 && estadof1==0){
                    universal(context,"utla01001010");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-0-1-0-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //75
                if(estadof8==0 && estadof7==1 && estadof6==0 && estadof5==0 && estadof4==1 && estadof3==0 && estadof2==1 && estadof1==1){
                    universal(context,"utla01001011");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-0-1-0-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //76
                if(estadof8==0 && estadof7==1 && estadof6==0 && estadof5==0 && estadof4==1 && estadof3==1 && estadof2==0 && estadof1==0){
                    universal(context,"utla01001100");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-0-1-1-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //77
                if(estadof8==0 && estadof7==1 && estadof6==0 && estadof5==0 && estadof4==1 && estadof3==1 && estadof2==0 && estadof1==1){
                    universal(context,"utla01001101");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-0-1-1-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //78
                if(estadof8==0 && estadof7==1 && estadof6==0 && estadof5==0 && estadof4==1 && estadof3==1 && estadof2==1 && estadof1==0){
                    universal(context,"utla01001110");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-0-1-1-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //79
                if(estadof8==0 && estadof7==1 && estadof6==0 && estadof5==0 && estadof4==1 && estadof3==1 && estadof2==1 && estadof1==1){
                    universal(context,"utla01001111");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-0-1-1-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //80
                if(estadof8==0 && estadof7==1 && estadof6==0 && estadof5==1 && estadof4==0 && estadof3==0 && estadof2==0 && estadof1==0){
                    universal(context,"utla01010000");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-1-0-0-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //81
                if(estadof8==0 && estadof7==1 && estadof6==0 && estadof5==1 && estadof4==0 && estadof3==0 && estadof2==0 && estadof1==1){
                    universal(context,"utla01010001");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-1-0-0-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //82
                if(estadof8==0 && estadof7==1 && estadof6==0 && estadof5==1 && estadof4==0 && estadof3==0 && estadof2==1 && estadof1==0){
                    universal(context,"utla01010010");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-1-0-0-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //83
                if(estadof8==0 && estadof7==1 && estadof6==0 && estadof5==1 && estadof4==0 && estadof3==0 && estadof2==1 && estadof1==1){
                    universal(context,"utla01010011");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-1-0-0-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //84
                if(estadof8==0 && estadof7==1 && estadof6==0 && estadof5==1 && estadof4==0 && estadof3==1 && estadof2==0 && estadof1==0){
                    universal(context,"utla01010100");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-1-0-1-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //85
                if(estadof8==0 && estadof7==1 && estadof6==0 && estadof5==1 && estadof4==0 && estadof3==1 && estadof2==0 && estadof1==1){
                    universal(context,"utla01010101");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-1-0-1-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //86
                if(estadof8==0 && estadof7==1 && estadof6==0 && estadof5==1 && estadof4==0 && estadof3==1 && estadof2==1 && estadof1==0){
                    universal(context,"utla01010110");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-1-0-1-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //87
                if(estadof8==0 && estadof7==1 && estadof6==0 && estadof5==1 && estadof4==0 && estadof3==1 && estadof2==1 && estadof1==1){
                    universal(context,"utla01010111");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-1-0-1-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //88
                if(estadof8==0 && estadof7==1 && estadof6==0 && estadof5==1 && estadof4==1 && estadof3==0 && estadof2==0 && estadof1==0){
                    universal(context,"utla01011000");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-1-1-0-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //89
                if(estadof8==0 && estadof7==1 && estadof6==0 && estadof5==1 && estadof4==1 && estadof3==0 && estadof2==0 && estadof1==1){
                    universal(context,"utla01011001");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-1-1-0-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //90
                if(estadof8==0 && estadof7==1 && estadof6==0 && estadof5==1 && estadof4==1 && estadof3==0 && estadof2==1 && estadof1==0){
                    universal(context,"utla01011010");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-1-1-0-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //91
                if(estadof8==0 && estadof7==1 && estadof6==0 && estadof5==1 && estadof4==1 && estadof3==0 && estadof2==1 && estadof1==1){
                    universal(context,"utla01011011");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-1-1-0-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //92
                if(estadof8==0 && estadof7==1 && estadof6==0 && estadof5==1 && estadof4==1 && estadof3==1 && estadof2==0 && estadof1==0){
                    universal(context,"utla01011100");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-1-1-1-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //93
                if(estadof8==0 && estadof7==1 && estadof6==0 && estadof5==1 && estadof4==1 && estadof3==1 && estadof2==0 && estadof1==1){
                    universal(context,"utla01011101");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-1-1-1-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //94
                if(estadof8==0 && estadof7==1 && estadof6==0 && estadof5==1 && estadof4==1 && estadof3==1 && estadof2==1 && estadof1==0){
                    universal(context,"utla01011110");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-1-1-1-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //95
                if(estadof8==0 && estadof7==1 && estadof6==0 && estadof5==1 && estadof4==1 && estadof3==1 && estadof2==1 && estadof1==1){
                    universal(context,"utla01011111");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-1-1-1-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //96
                if(estadof8==0 && estadof7==1 && estadof6==1 && estadof5==0 && estadof4==0 && estadof3==0 && estadof2==0 && estadof1==0){
                    universal(context,"utla01100000");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-0-0-0-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //97
                if(estadof8==0 && estadof7==1 && estadof6==1 && estadof5==0 && estadof4==0 && estadof3==0 && estadof2==0 && estadof1==1){
                    universal(context,"utla01100001");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-0-0-0-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //98
                if(estadof8==0 && estadof7==1 && estadof6==1 && estadof5==0 && estadof4==0 && estadof3==0 && estadof2==1 && estadof1==0){
                    universal(context,"utla01100010");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-0-0-0-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //99
                if(estadof8==0 && estadof7==1 && estadof6==1 && estadof5==0 && estadof4==0 && estadof3==0 && estadof2==1 && estadof1==1){
                    universal(context,"utla01100011");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-0-0-0-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //100
                if(estadof8==0 && estadof7==1 && estadof6==1 && estadof5==0 && estadof4==0 && estadof3==1 && estadof2==0 && estadof1==0){
                    universal(context,"utla01100100");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-0-0-1-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //101
                if(estadof8==0 && estadof7==1 && estadof6==1 && estadof5==0 && estadof4==0 && estadof3==1 && estadof2==0 && estadof1==1){
                    universal(context,"utla01100101");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-0-0-1-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //102
                if(estadof8==0 && estadof7==1 && estadof6==1 && estadof5==0 && estadof4==0 && estadof3==1 && estadof2==1 && estadof1==0){
                    universal(context,"utla01100110");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-0-0-1-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //103
                if(estadof8==0 && estadof7==1 && estadof6==1 && estadof5==0 && estadof4==0 && estadof3==1 && estadof2==1 && estadof1==1){
                    universal(context,"utla01100111");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-0-0-1-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //104
                if(estadof8==0 && estadof7==1 && estadof6==1 && estadof5==0 && estadof4==1 && estadof3==0 && estadof2==0 && estadof1==0){
                    universal(context,"utla01101000");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-0-1-0-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //105
                if(estadof8==0 && estadof7==1 && estadof6==1 && estadof5==0 && estadof4==1 && estadof3==0 && estadof2==0 && estadof1==1){
                    universal(context,"utla01101001");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-0-1-0-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //106
                if(estadof8==0 && estadof7==1 && estadof6==1 && estadof5==0 && estadof4==1 && estadof3==0 && estadof2==1 && estadof1==0){
                    universal(context,"utla01101010");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-0-1-0-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //107
                if(estadof8==0 && estadof7==1 && estadof6==1 && estadof5==0 && estadof4==1 && estadof3==0 && estadof2==1 && estadof1==1){
                    universal(context,"utla01101011");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-0-1-0-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //108
                if(estadof8==0 && estadof7==1 && estadof6==1 && estadof5==0 && estadof4==1 && estadof3==1 && estadof2==0 && estadof1==0){
                    universal(context,"utla01101100");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-0-1-1-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //109
                if(estadof8==0 && estadof7==1 && estadof6==1 && estadof5==0 && estadof4==1 && estadof3==1 && estadof2==0 && estadof1==1){
                    universal(context,"utla01101101");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-0-1-1-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //110
                if(estadof8==0 && estadof7==1 && estadof6==1 && estadof5==0 && estadof4==1 && estadof3==1 && estadof2==1 && estadof1==0){
                    universal(context,"utla01101110");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-0-1-1-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //111
                if(estadof8==0 && estadof7==1 && estadof6==1 && estadof5==0 && estadof4==1 && estadof3==1 && estadof2==1 && estadof1==1){
                    universal(context,"utla01101111");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-0-1-1-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //112
                if(estadof8==0 && estadof7==1 && estadof6==1 && estadof5==1 && estadof4==0 && estadof3==0 && estadof2==0 && estadof1==0){
                    universal(context,"utla01110000");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-1-0-0-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //113
                if(estadof8==0 && estadof7==1 && estadof6==1 && estadof5==1 && estadof4==0 && estadof3==0 && estadof2==0 && estadof1==1){
                    universal(context,"utla01110001");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-1-0-0-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //114
                if(estadof8==0 && estadof7==1 && estadof6==1 && estadof5==1 && estadof4==0 && estadof3==0 && estadof2==1 && estadof1==0){
                    universal(context,"utla01110010");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-1-0-0-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //115
                if(estadof8==0 && estadof7==1 && estadof6==1 && estadof5==1 && estadof4==0 && estadof3==0 && estadof2==1 && estadof1==1){
                    universal(context,"utla01110011");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-1-0-0-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //116
                if(estadof8==0 && estadof7==1 && estadof6==1 && estadof5==1 && estadof4==0 && estadof3==1 && estadof2==0 && estadof1==0){
                    universal(context,"utla01110100");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-1-0-1-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //117
                if(estadof8==0 && estadof7==1 && estadof6==1 && estadof5==1 && estadof4==0 && estadof3==1 && estadof2==0 && estadof1==1){
                    universal(context,"utla01110101");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-1-0-1-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //118
                if(estadof8==0 && estadof7==1 && estadof6==1 && estadof5==1 && estadof4==0 && estadof3==1 && estadof2==1 && estadof1==0){
                    universal(context,"utla01110110");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-1-0-1-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //119
                if(estadof8==0 && estadof7==1 && estadof6==1 && estadof5==1 && estadof4==0 && estadof3==1 && estadof2==1 && estadof1==1){
                    universal(context,"utla01110111");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-1-0-1-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //120
                if(estadof8==0 && estadof7==1 && estadof6==1 && estadof5==1 && estadof4==1 && estadof3==0 && estadof2==0 && estadof1==0){
                    universal(context,"utla01111000");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-1-1-0-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //121
                if(estadof8==0 && estadof7==1 && estadof6==1 && estadof5==1 && estadof4==1 && estadof3==0 && estadof2==0 && estadof1==1){
                    universal(context,"utla01111001");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-1-1-0-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //122
                if(estadof8==0 && estadof7==1 && estadof6==1 && estadof5==1 && estadof4==1 && estadof3==0 && estadof2==1 && estadof1==0){
                    universal(context,"utla01111010");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-1-1-0-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //123
                if(estadof8==0 && estadof7==1 && estadof6==1 && estadof5==1 && estadof4==1 && estadof3==0 && estadof2==1 && estadof1==1){
                    universal(context,"utla01111011");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-1-1-0-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //124
                if(estadof8==0 && estadof7==1 && estadof6==1 && estadof5==1 && estadof4==1 && estadof3==1 && estadof2==0 && estadof1==0){
                    universal(context,"utla01111100");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-1-1-1-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //125
                if(estadof8==0 && estadof7==1 && estadof6==1 && estadof5==1 && estadof4==1 && estadof3==1 && estadof2==0 && estadof1==1){
                    universal(context,"utla01111101");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-1-1-1-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //126
                if(estadof8==0 && estadof7==1 && estadof6==1 && estadof5==1 && estadof4==1 && estadof3==1 && estadof2==1 && estadof1==0){
                    universal(context,"utla01111110");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-1-1-1-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //127
                if(estadof8==0 && estadof7==1 && estadof6==1 && estadof5==1 && estadof4==1 && estadof3==1 && estadof2==1 && estadof1==1){
                    universal(context,"utla01111111");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-1-1-1-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //128
                if(estadof8==1 && estadof7==0 && estadof6==0 && estadof5==0 && estadof4==0 && estadof3==0 && estadof2==0 && estadof1==0){
                    universal(context,"utla10000000");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-0-0-0-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //129
                if(estadof8==1 && estadof7==0 && estadof6==0 && estadof5==0 && estadof4==0 && estadof3==0 && estadof2==0 && estadof1==1){
                    universal(context,"utla10000001");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-0-0-0-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //130
                if(estadof8==1 && estadof7==0 && estadof6==0 && estadof5==0 && estadof4==0 && estadof3==0 && estadof2==1 && estadof1==0){
                    universal(context,"utla10000010");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-0-0-0-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //131
                if(estadof8==1 && estadof7==0 && estadof6==0 && estadof5==0 && estadof4==0 && estadof3==0 && estadof2==1 && estadof1==1){
                    universal(context,"utla10000011");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-0-0-0-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //132
                if(estadof8==1 && estadof7==0 && estadof6==0 && estadof5==0 && estadof4==0 && estadof3==1 && estadof2==0 && estadof1==0){
                    universal(context,"utla10000100");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-0-0-1-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //133
                if(estadof8==1 && estadof7==0 && estadof6==0 && estadof5==0 && estadof4==0 && estadof3==1 && estadof2==0 && estadof1==1){
                    universal(context,"utla10000101");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-0-0-1-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //134
                if(estadof8==1 && estadof7==0 && estadof6==0 && estadof5==0 && estadof4==0 && estadof3==1 && estadof2==1 && estadof1==0){
                    universal(context,"utla10000110");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-0-0-1-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //135
                if(estadof8==1 && estadof7==0 && estadof6==0 && estadof5==0 && estadof4==0 && estadof3==1 && estadof2==1 && estadof1==1){
                    universal(context,"utla10000111");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-0-0-1-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //136
                if(estadof8==1 && estadof7==0 && estadof6==0 && estadof5==0 && estadof4==1 && estadof3==0 && estadof2==0 && estadof1==0){
                    universal(context,"utla10001000");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-0-1-0-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //137
                if(estadof8==1 && estadof7==0 && estadof6==0 && estadof5==0 && estadof4==1 && estadof3==0 && estadof2==0 && estadof1==1){
                    universal(context,"utla10001001");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-0-1-0-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //138
                if(estadof8==1 && estadof7==0 && estadof6==0 && estadof5==0 && estadof4==1 && estadof3==0 && estadof2==1 && estadof1==0){
                    universal(context,"utla10001010");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-0-1-0-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //139
                if(estadof8==1 && estadof7==0 && estadof6==0 && estadof5==0 && estadof4==1 && estadof3==0 && estadof2==1 && estadof1==1){
                    universal(context,"utla10001011");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-0-1-0-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //140
                if(estadof8==1 && estadof7==0 && estadof6==0 && estadof5==0 && estadof4==1 && estadof3==1 && estadof2==0 && estadof1==0){
                    universal(context,"utla10001100");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-0-1-1-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //141
                if(estadof8==1 && estadof7==0 && estadof6==0 && estadof5==0 && estadof4==1 && estadof3==1 && estadof2==0 && estadof1==1){
                    universal(context,"utla10001101");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-0-1-1-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //142
                if(estadof8==1 && estadof7==0 && estadof6==0 && estadof5==0 && estadof4==1 && estadof3==1 && estadof2==1 && estadof1==0){
                    universal(context,"utla10001110");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-0-1-1-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //143
                if(estadof8==1 && estadof7==0 && estadof6==0 && estadof5==0 && estadof4==1 && estadof3==1 && estadof2==1 && estadof1==1){
                    universal(context,"utla10001111");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-0-1-1-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //144
                if(estadof8==1 && estadof7==0 && estadof6==0 && estadof5==1 && estadof4==0 && estadof3==0 && estadof2==0 && estadof1==0){
                    universal(context,"utla10010000");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-1-0-0-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //145
                if(estadof8==1 && estadof7==0 && estadof6==0 && estadof5==1 && estadof4==0 && estadof3==0 && estadof2==0 && estadof1==1){
                    universal(context,"utla10010001");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-1-0-0-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //146
                if(estadof8==1 && estadof7==0 && estadof6==0 && estadof5==1 && estadof4==0 && estadof3==0 && estadof2==1 && estadof1==0){
                    universal(context,"utla10010010");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-1-0-0-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //147
                if(estadof8==1 && estadof7==0 && estadof6==0 && estadof5==1 && estadof4==0 && estadof3==0 && estadof2==1 && estadof1==1){
                    universal(context,"utla10010011");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-1-0-0-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //148
                if(estadof8==1 && estadof7==0 && estadof6==0 && estadof5==1 && estadof4==0 && estadof3==1 && estadof2==0 && estadof1==0){
                    universal(context,"utla10010100");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-1-0-1-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //149
                if(estadof8==1 && estadof7==0 && estadof6==0 && estadof5==1 && estadof4==0 && estadof3==1 && estadof2==0 && estadof1==1){
                    universal(context,"utla10010101");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-1-0-1-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //150
                if(estadof8==1 && estadof7==0 && estadof6==0 && estadof5==1 && estadof4==0 && estadof3==1 && estadof2==1 && estadof1==0){
                    universal(context,"utla10010110");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-1-0-1-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //151
                if(estadof8==1 && estadof7==0 && estadof6==0 && estadof5==1 && estadof4==0 && estadof3==1 && estadof2==1 && estadof1==1){
                    universal(context,"utla10010111");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-1-0-1-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //152
                if(estadof8==1 && estadof7==0 && estadof6==0 && estadof5==1 && estadof4==1 && estadof3==0 && estadof2==0 && estadof1==0){
                    universal(context,"utla10011000");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-1-1-0-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //153
                if(estadof8==1 && estadof7==0 && estadof6==0 && estadof5==1 && estadof4==1 && estadof3==0 && estadof2==0 && estadof1==1){
                    universal(context,"utla10011001");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-1-1-0-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //154
                if(estadof8==1 && estadof7==0 && estadof6==0 && estadof5==1 && estadof4==1 && estadof3==0 && estadof2==1 && estadof1==0){
                    universal(context,"utla10011010");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-1-1-0-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //155
                if(estadof8==1 && estadof7==0 && estadof6==0 && estadof5==1 && estadof4==1 && estadof3==0 && estadof2==1 && estadof1==1){
                    universal(context,"utla10011011");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-1-1-0-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //156
                if(estadof8==1 && estadof7==0 && estadof6==0 && estadof5==1 && estadof4==1 && estadof3==1 && estadof2==0 && estadof1==0){
                    universal(context,"utla10011100");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-1-1-1-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //157
                if(estadof8==1 && estadof7==0 && estadof6==0 && estadof5==1 && estadof4==1 && estadof3==1 && estadof2==0 && estadof1==1){
                    universal(context,"utla10011101");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-1-1-1-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //158
                if(estadof8==1 && estadof7==0 && estadof6==0 && estadof5==1 && estadof4==1 && estadof3==1 && estadof2==1 && estadof1==0){
                    universal(context,"utla10011110");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-1-1-1-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //159
                if(estadof8==1 && estadof7==0 && estadof6==0 && estadof5==1 && estadof4==1 && estadof3==1 && estadof2==1 && estadof1==1){
                    universal(context,"utla10011111");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-1-1-1-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //160
                if(estadof8==1 && estadof7==0 && estadof6==1 && estadof5==0 && estadof4==0 && estadof3==0 && estadof2==0 && estadof1==0){
                    universal(context,"utla10100000");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-0-0-0-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //161
                if(estadof8==1 && estadof7==0 && estadof6==1 && estadof5==0 && estadof4==0 && estadof3==0 && estadof2==0 && estadof1==1){
                    universal(context,"utla10100001");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-0-0-0-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //162
                if(estadof8==1 && estadof7==0 && estadof6==1 && estadof5==0 && estadof4==0 && estadof3==0 && estadof2==1 && estadof1==0){
                    universal(context,"utla10100010");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-0-0-0-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //163
                if(estadof8==1 && estadof7==0 && estadof6==1 && estadof5==0 && estadof4==0 && estadof3==0 && estadof2==1 && estadof1==1){
                    universal(context,"utla10100011");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-0-0-0-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //164
                if(estadof8==1 && estadof7==0 && estadof6==1 && estadof5==0 && estadof4==0 && estadof3==1 && estadof2==0 && estadof1==0){
                    universal(context,"utla10100100");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-0-0-1-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //165
                if(estadof8==1 && estadof7==0 && estadof6==1 && estadof5==0 && estadof4==0 && estadof3==1 && estadof2==0 && estadof1==1){
                    universal(context,"utla10100101");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-0-0-1-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //166
                if(estadof8==1 && estadof7==0 && estadof6==1 && estadof5==0 && estadof4==0 && estadof3==1 && estadof2==1 && estadof1==0){
                    universal(context,"utla10100110");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-0-0-1-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //167
                if(estadof8==1 && estadof7==0 && estadof6==1 && estadof5==0 && estadof4==0 && estadof3==1 && estadof2==1 && estadof1==1){
                    universal(context,"utla10100111");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-0-0-1-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //168
                if(estadof8==1 && estadof7==0 && estadof6==1 && estadof5==0 && estadof4==1 && estadof3==0 && estadof2==0 && estadof1==0){
                    universal(context,"utla10101000");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-0-1-0-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //169
                if(estadof8==1 && estadof7==0 && estadof6==1 && estadof5==0 && estadof4==1 && estadof3==0 && estadof2==0 && estadof1==1){
                    universal(context,"utla10101001");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-0-1-0-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //170
                if(estadof8==1 && estadof7==0 && estadof6==1 && estadof5==0 && estadof4==1 && estadof3==0 && estadof2==1 && estadof1==0){
                    universal(context,"utla10101010");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-0-1-0-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //171
                if(estadof8==1 && estadof7==0 && estadof6==1 && estadof5==0 && estadof4==1 && estadof3==0 && estadof2==1 && estadof1==1){
                    universal(context,"utla10101011");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-0-1-0-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //172
                if(estadof8==1 && estadof7==0 && estadof6==1 && estadof5==0 && estadof4==1 && estadof3==1 && estadof2==0 && estadof1==0){
                    universal(context,"utla10101100");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-0-1-1-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //173
                if(estadof8==1 && estadof7==0 && estadof6==1 && estadof5==0 && estadof4==1 && estadof3==1 && estadof2==0 && estadof1==1){
                    universal(context,"utla10101101");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-0-1-1-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //174
                if(estadof8==1 && estadof7==0 && estadof6==1 && estadof5==0 && estadof4==1 && estadof3==1 && estadof2==1 && estadof1==0){
                    universal(context,"utla10101110");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-0-1-1-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //175
                if(estadof8==1 && estadof7==0 && estadof6==1 && estadof5==0 && estadof4==1 && estadof3==1 && estadof2==1 && estadof1==1){
                    universal(context,"utla10101111");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-0-1-1-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //176
                if(estadof8==1 && estadof7==0 && estadof6==1 && estadof5==1 && estadof4==0 && estadof3==0 && estadof2==0 && estadof1==0){
                    universal(context,"utla10110000");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-1-0-0-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //177
                if(estadof8==1 && estadof7==0 && estadof6==1 && estadof5==1 && estadof4==0 && estadof3==0 && estadof2==0 && estadof1==1){
                    universal(context,"utla10110001");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-1-0-0-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //178
                if(estadof8==1 && estadof7==0 && estadof6==1 && estadof5==1 && estadof4==0 && estadof3==0 && estadof2==1 && estadof1==0){
                    universal(context,"utla10110010");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-1-0-0-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //179
                if(estadof8==1 && estadof7==0 && estadof6==1 && estadof5==1 && estadof4==0 && estadof3==0 && estadof2==1 && estadof1==1){
                    universal(context,"utla10110011");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-1-0-0-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //180
                if(estadof8==1 && estadof7==0 && estadof6==1 && estadof5==1 && estadof4==0 && estadof3==1 && estadof2==0 && estadof1==0){
                    universal(context,"utla10110100");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-1-0-1-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //181
                if(estadof8==1 && estadof7==0 && estadof6==1 && estadof5==1 && estadof4==0 && estadof3==1 && estadof2==0 && estadof1==1){
                    universal(context,"utla10110101");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-1-0-1-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //182
                if(estadof8==1 && estadof7==0 && estadof6==1 && estadof5==1 && estadof4==0 && estadof3==1 && estadof2==1 && estadof1==0){
                    universal(context,"utla10110110");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-1-0-1-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //183
                if(estadof8==1 && estadof7==0 && estadof6==1 && estadof5==1 && estadof4==0 && estadof3==1 && estadof2==1 && estadof1==1){
                    universal(context,"utla10110111");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-1-0-1-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //184
                if(estadof8==1 && estadof7==0 && estadof6==1 && estadof5==1 && estadof4==1 && estadof3==0 && estadof2==0 && estadof1==0){
                    universal(context,"utla10111000");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-1-1-0-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //185
                if(estadof8==1 && estadof7==0 && estadof6==1 && estadof5==1 && estadof4==1 && estadof3==0 && estadof2==0 && estadof1==1){
                    universal(context,"utla10111001");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-1-1-0-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //186
                if(estadof8==1 && estadof7==0 && estadof6==1 && estadof5==1 && estadof4==1 && estadof3==0 && estadof2==1 && estadof1==0){
                    universal(context,"utla10111010");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-1-1-0-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //187
                if(estadof8==1 && estadof7==0 && estadof6==1 && estadof5==1 && estadof4==1 && estadof3==0 && estadof2==1 && estadof1==1){
                    universal(context,"utla10111011");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-1-1-0-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //188
                if(estadof8==1 && estadof7==0 && estadof6==1 && estadof5==1 && estadof4==1 && estadof3==1 && estadof2==0 && estadof1==0){
                    universal(context,"utla10111100");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-1-1-1-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //189
                if(estadof8==1 && estadof7==0 && estadof6==1 && estadof5==1 && estadof4==1 && estadof3==1 && estadof2==0 && estadof1==1){
                    universal(context,"utla10111101");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-1-1-1-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //190
                if(estadof8==1 && estadof7==0 && estadof6==1 && estadof5==1 && estadof4==1 && estadof3==1 && estadof2==1 && estadof1==0){
                    universal(context,"utla10111110");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-1-1-1-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //191
                if(estadof8==1 && estadof7==0 && estadof6==1 && estadof5==1 && estadof4==1 && estadof3==1 && estadof2==1 && estadof1==1){
                    universal(context,"utla10111111");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-1-1-1-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //192
                if(estadof8==1 && estadof7==1 && estadof6==0 && estadof5==0 && estadof4==0 && estadof3==0 && estadof2==0 && estadof1==0){
                    universal(context,"utla11000000");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-0-0-0-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //193
                if(estadof8==1 && estadof7==1 && estadof6==0 && estadof5==0 && estadof4==0 && estadof3==0 && estadof2==0 && estadof1==1){
                    universal(context,"utla11000001");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-0-0-0-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //194
                if(estadof8==1 && estadof7==1 && estadof6==0 && estadof5==0 && estadof4==0 && estadof3==0 && estadof2==1 && estadof1==0){
                    universal(context,"utla11000010");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-0-0-0-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //195
                if(estadof8==1 && estadof7==1 && estadof6==0 && estadof5==0 && estadof4==0 && estadof3==0 && estadof2==1 && estadof1==1){
                    universal(context,"utla11000011");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-0-0-0-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //196
                if(estadof8==1 && estadof7==1 && estadof6==0 && estadof5==0 && estadof4==0 && estadof3==1 && estadof2==0 && estadof1==0){
                    universal(context,"utla11000100");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-0-0-1-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //197
                if(estadof8==1 && estadof7==1 && estadof6==0 && estadof5==0 && estadof4==0 && estadof3==1 && estadof2==0 && estadof1==1){
                    universal(context,"utla11000101");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-0-0-1-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //198
                if(estadof8==1 && estadof7==1 && estadof6==0 && estadof5==0 && estadof4==0 && estadof3==1 && estadof2==1 && estadof1==0){
                    universal(context,"utla11000110");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-0-0-1-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //199
                if(estadof8==1 && estadof7==1 && estadof6==0 && estadof5==0 && estadof4==0 && estadof3==1 && estadof2==1 && estadof1==1){
                    universal(context,"utla11000111");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-0-0-1-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //200
                if(estadof8==1 && estadof7==1 && estadof6==0 && estadof5==0 && estadof4==1 && estadof3==0 && estadof2==0 && estadof1==0){
                    universal(context,"utla11001000");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-0-1-0-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //201
                if(estadof8==1 && estadof7==1 && estadof6==0 && estadof5==0 && estadof4==1 && estadof3==0 && estadof2==0 && estadof1==1){
                    universal(context,"utla11001001");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-0-1-0-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //202
                if(estadof8==1 && estadof7==1 && estadof6==0 && estadof5==0 && estadof4==1 && estadof3==0 && estadof2==1 && estadof1==0){
                    universal(context,"utla11001010");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-0-1-0-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //203
                if(estadof8==1 && estadof7==1 && estadof6==0 && estadof5==0 && estadof4==1 && estadof3==0 && estadof2==1 && estadof1==1){
                    universal(context,"utla11001011");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-0-1-0-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //204
                if(estadof8==1 && estadof7==1 && estadof6==0 && estadof5==0 && estadof4==1 && estadof3==1 && estadof2==0 && estadof1==0){
                    universal(context,"utla11001100");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-0-1-1-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //205
                if(estadof8==1 && estadof7==1 && estadof6==0 && estadof5==0 && estadof4==1 && estadof3==1 && estadof2==0 && estadof1==1){
                    universal(context,"utla11001101");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-0-1-1-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //206
                if(estadof8==1 && estadof7==1 && estadof6==0 && estadof5==0 && estadof4==1 && estadof3==1 && estadof2==1 && estadof1==0){
                    universal(context,"utla11001110");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-0-1-1-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //207
                if(estadof8==1 && estadof7==1 && estadof6==0 && estadof5==0 && estadof4==1 && estadof3==1 && estadof2==1 && estadof1==1){
                    universal(context,"utla11001111");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-0-1-1-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //208
                if(estadof8==1 && estadof7==1 && estadof6==0 && estadof5==1 && estadof4==0 && estadof3==0 && estadof2==0 && estadof1==0){
                    universal(context,"utla11010000");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-1-0-0-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //209
                if(estadof8==1 && estadof7==1 && estadof6==0 && estadof5==1 && estadof4==0 && estadof3==0 && estadof2==0 && estadof1==1){
                    universal(context,"utla11010001");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-1-0-0-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //210
                if(estadof8==1 && estadof7==1 && estadof6==0 && estadof5==1 && estadof4==0 && estadof3==0 && estadof2==1 && estadof1==0){
                    universal(context,"utla11010010");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-1-0-0-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //211
                if(estadof8==1 && estadof7==1 && estadof6==0 && estadof5==1 && estadof4==0 && estadof3==0 && estadof2==1 && estadof1==1){
                    universal(context,"utla11010011");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-1-0-0-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //212
                if(estadof8==1 && estadof7==1 && estadof6==0 && estadof5==1 && estadof4==0 && estadof3==1 && estadof2==0 && estadof1==0){
                    universal(context,"utla11010100");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-1-0-1-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //213
                if(estadof8==1 && estadof7==1 && estadof6==0 && estadof5==1 && estadof4==0 && estadof3==1 && estadof2==0 && estadof1==1){
                    universal(context,"utla11010101");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-1-0-1-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //214
                if(estadof8==1 && estadof7==1 && estadof6==0 && estadof5==1 && estadof4==0 && estadof3==1 && estadof2==1 && estadof1==0){
                    universal(context,"utla11010110");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-1-0-1-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //215
                if(estadof8==1 && estadof7==1 && estadof6==0 && estadof5==1 && estadof4==0 && estadof3==1 && estadof2==1 && estadof1==1){
                    universal(context,"utla11010111");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-1-0-1-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //216
                if(estadof8==1 && estadof7==1 && estadof6==0 && estadof5==1 && estadof4==1 && estadof3==0 && estadof2==0 && estadof1==0){
                    universal(context,"utla11011000");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-1-1-0-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //217
                if(estadof8==1 && estadof7==1 && estadof6==0 && estadof5==1 && estadof4==1 && estadof3==0 && estadof2==0 && estadof1==1){
                    universal(context,"utla11011001");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-1-1-0-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //218
                if(estadof8==1 && estadof7==1 && estadof6==0 && estadof5==1 && estadof4==1 && estadof3==0 && estadof2==1 && estadof1==0){
                    universal(context,"utla11011010");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-1-1-0-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //219
                if(estadof8==1 && estadof7==1 && estadof6==0 && estadof5==1 && estadof4==1 && estadof3==0 && estadof2==1 && estadof1==1){
                    universal(context,"utla11011011");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-1-1-0-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //220
                if(estadof8==1 && estadof7==1 && estadof6==0 && estadof5==1 && estadof4==1 && estadof3==1 && estadof2==0 && estadof1==0){
                    universal(context,"utla11011100");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-1-1-1-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //221
                if(estadof8==1 && estadof7==1 && estadof6==0 && estadof5==1 && estadof4==1 && estadof3==1 && estadof2==0 && estadof1==1){
                    universal(context,"utla11011101");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-1-1-1-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //222
                if(estadof8==1 && estadof7==1 && estadof6==0 && estadof5==1 && estadof4==1 && estadof3==1 && estadof2==1 && estadof1==0){
                    universal(context,"utla11011110");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-1-1-1-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //223
                if(estadof8==1 && estadof7==1 && estadof6==0 && estadof5==1 && estadof4==1 && estadof3==1 && estadof2==1 && estadof1==1){
                    universal(context,"utla11011111");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-1-1-1-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //224
                if(estadof8==1 && estadof7==1 && estadof6==1 && estadof5==0 && estadof4==0 && estadof3==0 && estadof2==0 && estadof1==0){
                    universal(context,"utla11100000");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-0-0-0-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //225
                if(estadof8==1 && estadof7==1 && estadof6==1 && estadof5==0 && estadof4==0 && estadof3==0 && estadof2==0 && estadof1==1){
                    universal(context,"utla11100001");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-0-0-0-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //226
                if(estadof8==1 && estadof7==1 && estadof6==1 && estadof5==0 && estadof4==0 && estadof3==0 && estadof2==1 && estadof1==0){
                    universal(context,"utla11100010");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-0-0-0-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //227
                if(estadof8==1 && estadof7==1 && estadof6==1 && estadof5==0 && estadof4==0 && estadof3==0 && estadof2==1 && estadof1==1){
                    universal(context,"utla11100011");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-0-0-0-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //228
                if(estadof8==1 && estadof7==1 && estadof6==1 && estadof5==0 && estadof4==0 && estadof3==1 && estadof2==0 && estadof1==0){
                    universal(context,"utla11100100");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-0-0-1-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //229
                if(estadof8==1 && estadof7==1 && estadof6==1 && estadof5==0 && estadof4==0 && estadof3==1 && estadof2==0 && estadof1==1){
                    universal(context,"utla11100101");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-0-0-1-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //230
                if(estadof8==1 && estadof7==1 && estadof6==1 && estadof5==0 && estadof4==0 && estadof3==1 && estadof2==1 && estadof1==0){
                    universal(context,"utla11100110");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-0-0-1-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //231
                if(estadof8==1 && estadof7==1 && estadof6==1 && estadof5==0 && estadof4==0 && estadof3==1 && estadof2==1 && estadof1==1){
                    universal(context,"utla11100111");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-0-0-1-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //232
                if(estadof8==1 && estadof7==1 && estadof6==1 && estadof5==0 && estadof4==1 && estadof3==0 && estadof2==0 && estadof1==0){
                    universal(context,"utla11101000");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-0-1-0-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //233
                if(estadof8==1 && estadof7==1 && estadof6==1 && estadof5==0 && estadof4==1 && estadof3==0 && estadof2==0 && estadof1==1){
                    universal(context,"utla11101001");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-0-1-0-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //234
                if(estadof8==1 && estadof7==1 && estadof6==1 && estadof5==0 && estadof4==1 && estadof3==0 && estadof2==1 && estadof1==0){
                    universal(context,"utla11101010");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-0-1-0-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //235
                if(estadof8==1 && estadof7==1 && estadof6==1 && estadof5==0 && estadof4==1 && estadof3==0 && estadof2==1 && estadof1==1){
                    universal(context,"utla11101011");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-0-1-0-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //236
                if(estadof8==1 && estadof7==1 && estadof6==1 && estadof5==0 && estadof4==1 && estadof3==1 && estadof2==0 && estadof1==0){
                    universal(context,"utla11101100");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-0-1-1-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //237
                if(estadof8==1 && estadof7==1 && estadof6==1 && estadof5==0 && estadof4==1 && estadof3==1 && estadof2==0 && estadof1==1){
                    universal(context,"utla11101101");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-0-1-1-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //238
                if(estadof8==1 && estadof7==1 && estadof6==1 && estadof5==0 && estadof4==1 && estadof3==1 && estadof2==1 && estadof1==0){
                    universal(context,"utla11101110");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-0-1-1-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //239
                if(estadof8==1 && estadof7==1 && estadof6==1 && estadof5==0 && estadof4==1 && estadof3==1 && estadof2==1 && estadof1==1){
                    universal(context,"utla11101111");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-0-1-1-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //240
                if(estadof8==1 && estadof7==1 && estadof6==1 && estadof5==1 && estadof4==0 && estadof3==0 && estadof2==0 && estadof1==0){
                    universal(context,"utla11110000");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-1-0-0-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //241
                if(estadof8==1 && estadof7==1 && estadof6==1 && estadof5==1 && estadof4==0 && estadof3==0 && estadof2==0 && estadof1==1){
                    universal(context,"utla11110001");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-1-0-0-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //242
                if(estadof8==1 && estadof7==1 && estadof6==1 && estadof5==1 && estadof4==0 && estadof3==0 && estadof2==1 && estadof1==0){
                    universal(context,"utla11110010");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-1-0-0-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //243
                if(estadof8==1 && estadof7==1 && estadof6==1 && estadof5==1 && estadof4==0 && estadof3==0 && estadof2==1 && estadof1==1){
                    universal(context,"utla11110011");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-1-0-0-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //244
                if(estadof8==1 && estadof7==1 && estadof6==1 && estadof5==1 && estadof4==0 && estadof3==1 && estadof2==0 && estadof1==0){
                    universal(context,"utla11110100");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-1-0-1-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //245
                if(estadof8==1 && estadof7==1 && estadof6==1 && estadof5==1 && estadof4==0 && estadof3==1 && estadof2==0 && estadof1==1){
                    universal(context,"utla11110101");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-1-0-1-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //246
                if(estadof8==1 && estadof7==1 && estadof6==1 && estadof5==1 && estadof4==0 && estadof3==1 && estadof2==1 && estadof1==0){
                    universal(context,"utla11110110");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-1-0-1-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //247
                if(estadof8==1 && estadof7==1 && estadof6==1 && estadof5==1 && estadof4==0 && estadof3==1 && estadof2==1 && estadof1==1){
                    universal(context,"utla11110111");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-1-0-1-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //248
                if(estadof8==1 && estadof7==1 && estadof6==1 && estadof5==1 && estadof4==1 && estadof3==0 && estadof2==0 && estadof1==0){
                    universal(context,"utla11111000");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-1-1-0-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //249
                if(estadof8==1 && estadof7==1 && estadof6==1 && estadof5==1 && estadof4==1 && estadof3==0 && estadof2==0 && estadof1==1){
                    universal(context,"utla11111001");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-1-1-0-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //250
                if(estadof8==1 && estadof7==1 && estadof6==1 && estadof5==1 && estadof4==1 && estadof3==0 && estadof2==1 && estadof1==0){
                    universal(context,"utla11111010");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-1-1-0-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //251
                if(estadof8==1 && estadof7==1 && estadof6==1 && estadof5==1 && estadof4==1 && estadof3==0 && estadof2==1 && estadof1==1){
                    universal(context,"utla11111011");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-1-1-0-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //252
                if(estadof8==1 && estadof7==1 && estadof6==1 && estadof5==1 && estadof4==1 && estadof3==1 && estadof2==0 && estadof1==0){
                    universal(context,"utla11111100");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-1-1-1-0-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //253
                if(estadof8==1 && estadof7==1 && estadof6==1 && estadof5==1 && estadof4==1 && estadof3==1 && estadof2==0 && estadof1==1){
                    universal(context,"utla11111101");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-1-1-1-0-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //254
                if(estadof8==1 && estadof7==1 && estadof6==1 && estadof5==1 && estadof4==1 && estadof3==1 && estadof2==1 && estadof1==0){
                    universal(context,"utla11111110");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-1-1-1-1-0\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }

                //255
                if(estadof8==1 && estadof7==1 && estadof6==1 && estadof5==1 && estadof4==1 && estadof3==1 && estadof2==1 && estadof1==1){
                    universal(context,"utla11111111");
                    save_action_phone(context,"L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-1-1-1-1-1\nCon confirmación", getTelCkto(context), obtenerTelPropietario(context), getDate(), getTime());
                    setStatusDefault();
                }
                //myDialog.dismiss();
            }
        });
    }

    public void setStatusDefault(){
        estadof1=0;estadof2=0;estadof3=0;estadof4=0;estadof5=0;estadof6=0;estadof7=0;estadof8=0;
    }

    public String getTelCkto(Context context){
        SharedPreferences preferences = context.getSharedPreferences("Destinatarios", Context.MODE_PRIVATE);
        String t1 = preferences.getString("telefono1","Sin configurar.");
        return t1;
    }

    public void mensaje(Context context){
        Toast.makeText(context, "No se ha configurado el dispositivo receptor aún.", Toast.LENGTH_SHORT).show();
    }

    public void EnviarMensaje(final Context context,String Numero, String Mensaje) {
        try {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(Numero, null, Mensaje, null, null);
            Toast.makeText(context, "Comando de control enviado.\n\nDestinatario:"+Numero, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(context, "Mensaje no enviado, datos incorrectos.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void msg(final Context context,String s){
        /*Toast toast = Toast.makeText(getApplicationContext(), "Campo respuesta es obligatorio", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();*/
        Toast.makeText(context,s,Toast.LENGTH_LONG).show();
    }

    public void universal(final Context context, final String mensaje1) {
        String telckto = getTelCkto(context);
        if (telckto.equals("Sin configurar.")) {
            mensaje(context);
        } else {
            final String mensaje = "¿Realmente desea cambiar el estado" +
                    " de la/las luminarias seleccionadas?";
            dialogo = new AlertDialog.Builder(context);
            dialogo.setIcon(R.drawable.ic_sms);
            //dialog.setTitle("COCESNA.");
            dialogo.setTitle("SMS CONTROL");
            dialogo.setMessage(mensaje);
            dialogo.setCancelable(false);
            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    try {
                        EnviarMensaje(context, getTelCkto(context), mensaje1);
                        //EnviarMensaje(getTelCkto(), "Cambiar");
                        myDialog.dismiss();
                    } catch (Exception e) {
                        msg(context,"Error. Posiblemente no haya señal.\n" +
                                "Intentelo mas tarde.");
                    }
                }
            });
            dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    Toast.makeText(context, "Operación Cancelada.", Toast.LENGTH_LONG).show();
                    //myDialog.dismiss();
                    estadof1=0;estadof2=0;estadof3=0;estadof4=0;
                    estadof5=0;estadof6=0;estadof7=0;estadof8=0;
                    sw0.setChecked(false);sw1.setChecked(false);sw2.setChecked(false);
                    sw3.setChecked(false);sw4.setChecked(false);sw5.setChecked(false);
                    sw6.setChecked(false);sw7.setChecked(false);sw8.setChecked(false);

                    f_all.setImageResource(R.drawable.foco1off);
                    f1.setImageResource(R.drawable.foco1off);
                    f2.setImageResource(R.drawable.foco1off);
                    f3.setImageResource(R.drawable.foco1off);
                    f4.setImageResource(R.drawable.foco1off);
                    f5.setImageResource(R.drawable.foco1off);
                    f6.setImageResource(R.drawable.foco1off);
                    f7.setImageResource(R.drawable.foco1off);
                    f8.setImageResource(R.drawable.foco1off);
                }
            });
            dialogo.show();
        }

    }


    //Cuadro de dialogo de solicitud de llamada perdida
    public void getCall(final Context context) {
        String telckto = getTelCkto(context);
        if (telckto.equals("Sin configurar.")) {
            mensaje(context);
        } else {
            String mensaje = Config.mensaje1+"\n\"Recibir una llamada perdida del sistema electrónico.\"" +
                    "\n\nRecordatorio:\n**El sistema electrónico debe estar en estado ON." +
                    "\n**Disponibilidad de señal para comunicación en ambos dispositivos." +
                    "\n**Disponibilidad de saldo en ambos dispositivos (Receptor y Emisor).\n";
            dialogo = new AlertDialog.Builder(context);
            dialogo.setIcon(R.drawable.ic_call);
            dialogo.setTitle("$-> Solicitud de Llamada.");
            dialogo.setMessage(mensaje);
            dialogo.setCancelable(false);
            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    try {
                        EnviarMensaje(context,getTelCkto(context), "llamame OK");
                        save_action_phone(context,"Solicitud de llamada perdida.",getTelReceptor(context),getTelPropietario(context),getDate(),getTime());
                    } catch (Exception e) {
                        msg(context,"Error. Posiblemente no haya señal.\n" +
                                "Intentelo mas tarde.");
                    }

                }
            });
            dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    Toast.makeText(context, "Operación Cancelada.", Toast.LENGTH_LONG).show();
                }
            });
            dialogo.show();
        }
    }


    //Cuadro de dialogo de solicitud de saludo por sms.
    public void dialog_Saludo(final Context context,String mensaje){
        myDialog = new Dialog(context);
        myDialog.setContentView(R.layout.custompopup2);
        myDialog.setTitle("SMS INTO");
        myDialog.setCancelable(false);
        ImageView ivClose = (ImageView)myDialog.findViewById(R.id.ivClose);
        TextView messageTv = (TextView)myDialog.findViewById(R.id.messageTv);
        messageTv.setText(mensaje.toString());
        Button btnAccept = (Button)myDialog.findViewById(R.id.btnAccept);
        /*
        DateFormat formatodate= new SimpleDateFormat("yyyy/MM/dd");
        String date= formatodate.format(new Date());
        DateFormat formatotime= new SimpleDateFormat("HH:mm:ss a");
        String time= formatotime.format(new Date());
        */
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                //Intent retornar = new Intent(context, MainActivity.class);
                //context.startActivity(retornar);

            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }


    public void dialog_temp_RH_voz(final Context context,String temperatura,String humedad) {
        myDialog = new Dialog(context);
        /*
        TextView tv_title,txtclose,address,tvTemperatura,tvHumedad,tvFecha,tvHora;
        myDialog.setContentView(R.layout.intosms_temp_rh);
        myDialog.setTitle("MONITOR AMBIENTE");
        myDialog.setCancelable(false);

        tv_title = (TextView)myDialog.findViewById(R.id.tv_title);
        txtclose = (TextView)myDialog.findViewById(R.id.txtclose);
        address = (TextView)myDialog.findViewById(R.id.address);
        tvTemperatura=(TextView)myDialog.findViewById(R.id.tvTemperatura);
        tvHumedad=(TextView)myDialog.findViewById(R.id.tvHumedad);
        tvFecha=(TextView)myDialog.findViewById(R.id.tvFecha);
        tvHora=(TextView)myDialog.findViewById(R.id.tvHora);

        DateFormat formatodate= new SimpleDateFormat("yyyy/MM/dd");
        String date= formatodate.format(new Date());
        DateFormat formatotime= new SimpleDateFormat("HH:mm:ss a");
        String time= formatotime.format(new Date());

        tv_title.setText("MONITOREO BLUETOOTH");
        address.setText("<<<DATOS DE SENSORES>>>");
        tvTemperatura.setText("Temperatura: "+temperatura+" °C");
        tvHumedad.setText("Humedad Relativa: "+humedad+" % [RH]");

        tvFecha.setText("Fecha: "+date);
        tvHora.setText("Hora: "+time);

        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
        */


        TextView tv_title,txtclose,address,tvTemperatura,tvHumedad,tvFecha,tvHora;
        myDialog.setContentView(R.layout.intosms_temp_rh);
        myDialog.setTitle("MONITOR AMBIENTE");
        myDialog.setCancelable(false);

        tv_title = (TextView)myDialog.findViewById(R.id.tv_title);
        txtclose = (TextView)myDialog.findViewById(R.id.txtclose);
        address = (TextView)myDialog.findViewById(R.id.address);
        tvTemperatura=(TextView)myDialog.findViewById(R.id.tvTemperatura);
        tvHumedad=(TextView)myDialog.findViewById(R.id.tvHumedad);
        tvFecha=(TextView)myDialog.findViewById(R.id.tvFecha);
        tvHora=(TextView)myDialog.findViewById(R.id.tvHora);

        DateFormat formatodate= new SimpleDateFormat("yyyy/MM/dd");
        String date= formatodate.format(new Date());
        DateFormat formatotime= new SimpleDateFormat("HH:mm:ss a");
        String time= formatotime.format(new Date());

        tv_title.setText("MONITOREO BLUETOOTH");
        address.setText("<<<DATOS DE SENSORES>>>");
        tvTemperatura.setText("Temperatura: "+temperatura+" °C");
        tvHumedad.setText("Humedad Relativa: "+humedad+" % [RH]");

        tvFecha.setText("Fecha: "+date);
        tvHora.setText("Hora: "+time);

        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

    }


    public void dialog_luces_voz(final Context context,String foco1, String foco2, String foco3, String foco4, String foco5, String foco6, String foco7, String foco8) {
        myDialog = new Dialog(context);
        myDialog.setContentView(R.layout.dashboardbtvoz);
        myDialog.setTitle("MONITOR LUCES");
        myDialog.setCancelable(false);
        TextView xClose = (TextView) myDialog.findViewById(R.id.xClose);
        ImageView ivFoco1, ivFoco2, ivFoco3, ivFoco4, ivFoco5, ivFoco6, ivFoco7, ivFoco8;
        Button btnAceptar;
        ivFoco1 = (ImageView) myDialog.findViewById(R.id.ivFoco1);
        ivFoco2 = (ImageView) myDialog.findViewById(R.id.ivFoco2);
        ivFoco3 = (ImageView) myDialog.findViewById(R.id.ivFoco3);
        ivFoco4 = (ImageView) myDialog.findViewById(R.id.ivFoco4);
        ivFoco5 = (ImageView) myDialog.findViewById(R.id.ivFoco5);
        ivFoco6 = (ImageView) myDialog.findViewById(R.id.ivFoco6);
        ivFoco7 = (ImageView) myDialog.findViewById(R.id.ivFoco7);
        ivFoco8 = (ImageView) myDialog.findViewById(R.id.ivFoco8);
        btnAceptar = (Button) myDialog.findViewById(R.id.btnAceptar);

        xClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });


        if(foco1.equals("1")){
            ivFoco1.setImageResource(R.drawable.foco1on);
        }else{
            ivFoco1.setImageResource(R.drawable.foco1off);
        }

        if(foco2.equals("1")){
            ivFoco2.setImageResource(R.drawable.foco1on);
        }else{
            ivFoco2.setImageResource(R.drawable.foco1off);
        }

        if(foco3.equals("1")){
            ivFoco3.setImageResource(R.drawable.foco1on);
        }else{
            ivFoco3.setImageResource(R.drawable.foco1off);
        }

        if(foco4.equals("1")){
            ivFoco4.setImageResource(R.drawable.foco1on);
        }else{
            ivFoco4.setImageResource(R.drawable.foco1off);
        }

        if(foco5.equals("1")){
            ivFoco5.setImageResource(R.drawable.foco1on);
        }else{
            ivFoco5.setImageResource(R.drawable.foco1off);
        }

        if(foco6.equals("1")){
            ivFoco6.setImageResource(R.drawable.foco1on);
        }else{
            ivFoco6.setImageResource(R.drawable.foco1off);
        }

        if(foco7.equals("1")){
            ivFoco7.setImageResource(R.drawable.foco1on);
        }else{
            ivFoco7.setImageResource(R.drawable.foco1off);
        }

        if(foco8.equals("1")){
            ivFoco8.setImageResource(R.drawable.foco1on);
        }else{
            ivFoco8.setImageResource(R.drawable.foco1off);
        }

        /*if(foco1.equals("1") && foco2.equals("1") && foco3.equals("1") && foco4.equals("1") &&
                foco5.equals("1") && foco6.equals("1") && foco7.equals("1") && foco8.equals("1")){
        }else{ }*/

        /*

        Toast.makeText(context,
                "foco1: "+foco1 +"\n" +
                "foco2: "+foco2 +"\n" +
                "foco3: "+foco3 +"\n" +
                "foco4: "+foco4 +"\n" +
                "foco5: "+foco5 +"\n" +
                "foco6: "+foco6 +"\n" +
                "foco7: "+foco7 +"\n" +
                "foco8: "+foco8, Toast.LENGTH_SHORT).show();  */


        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }


    //Ultima función adicionada: Dialogo que mostrará todos los datos de los sensores.
    public void dialog_info_all_sensors_voz(final Context context,String foco1, String foco2, String foco3, String foco4, String foco5, String foco6, String foco7, String foco8, String temperatura, String humedad) {
        myDialog = new Dialog(context);
        myDialog.setContentView(R.layout.dashboardbtvozsensores);
        myDialog.setTitle("MONITOR LUCES");
        myDialog.setCancelable(false);
        TextView xClose = (TextView) myDialog.findViewById(R.id.xClose);
        TextView tv_temperatura = (TextView) myDialog.findViewById(R.id.tv_temperatura);
        TextView tv_humedad = (TextView) myDialog.findViewById(R.id.tv_humedad);
        ImageView ivFoco1, ivFoco2, ivFoco3, ivFoco4, ivFoco5, ivFoco6, ivFoco7, ivFoco8;
        Button btnAceptar;
        ivFoco1 = (ImageView) myDialog.findViewById(R.id.ivFoco1);
        ivFoco2 = (ImageView) myDialog.findViewById(R.id.ivFoco2);
        ivFoco3 = (ImageView) myDialog.findViewById(R.id.ivFoco3);
        ivFoco4 = (ImageView) myDialog.findViewById(R.id.ivFoco4);
        ivFoco5 = (ImageView) myDialog.findViewById(R.id.ivFoco5);
        ivFoco6 = (ImageView) myDialog.findViewById(R.id.ivFoco6);
        ivFoco7 = (ImageView) myDialog.findViewById(R.id.ivFoco7);
        ivFoco8 = (ImageView) myDialog.findViewById(R.id.ivFoco8);
        btnAceptar = (Button) myDialog.findViewById(R.id.btnAceptar);

        xClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });


        if(foco1.equals("1")){
            ivFoco1.setImageResource(R.drawable.foco1on);
        }else{
            ivFoco1.setImageResource(R.drawable.foco1off);
        }

        if(foco2.equals("1")){
            ivFoco2.setImageResource(R.drawable.foco1on);
        }else{
            ivFoco2.setImageResource(R.drawable.foco1off);
        }

        if(foco3.equals("1")){
            ivFoco3.setImageResource(R.drawable.foco1on);
        }else{
            ivFoco3.setImageResource(R.drawable.foco1off);
        }

        if(foco4.equals("1")){
            ivFoco4.setImageResource(R.drawable.foco1on);
        }else{
            ivFoco4.setImageResource(R.drawable.foco1off);
        }

        if(foco5.equals("1")){
            ivFoco5.setImageResource(R.drawable.foco1on);
        }else{
            ivFoco5.setImageResource(R.drawable.foco1off);
        }

        if(foco6.equals("1")){
            ivFoco6.setImageResource(R.drawable.foco1on);
        }else{
            ivFoco6.setImageResource(R.drawable.foco1off);
        }

        if(foco7.equals("1")){
            ivFoco7.setImageResource(R.drawable.foco1on);
        }else{
            ivFoco7.setImageResource(R.drawable.foco1off);
        }

        if(foco8.equals("1")){
            ivFoco8.setImageResource(R.drawable.foco1on);
        }else{
            ivFoco8.setImageResource(R.drawable.foco1off);
        }

        tv_temperatura.setText(temperatura+" °C");
        tv_humedad.setText(humedad + " % RH");

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }


    //Cuadro de dialogo de solicitud de saludo por sms.
    public void ayuda_comandos(final Context context){
        myDialog = new Dialog(context);
        myDialog.setContentView(R.layout.help_comandosvoz);
        myDialog.setTitle("Help");
        myDialog.setCancelable(true);

        TextView txtclose = (TextView)myDialog.findViewById(R.id.txtclose);

        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }


}
