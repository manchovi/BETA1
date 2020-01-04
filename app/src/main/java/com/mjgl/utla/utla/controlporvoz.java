package com.mjgl.utla.utla;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.speech.RecognizerIntent;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class controlporvoz extends AppCompatActivity {

    //private final String DEVICE_ADDRESS="20:16:12:12:02:04";

    /*
    *OJOTE: LA VENTAJA DE ESTA FUNCIONALIDAD ES QUE LE PUEDO ASIGNAR DIRECTAMENTE CON
    * QUE BLUETOOTH ME VOY A CONECTAR.
    */

    // MAC BLUETOOTH UTLA
    //private final String DEVICE_ADDRESS="20:16:11:28:26:40";
    private String DEVICE_ADDRESS="";

    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");//Serial Port Service ID
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private BluetoothAdapter bluetoothAdapter;
    private OutputStream outputStream;
    private InputStream inputStream;
    boolean deviceConnected=false;
    Thread thread;
    byte buffer[];
    int bufferPosition;
    boolean stopThread;
    boolean found=false;
    boolean connected=false;
    private ProgressDialog pd;
    ProgressDialog progressDialog;

    private StringBuilder recDataString = new StringBuilder();

    TextView grabar, grabar1, txtReceiver, tv_mac;
    AlertDialog.Builder dialog;
    AlertDialog.Builder dialogo;
    ImageView microfono;
    private static final int RECOGNIZE_SPEECH_ACTIVITY = 1;
    dbSQLiteHelper admin = new dbSQLiteHelper(this);
    Toolbar toolbar;

    String temperatura = "";
    String humedad = "";
    String command;
    int contador = 0;

    //Creando instancia para ver si funciona
    superClase superclase = new superClase();


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new android.app.AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Bluetooth Control Voz")
                    .setMessage("¿Esta seguro que desea salir?\n" +
                            "\nTenga en cuenta que se cerrara la conexión establecida con " +
                            "el módulo BlueTooth receptor si la hay.")
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {//un listener que al pulsar, cierre la aplicacion
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Salir
                            Disconnect();
                            controlporvoz.this.finish();
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
        setContentView(R.layout.activity_controlporvoz);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_left));
        toolbar.setTitleTextColor(getResources().getColor(R.color.mycolor_blanco));
        toolbar.setTitleMargin(0, 0, 0, 0);
        toolbar.setSubtitle("Control de luces por voz");
        getSupportActionBar().setTitle("UTLA");

        //Función para evitar la rotación de la pantalla del CELULAR.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //y esto para pantalla completa (oculta incluso la barra de estado)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        grabar = (TextView) findViewById(R.id.txtGrabarVoz);
        grabar1 = (TextView) findViewById(R.id.txtGrabarVoz1);
        txtReceiver = (TextView) findViewById(R.id.txtReceiver);
        tv_mac = (TextView) findViewById(R.id.tv_mac);

        microfono = (ImageView) findViewById(R.id.img_btn_hablar);

        pd = new ProgressDialog(controlporvoz.this);
        //pd.setMessage("Buscando Receptor BT, por favor espere...");
        progressDialog = new ProgressDialog(controlporvoz.this);

        //Toast.makeText(this, ""+getMacBT().toString().trim(), Toast.LENGTH_SHORT).show();
        DEVICE_ADDRESS=getMacBT().toString().trim();
        tv_mac.setText("Address MAC:"+DEVICE_ADDRESS);

        microfono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(controlporvoz.this, "Hola", Toast.LENGTH_SHORT).show();
                onClickImgBtnHablar();
            }
        });


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*startActivity(new Intent(getApplicationContext(),MainActivity.class));
                controlporvoz.this.finish();*/
                Disconnect1();
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RECOGNIZE_SPEECH_ACTIVITY:
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> speech = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String strSpeech2Text = speech.get(0);
                    grabar1.setText("Has dicho:" + strSpeech2Text);
                    grabar.setText(strSpeech2Text);

                    if (grabar.getText().equals("Bluetooth") || grabar.getText().equals("BlueTooth") || grabar.getText().equals("bluetooth")) {
                        /*
                        if (BTinit()) {
                            BTconnect();
                        }
                        */
                        startConex();
                    }

                    if (grabar.getText().equals("Apagar bluetooth") || grabar.getText().equals("apagar bluetooth") || grabar.getText().equals("Apagar Bluetooth")) {
                        DisabledBT();
                    }


                    if (grabar.getText().equals("foco 1") || grabar.getText().equals("foco uno") || grabar.getText().equals("Foco 1")) {
                        //Toast.makeText(this, "Has dicho: " + grabar.getText().toString(), Toast.LENGTH_SHORT).show();
                        /*
                        command = "1";
                        try{
                            //socket.getOutputStream().write("infotemphumedad".toString().getBytes());
                            outputStream.write(command.getBytes()); //transmits the value of command to the bluetooth module
                            Toast.makeText(getApplicationContext(),"dato: "+command +"\n" +
                                    "\nExcelente...",Toast.LENGTH_LONG).show();

                        }catch (IOException e){
                            e.printStackTrace();
                        }
                        */
                        if (socket != null) {
                            try {
                                socket.getOutputStream().write("focounoenciende".toString().getBytes());
                                //outputStream.write("focounoenciende".toString().getBytes()); //transmits the value of command to the bluetooth module
                                msg("Orden ejecutada correctamente. \n\nStatus cambiado. :-)");
                                txtReceiver.setText(null);
                                //textView.append("\nSent Data:" + string + "\n");
                            } catch (IOException e) {
                                //e.printStackTrace();
                                /*msg("Error. Posiblemente el bluetooth de su\n" +
                                        "dispositivo no este activado.");*/
                                msg("Error. Posiblemente se deba a:\n" +
                                        "No se ha establecido conexión con el dispositivo BlueTooth Receptor.\n" +
                                        "\nSolución: \n" +
                                        "1. Clic en el microfono y diga el comando <<Bluetooth>>.\n" +
                                        "2. Clic en los 3 puntitos de la parte superior derecha y haga clic en la opción Encender/Enlazar BT.");
                                socket = null;
                            }

                        } else {
                            msg("Error. El comando deseado no se puede ejecutar.\n" +
                                    "\nNo ha establecido conexión con el módulo Bluetooth Receptor.");
                        }
                    }

                    if (grabar.getText().equals("foco 2") || grabar.getText().equals("foco dos") || grabar.getText().equals("Foco 2")) {
                        //Toast.makeText(this, "Has dicho: " + grabar.getText().toString(), Toast.LENGTH_SHORT).show();
                        if (socket != null) {
                            try {
                                socket.getOutputStream().write("focodosenciende".toString().getBytes());
                                //outputStream.write("focodosenciende".toString().getBytes()); //transmits the value of command to the bluetooth module
                                msg("Orden ejecutada correctamente. \n\nStatus cambiado. :-)");
                                txtReceiver.setText(null);
                                //textView.append("\nSent Data:" + string + "\n");
                            } catch (IOException e) {
                                //e.printStackTrace();
                            /*msg("Error. Posiblemente el bluetooth de su\n" +
                                    "dispositivo no este activado o no ha establecido conexión con el dispositivo BlueTooth Receptor.");*/
                                msg("Error. Posiblemente se deba a:\n" +
                                        "No se ha establecido conexión con el dispositivo BlueTooth Receptor.\n" +
                                        "\nSolución: \n" +
                                        "1. Clic en el microfono y diga el comando <<Bluetooth>>.\n" +
                                        "2. Clic en los 3 puntitos de la parte superior derecha y haga clic en la opción Encender/Enlazar BT.");

                                socket = null;
                            }

                        } else {
                            msg("Error. El comando deseado no se puede ejecutar.\n" +
                                    "\nNo ha establecido conexión con el módulo Bluetooth Receptor.");
                        }

                    }

                    if (grabar.getText().equals("foco 3") || grabar.getText().equals("foco tres") || grabar.getText().equals("Foco 3")) {
                        //Toast.makeText(this, "Has dicho: " + grabar.getText().toString(), Toast.LENGTH_SHORT).show();
                        if (socket != null) {
                            try {
                                socket.getOutputStream().write("focotresenciende".toString().getBytes());
                                //outputStream.write("focotresenciende".toString().getBytes()); //transmits the value of command to the bluetooth module
                                msg("Orden ejecutada correctamente. \n\nStatus cambiado. :-)");
                                txtReceiver.setText(null);
                                //textView.append("\nSent Data:" + string + "\n");
                            } catch (IOException e) {
                                //e.printStackTrace();
                                msg("Error. Posiblemente se deba a:\n" +
                                        "No se ha establecido conexión con el dispositivo BlueTooth Receptor.\n" +
                                        "\nSolución: \n" +
                                        "1. Clic en el microfono y diga el comando <<Bluetooth>>.\n" +
                                        "2. Clic en los 3 puntitos de la parte superior derecha y haga clic en la opción Encender/Enlazar BT.");
                                socket = null;
                            }

                        } else {
                            msg("Error. El comando deseado no se puede ejecutar.\n" +
                                    "\nNo ha establecido conexión con el módulo Bluetooth Receptor.");
                        }

                    }


                    if (grabar.getText().equals("foco 4") || grabar.getText().equals("foco cuatro") || grabar.getText().equals("Foco 4")) {
                        //Toast.makeText(this, "Has dicho: " + grabar.getText().toString(), Toast.LENGTH_SHORT).show();
                        if (socket != null) {
                            try {
                                socket.getOutputStream().write("fococuatroenciende".toString().getBytes());
                                //outputStream.write("fococuatroenciende".toString().getBytes()); //transmits the value of command to the bluetooth module
                                msg("Orden ejecutada correctamente. \n\nStatus cambiado. :-)");
                                txtReceiver.setText(null);
                                //textView.append("\nSent Data:" + string + "\n");
                            } catch (IOException e) {
                                //e.printStackTrace();
                                msg("Error. Posiblemente se deba a:\n" +
                                        "No se ha establecido conexión con el dispositivo BlueTooth Receptor.\n" +
                                        "\nSolución: \n" +
                                        "1. Clic en el microfono y diga el comando <<Bluetooth>>.\n" +
                                        "2. Clic en los 3 puntitos de la parte superior derecha y haga clic en la opción Encender/Enlazar BT.");
                                socket = null;
                            }

                        } else {
                            msg("Error. El comando deseado no se puede ejecutar.\n" +
                                    "\nNo ha establecido conexión con el módulo Bluetooth Receptor.");
                        }

                    }


                    if (grabar.getText().equals("foco 5") || grabar.getText().equals("foco cinco") || grabar.getText().equals("Foco 5")) {
                        //Toast.makeText(this, "Has dicho: " + grabar.getText().toString(), Toast.LENGTH_SHORT).show();
                        if (socket != null) {
                            try {
                                socket.getOutputStream().write("fococincoenciende".toString().getBytes());
                                //outputStream.write("fococincoenciende".toString().getBytes()); //transmits the value of command to the bluetooth module
                                msg("Orden ejecutada correctamente. \n\nStatus cambiado. :-)");
                                txtReceiver.setText(null);
                                //textView.append("\nSent Data:" + string + "\n");
                            } catch (IOException e) {
                                //e.printStackTrace();
                                msg("Error. Posiblemente se deba a:\n" +
                                        "No se ha establecido conexión con el dispositivo BlueTooth Receptor.\n" +
                                        "\nSolución: \n" +
                                        "1. Clic en el microfono y diga el comando <<Bluetooth>>.\n" +
                                        "2. Clic en los 3 puntitos de la parte superior derecha y haga clic en la opción Encender/Enlazar BT.");
                                socket = null;
                            }

                        } else {
                            msg("Error. El comando deseado no se puede ejecutar.\n" +
                                    "\nNo ha establecido conexión con el módulo Bluetooth Receptor.");
                        }

                    }


                    if (grabar.getText().equals("foco 6") || grabar.getText().equals("foco seis") || grabar.getText().equals("Foco 6")) {
                        //Toast.makeText(this, "Has dicho: " + grabar.getText().toString(), Toast.LENGTH_SHORT).show();
                        if (socket != null) {
                            try {
                                socket.getOutputStream().write("focoseisenciende".toString().getBytes());
                                //outputStream.write("focoseisenciende".toString().getBytes()); //transmits the value of command to the bluetooth module
                                msg("Orden ejecutada correctamente. \n\nStatus cambiado. :-)");
                                txtReceiver.setText(null);
                                //textView.append("\nSent Data:" + string + "\n");
                            } catch (IOException e) {
                                //e.printStackTrace();
                                msg("Error. Posiblemente se deba a:\n" +
                                        "No se ha establecido conexión con el dispositivo BlueTooth Receptor.\n" +
                                        "\nSolución: \n" +
                                        "1. Clic en el microfono y diga el comando <<Bluetooth>>.\n" +
                                        "2. Clic en los 3 puntitos de la parte superior derecha y haga clic en la opción Encender/Enlazar BT.");
                                socket = null;
                            }

                        } else {
                            msg("Error. El comando deseado no se puede ejecutar.\n" +
                                    "\nNo ha establecido conexión con el módulo Bluetooth Receptor.");
                        }

                    }


                    if (grabar.getText().equals("foco 7") || grabar.getText().equals("foco siete") || grabar.getText().equals("Foco 7")) {
                        //Toast.makeText(this, "Has dicho: " + grabar.getText().toString(), Toast.LENGTH_SHORT).show();
                        if (socket != null) {
                            try {
                                socket.getOutputStream().write("focosieteenciende".toString().getBytes());
                                //outputStream.write("focosieteenciende".toString().getBytes()); //transmits the value of command to the bluetooth module
                                msg("Orden ejecutada correctamente. \n\nStatus cambiado. :-)");
                                txtReceiver.setText(null);
                                //textView.append("\nSent Data:" + string + "\n");
                            } catch (IOException e) {
                                //e.printStackTrace();
                                msg("Error. Posiblemente se deba a:\n" +
                                        "No se ha establecido conexión con el dispositivo BlueTooth Receptor.\n" +
                                        "\nSolución: \n" +
                                        "1. Clic en el microfono y diga el comando <<Bluetooth>>.\n" +
                                        "2. Clic en los 3 puntitos de la parte superior derecha y haga clic en la opción Encender/Enlazar BT.");
                                socket = null;
                            }

                        } else {
                            msg("Error. El comando deseado no se puede ejecutar.\n" +
                                    "\nNo ha establecido conexión con el módulo Bluetooth Receptor.");
                        }

                    }


                    if (grabar.getText().equals("foco 8") || grabar.getText().equals("foco ocho") || grabar.getText().equals("Foco 8")) {
                        //Toast.makeText(this, "Has dicho: " + grabar.getText().toString(), Toast.LENGTH_SHORT).show();
                        if (socket != null) {
                            try {
                                socket.getOutputStream().write("focoochoenciende".toString().getBytes());
                                //outputStream.write("focoochoenciende".toString().getBytes()); //transmits the value of command to the bluetooth module
                                msg("Orden ejecutada correctamente. \n\nStatus cambiado. :-)");
                                txtReceiver.setText(null);
                                //textView.append("\nSent Data:" + string + "\n");
                            } catch (IOException e) {
                                //e.printStackTrace();
                                msg("Error. Posiblemente se deba a:\n" +
                                        "No se ha establecido conexión con el dispositivo BlueTooth Receptor.\n" +
                                        "\nSolución: \n" +
                                        "1. Clic en el microfono y diga el comando <<Bluetooth>>.\n" +
                                        "2. Clic en los 3 puntitos de la parte superior derecha y haga clic en la opción Encender/Enlazar BT.");
                                socket = null;
                            }

                        } else {
                            msg("Error. El comando deseado no se puede ejecutar.\n" +
                                    "\nNo ha establecido conexión con el módulo Bluetooth Receptor.");
                        }

                    }

                    if (grabar.getText().equals("manuel") || grabar.getText().equals("Manuel") || grabar.getText().equals("MANUEL")
                            || grabar.getText().equals("UTLA") || grabar.getText().equals("utla") || grabar.getText().equals("udla")
                            || grabar.getText().equals("Luces") || grabar.getText().equals("luces")
                            || grabar.getText().equals("tesis")) {
                        //Toast.makeText(this, "Has dicho: " + grabar.getText().toString(), Toast.LENGTH_SHORT).show();

                        if (socket != null) {
                            try {
                                socket.getOutputStream().write("manuel".toString().getBytes());
                                //outputStream.write("manuel".toString().getBytes()); //transmits the value of command to the bluetooth module
                                //socket.getOutputStream().write("manuel".toString().getBytes());
                                msg("Orden ejecutada correctamente. \n\nStatus todas luminarias cambiado. :-)");
                                txtReceiver.setText(null);
                                //textView.append("\nSent Data:" + string + "\n");
                            } catch (IOException e) {
                                //e.printStackTrace();
                                msg("Error. Posiblemente se deba a:\n" +
                                        "No se ha establecido conexión con el dispositivo BlueTooth Receptor.\n" +
                                        "\nSolución: \n" +
                                        "1. Clic en el microfono y diga el comando <<Bluetooth>>.\n" +
                                        "2. Clic en los 3 puntitos de la parte superior derecha y haga clic en la opción Encender/Enlazar BT.");
                                socket = null;
                            }

                        } else {
                            msg("Error. El comando deseado no se puede ejecutar.\n" +
                                    "\nNo ha establecido conexión con el módulo Bluetooth Receptor.");
                        }

                    }


                    if (grabar.getText().equals("estado luces") || grabar.getText().equals("Estado luces") || grabar.getText().equals("Estado Luces")) {
                        //Toast.makeText(this, "Has dicho: " + grabar.getText().toString(), Toast.LENGTH_SHORT).show();
                        if (socket != null) {
                            try {
                                socket.getOutputStream().write("infoluceslab".toString().getBytes());
                                //outputStream.write("infoluceslab".toString().getBytes()); //transmits the value of command to the bluetooth module
                                msg("Orden ejecutada correctamente. \n\nEspere respuesta via SMS en su dispositivo móvil.");
                                txtReceiver.setText(null);
                                //textView.append("\nSent Data:" + string + "\n");
                            } catch (IOException e) {
                                //e.printStackTrace();
                                msg("Error. Posiblemente se deba a:\n" +
                                        "No se ha establecido conexión con el dispositivo BlueTooth Receptor.\n" +
                                        "\nSolución: \n" +
                                        "1. Clic en el microfono y diga el comando <<Bluetooth>>.\n" +
                                        "2. Clic en los 3 puntitos de la parte superior derecha y haga clic en la opción Encender/Enlazar BT.");
                                socket = null;
                            }

                        } else {
                            msg("Error. El comando deseado no se puede ejecutar.\n" +
                                    "\nNo ha establecido conexión con el módulo Bluetooth Receptor.");
                        }

                    }

                    if (grabar.getText().equals("Llámame") || grabar.getText().equals("llámame") || grabar.getText().equals("llamame")
                            || grabar.getText().equals("Llámame tesis") || grabar.getText().equals("llámame tesis") || grabar.getText().equals("llamame tesis")
                            || grabar.getText().equals("call me") || grabar.getText().equals("Call me")) {
                        //Toast.makeText(this, "Has dicho: " + grabar.getText().toString(), Toast.LENGTH_SHORT).show();
                        if (socket != null) {
                            try {
                                socket.getOutputStream().write("Llamamesistema".toString().getBytes());
                                //outputStream.write("Llamamesistema".toString().getBytes()); //transmits the value of command to the bluetooth module
                                msg("Orden ejecutada correctamente. \n\nEspere respuesta via llamada entrante en su dispositivo móvil.");
                                txtReceiver.setText(null);
                                //textView.append("\nSent Data:" + string + "\n");
                            } catch (IOException e) {
                                //e.printStackTrace();
                                msg("Error. Posiblemente se deba a:\n" +
                                        "No se ha establecido conexión con el dispositivo BlueTooth Receptor.\n" +
                                        "\nSolución: \n" +
                                        "1. Clic en el microfono y diga el comando <<Bluetooth>>.\n" +
                                        "2. Clic en los 3 puntitos de la parte superior derecha y haga clic en la opción Encender/Enlazar BT.");
                                socket = null;
                            }

                        } else {
                            msg("Error. El comando deseado no se puede ejecutar.\n" +
                                    "\nNo ha establecido conexión con el módulo Bluetooth Receptor.");
                        }

                    }

                    if (grabar.getText().equals("ambiente") || grabar.getText().equals("Ambiente")
                            || grabar.getText().equals("Temperatura") || grabar.getText().equals("temperatura")
                            || grabar.getText().equals("Humedad") || grabar.getText().equals("humedad")) {
                        //Toast.makeText(this, "Has dicho: " + grabar.getText().toString(), Toast.LENGTH_SHORT).show();
                        if (socket != null) {
                            try {
                                socket.getOutputStream().write("infotemphumedad".toString().getBytes());
                                //outputStream.write("infotemphumedad".toString().getBytes()); //transmits the value of command to the bluetooth module
                                msg("Orden ejecutada correctamente. \n\nEspere la respuesta a su petición.");
                                txtReceiver.setText(null);
                                //textView.append("\nSent Data:" + string + "\n");
                            } catch (IOException e) {
                                //e.printStackTrace();
                                msg("Error. Posiblemente se deba a:\n" +
                                        "No se ha establecido conexión con el dispositivo BlueTooth Receptor.\n" +
                                        "\nSolución: \n" +
                                        "1. Clic en el microfono y diga el comando <<Bluetooth>>.\n" +
                                        "2. Clic en los 3 puntitos de la parte superior derecha y haga clic en la opción Encender/Enlazar BT.");
                                socket = null;
                            }

                        } else {
                            msg("Error. El comando deseado no se puede ejecutar.\n" +
                                    "\nNo ha establecido conexión con el módulo Bluetooth Receptor.");
                        }

                    }



                    if (grabar.getText().equals("Sensores") || grabar.getText().equals("sensores")
                            || grabar.getText().equals("Sensor") || grabar.getText().equals("sensor")
                            || grabar.getText().equals("Info") || grabar.getText().equals("info")) {
                        //Toast.makeText(this, "Has dicho: " + grabar.getText().toString(), Toast.LENGTH_SHORT).show();
                        if (socket != null) {
                            try {
                                socket.getOutputStream().write("todosensoresinfoki".toString().getBytes());
                                //outputStream.write("infotemphumedad".toString().getBytes()); //transmits the value of command to the bluetooth module
                                msg("Orden ejecutada correctamente. \n\nEspere la respuesta a su petición.");
                                txtReceiver.setText(null);
                                //textView.append("\nSent Data:" + string + "\n");
                            } catch (IOException e) {
                                //e.printStackTrace();
                                msg("Error. Posiblemente se deba a:\n" +
                                        "No se ha establecido conexión con el dispositivo BlueTooth Receptor.\n" +
                                        "\nSolución: \n" +
                                        "1. Clic en el microfono y diga el comando <<Bluetooth>>.\n" +
                                        "2. Clic en los 3 puntitos de la parte superior derecha y haga clic en la opción Encender/Enlazar BT.");
                                socket = null;
                            }

                        } else {
                            msg("Error. El comando deseado no se puede ejecutar.\n" +
                                    "\nNo ha establecido conexión con el módulo Bluetooth Receptor.");
                        }

                    }





                    if (grabar.getText().equals("ayuda") || grabar.getText().equals("Ayuda")) {
                        //Toast.makeText(this, "Hola compañeros soy Prof. Gámez y estoy exponiendoles el tema del reconocimiento de voz en android studio.", Toast.LENGTH_LONG).show();
                        help();
                    }

                    if (grabar.getText().equals("retornar menú")) {
                        Disconnect();
                        Intent intent = new Intent(controlporvoz.this, MainActivity.class);
                        startActivity(intent);
                        controlporvoz.this.finish();
                    }
                }

                break;

            default:
                break;
        }
    }


    public void help() {
        superclase.ayuda_comandos(controlporvoz.this);
        /*
        dialog = new
                AlertDialog.Builder(controlporvoz.this);
        dialog.setIcon(R.drawable.ic_mic);
        dialog.setTitle("COMANDOS VOZ"); //* Comandos de voz para control.
        dialog.setMessage("" +
                "foco uno o foco 1: " +
                "\t->ON-OFF Luminaria uno.\n" +
                "\nfoco dos o foco 2: " +
                "\t->ON-OFF Luminaria dos.\n" +
                "\nfoco tres o foco 3: " +
                "\t->ON-OFF Luminaria tres." +
                "\n." +
                "\n." +
                "\n." +
                "\nfoco ocho o foco 8: " +
                "\t->ON-OFF Luminaria ocho.\n" +
                "\nLuces o luces: " +
                "\t->ON-OFF Todas las Luminarias.\n" +
                "\nretornar menú ~ IR A MENÚ PRINCIPAL.");
        dialog.setCancelable(false);
        dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo, int id) {
                grabar.setText(null);
            }
        });
        dialog.show();
        */
    }


    //OBtengo nombres y apellidos del usuario admin
    public String getNameBD() {
        int codigo = 0;
        String name = "";
        String apellidos = "";
        SQLiteDatabase db = admin.getWritableDatabase();
        Cursor fila = db.rawQuery("select codigo,nombres,apellidos from usuarios where codigo=1", null);
        if (fila.moveToFirst()) {
            codigo = fila.getInt(0);
            name = fila.getString(1);
            apellidos = fila.getString(2);
        }
        return name + " " + apellidos;
    }


    //public void onClickImgBtnHablar(View v) {


    public void onClickImgBtnHablar() {
        Intent intentActionRecognizeSpeech = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        // Configura el Lenguaje (Español-México)
        intentActionRecognizeSpeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "es-MX");
        //intentActionRecognizeSpeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        //intentActionRecognizeSpeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intentActionRecognizeSpeech.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi: " + getNameBD() + "\n Di un comando.");

        try {
            startActivityForResult(intentActionRecognizeSpeech, RECOGNIZE_SPEECH_ACTIVITY);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), "Tú dispositivo no soporta el reconocimiento por voz", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_controporvoz, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_ayuda) {
            //Toast.makeText(getApplicationContext(),"Buscando la ayuda solictada.\n" +
            //        "Espere por favor...",Toast.LENGTH_LONG).show();
            help();
            return true;
        } else if (id == R.id.action_salir) {
            //Intent intent=new Intent(controlporvoz.this, MainActivity.class);
            //startActivity(intent);
            //controlporvoz.this.finish();
            Disconnect1();
            return true;

        } else if (id == R.id.action_activarbt) {
            /*
            if (getMacBT().isEmpty()) {
                Toast.makeText(getApplicationContext(), "No ha configurado la dirección MAC.", Toast.LENGTH_SHORT).show();
            } else {
                //BTinit();
            }
            */
            startConex();
            return true;

        } else if (id == R.id.action_deshabilitarbt) {
            DisabledBT();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void startConex(){
        if(BTinit()) {
            pd.setMessage("Buscando Bluetoth Receptor Por favor espere...");
            pd.setCancelable(false);
            pd.show();
            //progressDialog.hide();
            //pd.hide();
            hilo();
        }else{
            //Toast.makeText(this, "Habilitando...", Toast.LENGTH_SHORT).show();
            //BTinit();
        }
    }


    //De aqui en adelante las funciones del bluetooth.
    public boolean BTinit()
    {
        //boolean found=false;
        found=false;
        //BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(),"Device doesnt Support Bluetooth",Toast.LENGTH_SHORT).show();
        }
        if(!bluetoothAdapter.isEnabled())
        {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter, 0);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        if(bondedDevices.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Please Pair the Device first",Toast.LENGTH_SHORT).show();
            //setUiEnabled(false);
        }
        else
        {
            for (BluetoothDevice iterator : bondedDevices)
            {
                //if(iterator.getAddress().equals(getMacBT().toString().trim()))
                if(iterator.getAddress().equals(DEVICE_ADDRESS))
                {
                    device=iterator;
                    found=true;
                    break;
                }
            }
        }
        return found;
    }



    public boolean BTconnect()
    {
        //pd.show();
        //boolean connected=true;
        connected=true;
        try {
            socket = device.createRfcommSocketToServiceRecord(PORT_UUID);
            socket.connect();
        } catch (IOException e) {
            e.printStackTrace();
            connected=false;
        }
        if(connected)
        {
            try {
                outputStream=socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inputStream=socket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return connected;
    }


    void hilo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //Aca se colocan las acciones que se ejecutarán en segundo plano.
                //for (int i=1;i<=5;i++){
                demora();
                //}
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //pd.hide();
                        try {
                            //if (BTinit() && (bluetoothAdapter.isEnabled())) {
                            if (BTinit()) {
                                if (BTconnect()) {
                                    deviceConnected = true;
                                    beginListenForData();
                                    pd.hide();
                                    outputStream.write("1".getBytes());
                                    Toast.makeText(controlporvoz.this, "Conexión Correcta.", Toast.LENGTH_SHORT).show();
                                }else{
                                    //progressDialog.hide();
                                    pd.hide();
                                    Toast.makeText(controlporvoz.this, "Error. La conexión fallo.\n" +
                                            "No se encontró el dispositivo Bluetooth receptor Listo.\n" +
                                            "Verifique por favor y vuelva a intentarlo.", Toast.LENGTH_LONG).show();
                                }
                            }

                        }catch (Exception e) {

                        }
                    }
                });
            }
        }).start();
    }


    private void demora(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    void beginListenForData()
    {
        final Handler handler = new Handler();
        stopThread = false;
        buffer = new byte[1024];
        Thread thread  = new Thread(new Runnable()
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopThread)
                {
                    try
                    {
                        int byteCount = inputStream.available();
                        if(byteCount > 0)
                        {
                            byte[] rawBytes = new byte[byteCount];
                            inputStream.read(rawBytes);
                            final String string=new String(rawBytes,"UTF-8");
                            handler.post(new Runnable() {
                                public void run()
                                {
                                    //Toast.makeText(controlporvoz.this, "Data: "+string, Toast.LENGTH_SHORT).show();

                                    //txtReceiver.setText(null);
                                    txtReceiver.append(string);
                                    //textView.append("\nSent Data:" + string + "\n");
                                    //Toast.makeText(controlporvoz.this, "Cadena:"+string, Toast.LENGTH_LONG).show();

                                    /*
                                    Aqui voy adicionar el llamado a las funciones para lanzar dialog o modal de respuesta
                                     INICIO
                                     */
                                    recDataString.append(string);
                                    int endOfLineIndex = recDataString.indexOf("~");                    // determine the end-of-line
                                    String primercaracter = recDataString.substring(0, 1);

                                    if (primercaracter.equals("#")) {

                                        if (endOfLineIndex > 0) {                                               // make sure there data before ~
                                            String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string
                                            int dataLength = dataInPrint.length();                              //get length of data received
                                           /* Toast.makeText(luces_control.this, "Datos recibidos = " + dataInPrint + "\n"
                                                    + "Tamaño String= " + String.valueOf(dataLength), Toast.LENGTH_SHORT).show();*/
                                            if (recDataString.charAt(0) == '#') {
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
                                            }

                                            //tvLuz10Bajo.setText(String.valueOf(temperatura)+"°C:"+String.valueOf(humedad)+"% RH");
                                            //dialog_BT_data(temperatura, humedad);

                                            //Dialog llamado para pruebas.
                                            //superclase.dialog_Saludo(controlporvoz.this, "Vaya que funciono....");
                                            superclase.dialog_temp_RH_voz(controlporvoz.this, temperatura, humedad);

                                            recDataString.delete(0, recDataString.length());                    //clear all string data

                                            dataInPrint = " ";
                                            temperatura = "";
                                            humedad = "";
                                            primercaracter = "";
                                        }
                                    } else if (primercaracter.equals("@")) {
                                        if (endOfLineIndex > 0) {                                               // make sure there data before ~
                                            String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string
                                            int dataLength = dataInPrint.length();                              //get length of data received

                                            /*
                                            Toast.makeText(controlporvoz.this, "Receiver: " + dataInPrint + "\n"
                                                    + "Tamaño String= " + String.valueOf(dataLength), Toast.LENGTH_SHORT).show();
                                           */

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

                                            superclase.dialog_luces_voz(controlporvoz.this, foco1, foco2, foco3, foco4, foco5, foco6, foco7, foco8);

                                            recDataString.delete(0, recDataString.length());
                                            dataInPrint = "";
                                            primercaracter = "";
                                            foco1="0";foco2="0";foco3="0";foco4="0";
                                            foco5="0";foco6="0";foco7="0";foco8="0";
                                        }
                                    }else if (primercaracter.equals("x")) {
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

                                            String foco1 = recDataString.substring(2, 3);
                                            String foco2 = recDataString.substring(4, 5);
                                            String foco3 = recDataString.substring(6, 7);
                                            String foco4 = recDataString.substring(8, 9);
                                            String foco5 = recDataString.substring(10, 11);
                                            String foco6 = recDataString.substring(12, 13);
                                            String foco7 = recDataString.substring(14, 15);
                                            String foco8 = recDataString.substring(16, 17);

                                            superclase.dialog_info_all_sensors_voz(controlporvoz.this, foco1, foco2, foco3, foco4, foco5, foco6, foco7, foco8, temperatura, humedad);

                                            recDataString.delete(0, recDataString.length());
                                            dataInPrint = " ";
                                            temperatura = "";
                                            humedad = "";
                                            primercaracter = "";
                                        }
                                    }


                                    //Fin de la decodificación de la cadena de respuesta del controlador.
                                }
                            });
                        }
                    }
                    catch (IOException ex)
                    {
                        stopThread = true;
                    }
                }
            }
        });

        thread.start();
    }

    //Inicio función DisabledBT();
    public void DisabledBT() {
        try {
            if (bluetoothAdapter.isEnabled()) {
                //Disconnect();

                String mensaje = "¿Esta seguro que desea apagar su BT?\n" +
                        "\nTenga en cuenta que se cerrara la conexión establecida con " +
                        "el módulo BlueTooth receptor si la hay.";
                dialogo = new AlertDialog.Builder(controlporvoz.this);
                dialogo.setIcon(R.drawable.ic_bluetooth);
                //dialog.setTitle("COCESNA.");
                dialogo.setTitle("ADVERTENCIA");
                dialogo.setMessage(mensaje);
                dialogo.setCancelable(false);
                dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo, int id) {
                        try {
                            bluetoothAdapter.disable();
                            socket.close();
                            socket=null;
                            Toast.makeText(getApplicationContext(), "Bluetooth apagado correctamente.", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            //e.printStackTrace();
                            msg("Bluetooth apagado.");
                        }
                        //luces_control.this.finish();
                        //finish();
                    }
                });
                dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo, int id) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Su BlueTooth continuará encendido.", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                });
                dialogo.show();

                /*
                bluetoothAdapter.disable();
                Toast.makeText(getApplicationContext(), "Bluetooth apagado correctamente.", Toast.LENGTH_LONG).show();
                */

            } else {
                Toast.makeText(getApplicationContext(), "Su Bluetooth ya esta apagado.", Toast.LENGTH_LONG).show();
            }

        }catch (Exception e)
        {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            bluetoothAdapter.disable();
            msg("Bluetooth Apagado.");

        }
    }
    //Fin función DisabledBT();

















    private String getMacBT(){
        SharedPreferences preferences = getSharedPreferences("Destinatarios", Context.MODE_PRIVATE);
        String address = preferences.getString("mac","00:00:00:00:00:00");
        return address;
    }

    //Apagar bluetooth
    private void Disconnect(){
        if (socket != null) {
            try
            {
                socket.close();
            }
            catch (IOException e)
            { msg("Error");}
        }
    }

    private void Disconnect1() {
        //if (socket != null) {
            String mensaje = "¿Esta seguro que desea salir?\n" +
                    "\nTenga en cuenta que se cerrara la conexión establecida con " +
                    "el módulo BlueTooth receptor si la hay.";
            dialogo = new AlertDialog.Builder(controlporvoz.this);
            //dialogo.setIcon(R.drawable.ic_bluetooth);
            dialogo.setIcon(android.R.drawable.ic_dialog_alert);
            //dialog.setTitle("COCESNA.");
            dialogo.setTitle("Bluetooth Control Voz");
            dialogo.setMessage(mensaje);
            dialogo.setCancelable(false);
            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    try {
                        if (socket != null) {
                            socket.close();
                         }
                        controlporvoz.this.finish();
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
        //}
    }

    private void msg(String texto){
        /*Toast toast = Toast.makeText(getApplicationContext(), "Campo respuesta es obligatorio", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();*/
        Toast.makeText(getApplicationContext(),texto,Toast.LENGTH_LONG).show();
    }


}
