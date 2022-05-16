package net.iessochoa.davidcuarterogambin.practica5.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface DiarioDao {

    // Opciones CRUD

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DiaDiario diaDiario);

    @Update
    void update(DiaDiario diaDiario);

    @Delete
    void deleteByDiaDiario(DiaDiario diaDiario);

    @Query("DELETE FROM " + DiaDiario.TABLE_NAME)
    void deleteAll();

    @Query("SELECT * FROM " + DiaDiario.TABLE_NAME)
    LiveData<List<DiaDiario>> getAllDiario();

    @Query("SELECT * FROM diario WHERE resumen LIKE '%' || :resumen || '%'")
    LiveData<List<DiaDiario>> getDiaDiarioOrderBy(String resumen);

    @Query("SELECT AVG(valoracion_dia) FROM " + DiaDiario.TABLE_NAME)
    Single<Float> getValoracionTotal();
}
