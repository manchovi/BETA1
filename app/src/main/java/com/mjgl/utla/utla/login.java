package com.mjgl.utla.utla;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class login extends AppCompatActivity {
    int conta = 0;
    Dialog myDialog;
    Dialog MyDialog1;
    Button positivePopupBtn, btnAccept;
    ImageView closePopupPositiveImg;

    EditText etEmail, etClave;
    TextInputLayout tiEmail, tiClave;
    Button btnLogin;

    boolean estado_correo;
    Spinner combo;

    //SharedPreferences sharedpreferences;

    //private Cursor fila;
    dbSQLiteHelper base = new dbSQLiteHelper(this);

    RelativeLayout rellay0, rellay1, rellay2, rellay3;
    //Button btnOlvidoClave;
    ImageView imgView_logo;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
            rellay3.setVisibility(View.VISIBLE);
        }
    };


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new android.app.AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Cerrar")
                    .setMessage("Confirme que realmente desea cerrar la App.")
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {//un listener que al pulsar, cierre la aplicacion
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Salir
                            //login.this.finish();
                            finishAffinity();
                            //finish();
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
        setContentView(R.layout.activity_login);

        //Función para evitar la rotación de la pantalla del CELULAR.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //y esto para pantalla completa (oculta incluso la barra de estado)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        rellay1 = (RelativeLayout) findViewById(R.id.rellay1);
        rellay2 = (RelativeLayout) findViewById(R.id.rellay2);
        rellay3 = (RelativeLayout) findViewById(R.id.rellay3);

        etEmail = (EditText)findViewById(R.id.etEmail);
        etClave = (EditText)findViewById(R.id.etClave);
        tiEmail= (TextInputLayout)findViewById(R.id.tiEmail);
        tiClave= (TextInputLayout)findViewById(R.id.tiClave);
        btnLogin = (Button)findViewById(R.id.btnLogin);

        myDialog = new Dialog(this);


        handler.postDelayed(runnable, 0); //2000 is the timeout for the splash

        //imgView_logo = (ImageView) findViewById(R.id.imgView_logo);
        //imgView_logo.setVisibility(View.GONE);

        Button btnRegistrar = (Button) findViewById(R.id.btnRegistrar);
        btnRegistrar.setPaintFlags(btnRegistrar.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        Button btnOlvidoClave = (Button) findViewById(R.id.btnOlvidoClave);
        btnOlvidoClave.setPaintFlags(btnOlvidoClave.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowRegister();
            }
        });


        btnOlvidoClave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowrecuperarClave1();
            }
        });

       /* btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
    }

    public void ingresar(View v){
        //dbSQLiteHelper admin=new dbSQLiteHelper(this,"dbutla",null,1);
        dbSQLiteHelper admin=new dbSQLiteHelper(this);
        SQLiteDatabase db=admin.getWritableDatabase();

        String usuario=etEmail.getText().toString();
        String contrasena=etClave.getText().toString();

        if (Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()== false) {
            //mEmail.setBackgroundColor(Color.GREEN);
            etEmail.setText(null);
            tiEmail.setError("Correo invalido.");
            etEmail.requestFocus();
            estado_correo = false;
        }else {
                //mEmail.setBackgroundColor(Color.WHITE);
                //int myDynamicColor = Color.parseColor("#FFFF00"); // Here you can pass a string taken from the user or from wherever you want.
                //mEmail.setBackgroundColor(myDynamicColor);
                //mEmail.setBackgroundColor(Color.parseColor("#ffffff"));
                estado_correo = true;
                tiEmail.setError(null);
              }

        //if(etEmail.getText().length()==0 || etClave.getText().length()==0) {
        if(estado_correo==false && (usuario.length()==0 || contrasena.length()==0)) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_LONG).show();
            etEmail.requestFocus();

        }else{
            //Cursor fila = bd.rawQuery("select nombre, importe from empleados where codigo="+codigo, null);
            //fila=db.rawQuery("select usuario,contrasena from usuarios where usuario='"+usuario+"' and contrasena='"+contrasena+"'",null);
            //Cursor fila=db.rawQuery("select usuario,clave from usuarios where usuario='"+usuario+"' and clave='"+contrasena+"'",null);
            Cursor fila=db.rawQuery("select codigo,usuario,clave,nombres,apellidos from usuarios where usuario='"+usuario+"' and clave='"+contrasena+"'",null);
            //preguntamos si el cursor tiene algun valor almacenado.
            //if(fila.moveToFirst()==true) {
            if(fila.moveToFirst()){
                //capturamos los valores del cursos y lo almacenamos en variable
                int codigo = fila.getInt(0);
                String id = String.valueOf(codigo);
                String usua = fila.getString(1);
                String pass = fila.getString(2);
                String nombres = fila.getString(3);
                String apellidos = fila.getString(4);
                //preguntamos si los datos ingresados son iguales
                if (usuario.equals(usua) && contrasena.equals(pass)) {
                    //OBTENIENDO LA FECHA Y HORA ACTUAL DEL SISTEMA.
                    DateFormat formatodate= new SimpleDateFormat("yyyy/MM/dd");
                    String date= formatodate.format(new Date());
                    DateFormat formatotime= new SimpleDateFormat("HH:mm:ss a");
                    String time= formatotime.format(new Date());

                    //Archivo para variables de inicio de sesión
                    SharedPreferences preferences = getSharedPreferences("variablesesion", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();

                    editor.putInt("tipo", codigo);
                    editor.putString("nombrecompleto", nombres +" "+apellidos);
                    editor.putString("nombres", nombres);
                    editor.putString("apellidos", apellidos);
                    editor.putString("usuario", usua);
                    editor.putString("fh", date +" "+ time);

                    if(codigo==1){
                        editor.putBoolean("habilitaOpciones", true);
                    }else{
                        editor.putBoolean("habilitaOpciones", false);
                    }

                    editor.commit();

                    //Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    Intent menuPrincipal = new Intent(this, MainActivity.class);
                    //menuPrincipal.putExtra("usuario", usuario.toString());
                    menuPrincipal.putExtra("senal", "1");
                    menuPrincipal.putExtra("codigo", codigo);
                    menuPrincipal.putExtra("usuario", nombres + " " + apellidos);
                    startActivity(menuPrincipal);
                    finish();
                    //limpiamos las las cajas de texto
                    etEmail.setText("");
                    etClave.setText("");
                   }
                }else {
                    /*Toast toast = Toast.makeText(getApplicationContext(), "Campo respuesta es obligatorio", Toast.LENGTH_SHORT);
                      toast.setGravity(Gravity.CENTER, 0, 0);
                      toast.show();*/
                        //limpiamos las las cajas de texto
                        etEmail.setText("");
                        etClave.setText("");
                        etEmail.requestFocus();
                        Toast.makeText(getApplicationContext(), "Sorry. Usuario desconocido. \nVuelta a intentarlo nuevamente.", Toast.LENGTH_LONG).show();
                       }
            }
        }


    private void ShowRegister() {
        myDialog.setContentView(R.layout.dialog_registrarme);
        myDialog.setCancelable(false);

        closePopupPositiveImg = (ImageView)myDialog.findViewById(R.id.closePopupPositiveImg);
        btnAccept = (Button)myDialog.findViewById(R.id.btnAccept);

        TextInputLayout ti_Nombres = (TextInputLayout)myDialog.findViewById(R.id.ti_Nombres);
        final EditText et_Nombres = (EditText)myDialog.findViewById(R.id.et_Nombres);

        TextInputLayout ti_Apellidos = (TextInputLayout)myDialog.findViewById(R.id.ti_Apellidos);
        final EditText et_Apellidos = (EditText)myDialog.findViewById(R.id.et_Apellidos);

        final TextInputLayout tiUsuario = (TextInputLayout)myDialog.findViewById(R.id.tiUsuario);
        final EditText etUsuario = (EditText)myDialog.findViewById(R.id.etUsuario);

        TextInputLayout tiPassword1 = (TextInputLayout)myDialog.findViewById(R.id.tiPassword1);
        final EditText etPassword1 = (EditText)myDialog.findViewById(R.id.etPassword1);

        TextInputLayout tiPassword2 = (TextInputLayout)myDialog.findViewById(R.id.tiPassword2);
        final EditText etPassword2 = (EditText)myDialog.findViewById(R.id.etPassword2);

        combo = (Spinner)myDialog.findViewById(R.id.combo);  //Aca tomo la pregunta se seguridad seleccionada.

        TextInputLayout tiRespuesta = (TextInputLayout)myDialog.findViewById(R.id.tiRespuesta);
        final EditText etRespuesta = (EditText)myDialog.findViewById(R.id.etRespuesta);

        conta = 0;
        final String[] lista =new String[]{
                "[Escoja pregunta de seguridad]",
                "¿Cuál es el nombre de tu universidad favorita?",
                "¿Cuál es la ubicación de tu universidad favorita?",
                "¿Cuál es el nombre de tu profesor favorito?",
                "¿Cuál es el nombre de tu materia favorita?",
                "¿Cuál es el nombre de tu profesor de laboratorios?",
                "¿Cuál es el año que vas egrasar de tu carrera universitaria?",
                "¿Cuál es el nombre de tu mascota favorita?",
                "¿Cuál es el nombre de tu mejor amig@?",
                "¿Cuál es el número de tu primer celular?",
                "¿Cuál es el nombre de tu primer novi@?",
                "¿Cuál es el lugar de tu nacimiento?",
                "¿Cuál es tu color favorito?",
                "¿Cuál es el color de tu vehículo?",
                "¿Cuál es la fecha de tu nacimiento?",
                "¿Cuál es el nombre de tu hijo(a) favorito?"
        };
        ArrayAdapter<String> adaptador1 =new ArrayAdapter<String> (this,android.R.layout.simple_spinner_item, lista);
        //final Spinner combo = (Spinner)myDialog.findViewById(R.id.combo);

        combo.setAdapter(adaptador1);

        combo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(Registrarme.this, combo.getSelectedItemPosition() + " " + combo.getSelectedItem().toString(),Toast.LENGTH_LONG).show();

                if(conta>=1){
                    //Toast.makeText(login.this, combo.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
                    Toast toast = Toast.makeText(getApplicationContext(), combo.getSelectedItem().toString(), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                conta++;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        closePopupPositiveImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((!et_Nombres.getText().toString().isEmpty()) && (!et_Apellidos.getText().toString().isEmpty())
                        && (!etUsuario.getText().toString().isEmpty()) && (!etPassword1.getText().toString().isEmpty())
                        && (!etPassword2.getText().toString().isEmpty()) && (!etRespuesta.getText().toString().isEmpty())
                        && combo.getSelectedItemPosition() > 0){
                    //Toast.makeText(getApplicationContext(), "Todos los campos son obligatorios.\n" +
                    //        "Complete la información para poder continuar.", Toast.LENGTH_LONG).show();
                    if (Patterns.EMAIL_ADDRESS.matcher(etUsuario.getText().toString()).matches() == false) {
                        tiUsuario.setError("Correo invalido.");
                        //tiUsuario.setVisibility(View.VISIBLE);
                        //mensaje1.setVisibility(View.GONE);
                        etUsuario.requestFocus();
                        estado_correo = false;
                    } else {
                        estado_correo = true;
                        tiUsuario.setError(null);
                        //tiUsuario.setVisibility(View.GONE);
                    }

                    if(estado_correo==true){
                        //Toast.makeText(getApplicationContext(), "segundo if O.K...", Toast.LENGTH_LONG).show();
                        if (Objects.equals(etPassword1.getText().toString(), etPassword2.getText().toString())) {
                            //Toast.makeText(getApplicationContext(), "tercer if O.K...", Toast.LENGTH_LONG).show();
                            //Aca introduciré el código fuente para guardar el registro en la BD sqlite.

                            //dbSQLiteHelper base = new dbSQLiteHelper(this);
                            dto datos = new dto();

                            datos.setNombres(et_Nombres.getText().toString());
                            datos.setApellidos(et_Apellidos.getText().toString());
                            datos.setUser(etUsuario.getText().toString());
                            datos.setPass(etPassword1.getText().toString());
                            datos.setPregunta(combo.getSelectedItem().toString());
                            datos.setRespuesta(etRespuesta.getText().toString());

                            if(base.addRegister(datos)){
                                Toast.makeText(getApplicationContext(), "Registro creado correctamente",Toast.LENGTH_LONG).show();
                                et_Nombres.setText(null);
                                et_Apellidos.setText(null);
                                etUsuario.setText(null);
                                etPassword1.setText(null);
                                etPassword2.setText(null);
                                combo.setSelection(0);
                                etRespuesta.setText(null);
                                //tiUsuario.setVisibility(View.GONE);
                                conta=0;
                                et_Nombres.requestFocus();

                            }else{
                                Toast.makeText(getApplicationContext(), "Error. Ya existe un registro con este" +
                                        " nombre de usuario: "+etUsuario.getText().toString(),Toast.LENGTH_LONG).show();
                            }

                        }else{
                            //Toast.makeText(getApplicationContext(), "tercer if NOT...", Toast.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(), "La contraseña ingresada no coincide.\n" +
                                    "Verifique y corrija para poder continuar.", Toast.LENGTH_LONG).show();
                            etPassword1.setText(null);
                            etPassword2.setText(null);
                            etPassword1.requestFocus();
                        }

                    }else{
                        Toast.makeText(getApplicationContext(), "El nombre de usuario debe ser una dirección de correo.\n" +
                                "Ej. manuel.gamez@itca.edu.sv", Toast.LENGTH_LONG).show();
                    }


                }else{
                    //Toast.makeText(getApplicationContext(), "primer if NOT...", Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), "Todos los campos son obligatorios.\n" +
                            "Complete la información para poder continuar.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void ShowrecuperarClave1() {
        myDialog.setContentView(R.layout.dialog_recuperarpassword1);
        myDialog.setCancelable(false);
        ImageView ivclose = (ImageView)myDialog.findViewById(R.id.ivclose);
        Button btnAceptar = (Button)myDialog.findViewById(R.id.btnAceptar);
        final EditText etCorreo = (EditText)myDialog.findViewById(R.id.etCorreo);
        final TextInputLayout tiCorreo = (TextInputLayout)myDialog.findViewById(R.id.tiCorreo);

        ivclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etCorreo.getText().toString().isEmpty()){
                    if (Patterns.EMAIL_ADDRESS.matcher(etCorreo.getText().toString()).matches() == false) {
                        tiCorreo.setError("Correo invalido.");
                        tiCorreo.requestFocus();
                        estado_correo = false;
                    } else {
                        estado_correo = true;
                        tiCorreo.setError(null);
                    }

                    if(estado_correo==true){
                        //Toast.makeText(getApplicationContext(), "Vamos bien chovi....", Toast.LENGTH_LONG).show();
                        dto datos = new dto();
                        datos.setUser(etCorreo.getText().toString());
                        if(base.consultaUser(datos)){
                           /* Toast.makeText(getApplicationContext(), "Registro encontrado" +
                                    "\n"+datos.getUser(),Toast.LENGTH_LONG).show();*/
                            ///CON ESTE CODIGO LOGRE CERRAR EL DIAGO1-INICIO-FIN


                            /*NO FUE NECESARIO PARA ESTE TIPO DE VENTANA GENERAR EL CIERRE DEL DIALOGO #1.
                            /*myDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    dialog.cancel();
                                }
                            });
                            myDialog.cancel();*/

                            //Acontinuación llamo el siguiente cuadro de dialogo y le paso
                            // los datos necesario para mostrar en el dialogo 2..

                            //AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion", null, 1);
                            //SQLiteDatabase bd = admin.getWritableDatabase();
                            String correo = etCorreo.getText().toString();
                            Cursor fila = base.getWritableDatabase().rawQuery("select codigo,nombres,apellidos,usuario,clave,pregunta,respuesta,fecha from usuarios where usuario='"+correo+"'", null);
                            if (fila.moveToFirst()) {
                                //String codigo=fila.getString(0);
                                int codigo=fila.getInt(0);
                                String nombres = fila.getString(1);
                                String apellidos=fila.getString(2);
                                String usuario=fila.getString(3);
                                String clave=fila.getString(4);
                                String pregunta=fila.getString(5);
                                String respuesta=fila.getString(6);
                                String fecha=fila.getString(7);
                                /*Toast.makeText(getApplicationContext(),codigo + "\n"
                                        + nombres + "\n"
                                        + apellidos + "\n"
                                        + usuario + "\n"
                                        + clave + "\n"
                                        + pregunta + "\n"
                                        + respuesta + "\n"
                                        + fecha + "\n",Toast.LENGTH_LONG).show();*/

                                ShowrecuperarClave2(codigo, nombres + " "+ apellidos, pregunta);

                            } else {
                                //Toast.makeText(getApplicationContext(), "No se encontrarón resultados que mostrar para la busqueda especificada", Toast.LENGTH_SHORT).show();
                            }
                            base.close();

                        }else{
                            Toast.makeText(getApplicationContext(), "No se han encontrado resultados en la busqueda especificada.",Toast.LENGTH_LONG).show();
                            etCorreo.setText(null);
                            etCorreo.requestFocus();
                        }

                    }else{
                        etCorreo.setText(null);
                        etCorreo.requestFocus();
                        Toast.makeText(getApplicationContext(), "El nombre de usuario ingresado no es válido. Debe ser una dirección de e-mail.", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Es obligatorio que ingrese su nombre de usuario.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void ShowrecuperarClave2(final int id, String nombres, String pregunta) {
        myDialog.setContentView(R.layout.dialog_recuperarpassword2);
        myDialog.setCancelable(false);
        ImageView ivclose = (ImageView)myDialog.findViewById(R.id.ivclose);

        Button btnAceptar = (Button)myDialog.findViewById(R.id.btnAceptar);
        Button btnCompleteOperation = (Button)myDialog.findViewById(R.id.btnCompleteOperation);

        TextView tv_question_bd = (TextView)myDialog.findViewById(R.id.tv_question_bd);
        final TextView tv_id = (TextView)myDialog.findViewById(R.id.tv_id);                    //En este campo envio el id del registro a verificar.
        //tv_id.setText(""+id);

        final EditText et_respuesta = (EditText)myDialog.findViewById(R.id.et_respuesta);
        //final TextInputLayout tiCorreo = (TextInputLayout)myDialog.findViewById(R.id.tiCorreo);

        tv_question_bd.setText(nombres +"\n"+pregunta);

        final LinearLayout bloque1 = (LinearLayout)myDialog.findViewById(R.id.bloque1);
        final LinearLayout bloque2 = (LinearLayout)myDialog.findViewById(R.id.bloque2);
        bloque2.setEnabled(false);
        bloque2.setVisibility(View.INVISIBLE);
        bloque2.setVisibility(View.GONE);

        final EditText et_pass1 = (EditText)myDialog.findViewById(R.id.et_pass1);
        final EditText et_pass2 = (EditText)myDialog.findViewById(R.id.et_pass2);
        et_pass1.setEnabled(false);
        et_pass2.setEnabled(false);

        ivclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_respuesta.getText().toString().isEmpty()) {
                    //Toast.makeText(getApplicationContext(),"Debe ingresar los datos de su cuenta.",Toast.LENGTH_LONG).show();
                    Toast toast = Toast.makeText(getApplicationContext(), "Campo respuesta es obligatorio.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else{
                    //Toast.makeText(getApplicationContext(), "Vamos bien...", Toast.LENGTH_LONG).show();
                    dto datos = new dto();
                    datos.setRespuesta(et_respuesta.getText().toString());
                    if(base.verificoRespuesta(datos)) {
                        Toast toast = Toast.makeText(getApplicationContext(), "EXCELENTE. \nAhora puede ingresar una nueva clave para su cuenta.", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        et_respuesta.setText(null);
                        bloque2.setVisibility(View.VISIBLE);
                        bloque2.setEnabled(true);
                        et_pass1.setEnabled(true);
                        et_pass2.setEnabled(true);
                        bloque1.setVisibility(View.GONE);
                    }else{
                        Toast toast = Toast.makeText(getApplicationContext(), "La respuesta ingresada es incorrecta. \n\nRespuesta: " + et_respuesta.getText().toString(), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        et_respuesta.setText(null);
                        et_respuesta.requestFocus();
                    }
                }
            }
        });


        btnCompleteOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"Vamos bien..",Toast.LENGTH_LONG).show();
                if ((!et_pass1.getText().toString().isEmpty()) && (!et_pass2.getText().toString().isEmpty())) {
                    if (Objects.equals(et_pass1.getText().toString(), et_pass2.getText().toString())) {
                        String clave=et_pass1.getText().toString();
                        String codigo=String.valueOf(id);
                        tv_id.setText(String.valueOf(id));
                        String pregunta="H";

                        dto datos = new dto();
                        datos.setCodigo(Integer.parseInt(codigo));
                        datos.setPass(clave);
                        if(base.updateClave(datos)) {
                            Toast toast = Toast.makeText(getApplicationContext(), "FELICIDADES.\nSu nueva clave ha sido creado correctamente. \n\nGRACIAS!!!", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            //myDialog.cancel();
                            myDialog.dismiss();
                        }else{
                            Toast.makeText(getApplicationContext(), "Error al intentar actualizar su clave.\n" +
                                    "Intentelo mas tarde...", Toast.LENGTH_LONG).show();
                        }

                    }else{
                        Toast toast = Toast.makeText(getApplicationContext(), "Las contraseñas ingresadas no coinciden", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        et_pass1.requestFocus();
                        et_pass1.setText(null);
                        et_pass2.setText(null);
                    }
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), "Debe ingresar y confirmar clave nueva", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }



            }
        });
    }


}
