package com.mjgl.utla.utla;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DialogoConfirmacion extends DialogFragment {
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder =
        		new AlertDialog.Builder(getActivity());
        
        builder.setMessage("Está a punto de finalizar su sesión.\n¿Realmente desea salir?")
               .setIcon(R.drawable.ic_close)
        	   .setTitle("Advertencia.")
               .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       //Toast.makeText(getActivity(), " Aceptar!!", Toast.LENGTH_SHORT).show();
                       destroySesionVariables();
                       ir();
                       dialog.cancel();
                   }
               })
               .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       //Toast.makeText(getActivity(), " Cancelar!!", Toast.LENGTH_SHORT).show();
       					dialog.cancel();
                   }
               });

        return builder.create();
    }

    private void ir() {
        Intent ventana = new Intent(getActivity(), login.class); //CORREGIR O VERIFICAR ESTO..
        startActivity(ventana);
        getActivity().finish();
    }

    public void destroySesionVariables(){
        SharedPreferences preferences = getActivity().getSharedPreferences("variablesesion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }
}
