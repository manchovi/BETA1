package com.mjgl.utla.utla;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class graficabarra extends AppCompatActivity {
    Toolbar toolbar;

    private ProgressDialog pd;
    private TextView tv_res;
    private TextView tv_fh;
    private Button Btn_a;

    private ImageView ivLuz1, ivLuz2, ivLuz3, ivLuz4, ivLuz5, ivLuz6, ivLuz7, ivLuz8;
    private TextView tvLuz1Alto, tvLuz2Alto, tvLuz3Alto, tvLuz4Alto, tvLuz5Alto, tvLuz6Alto, tvLuz7Alto,tvLuz8Alto;


    ArrayList<BarDataSet> yAxis;
    ArrayList<BarEntry> yValues;
    ArrayList<String> xAxis1;
    BarEntry values ;
    BarChart chart;

    BarData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graficabarra2);

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
                graficabarra.this.finish();
            }
        });


        pd = new ProgressDialog(graficabarra.this);
        //pd.setMessage("loading...");
        pd.setMessage("Estableciendo conexión con el servidor, " +
                "Por favor espere...");

        tv_res = (TextView)findViewById(R.id.tv_res);
        tv_fh = (TextView)findViewById(R.id.tv_fh);
        Btn_a = (Button)findViewById(R.id.Btn_a);

        ivLuz1 = (ImageView) findViewById(R.id.ivLuz1);
        ivLuz2 = (ImageView) findViewById(R.id.ivLuz2);
        ivLuz3 = (ImageView) findViewById(R.id.ivLuz3);
        ivLuz4 = (ImageView) findViewById(R.id.ivLuz4);
        ivLuz5 = (ImageView) findViewById(R.id.ivLuz5);
        ivLuz6 = (ImageView) findViewById(R.id.ivLuz6);
        ivLuz7 = (ImageView) findViewById(R.id.ivLuz7);
        ivLuz8 = (ImageView) findViewById(R.id.ivLuz8);

        tvLuz1Alto = (TextView)findViewById(R.id.tvLuz1Alto);
        tvLuz2Alto = (TextView)findViewById(R.id.tvLuz2Alto);
        tvLuz3Alto = (TextView)findViewById(R.id.tvLuz3Alto);
        tvLuz4Alto = (TextView)findViewById(R.id.tvLuz4Alto);
        tvLuz5Alto = (TextView)findViewById(R.id.tvLuz5Alto);
        tvLuz6Alto = (TextView)findViewById(R.id.tvLuz6Alto);
        tvLuz7Alto = (TextView)findViewById(R.id.tvLuz7Alto);
        tvLuz8Alto = (TextView)findViewById(R.id.tvLuz8Alto);


        Btn_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load_data_from_server();
            }
        });


        chart = (BarChart) findViewById(R.id.chart);
        load_data_from_server();

    }

    private String infoSharedPreferences(){
        //Buscando datos en archivo credenciales.xml
        SharedPreferences preferences = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        String server = preferences.getString("servidor","Sin configurar aun.");
        String folder = preferences.getString("folder","Sin configurar aun.");
        return server + folder;
    }


    public void load_data_from_server() {
        pd.show();
        //String url = "http://192.168.43.46/cocesna/service/grafok.php";
        final String pc = infoSharedPreferences();
        String url = pc + "/grafok1.php";
        //String url = pc + "/service/grafok1.php";

        //String url = "http://"+ pc + "/service/grafok.php";
        xAxis1 = new ArrayList<>();
        yAxis = null;
        yValues = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("string",response);

                        try {
                            System.out.println("RESPUESTA DE SERVIDOR : "+response);
                            Toast.makeText(graficabarra.this, response, Toast.LENGTH_LONG).show();

                            JSONArray jsonarray = new JSONArray(response);
                            String cadena="";String cadena1="";

                            String lamp1="", lamp2="", lamp3="", lamp4="";
                            String lamp5="", lamp6="", lamp7="", lamp8="";

                            for(int i=0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                String score = jsonobject.getString("score").trim();          //Valores de la base de datos.
                                String name = jsonobject.getString("name").trim();
                                String fh = jsonobject.getString("fh").trim();
                                //String hora = jsonobject.getString("hora").trim();

                                String par1 = jsonobject.getString("par1").trim();
                                String par2 = jsonobject.getString("par2").trim();
                                String par3 = jsonobject.getString("par3").trim();
                                String par4 = jsonobject.getString("par4").trim();

                                //Toast.makeText(graficabarra.this, ""+par1, Toast.LENGTH_LONG).show();

                                //LUZ1 Y LUZ2
                                if(i==0 && par1.equals("1")){
                                    ivLuz1.setImageResource(R.drawable.ic_luz13);
                                    tvLuz1Alto.setText("Luminaria 1 : ON");
                                }else if(i==0 && par1.equals("0")){
                                    ivLuz1.setImageResource(R.drawable.ic_luz222);
                                    tvLuz1Alto.setText("Luminaria 1 : OFF");
                                }

                                if(i==1 && par1.equals("1")){
                                    ivLuz2.setImageResource(R.drawable.ic_luz13);
                                    tvLuz2Alto.setText("Luminaria 2 : ON");
                                }else if(i==1 && par1.equals("0")){
                                    ivLuz2.setImageResource(R.drawable.ic_luz222);
                                    tvLuz2Alto.setText("Luminaria 2 : OFF");
                                }

                                //LUZ3 Y LUZ4
                                if(i==0 && par2.equals("1")){
                                    ivLuz3.setImageResource(R.drawable.ic_luz13);
                                    tvLuz3Alto.setText("Luminaria 3 : ON");
                                }else if(i==0 && par2.equals("0")){
                                    ivLuz3.setImageResource(R.drawable.ic_luz222);
                                    tvLuz3Alto.setText("Luminaria 3 : OFF");
                                }

                                if(i==1 && par2.equals("1")){
                                    ivLuz4.setImageResource(R.drawable.ic_luz13);
                                    tvLuz4Alto.setText("Luminaria 4 : ON");
                                }else if(i==1 && par2.equals("0")){
                                    ivLuz4.setImageResource(R.drawable.ic_luz222);
                                    tvLuz4Alto.setText("Luminaria 4 : OFF");
                                }


                                //LUZ5 Y LUZ6
                                if(i==0 && par3.equals("1")){
                                    ivLuz5.setImageResource(R.drawable.ic_luz13);
                                    tvLuz5Alto.setText("Luminaria 5 : ON");
                                }else if(i==0 && par3.equals("0")){
                                    ivLuz5.setImageResource(R.drawable.ic_luz222);
                                    tvLuz5Alto.setText("Luminaria 5 : OFF");
                                }

                                if(i==1 && par3.equals("1")){
                                    ivLuz6.setImageResource(R.drawable.ic_luz13);
                                    tvLuz6Alto.setText("Luminaria 6 : ON");
                                }else if(i==1 && par3.equals("0")){
                                    ivLuz6.setImageResource(R.drawable.ic_luz222);
                                    tvLuz6Alto.setText("Luminaria 6 : OFF");
                                }

                                //LUZ7 Y LUZ8
                                if(i==0 && par4.equals("1")){
                                    ivLuz7.setImageResource(R.drawable.ic_luz13);
                                    tvLuz7Alto.setText("Luminaria 7 : ON");
                                }else if(i==0 && par4.equals("0")){
                                    ivLuz7.setImageResource(R.drawable.ic_luz222);
                                    tvLuz7Alto.setText("Luminaria 7 : OFF");
                                }

                                if(i==1 && par4.equals("1")){
                                    ivLuz8.setImageResource(R.drawable.ic_luz13);
                                    tvLuz8Alto.setText("Luminaria 8 : ON");
                                }else if(i==1 && par4.equals("0")){
                                    ivLuz8.setImageResource(R.drawable.ic_luz222);
                                    tvLuz8Alto.setText("Luminaria 8 : OFF");
                                }



                                if (i<1){
                                    cadena = cadena + "[" + score + " °C";
                                    cadena1 = cadena1 + "[" + fh ;
                                    lamp1 = "Luz1:" + par1 + ":Luz2:";
                                    lamp2 = "Luz3:" + par2 + ":Luz4:";

                                }else{
                                    cadena = cadena + " : " + score + " % RH]";
                                    cadena1 = cadena1 + " : " + fh + "]";
                                    lamp1 = lamp1 + par1;
                                    lamp2 = lamp2 + par2;
                                }

                                tv_res.setText("[T. °C : RH] = " + cadena);
                                tv_fh.setText("[Fecha : Hora] = " + cadena1);
                                //Fecha: 00/00/000 Hora: 00:00:00

                                xAxis1.add(name);
                                values = new BarEntry(Float.parseFloat(String.valueOf(score)),i);
                                yValues.add(values);
                            }

                            //Toast.makeText(graficabarra.this, lamp1+" "+lamp2, Toast.LENGTH_LONG).show();
                            Toast.makeText(graficabarra.this, cadena, Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        BarDataSet barDataSet1 = new BarDataSet(yValues, "ESTA VISUALIZANDO LA ULTIMA LECTURA REALIZADA.");
                        //barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);
                        //barDataSet1.setColors(ColorTemplate.JOYFUL_COLORS);
                        barDataSet1.setColors(ColorTemplate.PASTEL_COLORS);     //me gusto.
                        //barDataSet1.setColors(ColorTemplate.VORDIPLOM_COLORS);
                        //barDataSet1.setColors(ColorTemplate.LIBERTY_COLORS);

                        //barDataSet1.setColor(Color.rgb(0, 182, 239));


//                        BarDataSet barDataSet1 = new BarDataSet(yValues, "Goals LaLiga 16/17");
//                        barDataSet1.setColor(Color.rgb(0, 82, 159));

                        yAxis = new ArrayList<>();
                        yAxis.add(barDataSet1);
                        String names[]= xAxis1.toArray(new String[xAxis1.size()]);
                        data = new BarData(names,yAxis);
                        //data = new BarData(names, (IBarDataSet) yAxis);
                        chart.setData(data);
                        chart.setDescription("");
                        chart.animateXY(2000, 2000);
                        chart.invalidate();
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
            //AyudaBT();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
