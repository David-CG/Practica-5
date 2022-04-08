package net.iessochoa.davidcuarterogambin.practica5.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import net.iessochoa.davidcuarterogambin.practica5.model.DiaDiario;
import net.iessochoa.davidcuarterogambin.practica5.repository.DiarioRepository;

import java.util.List;

import io.reactivex.Single;

public class DiarioViewModel {

    private DiarioRepository repository;
    private MutableLiveData<String> busqueda;
    private LiveData<List<DiaDiario>> allDiarios;

    public DiarioViewModel(@NonNull Application application) {
        super(application);
        repository = DiarioRepository.getInstance(application);
        allDiarios = repository.getAllDiarios();

        busqueda = new MutableLiveData<>();
        busqueda.setValue("");

        allDiarios = Transformations.switchMap(busqueda, repository::getAllDiarios);
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

    public void update(DiaDiario diaDiario) {
        DiarioRepository.update(diaDiario);
    }

    public Single<Float> getValoracionTotal() {
        return repository.getValoracionDiario();
    }

    public void setBusqueda(String resultado) {
        busqueda.setValue(resultado);
    }
}
