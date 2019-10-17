package com.mjgl.utla.utla;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class graphic_dificult extends AppCompatActivity {
    private TextView text1;
    //RequestQueue requestQueue;               //BD
    ProgressDialog progressDialog;             //BD

    //String url = "http://192.168.43.46/cocesna/service/grafica1.php";
    //String url = "http://mjgl.com.sv/service/grafica1.php";

    public Double[] temperatura = new Double[10];
    public Double[] humedad = new Double[10];

    ArrayList<Double> hume = new ArrayList<Double>();
    public double humeX, humeY;

    //public Double[] Datos5 = new Double[10];

    public double Array_0;
    public double Array_1;
    public double Array_2;
    public double Array_3;
    public double Array_4;
    public double Array_5;
    public double Array_6;
    public double Array_7;
    public double Array_8;
    public double Array_9;

    public double Array_hum0;
    public double Array_hum1;
    public double Array_hum2;
    public double Array_hum3;
    public double Array_hum4;
    public double Array_hum5;
    public double Array_hum6;
    public double Array_hum7;
    public double Array_hum8;
    public double Array_hum9;

    //private TextView textOK;
    private TextView tv_array1;
    private TextView tv_array2;
    private TextView tv_array3;
    private TextView tv_array4;
    private TextView tv_array5;
    private TextView tv_array6;
    private TextView tv_array7;
    private TextView tv_array8;
    private TextView tv_array9;
    private TextView tv_array10;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphic_dificult);

        tv_array1 = (TextView) findViewById(R.id.tv_array1);
        tv_array2 = (TextView) findViewById(R.id.tv_array2);
        tv_array3 = (TextView) findViewById(R.id.tv_array3);
        tv_array4 = (TextView) findViewById(R.id.tv_array4);
        tv_array5 = (TextView) findViewById(R.id.tv_array5);
        tv_array6 = (TextView) findViewById(R.id.tv_array6);
        tv_array7 = (TextView) findViewById(R.id.tv_array7);
        tv_array8 = (TextView) findViewById(R.id.tv_array8);
        tv_array9 = (TextView) findViewById(R.id.tv_array9);
        tv_array10 = (TextView) findViewById(R.id.tv_array10);

        //textOK = (TextView) findViewById(R.id.textOK);

        //requestQueue = Volley.newRequestQueue(this);                //BD
        progressDialog = new ProgressDialog(this);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(progressBar.VISIBLE);

        grafica_db();
    }

    private String infoSharedPreferences() {
        //Buscando datos en archivo credenciales.xml
        SharedPreferences preferences = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        String server = preferences.getString("servidor", "Sin configurar.");
        String folder = preferences.getString("folder", "Sin configurar.");
        return server + folder;
    }

    public void grafica_db() {
        final String pc = infoSharedPreferences();
        //final String url = "http://" + pc +"/service/grafica1.php";
        final String url = pc + "/grafica1.php";
        //final String url = pc + "/service/grafica1.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        // Log.d("string", response);
                        //Toast.makeText(getApplicationContext(), url, Toast.LENGTH_SHORT).show();
                        try {
                            //System.out.println("RESPUESTA DE SERVIDOR : " + response);
                            if (response.equals("0")) {
                                //Toast.makeText(grafica1.this, response, Toast.LENGTH_LONG).show();
                                Toast toast = Toast.makeText(getApplicationContext(), "No se encontrarón datos que graficar\nen el servidor." +
                                        "\n\n", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            } else {

                                /*Toast toast = Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();*/

                                JSONArray jsonarray = new JSONArray(response);
                                String trama_temp = "";
                                String trama_hume = "";
                                String resulta = "";
                                String tem = "";
                                String hum = "";
                                String total = "";
                                Double a;
                                Double b;
                                Double z;
                                Double x;

                                for (int i = 0; i < jsonarray.length(); i++) {
                                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                                    tem = jsonobject.getString("temperatura").trim();          //Valores de la base de datos.
                                    hum = jsonobject.getString("humedad").trim();          //Valores de la base de datos.
                                    a = Double.parseDouble(tem);
                                    b = Double.parseDouble(hum);

                                    temperatura[i] = a;
                                    humedad[i] = b;

                                    if (i == 0) {
                                        //tv_array1.setText(String.valueOf(humedad[i]));
                                        //x = Double.parseDouble(String.valueOf(tv_array1.getText()));
                                        Array_0 = temperatura[i];
                                        Array_hum0 = humedad[i];

                                        tv_array1.setText(String.valueOf(Array_0));
                                    }
                                    if (i == 1) {
                                        //tv_array2.setText(String.valueOf(humedad[i]));
                                        //intent.putExtra("dos", tv_array2.getText().toString().trim());
                                        //intent.putExtra("tres", tv_array3.getText().toString().trim());
                                        Array_1 = temperatura[i];
                                        Array_hum1 = humedad[i];

                                        tv_array2.setText(String.valueOf(Array_1));
                                    }
                                    if (i == 2) {
                                        //tv_array3.setText(String.valueOf(humedad[i]));
                                        Array_2 = temperatura[i];
                                        Array_hum2 = humedad[i];

                                        tv_array3.setText(String.valueOf(Array_2));
                                    }
                                    if (i == 3) {
                                        //tv_array4.setText(String.valueOf(humedad[i]));
                                        Array_3 = temperatura[i];
                                        Array_hum3 = humedad[i];

                                        tv_array4.setText(String.valueOf(Array_3));
                                    }
                                    if (i == 4) {
                                        //tv_array5.setText(String.valueOf(humedad[i]));
                                        Array_4 = temperatura[i];
                                        Array_hum4 = humedad[i];

                                        tv_array5.setText(String.valueOf(Array_4));
                                    }
                                    if (i == 5) {
                                        //tv_array6.setText(String.valueOf(humedad[i]));
                                        Array_5 = temperatura[i];
                                        Array_hum5 = humedad[i];

                                        tv_array6.setText(String.valueOf(Array_5));
                                    }
                                    if (i == 6) {
                                        //tv_array7.setText(String.valueOf(humedad[i]));
                                        Array_6 = temperatura[i];
                                        Array_hum6 = humedad[i];

                                        tv_array7.setText(String.valueOf(Array_6));
                                    }
                                    if (i == 7) {
                                        //tv_array8.setText(String.valueOf(humedad[i]));
                                        Array_7 = temperatura[i];
                                        Array_hum7 = humedad[i];

                                        tv_array8.setText(String.valueOf(Array_7));
                                    }
                                    if (i == 8) {
                                        //tv_array9.setText(String.valueOf(humedad[i]));
                                        Array_8 = temperatura[i];
                                        Array_hum8 = humedad[i];

                                        tv_array9.setText(String.valueOf(Array_8));
                                    }
                                    if (i == 9) {
                                        //tv_array10.setText(String.valueOf(humedad[i]));
                                        Array_9 = temperatura[i];
                                        Array_hum9 = humedad[i];

                                        tv_array10.setText(String.valueOf(Array_9));
                                    }

                                }   //LLAME FIN DEL FOR.


                                //*/Double[] Datos5 = {28.20, 20.00, 18.10, 16.50, 18.00, 12.40, 18.60, 21.04, 18.09, 15.80};   //Serie 1
                                Double[] Datos5 = {Array_0, Array_1, Array_2, Array_3, Array_4, Array_5, Array_6, Array_7, Array_8, Array_9};   //Serie 1
                                Double[] Datos6 = {Array_hum0, Array_hum1, Array_hum2, Array_hum3, Array_hum4, Array_hum5, Array_hum6, Array_hum7, Array_hum8, Array_hum9};   //Serie 1

                                // ABRIMOS UNA NUEVA ACTIVITY Y MANDAMOS LOS DATOS QUE SE VAN A MOSTRAR
                                //Intent intent = new Intent(graphic_dificult.this, pruebas.class);

                                Intent intent = new Intent(getApplicationContext(), graficalinea.class);
                                //intent.putExtra("id", (Double) one);
                                intent.putExtra("array0", (Double) Datos5[0]);
                                intent.putExtra("array1", (Double) Datos5[1]);
                                intent.putExtra("array2", (Double) Datos5[2]);
                                intent.putExtra("array3", (Double) Datos5[3]);
                                intent.putExtra("array4", (Double) Datos5[4]);
                                intent.putExtra("array5", (Double) Datos5[5]);
                                intent.putExtra("array6", (Double) Datos5[6]);
                                intent.putExtra("array7", (Double) Datos5[7]);
                                intent.putExtra("array8", (Double) Datos5[8]);
                                intent.putExtra("array9", (Double) Datos5[9]);

                                intent.putExtra("h_array0", (Double) Datos6[0]);
                                intent.putExtra("h_array1", (Double) Datos6[1]);
                                intent.putExtra("h_array2", (Double) Datos6[2]);
                                intent.putExtra("h_array3", (Double) Datos6[3]);
                                intent.putExtra("h_array4", (Double) Datos6[4]);
                                intent.putExtra("h_array5", (Double) Datos6[5]);
                                intent.putExtra("h_array6", (Double) Datos6[6]);
                                intent.putExtra("h_array7", (Double) Datos6[7]);
                                intent.putExtra("h_array8", (Double) Datos6[8]);
                                intent.putExtra("h_array9", (Double) Datos6[9]);
                                startActivity(intent);
                                //finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null) {
                            progressDialog.dismiss();
                            //Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                           /* Toast toast = Toast.makeText(getApplicationContext(), "No se logró establecer la conexión con el servidor." +
                                    " Por favor intente mas tarde. \n\nDISCULPAS POR LAS MOLESTIAS.", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();*/

                            Toast.makeText(getApplicationContext(), "No se logró establecer la conexión con el servidor: " + pc +
                                    " \n\nPor favor intente mas tarde. \n\nRecuerde respetar minúsculas y mayúsculas en la URL especificada." +
                                    "\n\nDISCULPAS POR LAS MOLESTIAS.", Toast.LENGTH_LONG).show();
                            //pd.hide();
                        }
                    }
                }) {
            //protected Map<String, String> getParams() throws AuthFailureError {
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("senal", "1");
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);                //BD
        requestQueue.add(stringRequest);
        //MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}

