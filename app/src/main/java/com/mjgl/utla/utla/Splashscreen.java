package com.mjgl.utla.utla;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.felipecsl.gifimageview.library.GifImageView;

public class Splashscreen extends AppCompatActivity {
    private GifImageView gifImageView;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        //progressBar.setMessage("Espere por favor, Estamos verificando su respuesta en el servidor");
        //progressBar.show();

        //esto quita el título de la activity en la parte superior
        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        //y esto para pantalla completa (oculta incluso la barra de estado)
        /*QUITE ESTA LINEA PARA QUE ME MUESTRE LO DE ARRIBA DE LA PANTALLA DEL PHONE*/
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Función para evitar la rotación de la pantalla del CELULAR.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //y esto para pantalla completa (oculta incluso la barra de estado)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        //progressBar = (ProgressBar) findViewById(R.id.progressbar);
        //progressBar.setVisibility(progressBar.VISIBLE);

        /*gifImageView = (GifImageView)findViewById(R.id.gifImageView);
        try{
            InputStream inputStream = getAssets().open("loader.gif");
            byte [] bytes = IOUtils.toByteArray(inputStream);

            gifImageView.setBytes(bytes);
            gifImageView.startAnimation();
        }
        catch (IOException ex)
        {

        }*/

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Splashcreen.this.startActivity(new Intent(Splashcreen.this, Activity_Menu.class));
                //Splashcreen.this.startActivity(new Intent(Splashcreen.this, graphic_dificult.class));

                Splashscreen.this.startActivity(new Intent(Splashscreen.this, login.class));
                //Splashcreen.this.startActivity(new Intent(Splashcreen.this, GoogleMapsAndroid.class));
                //SplashScreen.this.finish();
                finish();
            }
        },5000);
    }
}
