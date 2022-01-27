package net.iessochoa.davidcuarterogambin.practica5.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.iessochoa.davidcuarterogambin.practica5.R;
import net.iessochoa.davidcuarterogambin.practica5.model.DiaDiario;

public class MainActivity extends AppCompatActivity {

    private final static int REQUEST_NUEVO_DIA = 0;

    FloatingActionButton fabNuevo;

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

        fabNuevo.setOnClickListener(view -> {
            Intent i = new Intent(this, EdicionDiaActivity.class);
            startActivityForResult(i, REQUEST_NUEVO_DIA);});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void iniciaViews() {
        fabNuevo = findViewById(R.id.fabNuevo);
    }
}