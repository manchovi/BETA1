package com.mjgl.utla.utla;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Recibir_SMS extends AppCompatActivity {
    public TextView fecha, hora;
    Toolbar toolbar;
    AlertDialog.Builder dialogo;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new android.app.AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_accessibility)
                    .setTitle("Información")
                    .setMessage("¿Realmente desea salir?")
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {//un listener que al pulsar, cierre la aplicacion
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            /*Intent intent = new Intent(DashboardLuces.this, luces_control_sms.class);
                            startActivity(intent);*/
                            //DashboardLuces.this.finishAffinity();
                            finish();
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
        setContentView(R.layout.activity_recibir__sms);

        //AL FINAL MEJOR CREE MI PROPIA TOOLBAR.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_left));
        toolbar.setTitleTextColor(getResources().getColor(R.color.mycolor_blanco));
        toolbar.setTitleMargin(0, 0, 0, 0);
        toolbar.setSubtitle("SMS Received.");
        getSupportActionBar().setTitle("UTLA");

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

        DateFormat formatodate= new SimpleDateFormat("yyyy/MM/dd");
        String date= formatodate.format(new Date());
        DateFormat formatotime= new SimpleDateFormat("HH:mm:ss a");
        String time= formatotime.format(new Date());

        fecha = (TextView)findViewById(R.id.fecha);
        hora = (TextView)findViewById(R.id.hora);

        fecha.setText("Fecha : "+date);
        hora.setText("Hora : "+time);

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            String addres = extras.getString("MessageNumber");
            String message = extras.getString("Message");
            String bandera = extras.getString("Flag");
            TextView addressField = (TextView)findViewById(R.id.address);
            TextView messageField = (TextView)findViewById(R.id.message);
            addressField.setText("Message From : " +addres);
            messageField.setText("Message : " + message);

            /*
            String sCadena = "Hola Mundo";
            String sSubCadena = sCadena.substring(5,10);
            System.out.println(sSubCadena);
            */


            if(bandera.equals("1")){

            }else{
                //System.out.println("palabra no encontrada");
                Toast.makeText(getApplicationContext(),"sms Temperatura y RH.",Toast.LENGTH_SHORT).show();
            }

        }

    }

    //private void goMenu(){
        //startActivity(new Intent(getApplicationContext(),MainActivity.class));
        /*Intent intent = new Intent(Recibir_SMS.this, MainActivity.class);
        startActivity(intent);*/
        //finish();
    //}

    private void goMenu(){
        //startActivity(new Intent(getApplicationContext(),MainActivity.class));
        String mensaje = "¿Realmente desea salir?";
        dialogo = new AlertDialog.Builder(Recibir_SMS.this);
        dialogo.setIcon(R.drawable.ic_accessibility);
        dialogo.setTitle("Información");
        dialogo.setMessage(mensaje);
        dialogo.setCancelable(false);
        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo, int id) {
                /*Intent intent = new Intent(DashboardLuces.this, luces_control_sms.class);
                startActivity(intent);*/
                //DashboardLuces.this.finishAffinity();
               finish();
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
