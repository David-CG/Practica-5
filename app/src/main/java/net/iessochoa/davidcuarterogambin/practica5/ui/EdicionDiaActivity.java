package net.iessochoa.davidcuarterogambin.practica5.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.iessochoa.davidcuarterogambin.practica5.R;

import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.N)
public class EdicionDiaActivity extends AppCompatActivity {

    static final String EXTRA_DIA = "net.iessochoa.davidcuartero.practica5.EdicionDiaActivity.DiaCreado";

    TextView tvFecha;
    ImageView ivFecha;
    EditText etResumen, etDiario;
    Spinner spValoracion;
    FloatingActionButton fabGuardar;

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

        fabGuardar.setOnClickListener(view -> {
            //getIntent().putExtra(EXTRA_DIA, (Parcelable) dia);
        });
    }

    public void onClickFecha() {

        Calendar newCalendar = Calendar.getInstance();

        DatePickerDialog dialogo = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);

                EdicionDiaActivity.this.tvFecha.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
            }

        }, newCalendar.get(Calendar.YEAR),
                newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));

        dialogo.show();
    }

    private void iniciaViews() {
        tvFecha = findViewById(R.id.tvFecha);
        ivFecha = findViewById(R.id.ivFecha);
        etResumen = findViewById(R.id.etResumen);
        etDiario = findViewById(R.id.etDiario);
        spValoracion = findViewById(R.id.spValoracion);
        fabGuardar = findViewById(R.id.fabGuardar);
    }
}