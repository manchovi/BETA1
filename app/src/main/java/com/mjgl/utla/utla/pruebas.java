package com.mjgl.utla.utla;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYStepMode;

import java.util.Arrays;

public class pruebas extends AppCompatActivity {
    private TextView text0;
    private TextView text1;
    private TextView text2;

    private Button BtnG_TH;

    private XYPlot myXYPlotTH;
    private XYPlot myXYPlotchovi;
    private XYPlot myXYPlot1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pruebas);

        Toast.makeText(this, "Entre por fin...", Toast.LENGTH_SHORT).show();

        BtnG_TH = (Button)findViewById(R.id.BtnG_TH);
        BtnG_TH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(pruebas.this, graphic_dificult.class);
                startActivity(intent);
                finish();
            }
        });

        myXYPlotTH = (XYPlot)findViewById(R.id.myXYPlotTH);
        myXYPlotTH.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 0.5); //CONTROLA EL INCREMENTO EN EL EJE X
        myXYPlotTH.setRangeStep(XYStepMode.INCREMENT_BY_VAL, 5);    //CONTOLA EL INCREMENTE EN EL EJE Y
        myXYPlotTH.getGraphWidget().getGridBackgroundPaint().setColor(Color.rgb(0,0,0));      //Color de fondo
        myXYPlotTH.getGraphWidget().getDomainGridLinePaint().setColor(Color.rgb(0, 0, 0));    //Color de lineas x.
        myXYPlotTH.getGraphWidget().getRangeGridLinePaint().setColor(Color.rgb(0, 120, 190)); //Color de lineas y.


        myXYPlot1 = (XYPlot)findViewById(R.id.myXYPlot1);
        myXYPlot1.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 0.5); //CONTROLA EL INCREMENTO EN EL EJE X
        myXYPlot1.setRangeStep(XYStepMode.INCREMENT_BY_VAL, 5);    //CONTOLA EL INCREMENTE EN EL EJE Y
        myXYPlot1.getGraphWidget().getGridBackgroundPaint().setColor(Color.rgb(0,0,0));      //Color de fondo
        myXYPlot1.getGraphWidget().getDomainGridLinePaint().setColor(Color.rgb(0, 0, 0));    //Color de lineas x.
        myXYPlot1.getGraphWidget().getRangeGridLinePaint().setColor(Color.rgb(0, 120, 190)); //Color de lineas y.
        //myXYPlotchovi.setRangeBoundaries(0, 5, BoundaryMode.FIXED);


        myXYPlotchovi = (XYPlot)findViewById(R.id.myXYPlotchovi);
        myXYPlotchovi.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 0.5); //CONTROLA EL INCREMENTO EN EL EJE X
        myXYPlotchovi.setRangeStep(XYStepMode.INCREMENT_BY_VAL, 5);    //CONTOLA EL INCREMENTE EN EL EJE Y
        myXYPlotchovi.getGraphWidget().getGridBackgroundPaint().setColor(Color.rgb(0,0,0));      //Color de fondo
        myXYPlotchovi.getGraphWidget().getDomainGridLinePaint().setColor(Color.rgb(0, 0, 0));    //Color de lineas x.
        myXYPlotchovi.getGraphWidget().getRangeGridLinePaint().setColor(Color.rgb(0, 120, 190)); //Color de lineas y.
        //myXYPlotchovi.setRangeBoundaries(0, 5, BoundaryMode.FIXED);

        text0 = (TextView)findViewById(R.id.text0);
        text1 = (TextView)findViewById(R.id.text1);
        text2 = (TextView)findViewById(R.id.text2);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            //String url = (String) bundle.get("direccion");
            //double persona_autorizada = bundle.getDouble("id");
            //float persona_autorizada = bundle.getFloat("id");
            //String nombre = bundle.getString("nombre");
            double pos0 = bundle.getDouble("array0");
            double pos1 = bundle.getDouble("array1");
            double pos2 = bundle.getDouble("array2");
            double pos3 = bundle.getDouble("array3");
            double pos4 = bundle.getDouble("array4");
            double pos5 = bundle.getDouble("array5");
            double pos6 = bundle.getDouble("array6");
            double pos7 = bundle.getDouble("array7");
            double pos8 = bundle.getDouble("array8");
            double pos9 = bundle.getDouble("array9");

            double h_pos0 = bundle.getDouble("h_array0");
            double h_pos1 = bundle.getDouble("h_array1");
            double h_pos2 = bundle.getDouble("h_array2");
            double h_pos3 = bundle.getDouble("h_array3");
            double h_pos4 = bundle.getDouble("h_array4");
            double h_pos5 = bundle.getDouble("h_array5");
            double h_pos6 = bundle.getDouble("h_array6");
            double h_pos7 = bundle.getDouble("h_array7");
            double h_pos8 = bundle.getDouble("h_array8");
            double h_pos9 = bundle.getDouble("h_array9");

            text1.setText(pos0 + ", " + pos1 + ", " + pos2 + ", " + pos3 + ", " + pos4 + ", " +
                          pos5 + ", " + pos6 + ", " + pos7 + ", " + pos8 + ", " + pos9);

            text2.setText(h_pos0 + ", " + h_pos1 + ", " + h_pos2 + ", " + h_pos3 + ", " + h_pos4 + ", " +
                    h_pos5 + ", " + h_pos6 + ", " + h_pos7 + ", " + h_pos8 + ", " + h_pos9);

            text0.setText("Temp=" + text1.getText() + "\n" + "Hume=" + text2.getText());

            Double[] vector_temp = {pos0, pos1, pos2, pos3, pos4, pos5, pos6, pos7, pos8, pos9};   //Serie 1
            Double[] vector_hum = {h_pos0, h_pos1, h_pos2, h_pos3, h_pos4, h_pos5, h_pos6, h_pos7, h_pos8, h_pos9};   //Serie 1

            /*for (int i = 0; i < vector_temp.length; i++) {
            System.out.println("Elemento: " + vector_temp[i]);
            }
            System.out.println("Tamaño / size: " + vector_temp.length);*/

            //DE ACA HE MOVIDO EL CODIGO QUE ANDO PROBANDO...
            XYSeries series1 = new SimpleXYSeries(Arrays.asList(vector_temp), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,"Temperatura");  //Nombre de la primera serie.
            //Modificamos los colores de la primera serie
            //LineAndPointFormatter series1Format = new LineAndPointFormatter(Color.rgb(0, 200, 0), Color.rgb(0, 100, 0), Color.rgb(150, 190, 150), null);
                                                                                //Color de la linea,    Color del punto,                 Relleno
            //LineAndPointFormatter series1Format = new LineAndPointFormatter(Color.rgb(9, 20, 202),Color.rgb(245, 4, 8),0x000000,null);
            LineAndPointFormatter series1Format = new LineAndPointFormatter(Color.rgb(128, 10, 12),Color.rgb(245, 4, 8),0x000000,null);
            //Uns vez definida la serie (datos y estilo), la añadimos al panel
            myXYPlotchovi.addSeries(series1, series1Format);



            /*******************************************************************************************/
            //GRAFICANDO LINEAS EN LA GRAFICA DE HUMEDAD.
            //DE ACA HE MOVIDO EL CODIGO QUE ANDO PROBANDO...
            XYSeries series2 = new SimpleXYSeries(Arrays.asList(vector_hum),
                    SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, //Solo valores verticales
                    "Humedad");                        //Nombre de la primera serie.

            //Modificamos los colores de la primera serie
            /*LineAndPointFormatter series2Format = new LineAndPointFormatter(
                    Color.rgb(0, 200, 0),                //Color de la linea
                    Color.rgb(0, 100, 0),                //Color del punto
                    Color.rgb(150, 190, 150), null); //Relleno*/
            LineAndPointFormatter series2Format = new LineAndPointFormatter(Color.rgb(2, 236, 14),Color.rgb(245, 4, 8),0x000000,null);

            //Uns vez definida la serie (datos y estilo), la añadimos al panel
            myXYPlot1.addSeries(series2, series2Format);
            /*******************************************************************************************/



            /*ACONTINUACION VOY A GRAFICAR TEMPERATURA Y HUMEDAD EN UNA MISMA GRÁFICA*/
            XYSeries series11 = new SimpleXYSeries(
                    Arrays.asList(vector_temp),
                    SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, //Solo valores verticales
                    "TEMPERATURA");                        //Nombre de la primera serie.
            XYSeries series22 = new SimpleXYSeries(
                    Arrays.asList(vector_hum),
                    SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, //Solo valores verticales
                    "HUMEDAD");
            //Modificamos los colores de la primera serie
            /*LineAndPointFormatter series11Format = new LineAndPointFormatter(
                    Color.rgb(0,200,0),                //Color de la linea
                    Color.rgb(0,100,0),                //Color del punto
                    Color.rgb(150,190,150),null); //Relleno*/
            LineAndPointFormatter series11Format = new LineAndPointFormatter(Color.rgb(250, 1, 13),Color.rgb(0, 255, 247),0x000000,null);
            //Uns vez definida la serie (datos y estilo), la añadimos al panel
            myXYPlotTH.addSeries(series11, series11Format);
            //Modificamos los colores de la segunda serie
            /*LineAndPointFormatter series22Format = new LineAndPointFormatter(
                    Color.rgb(0,0,200),          //Color de la linea
                    Color.rgb(0,0,100),          //Color del punto
                    Color.rgb(221,240,244),null);  //Relleno*/
            LineAndPointFormatter series22Format = new LineAndPointFormatter(Color.rgb(2, 236, 14),Color.rgb(245, 4, 8),0x000000,null);
            //Uns vez definida la serie (datos y estilo), la añadimos al panel
            myXYPlotTH.addSeries(series22, series22Format);

        }
    }
}
