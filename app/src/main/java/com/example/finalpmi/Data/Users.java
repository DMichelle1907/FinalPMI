package com.example.finalpmi.Data;

public class Users {
    private static String nombres, apellidos, correo, cuenta, telefono, password, foto;
    private  static long carrera;

    public static String getPhoto() {
        return foto;
    }

    public static void setPhoto(String photo) {
        Users.foto = foto;
    }

    public static String getCuenta() {
        return cuenta;
    }

    public static void setCuenta(String cuenta) {
        Users.cuenta = cuenta;
    }

    public Users(String nombres, String apellidos, String correo, String cuenta, String telefono, String password, long carrera){
        this.nombres = nombres;
        this.password=password;
        this.apellidos=apellidos;
        this.telefono=telefono;
        this.correo=correo;
        this.cuenta=cuenta;
        this.carrera=carrera;
    }

    public static String getNombres() {
        return nombres;
    }

    public static void setNombres(String nombres) {
        Users.nombres = nombres;
    }

    public static String getApellidos() {
        return apellidos;
    }

    public static void setApellidos(String apellidos) {
        Users.apellidos = apellidos;
    }

    public static String getCorreo() {
        return correo;
    }

    public static void setCorreo(String correo) {
        Users.correo = correo;
    }

    public static String getTelefono() {
        return telefono;
    }

    public static void setTelefono(String telefono) {
        Users.telefono = telefono;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        Users.password = password;
    }

    public static String getFoto() {
        return foto;
    }

    public static void setFoto(String foto) {
        Users.foto = foto;
    }

    public static long getCarrera() {
        return carrera;
    }

    public static void setCarrera(long carrera) {
        Users.carrera = carrera;
    }
}
