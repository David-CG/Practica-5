package net.iessochoa.davidcuarterogambin.practica5.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import net.iessochoa.davidcuarterogambin.practica5.model.DiaDiario;
import net.iessochoa.davidcuarterogambin.practica5.repository.DiarioRepository;

import java.util.List;

public class DiarioViewModel {

    private DiarioRepository repository;
    private LiveData<List<DiaDiario>> allDiarios;

    public DiarioViewModel(@NonNull Application application) {
        //super(application);
        repository = DiarioRepository.getInstance(application);
        allDiarios = repository.getAllDiarios();
    }

    public LiveData<List<DiaDiario>> getAllDiarios() {
        return allDiarios;
    }

    public void insert(DiaDiario diaDiario) {
        repository.insert(diaDiario);
    }

    public void delete(DiaDiario diaDiario) {
        repository.delete(diaDiario);
    }
}
