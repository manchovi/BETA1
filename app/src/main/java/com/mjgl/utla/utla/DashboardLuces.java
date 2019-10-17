package com.mjgl.utla.utla;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DashboardLuces extends AppCompatActivity {

    //boolean estadoAll = false;
    int estadoAll=0;
    /*boolean estadof1 = false;boolean estadof2 = false;boolean estadof3 = false;boolean estadof4 = false;
    boolean estadof5 = false;boolean estadof6 = false;boolean estadof7 = false;boolean estadof8 = false;*/

    int estadof1=0;int estadof2=0;int estadof3=0;int estadof4=0;
    int estadof5=0;int estadof6=0;int estadof7=0;int estadof8=0;

    ToggleButton sw0,sw1,sw2,sw3,sw4,sw5,sw6,sw7,sw8;
    Button button;
    TextView txtclose;

    Dialog myDialog;
    Toolbar toolbar;
    AlertDialog.Builder dialogo;
    String estadoOn = "ON";
    String estadoOff = "OFF";

    Context context;

    ImageView f_all,f1,f2,f3,f4,f5,f6,f7,f8;

    //ivLuz1
    ImageView ivLuz2, ivLuz3, ivLuz4, ivLuz5, ivLuz6, ivLuz7, ivLuz8;

    TextView tvLuz1Alto, tvLuz2Alto, tvLuz3Alto, tvLuz4Alto, tvLuz5Alto, tvLuz6Alto, tvLuz7Alto, tvLuz8Alto;
    TextView tvLuz1Bajo, tvLuz2Bajo, tvLuz3Bajo, tvLuz4Bajo, tvLuz5Bajo, tvLuz6Bajo, tvLuz7Bajo, tvLuz8Bajo;

    //Creando instancia para ver si funciona
    superClase superclase = new superClase();


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new android.app.AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_accessibility)
                    .setTitle("Información")
                    .setMessage("¿Realmente desea cerrar el dashboard?")
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {//un listener que al pulsar, cierre la aplicacion
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            /*Intent intent = new Intent(DashboardLuces.this, luces_control_sms.class);
                            startActivity(intent);*/
                            DashboardLuces.this.finishAffinity();
                            //DashboardLuces.this.finish();
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
        setContentView(R.layout.activity_dashboard_luces);

        //AL FINAL MEJOR CREE MI PROPIA TOOLBAR.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_left));
        toolbar.setTitleTextColor(getResources().getColor(R.color.mycolor_blanco));
        toolbar.setTitleMargin(0, 0, 0, 0);
        toolbar.setSubtitle("Estado actual de luminarias laboratorio");
        getSupportActionBar().setTitle("UTLA");

        myDialog = new Dialog(this);

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

        //ivLuz1 = (ImageView)findViewById(R.id.ivLuz1);

        final ImageView ivLuz1 = (ImageView)findViewById(R.id.ivLuz1);
        //ivLuz1.setImageResource(R.drawable.ic_luz22);

        ivLuz2 = (ImageView)findViewById(R.id.ivLuz2);
        ivLuz3 = (ImageView)findViewById(R.id.ivLuz3);
        ivLuz4 = (ImageView)findViewById(R.id.ivLuz4);
        ivLuz5 = (ImageView)findViewById(R.id.ivLuz5);
        ivLuz6 = (ImageView)findViewById(R.id.ivLuz6);
        ivLuz7 = (ImageView)findViewById(R.id.ivLuz7);
        ivLuz8 = (ImageView)findViewById(R.id.ivLuz8);

        ivLuz1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                luz1();
            }
        });

        ivLuz2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                luz2();
            }
        });

        ivLuz3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                luz3();
            }
        });

        ivLuz4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                luz4();
            }
        });

        ivLuz5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                luz5();
            }
        });

        ivLuz6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                luz6();
            }
        });

        ivLuz7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                luz7();
            }
        });

        ivLuz8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                luz8();
            }
        });


        tvLuz1Alto = (TextView)findViewById(R.id.tvLuz1Alto);
        tvLuz2Alto = (TextView)findViewById(R.id.tvLuz2Alto);
        tvLuz3Alto = (TextView)findViewById(R.id.tvLuz3Alto);
        tvLuz4Alto = (TextView)findViewById(R.id.tvLuz4Alto);
        tvLuz5Alto = (TextView)findViewById(R.id.tvLuz5Alto);
        tvLuz6Alto = (TextView)findViewById(R.id.tvLuz6Alto);
        tvLuz7Alto = (TextView)findViewById(R.id.tvLuz7Alto);
        tvLuz8Alto = (TextView)findViewById(R.id.tvLuz8Alto);

        tvLuz1Bajo = (TextView)findViewById(R.id.tvLuz1Bajo);
        tvLuz2Bajo = (TextView)findViewById(R.id.tvLuz2Bajo);
        tvLuz3Bajo = (TextView)findViewById(R.id.tvLuz3Bajo);
        tvLuz4Bajo = (TextView)findViewById(R.id.tvLuz4Bajo);
        tvLuz5Bajo = (TextView)findViewById(R.id.tvLuz5Bajo);
        tvLuz6Bajo = (TextView)findViewById(R.id.tvLuz6Bajo);
        tvLuz7Bajo = (TextView)findViewById(R.id.tvLuz7Bajo);
        tvLuz8Bajo = (TextView)findViewById(R.id.tvLuz8Bajo);

        //ivLuz1.setImageResource(R.drawable.ic_luz22);

        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            String addres = extras.getString("MessageNumber");
            String message = extras.getString("Message");
            String bandera = extras.getString("Flag");

            String sCadena = "Hola Mundo";
            String sSubCadena = sCadena.substring(5, 10);
            //System.out.println(sSubCadena);

            //ivLuz1.setImageResource(R.drawable.ic_luz22);

            if (bandera.equals("1")) {
                //System.out.println("palabra encontrada");
                //System.out.println("Posición : " + intIndex);
                String StatusFoco1 = message.substring(29, 30);
                if (StatusFoco1.equals("1")) {
                    //ivLuz1.setImageResource(R.drawable.ic_luz12);
                    ivLuz1.setBackgroundResource(R.drawable.cerclebackgroundpink);
                    ivLuz1.setImageResource(R.drawable.f1on);
                    tvLuz1Alto.setText("Luminaria 1 : "+estadoOn);
                    tvLuz1Bajo.setText("Estado actual de esta luminaria es: " +estadoOn);
                    //tvLuz1Bajo.setText("Estado actual de esta luminaria es: " + StatusFoco1);
                } else {
                    //ivLuz1.setImageResource(R.drawable.ic_luz22);
                    ivLuz1.setBackgroundResource(R.drawable.cerclebackgroundpink);
                    ivLuz1.setImageResource(R.drawable.f2off);
                    tvLuz1Alto.setText("Luminaria 1 : "+estadoOff);
                    tvLuz1Bajo.setText("Estado actual de esta luminaria es: "+estadoOff);
                    //tvLuz1Bajo.setText("Estado actual de esta luminaria es: " + StatusFoco1);
                }

                String StatusFoco2 = message.substring(39, 40);
                if (StatusFoco2.equals("1")) {
                    //ivLuz2.setImageResource(R.drawable.ic_luz12);
                    ivLuz2.setBackgroundResource(R.drawable.cerclebackgroundpink);
                    ivLuz2.setImageResource(R.drawable.f1on);
                    tvLuz2Alto.setText("Luminaria 2 : "+estadoOn);
                    tvLuz2Bajo.setText("Estado actual de esta luminaria es: " + estadoOn);
                } else {
                    //ivLuz2.setImageResource(R.drawable.ic_luz22);
                    ivLuz2.setBackgroundResource(R.drawable.cerclebackgroundpink);
                    ivLuz2.setImageResource(R.drawable.f2off);
                    tvLuz2Alto.setText("Luminaria 2 : "+estadoOff);
                    tvLuz2Bajo.setText("Estado actual de esta luminaria es: " + estadoOff);
                }

                String StatusFoco3 = message.substring(49, 50);
                if (StatusFoco3.equals("1")) {
                    //ivLuz3.setImageResource(R.drawable.ic_luz12);
                    ivLuz3.setBackgroundResource(R.drawable.cerclebackgroundpink);
                    ivLuz3.setImageResource(R.drawable.f1on);
                    tvLuz3Alto.setText("Luminaria 3 : "+estadoOn);
                    tvLuz3Bajo.setText("Estado actual de esta luminaria es: " + estadoOn);

                } else {
                    //ivLuz3.setImageResource(R.drawable.ic_luz22);
                    ivLuz3.setBackgroundResource(R.drawable.cerclebackgroundpink);
                    ivLuz3.setImageResource(R.drawable.f2off);
                    tvLuz3Alto.setText("Luminaria 3 : "+estadoOff);
                    tvLuz3Bajo.setText("Estado actual de esta luminaria es: " + estadoOff);
                }

                //arreglar de aca en adelante.
                String StatusFoco4 = message.substring(59, 60);
                if (StatusFoco4.equals("1")) {
                    //ivLuz4.setImageResource(R.drawable.ic_luz12);
                    ivLuz4.setBackgroundResource(R.drawable.cerclebackgroundpink);
                    ivLuz4.setImageResource(R.drawable.f1on);
                    tvLuz4Alto.setText("Luminaria 4 : "+estadoOn);
                    tvLuz4Bajo.setText("Estado actual de esta luminaria es: " + estadoOn);
                } else {
                    //ivLuz4.setImageResource(R.drawable.ic_luz22);
                    ivLuz4.setBackgroundResource(R.drawable.cerclebackgroundpink);
                    ivLuz4.setImageResource(R.drawable.f2off);
                    tvLuz4Alto.setText("Luminaria 4 : "+estadoOff);
                    tvLuz4Bajo.setText("Estado actual de esta luminaria es: " + estadoOff);
                }

                String StatusFoco5 = message.substring(69, 70);
                if (StatusFoco5.equals("1")) {
                    //ivLuz5.setImageResource(R.drawable.ic_luz12);
                    ivLuz5.setBackgroundResource(R.drawable.cerclebackgroundpink);
                    ivLuz5.setImageResource(R.drawable.f1on);
                    tvLuz5Alto.setText("Luminaria 5 : "+estadoOn);
                    tvLuz5Bajo.setText("Estado actual de esta luminaria es: " + estadoOn);
                } else {
                    //ivLuz5.setImageResource(R.drawable.ic_luz22);
                    ivLuz5.setBackgroundResource(R.drawable.cerclebackgroundpink);
                    ivLuz5.setImageResource(R.drawable.f2off);
                    tvLuz5Alto.setText("Luminaria 5 : "+estadoOff);
                    tvLuz5Bajo.setText("Estado actual de esta luminaria es: " + estadoOff);
                }

                String StatusFoco6 = message.substring(79, 80);
                if (StatusFoco6.equals("1")) {
                    //ivLuz6.setImageResource(R.drawable.ic_luz12);
                    ivLuz6.setBackgroundResource(R.drawable.cerclebackgroundpink);
                    ivLuz6.setImageResource(R.drawable.f1on);
                    tvLuz6Alto.setText("Luminaria 6 : "+estadoOn);
                    tvLuz6Bajo.setText("Estado actual de esta luminaria es: " + estadoOn);
                } else {
                    //ivLuz6.setImageResource(R.drawable.ic_luz22);
                    ivLuz6.setBackgroundResource(R.drawable.cerclebackgroundpink);
                    ivLuz6.setImageResource(R.drawable.f2off);
                    tvLuz6Alto.setText("Luminaria 6 : "+estadoOff);
                    tvLuz6Bajo.setText("Estado actual de esta luminaria es: " + estadoOff);
                }

                String StatusFoco7 = message.substring(89, 90);
                if (StatusFoco7.equals("1")) {
                    //ivLuz7.setImageResource(R.drawable.ic_luz12);
                    ivLuz7.setBackgroundResource(R.drawable.cerclebackgroundpink);
                    ivLuz7.setImageResource(R.drawable.f1on);
                    tvLuz7Alto.setText("Luminaria 7 : "+estadoOn);
                    tvLuz7Bajo.setText("Estado actual de esta luminaria es: " + estadoOn);
                } else {
                    //ivLuz7.setImageResource(R.drawable.ic_luz22);
                    ivLuz7.setBackgroundResource(R.drawable.cerclebackgroundpink);
                    ivLuz7.setImageResource(R.drawable.f2off);
                    tvLuz7Alto.setText("Luminaria 7 : "+estadoOff);
                    tvLuz7Bajo.setText("Estado actual de esta luminaria es: " + estadoOff);
                }

                String StatusFoco8 = message.substring(99, 100);
                if (StatusFoco8.equals("1")) {
                    //ivLuz8.setImageResource(R.drawable.ic_luz12);
                    ivLuz8.setBackgroundResource(R.drawable.cerclebackgroundpink);
                    ivLuz8.setImageResource(R.drawable.f1on);
                    tvLuz8Alto.setText("Luminaria 8 : "+estadoOn);
                    tvLuz8Bajo.setText("Estado actual de esta luminaria es: " + estadoOn);
                } else {
                    //ivLuz8.setImageResource(R.drawable.ic_luz22);
                    ivLuz8.setBackgroundResource(R.drawable.cerclebackgroundpink);
                    ivLuz8.setImageResource(R.drawable.f2off);
                    tvLuz8Alto.setText("Luminaria 8 : "+estadoOff);
                    tvLuz8Bajo.setText("Estado actual de esta luminaria es: " + estadoOff);
                }

                /*Toast.makeText(getApplicationContext(), "Foco 1:" + StatusFoco1 + "\n" +
                        "Foco 2:" + StatusFoco2 + "\n" +
                        "Foco 3:" + StatusFoco3.trim() + "\n" +
                        "Foco 4:" + StatusFoco4 + "\n" +
                        "Foco 5:" + StatusFoco5 + "\n" +
                        "Foco 6:" + StatusFoco6 + "\n" +
                        "Foco 7:" + StatusFoco7 + "\n" +
                        "Foco 8:" + StatusFoco8, Toast.LENGTH_SHORT).show();*/
            } else {
                //System.out.println("palabra no encontrada");
                //Toast.makeText(getApplicationContext(),"sms Temperatura y RH.",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void goMenu(){
        //startActivity(new Intent(getApplicationContext(),MainActivity.class));
        String mensaje = "¿Realmente desea cerrar el dashboard?";
        dialogo = new AlertDialog.Builder(DashboardLuces.this);
        dialogo.setIcon(R.drawable.ic_accessibility);
        dialogo.setTitle("Información");
        dialogo.setMessage(mensaje);
        dialogo.setCancelable(false);
        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo, int id) {
                /*Intent intent = new Intent(DashboardLuces.this, luces_control_sms.class);
                startActivity(intent);*/
                //DashboardLuces.this.finishAffinity();
                DashboardLuces.this.finish();
            }
        });
        dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo, int id) {
                Toast.makeText(getApplicationContext(), "Operación Cancelada.", Toast.LENGTH_LONG).show();
            }
        });
        dialogo.show();
    }


    //Codigo que he COPIADO DE LA CLASE luces_control_sms.

    private void msg(String s){
        /*Toast toast = Toast.makeText(getApplicationContext(), "Campo respuesta es obligatorio", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();*/
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    private void mensaje(){
        Toast.makeText(this, "No se ha configurado el dispositivo receptor aún.", Toast.LENGTH_SHORT).show();
    }

    private String getTelCkto(){
        SharedPreferences preferences = getSharedPreferences("Destinatarios", Context.MODE_PRIVATE);
        String t1 = preferences.getString("telefono1","Sin configurar.");
        return t1;
    }

    private void luz1() {
        String telckto = getTelCkto();
        if (telckto.equals("Sin configurar.")) {
            mensaje();
        }else{
            String mensaje = Config.mensaje1+"\n\"Cambiar el estado de la luminaria 1\"";
            dialogo = new AlertDialog.Builder(DashboardLuces.this);
            dialogo.setIcon(R.drawable.ic_sms);
            //dialog.setTitle("COCESNA.");
            dialogo.setTitle("SMS CONTROL");
            dialogo.setMessage(mensaje);
            dialogo.setCancelable(false);
            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    try {
                            EnviarMensaje(getTelCkto(), "Maria uno");
                            superclase.save_action_phone(DashboardLuces.this,"Cambio de estado luminaria 1. Con confirmación",superclase.getTelReceptor(getApplicationContext()),superclase.getTelPropietario(getApplicationContext()),superclase.getDate(),superclase.getTime());
                            //EnviarMensaje(getTelCkto(), "Manuel luz1");
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
            dialogo = new AlertDialog.Builder(DashboardLuces.this);
            dialogo.setIcon(R.drawable.ic_sms);
            //dialog.setTitle("COCESNA.");
            dialogo.setTitle("SMS CONTROL");
            dialogo.setMessage(mensaje);
            dialogo.setCancelable(false);
            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    try {
                            EnviarMensaje(getTelCkto(), "Brendali dos");
                        superclase.save_action_phone(DashboardLuces.this,"Cambio de estado luminaria 2. Con confirmación",superclase.getTelReceptor(getApplicationContext()),superclase.getTelPropietario(getApplicationContext()),superclase.getDate(),superclase.getTime());
                            //EnviarMensaje(getTelCkto(), "Jesus luz2");
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
            dialogo = new AlertDialog.Builder(DashboardLuces.this);
            dialogo.setIcon(R.drawable.ic_sms);
            //dialog.setTitle("COCESNA.");
            dialogo.setTitle("SMS CONTROL");
            dialogo.setMessage(mensaje);
            dialogo.setCancelable(false);
            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    try {
                            EnviarMensaje(getTelCkto(), "Beatriz tres");
                            superclase.save_action_phone(DashboardLuces.this,"Cambio de estado luminaria 3. Con confirmación",superclase.getTelReceptor(getApplicationContext()),superclase.getTelPropietario(getApplicationContext()),superclase.getDate(),superclase.getTime());
                            //EnviarMensaje(getTelCkto(), "Daniel luz3");
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
            dialogo = new AlertDialog.Builder(DashboardLuces.this);
            dialogo.setIcon(R.drawable.ic_sms);
            //dialog.setTitle("COCESNA.");
            dialogo.setTitle("SMS CONTROL");
            dialogo.setMessage(mensaje);
            dialogo.setCancelable(false);
            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    try {
                            EnviarMensaje(getTelCkto(), "Jhoseline cut");
                            superclase.save_action_phone(DashboardLuces.this,"Cambio de estado luminaria 4. Con confirmación",superclase.getTelReceptor(getApplicationContext()),superclase.getTelPropietario(getApplicationContext()),superclase.getDate(),superclase.getTime());
                            //EnviarMensaje(getTelCkto(), "Alejandra lu4");
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
            dialogo = new AlertDialog.Builder(DashboardLuces.this);
            dialogo.setIcon(R.drawable.ic_sms);
            //dialog.setTitle("COCESNA.");
            dialogo.setTitle("SMS CONTROL");
            dialogo.setMessage(mensaje);
            dialogo.setCancelable(false);
            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    try {
                            EnviarMensaje(getTelCkto(), "Oscar cinco");
                            superclase.save_action_phone(DashboardLuces.this,"Cambio de estado luminaria 5. Con confirmación",superclase.getTelReceptor(getApplicationContext()),superclase.getTelPropietario(getApplicationContext()),superclase.getDate(),superclase.getTime());
                            //EnviarMensaje(getTelCkto(), "Jose luz5");
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
            dialogo = new AlertDialog.Builder(DashboardLuces.this);
            dialogo.setIcon(R.drawable.ic_sms);
            //dialog.setTitle("COCESNA.");
            dialogo.setTitle("SMS CONTROL");
            dialogo.setMessage(mensaje);
            dialogo.setCancelable(false);
            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    try {
                            EnviarMensaje(getTelCkto(), "Douglas seis");
                            superclase.save_action_phone(DashboardLuces.this,"Cambio de estado luminaria 6. Con confirmación",superclase.getTelReceptor(getApplicationContext()),superclase.getTelPropietario(getApplicationContext()),superclase.getDate(),superclase.getTime());
                            //EnviarMensaje(getTelCkto(), "Carlos luz6");
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
            dialogo = new AlertDialog.Builder(DashboardLuces.this);
            dialogo.setIcon(R.drawable.ic_sms);
            //dialog.setTitle("COCESNA.");
            dialogo.setTitle("SMS CONTROL");
            dialogo.setMessage(mensaje);
            dialogo.setCancelable(false);
            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    try {
                            EnviarMensaje(getTelCkto(), "Antonio siete");
                            superclase.save_action_phone(DashboardLuces.this,"Cambio de estado luminaria 7. Con confirmación",superclase.getTelReceptor(getApplicationContext()),superclase.getTelPropietario(getApplicationContext()),superclase.getDate(),superclase.getTime());
                            //EnviarMensaje(getTelCkto(), "Juan luz7");
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

    private void luz8() {
        String telckto = getTelCkto();
        if (telckto.equals("Sin configurar.")) {
            mensaje();
        } else {
            String mensaje = Config.mensaje1+"\n\"Cambiar el estado de la luminaria 8\"";
            dialogo = new AlertDialog.Builder(DashboardLuces.this);
            dialogo.setIcon(R.drawable.ic_sms);
            //dialog.setTitle("COCESNA.");
            dialogo.setTitle("SMS CONTROL");
            dialogo.setMessage(mensaje);
            dialogo.setCancelable(false);
            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    try {
                            EnviarMensaje(getTelCkto(), "Olga ocho");
                            superclase.save_action_phone(DashboardLuces.this,"Cambio de estado luminaria 8. Con confirmación",superclase.getTelReceptor(getApplicationContext()),superclase.getTelPropietario(getApplicationContext()),superclase.getDate(),superclase.getTime());
                            //EnviarMensaje(getTelCkto(), "Beatriz luz8");
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

    private void getInfoStatusAllces() {
        String telckto = getTelCkto();
        if (telckto.equals("Sin configurar.")) {
            mensaje();
        } else {
            String mensaje = Config.mensaje1+"\n\"Obtener información del estado de todas las luminarias\"";
            dialogo = new AlertDialog.Builder(DashboardLuces.this);
            dialogo.setIcon(R.drawable.ic_sms);
            //dialog.setTitle("COCESNA.");
            dialogo.setTitle("SMS CONTROL");
            dialogo.setMessage(mensaje);
            dialogo.setCancelable(false);
            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    try {
                        EnviarMensaje(getTelCkto(), "info luceslab");
                        superclase.save_action_phone(DashboardLuces.this,"Consulta Estado Actual de Luminarias en el Laboratorio",superclase.getTelReceptor(getApplicationContext()),superclase.getTelPropietario(getApplicationContext()),superclase.getDate(),superclase.getTime());
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

    private void Alluces() {
        String telckto = getTelCkto();
        if (telckto.equals("Sin configurar.")) {
            mensaje();
        } else {
            String mensaje = Config.mensaje1+"\n\"Cambiar el estado de todas las luminarias\"";
            dialogo = new AlertDialog.Builder(DashboardLuces.this);
            dialogo.setIcon(R.drawable.ic_sms);
            dialogo.setTitle("SMS CONTROL");
            dialogo.setMessage(mensaje);
            dialogo.setCancelable(false);
            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    try {
                            EnviarMensaje(getTelCkto(), "Chovi OK");
                            superclase.save_action_phone(DashboardLuces.this,"Cambio de estado de todas las luminarias. Con confirmación",superclase.getTelReceptor(getApplicationContext()),superclase.getTelPropietario(getApplicationContext()),superclase.getDate(),superclase.getTime());
                            //EnviarMensaje(getTelCkto(), "Cambiar");
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

    public void universal(final String mensaje1) {
        Toast.makeText(getApplicationContext(), "Status Flag:"+estadof8+estadof7+estadof6+estadof5+estadof4+estadof3+estadof2+estadof1+
                "\n\nDato:"+mensaje1, Toast.LENGTH_SHORT).show();

        String telckto = getTelCkto();
        if (telckto.equals("Sin configurar.")) {
            mensaje();
        } else {
            final String mensaje = "¿Realmente desea cambiar el estado" +
                    " de la/las luminarias seleccionadas?";
            dialogo = new AlertDialog.Builder(DashboardLuces.this);
            dialogo.setIcon(R.drawable.ic_sms);
            //dialog.setTitle("COCESNA.");
            dialogo.setTitle("SMS CONTROL");
            dialogo.setMessage(mensaje);
            dialogo.setCancelable(false);
            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    try {
                        EnviarMensaje(getTelCkto(), mensaje1);
                        //EnviarMensaje(getTelCkto(), "Cambiar");
                        myDialog.dismiss();
                    } catch (Exception e) {
                        msg("Error. Posiblemente no haya señal.\n" +
                                "Intentelo mas tarde.");
                    }
                }
            });
            dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    Toast.makeText(getApplicationContext(), "Operación Cancelada.", Toast.LENGTH_LONG).show();
                    //myDialog.dismiss();
                    estadof1=0;estadof2=0;estadof3=0;estadof4=0;
                    estadof5=0;estadof6=0;estadof7=0;estadof8=0;
                    sw0.setChecked(false);sw1.setChecked(false);sw2.setChecked(false);
                    sw3.setChecked(false);sw4.setChecked(false);sw5.setChecked(false);
                    sw6.setChecked(false);sw7.setChecked(false);sw8.setChecked(false);

                    f_all.setImageResource(R.drawable.ic_luz22);
                    f1.setImageResource(R.drawable.ic_luz22);
                    f2.setImageResource(R.drawable.ic_luz22);
                    f3.setImageResource(R.drawable.ic_luz22);
                    f4.setImageResource(R.drawable.ic_luz22);
                    f5.setImageResource(R.drawable.ic_luz22);
                    f6.setImageResource(R.drawable.ic_luz22);
                    f7.setImageResource(R.drawable.ic_luz22);
                    f8.setImageResource(R.drawable.ic_luz22);

                }
            });
            dialogo.show();
        }

    }

    public void getCall() {
        String telckto = getTelCkto();
        if (telckto.equals("Sin configurar.")) {
            mensaje();
        } else {
            String mensaje = Config.mensaje1+"\n\"Recibir una llamada perdida del sistema electrónico.\"" +
                    "\n\nRecordatorio:\n**El sistema electrónico debe estar en estado ON." +
                    "\n**Disponibilidad de señal para comunicación en ambos dispositivos." +
                    "\n**Disponibilidad de saldo en ambos dispositivos (Receptor y Emisor).\n";

                   /* "Confirme si ¿Realmente desea recibir una llamada del sistema? \n" +
                    "Recuerde que el sistema le podra llamar si se cumple lo siguiente:\n" +
                    "\n * Estar debidamente alimentado y encendido." +
                    "\n * Exista señal de cobertura en este momento." +
                    "\n * Se posea saldo en el chip instalado en el sistema.\n";*/
            dialogo = new AlertDialog.Builder(DashboardLuces.this);
            dialogo.setIcon(R.drawable.ic_call);
            dialogo.setTitle("Solicitud de Llamada");
            dialogo.setMessage(mensaje);
            dialogo.setCancelable(false);
            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    try {
                        EnviarMensaje(getTelCkto(), "llamame OK");
                        superclase.save_action_phone(DashboardLuces.this,"Solicitud de llamada perdida.",superclase.getTelReceptor(getApplicationContext()),superclase.getTelPropietario(getApplicationContext()),superclase.getDate(),superclase.getTime());
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

    public void EnviarMensaje(String Numero, String Mensaje) {
        try {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(Numero, null, Mensaje, null, null);
            Toast.makeText(getApplicationContext(), "Comando de control enviado.\n\nDestinatario:"+Numero, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Mensaje no enviado, datos incorrectos.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_luces_control1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.opAlluces) {
            Alluces();
            return true;
        }else if(id==R.id.opInfoluces) {
            getInfoStatusAllces();
            return true;
        }else if(id==R.id.opRetoHacerlo){
            //Showpopup();
            superclase.onoffMultipleSMS(DashboardLuces.this);
            return true;
        }else if(id==R.id.opCall){
            //getCall();
            superclase.getCall(DashboardLuces.this);
            return true;
        /*}else if(id==R.id.opAyuda){
            Toast.makeText(getApplicationContext(),"Ayuda",Toast.LENGTH_LONG).show();
            return true;*/
        }else if(id==R.id.opSalir) {
            //Toast.makeText(getApplicationContext(),"Salir",Toast.LENGTH_LONG).show();
            goMenu();
            return true;
        }
        return super.onOptionsItemSelected(item);
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


    //fin



}
