package com.mjgl.utla.utla;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class smsInput extends AppCompatActivity {
    //Creando instancia para ver si funciona
    superClase superclase = new superClase();

    Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_input);
        myDialog = new Dialog(this);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        toolbar.setTitle("Sytem Control GSM Ready...");
        toolbar.setTitleMargin(0, 0, 0, 0);
        toolbar.setSubtitle("Universidad Técnica LatinoAmericana");

        //Función para evitar la rotación de la pantalla del CELULAR.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //y esto para pantalla completa (oculta incluso la barra de estado)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String addres = extras.getString("MessageNumber");
            String message = extras.getString("Message");
            String bandera = extras.getString("Flag");

            String sCadena = "Hola Mundo";
            String sSubCadena = sCadena.substring(5, 10);

            if (bandera.equals("2")) {
                //superclase.dialog_Saludo(smsInput.this,message);
                dialog_Saludo(message);
            }
        }
    }

    //Cuadro de dialogo de solicitud de saludo por sms.
    public void dialog_Saludo(String mensaje){
        //myDialog = new Dialog(context);
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
                smsInput.this.finish();
            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                smsInput.this.finish();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

}
