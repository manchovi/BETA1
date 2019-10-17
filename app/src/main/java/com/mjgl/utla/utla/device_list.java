package com.mjgl.utla.utla;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class device_list extends AppCompatActivity {
  //public class device_list extends ActionBarActivity{
  //private BluetoothAdapter btAdapter = null;
    BluetoothAdapter myBluetoothAdapter;
    Intent btEnablingIntent;
    int requestCodeForeEnable;
    Toolbar toolbar;
    //Declaramos Los Componentes
    Button btnVinculados;
    ListView listaDispositivos;
    //Bluetooth
    private BluetoothAdapter myBluetooth = null;
    private Set<BluetoothDevice> dispVinculados;
    public static String EXTRA_ADDRESS = "device_address";

    private ProgressDialog progress;
    String senal ="";


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new android.app.AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Advertencia.")
                    .setMessage("¿Esta seguro que desea salir de esta pantalla?")
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {//un listener que al pulsar, cierre la aplicacion
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Intent intent = new Intent(device_list.this, device_list.class);
                            //intent.putExtra("senal", "1");
                            //startActivity(intent);

                            //progress.dismiss();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finish();
                            //device_list.this.finish();
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
        setContentView(R.layout.activity_device_list);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            senal = bundle.getString("senal");
            if(senal.equals("1")){
                //Toast.makeText(this, "puchica...", Toast.LENGTH_SHORT).show();
                //progress.dismiss();
            }
        }

        //AL FINAL MEJOR CREE MI PROPIA TOOLBAR.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_flacha_backs));

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_left));
        toolbar.setTitleTextColor(getResources().getColor(R.color.mycolor_blanco));
        //toolbar.setTitleMarginTop(25);
        toolbar.setTitleMargin(0,0,0,0);
        toolbar.setSubtitle("Dispositivos Emparejados");
        getSupportActionBar().setTitle("UTLA");

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        //COn las siguientes 2 lineas habilito el boton con icono de flecha hacia atras en la parte superior
        //de la toolbar.

        /*SI FUNCIONA Y MUESTRA LA FECHA DE BACK...
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        FIN*/

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/


        /*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        */

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //Función para evitar la rotación de la pantalla del CELULAR.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        //esto quita el título de la activity en la parte superior
        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        //y esto para pantalla completa (oculta incluso la barra de estado)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Función para evitar la rotación de la pantalla del CELULAR.
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        btEnablingIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        requestCodeForeEnable = 1;

        //Declaramos nuestros componenetes ralcionandolos con los del layout
        btnVinculados = (Button)findViewById(R.id.button);
        listaDispositivos = (ListView)findViewById(R.id.listView);

        //Comprobamos que el dispositivo tiene bluetooth
        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        if(myBluetooth == null)
        {
            //Mostramos un mensaje, indicando al usuario que no tiene conexión bluetooth disponible
            Toast.makeText(getApplicationContext(), "Bluetooth no disponible", Toast.LENGTH_LONG).show();

            //finalizamos la aplicación
            finish();
        }
        else if(!myBluetooth.isEnabled())
        {
            //Preguntamos al usuario si desea encender el bluetooth
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon,1);
        }

        btnVinculados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                listaDispositivosvinculados();
            }
        });


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });
    }

    private void listaDispositivosvinculados(){
        dispVinculados = myBluetooth.getBondedDevices();
        ArrayList list = new ArrayList();
        if (dispVinculados.size()>0)
        {
            for(BluetoothDevice bt : dispVinculados)
            {
                list.add(bt.getName() + "\n" + bt.getAddress()); //Obtenemos los nombres y direcciones MAC de los disp. vinculados
            }
        }else{
            Toast.makeText(getApplicationContext(), "No se han encontrado dispositivos vinculados", Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        listaDispositivos.setAdapter(adapter);
        listaDispositivos.setOnItemClickListener(myListClickListener);
    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener()
    {
        public void onItemClick (AdapterView<?> av, View v, int arg2, long arg3)
        {
            // Get the device MAC address, the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            progress = ProgressDialog.show(device_list.this, "Conectando...", "Por favor espere!!!");

            // Make an intent to start next activity.
            Intent i = new Intent(device_list.this, luces_control.class);
            //Change the activity.
            i.putExtra(EXTRA_ADDRESS, address); //this will be received at ledControl (class) Activity
            startActivity(i);
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_device_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button_green_round, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.opEncenderBT) {
            //acciones aca
            bluetoothONMETHOD();
            //Toast.makeText(getApplicationContext(),"BT Encendido.",Toast.LENGTH_LONG).show();
            return true;
        } else if(id == R.id.opApagarBT){
            //Acciones aca
            bluetoothOFFMETHOD();
            //Toast.makeText(getApplicationContext(),"BT Apagado.",Toast.LENGTH_LONG).show();
            return true;
        }else if(id==R.id.opDispositivosVinculados){
            //Acciones aca
            listaDispositivosvinculados();
            Toast.makeText(getApplicationContext(),"Listado de dispositivos vinculados.",Toast.LENGTH_LONG).show();
            return true;
        }else if(id==R.id.opGOMenu){
            //Toast.makeText(getApplicationContext(),"Go menú",Toast.LENGTH_LONG).show();
            goMenu();
            return true;
        }else if(id==R.id.opGOHelp){
            //Toast.makeText(getApplicationContext(),"Ayuda",Toast.LENGTH_LONG).show();
            AyudaBT();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void goMenu(){
        Intent intent = new Intent(device_list.this, MainActivity.class);
        startActivity(intent);
    }

    private void bluetoothOFFMETHOD() {
        if(myBluetoothAdapter.isEnabled()){
            myBluetoothAdapter.disable();
            Toast.makeText(getApplicationContext(), "Bluetooth deshabilitado correctamente.",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == requestCodeForeEnable){
            if(resultCode == RESULT_OK){
                Toast.makeText(getApplicationContext(), "Bluetooth ha sido habilitado.",Toast.LENGTH_LONG).show();
            }else if(resultCode == RESULT_CANCELED){
                Toast.makeText(getApplicationContext(), "Bluetooth operación cancelado.",Toast.LENGTH_LONG).show();
            }

        }
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //hago un case por si en un futuro agrego mas opciones
                Log.i("ActionBar", "Atrás!");
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }*/

    private void bluetoothONMETHOD() {
        if(myBluetoothAdapter==null){
            Toast.makeText(getApplicationContext(), "Bluetooth does not support on this device",Toast.LENGTH_LONG).show();
            //Tu dispositivo no soporta Bluetooth
        }else{
            if(!myBluetoothAdapter.isEnabled()){
                startActivityForResult(btEnablingIntent, requestCodeForeEnable);
            }else{
                Toast.makeText(getApplicationContext(), "Su BlueTooth ya está listo!",Toast.LENGTH_LONG).show();
            }

        }
    }


    private void AyudaBT() {
        final android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(device_list.this);
        //AlertDialog.Builder mBuilder = new AlertDialog.Builder(getApplicationContext());
        mBuilder.setIcon(R.drawable.ic_check);
        mBuilder.setTitle("Ayuda BlueTooth.");
        mBuilder.setCancelable(false);
        final View mView = getLayoutInflater().inflate(R.layout.ayudabluetooth, null);

        //controles donde muestro informacion del archivo credenciales.xml creado con
        //SharedPreferences.
        //final Button btnCerrar =(Button) mView.findViewById(R.id.btnCerrar);
        Toast toast = Toast.makeText(getApplicationContext(), "Welcome to help BlueTooth.", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        /*mBuilder.setPositiveButton("GUARDAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(),"JAJAJA",Toast.LENGTH_LONG).show();

            }
        });*/
        //mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
        mBuilder.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        mBuilder.setView(mView);
        //final AlertDialog dialog = mBuilder.create();
        final android.app.AlertDialog dialog = mBuilder.create();
        dialog.show();

        /*btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
    }

}
