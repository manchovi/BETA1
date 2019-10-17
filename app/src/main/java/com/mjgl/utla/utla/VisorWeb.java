package com.mjgl.utla.utla;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

public class VisorWeb extends AppCompatActivity {

    AlertDialog.Builder dialog;
    private ProgressDialog pd;
    ProgressDialog progressDialog;
    private ProgressBar progressBar;
    boolean estado_nombre;
    boolean estado_correo;

    WebView visor;

    private Button BtnUpdate;
    View focusView = null;


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new android.app.AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Salir")
                    .setMessage("¿Esta seguro?\n\nSe retornará al menú principal.")
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {//un listener que al pulsar, cierre la aplicacion
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Salir
                            GoMain();
                            VisorWeb.this.finish();
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
        setContentView(R.layout.activity_visor_web);

        visor = (WebView) findViewById(R.id.webView);

        //Función para evitar la rotación de la pantalla del CELULAR.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //y esto para pantalla completa (oculta incluso la barra de estado)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        toolbar.setTitle("Portal");
        toolbar.setTitleMargin(0, 0, 0, 0);

        toolbar.setSubtitle("UTLA");
        setSupportActionBar(toolbar);

        if (checkConnectivity()) {
            go_home();
        }else{
            pageLocal();
        }
    }

    private void pageLocal(){
        //visor.loadUrl("http://www.cocesna.org");
        visor.loadUrl("file:///android_asset/index.html");
        visor.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
    }

    public String obtenerServer() {
        SharedPreferences preferences = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        String server = preferences.getString("servidor", "Sin configurar.");
        String portal = preferences.getString("portal", "Sin configurar.");
        return server + portal;
    }

    private void go_home() {
        if (checkConnectivity()) {
            visor.loadUrl("");
            //String url = "file:///android_asset/index.html

            visor.setInitialScale(1);
            visor.setWebChromeClient(new WebChromeClient());
            visor.getSettings().setAllowFileAccess(true);
            visor.getSettings().setPluginState(WebSettings.PluginState.ON);
            visor.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
            visor.setWebViewClient(new WebViewClient());
            visor.getSettings().setJavaScriptEnabled(true);
            visor.getSettings().setLoadWithOverviewMode(true);
            visor.getSettings().setUseWideViewPort(true);
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

            visor.getSettings().setSupportZoom(true);
            visor.getSettings().setBuiltInZoomControls(true);
            visor.getSettings().setDisplayZoomControls(true);

            //habilitamos javascript y el zoom
            //aca habilito soporte de javascript por el webview.
            visor.getSettings().setJavaScriptEnabled(true);
            //aca habilito control de zoom al control webview.

            //habilitamos los plugins (flash)
            //visor.getSettings().setPluginsEnabled(true);

            String porta = obtenerServer();
            visor.loadUrl(porta);
            //visor.loadUrl("http://mjgl.com.sv/portalutla/index.php");


            //visor.setWebViewClient(new WebViewClient());
            visor.setWebChromeClient(new WebChromeClient());

            //aca le digo que cargue siempre en el webview la pagina y subpaginas del sitio web. URL.
            visor.setWebViewClient(new WebViewClient() {
                                       //public boolean shouldOverrideUrlLoading(WebView view, String url){
                                       public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                           return false;
                                       }
                                   }
            );

            progressBar = (ProgressBar) findViewById(R.id.progressbar);
            visor.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int progress) {
                    progressBar.setProgress(0);
                    progressBar.setVisibility(View.VISIBLE);
                    VisorWeb.this.setProgress(progress * 1000);
                    progressBar.incrementProgressBy(progress);

                    if (progress == 100) {
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
            //visor.loadUrl("http://www.cocesna.org/");
        }else{
            pageLocal();
        }
    }


    private boolean checkConnectivity()
    {
        boolean enabled = true;

        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        if ((info == null || !info.isConnected() || !info.isAvailable()))
        {
            enabled = false;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.setIcon(R.drawable.ic_network_check);
            builder.setMessage(getString(R.string.noconnection));
            builder.setCancelable(false);
            builder.setNeutralButton(R.string.ok, null);
            builder.setTitle(getString(R.string.error));
            builder.create().show();
        }
        return enabled;
    }


    //METODO PARA UTILIZAR LA TECLA BACK DEL CELULAR PARA NAVEGAR HACIA ATRAS EN EN WEBVIEW.
    @Override
    public void onBackPressed()
    {
        if (visor.canGoBack())
        {
            visor.goBack();
        }
        else
        {
            super.onBackPressed();
        }
    }

    public void anterior() {
        if (checkConnectivity()) {
            visor.goBack();
        }else{
            pageLocal();
        }
    }

    public void siguiente() {
        if (checkConnectivity()) {
            visor.goForward();
        }else{
            pageLocal();
        }
    }

    public void detener() {
        {
            if (checkConnectivity()) {
                visor.stopLoading();
            }else{
                pageLocal();
            }
        }
    }

    public void actualizar() {
        if (checkConnectivity()) {
            visor.reload();
        }else{
            pageLocal();
        }
    }

    public void GoMain() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        //i.putExtra("usu", mEmail.getText().toString());
        startActivity(i);
        finish();
    }


    //MENÚ PRINCIPAL
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_retornar:
                GoMain();
                return true;

            case R.id.menu_atras:
                //Toast.makeText(getApplicationContext(), "Atras", Toast.LENGTH_SHORT).show();
                anterior();
                return true;

            case R.id.menu_adelante:
                //Toast.makeText(getApplicationContext(), "Adelante", Toast.LENGTH_SHORT).show();
                siguiente();
                return true;

            case R.id.menu_actualizar:
                //Toast.makeText(getApplicationContext(), "Actualizar", Toast.LENGTH_SHORT).show();
                actualizar();
                return true;

            case R.id.menu_home:
                if (checkConnectivity()) {
                    go_home();
                }
                return true;

            case R.id.menus_stop:
                detener();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
