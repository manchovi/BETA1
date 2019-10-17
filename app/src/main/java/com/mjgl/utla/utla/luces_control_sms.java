package com.mjgl.utla.utla;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class luces_control_sms extends AppCompatActivity implements  View.OnClickListener {
    AlertDialog.Builder dialogo;
    Toolbar toolbar;

    String numero = "78484255";   //Número de chip insertado en el shield GSM/GPRS SIM900
    Button btnLuz1, btnLuz2, btnLuz3, btnLuz4, btnLuz5, btnLuz6, btnLuz7, btnLuz8;
    Button btnLuzTodas, btnRetornar;
    //Button btnTempHumedad, btnEstadoLuces;
    Spinner combo;

    DashboardLuces dashboardLuces = new DashboardLuces();
    superClase objeto = new superClase();

    //Creando instancia para ver si funciona
    superClase superclase = new superClase();

    //NUEVO 2020
    String ENVIADO = "mensaje enviado";
    String ENTREGADO = "mensaje entregado";
    PendingIntent enviadoPI, entregadoPI;
    BroadcastReceiver smsSentReceiver, smsDeliveredReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luces_control_sms);

        //AL FINAL MEJOR CREE MI PROPIA TOOLBAR.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_left));
        toolbar.setTitleTextColor(getResources().getColor(R.color.mycolor_blanco));
        toolbar.setTitleMargin(0, 0, 0, 0);
        toolbar.setSubtitle("SMS CONTROL");
        getSupportActionBar().setTitle("UTLA");

        /*Estos dos objetos PendingIntent se utiliza para enviar transmisiones*/
        /*Cuando un mensaje SMS es enviado o entregado*/
        //Nuevo 2020.
        enviadoPI = PendingIntent.getBroadcast(this, 0, new Intent(ENVIADO), 0);
        entregadoPI = PendingIntent.getBroadcast(this, 0, new Intent(ENTREGADO), 0);

        //Función para evitar la rotación de la pantalla del CELULAR.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //y esto para pantalla completa (oculta incluso la barra de estado)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(),MainActivity.class));
                goMenu();
            }
        });

        //btnEstadoLuces = (Button)findViewById(R.id.btnEstadoLuces);
        //btnTempHumedad = (Button)findViewById(R.id.btnTempHumedad);

        btnLuz1 = (Button) findViewById(R.id.btnLuz1);
        btnLuz2 = (Button) findViewById(R.id.btnLuz2);
        btnLuz3 = (Button) findViewById(R.id.btnLuz3);
        btnLuz4 = (Button) findViewById(R.id.btnLuz4);
        btnLuz5 = (Button) findViewById(R.id.btnLuz5);
        btnLuz6 = (Button) findViewById(R.id.btnLuz6);
        btnLuz7 = (Button) findViewById(R.id.btnLuz7);
        btnLuz8 = (Button) findViewById(R.id.btnLuz8);

        btnLuzTodas = (Button) findViewById(R.id.btnLuzTodas);
        btnRetornar = (Button) findViewById(R.id.btnRetornar);

        combo = (Spinner) findViewById(R.id.combo);
        String[] lista = new String[]{
                "Sin confirmación",
                "Con confirmación",
        };

        ArrayAdapter<String> adaptador1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lista);
        combo.setAdapter(adaptador1);

        combo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(Registrarme.this, combo.getSelectedItemPosition() + " " + combo.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), combo.getSelectedItem().toString(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //btnEstadoLuces.setOnClickListener(this);
        //btnTempHumedad.setOnClickListener(this);

        btnLuz1.setOnClickListener(this);
        btnLuz2.setOnClickListener(this);
        btnLuz3.setOnClickListener(this);
        btnLuz4.setOnClickListener(this);
        btnLuz5.setOnClickListener(this);
        btnLuz6.setOnClickListener(this);
        btnLuz7.setOnClickListener(this);
        btnLuz8.setOnClickListener(this);
        btnLuzTodas.setOnClickListener(this);
        btnRetornar.setOnClickListener(this);
    }


    //Nuevo 2020
    @Override
    public void onResume() {
        super.onResume();
        //Creamos un Broadcast Receiver es una especie de receptor de los eventos
        //que produce el sisetma operativo android como SMS ENVIADO.
        smsSentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()){
                    case Activity
                            .RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS ENVIADO", Toast.LENGTH_SHORT).show();
                    break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "FALLA GENERICA", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "NO HAY SERVICIO", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "PAQUETE VACIO", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "RADIO APAGADO", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        //Reescribimos el método onReceiver que se activará cuando se produzca
        //algun evento relacionado al envio de mensaje.
        smsDeliveredReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS ENTREGADO", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS NO ENTREGADO", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        //Registrar los dos BroadcastReceivers necesarios para notificación.
        registerReceiver(smsDeliveredReceiver, new IntentFilter(ENTREGADO));
        registerReceiver(smsSentReceiver, new IntentFilter(ENVIADO));
    }

    @Override
    public void onPause(){
        super.onPause();
        //--unregister the two BroadcastReceivers--
        unregisterReceiver(smsSentReceiver);
        unregisterReceiver(smsDeliveredReceiver);
    }


    //String numero = "78484255";
    private String getTelCkto() {
        SharedPreferences preferences = getSharedPreferences("Destinatarios", Context.MODE_PRIVATE);
        String t1 = preferences.getString("telefono1", "Sin configurar.");
        return t1;
    }

    private void mensaje() {
        Toast.makeText(this, "No se ha configurado.", Toast.LENGTH_SHORT).show();
    }

    private void luz1() {
        String telckto = getTelCkto();
        if (telckto.equals("Sin configurar.")) {
            mensaje();
        } else {
            String mensaje = Config.mensaje1+"\n\"Cambiar el estado de la luminaria 1\"";
            dialogo = new AlertDialog.Builder(luces_control_sms.this);
            dialogo.setIcon(R.drawable.ic_sms);
            //dialog.setTitle("COCESNA.");
            dialogo.setTitle("SMS CONTROL");
            dialogo.setMessage(mensaje);
            dialogo.setCancelable(false);
            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    try {
                    /*
                        combo.getSelectedItem().toString()
                        combo.getSelectedItemPosition() > 0
                     */
                        if (combo.getSelectedItemPosition() > 0) {
                            EnviarMensaje(getTelCkto(), "Maria uno");
                            save_action_phone("Cambio de estado luminaria 1. Con confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());

                        } else if (combo.getSelectedItemPosition() == 0) {
                            EnviarMensaje(getTelCkto(), "Manuel luz1");
                            save_action_phone("Cambio de estado luminaria 1. Sin confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());

                        }

                    } catch (Exception e) {
                        //e.printStackTrace();
                        msg("Error. Posiblemente no haya señal.\n" +
                                "Intentelo mas tarde.");
                    }

                }
            });
            dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    //Toast toast = Toast.makeText(getApplicationContext(), "Operación Cancelada correctamente.", Toast.LENGTH_SHORT);
                    //toast.setGravity(Gravity.CENTER, 0, 0);
                    //toast.show();
                    Toast.makeText(getApplicationContext(), "Operación Cancelada.", Toast.LENGTH_LONG).show();
                }
            });
            dialogo.show();
        }
    }

    private void luz2() {
        String telckto = getTelCkto();
        if (telckto.equals("Sin configurar.")) {
            mensaje();
        } else {
            String mensaje = Config.mensaje1+"\n\"Cambiar el estado de la luminaria 2\"";
            dialogo = new AlertDialog.Builder(luces_control_sms.this);
            dialogo.setIcon(R.drawable.ic_sms);
            //dialog.setTitle("COCESNA.");
            dialogo.setTitle("SMS CONTROL");
            dialogo.setMessage(mensaje);
            dialogo.setCancelable(false);
            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    try {
                        //EnviarMensaje(numero, "Jesus luz2");
                        if (combo.getSelectedItemPosition() > 0) {
                            EnviarMensaje(getTelCkto(), "Brendali dos");
                            save_action_phone("Cambio de estado luminaria 2. Con confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                        } else if (combo.getSelectedItemPosition() == 0) {
                            EnviarMensaje(getTelCkto(), "Jesus luz2");
                            save_action_phone("Cambio de estado luminaria 2. Sin confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                        }

                    } catch (Exception e) {
                        //e.printStackTrace();
                        msg("Error. Posiblemente no haya señal.\n" +
                                "Intentelo mas tarde.");
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

    private void luz3() {
        String telckto = getTelCkto();
        if (telckto.equals("Sin configurar.")) {
            mensaje();
        } else {
            String mensaje = Config.mensaje1+"\n\"Cambiar el estado de la luminaria 3\"";
            dialogo = new AlertDialog.Builder(luces_control_sms.this);
            dialogo.setIcon(R.drawable.ic_sms);
            //dialog.setTitle("COCESNA.");
            dialogo.setTitle("SMS CONTROL");
            dialogo.setMessage(mensaje);
            dialogo.setCancelable(false);
            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    try {
                        //EnviarMensaje(numero, "Daniel luz3");
                        if (combo.getSelectedItemPosition() > 0) {
                            EnviarMensaje(getTelCkto(), "Beatriz tres");
                            save_action_phone("Cambio de estado luminaria 3. Con confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                        } else if (combo.getSelectedItemPosition() == 0) {
                            EnviarMensaje(getTelCkto(), "Daniel luz3");
                            save_action_phone("Cambio de estado luminaria 3. Sin confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                        }
                    } catch (Exception e) {
                        //e.printStackTrace();
                        msg("Error. Posiblemente no haya señal.\n" +
                                "Intentelo mas tarde.");
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

    private void luz4() {
        String telckto = getTelCkto();
        if (telckto.equals("Sin configurar.")) {
            mensaje();
        } else {
            String mensaje = Config.mensaje1+"\n\"Cambiar el estado de la luminaria 4\"";
            dialogo = new AlertDialog.Builder(luces_control_sms.this);
            dialogo.setIcon(R.drawable.ic_sms);
            //dialog.setTitle("COCESNA.");
            dialogo.setTitle("SMS CONTROL");
            dialogo.setMessage(mensaje);
            dialogo.setCancelable(false);
            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    try {
                        //EnviarMensaje(numero, "Alejandra luz4");
                        if (combo.getSelectedItemPosition() > 0) {
                            EnviarMensaje(getTelCkto(), "Jhoseline cut");
                            save_action_phone("Cambio de estado luminaria 4. Con confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                        } else if (combo.getSelectedItemPosition() == 0) {
                            EnviarMensaje(getTelCkto(), "Alejandra lu4");
                            save_action_phone("Cambio de estado luminaria 4. Sin confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                        }
                    } catch (Exception e) {
                        //e.printStackTrace();
                        msg("Error. Posiblemente no haya señal.\n" +
                                "Intentelo mas tarde.");
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

    private void luz5() {
        String telckto = getTelCkto();
        if (telckto.equals("Sin configurar.")) {
            mensaje();
        } else {
            String mensaje = Config.mensaje1+"\n\"Cambiar el estado de la luminaria 5\"";
            dialogo = new AlertDialog.Builder(luces_control_sms.this);
            dialogo.setIcon(R.drawable.ic_sms);
            //dialog.setTitle("COCESNA.");
            dialogo.setTitle("SMS CONTROL");
            dialogo.setMessage(mensaje);
            dialogo.setCancelable(false);
            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    try {
                        //EnviarMensaje(numero, "Jose luz5");
                        if (combo.getSelectedItemPosition() > 0) {
                            EnviarMensaje(getTelCkto(), "Oscar cinco");
                            save_action_phone("Cambio de estado luminaria 5. Con confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                        } else if (combo.getSelectedItemPosition() == 0) {
                            EnviarMensaje(getTelCkto(), "Jose luz5");
                            save_action_phone("Cambio de estado luminaria 5. Sin confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                        }
                    } catch (Exception e) {
                        //e.printStackTrace();
                        msg("Error. Posiblemente no haya señal.\n" +
                                "Intentelo mas tarde.");
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

    private void luz6() {
        String telckto = getTelCkto();
        if (telckto.equals("Sin configurar.")) {
            mensaje();
        } else {
            String mensaje = Config.mensaje1+"\n\"Cambiar el estado de la luminaria 6\"";
            dialogo = new AlertDialog.Builder(luces_control_sms.this);
            dialogo.setIcon(R.drawable.ic_sms);
            //dialog.setTitle("COCESNA.");
            dialogo.setTitle("SMS CONTROL");
            dialogo.setMessage(mensaje);
            dialogo.setCancelable(false);
            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    try {
                        //EnviarMensaje(numero, "Carlos luz6");
                        if (combo.getSelectedItemPosition() > 0) {
                            EnviarMensaje(getTelCkto(), "Douglas seis");
                            save_action_phone("Cambio de estado luminaria 6. Con confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                        } else if (combo.getSelectedItemPosition() == 0) {
                            EnviarMensaje(getTelCkto(), "Carlos luz6");
                            save_action_phone("Cambio de estado luminaria 6. Sin confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                        }
                    } catch (Exception e) {
                        //e.printStackTrace();
                        msg("Error. Posiblemente no haya señal.\n" +
                                "Intentelo mas tarde.");
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

    private void luz7() {
        String telckto = getTelCkto();
        if (telckto.equals("Sin configurar.")) {
            mensaje();
        } else {
            String mensaje = Config.mensaje1+"\n\"Cambiar el estado de la luminaria 7\"";
            dialogo = new AlertDialog.Builder(luces_control_sms.this);
            dialogo.setIcon(R.drawable.ic_sms);
            //dialog.setTitle("COCESNA.");
            dialogo.setTitle("SMS CONTROL");
            dialogo.setMessage(mensaje);
            dialogo.setCancelable(false);
            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    try {
                        //EnviarMensaje(numero, "Juan luz7");
                        if (combo.getSelectedItemPosition() > 0) {
                            EnviarMensaje(getTelCkto(), "Antonio siete");
                            save_action_phone("Cambio de estado luminaria 7. Con confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                        } else if (combo.getSelectedItemPosition() == 0) {
                            EnviarMensaje(getTelCkto(), "Juan luz7");
                            save_action_phone("Cambio de estado luminaria 7. Sin confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                        }
                    } catch (Exception e) {
                        //e.printStackTrace();
                        msg("Error. Posiblemente no haya señal.\n" +
                                "Intentelo mas tarde.");
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

    private void luz8() {
        String telckto = getTelCkto();
        if (telckto.equals("Sin configurar.")) {
            mensaje();
        } else {
            String mensaje = Config.mensaje1+"\n\"Cambiar el estado de la luminaria 8\"";
            dialogo = new AlertDialog.Builder(luces_control_sms.this);
            dialogo.setIcon(R.drawable.ic_sms);
            //dialog.setTitle("COCESNA.");
            dialogo.setTitle("SMS CONTROL");
            dialogo.setMessage(mensaje);
            dialogo.setCancelable(false);
            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    try {
                        //EnviarMensaje(numero, "Beatriz luz8");
                        if (combo.getSelectedItemPosition() > 0) {
                            EnviarMensaje(getTelCkto(), "Olga ocho");
                            save_action_phone("Cambio de estado luminaria 8. Con confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                        } else if (combo.getSelectedItemPosition() == 0) {
                            EnviarMensaje(getTelCkto(), "Beatriz luz8");
                            save_action_phone("Cambio de estado luminaria 8. Sin confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                        }
                    } catch (Exception e) {
                        //e.printStackTrace();
                        msg("Error. Posiblemente no haya señal.\n" +
                                "Intentelo mas tarde.");
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

    private void Alluces() {
        String telckto = getTelCkto();
        if (telckto.equals("Sin configurar.")) {
            mensaje();
        } else {
            String mensaje = Config.mensaje1+"\n\"Cambiar el estado de todas las luminarias\"";
            dialogo = new AlertDialog.Builder(luces_control_sms.this);
            dialogo.setIcon(R.drawable.ic_sms);
            //dialog.setTitle("COCESNA.");
            dialogo.setTitle("SMS CONTROL");
            dialogo.setMessage(mensaje);
            dialogo.setCancelable(false);
            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    try {
                        //EnviarMensaje(numero, "Cambiar");
                        if (combo.getSelectedItemPosition() > 0) {
                            EnviarMensaje(getTelCkto(), "Chovi OK");
                            save_action_phone("Cambio de estado de todas las luminarias. Con confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                        } else if (combo.getSelectedItemPosition() == 0) {
                            EnviarMensaje(getTelCkto(), "Cambiar");
                            save_action_phone("Cambio de estado de todas las luminarias. Sin confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                        }
                    } catch (Exception e) {
                        msg("Error. Posiblemente no haya señal.\n" +
                                "Intentelo mas tarde.");
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

    private void getCall() {
        String telckto = getTelCkto();
        if (telckto.equals("Sin configurar.")) {
            mensaje();
        } else {
            String mensaje = Config.mensaje1+"\n\"Recibir una llamada perdida del sistema electrónico.\"" +
                    "\n\nRecordatorio:\n**El sistema electrónico debe estar en estado ON." +
                    "\n**Disponibilidad de señal para comunicación en ambos dispositivos." +
                    "\n**Disponibilidad de saldo en ambos dispositivos (Receptor y Emisor).\n";
            dialogo = new AlertDialog.Builder(luces_control_sms.this);
            dialogo.setIcon(R.drawable.ic_call);
            //dialog.setTitle("COCESNA.");
            dialogo.setTitle("Solicitud de Call.");
            dialogo.setMessage(mensaje);
            dialogo.setCancelable(false);
            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    try {
                        EnviarMensaje(getTelCkto(), "llamame OK");
                        save_action_phone("Solicitud de llamada perdida.", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    } catch (Exception e) {
                        msg("Error. Posiblemente no haya señal.\n" +
                                "Intentelo mas tarde.");
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


    private void msg(String s) {
        /*Toast toast = Toast.makeText(getApplicationContext(), "Campo respuesta es obligatorio", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();*/
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.btnTempHumedad:
                getInfoTH();
                break;

            case R.id.btnEstadoLuces:
                getInfoStatusAllces();
                break;*/

            case R.id.btnLuz1:
                luz1();
                break;

            case R.id.btnLuz2:
                //EnviarMensaje("78484255", "Jesus luz2");
                luz2();
                break;

            case R.id.btnLuz3:
                luz3();
                break;

            case R.id.btnLuz4:
                luz4();
                break;

            case R.id.btnLuz5:
                luz5();
                break;

            case R.id.btnLuz6:
                luz6();
                break;

            case R.id.btnLuz7:
                luz7();
                break;

            case R.id.btnLuz8:
                luz8();
                break;

            case R.id.btnLuzTodas:
                //EnviarMensaje("78484255", "Cambiar");
                Alluces();
                break;

            case R.id.btnRetornar:
                goMenu();
                break;

            default:
                break;
        }
    }

    private void EnviarMensaje(String Numero, String Mensaje) {
        try {
            SmsManager sms = SmsManager.getDefault();
            //sms.sendTextMessage(Numero, null, Mensaje, null, null);
            sms.sendTextMessage(Numero, null, Mensaje, enviadoPI, entregadoPI);
            //Toast.makeText(getApplicationContext(), "Comando de control enviado.", Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), "Comando de control enviado.\n\nDestinatario:" + Numero, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Mensaje no enviado, datos incorrectos.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    /*
    private void mensajeLargo(){
    //OBTENIENDO LA FECHA Y HORA ACTUAL DEL SISTEMA.
    DateFormat formatodate = new SimpleDateFormat("yyyy/MM/dd");
    String date = formatodate.format(new Date());

    DateFormat formatotime = new SimpleDateFormat("HH:mm:ss a");
    String time = formatotime.format(new Date());
    String datosCompletos = "ALERTA COCESNA.\n" +
            "Temperatura: " + temperatura + " °C\n" +
            "Humedad: " + humedad + " RH\n" +
            "Fecha: " + fecha + "\n" +
            "Hora: " + hora + "\n" +
            "Send. by System IoT";
    try{
        SmsManager sms = SmsManager.getDefault();
        //sms.sendTextMessage(numTel, null, datosCompletos, null,null);  //FUNCION LIMITADO A MENOS CARACTERES POR SMS
        ArrayList msgTexts = sms.divideMessage(datosCompletos);
        sms.sendMultipartTextMessage(numTel, null, msgTexts, null, null);
        //Toast.makeText(getApplicationContext(), "Mensaje Enviado.", Toast.LENGTH_LONG).show();
        Toast toast = Toast.makeText(getApplicationContext(), "MENSAJE ENVIADO A MÓVIL: " + numTel, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    } catch(Exception e){
        Toast.makeText(getApplicationContext(), "Mensaje no enviado, datos incorrectos." + e.getMessage().toString(), Toast.LENGTH_LONG).show();
        e.printStackTrace();
    }
}

*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_luces_control, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.opCall) {
            //getCall();
            superclase.getCall(luces_control_sms.this);
            return true;
        } else if (id == R.id.opExit) {
            goMenu();
            return true;
        }

        /*if(id==R.id.opAyuda){
            Toast.makeText(getApplicationContext(),"Ayuda",Toast.LENGTH_LONG).show();
            //AyudaBT();
            return true;
        }else if(id==R.id.opSalir){
            Toast.makeText(getApplicationContext(),"Salir",Toast.LENGTH_LONG).show();
            return true;
        }else if(id==R.id.opCall){
            getCall();
            return true;
        }else if(id==R.id.opOnOffCombinado){
            //dashboardLuces.Showpopup();
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }


    private void goMenu() {
        //startActivity(new Intent(getApplicationContext(),MainActivity.class));
        Intent intent = new Intent(luces_control_sms.this, MainActivity.class);
        startActivity(intent);
    }


    //inicio: Función para estar guardando en la bd en la nube las acciones de control sin notificación
    //        o confirmación de cambio de estado.

    public String getDate() {
        //Función para obtener la fecha.
        DateFormat formatodate = new SimpleDateFormat("yyyy/MM/dd");
        String date = formatodate.format(new Date());
        return date;
    }

    public String getTime() {
        //Función para obtener la hora.
        DateFormat formatotime = new SimpleDateFormat("HH:mm:ss a");
        String time = formatotime.format(new Date());
        return time;
    }

    public String obtenerTelPropietario() {
        SharedPreferences preferences = getSharedPreferences("Destinatarios", MODE_PRIVATE);
        return preferences.getString("telefono0", "Sin configurar.");
    }

    public String obtenerServer() {
        SharedPreferences preferences = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        String server = preferences.getString("servidor", "Sin configurar.");
        String folder = preferences.getString("folder", "Sin configurar.");
        return server + folder;
    }

    public void save_action_phone(final String descripcion, final String celular1, final String celular2, final String fecha, final String hora) {
        String pc = obtenerServer();

        //String url_guardar_destinatarios = "http://" + pc + "/service/registrophone.php";
        String url_guardar_destinatarios = pc + "/service/registrophone.php";

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
                Toast.makeText(getApplicationContext(), "Sin Internet...", Toast.LENGTH_LONG).show();
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
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    //fin
}