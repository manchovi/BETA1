package com.mjgl.utla.utla;

public class dto {

    int codigo;
    String nombres;
    String apellidos;
    String user;
    String pass;
    String pregunta;
    String respuesta;
    String fechahora;

    public dto() {

    }

    public dto(int codigo, String nombres, String apellidos, String user, String pass, String pregunta, String respuesta) {
        this.codigo = codigo;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.user = user;
        this.pass = pass;
        this.pregunta = pregunta;
        this.respuesta = respuesta;
    }

    public dto(int codigo, String nombres, String apellidos, String user, String pass, String pregunta, String respuesta, String fechahora) {
        this.codigo = codigo;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.user = user;
        this.pass = pass;
        this.pregunta = pregunta;
        this.respuesta = respuesta;
        this.fechahora = fechahora;
    }

    public String getFechahora() {
        return fechahora;
    }

    public void setFechahora(String fechahora) {
        this.fechahora = fechahora;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }


}
