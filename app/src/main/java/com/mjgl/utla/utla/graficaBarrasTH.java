package com.mjgl.utla.utla;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class graficaBarrasTH extends AppCompatActivity {
    public TextView fecha, hora;
    private ProgressDialog pd;
    Toolbar toolbar;

    ArrayList<BarDataSet> yAxis;
    ArrayList<BarEntry> yValues;
    ArrayList<String> xAxis1;
    BarEntry values ;
    BarChart chart;

    BarData data;

    private TextView tv_res;
    private TextView tv_fh;
    //private Button Btn_a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafica_barras_th);

        pd = new ProgressDialog(graficaBarrasTH.this);
        //pd.setMessage("loading...");
        //pd.setMessage("Estableciendo conexión con el servidor, " +
        //        "Por favor espere...");

        //AL FINAL MEJOR CREE MI PROPIA TOOLBAR.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_atras));
        toolbar.setTitleTextColor(getResources().getColor(R.color.mycolor_blanco));
        toolbar.setTitleMargin(0, 0, 0, 0);
        toolbar.setSubtitle("Gráfica de temperatura y humedad relativa");
        getSupportActionBar().setTitle("SMS Received");

        //Función para evitar la rotación de la pantalla del CELULAR.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //y esto para pantalla completa (oculta incluso la barra de estado)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        tv_res = (TextView)findViewById(R.id.tv_res);
        tv_fh = (TextView)findViewById(R.id.tv_fh);
        //Btn_a = (Button)findViewById(R.id.Btn_a);
        chart = (BarChart) findViewById(R.id.chart);

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

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        //String date1 = sdf.format(new Date(user.getTime()));
        //holder.txtTime.setText(date1);

        /*
            fecha = (TextView)findViewById(R.id.fecha);
            hora = (TextView)findViewById(R.id.hora);

            fecha.setText("Fecha : "+date);
            hora.setText("Hora : "+time);
        */

        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            String addres = extras.getString("MessageNumber");
            String message = extras.getString("Message");
            String bandera = extras.getString("Flag");
            /*
            TextView addressField = (TextView) findViewById(R.id.address);
            TextView messageField = (TextView) findViewById(R.id.message);
            addressField.setText("Message From : " + addres);
            messageField.setText("Message : " + message);

            String sCadena = "Hola Mundo";
            String sSubCadena = sCadena.substring(5,10);
            System.out.println(sSubCadena);
            */
        }

        //GENERANDO LA GRÁFICA DE ENTRADA AL SMS
        xAxis1 = new ArrayList<>();
        yAxis = null;
        yValues = new ArrayList<>();

        Gson gson = new Gson();
        /*
        Employee employee = new Employee("John", 30, "John@gmail.com");
        String json = gson.toJson(employee);
        */

        String json = "{\"first_Name\":\"John\",\"age\":30,\"mail\":\"John@gmail.com\"}";
        Employee employee = gson.fromJson(json, (Type) graficaBarrasTH.this);

        //Toast.makeText(getApplication(), employee, Toast.LENGTH_LONG).show();

       // response = [{"name":"Temperatura","score":"29.80"},{"name":"Humedad","score":"48.00"}]
        //JSONArray jsonarray = new JSONArray(response);


    }

    private void goMenu() {
        //startActivity(new Intent(getApplicationContext(),MainActivity.class));
        Intent intent = new Intent(graficaBarrasTH.this, MainActivity.class);
        startActivity(intent);
    }
}
