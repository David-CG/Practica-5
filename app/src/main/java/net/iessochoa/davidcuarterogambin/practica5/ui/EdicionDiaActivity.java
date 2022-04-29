package net.iessochoa.davidcuarterogambin.practica5.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.iessochoa.davidcuarterogambin.practica5.R;
import net.iessochoa.davidcuarterogambin.practica5.model.DiaDiario;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.N)
public class EdicionDiaActivity extends AppCompatActivity {

    static final String EXTRA_DIA = "net.iessochoa.davidcuartero.practica5.EdicionDiaActivity.DiaCreado";

    TextView tvFecha;
    ImageView ivFecha;
    EditText etResumen, etContenido;
    Spinner spValoracion;
    FloatingActionButton fabGuardar;

    DiaDiario diaDiario = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edicion_dia);

        // Inicia las views de la activity
        iniciaViews();

        ivFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickFecha();
            }
        });

        // Spinner
        spValoracion.setAdapter(ArrayAdapter.createFromResource(this, R.array.spValoracion, android.R.layout.simple_spinner_dropdown_item));
        spValoracion.setSelection(5);

        //Comprueba si el día recibido no esta vacío
        diaDiario = getIntent().getParcelableExtra(EXTRA_DIA);
        if (diaDiario != null) {

            tvFecha.setText(diaDiario.getFechaFormatoLocal());
            etResumen.setText(diaDiario.getResumen());
            etContenido.setText(diaDiario.getContenido());
            spValoracion.setSelection(Arrays.asList(getResources().getStringArray(R.array.spValoracion)).indexOf(diaDiario.getValoracionDia()));
        }

        fabGuardar.setOnClickListener(view -> onClickGuardar());
    }

    public void onClickFecha() {

        Calendar newCalendar = Calendar.getInstance();

        DatePickerDialog dialogoFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onDateSet(DatePicker view, int anyo, int mes, int dia) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(anyo, mes, dia);

                EdicionDiaActivity.this.tvFecha.setText(new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR),
                newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));

        dialogoFecha.show();
    }

    public View.OnClickListener onClickGuardar() {
        return new View.OnClickListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onClick(View view) {
                etResumen.setText(etResumen.getText().toString().trim());
                etContenido.setText(etContenido.getText().toString().trim());
                if ((etResumen.getText().toString().equals("")) || (etContenido.getText().toString().equals(""))) {
                    camposIncompletos();
                } else {
                    Intent resultado = new Intent();
                    Date fecha = null;
                    try {
                        fecha = new SimpleDateFormat("dd/MM/yyyy").parse(tvFecha.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    resultado.putExtra(EXTRA_DIA, new DiaDiario(fecha, Integer.parseInt((String) spValoracion.getSelectedItem()), etResumen.getText().toString(), etContenido.getText().toString()));
                    setResult(RESULT_OK, resultado);
                    finish();
                }
            }
        };
    }

    private void camposIncompletos() {
        AlertDialog.Builder dialogoCampos = new AlertDialog.Builder(this);
        dialogoCampos.setTitle(R.string.titulo_campos_incompletos);
        dialogoCampos.setMessage(R.string.cont_campos_incompletos);
        dialogoCampos.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        dialogoCampos.show();
    }

    // Inicia los componentes de la activity
    private void iniciaViews() {
        tvFecha = findViewById(R.id.tvFecha);
        ivFecha = findViewById(R.id.ivFecha);
        etResumen = findViewById(R.id.etResumen);
        etContenido = findViewById(R.id.etContenido);
        spValoracion = findViewById(R.id.spValoracion);
        fabGuardar = findViewById(R.id.fabGuardar);
    }
}