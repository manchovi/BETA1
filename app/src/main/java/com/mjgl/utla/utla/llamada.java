package com.mjgl.utla.utla;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class llamada extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llamada);
        Button mDialButton = (Button)findViewById(R.id.btn_dial);
        final TextView mPhoneNoEt = (TextView)findViewById(R.id.et_phone_no);

        mDialButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                String phoneNo = mPhoneNoEt.getText().toString();
                if(!TextUtils.isEmpty(phoneNo)){
                    String dial = "tel:" + phoneNo;
                    //startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                }else{
                    Toast.makeText(llamada.this, "Enter a phone number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
