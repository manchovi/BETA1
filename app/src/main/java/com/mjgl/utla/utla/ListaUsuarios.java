package com.mjgl.utla.utla;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListaUsuarios extends AppCompatActivity {

    //private Cursor fila;
    dbSQLiteHelper base = new dbSQLiteHelper(this);

    List<dto> userList;
    //dbSQLiteHelper admin=new dbSQLiteHelper(this);
    //SQLiteDatabase db=admin.getWritableDatabase();

    //QLiteDatabase mDatabase;
    ListView lvUsuarios;
    UsersAdapter adapter;

    int cantidadRegistros=0;
    int codigo;
    String nombre;
    /*
    List<Employee> employeeList;
    SQLiteDatabase mDatabase;
    ListView listViewEmployees;
    EmployeeAdapter adapter;
    */
    TextView textView;
    Cursor cursorUsuarios;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_usuarios);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        textView = (TextView)findViewById(R.id.textView);

        //setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_left));
        toolbar.setTitleTextColor(getResources().getColor(R.color.mycolor_blanco));
        toolbar.setTitle("UTLA");
        toolbar.setTitleMargin(0, 0, 0, 0);
        toolbar.setSubtitle("Usuario Registrados");
        setSupportActionBar(toolbar);

        //Función para evitar la rotación de la pantalla del CELULAR.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //y esto para pantalla completa (oculta incluso la barra de estado)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        lvUsuarios = (ListView)findViewById(R.id.lvUsuarios);
        userList = new ArrayList<>();


        lvUsuarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dto a = (dto) parent.getItemAtPosition(position);
                AlertDialog.Builder al = new AlertDialog.Builder(ListaUsuarios.this);
                al.setCancelable(true);
                al.setTitle("Detalle Usuario");
                al.setMessage(a.getUser()+ " " + a.getNombres() + " " + a.getApellidos() + " " + a.getPregunta());
                //al.setMessage(a.tostring());
                al.show();

            }
        });

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            codigo = bundle.getInt("codigo");
            nombre = bundle.getString("nombre");
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });
        showUserFromDatabase(codigo);
    }

    private void showUserFromDatabase(int cod) {
        dbSQLiteHelper admin=new dbSQLiteHelper(this);
        SQLiteDatabase db=admin.getWritableDatabase();

        if(cod==1) {
            //Cursor cursorUsuarios = db.rawQuery("SELECT * FROM usuarios", null);
            cursorUsuarios = db.rawQuery("SELECT * FROM usuarios", null);
        }else{
            cursorUsuarios=db.rawQuery("select * from usuarios where codigo='"+cod+"'",null);
        }

        if (cursorUsuarios.moveToFirst()) {
            do {
                cantidadRegistros++;
                userList.add(new dto(
                        cursorUsuarios.getInt(0),
                        cursorUsuarios.getString(1),
                        cursorUsuarios.getString(2),
                        cursorUsuarios.getString(3),
                        cursorUsuarios.getString(4),
                        cursorUsuarios.getString(5),
                        cursorUsuarios.getString(6),
                        cursorUsuarios.getString(7)
                ));
            } while (cursorUsuarios.moveToNext());
        }
        cursorUsuarios.close();
        adapter = new UsersAdapter(this, R.layout.list_layout_usuarios, userList, cantidadRegistros, cod);
        lvUsuarios.setAdapter(adapter);
        textView.setText("RESULTADOS ENCONTRADOS: "+cantidadRegistros);

    }
}
