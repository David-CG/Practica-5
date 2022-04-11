package net.iessochoa.davidcuarterogambin.practica5.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

@Entity(tableName = DiaDiario.TABLE_NAME,
        indices = {@Index(value = {DiaDiario.FECHA}, unique = true)})
public class DiaDiario implements Parcelable {

    public static final String TABLE_NAME = "diario";
    public static final String ID = BaseColumns._ID;
    public static final String FECHA = "fecha";
    public static final String VALORACION_DIA = "valoracion_dia";
    public static final String RESUMEN = "resumen";
    public static final String CONTENIDO = "contenido";
    public static final String FOTO_URI = "foto_uri";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    @NonNull
    private int id;

    @ColumnInfo(name = FECHA)
    private Date fecha;

    @ColumnInfo(name = VALORACION_DIA)
    private int valoracionDia;

    @ColumnInfo(name = RESUMEN)
    @NonNull
    private String resumen;

    @ColumnInfo(name = CONTENIDO)
    @NonNull
    private String contenido;

    @ColumnInfo(name = FOTO_URI)
    private String fotoUri;

    // ************************* Constructores *************************

    @Ignore
    public DiaDiario(Date fecha, int valoracionDia, String resumen, String contenido, String fotoUri) {
        this.fecha = fecha;
        this.valoracionDia = valoracionDia;
        this.resumen = resumen;
        this.contenido = contenido;
        this.fotoUri = fotoUri;
    }

    public DiaDiario(Date fecha, int valoracionDia, String resumen, String contenido) {
        this.fecha = fecha;
        this.valoracionDia = valoracionDia;
        this.resumen = resumen;
        this.contenido = contenido;
        this.fotoUri = "";
    }

    // *********************** Getters y setters ***********************

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    @NonNull
    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    @NonNull
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @NonNull
    public int getValoracionDia() {
        return valoracionDia;
    }

    public void setValoracionDia(int valoracionDia) {
        this.valoracionDia = valoracionDia;
    }

    public String getFotoUri() {
        return fotoUri;
    }

    public void setFotoUri(String fotoUri) {
        this.fotoUri = fotoUri;
    }

    public int getValoracionResumida() {
        if (valoracionDia < 5) {
            return 1;
        } else if (valoracionDia < 8) {
            return 2;
        } else {
            return 3;
        }
    }

    public static int getValoracionResumida(DiaDiario diaDiario) {
        if (diaDiario.valoracionDia < 5) {
            return 1;
        } else if (diaDiario.valoracionDia < 8) {
            return 2;
        } else {
            return 3;
        }
    }

    public String getFechaFormatoLocal() {
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM,
                Locale.getDefault());
        return df.format(fecha);
    }

    public static String getFechaFormatoLocal(Date fechaDia) {
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM,
                Locale.getDefault());
        return df.format(fechaDia);
    }

    // *************************** Parcerable **************************

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeLong(this.fecha != null ? this.fecha.getTime() : -1);
        dest.writeInt(this.valoracionDia);
        dest.writeString(this.resumen);
        dest.writeString(this.contenido);
        dest.writeString(this.fotoUri);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        long tmpFecha = source.readLong();
        this.fecha = tmpFecha == -1 ? null : new Date(tmpFecha);
        this.valoracionDia = source.readInt();
        this.resumen = source.readString();
        this.contenido = source.readString();
        this.fotoUri = source.readString();
    }

    protected DiaDiario(Parcel in) {
        this.id = in.readInt();
        long tmpFecha = in.readLong();
        this.fecha = tmpFecha == -1 ? null : new Date(tmpFecha);
        this.valoracionDia = in.readInt();
        this.resumen = in.readString();
        this.contenido = in.readString();
        this.fotoUri = in.readString();
    }

    public static final Parcelable.Creator<DiaDiario> CREATOR = new Parcelable.Creator<DiaDiario>() {
        @Override
        public DiaDiario createFromParcel(Parcel source) {
            return new DiaDiario(source);
        }

        @Override
        public DiaDiario[] newArray(int size) {
            return new DiaDiario[size];
        }
    };
}