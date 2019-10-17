package com.mjgl.utla.utla;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class dbSQLiteHelper extends SQLiteOpenHelper {
    /*
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Lawyers.db";

    public LawyersDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    */

    //OJO: A estas dos formas de trabajar con la BD en SQLite.

    /*public dbSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }*/

    public dbSQLiteHelper(Context context){
        super(context, "db_utla.db",null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("create table usuarios(codigo integer not null primary key autoincrement,usuario text,clave text)");
        //db.execSQL("insert into usuarios values('utla@utla.edu.sv','admin')");

        //db.execSQL("create table usuarios(codigo integer primary key,usuario text,clave text)");
        //db.execSQL("insert into usuarios values(01,'admin','admin')");

        //String f = getDateTime();

        //db.execSQL("create table usuarios(codigo integer not null primary key autoincrement,nombres varchar(50) not null,apellidos varchar(50) not null,usuario varchar(100) not null,clave varchar(10) not null,pregunta varchar(100) not null,respuesta varchar(100) not null, fecha datetime NOT NULL)");
        db.execSQL("create table usuarios(codigo integer not null primary key autoincrement,nombres varchar(50) not null,apellidos varchar(50) not null,usuario varchar(100) not null,clave varchar(10) not null,pregunta varchar(100) not null,respuesta varchar(100) not null, fecha datetime NOT NULL)");
        //db.execSQL("insert into usuarios values(null,'Administrador','admin','admin@utla.edu.sv','admin','¿Cuál es el nombre de tu universidad favorita?','UTLA',datetime())");
        db.execSQL("insert into usuarios values(null,'Universidad Técnica','Latinoamerica','admin@utla.edu.sv','admin','¿Cuál es el nombre de tu universidad favorita?','UTLA', datetime('now','localtime'))");
        //db.execSQL("insert into usuarios values(1,'Administrador','admin','admin@utla.edu.sv','admin','¿Cuál es el nombre de tu universidad favorita?','UTLA')");

        /*
        String tabla1 = "create table datos(id integer not null primary key autoincrement," +
                "nombre varchar(100) not null, correo varchar(100), telefono varchar(10))";
        db.execSQL(tabla1);
        */
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("create table usuarios(codigo integer not null primary key autoincrement,usuario text,clave text)");
        //db.execSQL("insert into usuarios values('utla@utla.edu.sv','admin')");

        db.execSQL("drop table if exists usuarios");
        onCreate(db);

        //db.execSQL("insert into usuarios values('admin','admin')");
    }

    //Función para insertar datos.
    //DTO: Data Transfer Object
    public boolean insertardatos(dto datos){
        boolean estado = true;
        int resultado;
        ContentValues registro = new ContentValues();
        try{
            //registro.put("codigo",datos.getCodigo());
            registro.put("nombres",datos.getNombres());
            registro.put("apellidos",datos.getApellidos());
            registro.put("usuario", datos.getUser());
            registro.put("clave", datos.getPass());
            registro.put("pregunta", datos.getPregunta());
            registro.put("respuesta", datos.getRespuesta());

            /*adicione estas lineas para verificar sino existe un usuario con el mismo correo
              INICIO.*/
            Cursor fila = this.getWritableDatabase().rawQuery("select usuario from usuarios where usuario='"+datos.getUser()+"'", null);
            if(fila.moveToFirst()==true){
                estado = false;
            }else {
                /*FIN*/
                //estado = (boolean)this.getWritableDatabase().insert("datos","nombre, correo, telefono",registro);
                resultado = (int) this.getWritableDatabase().insert("usuarios", "nombres,apellidos,usuario,clave,pregunta,respuesta", registro);
                if (resultado > 0) estado = true;
                else estado = false;
            }
        }catch (Exception e){
            estado = false;
            Log.e("error.",e.toString());
        }
        return estado;
    }

    public boolean addRegister(dto datos){
        boolean estado = true;
        int resultado;
        try{
            int codigo = datos.getCodigo();
            String nombres = datos.getNombres();
            String apellidos = datos.getApellidos();
            String usuario = datos.getUser();
            String clave = datos.getPass();
            String pregunta = datos.getPregunta();
            String respuesta = datos.getRespuesta();

            //getting the current time for joining date
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String fecha1 = sdf.format(cal.getTime());

            Cursor fila = this.getWritableDatabase().rawQuery("select usuario from usuarios where usuario='"+datos.getUser()+"'", null);
            if(fila.moveToFirst()==true){
                estado = false;
            }else {
                //estado = (boolean)this.getWritableDatabase().insert("datos","nombre, correo, telefono",registro);
                //resultado = (int) this.getWritableDatabase().insert("usuarios", "nombres,apellidos,usuario,clave,pregunta,respuesta", registro);
                String SQL = "INSERT INTO usuarios \n" +
                        "(nombres,apellidos,usuario,clave,pregunta,respuesta,fecha)\n" +
                        "VALUES \n" +
                        "(?, ?, ?, ?, ?, ?, ?);";

                //resultado = (int) this.getWritableDatabase().insert("usuarios", "nombres,apellidos,usuario,clave,pregunta,respuesta", registro);
                this.getWritableDatabase().execSQL(SQL, new String[]{nombres,apellidos,usuario,clave,pregunta,respuesta,fecha1});
                //if (resultado > 0) estado = true;
                //else estado = false;
                estado = true;
            }
        }catch (Exception e){
            estado = false;
            Log.e("error.",e.toString());
        }
        return estado;
    }

    //Función para actualizar datos
    public boolean updateDatos(dto datos){
        //GestionSQLiteOpenHelper gestionSQLiteOpenHelper = new GestionSQLiteOpenHelper(this, "gestionn", null, 1);
        //SQLiteDatabase bd = gestionSQLiteOpenHelper.getWritableDatabase();
        boolean estado = true;
        SQLiteDatabase bd = this.getWritableDatabase();
        try{
            int codigo = datos.getCodigo();
            String nombres = datos.getNombres();
            String apellidos = datos.getApellidos();
            String usuario = datos.getUser();
            String clave = datos.getPass();
            String pregunta = datos.getPregunta();
            String respuesta = datos.getRespuesta();

            ContentValues registro = new ContentValues();
            //registro.put("codigo",codigo);
            registro.put("nombres",nombres);
            registro.put("apellidos",apellidos);
            registro.put("usuario",usuario);
            registro.put("clave",clave);
            registro.put("pregunta",pregunta);
            registro.put("respuesta", respuesta);

            int cant = (int)this.getWritableDatabase().update("usuarios", registro, "codigo="+codigo, null);
            //int cant = bd.update("usuarios", registro,"codigo="+codigo,null);
            bd.close();

            if (cant > 0) estado = true;
            else estado = false;
            /*if(cant==1){
                return true;
            }else{
                return false;
            }*/
        }catch(Exception e){
            estado = false;
            Log.e("error.",e.toString());
        }

        return estado;
    }

    //Función para eliminar datos
    public boolean deleteDatos(dto datos){
        boolean estado = true;
        SQLiteDatabase bd = this.getWritableDatabase();
        try{
            int codigo = datos.getCodigo();
            //int cant = (int)this.getWritableDatabase().delete("usuarios","codigo="+codigo, null);
            int cant = (int)bd.delete("usuarios","codigo="+codigo, null);
            bd.close();
            if (cant > 0) estado = true;
            else estado = false;
        }catch (Exception e){
            estado = false;
            Log.e("Error.", e.toString());
        }
        return estado;
    }

    //Función para consultar la existencia del nombre de usuario o dirección de correo.
    public boolean consultaUser(dto datos) {
        boolean estado = false;
        SQLiteDatabase bd = this.getWritableDatabase();
        try {
            String user = datos.getUser();
            Cursor fila = bd.rawQuery("select usuario from usuarios where usuario='"+datos.getUser()+"'", null);
            if(fila.moveToFirst()){
               estado = true;
            }else{
               estado = false;
            }
            bd.close();
        } catch (Exception e) {
            estado = false;
            Log.e("error.",e.toString());
        }
        return estado;
    }

    public boolean consultaNombrecompleto(dto datos) {
        boolean estado = false;
        SQLiteDatabase bd = this.getWritableDatabase();
        try {
            String user = datos.getUser();
            Cursor fila = bd.rawQuery("select codigo,nombres,apellidos from usuarios where codigo=1", null);
            if(fila.moveToFirst()){
                estado = true;
            }else{
                estado = false;
            }
            bd.close();
        } catch (Exception e) {
            estado = false;
            Log.e("error.",e.toString());
        }
        return estado;
    }

    public void showEmployeesFromDatabase() {
        SQLiteDatabase bd = this.getWritableDatabase();
        Cursor cursorEmployees = bd.rawQuery("SELECT * FROM usuarios", null);
        /*//if the cursor has some data
        if (cursorEmployees.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                employeeList.add(new Employee(
                        cursorEmployees.getInt(0),
                        cursorEmployees.getString(1),
                        cursorEmployees.getString(2),
                        cursorEmployees.getString(3),
                        cursorEmployees.getDouble(4)
                ));
            } while (cursorEmployees.moveToNext());
        }
        //closing the cursor
        cursorEmployees.close();*/

    }

    //Obtener la lista de comentarios en la base de datos
    public ArrayList<dto> getUsers(){
        //Creamos el cursor
        //SQLiteDatabase bd = this.getWritableDatabase();
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<dto> lista=new ArrayList<dto>();
        //Cursor c =  db.rawQuery( "select * from contacts", null );
        Cursor c = db.rawQuery("select codigo,nombres,apellidos,usuario,clave,pregunta,respuesta,fecha from usuarios", null);
        if (c != null && c.getCount()>0) {
            c.moveToFirst();
            do {
                //Asignamos el valor en nuestras variables para crear un nuevo objeto Comentario
                int codigo = c.getInt(c.getColumnIndex("codigo"));
                String nombres = c.getString(c.getColumnIndex("nombres"));
                String apellidos = c.getString(c.getColumnIndex("apellidos"));
                String usuario = c.getString(c.getColumnIndex("usuario"));
                String clave = c.getString(c.getColumnIndex("clave"));
                String pregunta = c.getString(c.getColumnIndex("pregunta"));
                String respuesta = c.getString(c.getColumnIndex("respuesta"));
                String fecha = c.getString(c.getColumnIndex("fecha"));
                dto com =new dto(codigo,nombres,apellidos,usuario,clave,pregunta,respuesta,fecha);
                //Añadimos el comentario a la lista
                lista.add(com);
            } while (c.moveToNext());
        }
        //Cerramos el cursor
        c.close();
        return lista;
    }

    //Obtener la lista de comentarios en la base de datos
    public ArrayList<String> getUsers1(){
        //Creamos el cursor
        //SQLiteDatabase bd = this.getWritableDatabase();
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> lista=new ArrayList<String>();
        //Cursor c =  db.rawQuery( "select * from contacts", null );
        Cursor c = db.rawQuery("select * from usuarios", null);
        if (c != null && c.getCount()>0) {
            c.moveToFirst();
            do {
                //Asignamos el valor en nuestras variables para crear un nuevo objeto Comentario
                int codigo = c.getInt(c.getColumnIndex("codigo"));
                //int codigo =c.getInt(0);
                String nombres = c.getString(c.getColumnIndex("nombres"));
                //String nombres =c.getString(1);
                String apellidos = c.getString(c.getColumnIndex("apellidos"));
                //String apellidos =c.getString(2);
                String usuario = c.getString(c.getColumnIndex("usuario"));
                //String usuario =c.getString(3);
                String clave = c.getString(c.getColumnIndex("clave"));
                //String clave =c.getString(4);
                String pregunta = c.getString(c.getColumnIndex("pregunta"));
                //String pregunta =c.getString(5);
                String respuesta = c.getString(c.getColumnIndex("respuesta"));
                //String respuesta =c.getString(6);
                String fecha = c.getString(c.getColumnIndex("fecha"));
                //String fecha =c.getString(7);

                //Añadimos el comentario a la lista
                lista.add(String.valueOf(codigo));
                lista.add(nombres);
                lista.add(apellidos);
                lista.add(usuario);
                lista.add(clave);
                lista.add(pregunta);
                lista.add(respuesta);
                lista.add(fecha);
            } while (c.moveToNext());
        }
        //Cerramos el cursor
        c.close();
        return lista;
    }

    public ArrayList<String> getAllUsers() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from usuarios", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex("usuario")));
            res.moveToNext();
        }

        res.close();
        return array_list;
    }

    public dto getUser(int id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("usuarios",
                new String[]{"codigo","nombres","apellidos","usuario","clave","pregunta","respuesta","fecha"},
                 "codigo=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        dto datos = new dto(
                cursor.getInt(cursor.getColumnIndex("codigo")),
                cursor.getString(cursor.getColumnIndex("nombres")),
                cursor.getString(cursor.getColumnIndex("apellidos")),
                cursor.getString(cursor.getColumnIndex("usuario")),
                cursor.getString(cursor.getColumnIndex("clave")),
                cursor.getString(cursor.getColumnIndex("pregunta")),
                cursor.getString(cursor.getColumnIndex("respuesta")),
                cursor.getString(cursor.getColumnIndex("fecha")));
        //close the db connection
        cursor.close();
        return datos;
    }

    //Función para consultar la existencia del nombre de usuario o dirección de correo.
    public boolean verificoRespuesta(dto datos) {
        boolean estado = false;
        SQLiteDatabase bd = this.getWritableDatabase();
        try {
            String user = datos.getRespuesta();
            Cursor fila = bd.rawQuery("select respuesta from usuarios where respuesta='"+datos.getRespuesta()+"'", null);
            if(fila.moveToFirst()){
                estado = true;
            }else{
                estado = false;
            }
            bd.close();
        } catch (Exception e) {
            estado = false;
            Log.e("error.",e.toString());
        }
        return estado;
    }

    //Función para actualizar datos
    public boolean updateClave(dto datos){
        //GestionSQLiteOpenHelper gestionSQLiteOpenHelper = new GestionSQLiteOpenHelper(this, "gestionn", null, 1);
        //SQLiteDatabase bd = gestionSQLiteOpenHelper.getWritableDatabase();
        boolean estado = true;
        SQLiteDatabase bd = this.getWritableDatabase();
        try{
            int codigo = datos.getCodigo();
            String clave = datos.getPass();
            ContentValues registro = new ContentValues();
            //registro.put("codigo",codigo);
            registro.put("clave",clave);
            int cant = (int)this.getWritableDatabase().update("usuarios", registro, "codigo="+codigo, null);
            //int cant = bd.update("usuarios", registro,"codigo="+codigo,null);
            bd.close();

            if (cant > 0) estado = true;
            else estado = false;
            /*if(cant==1){
                return true;
            }else{
                return false;
            }*/
        }catch(Exception e){
            estado = false;
            Log.e("error.",e.toString());
        }

        return estado;
    }

   /* public ObjectStudent readSingleRecord(int studentId) {

        ObjectStudent objectStudent = null;
        String sql = "SELECT * FROM students WHERE id = " + studentId;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
            String firstname = cursor.getString(cursor.getColumnIndex("firstname"));
            String email = cursor.getString(cursor.getColumnIndex("email"));

            objectStudent = new ObjectStudent();
            objectStudent.id = id;
            objectStudent.firstname = firstname;
            objectStudent.email = email;

        }

        cursor.close();
        db.close();

        return objectStudent;

    }*/

}
