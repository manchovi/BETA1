package com.mjgl.utla.utla;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class luces_control extends AppCompatActivity {

    Handler bluetoothIn;
    final int handlerState = 0;        				 //used to identify handler message
    private StringBuilder recDataString = new StringBuilder();
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    // SPP UUID service - this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    //private BluetoothDevice device;
    private BluetoothSocket btSocket = null;
    //BluetoothSocket btSocket = null;
    //private OutputStream outputStream;
    //BluetoothAdapter myBluetooth = null;
    private BluetoothAdapter btAdapter = null;
    private BluetoothAdapter BA;
    //private BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;

    private ConnectedThread mConnectedThread;
    // String for MAC address
    private static String address = null;




    boolean bandera = false;
    private static final int RECOGNIZE_SPEECH_ACTIVITY = 1;

    AlertDialog.Builder dialogo;
    ImageView ivLuz1, ivLuz2, ivLuz3, ivLuz4, ivLuz5, ivLuz6, ivLuz7, ivLuz8, ivLuz9, ivLuz10;
    TextView tvLuz1Bajo, tvLuz2Bajo, tvLuz3Bajo, tvLuz4Bajo, tvLuz5Bajo, tvLuz6Bajo, tvLuz7Bajo, tvLuz8Bajo, tvLuz9Bajo, tvLuz10Bajo, tv_Data;
    ImageView btnEstadoLuces, btnRetornar;
    SeekBar brightness;
    TextView lumn;
    Toolbar toolbar;
    private TextView grabar = null;

    //String address = null
    ProgressDialog progress;
    Dialog myDialog;
    dbSQLiteHelper admin=new dbSQLiteHelper(this);

    String temperatura="";
    String humedad="";


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new android.app.AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Advertencia.")
                    .setMessage("¿Esta seguro que desea salir?\n" +
                            "Se cerrara la conexión abierta con \n" +
                            "el módulo BlueTooth.")
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {//un listener que al pulsar, cierre la aplicacion
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Salir
                            Disconnet1();
                            salir();
                        }
                    })
                    .show();
            // Si el listener devuelve true, significa que el evento esta procesado, y nadie debe hacer nada mas
            return true;
        }
        //para las demas cosas, se reenvia el evento al listener habitual
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luces_control);

        myDialog = new Dialog(this);

        //mConnectedThread.write("todosensoresinfoki");

        //AL FINAL MEJOR CREE MI PROPIA TOOLBAR.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_left));
        toolbar.setTitleTextColor(getResources().getColor(R.color.mycolor_blanco));
        toolbar.setTitleMargin(0,0,0,0);
        toolbar.setSubtitle("BLUETOOTH CONTROL");
        getSupportActionBar().setTitle("UTLA");

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //COn las siguientes 2 lineas habilito el boton con icono de flecha hacia atras en la parte superior
        //de la toolbar.

        /*
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        */

        //Función para evitar la rotación de la pantalla del CELULAR.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //esto quita el título de la activity en la parte superior
        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        //y esto para pantalla completa (oculta incluso la barra de estado)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Función para evitar la rotación de la pantalla del CELULAR.
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        grabar = (TextView) findViewById(R.id.txtGabar);

        //Intent newint = getIntent();
        //address = newint.getStringExtra(device_list.EXTRA_ADDRESS); //recivimos la mac address obtenida en la actividad anterior
        //new ConnectBT().execute(); //Call the class to connect

        tvLuz1Bajo = (TextView)findViewById(R.id.tvLuz1Bajo);
        tvLuz2Bajo = (TextView)findViewById(R.id.tvLuz2Bajo);
        tvLuz3Bajo = (TextView)findViewById(R.id.tvLuz3Bajo);
        tvLuz4Bajo = (TextView)findViewById(R.id.tvLuz4Bajo);
        tvLuz5Bajo = (TextView)findViewById(R.id.tvLuz5Bajo);
        tvLuz6Bajo = (TextView)findViewById(R.id.tvLuz6Bajo);
        tvLuz7Bajo = (TextView)findViewById(R.id.tvLuz7Bajo);
        tvLuz8Bajo = (TextView)findViewById(R.id.tvLuz8Bajo);
        tvLuz9Bajo = (TextView)findViewById(R.id.tvLuz9Bajo);
        tvLuz10Bajo = (TextView)findViewById(R.id.tvLuz10Bajo);
        tv_Data = (TextView)findViewById(R.id.tv_Data);


        ivLuz1 = (ImageView) findViewById(R.id.ivLuz1);
        ivLuz2 = (ImageView)findViewById(R.id.ivLuz2);
        ivLuz3 = (ImageView)findViewById(R.id.ivLuz3);
        ivLuz4 = (ImageView)findViewById(R.id.ivLuz4);
        ivLuz5 = (ImageView)findViewById(R.id.ivLuz5);
        ivLuz6 = (ImageView)findViewById(R.id.ivLuz6);
        ivLuz7 = (ImageView)findViewById(R.id.ivLuz7);
        ivLuz8 = (ImageView)findViewById(R.id.ivLuz8);
        ivLuz9 = (ImageView)findViewById(R.id.ivLuz9);    //Todas las luces
        ivLuz10 = (ImageView)findViewById(R.id.ivLuz10);  //Consulta temperatura y humedad relativa

        btnEstadoLuces = (ImageView) findViewById(R.id.btnEstadoLuces);
        btnRetornar = (ImageView) findViewById(R.id.btnRetornar);

        //Toast.makeText(this, "Ya entramos...", Toast.LENGTH_LONG).show();

        //Snackbar.make(null, "Universidad Técnica Latinoamerica\nSanta Tecla, 2018-2019", Snackbar.LENGTH_LONG)
         //       .setAction("Action", null).show();

        //ivLuz1.setBackgroundResource(R.drawable.cerclebackgroundpink);
        //ivLuz1.setImageResource(R.drawable.f1on);

        //LECTURA DE INFORMACIÓN ENVIADA DESDE ARDUINO. MONITOREO POR BLUETOOTH
        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {										   //if message is what we want
                    String readMessage = (String) msg.obj;                                                                // msg.arg1 = bytes from connect thread
                    recDataString.append(readMessage);
                    //Toast.makeText(luces_control.this, "Datos recibidos = " + readMessage, Toast.LENGTH_SHORT).show();
                    //keep appending to string until ~
                    int endOfLineIndex = recDataString.indexOf("~");                    // determine the end-of-line

                    String primercaracter = recDataString.substring(0, 1);
                    //Toast.makeText(luces_control.this, ""+primercaracter, Toast.LENGTH_SHORT).show();

                    if(primercaracter.equals("#")) {

                        if (endOfLineIndex > 0) {                                           // make sure there data before ~
                            String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string
                            int dataLength = dataInPrint.length();                            //get length of data received
                           /* Toast.makeText(luces_control.this, "Datos recibidos = " + dataInPrint + "\n"
                                    + "Tamaño String= " + String.valueOf(dataLength), Toast.LENGTH_SHORT).show();*/

                            tv_Data.setText("Receiver:" + dataInPrint);

                            if (recDataString.charAt(0) == '#')                                //if it starts with # we know it is what we are looking for
                            {
                            /*
                            String sensor0 = recDataString.substring(1, 5);             //get sensor value from string between indices 1-5
                            String sensor1 = recDataString.substring(6, 10);            //same again...
                            String sensor2 = recDataString.substring(11, 15);
                            String sensor3 = recDataString.substring(16, 20);
                            */

                                /*Patrón a decodificar: Estado de temperatura y humedad relativa */
                                //Cadena a tratar: #:valortemperatura,valorhumedad~
                                String str = recDataString.toString();
                                //Toast.makeText(this, ""+str, Toast.LENGTH_LONG).show();
                                for (int i = 0; i <= str.length() - 1; i++) {
                                    char c = str.charAt(i);
                                    //System.out.println(sCadena.charAt(5));
                                    if (String.valueOf(c).equals(":")) {
                                        int k = i;
                                        //Toast.makeText(this, "Posición :"+k, Toast.LENGTH_SHORT).show();
                                        for (int j = (k + 1); j <= str.length() - 1; j++) {
                                            char x = str.charAt(j);
                                            if (String.valueOf(x).equals(",")) {
                                                break;
                                            }
                                            temperatura += String.valueOf(x);
                                        }
                                    }
                                }

                                //Toast.makeText(luces_control.this, ""+temperatura, Toast.LENGTH_SHORT).show();

                                for (int n = 0; n <= str.length() - 1; n++) {
                                    char c = str.charAt(n);
                                    //System.out.println(sCadena.charAt(5));
                                    if (String.valueOf(c).equals(",")) {
                                        int k = n;
                                        for (int j = (k + 1); j <= str.length() - 1; j++) {
                                            char x = str.charAt(j);
                                            if (String.valueOf(x).equals("~")) {
                                                break;
                                            }
                                            humedad += String.valueOf(x);
                                        }
                                    }
                                }
                                //Toast.makeText(luces_control.this, ""+humedad, Toast.LENGTH_SHORT).show();
                            }

                            tvLuz10Bajo.setText(String.valueOf(temperatura)+"°C:"+String.valueOf(humedad)+"% RH");

                            dialog_BT_data(temperatura, humedad);

                            //sensorView1.setText(temperatura);
                            //sensorView2.setText(humedad);
                            //sensorView3.setText(chovi);

                            recDataString.delete(0, recDataString.length());                    //clear all string data
                            // strIncom =" ";

                            dataInPrint = " ";
                            temperatura = "";
                            humedad = "";
                            primercaracter = "";
                        }
                    }else if(primercaracter.equals("@")){
                        if (endOfLineIndex > 0) {                                               // make sure there data before ~
                            String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string
                            int dataLength = dataInPrint.length();                              //get length of data received

                            /*
                            Toast.makeText(luces_control.this, "Receiver: " + dataInPrint + "\n"
                                    + "Tamaño String= " + String.valueOf(dataLength), Toast.LENGTH_SHORT).show();
                            */
                            tv_Data.setText("Receiver:" + dataInPrint);

                            /* Patrón a decodificar: Estado de luminarias.
                               @:datoVariable# datoVariable$ datoVariable% datoVariable
   *                            &datoVariable* datoVariable, datoVariable. datoVariable~
                             */
                            String foco1 = recDataString.substring(2, 3);
                            String foco2 = recDataString.substring(4, 5);
                            String foco3 = recDataString.substring(6, 7);
                            String foco4 = recDataString.substring(8, 9);
                            String foco5 = recDataString.substring(10, 11);
                            String foco6 = recDataString.substring(12, 13);
                            String foco7 = recDataString.substring(14, 15);
                            String foco8 = recDataString.substring(16, 17);

                            if(foco1.equals("1")){
                                ivLuz1.setBackgroundResource(R.drawable.cerclebackgroundpink);
                                ivLuz1.setImageResource(R.drawable.f1on);
                                tvLuz1Bajo.setText("Estado actual: ON");
                            }else{
                                ivLuz1.setBackgroundResource(R.drawable.cerclebackgroundpink);
                                ivLuz1.setImageResource(R.drawable.f2off);
                                tvLuz1Bajo.setText("Estado actual: OFF");
                            }

                            if(foco2.equals("1")){
                                ivLuz2.setBackgroundResource(R.drawable.cerclebackgroundpink);
                                ivLuz2.setImageResource(R.drawable.f1on);
                                tvLuz2Bajo.setText("Estado actual: ON");
                            }else{
                                ivLuz2.setBackgroundResource(R.drawable.cerclebackgroundpink);
                                ivLuz2.setImageResource(R.drawable.f2off);
                                tvLuz2Bajo.setText("Estado actual: OFF");
                            }

                            if(foco3.equals("1")){
                                ivLuz3.setBackgroundResource(R.drawable.cerclebackgroundpink);
                                ivLuz3.setImageResource(R.drawable.f1on);
                                tvLuz3Bajo.setText("Estado actual: ON");
                            }else{
                                ivLuz3.setBackgroundResource(R.drawable.cerclebackgroundpink);
                                ivLuz3.setImageResource(R.drawable.f2off);
                                tvLuz3Bajo.setText("Estado actual: OFF");
                            }

                            if(foco4.equals("1")){
                                ivLuz4.setBackgroundResource(R.drawable.cerclebackgroundpink);
                                ivLuz4.setImageResource(R.drawable.f1on);
                                tvLuz4Bajo.setText("Estado actual: ON");
                            }else{
                                ivLuz4.setBackgroundResource(R.drawable.cerclebackgroundpink);
                                ivLuz4.setImageResource(R.drawable.f2off);
                                tvLuz4Bajo.setText("Estado actual: OFF");
                            }

                            if(foco5.equals("1")){
                                ivLuz5.setBackgroundResource(R.drawable.cerclebackgroundpink);
                                ivLuz5.setImageResource(R.drawable.f1on);
                                tvLuz5Bajo.setText("Estado actual: ON");
                            }else{
                                ivLuz5.setBackgroundResource(R.drawable.cerclebackgroundpink);
                                ivLuz5.setImageResource(R.drawable.f2off);
                                tvLuz5Bajo.setText("Estado actual: OFF");
                            }

                            if(foco6.equals("1")){
                                ivLuz6.setBackgroundResource(R.drawable.cerclebackgroundpink);
                                ivLuz6.setImageResource(R.drawable.f1on);
                                tvLuz6Bajo.setText("Estado actual: ON");
                            }else{
                                ivLuz6.setBackgroundResource(R.drawable.cerclebackgroundpink);
                                ivLuz6.setImageResource(R.drawable.f2off);
                                tvLuz6Bajo.setText("Estado actual: OFF");
                            }

                            if(foco7.equals("1")){
                                ivLuz7.setBackgroundResource(R.drawable.cerclebackgroundpink);
                                ivLuz7.setImageResource(R.drawable.f1on);
                                tvLuz7Bajo.setText("Estado actual: ON");
                            }else{
                                ivLuz7.setBackgroundResource(R.drawable.cerclebackgroundpink);
                                ivLuz7.setImageResource(R.drawable.f2off);
                                tvLuz7Bajo.setText("Estado actual: OFF");
                            }

                            if(foco8.equals("1")){
                                ivLuz8.setBackgroundResource(R.drawable.cerclebackgroundpink);
                                ivLuz8.setImageResource(R.drawable.f1on);
                                tvLuz8Bajo.setText("Estado actual: ON");
                            }else{
                                ivLuz8.setBackgroundResource(R.drawable.cerclebackgroundpink);
                                ivLuz8.setImageResource(R.drawable.f2off);
                                tvLuz8Bajo.setText("Estado actual: OFF");
                            }

                            if(foco1.equals("1") && foco2.equals("1") && foco3.equals("1") && foco4.equals("1") &&
                               foco5.equals("1") && foco6.equals("1") && foco7.equals("1") && foco8.equals("1")){
                                ivLuz9.setBackgroundResource(R.drawable.cerclebackgroundpink);
                                ivLuz9.setImageResource(R.drawable.f1on);
                                tvLuz9Bajo.setText("Estado actual: ON");
                            }else{
                                ivLuz9.setBackgroundResource(R.drawable.cerclebackgroundpink);
                                ivLuz9.setImageResource(R.drawable.f2off);
                                tvLuz9Bajo.setText("Estado actual: OFF");
                            }

                            /*
                            Toast.makeText(luces_control.this,
                                    "foco1: "+foco1 +"\n" +
                                    "foco2: "+foco2 +"\n" +
                                    "foco3: "+foco3 +"\n" +
                                    "foco4: "+foco4 +"\n" +
                                    "foco5: "+foco5 +"\n" +
                                    "foco6: "+foco6 +"\n" +
                                    "foco7: "+foco7 +"\n" +
                                    "foco8: "+foco8, Toast.LENGTH_SHORT).show();
                            */
                            recDataString.delete(0, recDataString.length());
                            dataInPrint = " ";
                            primercaracter="";
                        }
                    }else if(primercaracter.equals("x")){
                        if (endOfLineIndex > 0) {                                           // make sure there data before ~
                            String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string
                            int dataLength = dataInPrint.length();                            //get length of data received
                            //Toast.makeText(luces_control.this, "Datos recibidos = " + dataInPrint + "\n"
                            //        + "Tamaño String= " + String.valueOf(dataLength), Toast.LENGTH_SHORT).show();

                            /*
                            Patrón de datos a decodificar: Información de todos los sensores.
                                              x:datoVariable# datoVariable$ datoVariable% datoVariable
                                               &datoVariable* datoVariable, datoVariable. datoVariable
                                               +temperatura!humedadrelativa~
                            */
                            tv_Data.setText("Receiver:" + dataInPrint);

                            //Cadena a tratar: #:valortemperatura,valorhumedad~
                            String str = recDataString.toString();
                            //Toast.makeText(this, ""+str, Toast.LENGTH_LONG).show();
                            for (int i = 0; i <= str.length() - 1; i++) {
                                char c = str.charAt(i);
                                //System.out.println(sCadena.charAt(5));
                                if (String.valueOf(c).equals("+")) {
                                    int k = i;
                                    //Toast.makeText(this, "Posición :"+k, Toast.LENGTH_SHORT).show();
                                    for (int j = (k + 1); j <= str.length() - 1; j++) {
                                        char x = str.charAt(j);
                                        if (String.valueOf(x).equals("!")) {
                                            break;
                                        }
                                        temperatura += String.valueOf(x);
                                    }
                                }
                            }

                            for (int n = 0; n <= str.length() - 1; n++) {
                                char c = str.charAt(n);
                                //System.out.println(sCadena.charAt(5));
                                if (String.valueOf(c).equals("!")) {
                                    int k = n;
                                    for (int j = (k + 1); j <= str.length() - 1; j++) {
                                        char x = str.charAt(j);
                                        if (String.valueOf(x).equals("~")) {
                                            break;
                                        }
                                        humedad += String.valueOf(x);
                                    }
                                }
                            }

                            //T. [°C] : RH. [%]
                            tvLuz10Bajo.setText(String.valueOf(temperatura)+"°C:"+String.valueOf(humedad)+"% RH");

                            String foco1 = recDataString.substring(2, 3);
                            String foco2 = recDataString.substring(4, 5);
                            String foco3 = recDataString.substring(6, 7);
                            String foco4 = recDataString.substring(8, 9);
                            String foco5 = recDataString.substring(10, 11);
                            String foco6 = recDataString.substring(12, 13);
                            String foco7 = recDataString.substring(14, 15);
                            String foco8 = recDataString.substring(16, 17);

                            if(foco1.equals("1")){
                                ivLuz1.setBackgroundResource(R.drawable.cerclebackgroundpink);
                                ivLuz1.setImageResource(R.drawable.f1on);
                                tvLuz1Bajo.setText("Estado actual: ON");
                            }else{
                                ivLuz1.setBackgroundResource(R.drawable.cerclebackgroundpink);
                                ivLuz1.setImageResource(R.drawable.f2off);
                                tvLuz1Bajo.setText("Estado actual: OFF");
                            }

                            if(foco2.equals("1")){
                                ivLuz2.setBackgroundResource(R.drawable.cerclebackgroundpink);
                                ivLuz2.setImageResource(R.drawable.f1on);
                                tvLuz2Bajo.setText("Estado actual: ON");
                            }else{
                                ivLuz2.setBackgroundResource(R.drawable.cerclebackgroundpink);
                                ivLuz2.setImageResource(R.drawable.f2off);
                                tvLuz2Bajo.setText("Estado actual: OFF");
                            }

                            if(foco3.equals("1")){
                                ivLuz3.setBackgroundResource(R.drawable.cerclebackgroundpink);
                                ivLuz3.setImageResource(R.drawable.f1on);
                                tvLuz3Bajo.setText("Estado actual: ON");
                            }else{
                                ivLuz3.setBackgroundResource(R.drawable.cerclebackgroundpink);
                                ivLuz3.setImageResource(R.drawable.f2off);
                                tvLuz3Bajo.setText("Estado actual: OFF");
                            }

                            if(foco4.equals("1")){
                                ivLuz4.setBackgroundResource(R.drawable.cerclebackgroundpink);
                                ivLuz4.setImageResource(R.drawable.f1on);
                                tvLuz4Bajo.setText("Estado actual: ON");
                            }else{
                                ivLuz4.setBackgroundResource(R.drawable.cerclebackgroundpink);
                                ivLuz4.setImageResource(R.drawable.f2off);
                                tvLuz4Bajo.setText("Estado actual: OFF");
                            }

                            if(foco5.equals("1")){
                                ivLuz5.setBackgroundResource(R.drawable.cerclebackgroundpink);
                                ivLuz5.setImageResource(R.drawable.f1on);
                                tvLuz5Bajo.setText("Estado actual: ON");
                            }else{
                                ivLuz5.setBackgroundResource(R.drawable.cerclebackgroundpink);
                                ivLuz5.setImageResource(R.drawable.f2off);
                                tvLuz5Bajo.setText("Estado actual: OFF");
                            }

                            if(foco6.equals("1")){
                                ivLuz6.setBackgroundResource(R.drawable.cerclebackgroundpink);
                                ivLuz6.setImageResource(R.drawable.f1on);
                                tvLuz6Bajo.setText("Estado actual: ON");
                            }else{
                                ivLuz6.setBackgroundResource(R.drawable.cerclebackgroundpink);
                                ivLuz6.setImageResource(R.drawable.f2off);
                                tvLuz6Bajo.setText("Estado actual: OFF");
                            }

                            if(foco7.equals("1")){
                                ivLuz7.setBackgroundResource(R.drawable.cerclebackgroundpink);
                                ivLuz7.setImageResource(R.drawable.f1on);
                                tvLuz7Bajo.setText("Estado actual: ON");
                            }else{
                                ivLuz7.setBackgroundResource(R.drawable.cerclebackgroundpink);
                                ivLuz7.setImageResource(R.drawable.f2off);
                                tvLuz7Bajo.setText("Estado actual: OFF");
                            }

                            if(foco8.equals("1")){
                                ivLuz8.setBackgroundResource(R.drawable.cerclebackgroundpink);
                                ivLuz8.setImageResource(R.drawable.f1on);
                                tvLuz8Bajo.setText("Estado actual: ON");
                            }else{
                                ivLuz8.setBackgroundResource(R.drawable.cerclebackgroundpink);
                                ivLuz8.setImageResource(R.drawable.f2off);
                                tvLuz8Bajo.setText("Estado actual: OFF");
                            }

                            if(foco1.equals("1") && foco2.equals("1") && foco3.equals("1") && foco4.equals("1") &&
                                    foco5.equals("1") && foco6.equals("1") && foco7.equals("1") && foco8.equals("1")){
                                ivLuz9.setBackgroundResource(R.drawable.cerclebackgroundpink);
                                ivLuz9.setImageResource(R.drawable.f1on);
                                tvLuz9Bajo.setText("Estado actual: ON");
                            }else{
                                ivLuz9.setBackgroundResource(R.drawable.cerclebackgroundpink);
                                ivLuz9.setImageResource(R.drawable.f2off);
                                tvLuz9Bajo.setText("Estado actual: OFF");
                            }

                            recDataString.delete(0, recDataString.length());
                            dataInPrint = " ";
                            temperatura = "";
                            humedad = "";
                            primercaracter = "";
                        }
                    }
                }
            }
        };
            //Fin del bluetoothIn


        //BA = BluetoothAdapter.getDefaultAdapter();
        btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter
        checkBTState();

        //FINALIZANDO....


        btnRetornar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Disconnect();
                Disconnect();
            }
        });

        ivLuz1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               controlLuz1();
            }
        });

        ivLuz2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controlLuz2();
            }
        });

        ivLuz3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controlLuz3();
            }
        });

        ivLuz4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controlLuz4();
            }
        });

        ivLuz5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controlLuz5();
            }
        });

        ivLuz6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controlLuz6();
            }
        });

        ivLuz7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controlLuz7();
            }
        });

        ivLuz8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controlLuz8();
            }
        });

        ivLuz9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnOnAll();
            }
        });

        ivLuz10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInfoTH();
            }
        });

        btnEstadoLuces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInfoStatusAllces();
            }
        });


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(),MainActivity.class));
                Disconnect();
                //luces_control.this.finish();
            }
        });

        progress = ProgressDialog.show(this, "Conectando...", "Por favor espere!!!");
    }

    public void dialog_BT_data(String temperatura,String humedad){
        TextView tv_title,txtclose,address,tvTemperatura,tvHumedad,tvFecha,tvHora;
        myDialog.setContentView(R.layout.intosms_temp_rh);
        myDialog.setTitle("SMS INTO");
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

    //OBtengo nombres y apellidos del usuario admin
    public String getNameBD(){
        int codigo=0;
        String name="";String apellidos="";
        SQLiteDatabase db = admin.getWritableDatabase();
        Cursor fila = db.rawQuery("select codigo,nombres,apellidos from usuarios where codigo=1", null);
        if (fila.moveToFirst()) {
            codigo = fila.getInt(0);
            name = fila.getString(1);
            apellidos = fila.getString(2);
        }
        return name +" "+apellidos;
    }

    void salir(){
        Intent intent = new Intent(luces_control.this, device_list.class);
        //Intent intent = new Intent(luces_control.this, MainActivity.class);
        intent.putExtra("senal", "2");
        //intent.putExtra("codigo", codigo);
        intent.putExtra("usuario", getNameBD());
        startActivity(intent);
        luces_control.this.finish();
    }














    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connecetion with BT device using UUID
    }


    @Override
    public void onResume() {
        super.onResume();
        //Get MAC address from DeviceListActivity via intent
        Intent intent = getIntent();
        //Get the MAC address from the DeviceListActivty via EXTRA
        address = intent.getStringExtra(device_list.EXTRA_ADDRESS);

        //create device and set the MAC address
        //Log.i("ramiro", "adress : " + address);
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

            try {
                btSocket = createBluetoothSocket(device);
            } catch (IOException e) {
                Toast.makeText(getBaseContext(), "La creacción del Socket fallo", Toast.LENGTH_LONG).show();
            }

            //Establish the Bluetooth socket connection.
            try {
                btSocket.connect();
                Thread.sleep(1000);
                if (btSocket.isConnected()) {
                    bandera = true;
                }else{
                    bandera=false;
                }
            } catch (IOException e) {
                try {
                    btSocket.close();
                } catch (IOException e2) {
                    //insert code to deal with this
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        if(bandera) {
                mConnectedThread = new ConnectedThread(btSocket);
                mConnectedThread.start();

            try {
                //btSocket.getOutputStream().write("cadena a enviar".toString().getBytes());
                //btSocket.getOutputStream().write("todosensoresinfoki".toString().getBytes());

                //ENCENDER LED EN CIRCUITO PARA SABER QUE LA COMUNICACIÓN ESTA ESTABLECIDA
                //CORRECTAMENTE.
                //Thread.sleep(1000);
                mConnectedThread.write("todosensoresinfoki");
            //}catch(IOException e) {
            }catch(Exception e) {
                //Toast.makeText(this, "Error!!!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

                Toast.makeText(this, "Conectado.", Toast.LENGTH_SHORT).show();

                //Esta línea hacia que se me detuviera la App si estava puesta abajo.
                //mConnectedThread.write("todosensoresinfoki");
                progress.dismiss();

            }else{
                Toast.makeText(this, "Conexión Fallida. \n" +
                        "1. Asegurese que su módulo BT receptor este encendido.\n" +
                        "2. Que el dispositivo seleccionado en la lista sea el correcto.", Toast.LENGTH_SHORT).show();

                progress.dismiss();
                salir();
                //finish();
            }
    }



    //Inicio del método ConnectedThread
    //create new class for connect thread
    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {   }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }


        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            // Keep looping to listen for received messages
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);        	//read bytes from input buffer
                    String readMessage = new String(buffer, 0, bytes);
                    // Send the obtained bytes to the UI Activity via handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }

        //write method
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
            } catch (IOException e) {
                //if you cannot write, close the application
                Toast.makeText(getBaseContext(), "La Conexión fallo", Toast.LENGTH_LONG).show();
                finish();
            }
        }


    }
    //Fin del método ConnectedThread




    @Override
    public void onPause(){
        super.onPause();
        try
        {
            //Don't leave Bluetooth sockets open when leaving activity
            btSocket.close();
        } catch (IOException e2) {
            //insert code to deal with this
        }
    }




    //Checks that the Android device Bluetooth is available and prompts to be turned on if off
    private void checkBTState() {
        if(btAdapter==null) {
            Toast.makeText(getBaseContext(), "El dispositivo no soporta bluetooth", Toast.LENGTH_LONG).show();
        }else{
            if (btAdapter.isEnabled()) {
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }

    }




















    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_luces_control2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button_green_round, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if(id==R.id.opAyuda){
            Toast.makeText(getApplicationContext(),"Ayuda",Toast.LENGTH_LONG).show();
            //AyudaBT();
            return true;
        }else if(id==R.id.opSalir){
            Toast.makeText(getApplicationContext(),"Salir",Toast.LENGTH_LONG).show();
            return true;
        }else */
        if(id==R.id.opExit){
            //getCall();
            Disconnect();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getInfoTH(){
        if (btSocket!=null) {
            String mensaje = Config.mensaje;
            dialogo = new AlertDialog.Builder(luces_control.this);
            dialogo.setIcon(R.drawable.ic_bluetooth);
            dialogo.setTitle("Bluetooth Monitoreo");
            dialogo.setMessage(mensaje);
            dialogo.setCancelable(false);
            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    try {
                        //infotemphumedad

                        btSocket.getOutputStream().write("infotemphumedad".toString().getBytes());
                        confirmado();
                    } catch (IOException e) {
                        //e.printStackTrace();
                        msg("Error. Posiblemente el bluetooth de su\n" +
                                "dispositivo no este activado.");
                    }
                }
            });
            dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    Toast.makeText(getApplicationContext(), "Operación Cancelada.", Toast.LENGTH_LONG).show();
                }
            });
            dialogo.show();
        }
    }

    private void getInfoStatusAllces(){
        if (btSocket!=null) {
            String mensaje = Config.mensaje;
            dialogo = new AlertDialog.Builder(luces_control.this);
            dialogo.setIcon(R.drawable.ic_bluetooth);
            dialogo.setTitle("Bluetooth Monitoreo");
            dialogo.setMessage(mensaje);
            dialogo.setCancelable(false);
            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    try {
                        btSocket.getOutputStream().write("infoluceslab".toString().getBytes());
                        confirmado();
                    } catch (IOException e) {
                        //e.printStackTrace();
                        msg("Error. Posiblemente el bluetooth de su\n" +
                                "dispositivo no este activado.");
                    }
                }
            });
            dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    Toast.makeText(getApplicationContext(), "Operación Cancelada.", Toast.LENGTH_LONG).show();
                }
            });
            dialogo.show();
        }
    }

    private void getCall(){
        if (btSocket!=null) {
            String mensaje = "Confirme si ¿Realmente desea recibir una llamada del sistema? \n" +
                    "Recuerde que el sistema le podra llamar si se cumple lo siguiente:\n" +
                    "\n * Estar debidamente alimentado y encendido." +
                    "\n * Exista señal de cobertura en este momento." +
                    "\n * Se posea saldo en el chip instalado en el sistema y su celular.\n";
            dialogo = new AlertDialog.Builder(luces_control.this);
            dialogo.setIcon(R.drawable.ic_call);
            //dialog.setTitle("COCESNA.");
            dialogo.setTitle("Solicitud de Call.");
            dialogo.setMessage(mensaje);
            dialogo.setCancelable(false);
            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    try {
                        btSocket.getOutputStream().write("Llamamesistema".toString().getBytes());
                        confirmado();
                    } catch (IOException e) {
                        //e.printStackTrace();
                        msg("Error. Posiblemente el bluetooth de su\n" +
                                "dispositivo no este activado.");
                    }

                }
            });
            dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    Toast.makeText(getApplicationContext(), "Operación Cancelada.", Toast.LENGTH_LONG).show();
                }
            });
            dialogo.show();
        }
    }

    private void confirmado(){
        Toast.makeText(getApplicationContext(),"Petición enviada.\nEspere respuesta...",Toast.LENGTH_LONG).show();
    }

    private void controlLuz8() {
        if (btSocket!=null)
        {
            /*try
            {
                //btSocket.getOutputStream().write("TO".toString().getBytes());
                btSocket.getOutputStream().write("focoochoenciende".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error. Posiblemente el bluetooth de su\n" +
                        "dispositivo no este activado.");
            }*/

            String mensaje = Config.mensaje;
            dialogo = new AlertDialog.Builder(luces_control.this);
            dialogo.setIcon(R.drawable.ic_bluetooth);
            //dialog.setTitle("COCESNA.");
            dialogo.setTitle("Bluetooth Control");
            dialogo.setMessage(mensaje);
            dialogo.setCancelable(false);
            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    try {
                        btSocket.getOutputStream().write("focoochoenciende".toString().getBytes());
                        confirmado();
                    } catch (IOException e) {
                        //e.printStackTrace();
                        msg("Error. Posiblemente el bluetooth de su\n" +
                                "dispositivo no este activado.");
                    }

                }
            });
            dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Operación Cancelada correctamente.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            });
            dialogo.show();
        }
    }

    private void controlLuz7() {
        if (btSocket!=null)
        {
            /*try
            {
                //btSocket.getOutputStream().write("TO".toString().getBytes());
                btSocket.getOutputStream().write("focosieteenciende".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error. Posiblemente el bluetooth de su\n" +
                        "dispositivo no este activado.");
            }*/

            String mensaje = Config.mensaje;
            dialogo = new AlertDialog.Builder(luces_control.this);
            dialogo.setIcon(R.drawable.ic_bluetooth);
            //dialog.setTitle("COCESNA.");
            dialogo.setTitle("Bluetooth Control");
            dialogo.setMessage(mensaje);
            dialogo.setCancelable(false);
            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    try {
                        btSocket.getOutputStream().write("focosieteenciende".toString().getBytes());
                        confirmado();
                    } catch (IOException e) {
                        //e.printStackTrace();
                        msg("Error. Posiblemente el bluetooth de su\n" +
                                "dispositivo no este activado.");
                    }

                }
            });
            dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Operación Cancelada correctamente.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            });
            dialogo.show();

        }
    }

    private void controlLuz6() {
        if (btSocket!=null)
        {
            /*try
            {
                //btSocket.getOutputStream().write("TO".toString().getBytes());
                btSocket.getOutputStream().write("focoseisenciende".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error. Posiblemente el bluetooth de su\n" +
                        "dispositivo no este activado.");
            }*/

            String mensaje = Config.mensaje;
            dialogo = new AlertDialog.Builder(luces_control.this);
            dialogo.setIcon(R.drawable.ic_bluetooth);
            //dialog.setTitle("COCESNA.");
            dialogo.setTitle("Bluetooth Control");
            dialogo.setMessage(mensaje);
            dialogo.setCancelable(false);
            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    try {
                        btSocket.getOutputStream().write("focoseisenciende".toString().getBytes());
                        confirmado();
                    } catch (IOException e) {
                        //e.printStackTrace();
                        msg("Error. Posiblemente el bluetooth de su\n" +
                                "dispositivo no este activado.");
                    }

                }
            });
            dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Operación Cancelada correctamente.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            });
            dialogo.show();

        }
    }

    private void controlLuz5() {
        if (btSocket!=null)
        {
            /*try
            {
                //btSocket.getOutputStream().write("TO".toString().getBytes());
                btSocket.getOutputStream().write("fococincoenciende".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error. Posiblemente el bluetooth de su\n" +
                        "dispositivo no este activado.");
            }*/

            String mensaje = Config.mensaje;
            dialogo = new AlertDialog.Builder(luces_control.this);
            dialogo.setIcon(R.drawable.ic_bluetooth);
            //dialog.setTitle("COCESNA.");
            dialogo.setTitle("Bluetooth Control");
            dialogo.setMessage(mensaje);
            dialogo.setCancelable(false);
            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    try {
                        btSocket.getOutputStream().write("fococincoenciende".toString().getBytes());
                        confirmado();
                    } catch (IOException e) {
                        //e.printStackTrace();
                        msg("Error. Posiblemente el bluetooth de su\n" +
                                "dispositivo no este activado.");
                    }

                }
            });
            dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Operación Cancelada correctamente.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            });
            dialogo.show();
        }
    }

    private void controlLuz4() {
        if (btSocket!=null)
        {
            /*try
            {
                //btSocket.getOutputStream().write("TO".toString().getBytes());
                btSocket.getOutputStream().write("fococuatroenciende".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error. Posiblemente el bluetooth de su\n" +
                        "dispositivo no este activado.");
            }*/

            String mensaje = Config.mensaje;
            dialogo = new AlertDialog.Builder(luces_control.this);
            dialogo.setIcon(R.drawable.ic_bluetooth);
            //dialog.setTitle("COCESNA.");
            dialogo.setTitle("Bluetooth Control");
            dialogo.setMessage(mensaje);
            dialogo.setCancelable(false);
            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    try {
                        btSocket.getOutputStream().write("fococuatroenciende".toString().getBytes());
                        confirmado();
                    } catch (IOException e) {
                        //e.printStackTrace();
                        msg("Error. Posiblemente el bluetooth de su\n" +
                                "dispositivo no este activado.");
                    }

                }
            });
            dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Operación Cancelada correctamente.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            });
            dialogo.show();
        }
    }

    private void controlLuz3() {
        if (btSocket!=null)
        {
            /*try
            {
                //btSocket.getOutputStream().write("TO".toString().getBytes());
                btSocket.getOutputStream().write("focotresenciende".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error. Posiblemente el bluetooth de su\n" +
                        "dispositivo no este activado.");
            }*/

            String mensaje = Config.mensaje;
            dialogo = new AlertDialog.Builder(luces_control.this);
            dialogo.setIcon(R.drawable.ic_bluetooth);
            //dialog.setTitle("COCESNA.");
            dialogo.setTitle("Bluetooth Control");
            dialogo.setMessage(mensaje);
            dialogo.setCancelable(false);
            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    try {
                        btSocket.getOutputStream().write("focotresenciende".toString().getBytes());
                        confirmado();
                    } catch (IOException e) {
                        //e.printStackTrace();
                        msg("Error. Posiblemente el bluetooth de su\n" +
                                "dispositivo no este activado.");
                    }

                }
            });
            dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Operación Cancelada correctamente.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            });
            dialogo.show();
        }
    }

    private void controlLuz2() {
        if (btSocket!=null)
        {
            /*try
            {
                //btSocket.getOutputStream().write("TO".toString().getBytes());
                btSocket.getOutputStream().write("focodosenciende".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error. Posiblemente el bluetooth de su\n" +
                        "dispositivo no este activado.");
            }*/

            String mensaje = Config.mensaje;
            dialogo = new AlertDialog.Builder(luces_control.this);
            dialogo.setIcon(R.drawable.ic_bluetooth);
            //dialog.setTitle("COCESNA.");
            dialogo.setTitle("Bluetooth Control");
            dialogo.setMessage(mensaje);
            dialogo.setCancelable(false);
            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    try {
                        btSocket.getOutputStream().write("focodosenciende".toString().getBytes());
                        confirmado();
                    } catch (IOException e) {
                        //e.printStackTrace();
                        msg("Error. Posiblemente el bluetooth de su\n" +
                                "dispositivo no este activado.");
                    }

                }
            });
            dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Operación Cancelada correctamente.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            });
            dialogo.show();
        }
    }

    private void controlLuz1() {
        if (btSocket!=null)
        {
            /*try
            {
                //btSocket.getOutputStream().write("TO".toString().getBytes());
                btSocket.getOutputStream().write("focounoenciende".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error. Posiblemente el bluetooth de su\n" +
                        "dispositivo no este activado.");
            }*/

            String mensaje = Config.mensaje;
            dialogo = new AlertDialog.Builder(luces_control.this);
            dialogo.setIcon(R.drawable.ic_bluetooth);
            //dialog.setTitle("COCESNA.");
            dialogo.setTitle("Bluetooth Control");
            dialogo.setMessage(mensaje);
            dialogo.setCancelable(false);
            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    try {
                        btSocket.getOutputStream().write("focounoenciende".toString().getBytes());
                        confirmado();
                    } catch (IOException e) {
                        //e.printStackTrace();
                        msg("Error. Posiblemente el bluetooth de su\n" +
                                "dispositivo no este activado.");
                    }

                }
            });
            dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Operación Cancelada correctamente.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            });
            dialogo.show();
        }
    }

    private void turnOnAll() {
        if (btSocket!=null)
        {
                String mensaje = Config.mensaje;
                dialogo = new AlertDialog.Builder(luces_control.this);
                dialogo.setIcon(R.drawable.ic_bluetooth);
                //dialog.setTitle("COCESNA.");
                dialogo.setTitle("Bluetooth Control");
                dialogo.setMessage(mensaje);
                dialogo.setCancelable(false);
                dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo, int id) {
                        try {
                            btSocket.getOutputStream().write("manuel".toString().getBytes());
                            confirmado();

                        } catch (IOException e) {
                            //e.printStackTrace();
                            msg("Error. Posiblemente el bluetooth de su\n" +
                                    "dispositivo no este activado.");
                        }

                    }
                });
                dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo, int id) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Operación Cancelada correctamente.", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                });
                dialogo.show();
            }
        }

    private void Disconnet1(){
        if (btSocket != null) {
            try
            {
                btSocket.close();
            }
            catch (IOException e)
            { msg("Error");}
        }
    }

    private void Disconnect() {
        if (btSocket != null) {
            /*try
            {
                btSocket.close();
            }
            catch (IOException e)
            { msg("Error");}
        }
        finish();*/
            String mensaje = Config.mensajeSalir;
            dialogo = new AlertDialog.Builder(luces_control.this);
            dialogo.setIcon(R.drawable.ic_bluetooth);
            //dialog.setTitle("COCESNA.");
            dialogo.setTitle("Advertencia");
            dialogo.setMessage(mensaje);
            dialogo.setCancelable(false);
            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    try {
                        btSocket.close();
                        salir();
                        //luces_control.this.finish();
                        //confirmado();
                    } catch (IOException e) {
                        //e.printStackTrace();
                        msg("Error. Posiblemente el bluetooth de su\n" +
                                "dispositivo no este activado.");
                    }
                    //luces_control.this.finish();
                    //finish();
                }
            });
            dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Operación Cancelada correctamente.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            });
            dialogo.show();
        }
    }

    public void msg_dialgo(String msg, int nfoco){
        dialogo = new AlertDialog.Builder(luces_control.this);
        dialogo.setIcon(R.drawable.ic_bluetooth);
        //dialog.setTitle("COCESNA.");
        dialogo.setTitle("Bluetooth.");
        dialogo.setMessage(msg);
        dialogo.setCancelable(false);
        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo, int id) {
                //aca pondre la funcion para envio de email automatico.
                //configuracion();
                //capturar_info_bd();

            }
        });
        dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo, int id) {
                Toast toast = Toast.makeText(getApplicationContext(), "Operación Cancelada correctamente.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
        dialogo.show();
    }     //fin de funcion.

    private void msg(String s){
        /*Toast toast = Toast.makeText(getApplicationContext(), "Campo respuesta es obligatorio", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();*/
        Toast.makeText(getBaseContext(),s,Toast.LENGTH_LONG).show();
    }

}
