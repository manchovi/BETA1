package com.mjgl.utla.utla;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class UsersAdapter extends ArrayAdapter<dto>{
    Context mCtx;
    int listLayoutRes;
    List<dto> UsersList;
    //SQLiteDatabase mDatabase;
    int total;
    int bandera;
    Cursor cursorUsuarios;


    public UsersAdapter(Context mCtx, int listLayoutRes, List<dto> UsersList, int total, int bandera) {
    //public UsersAdapter(Context mCtx, int listLayoutRes, List<dto> UsersList, SQLiteDatabase mDatabase) {
        super(mCtx, listLayoutRes, UsersList);
        this.mCtx = mCtx;
        this.listLayoutRes = listLayoutRes;
        this.UsersList = UsersList;
        this.total = total;
        this.bandera = bandera;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(listLayoutRes, null);

        //final Employee employee = employeeList.get(position);
        final dto datos=UsersList.get(position);

        //final TextView tvCodigo = (TextView)view.findViewById(R.id.tvCodigo);
        final TextView tvNombres = (TextView)view.findViewById(R.id.tvNombres);
        TextView tvUsuarios = (TextView)view.findViewById(R.id.tvUsuario);
        TextView tvClave = (TextView)view.findViewById(R.id.tvClave);
        TextView tvPregunta = (TextView)view.findViewById(R.id.tvPregunta);
        TextView tvRespuesta = (TextView)view.findViewById(R.id.tvRespuesta);
        TextView tvFechaHora = (TextView)view.findViewById(R.id.tvFechaHora);


        //Toast.makeText(mCtx, ""+datos, Toast.LENGTH_SHORT).show();

        //tvCodigo.setText(datos.getCodigo());
        tvNombres.setText(datos.getNombres() + " " + datos.getApellidos());
        tvUsuarios.setText(datos.getUser());
        tvClave.setText(datos.getPass());
        tvPregunta.setText(datos.getPregunta());
        //tvRespuesta.setText(datos.getRespuesta());
        tvRespuesta.setText("**********");
        tvFechaHora.setText(datos.getFechahora());

        //Button btnEdit = (Button)view.findViewById(R.id.btnEdit);
        //Button btnDelete = (Button)view.findViewById(R.id.btnDelete);

        ImageView btnEdit = (ImageView) view.findViewById(R.id.btnEdit);
        ImageView btnDelete = (ImageView) view.findViewById(R.id.btnDelete);

        //Este bloque de la condición lo he puesto para que se oculte el boton eliminar
        //Previniendo con ello que los usuarios no se puedan auto eliminar. Solo el master lo puede hacer.
        if(bandera==1){
            if(String.valueOf(datos.getCodigo()).equals("1")){
                btnDelete.setVisibility(View.INVISIBLE);
            }else {
                btnDelete.setVisibility(View.VISIBLE);
            }
        }else{
            btnDelete.setVisibility(View.GONE);
        }

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUsuarios(datos);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                //builder.setTitle("¿Esta seguro de borrar el registro? "+tvNombres.getText().toString());
                builder.setTitle("¿Esta seguro de borrar el registro? "+datos.getNombres());

                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean estado = false;
                        dbSQLiteHelper admin = new dbSQLiteHelper(mCtx);
                        SQLiteDatabase db = admin.getWritableDatabase();
                        /*
                        INICIO:ADICIONES
                         */
                        Cursor fila=db.rawQuery("select codigo from usuarios where codigo=1",null);
                        //Cursor fila=db.rawQuery("select codigo from usuarios where codigo='"+datos.getCodigo()+"'",null);
                        //if(fila.moveToFirst()==true) {
                        if(fila.moveToFirst()){
                            int codigo = fila.getInt(0);
                            String id = String.valueOf(codigo);
                            int valor = datos.getCodigo();
                            String valor1=String.valueOf(valor);
                            if (valor1.equals(id)) {
                                estado = true;
                            }
                        }else{
                            estado = false;
                        }
                        //db.close();

                        if(estado){
                            //Toast.makeText(getContext(), "vere..", Toast.LENGTH_SHORT).show();
                            Toast.makeText(mCtx, "Lo lamento: No te puedes autodestruir ADMIN." +
                                    " Dejaría inservible esta App.\n" +
                                    "\nTe saluda el creador, bye.", Toast.LENGTH_SHORT).show();
                        }else {
                        /*
                        FIN
                         */

                            String sql = "DELETE FROM usuarios WHERE codigo = ?";
                            db.execSQL(sql, new Integer[]{datos.getCodigo()});

                            reloadUsersFromDatabase(bandera);

                        }
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        return view;
    }

    private void updateUsuarios(final dto datos) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        final View view = inflater.inflate(R.layout.dialog_update_cuentausuario, null);
        builder.setView(view);

        final EditText etNombres = view.findViewById(R.id.etNombres);
        final EditText etApellidos = view.findViewById(R.id.etApellidos);
        final EditText etUsuario = view.findViewById(R.id.etUsuario);
        final EditText etClave = view.findViewById(R.id.etClave);

        final TextView tvFechaHora = view.findViewById(R.id.tvFechaHora);
        final TextView tvId = view.findViewById(R.id.tvId);

        final TextView tvPreguntaSeguridad = view.findViewById(R.id.tvPreguntaSeguridad);

        /*final Spinner spinnerPregunta = view.findViewById(R.id.spinnerPregunta);
        final String[] lista =new String[]{
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
        };*/
        //ArrayAdapter<String> adaptador1 =new ArrayAdapter<String> (mCtx,android.R.layout.simple_spinner_item, lista);
        //spinnerPregunta.setAdapter(adaptador1);

        //final Spinner combo = (Spinner)myDialog.findViewById(R.id.combo);

        final EditText etRespuesta = view.findViewById(R.id.etRespuesta);
        final Button btnActualizar = view.findViewById(R.id.btnActualizar);
        final Button btnCancel = view.findViewById(R.id.btnCancel);

        etNombres.setText(datos.getNombres());
        etApellidos.setText(datos.getApellidos());
        etUsuario.setText(datos.getUser());
        etClave.setText(datos.getPass());

        tvId.setText(String.valueOf(datos.getCodigo()));
        tvFechaHora.setText(datos.getFechahora());

        tvPreguntaSeguridad.setText(datos.getPregunta());
        //etRespuesta.setText(datos.getRespuesta());
        etRespuesta.setText("**********");

        final AlertDialog dialog = builder.create();
        dialog.show();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        view.findViewById(R.id.btnActualizar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String codigo = tvId.getText().toString();
                String fechahora=tvFechaHora.getText().toString();

                String nombres = etNombres.getText().toString().trim();
                String apellidos = etApellidos.getText().toString().trim();
                String usuario = etUsuario.getText().toString();
                String clave = etClave.getText().toString();
                //String pregunta = spinnerPregunta.getSelectedItem().toString();
                String pregunta = tvPreguntaSeguridad.getText().toString();
                String respuesta = etRespuesta.getText().toString();

                if (nombres.isEmpty()) {
                    etNombres.setError("Es obligación que escriba su nombre");
                    etNombres.requestFocus();
                    return;
                }

                if (apellidos.isEmpty()) {
                    etApellidos.setError("Es obligación que escriba su apellido");
                    etApellidos.requestFocus();
                    return;
                }

                if (usuario.isEmpty()) {
                    etUsuario.setError("Es obligación que escriba un nombre de usuario");
                    etUsuario.requestFocus();
                    return;
                }

                if (Patterns.EMAIL_ADDRESS.matcher(etUsuario.getText().toString()).matches() == false) {
                    etUsuario.setError("El nombre de usuario debe ser un e-mail");
                    etUsuario.requestFocus();
                    return;
                }

                /*if (clave.isEmpty()) {
                    etClave.setError("Es obligación que escriba un password");
                    etClave.requestFocus();
                    return;
                }

                if (respuesta.isEmpty()) {
                    etRespuesta.setError("Es obligación respuesta de seguridad");
                    etRespuesta.requestFocus();
                    return;
                }*/

                /*String sql = "UPDATE usuarios \n" +
                        "SET nombres = ?, \n" +
                        "apellidos = ?, \n" +
                        "usuario = ?, \n" +
                        "clave = ?, \n" +
                        "pregunta = ?, \n" +
                        "respuesta = ? \n" +
                        "WHERE codigo = ?;\n";*/



                /*Cursor fila = this.getWritableDatabase().rawQuery("select usuario from usuarios where usuario='"+datos.getUser()+"'", null);
                if(fila.moveToFirst()==true){
                    estado = false;
                }else {*/

                dbSQLiteHelper admin = new dbSQLiteHelper(mCtx);
                SQLiteDatabase db = admin.getWritableDatabase();
                Cursor fila=db.rawQuery("select usuario from usuarios where usuario='"+ etUsuario.getText().toString()+"'",null);
                //if(fila.moveToFirst()==true) {
                if(fila.moveToFirst()) {
                    Toast toast = Toast.makeText(mCtx, "Ya existe un registro con el e-mail ingresado: " + etUsuario.getText().toString()+
                            "\n\nNo se ha realizado ningún cambio.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else{

                    String sql = "UPDATE usuarios \n" +
                            "SET nombres = ?, \n" +
                            "apellidos = ?, \n" +
                            "usuario = ? \n" +
                            "WHERE codigo = ?;\n";

                    //db.execSQL(sql, new String[]{nombres, apellidos, usuario, clave, pregunta, respuesta, String.valueOf(datos.getCodigo())});
                    db.execSQL(sql, new String[]{nombres, apellidos, usuario, String.valueOf(datos.getCodigo())});
                    Toast.makeText(mCtx, "Usuario actualizado correctamente", Toast.LENGTH_SHORT).show();

                    reloadUsersFromDatabase(bandera);
                    dialog.dismiss();

                }

                //reloadUsersFromDatabase(bandera);
                //dialog.dismiss();
            }
        });
    }

    private void reloadUsersFromDatabase(int cod) {
        dbSQLiteHelper admin=new dbSQLiteHelper(mCtx);
        SQLiteDatabase db=admin.getWritableDatabase();

        //Cursor cursorUsuarios = db.rawQuery("SELECT * FROM usuarios", null);

        if(cod==1) {
            //Cursor cursorUsuarios = db.rawQuery("SELECT * FROM usuarios", null);
            cursorUsuarios = db.rawQuery("SELECT * FROM usuarios", null);
        }else{
            cursorUsuarios=db.rawQuery("select * from usuarios where codigo='"+cod+"'",null);
        }
        if (cursorUsuarios.moveToFirst()) {
            UsersList.clear();
            do {
                UsersList.add(new dto(
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
        notifyDataSetChanged();
    }
}
