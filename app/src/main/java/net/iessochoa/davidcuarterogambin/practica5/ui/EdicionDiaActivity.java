package net.iessochoa.davidcuarterogambin.practica5.ui;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import net.iessochoa.davidcuarterogambin.practica5.R;
import net.iessochoa.davidcuarterogambin.practica5.model.DiaDiario;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.N)
public class EdicionDiaActivity extends AppCompatActivity {

    static final String EXTRA_DIA = "net.iessochoa.davidcuartero.practica5.EdicionDiaActivity.DiaCreado";
    private static final int STATUS_CODE_SELECCION_IMAGEN = 300;
    private static final int MY_PERMISSIONS = 100;

    ConstraintLayout clPrincipal;
    TextView tvFecha;
    Date fecha;
    ImageView ivFecha, ivFotoDia;
    EditText etResumen, etContenido;
    Spinner spValoracion;
    FloatingActionButton fabGuardar, fabImagen;
    private Uri uriFoto = null;

    DiaDiario diaDiario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edicion_dia);

        // Inicia las views de la activity
        iniciaViews();

        ivFecha.setOnClickListener(view -> onClickFecha());

        // Spinner
        spValoracion.setAdapter(ArrayAdapter.createFromResource(this, R.array.spValoracion, android.R.layout.simple_spinner_dropdown_item));
        spValoracion.setSelection(5);

        //Comprueba si el día recibido no esta vacío
        diaDiario = getIntent().getParcelableExtra(EXTRA_DIA);
        if (diaDiario != null) {

            tvFecha.setText(diaDiario.getFechaFormatoLocal());
            etResumen.setText(diaDiario.getResumen());
            etContenido.setText(diaDiario.getContenido());
            spValoracion.setSelection(Arrays.asList(getResources().getStringArray(R.array.spValoracion)).indexOf((Integer) diaDiario.getValoracionDia()));
            fecha = diaDiario.getFecha();
        }

        // Comprueba que no haya campos vacíos y guarda los datos introducidos para enviarlos al main
        fabGuardar.setOnClickListener(view -> {
            etResumen.setText(etResumen.getText().toString().trim());
            etContenido.setText(etContenido.getText().toString().trim());
            if ((tvFecha == null || etResumen.getText().toString().isEmpty()) || (etContenido.getText().toString().isEmpty())) {
                camposIncompletos();
            } else {
                if (diaDiario != null) {
                    diaDiario.setFecha(fecha);
                    diaDiario.setValoracionDia(Integer.parseInt(spValoracion.getSelectedItem().toString()));
                    diaDiario.setContenido(etContenido.getText().toString());
                    diaDiario.setResumen(etResumen.getText().toString());
                    Intent intent = getIntent();
                    intent.putExtra(EXTRA_DIA, diaDiario);
                    setResult(RESULT_OK, intent);
                } else {
                    diaDiario = new DiaDiario(fecha, Integer.parseInt(spValoracion.getSelectedItem().toString()), etResumen.getText().toString(), etContenido.getText().toString());
                    Intent intent = getIntent();
                    intent.putExtra(EXTRA_DIA, diaDiario);
                    setResult(RESULT_OK, intent);
                }
                finish();
            }
        });


        fabImagen.setOnClickListener(view -> {
            ocultarTeclado();
            if (noNecesarioSolicitarPermisos()) {
                muestraOpcionesImagen();
            }
        });
    }

    // Crea el diálogo de calendario para escoger la fecha deseada.
    public void onClickFecha() {

        Calendar newCalendar = Calendar.getInstance();

        @SuppressLint("SimpleDateFormat") DatePickerDialog dialogoFecha = new DatePickerDialog(this, (view, anyo, mes, dia) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(anyo, mes, dia);
            fecha = calendar.getTime();
            EdicionDiaActivity.this.tvFecha.setText(new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime()));
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        dialogoFecha.show();
    }

    // Diálogo que se muestra cuando uno o más campos están incompletos.
    private void camposIncompletos() {
        AlertDialog.Builder dialogoCampos = new AlertDialog.Builder(this);
        dialogoCampos.setTitle(R.string.titulo_campos_incompletos);
        dialogoCampos.setMessage(R.string.cont_campos_incompletos);
        dialogoCampos.setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
        });
        dialogoCampos.show();
    }

    private void guardarDiaPreferencias(Date fecha) {
        //buscamos el fichero de preferencias
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);

        //lo abrimos en modo edición
        SharedPreferences.Editor editor = sharedPref.edit();

        //guardamos la fecha del día como entero
        editor.putLong(getString(R.string.pref_key_ultimo_dia), fecha.getTime());

        //finalizamos
        editor.apply();
    }
    // lanza una activity para elegir una imagen de la galería
    private void elegirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, getString(R.string.seleccione_imagen)), STATUS_CODE_SELECCION_IMAGEN);
    }

    // muestra la foto elegida en
    private void muestraFoto() {
        Glide.with(this).load(uriFoto).into(ivFotoDia);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case STATUS_CODE_SELECCION_IMAGEN:
                    uriFoto = data.getData();
                    muestraFoto();
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[]
            permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, R.string.permisos_aceptados, Toast.LENGTH_SHORT).show();
                muestraOpcionesImagen();
            } else {
                //si no se aceptan los permisos
                muestraExplicacionDenegacionPermisos();
            }
        }
    }

    private void muestraOpcionesImagen() {
        final CharSequence[] option = {getString(R.string.tomar_foto), getString(R.string.elegir_de_la_galería), getString(android.R.string.cancel)};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(android.R.string.dialog_alert_title);
        builder.setItems(option, (dialog, which) -> {
            switch (which) {
                case 0:
                    // abrirCamara(); // opcional
                    break;
                case 1:
                    elegirGaleria();
                    break;
            }
            dialog.dismiss();
        });
        builder.show();
    }

    private boolean noNecesarioSolicitarPermisos() {
        // si la versión es inferior a la 6
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;
        // comprobamos si tenemos los permisos
        if ((checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) && (checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED))
            return true;
        // indicamos al usuario porqué necesitamos los permisos siempre que no
        // haya indicado que no lo volvamos a hacer
        if ((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) || (shouldShowRequestPermissionRationale(CAMERA))) {
            Snackbar.make(clPrincipal, R.string.necesito_permisos, Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, v ->
                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS)).show();
        } else {
            //pedimos permisos sin indicar el porqué
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
        }
        //necesario pedir permisos
        return false;
    }

    /**
     * Si se deniegan los permisos mostramos las opciones de la aplicación
     * para que el usuario acepte los permisos
     */
    private void muestraExplicacionDenegacionPermisos() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.peticion_permisos);
        builder.setMessage(R.string.necesito_permisos);
        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
            Intent intent = new Intent();

            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
            dialog.dismiss();
            finish();
        });
        builder.show();
    }

    /**
     * Permite ocultar el teclado
     */
    private void ocultarTeclado() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        // mgr.showSoftInput(etDatos, InputMethodManager.HIDE_NOT_ALWAYS);
        if (imm != null) {
            imm.hideSoftInputFromWindow(etResumen.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(etContenido.getWindowToken(), 0);
        }
    }


    // Inicia los componentes de la activity.
    private void iniciaViews() {
        tvFecha = findViewById(R.id.tvFecha);
        ivFecha = findViewById(R.id.ivFecha);
        ivFotoDia = findViewById(R.id.ivFotoDia);
        etResumen = findViewById(R.id.etResumen);
        etContenido = findViewById(R.id.etContenido);
        spValoracion = findViewById(R.id.spValoracion);
        fabGuardar = findViewById(R.id.fabGuardar);
        fabImagen = findViewById(R.id.fabImagen);
        clPrincipal = findViewById(R.id.clPrincipal);
    }
}