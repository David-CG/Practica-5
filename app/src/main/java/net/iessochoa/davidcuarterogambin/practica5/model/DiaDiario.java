package net.iessochoa.davidcuarterogambin.practica5.model;

import android.provider.BaseColumns;

import java.util.Date;

class DiaDiario {
    public static final String TABLE_NAME="diario";
    public static final String ID= BaseColumns._ID;
    public static final String FECHA="fecha";
    public static final String VALORACION_DIA="valoracion_dia";
    public static final String RESUMEN="resumen";
    public static final String CONTENIDO="contenido";
    public static final String FOTO_URI="foto_uri";
    private int id;
    private Date fecha;
    private int valoracionDia;
    private String resumen;
    private String contenido;
    private String fotoUri;
}