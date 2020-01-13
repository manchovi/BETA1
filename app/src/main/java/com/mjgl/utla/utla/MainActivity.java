package com.mjgl.utla.utla;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    AlertDialog.Builder dialogo1;
    AlertDialog.Builder dialogo;
    String nombrecompleto;
    Dialog myDialog;
    Button positivePopupBtn, btnAccept;
    ImageView BtnCerrar;
    boolean estado_correo;
    AlertDialog.Builder dialog;
    AlertDialog.Builder dialogo2;
    Session session = null;           //variable utilizada para envio de correo automatico.
    //String destinatario = "winmanuel07@gmail.com";
    String destinatario = Config.DESTINATARIO;
    String asunto = Config.ASUNTO;
    private ProgressDialog pd;
    ProgressDialog progressDialog;
    int tipoUser;
    private boolean habilitar;
    boolean estado_nombre;
    boolean estado_dir_api;
    boolean estado_dir_portal;
    dbSQLiteHelper admin = new dbSQLiteHelper(this);
    String f = "";
    String h = "";
    static SharedPreferences datos;
    /*
          INICIO
          REDUCIR ESTE CODIGO CON LA HERENCIA DESPUES.
     */
    int estadoAll = 0;
    int estadof1 = 0;
    int estadof2 = 0;
    int estadof3 = 0;
    int estadof4 = 0;
    int estadof5 = 0;
    int estadof6 = 0;
    int estadof7 = 0;
    int estadof8 = 0;
    ToggleButton sw0, sw1, sw2, sw3, sw4, sw5, sw6, sw7, sw8;
    Button button;
    TextView txtclose;

    ImageView f_all, f1, f2, f3, f4, f5, f6, f7, f8;

    //public MainActivity(Context context) {
    //}

    //Creando instancia para ver si funciona
    superClase superclase = new superClase();

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new android.app.AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Advertencia")
                    .setMessage("¿Está a punto de finalizar su sesión\n¿Realmente desea salir?")
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {//un listener que al pulsar, cierre la aplicacion
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            destroySesionVariables();
                            Intent closesesion = new Intent(MainActivity.this, login.class);
                            startActivity(closesesion);
                            MainActivity.this.finish();
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
        setContentView(R.layout.activity_main);

        //Las dos líneas a continuación fuerón unas pruebas exitosas que realice para enviar datos a la BD en la nube.
        //superclase.save_action_phone(getApplicationContext(),"descripcion","78483248","61107065","2018-12-14","08:28:00 a.m.");
        //Toast.makeText(getApplicationContext(),"Lo guardo....",Toast.LENGTH_LONG).show();


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        toolbar.setTitle("UTLA");
        //toolbar.setTitleMargin(0, 0, 0, 0);
        //toolbar.setSubtitle("MENÚ PRINCIPAL");
        //setSupportActionBar(toolbar);

        pd = new ProgressDialog(MainActivity.this);
        pd.setMessage("Procesando, por favor espere...");
        progressDialog = new ProgressDialog(this);

        datos = getPreferences(this.MODE_PRIVATE);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Universidad Técnica Latinoamerica\nTrabajo de Graduación 2019\nGrado: Ing. en Electrónica", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        Menu nav_Menu = navigationView.getMenu();

        navigationView.setNavigationItemSelectedListener(this);

        /*private void hideItem()
        {
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_settings).setVisible(false);
        }*/

        //Función para evitar la rotación de la pantalla del CELULAR.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //y esto para pantalla completa (oculta incluso la barra de estado)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        myDialog = new Dialog(this);

        //PONER ACA EL CODIGO DE LA ACCION QUE REALIZARA ESTE FRAGMENTO.
        Button btnConfiguracion = (Button) findViewById(R.id.btnConfiguracion);
        Button BtnBtControl = (Button) findViewById(R.id.BtnBtControl);
        Button BtnGSMControl = (Button) findViewById(R.id.BtnGSMControl);

        Button btnTempHumedad = (Button) findViewById(R.id.btnTempHumedad);
        Button btnEstadoLuces = (Button) findViewById(R.id.btnEstadoLuces);

        Button BtnGSMControl1 = (Button) findViewById(R.id.BtnGSMControl1);
        Button BtnControl_X_Voz = (Button) findViewById(R.id.BtnControl_X_Voz);

        Button BtnGraficaLinea = (Button) findViewById(R.id.BtnGraficaLinea);
        Button BtnGraficaBarra = (Button) findViewById(R.id.BtnGraficaBarra);
        Button BtnGraficaPastel = (Button) findViewById(R.id.BtnGraficaPastel);
        Button BtnRedeSociales = (Button) findViewById(R.id.BtnRedeSociales);
        Button BtnSmsTelefonoTH = (Button) findViewById(R.id.BtnSmsTelefonoTH);
        Button BtnLocalizacion = (Button) findViewById(R.id.BtnLocalizacion);
        Button BtnAcercade = (Button) findViewById(R.id.BtnAcercade);

        LinearLayout linear1 = (LinearLayout) findViewById(R.id.linear1);
        LinearLayout linear2 = (LinearLayout) findViewById(R.id.linear2);
        LinearLayout linear3 = (LinearLayout) findViewById(R.id.linear3);
        LinearLayout linear4 = (LinearLayout) findViewById(R.id.linear4);
        LinearLayout linear5 = (LinearLayout) findViewById(R.id.linear5);
        LinearLayout linear6 = (LinearLayout) findViewById(R.id.linear6);
        LinearLayout linear8 = (LinearLayout) findViewById(R.id.linear8);  //ultimos dos button

        Button btnPerfilUser = (Button) findViewById(R.id.btnPerfilUser);
        Button BtnServerSource = (Button) findViewById(R.id.BtnServerSource);

        String persona_autorizada = "";
        String codigo = "";
        String senal = "";


        //OJALA NO ME DE DESMADRE
       /* Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            String addres = extras.getString("MessageNumber");
            String message = extras.getString("Message");
            String bandera = extras.getString("Flag");
            if (bandera.equals("2")) {
                superclase.dialog_Saludo(MainActivity.this,message);
            }
        }*/
        //FIN DEL OJALA NO ME ...

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            //codigo = bundle.getString("codigo");
            senal = bundle.getString("senal");
            persona_autorizada = bundle.getString("usuario");
            //tipoUser = bundle.getInt("codigo");
            //dato.setText("Bienvenid@: " + persona_autorizada);
            final String finalPersona_autorizada = persona_autorizada;
            if (senal.equals("1")) {
                dialog = new
                        AlertDialog.Builder(MainActivity.this);
                dialog.setIcon(R.drawable.ic_done);
                dialog.setTitle("<<<< UTLA >>>>");
                dialog.setMessage("FELICIDADES!!!\nBienvenid@, " + persona_autorizada + ".\n\nSe ha identificado correctamente.");
                dialog.setCancelable(false);

                dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo, int id) {
                        //toolbar.setSubtitle(finalPersona_autorizada.toString());
                        //toolbar.setSubtitle("");
                    }
                });
                dialog.show();
            } else {
                //toolbar.setSubtitle(finalPersona_autorizada.toString());
                //toolbar.setSubtitle("");
            }
        }

        SharedPreferences preferences = getSharedPreferences("variablesesion", Context.MODE_PRIVATE);
        final int codigo1 = preferences.getInt("tipo", 1);
        nombrecompleto = preferences.getString("nombrecompleto", "NA");
        String nom = preferences.getString("nombres", "NA");
        String apel = preferences.getString("apellidos", "NA");
        String usu = preferences.getString("usuario", "NA");
        String fh = preferences.getString("fh", "NA");
        //boolean habilitar = intent.getExtras().getBoolean("habilitaOpciones");
        habilitar = preferences.getBoolean("habilitaOpciones", false);

        //toolbar.setSubtitle(nombrecompleto);
        //toolbar.setSubtitle("");
        setSupportActionBar(toolbar);

        if (codigo1 != 1) {
            /*btnConfiguracion.setVisibility(View.GONE);//btnConfiguracion.setEnabled(false);
            //btnConfiguracion.setActivated(false);
            btnPerfilUser.setVisibility(View.VISIBLE);//btnPerfilUser.setEnabled(false);
            BtnServerSource.setVisibility(View.GONE);//BtnServerSource.setEnabled(false);
            BtnBtControl.setVisibility(View.VISIBLE);//BtnBtControl.setEnabled(true);
            BtnGSMControl.setVisibility(View.GONE);  //BtnGSMControl.setEnabled(false);
            BtnGraficaLinea.setVisibility(View.GONE);//BtnGraficaLinea.setEnabled(false);
            BtnGraficaBarra.setVisibility(View.GONE);//BtnGraficaBarra.setEnabled(false);
            BtnGraficaPastel.setVisibility(View.GONE);//BtnGraficaPastel.setEnabled(false);
            BtnRedeSociales.setVisibility(View.GONE);//BtnRedeSociales.setEnabled(false);
            BtnSmsTelefonoTH.setVisibility(View.GONE);//BtnSmsTelefonoTH.setEnabled(false);
            BtnLocalizacion.setVisibility(View.VISIBLE);//BtnLocalizacion.setEnabled(true);
            BtnAcercade.setVisibility(View.VISIBLE);//BtnAcercade.setEnabled(true);*/

            linear1.setVisibility(View.GONE);
            linear3.setVisibility(View.GONE);

            //linear4.setVisibility(View.GONE);
            BtnControl_X_Voz.setVisibility(View.VISIBLE);
            BtnGraficaLinea.setVisibility(View.GONE);

            linear5.setVisibility(View.GONE);
            linear6.setVisibility(View.GONE);
            linear8.setVisibility(View.GONE);

            //BtnGSMControl1.setVisibility(View.GONE);
            //BtnControl_X_Voz.setVisibility(View.GONE);

            linear2.setVisibility(View.VISIBLE);
            //linear6.setVisibility(View.VISIBLE);

            nav_Menu.findItem(R.id.config_userMaster).setVisible(false);
            nav_Menu.findItem(R.id.control_bt).setVisible(true);
            nav_Menu.findItem(R.id.control_bt1).setVisible(true);
            nav_Menu.findItem(R.id.control_sms).setVisible(false);
            nav_Menu.findItem(R.id.control_sms1).setVisible(false);
            nav_Menu.findItem(R.id.g_linea).setVisible(false);
            nav_Menu.findItem(R.id.g_barra).setVisible(false);
            nav_Menu.findItem(R.id.g_pastel).setVisible(false);
            nav_Menu.findItem(R.id.sms_social).setVisible(false);
            nav_Menu.findItem(R.id.sms_celular).setVisible(false);
            nav_Menu.findItem(R.id.correo_automatico).setVisible(false);
            nav_Menu.findItem(R.id.ubicanos).setVisible(true);
            //nav_Menu.findItem(R.id.segundo_plano).setVisible(false);
            nav_Menu.findItem(R.id.acercade).setVisible(true);
            nav_Menu.findItem(R.id.cerrar_sesion).setVisible(true);

        } else {
            /*btnConfiguracion.setVisibility(View.VISIBLE);//btnConfiguracion.setEnabled(true);
            btnPerfilUser.setVisibility(View.VISIBLE);   //btnPerfilUser.setEnabled(true);
            BtnServerSource.setVisibility(View.VISIBLE); //BtnServerSource.setEnabled(true);
            BtnBtControl.setVisibility(View.VISIBLE);    //BtnBtControl.setEnabled(true);
            BtnGSMControl.setVisibility(View.VISIBLE);   //BtnGSMControl.setEnabled(true);
            BtnGraficaLinea.setVisibility(View.VISIBLE); //BtnGraficaLinea.setEnabled(true);
            BtnGraficaBarra.setVisibility(View.VISIBLE); //BtnGraficaBarra.setEnabled(true);
            BtnGraficaPastel.setVisibility(View.VISIBLE);//BtnGraficaPastel.setEnabled(true);
            BtnRedeSociales.setVisibility(View.VISIBLE);  //BtnRedeSociales.setEnabled(true);
            BtnSmsTelefonoTH.setVisibility(View.VISIBLE); //BtnSmsTelefonoTH.setEnabled(true);
            BtnLocalizacion.setVisibility(View.VISIBLE);  //BtnLocalizacion.setEnabled(true);
            BtnAcercade.setVisibility(View.VISIBLE);      //BtnAcercade.setEnabled(true);*/

            linear1.setVisibility(View.VISIBLE);
            linear3.setVisibility(View.VISIBLE);
            linear4.setVisibility(View.VISIBLE);
            linear5.setVisibility(View.VISIBLE);
            linear2.setVisibility(View.VISIBLE);
            linear8.setVisibility(View.VISIBLE);

            //BtnGSMControl1.setVisibility(View.VISIBLE);
            //BtnControl_X_Voz.setVisibility(View.VISIBLE);

            nav_Menu.findItem(R.id.config_userMaster).setVisible(true);
            nav_Menu.findItem(R.id.control_bt).setVisible(true);
            nav_Menu.findItem(R.id.control_bt1).setVisible(true);
            nav_Menu.findItem(R.id.control_sms).setVisible(true);
            nav_Menu.findItem(R.id.control_sms1).setVisible(true);
            nav_Menu.findItem(R.id.g_linea).setVisible(true);
            nav_Menu.findItem(R.id.g_barra).setVisible(true);
            nav_Menu.findItem(R.id.g_pastel).setVisible(true);
            nav_Menu.findItem(R.id.sms_social).setVisible(true);
            nav_Menu.findItem(R.id.sms_celular).setVisible(true);
            nav_Menu.findItem(R.id.correo_automatico).setVisible(true);
            nav_Menu.findItem(R.id.ubicanos).setVisible(true);
            //nav_Menu.findItem(R.id.segundo_plano).setVisible(true);
            nav_Menu.findItem(R.id.acercade).setVisible(true);
            nav_Menu.findItem(R.id.cerrar_sesion).setVisible(true);
        }

        //BOTON 1
        btnConfiguracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowLoginMasterDestinarios();
            }
        });

        //BOTON 2 - SERVIDOR DEL SISTEMA UTLA
        BtnServerSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                config_server();
            }
        });

        //BOTON 3 - USUARIOS REGISTRADOS EN EL SISTEMA
        btnPerfilUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListaUsuarios.class);
                intent.putExtra("codigo", codigo1);
                intent.putExtra("nombre", nombrecompleto);
                startActivity(intent);
            }
        });

        //BOTON 4 - BLUETOOTH CONTROL
        BtnBtControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity_Bluetooth_Control();
            }
        });

        //BOTON 5 - GSM CONTROL
        BtnGSMControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity_Gsm_Control();
            }
        });

        BtnGSMControl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "Pendiente la acción...", Toast.LENGTH_SHORT).show();
                //onoffMultipleSMS();
                String telckto = getTelCkto();
                if (telckto.equals("Sin configurar.")) {
                    mensaje1();
                } else {
                    superclase.onoffMultipleSMS(MainActivity.this);
                }
            }
        });


        btnEstadoLuces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInfoStatusAllces();
            }
        });


        btnTempHumedad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInfoTH();
            }
        });


        BtnControl_X_Voz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //aqui va...
                ventana_control_voz();
            }
        });

        //BOTON 6
        BtnGraficaLinea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graflinea();
            }
        });

        //BOTON 7
        BtnGraficaBarra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grafbarra();
            }
        });

        //BOTON 8
        BtnGraficaPastel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grafpastel();
            }
        });


        //BOTON 9
        BtnRedeSociales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviar_email();
            }
        });

        //BOTON 10
        BtnSmsTelefonoTH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String telckto = getTelCkto();
                if (telckto.equals("Sin configurar.")) {
                    mensaje();
                } else {
                    //confirmar();
                    superclase.confirmar(MainActivity.this);
                    //superclase.about(MainActivity.this);
                }
            }
        });

        //BOTON 11
        BtnLocalizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapa();
            }
        });

        //BOTON 12
        BtnAcercade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Showpopup();
            }
        });

        BtnAcercade.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
    }

    private void ventana_control_voz() {
        Intent intent = new Intent(getApplicationContext(), controlporvoz.class);
        startActivity(intent);
    }


    private void getInfoTH() {
        String telckto = getTelCkto();
        if (telckto.equals("Sin configurar.")) {
            mensaje();
        } else {
            String mensaje = Config.mensaje1 + "\n\"Consultar el estado de la temperatura y humedad relativa.\"";
            dialogo = new AlertDialog.Builder(MainActivity.this);
            dialogo.setIcon(R.drawable.ic_sms);
            //dialog.setTitle("COCESNA.");
            dialogo.setTitle("SMS CONTROL");
            dialogo.setMessage(mensaje);
            dialogo.setCancelable(false);
            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    try {
                        EnviarMensaje(getTelCkto(), "info temphum");
                        superclase.save_action_phone(getApplicationContext(), "Consulta Estado de Temperatura y Humedad Relativa en el Laboratorio.", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
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

    private void getInfoStatusAllces() {
        String telckto = getTelCkto();
        if (telckto.equals("Sin configurar.")) {
            mensaje();
        } else {
            String mensaje = Config.mensaje1 + "\n\"Consultar el estado de todas las luminarias.\"";
            dialogo = new AlertDialog.Builder(MainActivity.this);
            dialogo.setIcon(R.drawable.ic_sms);
            //dialog.setTitle("COCESNA.");
            dialogo.setTitle("SMS CONTROL");
            dialogo.setMessage(mensaje);
            dialogo.setCancelable(false);
            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    try {
                        EnviarMensaje(getTelCkto(), "info luceslab");
                        superclase.save_action_phone(getApplicationContext(), "Consulta Estado Actual de Luminarias en el Laboratorio", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
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


    private String infoSharedPreferences1() {
        //Buscando datos en archivo credenciales.xml
        SharedPreferences preferences = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        String server = preferences.getString("servidor", "Sin configurar aun.");
        String folder = preferences.getString("folder", "Sin configurar aun.");
        return server + folder;
    }

    public void email_automatico() {
        String comprobacion = conf_Server();
        if (comprobacion.equals("Sin configurar.")) {
            mensaje1();
        } else {
            dialogo2 = new AlertDialog.Builder(MainActivity.this);
            dialogo2.setIcon(R.drawable.ic_mail);
            //dialog.setTitle("COCESNA.");
            dialogo2.setTitle("Message to E-mail");
            dialogo2.setMessage("¿Realmente desea enviar el email?\n\n" +
                    "Recuerde: La información se enviará a la dirección e-mail que se ha especificado en la opción configuración.");
            dialogo2.setCancelable(false);
            dialogo2.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo2, int id) {
                    //aca pondre la funcion para envio de email automatico.
                    configuracion();
                    capturar_info_bd();
                }
            });
            dialogo2.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo2, int id) {
                    Toast toast = Toast.makeText(getApplicationContext(), "CANCELADO.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            });
            dialogo2.show();
        }
    }     //fin de funcion.


    public void configuracion() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                //return new PasswordAuthentication("cocesna1718@gmail.com", "cocesna_2018_");
                return new PasswordAuthentication(Config.EMAIL, Config.PASSWORD);
                //return new PasswordAuthentication("testfrom354@gmail.com", "p1234p1234");
            }
        });
    }

    private void capturar_info_bd() {
        pd.show();
        final String pc = infoSharedPreferences();

        //url = "http://" + pc +"/service/grafica55.php";
        String url = pc + "/grafica55.php";
        //String url = pc + "/service/grafica55.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("string", response);
                        // Hiding the progress dialog after all task complete.
                        //progressDialog.dismiss();
                        try {
                            //System.out.println("RESPUESTA DE SERVIDOR : "+response);
                            //Toast.makeText(grafica5.this, response, Toast.LENGTH_LONG).show();
                            JSONArray jsonarray = new JSONArray(response);
                            String cadena = "";
                            for (int i = 0; i < jsonarray.length(); i++) {
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

                                String l1 = "", l2 = "", l3 = "", l4 = "", l5 = "", l6 = "", l7 = "", l8 = "";
                                if (luz1 == 1) {
                                    l1 = "Encendido";
                                } else {
                                    l1 = "Apagado";
                                }

                                if (luz2 == 1) {
                                    l2 = "Encendido";
                                } else {
                                    l2 = "Apagado";
                                }

                                if (luz3 == 1) {
                                    l3 = "Encendido";
                                } else {
                                    l3 = "Apagado";
                                }

                                if (luz4 == 1) {
                                    l4 = "Encendido";
                                } else {
                                    l4 = "Apagado";
                                }

                                if (luz5 == 1) {
                                    l5 = "Encendido";
                                } else {
                                    l5 = "Apagado";
                                }

                                if (luz6 == 1) {
                                    l6 = "Encendido";
                                } else {
                                    l6 = "Apagado";
                                }

                                if (luz7 == 1) {
                                    l7 = "Encendido";
                                } else {
                                    l7 = "Apagado";
                                }

                                if (luz8 == 1) {
                                    l8 = "Encendido";
                                } else {
                                    l8 = "Apagado";
                                }

                                String fecha = jsonobject.getString("fecha").trim();
                                String hora = jsonobject.getString("hora").trim();

                                //Toast.makeText(MainActivity.this, "E-mail Destinatario: "+ infoConfDestinatarioEmail1(), Toast.LENGTH_SHORT).show();

                                //sendEmail(infoConfDestinatarioEmail1(), asunto, temperatura, humedad, fecha, hora);
                                sendEmail(infoConfDestinatarioEmail1(), asunto, temperatura, humedad, l1, l2, l3, l4, l5, l6, l7, l8, fecha, hora);
                                //et_T.setText(temperatura + "");
                                /*et_T.setText(String.valueOf(temperatura));
                                et_H.setText(humedad + "");*/
                                //Toast.makeText(getApplicationContext(), temperatura +"", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pd.hide();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Hiding the progress dialog after all task complete.
                //progressDialog.dismiss();
                // Showing error message if something goes wrong.
                Toast.makeText(getApplicationContext(), "Algo salió mal con la conexión al servidor\n" +
                        "Intente mas tarde.", Toast.LENGTH_LONG).show();
                pd.hide();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("senal", "1");
                return map;
            }
        };
        //requestQueue.add(request);
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private void sendEmail(String email, String subject, double temperatura, double humedad, String l1, String l2, String l3, String l4, String l5, String l6, String l7, String l8, String fecha, String hora) {

        //OBTENIENDO LA FECHA Y HORA ACTUAL DEL SISTEMA.
        DateFormat formatodate = new SimpleDateFormat("yyyy/MM/dd");
        String date = formatodate.format(new Date());

        DateFormat formatotime = new SimpleDateFormat("HH:mm:ss a");
        String time = formatotime.format(new Date());

        DecimalFormat df = new DecimalFormat("#.00");
        String message = "*************************************************************\n" +
                "Reporte de última actualización en la base de datos.\n" +
                "*************************************************************\n" +
                "\nINFORMACIÓN RECOPILADA.\n\n" +
                //"*Temperatura: " + temperatura + " °C.\n" +
                //"*Temperatura: " + temperatura + " °C.\n" +
                //"*Temperatura: " + temperatura + "°C ~ " + (((Double.parseDouble(String.valueOf(temperatura)))*1.8)+32) + " °F.\n" +
                //"*Temperatura: " + temperatura + "°C ~ " + String.format("%.2f",((((Double.parseDouble(String.valueOf(temperatura)))*1.8)+32))) + " °F.\n" +
                "*Temperatura: " + temperatura + "°C ~ " + df.format(((((Double.parseDouble(String.valueOf(temperatura)))*1.8)+32.0))) + " °F.\n" +
                "*Humedad Relativa: " + humedad + " % [R.H.].\n" +
                "*Luminaria # 1: " + l1 + "\n" +
                "*Luminaria # 2: " + l2 + "\n" +
                "*Luminaria # 3: " + l3 + "\n" +
                "*Luminaria # 4: " + l4 + "\n" +
                "*Luminaria # 5: " + l5 + "\n" +
                "*Luminaria # 6: " + l6 + "\n" +
                "*Luminaria # 7: " + l7 + "\n" +
                "*Luminaria # 8: " + l8 + "\n" +
                "\nFecha y Hora de la última actualización: " +
                "Fecha: " + fecha + " / " + hora + "\n\n" +
                "---------------------------------------------------------------------" + "\n" +
                "\t\tMensaje Generado: " + date + " ~ " + time + "\n" +
                "---------------------------------------------------------------------" + "\n" +
                "Copyright(c) UTLA 2019~2020. " +
                "\nAll rights reserved.";

        //Creating SendMail object
        SendMail sm = new SendMail(this, email, subject, message);

        //Executing sendmail to send email
        sm.execute();
    }




    /*
       INICIO
       Despues veré como reducir este código con la aplicación de la herencia.
       por cuestión de tiempo lo estoy redundando acá. Pero no es lo correcto.
     */


    public void onoffMultipleSMS() {
        //TextView txtclose;
        myDialog.setContentView(R.layout.onoffcombinado);
        myDialog.setTitle("App Creado por,");
        myDialog.setCancelable(false);

        f_all = (ImageView) myDialog.findViewById(R.id.ivFocoAll);
        f1 = (ImageView) myDialog.findViewById(R.id.ivFoco1);
        f2 = (ImageView) myDialog.findViewById(R.id.ivFoco2);
        f3 = (ImageView) myDialog.findViewById(R.id.ivFoco3);
        f4 = (ImageView) myDialog.findViewById(R.id.ivFoco4);
        f5 = (ImageView) myDialog.findViewById(R.id.ivFoco5);
        f6 = (ImageView) myDialog.findViewById(R.id.ivFoco6);
        f7 = (ImageView) myDialog.findViewById(R.id.ivFoco7);
        f8 = (ImageView) myDialog.findViewById(R.id.ivFoco8);

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

        button = (Button) myDialog.findViewById(R.id.btnAplicar);

        txtclose = (TextView) myDialog.findViewById(R.id.txtclose);

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
                    estadof1 = 1;
                    estadof2 = 1;
                    estadof3 = 1;
                    estadof4 = 1;
                    estadof5 = 1;
                    estadof6 = 1;
                    estadof7 = 1;
                    estadof8 = 1;
                    //sw0.setChecked(true);
                    sw1.setChecked(true);
                    sw2.setChecked(true);
                    sw3.setChecked(true);
                    sw4.setChecked(true);
                    sw5.setChecked(true);
                    sw6.setChecked(true);
                    sw7.setChecked(true);
                    sw8.setChecked(true);

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
                    estadof1 = 0;
                    estadof2 = 0;
                    estadof3 = 0;
                    estadof4 = 0;
                    estadof5 = 0;
                    estadof6 = 0;
                    estadof7 = 0;
                    estadof8 = 0;
                    //sw0.setChecked(true);
                    sw1.setChecked(false);
                    sw2.setChecked(false);
                    sw3.setChecked(false);
                    sw4.setChecked(false);
                    sw5.setChecked(false);
                    sw6.setChecked(false);
                    sw7.setChecked(false);
                    sw8.setChecked(false);

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
                if (estadof1 == 0 && estadof2 == 0 && estadof3 == 0 && estadof4 == 0 && estadof5 == 0 && estadof6 == 0 && estadof7 == 0 && estadof8 == 0) {
                    //universal("utla00000000");
                    //setStatusDefault();
                    Toast.makeText(getApplicationContext(), "No ha especificado un cambio válido.\n\n" +
                            "Debe especificar al menos un cambio de estado de luminaria para que se envie" +
                            " la orden al sistema de control.", Toast.LENGTH_SHORT).show();
                }

                //uno
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 0 && estadof5 == 0 && estadof4 == 0 && estadof3 == 0 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla00000001");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-0-0-0-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //dos
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 0 && estadof5 == 0 && estadof4 == 0 && estadof3 == 0 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla00000010");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-0-0-0-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //tres
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 0 && estadof5 == 0 && estadof4 == 0 && estadof3 == 0 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla00000011");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-0-0-0-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //cuatro
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 0 && estadof5 == 0 && estadof4 == 0 && estadof3 == 1 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla00000100");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-0-0-1-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //cinco
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 0 && estadof5 == 0 && estadof4 == 0 && estadof3 == 1 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla00000101");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-0-0-1-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //seis
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 0 && estadof5 == 0 && estadof4 == 0 && estadof3 == 1 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla00000110");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-0-0-1-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //siete
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 0 && estadof5 == 0 && estadof4 == 0 && estadof3 == 1 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla00000111");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-0-0-1-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //ocho
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 0 && estadof5 == 0 && estadof4 == 1 && estadof3 == 0 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla00001000");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-0-1-0-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //nueve
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 0 && estadof5 == 0 && estadof4 == 1 && estadof3 == 0 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla00001001");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-0-1-0-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //dies
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 0 && estadof5 == 0 && estadof4 == 1 && estadof3 == 0 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla00001010");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-0-1-0-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //11
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 0 && estadof5 == 0 && estadof4 == 1 && estadof3 == 0 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla00001011");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-0-1-0-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //12
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 0 && estadof5 == 0 && estadof4 == 1 && estadof3 == 1 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla00001100");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-0-1-1-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //13
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 0 && estadof5 == 0 && estadof4 == 1 && estadof3 == 1 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla00001101");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-0-1-1-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //14
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 0 && estadof5 == 0 && estadof4 == 1 && estadof3 == 1 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla00001110");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-0-1-1-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //15
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 0 && estadof5 == 0 && estadof4 == 1 && estadof3 == 1 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla00001111");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-0-1-1-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //16
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 0 && estadof5 == 1 && estadof4 == 0 && estadof3 == 0 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla00010000");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-1-0-0-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //17
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 0 && estadof5 == 1 && estadof4 == 0 && estadof3 == 0 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla00010001");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-1-0-0-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //18
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 0 && estadof5 == 1 && estadof4 == 0 && estadof3 == 0 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla00010010");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-1-0-0-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //19
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 0 && estadof5 == 1 && estadof4 == 0 && estadof3 == 0 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla00010011");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-1-0-0-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //20
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 0 && estadof5 == 1 && estadof4 == 0 && estadof3 == 1 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla00010100");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-1-0-1-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //21
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 0 && estadof5 == 1 && estadof4 == 0 && estadof3 == 1 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla00010101");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-1-0-1-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //22
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 0 && estadof5 == 1 && estadof4 == 0 && estadof3 == 1 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla00010110");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-1-0-1-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //23
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 0 && estadof5 == 1 && estadof4 == 0 && estadof3 == 1 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla00010111");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-1-0-1-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //24
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 0 && estadof5 == 1 && estadof4 == 1 && estadof3 == 0 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla00011000");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-1-1-0-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //25
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 0 && estadof5 == 1 && estadof4 == 1 && estadof3 == 0 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla00011001");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-1-1-0-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //26
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 0 && estadof5 == 1 && estadof4 == 1 && estadof3 == 0 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla00011010");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-1-1-0-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //27
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 0 && estadof5 == 1 && estadof4 == 1 && estadof3 == 0 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla00011011");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-1-1-0-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //28
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 0 && estadof5 == 1 && estadof4 == 1 && estadof3 == 1 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla00011100");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-1-1-1-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //29
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 0 && estadof5 == 1 && estadof4 == 1 && estadof3 == 1 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla00011101");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-1-1-1-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //30
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 0 && estadof5 == 1 && estadof4 == 1 && estadof3 == 1 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla00011110");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-1-1-1-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //31
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 0 && estadof5 == 1 && estadof4 == 1 && estadof3 == 1 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla00011111");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-0-1-1-1-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //32
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 1 && estadof5 == 0 && estadof4 == 0 && estadof3 == 0 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla00100000");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-0-0-0-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //33
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 1 && estadof5 == 0 && estadof4 == 0 && estadof3 == 0 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla00100001");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-0-0-0-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //34
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 1 && estadof5 == 0 && estadof4 == 0 && estadof3 == 0 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla00100010");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-0-0-0-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //35
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 1 && estadof5 == 0 && estadof4 == 0 && estadof3 == 0 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla00100011");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-0-0-0-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //36
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 1 && estadof5 == 0 && estadof4 == 0 && estadof3 == 1 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla00100100");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-0-0-1-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //37
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 1 && estadof5 == 0 && estadof4 == 0 && estadof3 == 1 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla00100101");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-0-0-1-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //38
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 1 && estadof5 == 0 && estadof4 == 0 && estadof3 == 1 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla00100110");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-0-0-1-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //39
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 1 && estadof5 == 0 && estadof4 == 0 && estadof3 == 1 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla00100111");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-0-0-1-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //40
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 1 && estadof5 == 0 && estadof4 == 1 && estadof3 == 0 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla00101000");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-0-1-0-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //41
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 1 && estadof5 == 0 && estadof4 == 1 && estadof3 == 0 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla00101001");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-0-1-0-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //42
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 1 && estadof5 == 0 && estadof4 == 1 && estadof3 == 0 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla00101010");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-0-1-0-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //43
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 1 && estadof5 == 0 && estadof4 == 1 && estadof3 == 0 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla00101011");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-0-1-0-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //44
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 1 && estadof5 == 0 && estadof4 == 1 && estadof3 == 1 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla00101100");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-0-1-1-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //45
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 1 && estadof5 == 0 && estadof4 == 1 && estadof3 == 1 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla00101101");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-0-1-1-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //46
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 1 && estadof5 == 0 && estadof4 == 1 && estadof3 == 1 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla00101110");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-0-1-1-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //47
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 1 && estadof5 == 0 && estadof4 == 1 && estadof3 == 1 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla00101111");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-0-1-1-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //48
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 1 && estadof5 == 1 && estadof4 == 0 && estadof3 == 0 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla00110000");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-1-0-0-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //49
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 1 && estadof5 == 1 && estadof4 == 0 && estadof3 == 0 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla00110001");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-1-0-0-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //50
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 1 && estadof5 == 1 && estadof4 == 0 && estadof3 == 0 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla00110010");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-1-0-0-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //51
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 1 && estadof5 == 1 && estadof4 == 0 && estadof3 == 0 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla00110011");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-1-0-0-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //52
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 1 && estadof5 == 1 && estadof4 == 0 && estadof3 == 1 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla00110100");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-1-0-1-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //53
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 1 && estadof5 == 1 && estadof4 == 0 && estadof3 == 1 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla00110101");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-1-0-1-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //54
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 1 && estadof5 == 1 && estadof4 == 0 && estadof3 == 1 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla00110110");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-1-0-1-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //55
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 1 && estadof5 == 1 && estadof4 == 0 && estadof3 == 1 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla00110111");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-1-0-1-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //56
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 1 && estadof5 == 1 && estadof4 == 1 && estadof3 == 0 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla00111000");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-1-1-0-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //57
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 1 && estadof5 == 1 && estadof4 == 1 && estadof3 == 0 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla00111001");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-1-1-0-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //58
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 1 && estadof5 == 1 && estadof4 == 1 && estadof3 == 0 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla00111010");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-1-1-0-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //59
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 1 && estadof5 == 1 && estadof4 == 1 && estadof3 == 0 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla00111011");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-1-1-0-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //60
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 1 && estadof5 == 1 && estadof4 == 1 && estadof3 == 1 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla00111100");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-1-1-1-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //61
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 1 && estadof5 == 1 && estadof4 == 1 && estadof3 == 1 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla00111101");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-1-1-1-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //62
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 1 && estadof5 == 1 && estadof4 == 1 && estadof3 == 1 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla00111110");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-1-1-1-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //63
                if (estadof8 == 0 && estadof7 == 0 && estadof6 == 1 && estadof5 == 1 && estadof4 == 1 && estadof3 == 1 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla00111111");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-0-1-1-1-1-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //64
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 0 && estadof5 == 0 && estadof4 == 0 && estadof3 == 0 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla01000000");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-0-0-0-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //65
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 0 && estadof5 == 0 && estadof4 == 0 && estadof3 == 0 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla01000001");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-0-0-0-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //66
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 0 && estadof5 == 0 && estadof4 == 0 && estadof3 == 0 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla01000010");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-0-0-0-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //67
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 0 && estadof5 == 0 && estadof4 == 0 && estadof3 == 0 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla01000011");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-0-0-0-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //68
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 0 && estadof5 == 0 && estadof4 == 0 && estadof3 == 1 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla01000100");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-0-0-1-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //69
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 0 && estadof5 == 0 && estadof4 == 0 && estadof3 == 1 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla01000101");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-0-0-1-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //70
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 0 && estadof5 == 0 && estadof4 == 0 && estadof3 == 1 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla01000110");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-0-0-1-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //71
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 0 && estadof5 == 0 && estadof4 == 0 && estadof3 == 1 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla01000111");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-0-0-1-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //72
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 0 && estadof5 == 0 && estadof4 == 1 && estadof3 == 0 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla01001000");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-0-1-0-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //73
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 0 && estadof5 == 0 && estadof4 == 1 && estadof3 == 0 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla01001001");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-0-1-0-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //74
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 0 && estadof5 == 0 && estadof4 == 1 && estadof3 == 0 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla01001010");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-0-1-0-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //75
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 0 && estadof5 == 0 && estadof4 == 1 && estadof3 == 0 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla01001011");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-0-1-0-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //76
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 0 && estadof5 == 0 && estadof4 == 1 && estadof3 == 1 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla01001100");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-0-1-1-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //77
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 0 && estadof5 == 0 && estadof4 == 1 && estadof3 == 1 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla01001101");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-0-1-1-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //78
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 0 && estadof5 == 0 && estadof4 == 1 && estadof3 == 1 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla01001110");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-0-1-1-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //79
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 0 && estadof5 == 0 && estadof4 == 1 && estadof3 == 1 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla01001111");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-0-1-1-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //80
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 0 && estadof5 == 1 && estadof4 == 0 && estadof3 == 0 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla01010000");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-1-0-0-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //81
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 0 && estadof5 == 1 && estadof4 == 0 && estadof3 == 0 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla01010001");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-1-0-0-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //82
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 0 && estadof5 == 1 && estadof4 == 0 && estadof3 == 0 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla01010010");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-1-0-0-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //83
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 0 && estadof5 == 1 && estadof4 == 0 && estadof3 == 0 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla01010011");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-1-0-0-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //84
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 0 && estadof5 == 1 && estadof4 == 0 && estadof3 == 1 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla01010100");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-1-0-1-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //85
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 0 && estadof5 == 1 && estadof4 == 0 && estadof3 == 1 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla01010101");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-1-0-1-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //86
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 0 && estadof5 == 1 && estadof4 == 0 && estadof3 == 1 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla01010110");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-1-0-1-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //87
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 0 && estadof5 == 1 && estadof4 == 0 && estadof3 == 1 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla01010111");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-1-0-1-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //88
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 0 && estadof5 == 1 && estadof4 == 1 && estadof3 == 0 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla01011000");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-1-1-0-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //89
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 0 && estadof5 == 1 && estadof4 == 1 && estadof3 == 0 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla01011001");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-1-1-0-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //90
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 0 && estadof5 == 1 && estadof4 == 1 && estadof3 == 0 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla01011010");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-1-1-0-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //91
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 0 && estadof5 == 1 && estadof4 == 1 && estadof3 == 0 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla01011011");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-1-1-0-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //92
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 0 && estadof5 == 1 && estadof4 == 1 && estadof3 == 1 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla01011100");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-1-1-1-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //93
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 0 && estadof5 == 1 && estadof4 == 1 && estadof3 == 1 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla01011101");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-1-1-1-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //94
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 0 && estadof5 == 1 && estadof4 == 1 && estadof3 == 1 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla01011110");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-1-1-1-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //95
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 0 && estadof5 == 1 && estadof4 == 1 && estadof3 == 1 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla01011111");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-0-1-1-1-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //96
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 1 && estadof5 == 0 && estadof4 == 0 && estadof3 == 0 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla01100000");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-0-0-0-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //97
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 1 && estadof5 == 0 && estadof4 == 0 && estadof3 == 0 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla01100001");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-0-0-0-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //98
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 1 && estadof5 == 0 && estadof4 == 0 && estadof3 == 0 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla01100010");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-0-0-0-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //99
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 1 && estadof5 == 0 && estadof4 == 0 && estadof3 == 0 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla01100011");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-0-0-0-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //100
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 1 && estadof5 == 0 && estadof4 == 0 && estadof3 == 1 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla01100100");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-0-0-1-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //101
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 1 && estadof5 == 0 && estadof4 == 0 && estadof3 == 1 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla01100101");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-0-0-1-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //102
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 1 && estadof5 == 0 && estadof4 == 0 && estadof3 == 1 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla01100110");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-0-0-1-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //103
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 1 && estadof5 == 0 && estadof4 == 0 && estadof3 == 1 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla01100111");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-0-0-1-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //104
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 1 && estadof5 == 0 && estadof4 == 1 && estadof3 == 0 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla01101000");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-0-1-0-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //105
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 1 && estadof5 == 0 && estadof4 == 1 && estadof3 == 0 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla01101001");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-0-1-0-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //106
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 1 && estadof5 == 0 && estadof4 == 1 && estadof3 == 0 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla01101010");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-0-1-0-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //107
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 1 && estadof5 == 0 && estadof4 == 1 && estadof3 == 0 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla01101011");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-0-1-0-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //108
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 1 && estadof5 == 0 && estadof4 == 1 && estadof3 == 1 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla01101100");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-0-1-1-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //109
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 1 && estadof5 == 0 && estadof4 == 1 && estadof3 == 1 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla01101101");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-0-1-1-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //110
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 1 && estadof5 == 0 && estadof4 == 1 && estadof3 == 1 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla01101110");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-0-1-1-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //111
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 1 && estadof5 == 0 && estadof4 == 1 && estadof3 == 1 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla01101111");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-0-1-1-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //112
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 1 && estadof5 == 1 && estadof4 == 0 && estadof3 == 0 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla01110000");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-1-0-0-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //113
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 1 && estadof5 == 1 && estadof4 == 0 && estadof3 == 0 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla01110001");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-1-0-0-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //114
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 1 && estadof5 == 1 && estadof4 == 0 && estadof3 == 0 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla01110010");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-1-0-0-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //115
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 1 && estadof5 == 1 && estadof4 == 0 && estadof3 == 0 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla01110011");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-1-0-0-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //116
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 1 && estadof5 == 1 && estadof4 == 0 && estadof3 == 1 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla01110100");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-1-0-1-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //117
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 1 && estadof5 == 1 && estadof4 == 0 && estadof3 == 1 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla01110101");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-1-0-1-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //118
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 1 && estadof5 == 1 && estadof4 == 0 && estadof3 == 1 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla01110110");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-1-0-1-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //119
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 1 && estadof5 == 1 && estadof4 == 0 && estadof3 == 1 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla01110111");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-1-0-1-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //120
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 1 && estadof5 == 1 && estadof4 == 1 && estadof3 == 0 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla01111000");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-1-1-0-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //121
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 1 && estadof5 == 1 && estadof4 == 1 && estadof3 == 0 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla01111001");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-1-1-0-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //122
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 1 && estadof5 == 1 && estadof4 == 1 && estadof3 == 0 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla01111010");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-1-1-0-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //123
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 1 && estadof5 == 1 && estadof4 == 1 && estadof3 == 0 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla01111011");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-1-1-0-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //124
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 1 && estadof5 == 1 && estadof4 == 1 && estadof3 == 1 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla01111100");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-1-1-1-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //125
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 1 && estadof5 == 1 && estadof4 == 1 && estadof3 == 1 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla01111101");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-1-1-1-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //126
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 1 && estadof5 == 1 && estadof4 == 1 && estadof3 == 1 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla01111110");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-1-1-1-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //127
                if (estadof8 == 0 && estadof7 == 1 && estadof6 == 1 && estadof5 == 1 && estadof4 == 1 && estadof3 == 1 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla01111111");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 0-1-1-1-1-1-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //128
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 0 && estadof5 == 0 && estadof4 == 0 && estadof3 == 0 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla10000000");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-0-0-0-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //129
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 0 && estadof5 == 0 && estadof4 == 0 && estadof3 == 0 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla10000001");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-0-0-0-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //130
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 0 && estadof5 == 0 && estadof4 == 0 && estadof3 == 0 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla10000010");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-0-0-0-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //131
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 0 && estadof5 == 0 && estadof4 == 0 && estadof3 == 0 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla10000011");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-0-0-0-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //132
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 0 && estadof5 == 0 && estadof4 == 0 && estadof3 == 1 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla10000100");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-0-0-1-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //133
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 0 && estadof5 == 0 && estadof4 == 0 && estadof3 == 1 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla10000101");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-0-0-1-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //134
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 0 && estadof5 == 0 && estadof4 == 0 && estadof3 == 1 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla10000110");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-0-0-1-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //135
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 0 && estadof5 == 0 && estadof4 == 0 && estadof3 == 1 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla10000111");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-0-0-1-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //136
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 0 && estadof5 == 0 && estadof4 == 1 && estadof3 == 0 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla10001000");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-0-1-0-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //137
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 0 && estadof5 == 0 && estadof4 == 1 && estadof3 == 0 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla10001001");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-0-1-0-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //138
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 0 && estadof5 == 0 && estadof4 == 1 && estadof3 == 0 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla10001010");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-0-1-0-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //139
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 0 && estadof5 == 0 && estadof4 == 1 && estadof3 == 0 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla10001011");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-0-1-0-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //140
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 0 && estadof5 == 0 && estadof4 == 1 && estadof3 == 1 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla10001100");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-0-1-1-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //141
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 0 && estadof5 == 0 && estadof4 == 1 && estadof3 == 1 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla10001101");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-0-1-1-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //142
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 0 && estadof5 == 0 && estadof4 == 1 && estadof3 == 1 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla10001110");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-0-1-1-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //143
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 0 && estadof5 == 0 && estadof4 == 1 && estadof3 == 1 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla10001111");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-0-1-1-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //144
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 0 && estadof5 == 1 && estadof4 == 0 && estadof3 == 0 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla10010000");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-1-0-0-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //145
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 0 && estadof5 == 1 && estadof4 == 0 && estadof3 == 0 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla10010001");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-1-0-0-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //146
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 0 && estadof5 == 1 && estadof4 == 0 && estadof3 == 0 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla10010010");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-1-0-0-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //147
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 0 && estadof5 == 1 && estadof4 == 0 && estadof3 == 0 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla10010011");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-1-0-0-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //148
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 0 && estadof5 == 1 && estadof4 == 0 && estadof3 == 1 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla10010100");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-1-0-1-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //149
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 0 && estadof5 == 1 && estadof4 == 0 && estadof3 == 1 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla10010101");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-1-0-1-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //150
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 0 && estadof5 == 1 && estadof4 == 0 && estadof3 == 1 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla10010110");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-1-0-1-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //151
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 0 && estadof5 == 1 && estadof4 == 0 && estadof3 == 1 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla10010111");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-1-0-1-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //152
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 0 && estadof5 == 1 && estadof4 == 1 && estadof3 == 0 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla10011000");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-1-1-0-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //153
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 0 && estadof5 == 1 && estadof4 == 1 && estadof3 == 0 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla10011001");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-1-1-0-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //154
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 0 && estadof5 == 1 && estadof4 == 1 && estadof3 == 0 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla10011010");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-1-1-0-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //155
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 0 && estadof5 == 1 && estadof4 == 1 && estadof3 == 0 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla10011011");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-1-1-0-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //156
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 0 && estadof5 == 1 && estadof4 == 1 && estadof3 == 1 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla10011100");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-1-1-1-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //157
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 0 && estadof5 == 1 && estadof4 == 1 && estadof3 == 1 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla10011101");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-1-1-1-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //158
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 0 && estadof5 == 1 && estadof4 == 1 && estadof3 == 1 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla10011110");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-1-1-1-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //159
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 0 && estadof5 == 1 && estadof4 == 1 && estadof3 == 1 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla10011111");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-0-1-1-1-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //160
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 1 && estadof5 == 0 && estadof4 == 0 && estadof3 == 0 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla10100000");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-0-0-0-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //161
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 1 && estadof5 == 0 && estadof4 == 0 && estadof3 == 0 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla10100001");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-0-0-0-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //162
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 1 && estadof5 == 0 && estadof4 == 0 && estadof3 == 0 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla10100010");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-0-0-0-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //163
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 1 && estadof5 == 0 && estadof4 == 0 && estadof3 == 0 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla10100011");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-0-0-0-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //164
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 1 && estadof5 == 0 && estadof4 == 0 && estadof3 == 1 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla10100100");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-0-0-1-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //165
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 1 && estadof5 == 0 && estadof4 == 0 && estadof3 == 1 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla10100101");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-0-0-1-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //166
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 1 && estadof5 == 0 && estadof4 == 0 && estadof3 == 1 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla10100110");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-0-0-1-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //167
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 1 && estadof5 == 0 && estadof4 == 0 && estadof3 == 1 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla10100111");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-0-0-1-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //168
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 1 && estadof5 == 0 && estadof4 == 1 && estadof3 == 0 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla10101000");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-0-1-0-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //169
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 1 && estadof5 == 0 && estadof4 == 1 && estadof3 == 0 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla10101001");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-0-1-0-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //170
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 1 && estadof5 == 0 && estadof4 == 1 && estadof3 == 0 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla10101010");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-0-1-0-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //171
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 1 && estadof5 == 0 && estadof4 == 1 && estadof3 == 0 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla10101011");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-0-1-0-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //172
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 1 && estadof5 == 0 && estadof4 == 1 && estadof3 == 1 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla10101100");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-0-1-1-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //173
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 1 && estadof5 == 0 && estadof4 == 1 && estadof3 == 1 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla10101101");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-0-1-1-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //174
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 1 && estadof5 == 0 && estadof4 == 1 && estadof3 == 1 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla10101110");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-0-1-1-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //175
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 1 && estadof5 == 0 && estadof4 == 1 && estadof3 == 1 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla10101111");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-0-1-1-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //176
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 1 && estadof5 == 1 && estadof4 == 0 && estadof3 == 0 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla10110000");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-1-0-0-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //177
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 1 && estadof5 == 1 && estadof4 == 0 && estadof3 == 0 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla10110001");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-1-0-0-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //178
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 1 && estadof5 == 1 && estadof4 == 0 && estadof3 == 0 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla10110010");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-1-0-0-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //179
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 1 && estadof5 == 1 && estadof4 == 0 && estadof3 == 0 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla10110011");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-1-0-0-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //180
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 1 && estadof5 == 1 && estadof4 == 0 && estadof3 == 1 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla10110100");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-1-0-1-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //181
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 1 && estadof5 == 1 && estadof4 == 0 && estadof3 == 1 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla10110101");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-1-0-1-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //182
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 1 && estadof5 == 1 && estadof4 == 0 && estadof3 == 1 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla10110110");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-1-0-1-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //183
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 1 && estadof5 == 1 && estadof4 == 0 && estadof3 == 1 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla10110111");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-1-0-1-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //184
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 1 && estadof5 == 1 && estadof4 == 1 && estadof3 == 0 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla10111000");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-1-1-0-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //185
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 1 && estadof5 == 1 && estadof4 == 1 && estadof3 == 0 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla10111001");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-1-1-0-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //186
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 1 && estadof5 == 1 && estadof4 == 1 && estadof3 == 0 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla10111010");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-1-1-0-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //187
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 1 && estadof5 == 1 && estadof4 == 1 && estadof3 == 0 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla10111011");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-1-1-0-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //188
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 1 && estadof5 == 1 && estadof4 == 1 && estadof3 == 1 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla10111100");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-1-1-1-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //189
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 1 && estadof5 == 1 && estadof4 == 1 && estadof3 == 1 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla10111101");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-1-1-1-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //190
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 1 && estadof5 == 1 && estadof4 == 1 && estadof3 == 1 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla10111110");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-1-1-1-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //191
                if (estadof8 == 1 && estadof7 == 0 && estadof6 == 1 && estadof5 == 1 && estadof4 == 1 && estadof3 == 1 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla10111111");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-0-1-1-1-1-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //192
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 0 && estadof5 == 0 && estadof4 == 0 && estadof3 == 0 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla11000000");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-0-0-0-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //193
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 0 && estadof5 == 0 && estadof4 == 0 && estadof3 == 0 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla11000001");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-0-0-0-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //194
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 0 && estadof5 == 0 && estadof4 == 0 && estadof3 == 0 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla11000010");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-0-0-0-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //195
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 0 && estadof5 == 0 && estadof4 == 0 && estadof3 == 0 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla11000011");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-0-0-0-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //196
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 0 && estadof5 == 0 && estadof4 == 0 && estadof3 == 1 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla11000100");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-0-0-1-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //197
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 0 && estadof5 == 0 && estadof4 == 0 && estadof3 == 1 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla11000101");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-0-0-1-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //198
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 0 && estadof5 == 0 && estadof4 == 0 && estadof3 == 1 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla11000110");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-0-0-1-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //199
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 0 && estadof5 == 0 && estadof4 == 0 && estadof3 == 1 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla11000111");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-0-0-1-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //200
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 0 && estadof5 == 0 && estadof4 == 1 && estadof3 == 0 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla11001000");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-0-1-0-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //201
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 0 && estadof5 == 0 && estadof4 == 1 && estadof3 == 0 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla11001001");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-0-1-0-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //202
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 0 && estadof5 == 0 && estadof4 == 1 && estadof3 == 0 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla11001010");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-0-1-0-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //203
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 0 && estadof5 == 0 && estadof4 == 1 && estadof3 == 0 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla11001011");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-0-1-0-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //204
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 0 && estadof5 == 0 && estadof4 == 1 && estadof3 == 1 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla11001100");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-0-1-1-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //205
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 0 && estadof5 == 0 && estadof4 == 1 && estadof3 == 1 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla11001101");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-0-1-1-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //206
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 0 && estadof5 == 0 && estadof4 == 1 && estadof3 == 1 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla11001110");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-0-1-1-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //207
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 0 && estadof5 == 0 && estadof4 == 1 && estadof3 == 1 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla11001111");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-0-1-1-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //208
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 0 && estadof5 == 1 && estadof4 == 0 && estadof3 == 0 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla11010000");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-1-0-0-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //209
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 0 && estadof5 == 1 && estadof4 == 0 && estadof3 == 0 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla11010001");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-1-0-0-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //210
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 0 && estadof5 == 1 && estadof4 == 0 && estadof3 == 0 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla11010010");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-1-0-0-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //211
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 0 && estadof5 == 1 && estadof4 == 0 && estadof3 == 0 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla11010011");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-1-0-0-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //212
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 0 && estadof5 == 1 && estadof4 == 0 && estadof3 == 1 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla11010100");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-1-0-1-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //213
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 0 && estadof5 == 1 && estadof4 == 0 && estadof3 == 1 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla11010101");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-1-0-1-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //214
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 0 && estadof5 == 1 && estadof4 == 0 && estadof3 == 1 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla11010110");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-1-0-1-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //215
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 0 && estadof5 == 1 && estadof4 == 0 && estadof3 == 1 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla11010111");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-1-0-1-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //216
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 0 && estadof5 == 1 && estadof4 == 1 && estadof3 == 0 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla11011000");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-1-1-0-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //217
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 0 && estadof5 == 1 && estadof4 == 1 && estadof3 == 0 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla11011001");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-1-1-0-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //218
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 0 && estadof5 == 1 && estadof4 == 1 && estadof3 == 0 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla11011010");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-1-1-0-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //219
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 0 && estadof5 == 1 && estadof4 == 1 && estadof3 == 0 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla11011011");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-1-1-0-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //220
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 0 && estadof5 == 1 && estadof4 == 1 && estadof3 == 1 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla11011100");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-1-1-1-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //221
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 0 && estadof5 == 1 && estadof4 == 1 && estadof3 == 1 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla11011101");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-1-1-1-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //222
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 0 && estadof5 == 1 && estadof4 == 1 && estadof3 == 1 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla11011110");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-1-1-1-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //223
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 0 && estadof5 == 1 && estadof4 == 1 && estadof3 == 1 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla11011111");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-0-1-1-1-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //224
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 1 && estadof5 == 0 && estadof4 == 0 && estadof3 == 0 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla11100000");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-0-0-0-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //225
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 1 && estadof5 == 0 && estadof4 == 0 && estadof3 == 0 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla11100001");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-0-0-0-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //226
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 1 && estadof5 == 0 && estadof4 == 0 && estadof3 == 0 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla11100010");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-0-0-0-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //227
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 1 && estadof5 == 0 && estadof4 == 0 && estadof3 == 0 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla11100011");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-0-0-0-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //228
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 1 && estadof5 == 0 && estadof4 == 0 && estadof3 == 1 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla11100100");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-0-0-1-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //229
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 1 && estadof5 == 0 && estadof4 == 0 && estadof3 == 1 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla11100101");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-0-0-1-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //230
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 1 && estadof5 == 0 && estadof4 == 0 && estadof3 == 1 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla11100110");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-0-0-1-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //231
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 1 && estadof5 == 0 && estadof4 == 0 && estadof3 == 1 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla11100111");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-0-0-1-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //232
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 1 && estadof5 == 0 && estadof4 == 1 && estadof3 == 0 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla11101000");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-0-1-0-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //233
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 1 && estadof5 == 0 && estadof4 == 1 && estadof3 == 0 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla11101001");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-0-1-0-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //234
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 1 && estadof5 == 0 && estadof4 == 1 && estadof3 == 0 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla11101010");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-0-1-0-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //235
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 1 && estadof5 == 0 && estadof4 == 1 && estadof3 == 0 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla11101011");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-0-1-0-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //236
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 1 && estadof5 == 0 && estadof4 == 1 && estadof3 == 1 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla11101100");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-0-1-1-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //237
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 1 && estadof5 == 0 && estadof4 == 1 && estadof3 == 1 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla11101101");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-0-1-1-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //238
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 1 && estadof5 == 0 && estadof4 == 1 && estadof3 == 1 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla11101110");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-0-1-1-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //239
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 1 && estadof5 == 0 && estadof4 == 1 && estadof3 == 1 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla11101111");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-0-1-1-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //240
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 1 && estadof5 == 1 && estadof4 == 0 && estadof3 == 0 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla11110000");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-1-0-0-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //241
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 1 && estadof5 == 1 && estadof4 == 0 && estadof3 == 0 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla11110001");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-1-0-0-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //242
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 1 && estadof5 == 1 && estadof4 == 0 && estadof3 == 0 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla11110010");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-1-0-0-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //243
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 1 && estadof5 == 1 && estadof4 == 0 && estadof3 == 0 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla11110011");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-1-0-0-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //244
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 1 && estadof5 == 1 && estadof4 == 0 && estadof3 == 1 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla11110100");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-1-0-1-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //245
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 1 && estadof5 == 1 && estadof4 == 0 && estadof3 == 1 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla11110101");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-1-0-1-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //246
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 1 && estadof5 == 1 && estadof4 == 0 && estadof3 == 1 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla11110110");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-1-0-1-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //247
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 1 && estadof5 == 1 && estadof4 == 0 && estadof3 == 1 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla11110111");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-1-0-1-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //248
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 1 && estadof5 == 1 && estadof4 == 1 && estadof3 == 0 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla11111000");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-1-1-0-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //249
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 1 && estadof5 == 1 && estadof4 == 1 && estadof3 == 0 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla11111001");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-1-1-0-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //250
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 1 && estadof5 == 1 && estadof4 == 1 && estadof3 == 0 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla11111010");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-1-1-0-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //251
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 1 && estadof5 == 1 && estadof4 == 1 && estadof3 == 0 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla11111011");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-1-1-0-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //252
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 1 && estadof5 == 1 && estadof4 == 1 && estadof3 == 1 && estadof2 == 0 && estadof1 == 0) {
                    universal("utla11111100");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-1-1-1-0-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //253
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 1 && estadof5 == 1 && estadof4 == 1 && estadof3 == 1 && estadof2 == 0 && estadof1 == 1) {
                    universal("utla11111101");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-1-1-1-0-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //254
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 1 && estadof5 == 1 && estadof4 == 1 && estadof3 == 1 && estadof2 == 1 && estadof1 == 0) {
                    universal("utla11111110");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-1-1-1-1-0\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }

                //255
                if (estadof8 == 1 && estadof7 == 1 && estadof6 == 1 && estadof5 == 1 && estadof4 == 1 && estadof3 == 1 && estadof2 == 1 && estadof1 == 1) {
                    universal("utla11111111");
                    save_action_phone("L8-L7-L6-L5-L4-L3-L2-L1\n 1-1-1-1-1-1-1-1\nCon confirmación", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                    setStatusDefault();
                }
                //myDialog.dismiss();
            }
        });
    }


    private void getCall() {
        String telckto = getTelCkto();
        if (telckto.equals("Sin configurar.")) {
            mensaje();
        } else {
            String mensaje = "Confirme si ¿Realmente desea recibir una llamada del sistema? \n" +
                    "Recuerde que el sistema le podra llamar si se cumple lo siguiente:\n" +
                    "\n * Estar debidamente alimentado y encendido." +
                    "\n * Exista señal de cobertura en este momento." +
                    "\n * Se posea saldo en el chip instalado en el sistema.\n";
            dialogo = new AlertDialog.Builder(MainActivity.this);
            dialogo.setIcon(R.drawable.ic_call);
            //dialog.setTitle("COCESNA.");
            dialogo.setTitle("Solicitud de Call.");
            dialogo.setMessage(mensaje);
            dialogo.setCancelable(false);
            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    try {
                        EnviarMensaje(getTelCkto(), "llamame OK");
                        superclase.save_action_phone(getApplicationContext(), "Solicitud de llamada perdida.", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
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


    public void universal1(final String contenido_sms, final String title, final String Message, final String mensaje_bd) {
        /*Toast.makeText(getApplicationContext(), "Status Flag:"+estadof8+estadof7+estadof6+estadof5+estadof4+estadof3+estadof2+estadof1+
                "\n\nDato:"+mensaje1, Toast.LENGTH_SHORT).show();*/
        String telckto = getTelCkto();
        if (telckto.equals("Sin configurar.")) {
            mensaje();
        } else {
            dialogo = new AlertDialog.Builder(MainActivity.this);
            dialogo.setIcon(R.drawable.ic_sms);
            //dialog.setTitle("COCESNA.");
            //dialogo.setTitle("Solicitud SMS Entrante");
            dialogo.setTitle(title);
            dialogo.setMessage(Message);
            dialogo.setCancelable(false);
            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo, int id) {
                    try {
                        EnviarMensaje(getTelCkto(), contenido_sms);
                        superclase.save_action_phone(getApplicationContext(), mensaje_bd, getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
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

                }
            });
            dialogo.show();
        }
    }


    public void universal(final String mensaje1) {
        /*Toast.makeText(getApplicationContext(), "Status Flag:"+estadof8+estadof7+estadof6+estadof5+estadof4+estadof3+estadof2+estadof1+
                "\n\nDato:"+mensaje1, Toast.LENGTH_SHORT).show();*/
        String telckto = getTelCkto();
        if (telckto.equals("Sin configurar.")) {
            mensaje();
        } else {
            final String mensaje = "¿Realmente desea cambiar el estado" +
                    " de la/las luminarias seleccionadas?";
            dialogo = new AlertDialog.Builder(MainActivity.this);
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
                    estadof1 = 0;
                    estadof2 = 0;
                    estadof3 = 0;
                    estadof4 = 0;
                    estadof5 = 0;
                    estadof6 = 0;
                    estadof7 = 0;
                    estadof8 = 0;
                    sw0.setChecked(false);
                    sw1.setChecked(false);
                    sw2.setChecked(false);
                    sw3.setChecked(false);
                    sw4.setChecked(false);
                    sw5.setChecked(false);
                    sw6.setChecked(false);
                    sw7.setChecked(false);
                    sw8.setChecked(false);

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

    public void setStatusDefault() {
        estadof1 = 0;
        estadof2 = 0;
        estadof3 = 0;
        estadof4 = 0;
        estadof5 = 0;
        estadof6 = 0;
        estadof7 = 0;
        estadof8 = 0;
    }

    private String getTelCkto() {
        SharedPreferences preferences = getSharedPreferences("Destinatarios", Context.MODE_PRIVATE);
        String t1 = preferences.getString("telefono1", "Sin configurar.");
        return t1;
    }

    private void mensaje() {
        Toast.makeText(this, "No se ha configurado.", Toast.LENGTH_SHORT).show();
    }

    public void EnviarMensaje(String Numero, String Mensaje) {
        try {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(Numero, null, Mensaje, null, null);
            Toast.makeText(getApplicationContext(), "Comando de control enviado.\n\nDestinatario:" + Numero, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Mensaje no enviado, datos incorrectos.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    private void msg(String s) {
        /*Toast toast = Toast.makeText(getApplicationContext(), "Campo respuesta es obligatorio", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();*/
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }
    /*
       FIN
     */


    public void confirmar() {
        //android.app.AlertDialog.Builder dialogo1 = new android.app.AlertDialog.Builder(getContext());
        dialogo1 = new AlertDialog.Builder(MainActivity.this);
        dialogo1.setIcon(R.drawable.ic_sms);
        //dialog.setTitle("COCESNA.");
        dialogo1.setTitle("Advertencia.");
        dialogo1.setMessage("¿Realmente desea compartir los datos con el phone registrado?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                //EnviarSMS();
                superclase.EnviarSMS(MainActivity.this);
            }
        });
        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                //Toast toast = Toast.makeText(getApplicationContext(), "PROCESO CANCELADO CORRECTAMENTE.", Toast.LENGTH_SHORT);
                Toast toast = Toast.makeText(MainActivity.this, "PROCESO CANCELADO CORRECTAMENTE.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
        dialogo1.show();
    }


    public void EnviarSMS() {
        String servidor = conf_Server();
        if (servidor.equals("Sin configurar.")) {
            mensaje1();
        } else {

            pd.show();
            String pc = infoSharedPreferences();
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
                            Log.d("string", response);
                            try {
                                //System.out.println("RESPUESTA DE SERVIDOR : "+response);
                                //Toast.makeText(grafica5.this, response, Toast.LENGTH_LONG).show();
                                JSONArray jsonarray = new JSONArray(response);
                                String cadena = "";

                                for (int i = 0; i < jsonarray.length(); i++) {
                                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                                    double temperatura = jsonobject.getDouble("temperatura");          //Valores de la base de datos.
                                    double humedad = jsonobject.getDouble("humedad");
                                    String fecha = jsonobject.getString("fecha").trim();
                                    String hora = jsonobject.getString("hora").trim();

                                    //OBTENIENDO LA FECHA Y HORA ACTUAL DEL SISTEMA.
                                    DateFormat formatodate = new SimpleDateFormat("yyyy/MM/dd");
                                    String date = formatodate.format(new Date());

                                    DateFormat formatotime = new SimpleDateFormat("HH:mm:ss a");
                                    String time = formatotime.format(new Date());
                                    String datosCompletos = "SMS UTLA.\n" +
                                            "Temperatura: " + temperatura + " °C\n" +
                                            "Humedad: " + humedad + " RH\n" +
                                            "Fecha: " + fecha + "\n" +
                                            "Hora: " + hora + "\n" +
                                            "by system UTLA";
                                    try {
                                        SmsManager sms = SmsManager.getDefault();
                                        //sms.sendTextMessage(numTel, null, datosCompletos, null,null);  //FUNCION LIMITADO A MENOS CARACTERES POR SMS
                                        ArrayList msgTexts = sms.divideMessage(datosCompletos);
                                        sms.sendMultipartTextMessage(infoConfDestinatarioTel2(), null, msgTexts, null, null);
                                        //Toast.makeText(getApplicationContext(), "Mensaje Enviado.", Toast.LENGTH_LONG).show();
                                        Toast toast = Toast.makeText(getApplicationContext(), "MENSAJE ENVIADO A MÓVIL: " + infoConfDestinatarioTel2(), Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                    } catch (Exception e) {
                                        Toast.makeText(getApplicationContext(), "Mensaje no enviado, datos incorrectos." + e.getMessage().toString(), Toast.LENGTH_LONG).show();
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
                            if (error != null) {
                                Toast.makeText(getApplicationContext(), "Algo salió mal.", Toast.LENGTH_LONG).show();
                                pd.hide();
                            }
                        }
                    }
            );
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        }
    }




    public void grafpastel() {
        String comprobacion = conf_Server();
        if (comprobacion.equals("Sin configurar.")) {
            mensaje1();
        } else {
            Intent intent = new Intent(getApplicationContext(), graficapastel.class);
            startActivity(intent);
        }
    }

    public void grafbarra() {
        String comprobacion = conf_Server();
        if (comprobacion.equals("Sin configurar.")) {
            mensaje1();
        } else {
            Intent intent = new Intent(getApplicationContext(), graficabarra.class);
            startActivity(intent);
        }
    }

    public void graflinea() {
        String comprobacion = conf_Server();
        if (comprobacion.equals("Sin configurar.")) {
            mensaje1();
        } else {
            Intent intent = new Intent(getApplicationContext(), graphic_dificult.class);
            startActivity(intent);
        }
    }

    private String conf_Server() {
        //Buscando datos en archivo credenciales.xml
        SharedPreferences preferences = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        String server = preferences.getString("servidor", "Sin configurar.");
        return server;
    }

    private String conf_folder() {
        //Buscando datos en archivo credenciales.xml
        SharedPreferences preferences = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        String folder = preferences.getString("folder", "Sin configurar aun.");
        return folder;
    }


    private String infoSharedPreferences() {
        //Buscando datos en archivo credenciales.xml
        SharedPreferences preferences = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        String server = preferences.getString("servidor", "Sin configurar aun.");
        String folder = preferences.getString("folder", "Sin configurar aun.");
        return server + folder;
    }


    private void mensaje1() {
        Toast.makeText(this, "No se ha configurado.", Toast.LENGTH_SHORT).show();
    }

    //NUEVAS FUNCIONES PARA UTILIZAR  CON EL ARCHIVO XML CREADO CON SHAREDPREFERENCES.
    /*public  void logout(View view){
        SharedPreferences preferences = getSharedPreferences("variablesesion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }*/

    public void destroySesionVariables() {
        SharedPreferences preferences = getSharedPreferences("variablesesion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    public void close(View view) {
        finish();
    }

    public void enviar_email() {
        String comprobacion = conf_Server();
        if (comprobacion.equals("Sin configurar.")) {
            mensaje1();
        } else {
            pd.show();
            String pc = infoSharedPreferences();

            //String url = "http://" + pc + "/service/grafica5.php";
            //String url = pc + "/service/grafica5.php";
            String url = pc + "/grafica5.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @SuppressLint("ResourceType")
                        @Override
                        public void onResponse(String response) {
                            Log.d("string", response);
                            try {
                                //System.out.println("RESPUESTA DE SERVIDOR : "+response);
                                //Toast.makeText(grafica5.this, response, Toast.LENGTH_LONG).show();
                                JSONArray jsonarray = new JSONArray(response);
                                String cadena = "";
                                for (int i = 0; i < jsonarray.length(); i++) {
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

                                    String l1 = "", l2 = "", l3 = "", l4 = "", l5 = "", l6 = "", l7 = "", l8 = "";
                                    if (luz1 == 1) {
                                        l1 = "Encendido";
                                    } else {
                                        l1 = "Apagado";
                                    }

                                    if (luz2 == 1) {
                                        l2 = "Encendido";
                                    } else {
                                        l2 = "Apagado";
                                    }

                                    if (luz3 == 1) {
                                        l3 = "Encendido";
                                    } else {
                                        l3 = "Apagado";
                                    }

                                    if (luz4 == 1) {
                                        l4 = "Encendido";
                                    } else {
                                        l4 = "Apagado";
                                    }

                                    if (luz5 == 1) {
                                        l5 = "Encendido";
                                    } else {
                                        l5 = "Apagado";
                                    }

                                    if (luz6 == 1) {
                                        l6 = "Encendido";
                                    } else {
                                        l6 = "Apagado";
                                    }

                                    if (luz7 == 1) {
                                        l7 = "Encendido";
                                    } else {
                                        l7 = "Apagado";
                                    }

                                    if (luz8 == 1) {
                                        l8 = "Encendido";
                                    } else {
                                        l8 = "Apagado";
                                    }

                                    String fecha = jsonobject.getString("fecha").trim();
                                    String hora = jsonobject.getString("hora").trim();

                                    //Toast.makeText(grafica5.this, fecha, Toast.LENGTH_LONG).show();
                                    f = fecha;
                                    h = hora;

                                    //OBTENIENDO LA FECHA Y HORA ACTUAL DEL SISTEMA.
                                    DateFormat formatodate = new SimpleDateFormat("yyyy/MM/dd");
                                    String date = formatodate.format(new Date());

                                /*Date date1 = new Date();
                                Format formatter = new SimpleDateFormat("hh:mm:ss a");
                                String s = formatter.format(date1);
                                System.out.println(s);*/

                                    DateFormat formatotime = new SimpleDateFormat("HH:mm:ss a");
                                    String time = formatotime.format(new Date());

                                /*Intent emailIntent = new Intent(Intent.ACTION_SEND);
                                emailIntent.setData(Uri.parse("mailto:"));
                                //emailIntent.setType("text/html");
                                emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
                                emailIntent.putExtra(Intent.EXTRA_CC, cc);
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, asunto);
                                emailIntent.putExtra(Intent.EXTRA_TEXT, mensaje);
                                emailIntent.setType("message/rfc822");
                                startActivity(Intent.createChooser(emailIntent, "Email "));*/

                                    DecimalFormat df = new DecimalFormat("#.00");
                                    //double t= Double.parseDouble(df.format(temperatura));
                                    //double convertemperatura = (t * 1.8)+32;

                                    //Double x = Double.parseDouble(String.format("%.2f", temperatura));
                                    //double cv =((x * 1.8)+32.0);
                                   // String r = String.valueOf(cv);

                                    String message = "*********************************\n" +
                                            "Reporte de última actualización \nen la base de datos.\n" +
                                            "*********************************\n" +
                                            "\nINFORMACIÓN RECOPILADA.\n\n" +
                                            //"*Temperatura: " + temperatura + " °C.\n" +
                                            //"*Temperatura: " + temperatura + "°C ~ " + (((Double.parseDouble(String.valueOf(temperatura)))*1.8)+32) + " °F.\n" +
                                            //"*Temperatura: " + temperatura + "°C ~ " + String.format("%.2f",((((Double.parseDouble(String.valueOf(temperatura)))*1.8)+32))) + " °F.\n" +
                                            "*Temperatura: " + temperatura + "°C ~ " + df.format(((((Double.parseDouble(String.valueOf(temperatura)))*1.8)+32.0))) + " °F.\n" +
                                            "*Humedad Relativa: " + humedad + " % [R.H.].\n" +
                                            "*Luminaria # 1: " + l1 + "\n" +
                                            "*Luminaria # 2: " + l2 + "\n" +
                                            "*Luminaria # 3: " + l3 + "\n" +
                                            "*Luminaria # 4: " + l4 + "\n" +
                                            "*Luminaria # 5: " + l5 + "\n" +
                                            "*Luminaria # 6: " + l6 + "\n" +
                                            "*Luminaria # 7: " + l7 + "\n" +
                                            "*Luminaria # 8: " + l8 + "\n" +
                                            "\nFecha y hora de la última actualización: " +
                                            "Fecha: " + fecha + " / " + hora + "\n\n" +
                                            "-------------------------------------------------------" + "\n" +
                                            "Mensaje Generado: " + date + " ~ " + time + "\n" +
                                            "-------------------------------------------------------" + "\n" +
                                            "Copyright(c) UTLA 2019~2020. " +
                                            "\nAll rights reserved.";

                                    //Instanciamos un Intent del tipo ACTION_SEND
                                    Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                                    //emailIntent.setData(Uri.parse("mailto:"));
                                    //Aqui definimos la tipologia de datos del contenido dle Email en este caso text/html
                                    emailIntent.setType("text/html");
                                    //emailIntent.setType("message/rfc822");
                                    // Indicamos con un Array de tipo String las direcciones de correo a las cuales enviar
                                    //emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"androdfast@gmail.com"});

                                    //emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{destinatario});  //ACA PONGO LA DIRECCION EMAIL A QUIEN VOY A ENVIAR.
                                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{infoConfDestinatarioEmail1()});  //ACA PONGO LA DIRECCION EMAIL A QUIEN VOY A ENVIAR.

                                    // Aqui definimos un titulo para el Email
                                    //emailIntent.putExtra(android.content.Intent.EXTRA_TITLE, "El Titulo");
                                    emailIntent.putExtra(android.content.Intent.EXTRA_TITLE, "El Titulo:Chovi");
                                    // Aqui definimos un Asunto para el Email
                                    //emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "El Asunto");
                                    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Reporte del Sistema UTLA");

                                    // Aqui obtenemos la referencia al texto y lo pasamos al Email Intent
                                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);    //Mensaje Enviado por Whatsapp
                                    //emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.my_text));

                                    try {
                                        //Enviamos el Correo iniciando una nueva Activity con el emailIntent.
                                        startActivity(Intent.createChooser(emailIntent, getNameBD() + " \nSeleccione medio para compartir información Temp °C y RH."));
                                    } catch (android.content.ActivityNotFoundException ex) {
                                        Toast.makeText(getApplicationContext(), "No hay ningun cliente de correo instalado.", Toast.LENGTH_SHORT).show();
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
                            if (error != null) {
                                Toast.makeText(getApplicationContext(), "Algo salió mal con la conexión al servidor.\n" +
                                        "Por favor intente mas tarde.", Toast.LENGTH_LONG).show();
                                pd.hide();
                            }
                        }
                    }
            );
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        }
    }





    private void config_server() {
        final android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
        //AlertDialog.Builder mBuilder = new AlertDialog.Builder(getApplicationContext());
        //mBuilder.setIcon(R.drawable.ic_servidor);
        //mBuilder.setTitle("<<<UTLA>>>");

        mBuilder.setCancelable(false);
        final View mView = getLayoutInflater().inflate(R.layout.dialog_server, null);

        //controles donde muestro informacion del archivo credenciales.xml creado con
        //SharedPreferences.
        final Spinner sprotocolo = (Spinner)mView.findViewById(R.id.sprotocolo);
        final TextView tv_encabezado = (TextView)mView.findViewById(R.id.tv_encabezado);
        final TextView tv_encabezado1 = (TextView)mView.findViewById(R.id.tv_encabezado1);
        final TextView tv_servidor = (TextView)mView.findViewById(R.id.tv_servidor);
        final TextView tv_directorio = (TextView)mView.findViewById(R.id.tv_directorio);
        final TextView tv_directorioPortal = (TextView)mView.findViewById(R.id.tv_directorioPortal);
        final TextView tv_fecha = (TextView)mView.findViewById(R.id.tv_fecha);
        final TextView tv_hora = (TextView)mView.findViewById(R.id.tv_hora);

        final EditText dominio = (EditText) mView.findViewById(R.id.et_dominio);
        final EditText directorio = (EditText) mView.findViewById(R.id.et_directorio);
        final EditText portal = (EditText) mView.findViewById(R.id.et_portal);
        final TextInputLayout ti_dominio=(TextInputLayout)mView.findViewById(R.id.ti_dominio);

        Button btnSave = (Button)mView.findViewById(R.id.btnSave);      //BOTONES DEL DIALOG CONFIGURACION.
        Button btnCancel = (Button)mView.findViewById(R.id.btnCancel);
        TextView txtclose = (TextView)mView.findViewById(R.id.txtclose);

        final String[] lista =new String[]{
                "http://",
                "https://",
        };
        ArrayAdapter<String> adaptador1 =new ArrayAdapter<String> (this,android.R.layout.simple_spinner_item, lista);
        sprotocolo.setAdapter(adaptador1);

        Toast toast = Toast.makeText(getApplicationContext(), "Settings Your Server PC", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        //Buscando datos en archivo credenciales.xml
        SharedPreferences preferences = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        String server = preferences.getString("servidor","Sin configurar.");
        String folder = preferences.getString("folder","Sin configurar.");
        String porta = preferences.getString("portal","Sin configurar.");
        String fec = preferences.getString("fecha", "NA.");
        String hor = preferences.getString("hora", "NA.");
        if (server.equals("Sin configurar.")){
            tv_servidor.setText("Servidor: " +server);
            tv_directorio.setText("API-WebService: " + folder);
            tv_directorioPortal.setText("Portal Web: " + porta);
            tv_fecha.setText("Fecha de Registro: "+fec);
            tv_hora.setText("Hora de Registro: "+hor);
        }else{
            tv_encabezado.setVisibility(mView.VISIBLE);
            tv_encabezado.setText("Server API/Web service: "+ server + folder);
            tv_encabezado1.setText("Server Portal Web: "+ server + porta);
            tv_servidor.setVisibility(mView.GONE);
            tv_directorio.setVisibility(mView.GONE);
            tv_directorioPortal.setVisibility(mView.GONE);
            tv_fecha.setVisibility(mView.GONE);
            tv_hora.setVisibility(mView.GONE);
        }

        dominio.setText(server);
        directorio.setText(folder);
        portal.setText(porta);
        ////////////////////////////////////////////////////////////////////////////////////
        /*mBuilder.setPositiveButton("GUARDAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(),"JAJAJA",Toast.LENGTH_LONG).show();

            }
        });
        //mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
        mBuilder.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });*/
        ////////////////////////////////////////////////////////////////////////////////////
        mBuilder.setView(mView);
        //final AlertDialog dialog = mBuilder.create();
        final android.app.AlertDialog dialog = mBuilder.create();
        dialog.show();

        //btnVerificarRespuesta.setOnClickListener(new View.OnClickListener() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                estado_nombre = false;
                estado_dir_api = false;
                estado_dir_portal = false;
                //if (Patterns.DOMAIN_NAME.matcher(dominio.getText().toString()).matches()==false){
                if (Patterns.DOMAIN_NAME.matcher(dominio.getText().toString()).matches() == false &&
                        Patterns.IP_ADDRESS.matcher(dominio.getText().toString()).matches() == false) {
                    dominio.setText(null);
                    dominio.setError("Nombre de dominio o IP inválido");
                    ti_dominio.setError("Nombre de dominio o IP inválido");
                    estado_nombre = false;
                    dominio.requestFocus();
                    //ti_dominio.setVisibility(View.VISIBLE);
                } else {
                    estado_nombre = true;
                    ti_dominio.setError(null);
                    //ti_dominio.setVisibility(View.GONE);
                }

                if(directorio.getText().toString().length()==0){
                    directorio.setError("Campo Obligadorio");
                    directorio.requestFocus();
                    estado_dir_api = false;
                }else{
                    estado_dir_api = true;
                }

                if(portal.getText().toString().length()==0){
                    portal.setError("Campo Obligadorio");
                    portal.requestFocus();
                    estado_dir_portal = false;
                }else{
                    estado_dir_portal = true;
                }


                if(estado_nombre && estado_dir_api && estado_dir_portal){
                    SharedPreferences preferences = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
                    //OBTENIENDO LA FECHA Y HORA ACTUAL DEL SISTEMA.
                    DateFormat formatodate= new SimpleDateFormat("yyyy/MM/dd");
                    String date= formatodate.format(new Date());
                    DateFormat formatotime= new SimpleDateFormat("HH:mm:ss a");
                    String time= formatotime.format(new Date());
                    String servidor = dominio.getText().toString();
                    String folder = directorio.getText().toString();
                    String folder1 = portal.getText().toString();

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("servidor", sprotocolo.getSelectedItem().toString() + servidor + "/");
                    editor.putString("folder", folder);
                    editor.putString("portal", folder1);
                    editor.putString("fecha", date);
                    editor.putString("hora", time);
                    editor.commit();

                    tv_encabezado.setVisibility(mView.VISIBLE);
                    tv_encabezado.setText("Server API-Web Service: "+ servidor +"/" + folder);

                    tv_encabezado1.setVisibility(mView.VISIBLE);
                    tv_encabezado1.setText("Server Portal Web: "+ servidor +"/" + folder1);

                    tv_servidor.setVisibility(mView.VISIBLE);
                    tv_directorio.setVisibility(mView.VISIBLE);
                    tv_fecha.setVisibility(mView.VISIBLE);
                    tv_hora.setVisibility(mView.VISIBLE);

                    tv_servidor.setText("Server: " +servidor);
                    tv_directorio.setText("Directorios: " +folder+", "+folder1);
                    tv_fecha.setText("Fecha de Registro: "+date);
                    tv_hora.setText("Hora de Registro: "+time);

                    //CON ESTE CODIGO LOGRE CERRAR EL DIAGO1-INICIO-FIN
                    /*mBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            dialog.cancel();
                        }
                    });
                    dialog.cancel();*/

                    Toast.makeText(getApplicationContext(),"Registro creado correctamente!!!",Toast.LENGTH_LONG).show();
                }

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CON ESTE CODIGO LOGRE CERRAR EL DIAGO1-INICIO-FIN
                mBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.cancel();
                    }
                });
                dialog.cancel();
            }
        });


        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

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

    private void ShowLoginMasterDestinarios() {
        myDialog.setContentView(R.layout.custompopup_login);
        myDialog.setCancelable(false);
        BtnCerrar = (ImageView)myDialog.findViewById(R.id.BtnCerrar);

        final Button btnAccept1 = (Button)myDialog.findViewById(R.id.btnAccept);

        final EditText etEmail = (EditText)myDialog.findViewById(R.id.etEmail);
        final EditText etPassword = (EditText)myDialog.findViewById(R.id.etPassword);
        final TextInputLayout tiError = (TextInputLayout)myDialog.findViewById(R.id.tiError);
        //Button btnAccept = (Button)findViewById(R.id.btnAccept);

        BtnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

        btnAccept1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "O.K", Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(), "ESTOY BIEN...", Toast.LENGTH_SHORT).show();
                //dbSQLiteHelper admin=new dbSQLiteHelper(this);
                boolean senalEmail = false;
                boolean senalContra = false;
                if(!etEmail.getText().toString().isEmpty() || !etPassword.getText().toString().isEmpty()) {

                    SQLiteDatabase db = admin.getWritableDatabase();
                    String usuario = etEmail.getText().toString();
                    String contrasena = etPassword.getText().toString();

                    if (Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches() == false) {
                        //mEmail.setBackgroundColor(Color.GREEN);
                        //etEmail.setText(null);
                        //etEmail.setError("Correo inválido.");
                        //tiError.setError("Correo inválido.");
                        //etEmail.requestFocus();
                        estado_correo = false;
                    } else {
                        estado_correo = true;
                        //etEmail.setError(null);
                    }

                    if (estado_correo == false) {
                        etEmail.setError("E-mail inválido.");
                        etEmail.requestFocus();
                        senalEmail = false;
                    } else {
                        senalEmail = true;
                    }

                    if (etPassword.getText().toString().isEmpty()){
                        etPassword.setError("Campo password obligatorio.");
                        etPassword.requestFocus();
                        senalContra = false;
                    }else {
                        senalContra = true;
                    }

                    if (senalEmail == true && senalContra == true) {
                        Cursor fila = db.rawQuery("select codigo,usuario,clave,nombres,apellidos from usuarios where usuario='" + usuario + "' and clave='" + contrasena + "' and codigo=1", null);
                        if (fila.moveToFirst()) {
                            int codigo = fila.getInt(0);
                            String usua = fila.getString(1);
                            String pass = fila.getString(2);
                            if (usuario.equals(usua) && contrasena.equals(pass)) {
                                ShowDialogConfiguracion();
                            } else {
                                Toast.makeText(getApplicationContext(), "No eres Master", Toast.LENGTH_SHORT).show();
                            }
                            db.close();
                            //}
                        } else {
                            Toast.makeText(getApplicationContext(), "Usuario desconocido.", Toast.LENGTH_SHORT).show();
                        }
                    }

                }else{
                    Toast.makeText(getApplicationContext(), "Todos los campos son obligatorios", Toast.LENGTH_LONG).show();
                    //etEmail.setError("Campo Obligatorio.");
                    //etPassword.setError("Campo Obligatorio.");
                }
            }
        });
    }

    public static String obtenerValor(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("Destinatarios", MODE_PRIVATE);
        return  preferences.getString("telefono2", "Sin configurar.");
    }

    private String infoConfDestinatarioTel1(){
        SharedPreferences preferences = getSharedPreferences("Destinatarios", Context.MODE_PRIVATE);
        String t1 = preferences.getString("telefono1","Sin configurar.");
        String t2 = preferences.getString("telefono2","Sin configurar.");
        String e1 = preferences.getString("email1","Sin configurar.");
        String fe = preferences.getString("fecha","Sin configurar.");
        String ho = preferences.getString("hora","Sin configurar.");
        return t1;
    }

    private String infoConfDestinatarioTel2(){
        SharedPreferences preferences = getSharedPreferences("Destinatarios", Context.MODE_PRIVATE);
        String t2 = preferences.getString("telefono2","Sin configurar.");
        return t2;
    }

    private String infoConfDestinatarioEmail1(){
        SharedPreferences preferences = getSharedPreferences("Destinatarios", Context.MODE_PRIVATE);
        String e1 = preferences.getString("email1","");
        return e1;
    }

    public void ShowDialogConfiguracion(){
        myDialog.setContentView(R.layout.dialog_config_destinatarios);
        myDialog.setTitle("Configuración destinatarios");
        myDialog.setCancelable(false);

        //TextView txtclose = (TextView)myDialog.findViewById(R.id.txtclose);
        Button btnCancel = (Button)myDialog.findViewById(R.id.btnCancel);
        Button btnAplicar = (Button)myDialog.findViewById(R.id.btnAplicar);
        ImageView BtnCerrar = (ImageView)myDialog.findViewById(R.id.BtnCerrar);

        final EditText etTelefonoPropietario = (EditText)myDialog.findViewById(R.id.etTelefonoPropietario);
        final EditText etTelefono1 = (EditText)myDialog.findViewById(R.id.etTelefono1);
        final EditText etTelefono2 = (EditText)myDialog.findViewById(R.id.etTelefono2);
        final EditText etdireccioncorreo = (EditText)myDialog.findViewById(R.id.etdireccioncorreo);
        final EditText etMacBT = (EditText)myDialog.findViewById(R.id.etMacBT);
        final TextView tvFechaHora = (TextView)myDialog.findViewById(R.id.tvFechaHora);

        SharedPreferences preferences = getSharedPreferences("Destinatarios", Context.MODE_PRIVATE);
        String tel0 = preferences.getString("telefono0","");
        String tel1 = preferences.getString("telefono1","");
        String tel2 = preferences.getString("telefono2","");
        String ema1 = preferences.getString("email1","");
        String macBT = preferences.getString("mac","");
        String fec = preferences.getString("fecha", "");
        String hor = preferences.getString("hora", "");

       /*SharedPreferences.Editor editor = datos.edit();
        String tel1 = datos.getString("telefono1","Sin configurar.");
        String tel2 = datos.getString("telefono2","Sin configurar.");
        String ema1 = datos.getString("email1","Sin configurar.");
        String fec = datos.getString("fecha","Sin configurar.");
        String hor = datos.getString("hora","Sin configurar.");*/

        etTelefonoPropietario.setText(tel0);
        etTelefono1.setText(tel1);
        etTelefono2.setText(tel2);
        etdireccioncorreo.setText(ema1);
        etMacBT.setText(macBT);
        tvFechaHora.setText(fec +" "+hor);

        BtnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

        btnAplicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean estado_tel0=false;
                boolean estado_tel1=false;boolean estado_tel2=false;boolean estado_email1=false;
                boolean estado_mac=false;
                //Toast.makeText(getApplicationContext(),"bien...",Toast.LENGTH_LONG).show();

                if((etTelefonoPropietario.getText().toString().isEmpty())){
                    etTelefonoPropietario.setText(null);
                    etTelefonoPropietario.setError("Campo Teléfono obligatorio.");
                    estado_tel0 = false;
                    etTelefonoPropietario.requestFocus();
                }

                if((etTelefono1.getText().toString().isEmpty())){
                    etTelefono1.setText(null);
                    etTelefono1.setError("Campo Teléfono obligatorio.");
                    estado_tel1 = false;
                    etTelefono1.requestFocus();
                }

                if(etTelefono2.getText().toString().isEmpty()){
                    etTelefono2.setText(null);
                    etTelefono2.setError("Campo Teléfono obligatorio.");
                    estado_tel2 = false;
                    etTelefono2.requestFocus();
                }

                if (Patterns.PHONE.matcher(etTelefonoPropietario.getText().toString()).matches()==false){
                    etTelefonoPropietario.setText(null);
                    etTelefonoPropietario.setError("Teléfono invalido.");
                    estado_tel0 = false;
                    etTelefonoPropietario.requestFocus();
                }else{
                    estado_tel0 = true;
                    etTelefonoPropietario.setError(null);
                }

                if(estado_tel0 == true) {
                    if (Patterns.PHONE.matcher(etTelefono1.getText().toString()).matches()==false){
                        etTelefono1.setText(null);
                        etTelefono1.setError("Teléfono invalido.");
                        estado_tel1 = false;
                        etTelefono1.requestFocus();
                    }else{
                        estado_tel1 = true;
                        etTelefono1.setError(null);
                    }
                }

                if(estado_tel1 == true) {
                    if (Patterns.PHONE.matcher(etTelefono2.getText().toString()).matches() == false) {
                        etTelefono2.setText(null);
                        etTelefono2.setError("Teléfono invalido.");
                        estado_tel2 = false;
                        etTelefono2.requestFocus();
                    } else {
                        estado_tel2 = true;
                        etTelefono2.setError(null);
                    }
                }

                if (estado_tel2==true){
                    if (Patterns.EMAIL_ADDRESS.matcher(etdireccioncorreo.getText().toString()).matches()==false){
                        etdireccioncorreo.setText(null);
                        etdireccioncorreo.setError("e-mail inválido.");
                        estado_email1 = false;
                        etdireccioncorreo.requestFocus();
                    }else{
                        estado_email1 = true;
                        etdireccioncorreo.setError(null);
                    }
                }

                if(etMacBT.getText().toString().isEmpty()){
                    etMacBT.setText(null);
                    etMacBT.setError("Campo dirección MAC obligatorio.\nValores Sistema Hexadecimal");
                    estado_mac = false;
                    etMacBT.requestFocus();
                }else{
                    estado_mac = true;
                }

                if(estado_tel0==true && estado_tel1==true && estado_tel2==true && estado_email1==true && estado_mac==true){

                    settingDestinatarios1(etTelefonoPropietario.getText().toString(), etTelefono1.getText().toString(), etTelefono2.getText().toString(), etdireccioncorreo.getText().toString(), etMacBT.getText().toString());

                    Toast.makeText(MainActivity.this, "Configuración guardada correctamente.", Toast.LENGTH_LONG).show();
                    myDialog.dismiss();
                }
            }
        });
    }

    /*
    //original función para Obtener número de teléfono desde la aplicación.
    @SuppressLint("MissingPermission")
    private String getPhoneNumber(){
        TelephonyManager mTelephonyManager;
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return mTelephonyManager.getLine1Number();

        //TelephonyManager tMgr = (TelephonyManager)mAppContext.getSystemService(Context.TELEPHONY_SERVICE);
        //String mPhoneNumber = tMgr.getLine1Number();
    }
    */

    public void settingDestinatarios1(String t0, String t1, String t2, String email1, String MAC){
        SharedPreferences preferences = getSharedPreferences("Destinatarios", Context.MODE_PRIVATE);
        //OBTENIENDO LA FECHA Y HORA ACTUAL DEL SISTEMA.
        DateFormat formatodate= new SimpleDateFormat("yyyy/MM/dd");
        String date= formatodate.format(new Date());
        DateFormat formatotime= new SimpleDateFormat("HH:mm:ss a");
        String time= formatotime.format(new Date());

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("telefono0", t0);
        editor.putString("telefono1", t1);
        editor.putString("telefono2", t2);
        editor.putString("email1", email1);
        editor.putString("mac", MAC);
        editor.putString("fecha", date);
        editor.putString("hora", time);
        editor.commit();
    }

    //*******************************************************************************
    //*******************************************************************************
    //*******************************************************************************
    public static void settingDestinatarios(String t1, String t2, String email1){
        DateFormat formatodate= new SimpleDateFormat("yyyy/MM/dd");
        String date= formatodate.format(new Date());
        DateFormat formatotime= new SimpleDateFormat("HH:mm:ss a");
        String time= formatotime.format(new Date());

        SharedPreferences.Editor editor = datos.edit();
        editor.putString("telefono1", t1);
        editor.putString("telefono2", t2);
        editor.putString("email1", email1);
        editor.putString("fecha", date);
        editor.putString("hora", time);
        editor.commit();
    }

    //JOJOJOJOJOJOJOJOJ0JLJOJOJOJOJOJOJOJOJOJOJOJOJOJOOJOJOJOJOJOJOJOJOJOJOJOJOJOJOJOJO
    //ESTE ES EL MÉTODO CREADO Y FUNCIONAL CON SHAREDPREFERENCED EN MODO STATIC.

    public static String getTel2Receptor(){
        String t2 = datos.getString("telefono2","Sin configurar.");
        return t2;
    }
    //*******************************************************************************
    //*******************************************************************************
    //*******************************************************************************
   public void about1(){
        myDialog.setContentView(R.layout.acerca);
        myDialog.setTitle("Creado por");
        myDialog.setCancelable(true);

        Button cerrar = (Button)myDialog.findViewById(R.id.btncerrar);
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.show();
   }

    public void about(){
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
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    public void Showpopup(){
        TextView txtclose;
        Button btnFollow;
        myDialog.setContentView(R.layout.custompopup1);
        myDialog.setTitle("App Creado por,");
        myDialog.setCancelable(true);
        txtclose = (TextView)myDialog.findViewById(R.id.txtclose);
        btnFollow = (Button)myDialog.findViewById(R.id.btnfollow);

        //INICIO:PROBANDO REDONDEAR LA IMAGEN
        //extraemos el drawable en un bitmap
        Drawable originalDrawable = getResources().getDrawable(R.drawable.photo3);
        Bitmap originalBitmap = ((BitmapDrawable) originalDrawable).getBitmap();

        //creamos el drawable redondeado
        RoundedBitmapDrawable roundedDrawable =
                RoundedBitmapDrawableFactory.create(getResources(), originalBitmap);

        //asignamos el CornerRadius
        roundedDrawable.setCornerRadius(originalBitmap.getHeight());

        ImageView imageView = (ImageView)myDialog.findViewById(R.id.iv_photo);

        imageView.setImageDrawable(roundedDrawable);

        //FIN DEL REDONDEO DE LA IMAGEN.



        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        //myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"Website: mjgl.com.sv" +
                //        "\nFacebook: chovi gamez",Toast.LENGTH_LONG).show();
                Toast toast = Toast.makeText(getApplicationContext(), "Website: mjgl.com.sv" +
                                "\nFacebook: chovi gamez", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
    }

    public void activity_Bluetooth_Control() {
        Intent intent = new Intent(getApplicationContext(), device_list.class);
        startActivity(intent);
    }

    public void activity_Gsm_Control() {
        String telckto = getTelCkto();
        if (telckto.equals("Sin configurar.")) {
            mensaje1();
        } else {
            Intent intent = new Intent(getApplicationContext(), luces_control_sms.class);
            startActivity(intent);
        }
    }

    public void finalizar(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogoConfirmacion dialogo = new DialogoConfirmacion();
        dialogo.show(fragmentManager, "tagConfirmacion");
    }

    public void mapa(){
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        if (habilitar) { //Habilita
            menu.findItem(R.id.action_reset).setVisible(true);
            menu.findItem(R.id.action_call).setVisible(true);
            menu.findItem(R.id.menus_settings).setVisible(true);
            menu.findItem(R.id.menus_portal).setVisible(true);
            menu.findItem(R.id.menus_bt).setVisible(true);
            menu.findItem(R.id.menus_bt1).setVisible(true);
            menu.findItem(R.id.menus_sms).setVisible(true);
            menu.findItem(R.id.menus_sms1).setVisible(true);
            menu.findItem(R.id.menu_saludo).setVisible(true);
            menu.findItem(R.id.menu_callme).setVisible(true);
            menu.findItem(R.id.menu_send_email).setVisible(true);
            menu.findItem(R.id.menus_ubicacion).setVisible(true);
            menu.findItem(R.id.menus_creador).setVisible(true);
            menu.findItem(R.id.menus_salir).setVisible(true);

        }else{ //Deshabilita.
            menu.findItem(R.id.action_reset).setVisible(false);
            menu.findItem(R.id.action_call).setVisible(false);
            menu.findItem(R.id.menus_settings).setVisible(false);
            menu.findItem(R.id.menus_portal).setVisible(false);
            menu.findItem(R.id.menus_bt).setVisible(true);
            menu.findItem(R.id.menus_bt1).setVisible(true);
            menu.findItem(R.id.menus_sms).setVisible(false);
            menu.findItem(R.id.menus_sms1).setVisible(false);

            menu.findItem(R.id.menu_saludo).setVisible(false);
            menu.findItem(R.id.menu_callme).setVisible(false);
            menu.findItem(R.id.menu_send_email).setVisible(false);

            menu.findItem(R.id.menus_ubicacion).setVisible(true);
            menu.findItem(R.id.menus_creador).setVisible(true);
            menu.findItem(R.id.menus_salir).setVisible(true);
        }

        return true;
    }

    @SuppressLint("MissingPermission")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button_green_round, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //ESTA LÍNEA ES DE LAS ULTIMAS QUE HE ADICIONADO PARA REALIZAR LLAMADAS.

        if(id== R.id.action_reset){
            String telckto = getTelCkto();
            if (telckto.equals("Sin configurar.")) {
                mensaje();
            } else {
                universal1("utla0reset0", "Solicitud Resetear Sistema", Config.mensaje1 + "\n\"Realizar un RESET al sistema de monitoreo y control.\"" +
                        "\n\nEsta opción es recomendable utilizarla únicamente para los siguientes casos:\n" +
                        "\n* El sistema no responde a las ordenes dadas desde la App a pesar de poseer saldo (SMS-CALL)." +
                        "\n* El sistema se quedo sin saldo y se realizó una nueva recarga." +
                        "\n* Colocar en el estado predeterminado (OFF) todas las cargas.", "Reset Sistema");
                //superclase.save_action_phone(MainActivity.this,"Se realizo un RESET a ARDUINO.",getTelCkto(),obtenerTelPropietario(),getDate(),getTime());
            }
            return true;
        }else if(id == R.id.action_call){
            String telckto = getTelCkto();
            if (telckto.equals("Sin configurar.")) {
                mensaje();
            } else {
                String phone = infoConfDestinatarioTel1();
                if (!phone.isEmpty()) {
                    String dial = "tel:" + phone;
                    //startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                    superclase.save_action_phone(MainActivity.this, "Llamada realizada al sistema electrónico desde la App. Cambio de estado de todas luminarias.", getTelCkto(), obtenerTelPropietario(), getDate(), getTime());
                } else {
                    Toast.makeText(MainActivity.this, "Enter a phone number in setting", Toast.LENGTH_SHORT).show();
                }
            }
            return true;
        }else if (id == R.id.menus_settings) {
            ShowLoginMasterDestinarios();
            return true;
        }else if (id == R.id.menus_portal) {
            GoPortal();
            return true;
        }else if (id == R.id.menus_bt) {
            activity_Bluetooth_Control();
            return true;
        }else if (id == R.id.menus_bt1) {
            ventana_control_voz();
            return true;
        }else if (id == R.id.menus_sms) {
            activity_Gsm_Control();
            return true;
        }else if (id == R.id.menus_sms1) {
            String telckto = getTelCkto();
            if (telckto.equals("Sin configurar.")) {
                mensaje();
            } else {
                //onoffMultipleSMS();
                superclase.onoffMultipleSMS(MainActivity.this);
            }
            return true;
        }else if (id==R.id.menu_saludo) {
            String telckto = getTelCkto();
            if (telckto.equals("Sin configurar.")) {
                mensaje();
            } else {
                universal1("comoestas tu", "Solicitud SMS Entrante", Config.mensaje1 + "\n\"Solicitar mensaje de saludo de parte del sistema.\"" +
                        "\n\nEsta opción permite verificar la disponibilidad de señal o red GSM en el sistema.", "Solicitud Saludo App: Como estás Sistema Electrónico.");
            }
            return true;
        }else if(id==R.id.menu_callme){
            String telckto = getTelCkto();
            if (telckto.equals("Sin configurar.")) {
                mensaje();
            } else {
                //getCall();
                superclase.getCall(MainActivity.this);
            }
            return true;
        }else if (id==R.id.menu_send_email)   {
            email_automatico();
            return true;
        }else if (id == R.id.menus_ubicacion) {
            mapa();
            return true;
        }else if (id == R.id.menus_creador) {
            Showpopup();
            return true;
        }else if (id == R.id.menus_salir) {
            finalizar();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.config_userMaster) {
            // Handle the camera action
            ShowLoginMasterDestinarios();
        } else if (id == R.id.control_bt) {
            activity_Bluetooth_Control();
        } else if (id == R.id.control_bt1) {
            ventana_control_voz();
        }else if (id == R.id.control_sms) {
            activity_Gsm_Control();
        } else if (id == R.id.control_sms1) {
            String telckto = getTelCkto();
            if (telckto.equals("Sin configurar.")) {
                mensaje();
            } else {
                //onoffMultipleSMS();
                superclase.onoffMultipleSMS(MainActivity.this);
                //superclase.dialog_Saludo(MainActivity.this,"CONTENIDO DEL MENSAJE ENTRANTE ENVIADO POR CIRCUITO");
            }
        } else if (id == R.id.g_linea) {
            graflinea();
        }else if (id == R.id.g_barra) {
            grafbarra();
        } else if (id == R.id.g_pastel) {
            grafpastel();
        } else if (id == R.id.sms_social) {
            enviar_email();
        } else if (id == R.id.sms_celular) {
            //confirmar();
            String telckto = getTelCkto();
            if (telckto.equals("Sin configurar.")) {
                mensaje();
            } else {
                superclase.confirmar(MainActivity.this);
            }

        }else if (id == R.id.correo_automatico) {
            email_automatico();
        }else if (id == R.id.ubicanos) {
            mapa();
        /*}else if (id == R.id.segundo_plano) {*/
        }else if (id == R.id.acercade) {
            Showpopup();
            //about();
            //about1();
        }else if (id == R.id.cerrar_sesion) {
            finalizar();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void GoPortal() {
        String comprobacion = conf_Server();
        if (comprobacion.equals("Sin configurar.")) {
            mensaje1();
        } else {
            Intent i = new Intent(getApplicationContext(), VisorWeb.class);
            //i.putExtra("usu", mEmail.getText().toString());
            startActivity(i);
            finish();
        }
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
        String url_guardar_destinatarios = pc + "/registrophone.php";

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
