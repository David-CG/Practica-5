package net.iessochoa.davidcuarterogambin.practica5.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.iessochoa.davidcuarterogambin.practica5.R;
import net.iessochoa.davidcuarterogambin.practica5.model.DiaDiario;
import net.iessochoa.davidcuarterogambin.practica5.viewmodels.DiarioViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static int REQUEST_NUEVO_DIA = 0;

    FloatingActionButton fabNuevo;
    DiarioViewModel diarioViewModel;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicia las views de la activity
        iniciaViews();

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        diarioViewModel = new ViewModelProvider(this).get(DiarioViewModel.class);
        diarioViewModel.getAllDiarios().observe(this, new
                Observer<List<DiaDiario>>() {
                    @Override
                    public void onChanged(List<DiaDiario> diario) {
                        // adapter.setDiario(diario);
                        Log.d("P5", "tamaño: " + diario.size());
                    }
                });


        fabNuevo.setOnClickListener(view -> {
            Intent i = new Intent(this, EdicionDiaActivity.class);
            startActivityForResult(i, REQUEST_NUEVO_DIA);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void iniciaViews() {
        fabNuevo = findViewById(R.id.fabNuevo);
    }

    // onActivityResult editar y nuevo
}