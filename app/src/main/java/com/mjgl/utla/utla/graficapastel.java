package com.mjgl.utla.utla;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class graficapastel extends AppCompatActivity {
    Toolbar toolbar;

    private PieChart pieChart;
    private TextView tv_res;
    private TextView tv_fecha;
    private ProgressDialog pd;
    private Button Btn_b;
    String f="";String h="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graficapastel);

        pd = new ProgressDialog(graficapastel.this);
        //pd.setMessage("loading");
        pd.setMessage("Estableciendo conexión con el servidor, " +
                "Por favor espere...");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_left));
        toolbar.setTitleTextColor(getResources().getColor(R.color.mycolor_blanco));
        toolbar.setTitleMargin(0,0,0,0);
        toolbar.setSubtitle("Lectura de Temp. °C y RH [%]");
        getSupportActionBar().setTitle("UTLA");

        //Función para evitar la rotación de la pantalla del CELULAR.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //y esto para pantalla completa (oculta incluso la barra de estado)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                graficapastel.this.finish();
            }
        });

        tv_res = (TextView)findViewById(R.id.tv_res);
        tv_fecha = (TextView)findViewById(R.id.tv_fecha);
        pieChart = (PieChart) findViewById(R.id.pieChart);
        Btn_b = (Button)findViewById(R.id.Btn_b) ;

        Btn_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //load_data_from_server();
            }
        });

        // enable hole and configure
        pieChart.setDrawHoleEnabled(true);
        //pieChart.setHoleColorTransparent(true); //Aca controlo el color de fondo del circulo del centro del grafico.
        pieChart.setHoleRadius(7);                //Acca controlo el ancho del circulo del centro del grafico
        pieChart.setTransparentCircleRadius(10);
        // enable rotation of the chart by touch
        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);

        pieChart.animateXY(1500, 1500);

        load_data_from_server();

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                // display msg when value selected
                if (e == null)
                    return;
                Toast toast = Toast.makeText(getApplicationContext(), e.getVal() + "", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }


    public PieChart configureChart(PieChart chart) {
        chart.setHoleColor(getResources().getColor(android.R.color.background_dark));
        chart.setHoleRadius(60f);
        chart.setDescription("");
        chart.setTransparentCircleRadius(5f);
        //chart.setDrawYValues(true);
        chart.setDrawCenterText(true);
        chart.setDrawHoleEnabled(false);
        chart.setRotationAngle(0);
        //chart.setDrawXValues(false);
        chart.setRotationEnabled(true);
        chart.setUsePercentValues(true);
        chart.setCenterText("MPAndroidChart\nLibrary");
        return chart;
    }


    private PieChart setData(PieChart chart) {
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        yVals1.add(new Entry(25, 0));
        yVals1.add(new Entry(75, 1));
        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("Good");
        xVals.add("Bad");
        PieDataSet set1 = new PieDataSet(yVals1, "");
        set1.setSliceSpace(0f);
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(getResources().getColor(android.R.color.holo_green_light));
        colors.add(getResources().getColor(android.R.color.holo_red_light));
        set1.setColors(colors);
        PieData data = new PieData(xVals, set1);
        chart.setData(data);
        chart.highlightValues(null);
        chart.invalidate();
        return chart;
    }


    private String infoSharedPreferences(){
        //Buscando datos en archivo credenciales.xml
        SharedPreferences preferences = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        String server = preferences.getString("servidor","Sin configurar aun.");
        String folder = preferences.getString("folder","Sin configurar aun.");
        return server + folder;
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

            load_data_from_server();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void load_data_from_server() {
        pd.show();
        //String url = "http://192.168.43.46/cocesna/service/grafica5.php";
        final String pc = infoSharedPreferences();
        String url = pc +"/grafica5.php";
        //String url = pc +"/service/grafica5.php";

        //String url = "http://" + pc +"/service/grafica5.php";

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
                                String fecha = jsonobject.getString("fecha").trim();
                                String hora = jsonobject.getString("hora").trim();

                                //Toast.makeText(grafica5.this, fecha, Toast.LENGTH_LONG).show();
                                f = fecha;h=hora;

                                tv_res.setText("[Temperatura : Humedad] = [" + temperatura + " : " + humedad + "]");
                                tv_fecha.setText("[Fecha: " + fecha + " ~ Hora: " + hora + "]");

                                /*creamos una lista para los valores Y*/
                                ArrayList<Entry> valsY = new ArrayList<Entry>();
                                valsY.add(new Entry((float) temperatura,0));
                                valsY.add(new Entry((float) humedad,1));

                                /*creamos una lista para los valores X*/
                                ArrayList<String> valsX = new ArrayList<String>();
                                valsX.add("Temp. °C");
                                valsX.add("Humedad [RH %]");

                                /*creamos una lista de colores*/
                                ArrayList<Integer> colors = new ArrayList<Integer>();
                                colors.add(getResources().getColor(R.color.red_flat));
                                colors.add(getResources().getColor(R.color.green_flat));

                                /*seteamos los valores de Y y los colores*/
                                PieDataSet set1 = new PieDataSet(valsY, "");
                                set1.setSliceSpace(3f);
                                set1.setColors(colors);

                                // customize legends
                                Legend l = pieChart.getLegend();
                                //l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
                                //l.setPosition(Legend.LegendPosition.PIECHART_CENTER);
                                l.setTextColor(Color.BLACK);
                                l.setTextSize(18);
                                l.setEnabled(true);
                                l.setXEntrySpace(50);
                                l.setYEntrySpace(5);

                                /*seteamos los valores de X*/
                                PieData data = new PieData(valsX, set1);
                                data.setValueTextSize(18f);             //tamaño del texto dentro del gráfico
                                data.setValueTextColor(Color.WHITE);    //color del texto dentro del gráfico
                                pieChart.setData(data);

                                // undo all highlights
                                pieChart.highlightValues(null);

                                // update pie chart
                                pieChart.invalidate();
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

                            Toast.makeText(getApplicationContext(), "No se logró establecer la conexión con el servidor: " + pc +
                                    " \n\nPor favor intente mas tarde. \n\nRecuerde respetar minúsculas y mayúsculas en la URL especificada." +
                                    "\n\nDISCULPAS POR LAS MOLESTIAS.", Toast.LENGTH_LONG).show();
                            pd.hide();
                        }
                    }
                }
        );

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

}
