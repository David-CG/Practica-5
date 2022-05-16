package net.iessochoa.davidcuarterogambin.practica5.model;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {DiaDiario.class}, version = 1)

@TypeConverters({TransformaFechaSQLite.class})
public abstract class DiarioDatabase extends RoomDatabase {
    public abstract DiarioDao diarioDao();

    private static volatile DiarioDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static DiarioDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DiarioDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            DiarioDatabase.class, "diario_database")
                            .addCallback(sRoomDatabaseCallback).build();
                }
            }
        }
        return INSTANCE;
    }

    // Inserta datos de prueba al iniciar la app por primera vez

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                DiarioDao diarioDao = INSTANCE.diarioDao();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                DiaDiario diaDiario;

                try {
                    // 1 ************************************************************************
                    diaDiario = new DiaDiario(simpleDateFormat.parse("1-2-2022"),
                            1,
                            "Último mes de clase, agobio y cansancio.",
                            "Mucha carga de trabajo con examenes y prácticas.");
                    diarioDao.insert(diaDiario);
                    // 2 ************************************************************************
                    diaDiario = new DiaDiario(simpleDateFormat.parse("7-2-2022"),
                            3,
                            "Mal día, dolor de cabeza.",
                            "Me tuve que ir de clase y me tomé un paracetamol, después me puse a trabajar en PMDM");
                    diarioDao.insert(diaDiario);
                    // 3 ************************************************************************
                    diaDiario = new DiaDiario(simpleDateFormat.parse("1-1-2022"),
                            10,
                            "Día divertido, reunión con mis amigos.",
                            "Nos juntamos para cenar y ver las campanadas juntos.");
                    diarioDao.insert(diaDiario);
                    // 4 ************************************************************************
                    diaDiario = new DiaDiario(simpleDateFormat.parse("25-12-2021"),
                            6,
                            "Navidad",
                            "Un día normal como todos");
                    diarioDao.insert(diaDiario);
                    // 5 ************************************************************************
                    diaDiario = new DiaDiario(simpleDateFormat.parse("13-9-2021"),
                            8,
                            "Comienzo del curso",
                            "A ver si me aplico y lo puedo sacar.");
                    diarioDao.insert(diaDiario);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            });
        }
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };
}
