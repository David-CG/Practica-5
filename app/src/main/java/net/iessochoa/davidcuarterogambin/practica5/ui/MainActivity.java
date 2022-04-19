package net.iessochoa.davidcuarterogambin.practica5.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.iessochoa.davidcuarterogambin.practica5.R;
import net.iessochoa.davidcuarterogambin.practica5.model.DiaDiario;
import net.iessochoa.davidcuarterogambin.practica5.viewmodels.DiarioViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static int REQUEST_NUEVO_DIA = 0;
    private final static int REQUEST_EDITA_DIA = 1;

    DiarioViewModel diarioViewModel;
    DiarioAdapter diarioAdapter;

    FloatingActionButton fabNuevo;
    Toolbar toolbar;
    RecyclerView rvLista;
    SearchView svBusqueda;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicia las views de la activity
        iniciaViews();

        // RecyclerView

        diarioAdapter = new DiarioAdapter();
        rvLista.setLayoutManager(new LinearLayoutManager(this));
        rvLista.setAdapter(diarioAdapter);
        diarioViewModel = new ViewModelProvider(this).get(DiarioViewModel.class);
        diarioViewModel.getAllDiarios().observe(this, new Observer<List<DiaDiario>>() {
            @Override
            public void onChanged(List<DiaDiario> diario) {
                diarioAdapter.setListaTareas(diario);
            }
        });

        // Nuevo diario

        fabNuevo.setOnClickListener(view -> {
            Intent i = new Intent(this, EdicionDiaActivity.class);
            startActivityForResult(i, REQUEST_NUEVO_DIA);
        });

        // Toolbar
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void iniciaViews() {
        fabNuevo = findViewById(R.id.fabNuevo);
        toolbar = findViewById(R.id.toolbar);
        rvLista = findViewById(R.id.rvLista);
        svBusqueda = findViewById(R.id.svBusqueda);
    }

    // onActivityResult editar y nuevo
}