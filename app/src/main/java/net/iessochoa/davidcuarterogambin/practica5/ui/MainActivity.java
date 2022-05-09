package net.iessochoa.davidcuarterogambin.practica5.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.iessochoa.davidcuarterogambin.practica5.R;
import net.iessochoa.davidcuarterogambin.practica5.model.DiaDiario;
import net.iessochoa.davidcuarterogambin.practica5.viewmodels.DiarioViewModel;

import java.util.Date;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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
        diarioViewModel.getAllDiarios().observe(this, diario -> diarioAdapter.setListaTareas(diario));

        // ****************************** Controles RecyclerView ***********************************

        // Para editar hay que pulsar en el cardView y lanza una activity para obtener resultado
        diarioAdapter.setOnClickItemListener(diaDiario -> {
            Intent intent = new Intent(MainActivity.this, EdicionDiaActivity.class);
            intent.putExtra(EdicionDiaActivity.EXTRA_DIA, diaDiario);
            startActivityForResult(intent, REQUEST_EDITA_DIA);
        });


        diarioAdapter.setOnClickBorrarListener(this::eliminarDia);

        // ************************************* Nuevo diario **************************************

        fabNuevo.setOnClickListener(view -> {
            Intent i = new Intent(this, EdicionDiaActivity.class);
            startActivityForResult(i, REQUEST_NUEVO_DIA);
        });

        // *************************************** Toolbar *****************************************
        setSupportActionBar(toolbar);

        // ************************************** Búsqueda *****************************************

        svBusqueda.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                diarioViewModel.setBusqueda(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0) {
                    diarioViewModel.setBusqueda("");
                }
                return false;
            }
        });

        // ******************************** Orientación pantalla ***********************************
        // Si la pantalla está en horizontal
        int orientacion = getResources().getConfiguration().orientation;
        if (orientacion == Configuration.ORIENTATION_PORTRAIT)
            rvLista.setLayoutManager(new LinearLayoutManager(this));
        else
            rvLista.setLayoutManager(new GridLayoutManager(this, 2));

        // *************************************** Swiper ******************************************

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT |
                ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                diaDiario = ((DiarioAdapter.DiarioViewHolder) viewHolder).getDia();
                eliminarDia(diaDiario);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rvLista);

    }

    // ********************* Menu *************************
    // Añade el menú y los botones del menu con sus respectivas acciones

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_ordenar:
                dialogoOrdenar();
                return true;

            case R.id.action_valoravida:
                dialogoValorVida();
                return true;

            case R.id.action_fechas:
                return true;

            case R.id.action_ultimodia:
                muestraUltimoDia();
                return true;

            case R.id.action_config:
                Intent intent = new Intent(this, PreferenciasActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_acercade:
                FragmentManager fragmentManager = getSupportFragmentManager();
                DialogoAcercaDe dialogo = new DialogoAcercaDe();
                dialogo.show(fragmentManager, getString(R.string.about_title));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Método que crea el diálogo de ordenar
    public void dialogoOrdenar() {
        Context context = getApplicationContext();
        final CharSequence[] items = {"Fecha", "Valoración", "Resumen"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle(R.string.ordena_titulo);
        builder.setItems(items, (dialog, item) -> {
            Toast.makeText(context, getString(R.string.ordena_contenido) + items[item], Toast.LENGTH_SHORT).show();
            dialog.dismiss();

        }).show();
    }

    // Clase que crea el diálogo Acerca de
    public static class DialogoAcercaDe extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setMessage(R.string.about_content)
                    .setTitle(getString(R.string.about_title))
                    .setPositiveButton(R.string.ok_button, (dialog, id) -> dialog.cancel());

            return builder.create();
        }
    }

    private void dialogoValorVida() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater layoutInflater = MainActivity.this.getLayoutInflater();
        View valoraVida = layoutInflater.inflate(R.layout.valora_vida, null);
        ImageView ivValoraVida = valoraVida.findViewById(R.id.ivValoraVida);

        diarioViewModel.getValoracionTotal().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Float>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Float aFloat) {
                if (aFloat < 5) {
                    ivValoraVida.setImageResource(R.drawable.sad);
                } else if (aFloat >= 5 && aFloat < 8) {
                    ivValoraVida.setImageResource(R.drawable.neutral);
                } else {
                    ivValoraVida.setImageResource(R.drawable.smile);
                }
                alertDialog.setPositiveButton(R.string.ok_button, null);
                alertDialog.setView(valoraVida);
                alertDialog.show();
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }


    private void muestraUltimoDia() {

        //recuperamos las preferencias
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);

        //recuperamos la fecha del últimos día editado
        long ultimoDia = sharedPref.getLong(getString(R.string.pref_key_ultimo_dia), 0);

        //mostramos el resultado
        Toast.makeText(this, "El último día editado " + DiaDiario.getFechaFormatoLocal(new Date(ultimoDia)), Toast.LENGTH_LONG).show();
    }

    // ******************* Acciones ***********************

    @RequiresApi(api = Build.VERSION_CODES.N)
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

        dialogoEliminar.setPositiveButton(android.R.string.yes, (dialogInterface, i) -> diarioViewModel.delete(diaDiario));
        dialogoEliminar.setNegativeButton(android.R.string.no, (dialogInterface, i) -> {
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