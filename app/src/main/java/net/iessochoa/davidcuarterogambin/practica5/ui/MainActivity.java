package net.iessochoa.davidcuarterogambin.practica5.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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

    DiaDiario diaDiario;
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

        // ****************************** Controles RecyclerView ***********************************

        // Para editar hay que pulsar en el cardView y lanza una activity para obtener resultado
        diarioAdapter.setOnClickItemListener(new DiarioAdapter.OnItemClickItemListener() {
            @Override
            public void onItemClickItem(DiaDiario diaDiario) {
                Intent intent = new Intent(MainActivity.this, EdicionDiaActivity.class);
                intent.putExtra(EdicionDiaActivity.EXTRA_DIA, diaDiario);
                startActivityForResult(intent, REQUEST_EDITA_DIA);
            }
        });


        diarioAdapter.setOnClickBorrarListener(new DiarioAdapter.OnItemClickBorrarListener() {
            @Override
            public void onItemClickBorrar(DiaDiario diaDiario) {
                eliminarDia(diaDiario);
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

    // Menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // Acciones

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case REQUEST_NUEVO_DIA:
                if (resultCode != RESULT_CANCELED) {
                    diaDiario = intent.getParcelableExtra(EdicionDiaActivity.EXTRA_DIA);
                    diarioViewModel.insert(diaDiario);
                }
                break;

            case REQUEST_EDITA_DIA:
                if (resultCode != RESULT_CANCELED) {
                    diaDiario = intent.getParcelableExtra(EdicionDiaActivity.EXTRA_DIA);
                    diarioViewModel.update(diaDiario);
                }
                break;
        }
    }

    private void eliminarDia(DiaDiario diaDiario) {
        AlertDialog.Builder dialogoEliminar = new AlertDialog.Builder(MainActivity.this);
        dialogoEliminar.setTitle("Aviso");
        dialogoEliminar.setMessage("¿Está seguro que desea eliminar esta tarea?");

        dialogoEliminar.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                diarioViewModel.delete(diaDiario);
            }
        });
        dialogoEliminar.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        dialogoEliminar.show();
    }

    private void iniciaViews() {
        fabNuevo = findViewById(R.id.fabNuevo);
        toolbar = findViewById(R.id.toolbar);
        rvLista = findViewById(R.id.rvLista);
        svBusqueda = findViewById(R.id.svBusqueda);
    }
}