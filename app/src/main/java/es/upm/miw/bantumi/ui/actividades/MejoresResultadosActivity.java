package es.upm.miw.bantumi.ui.actividades;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import es.upm.miw.bantumi.R;
import es.upm.miw.bantumi.datos.models.Puntuacion;
import es.upm.miw.bantumi.datos.models.PuntuacionRepositorio;
import es.upm.miw.bantumi.datos.views.PuntuacionListAdapter;

public class MejoresResultadosActivity extends AppCompatActivity {

    protected final String LOG_TAG = "MiW";

    PuntuacionRepositorio puntuacionRepositorio;
    List<Puntuacion> puntuaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mejoresresultados);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerViewResultados);
        final PuntuacionListAdapter adapter = new PuntuacionListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setPuntuaciones(puntuaciones);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_mejoresresultados, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.opcEliminarResultados:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.txtEliminarResultado)
                        .setMessage(R.string.deleteResultsMsg)
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {

                            Toast.makeText(this, R.string.deleteResultsConfirm, Toast.LENGTH_SHORT).show();
                            Log.i(LOG_TAG, "* Resultados Eliminados");
                            Log.i(LOG_TAG, "-------------------------------------------------------");
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
                return true;

                default:
                Snackbar.make(
                        findViewById(android.R.id.content),
                        getString(R.string.txtSinImplementar),
                        Snackbar.LENGTH_LONG
                ).show();
        }
        return true;
    }

}
