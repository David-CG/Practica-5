package net.iessochoa.davidcuarterogambin.practica5.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import net.iessochoa.davidcuarterogambin.practica5.model.DiaDiario;
import net.iessochoa.davidcuarterogambin.practica5.model.DiarioDao;
import net.iessochoa.davidcuarterogambin.practica5.model.DiarioDatabase;

import java.util.List;

import io.reactivex.Single;

public class DiarioRepository {
    private static volatile DiarioRepository INSTANCE;

    private DiarioDao diarioDao;
    private LiveData<List<DiaDiario>> allDiarios;

    public static DiarioRepository getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (DiarioRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DiarioRepository(application);
                }
            }
        }
        return INSTANCE;
    }

    private DiarioRepository(Application application) {
        DiarioDatabase db = DiarioDatabase.getDatabase(application);
        diarioDao = db.diarioDao();
        allDiarios = diarioDao.getAllDiario();
    }

    public LiveData<List<DiaDiario>> getAllDiarios() {
        return allDiarios;
    }

    public LiveData<List<DiaDiario>> getDiarioOrderByResumen(String resumen) {
        allDiarios = diarioDao.getDiaDiarioOrderBy(resumen);
        return allDiarios;
    }

    public Single<Float> getValoracionDiario() {
        return diarioDao.getValoracionTotal();
    }

    public void insert(DiaDiario diaDiario) {
        DiarioDatabase.databaseWriteExecutor.execute(() -> diarioDao.insert(diaDiario));
    }

    public void delete(DiaDiario diaDiario) {
        DiarioDatabase.databaseWriteExecutor.execute(() -> diarioDao.deleteByDiaDiario(diaDiario));
    }

    public void update(DiaDiario diaDiario){
        DiarioDatabase.databaseWriteExecutor.execute(()-> diarioDao.update(diaDiario));
    }

    public void deleteAll(){
        DiarioDatabase.databaseWriteExecutor.execute(() -> diarioDao.deleteAll());
    }
}
