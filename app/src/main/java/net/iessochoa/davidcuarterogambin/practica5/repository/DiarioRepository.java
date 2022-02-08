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

    public DiarioDao diarioDao;
    public LiveData<List<DiaDiario>> allDiarios;

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
        //allDiarios = diarioDao.getAllDiario();
    }

    public LiveData<List<DiaDiario>> getAllDiarios() {
        return allDiarios;
    }

    public Single<Integer> getValoracionDiario() {
        return DiarioDao.getValoracionTotal();
    }

    public void insert(DiaDiario diaDiario) {
        DiarioDatabase.databaseWriteExecutor.execute(() -> {
            DiarioDao.insert(diaDiario);
        });
    }

    public void delete(DiaDiario diaDiario) {
        DiarioDatabase.databaseWriteExecutor.execute(() -> {
            DiarioDao.deleteByDiaDiario(diaDiario);
        });
    }
}
