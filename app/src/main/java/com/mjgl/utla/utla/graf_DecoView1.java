package com.mjgl.utla.utla;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.EdgeDetail;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.charts.SeriesLabel;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class graf_DecoView1 extends AppCompatActivity {

    /* ESTA CLASE NO LA OCUPO EN ESTE PROYECTO
      * SOLO HICE UNA COPIA PORQUE PROBARÉ OTRA FORMA DE BUSQUEDA DE CARACTERES
       * EN UNA CADENA MAS EFICIENTE A LA QUE HE HECHO EN ESTA CLASE QUE ES COPIA.*/

    //float density = 20.0f;
    float humedity = 22.943f;
    float temperature = 80.78f;

    DecoView arcView, arcView1;
    TextView tvPorciento, tvPorciento1;
    Toolbar toolbar;
    AlertDialog.Builder dialogo;

    String i="";
    int correr=1;


    String addres="";
    String message="";
    String bandera="";

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new android.app.AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_accessibility)
                    .setTitle("Advertencia")
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
        setContentView(R.layout.activity_graf__deco_view1);

        //AL FINAL MEJOR CREE MI PROPIA TOOLBAR.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_left));
        toolbar.setTitleTextColor(getResources().getColor(R.color.mycolor_blanco));
        toolbar.setTitleMargin(0,0,0,0);
        toolbar.setSubtitle("Laboratorio Especializado");
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.white));
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

        //final DecoView arcView = (DecoView)findViewById(R.id.dynamicArcView);
        arcView = (DecoView)findViewById(R.id.dynamicArcView);
        //final TextView tvPorciento = (TextView) findViewById(R.id.tv_porciento);
        tvPorciento = (TextView) findViewById(R.id.tv_porciento);

        arcView1 = (DecoView)findViewById(R.id.dynamicArcView1);
        tvPorciento1 = (TextView) findViewById(R.id.tv_porciento1);

        DateFormat formatodate= new SimpleDateFormat("yyyy/MM/dd");
        String date= formatodate.format(new Date());
        DateFormat formatotime= new SimpleDateFormat("HH:mm:ss a");
        String time= formatotime.format(new Date());

        /*
        String paquete_SMS = "i"+String.valueOf(temperature)+","+String.valueOf(humedity)+"f";
        String dato_temperatura ="";
        Toast.makeText(this, ""+paquete_SMS, Toast.LENGTH_SHORT).show();
        String dato = "";
        while(correr<=paquete_SMS.length()){
            dato = paquete_SMS.substring(0, correr);
            if(dato.equals(",")) {
                dato_temperatura += paquete_SMS.substring(0, correr).toString();
                break;
            }
            correr++;
        }
        correr=0;
         */


         /*
         *      Haré un decodificador de la siguiente data
         *      (i)temperatura,humedad relativa(f)
         *     caracteriniciotrama-datotemperatura-separadordedatos-datohumedadrelativa-caracterfintrama
         */

        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            addres = extras.getString("MessageNumber");
            message = extras.getString("Message");
            bandera = extras.getString("Flag");
        }

        Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();

        String temperatura="";
        String humedad="";
        //String str = "i"+String.valueOf(temperature)+","+String.valueOf(humedity)+"f";
        String str = message;
        //for (int n = str.length ()-1; n>= 0; n--) {
        for (int n=1;n<=str.length()-1;n++) {
            char c = str.charAt(n);
            //System.out.println(sCadena.charAt(5));
            if(String.valueOf(c).equals(",")) {
                break;
            }
            temperatura += String.valueOf(c);
        }

        for (int n=0;n<=str.length()-1;n++) {
            char c = str.charAt(n);
            //System.out.println(sCadena.charAt(5));
            if(String.valueOf(c).equals(",")) {
                int k=n;
                for (int j=(k+1);j<=str.length()-1;j++) {
                    char x = str.charAt(j);
                    if(String.valueOf(x).equals("f")){
                        break;
                    }
                    humedad += String.valueOf(x);
                }
            }
        }
        //Toast.makeText(this, ""+temperatura+","+humedad, Toast.LENGTH_LONG).show();

        //String sCadena = "Hola Mundo";
        //String sSubCadena = sCadena.substring(0, 1);

        humedadRH(humedity);
        temperatura(temperature);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_graficalinea, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button_green_round, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id==R.id.action_add){
            Toast.makeText(getApplicationContext(),"Buscando información reciente.\n" +
                    "Espere por favor...",Toast.LENGTH_SHORT).show();

            //load_data_from_server();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    void humedadRH(float RH){
        //arcView.configureAngles(360, 0);
        arcView.configureAngles(360, 0);
        //arcView.disableHardwareAccelerationForDecoView();
        int series1Index = 0;

        //255,115,110,110 = #FF736E6E    -> Color gray
        //arcView.addSeries(new SeriesItem.Builder(Color.argb(255, 218, 218, 218))   //Color blanco.
        arcView.addSeries(new SeriesItem.Builder(Color.argb(255, 115, 110, 110))
                .setRange(0, 100, 100)
                .setInitialVisibility(false)
                .setLineWidth(20f)
                .build());
        //255:3:16:201 = #FF0310C9
        final SeriesItem seriesItem1 = new SeriesItem.Builder(Color.parseColor("#FF0310C9"))
                //final  SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(200, 255, 0, 0))
                //final SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(255, 0, 0, 0), Color.argb(255, 196, 0, 0))
                //final  SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(200, 255, 0, 0))
                .setRange(0, 100, 0)
                .setLineWidth(50f)
                .addEdgeDetail(new EdgeDetail(EdgeDetail.EdgeType.EDGE_OUTER, Color.parseColor("#0b388e"), 0.4f))
                .setSeriesLabel(new SeriesLabel.Builder("Humedad Relativa: %.0f%% RH").build())
                .setInterpolator(new DecelerateInterpolator())
                .setShowPointWhenEmpty(true)
                //.setCapRounded(true)
                //.setInset(new PointF(30, 30))
                //.setDrawAsPoint(false)
                //.setSpinClockwise(true)
                //.setShadowSize(30)
                //.setShadowColor(Color.DKGRAY)
                .setCapRounded(true)
                .setInset(new PointF(20f, 20f))
                .setDrawAsPoint(false)
                .setSpinClockwise(true)
                .setSpinDuration(1000)
                .setChartStyle(SeriesItem.ChartStyle.STYLE_DONUT)
                .setInitialVisibility(false)
                .build();

        series1Index = arcView.addSeries(seriesItem1);

        arcView.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW, true)
                .setDelay(1000)
                .setDuration(2000)
                .build()
        );

        arcView.addEvent(new DecoEvent.Builder(RH).setIndex(series1Index).setDelay(4000).build());
        //arcView.addEvent(new DecoEvent.Builder(25).setIndex(series1Index).setDelay(4000).build());
        //arcView.addEvent(new DecoEvent.Builder(100).setIndex(series1Index).setDelay(8000).build());
        //arcView.addEvent(new DecoEvent.Builder(10).setIndex(series1Index).setDelay(12000).build());

        seriesItem1.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                //obtenemos el porcentaje a mostrar
                float percentFilled = ((currentPosition - seriesItem1.getMinValue()) / (seriesItem1.getMaxValue() - seriesItem1.getMinValue()));
                //se lo pasamos al TextView
                tvPorciento.setText(String.format("%.0f%%", percentFilled * 100f));
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });
    }

    void temperatura(float t){
        //arcView1.configureAngles(180, 0);
        arcView1.configureAngles(270, 0);

        //arcView.disableHardwareAccelerationForDecoView();
        int series1Index = 0;
        //arcView1.addSeries(new SeriesItem.Builder(Color.argb(255, 218, 218, 218))
        arcView1.addSeries(new SeriesItem.Builder(Color.argb(255, 115, 110, 110))
                .setRange(0, 100, 100)
                .setInitialVisibility(false)
                .setLineWidth(20f)
                .build());
        //final SeriesItem seriesItem1 = new SeriesItem.Builder(Color.parseColor("#FFFF8800"))
                //final  SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(200, 255, 0, 0))
                final SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(255, 0, 0, 0), Color.argb(255, 196, 0, 0))
                //final  SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(200, 255, 0, 0))
                .setRange(0, 100, 0)
                .setLineWidth(50f)
                .addEdgeDetail(new EdgeDetail(EdgeDetail.EdgeType.EDGE_OUTER, Color.parseColor("#0b388e"), 0.4f))
                .setSeriesLabel(new SeriesLabel.Builder("Temperatura: %.0f°C").build())
                .setInterpolator(new DecelerateInterpolator())
                .setShowPointWhenEmpty(true)
                //.setCapRounded(true)
                //.setInset(new PointF(30, 30))
                //.setDrawAsPoint(false)
                //.setSpinClockwise(true)
                //.setShadowSize(30)
                //.setShadowColor(Color.DKGRAY)
                .setCapRounded(true)
                .setInset(new PointF(20f, 20f))
                .setDrawAsPoint(false)
                .setSpinClockwise(true)
                .setSpinDuration(1000)
                .setChartStyle(SeriesItem.ChartStyle.STYLE_DONUT)
                .setInitialVisibility(false)
                .build();

        series1Index = arcView1.addSeries(seriesItem1);

        arcView1.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW, true)
                .setDelay(1000)
                .setDuration(2000)
                .build()
        );

        arcView1.addEvent(new DecoEvent.Builder(t).setIndex(series1Index).setDelay(4000).build());
        //arcView.addEvent(new DecoEvent.Builder(25).setIndex(series1Index).setDelay(4000).build());
        //arcView.addEvent(new DecoEvent.Builder(100).setIndex(series1Index).setDelay(8000).build());
        //arcView.addEvent(new DecoEvent.Builder(10).setIndex(series1Index).setDelay(12000).build());

        seriesItem1.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                //obtenemos el porcentaje a mostrar
                float percentFilled = ((currentPosition - seriesItem1.getMinValue()) / (seriesItem1.getMaxValue() - seriesItem1.getMinValue()));
                //se lo pasamos al TextView
                tvPorciento1.setText(String.format("%.0f°C", percentFilled * 100f));
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });
    }


    private void goMenu(){
        //startActivity(new Intent(getApplicationContext(),MainActivity.class));
        String mensaje = "¿Realmente desea salir?";
        dialogo = new AlertDialog.Builder(graf_DecoView1.this);
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
