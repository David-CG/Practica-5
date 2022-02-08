package net.iessochoa.davidcuarterogambin.practica5.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface DiarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    static void insert(DiaDiario diaDiario) {}

    static void update(DiaDiario diaDiario) { }

    static void deleteByDiaDiario(DiaDiario diaDiario) { }

    static void deleteAll() { }

    static LiveData<List<DiaDiario>> getAllDiario(String resumen) { return null; }

    static LiveData<List<DiaDiario>> getDiaDiarioOrderBy(String order) { return null; }

    static Single<Integer> getValoracionTotal() { return null; }
}
