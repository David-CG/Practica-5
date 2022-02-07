package net.iessochoa.davidcuarterogambin.practica5.model;

import androidx.lifecycle.LiveData;

import java.util.List;

import io.reactivex.Single;

public interface DiarioDao {

    public static void insert(DiaDiario diaDiario) {

    }

    public static void deleteByDiaDiario(DiaDiario diaDiario) {

    }

    public static void update(DiaDiario diaDiario) {

    }

    public static void deleteAll() {

    }

    public static LiveData<List<DiaDiario>> getAllDiario(String resumen) {

        return null;
    }

    public static LiveData<List<DiaDiario>> getDiaDiarioOrderBy(String resumen) {

        return null;
    }

    public static Single<Float> getValoracionTotal() {

        return null;
    }
}
