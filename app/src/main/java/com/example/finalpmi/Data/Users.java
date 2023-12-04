package com.example.finalpmi.Data;

public class Users {
    private static String nombres, apellidos, correo, telefono, dni, fechanac, password, foto;
    private static Integer cuenta, id_carrera;



    public static String getPhoto() {
        return foto;
    }

    public static void setPhoto(String photo) {
        Users.foto = foto;
    }

    public Users(String nombres, String apellidos, String correo, String dni, String telefono, String fechanac, String password, String rutaFoto){
        this.nombres = nombres;
        this.password=password;
        this.apellidos=apellidos;
        this.telefono=telefono;
        this.correo=correo;
        this.dni=dni;
        this.fechanac=fechanac;
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

    public static String getDni() {
        return dni;
    }

    public static void setDni(String dni) {
        Users.dni = dni;
    }

    public static String getFechanac() {
        return fechanac;
    }

    public static void setFechanac(String fechanac) {
        Users.fechanac = fechanac;
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

    public static Integer getCuenta() {
        return cuenta;
    }

    public static void setCuenta(Integer cuenta) {
        Users.cuenta = cuenta;
    }

    public static Integer getId_carrera() {
        return id_carrera;
    }

    public static void setId_carrera(Integer id_carrera) {
        Users.id_carrera = id_carrera;
    }
}
