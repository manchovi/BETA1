package com.mjgl.utla.utla;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class BT extends AppCompatActivity {
    Button btnONBT,btnOFFBT;
    BluetoothAdapter myBluetoothAdapter;

    Intent btEnablingIntent;
    int requestCodeForeEnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bt);


        btnOFFBT = (Button)findViewById(R.id.btnOFFBT);
        btnONBT = (Button)findViewById(R.id.btnONBT);
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        btEnablingIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        requestCodeForeEnable = 1;

        bluetoothONMETHOD();
        bluetoothOFFMETHOD();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void bluetoothOFFMETHOD() {
        btnOFFBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myBluetoothAdapter.isEnabled()){
                    myBluetoothAdapter.disable();
                    Toast.makeText(getApplicationContext(), "Bluetooth deshabilitado correctamente.",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == requestCodeForeEnable){
            if(resultCode == RESULT_OK){
                Toast.makeText(getApplicationContext(), "Bluetooth ha sido habilitado.",Toast.LENGTH_LONG).show();
            }else if(resultCode == RESULT_CANCELED){
                Toast.makeText(getApplicationContext(), "Bluetooth operación cancelado.",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void bluetoothONMETHOD() {
        btnONBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myBluetoothAdapter==null){
                    Toast.makeText(getApplicationContext(), "Bluetooth does not support on this device",Toast.LENGTH_LONG).show();
                    //Tu dispositivo no soporta Bluetooth
                }else{
                    if(!myBluetoothAdapter.isEnabled()){
                        startActivityForResult(btEnablingIntent, requestCodeForeEnable);
                    }
                }
            }
        });
    }

}
