package es.upm.miw.bantumi.ui.actividades;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.upm.miw.bantumi.datos.models.Puntuacion;
import es.upm.miw.bantumi.datos.models.PuntuacionRepositorio;
import es.upm.miw.bantumi.ui.fragmentos.FinalAlertDialog;
import es.upm.miw.bantumi.R;
import es.upm.miw.bantumi.dominio.logica.JuegoBantumi;
import es.upm.miw.bantumi.ui.viewmodel.BantumiViewModel;

public class MainActivity extends AppCompatActivity {

    protected final String LOG_TAG = "MiW";
    public JuegoBantumi juegoBantumi;
    private BantumiViewModel bantumiVM;
    int numInicialSemillas;

    /** Variable de Reiniciar
    */
    private boolean partidaEnCurso = false;

    /** Variable de Ajustes
     * */
    private SharedPreferences preferencias;

    /** Variables instaciadas
     * */

    PuntuacionRepositorio puntuacionRepositorio;
    LiveData<List<Puntuacion>> puntuaciones;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Instancia el ViewModel y el juego, y asigna observadores a los huecos
        numInicialSemillas = getResources().getInteger(R.integer.intNumInicialSemillas);
        bantumiVM = new ViewModelProvider(this).get(BantumiViewModel.class);
        juegoBantumi = new JuegoBantumi(bantumiVM, JuegoBantumi.Turno.turnoJ1, numInicialSemillas);

        preferencias = PreferenceManager.getDefaultSharedPreferences(this);

        puntuacionRepositorio = new PuntuacionRepositorio(getApplication());
        puntuaciones = puntuacionRepositorio.getAllPuntuaciones();

        crearObservadores();
    }

    /**
     * Crea y subscribe los observadores asignados a las posiciones del tablero.
     * Si se modifica el contenido del tablero -> se actualiza la vista.
     */
    private void crearObservadores() {
        for (int i = 0; i < JuegoBantumi.NUM_POSICIONES; i++) {
            int finalI = i;
            bantumiVM.getNumSemillas(i).observe(    // Huecos y almacenes
                    this,
                    new Observer<Integer>() {
                        @Override
                        public void onChanged(Integer integer) {
                            mostrarValor(finalI, juegoBantumi.getSemillas(finalI));
                        }
                    });
        }
        bantumiVM.getTurno().observe(   // Turno
                this,
                new Observer<JuegoBantumi.Turno>() {
                    @Override
                    public void onChanged(JuegoBantumi.Turno turno) {
                        marcarTurno(juegoBantumi.turnoActual());
                    }
                }
        );
    }

    /**
     * Indica el turno actual cambiando el color del texto
     *
     * @param turnoActual turno actual
     */
    private void marcarTurno(@NonNull JuegoBantumi.Turno turnoActual) {
        TextView tvJugador1 = findViewById(R.id.tvPlayer1);
        TextView tvJugador2 = findViewById(R.id.tvPlayer2);
        switch (turnoActual) {
            case turnoJ1:
                tvJugador1.setTextColor(getColor(R.color.white));
                tvJugador1.setBackgroundColor(getColor(android.R.color.holo_blue_light));
                tvJugador2.setTextColor(getColor(R.color.black));
                tvJugador2.setBackgroundColor(getColor(R.color.white));
                break;
            case turnoJ2:
                tvJugador1.setTextColor(getColor(R.color.black));
                tvJugador1.setBackgroundColor(getColor(R.color.white));
                tvJugador2.setTextColor(getColor(R.color.white));
                tvJugador2.setBackgroundColor(getColor(android.R.color.holo_blue_light));
                break;
            default:
                tvJugador1.setTextColor(getColor(R.color.black));
                tvJugador2.setTextColor(getColor(R.color.black));
        }
    }

    /**
     * Muestra el valor <i>valor</i> en la posición <i>pos</i>
     *
     * @param pos posición a actualizar
     * @param valor valor a mostrar
     */
    private void mostrarValor(int pos, int valor) {
        String num2digitos = String.format(Locale.getDefault(), "%02d", pos);
        // Los identificadores de los huecos tienen el formato casilla_XX
        int idBoton = getResources().getIdentifier("casilla_" + num2digitos, "id", getPackageName());
        if (0 != idBoton) {
            TextView viewHueco = findViewById(idBoton);
            viewHueco.setText(String.valueOf(valor));
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.opciones_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.opcAcercaDe:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.aboutTitle)
                        .setMessage(R.string.aboutMessage)
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
                return true;

            // @TODO!!! resto opciones

            case R.id.opcReiniciarPartida:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.txtReiniciarPartida)
                        .setMessage(R.string.resetGameMsg)
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            juegoBantumi.inicializar(JuegoBantumi.Turno.turnoJ1);
                            partidaEnCurso = false;
                            invalidateOptionsMenu();
                            Toast.makeText(this, R.string.resetGameConfirm, Toast.LENGTH_SHORT).show();
                            Log.i(LOG_TAG, "* Partida Reiniciada");
                            Log.i(LOG_TAG, "-------------------------------------------------------");
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
                return true;

            case R.id.opcGuardarPartida:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.txtOpcionGuardar)
                        .setMessage(R.string.saveGameMsg)
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {

                            String datos = juegoBantumi.serializa();
                            guardarEnFichero(datos);
                            Toast.makeText(this, R.string.saveGameConfirm, Toast.LENGTH_SHORT).show();
                            Log.i(LOG_TAG, "* Partida guardada");
                            Log.i(LOG_TAG, "-------------------------------------------------------");

                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
                return true;

            case R.id.opcRecuperarPartida:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.txtOpcionRecuperar)
                        .setMessage(R.string.loadGameMsg)
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {

                            String datos = recuperarDeFichero();

                            if (datos == null || datos.isEmpty()) {
                                Log.w(LOG_TAG, "No se encontró partida guardada");
                                return;
                            }

                            juegoBantumi.deserializa(datos);
                            Toast.makeText(this, R.string.loadGameConfirm, Toast.LENGTH_SHORT).show();
                            Log.i(LOG_TAG, "* Partida recuperada");
                            Log.i(LOG_TAG, "-------------------------------------------------------");

                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
                return true;

            case R.id.opcMejoresResultados:
                Log.i(LOG_TAG, "opción MEJORES RESULTADOS");
                Intent abrir = new Intent(this, MejoresResultadosActivity.class);
                startActivity(abrir);
                break;

            case R.id.opcAjustes:
                Log.i(LOG_TAG, "opción AJUSTES");
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;

            default:
                Snackbar.make(
                        findViewById(android.R.id.content),
                        getString(R.string.txtSinImplementar),
                        Snackbar.LENGTH_LONG
                ).show();
        }
        return true;
    }

    /**
     * Acción que se ejecuta al pulsar sobre cualquier hueco
     *
     * @param v Vista pulsada (hueco)
     */
    public void huecoPulsado(@NonNull View v) {
        String resourceName = getResources().getResourceEntryName(v.getId()); // pXY
        int num = Integer.parseInt(resourceName.substring(resourceName.length() - 2));
        Log.i(LOG_TAG, "huecoPulsado(" + resourceName + ") num=" + num);

        /** Detecta el movimiento del jugador para habilitar el item reiniciar
         * */
        if (!partidaEnCurso && juegoBantumi.turnoActual() == JuegoBantumi.Turno.turnoJ1 && num >= 0 && num <= 5) {
            if (juegoBantumi.getSemillas(num) > 0) {
                partidaEnCurso = true;
                invalidateOptionsMenu();
                Log.i(LOG_TAG, "* La partida ha comenzado");
                Log.i(LOG_TAG, "-------------------------------------------------------");
            }
        }

        switch (juegoBantumi.turnoActual()) {
            case turnoJ1:
                Log.i(LOG_TAG, "* Juega Jugador");
                juegoBantumi.jugar(num);
                break;
            case turnoJ2:
                Log.i(LOG_TAG, "* Juega Computador");
                juegoBantumi.juegaComputador();
                break;
            default:    // JUEGO TERMINADO
                finJuego();
        }
        if (juegoBantumi.juegoTerminado()) {
            finJuego();
        }
    }

    /**
     * El juego ha terminado. Volver a jugar?
     */
    private void finJuego() {
        String texto = (juegoBantumi.getSemillas(6) > 6 * numInicialSemillas)
                ? "Gana Jugador 1"
                : "Gana Jugador 2";
        if (juegoBantumi.getSemillas(6) == 6 * numInicialSemillas) {
            texto = "¡¡¡ EMPATE !!!";
        }

        // @TODO guardar puntuación
        Puntuacion puntuacion = new Puntuacion("Jugador 1", new Date().toString(), juegoBantumi.getSemillas(6), juegoBantumi.getSemillas(13));
        puntuacionRepositorio.insert(puntuacion);

        Log.i(LOG_TAG, "[ Puntuación guardada ] Jugador 1: " + juegoBantumi.getSemillas(6) + " - Jugador 2: " + juegoBantumi.getSemillas(13));

        // terminar
        new FinalAlertDialog(texto).show(getSupportFragmentManager(), "ALERT_DIALOG");
    }

    /** Metodos de Reiniciar
     * */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem itemReiniciar = menu.findItem(R.id.opcReiniciarPartida);
        MenuItem itemGuardar   = menu.findItem(R.id.opcGuardarPartida);
        itemReiniciar.setEnabled(partidaEnCurso);
        itemGuardar.setEnabled(partidaEnCurso);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("partidaEnCurso", partidaEnCurso);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        partidaEnCurso = savedInstanceState.getBoolean("partidaEnCurso", false);
    }

    /** Metodos de Ajustes
     * */
    private String obtenerNombreFichero() {
        String nombreFichero = preferencias.getString(
                getString(R.string.key_NombreFichero),
                getString(R.string.default_NombreFichero)
        );
        Log.i(LOG_TAG, "Nombre fichero: " + nombreFichero);

        return nombreFichero;
    }

    private boolean utilizarMemInterna() {
        boolean utilizarMemInterna = !preferencias.getBoolean(
                getString(R.string.key_TarjetaSD),
                getResources().getBoolean(R.bool.default_prefTarjetaSD)
        );
        Log.i(LOG_TAG, "Memoria SD: " + ((!utilizarMemInterna) ? "ON" : "OFF"));

        return utilizarMemInterna;
    }

    /** Metodos de Guardar
    * */

    public void guardarEnFichero(String contenido){

        FileOutputStream fos;

        try {
            if (utilizarMemInterna()) {
                fos = openFileOutput(obtenerNombreFichero(), Context.MODE_PRIVATE);
                Log.i(LOG_TAG, "Partida guardada en memoria interna");
            } else {
                String estadoTarjetaSD = Environment.getExternalStorageState();
                if (estadoTarjetaSD.equals(Environment.MEDIA_MOUNTED)) {
                    String rutaFich = getExternalFilesDir(null) + "/" + obtenerNombreFichero();
                    fos = new FileOutputStream(rutaFich, false);
                    Log.i(LOG_TAG, "Partida guardada en tarjeta SD");
                } else {
                    Toast.makeText(
                            this,
                            getString(R.string.txtErrorMemExterna),
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }
            }
            fos.write((contenido).getBytes());
            fos.close();
            Log.i(LOG_TAG, "Partida guardada en fichero correctamente");
        } catch (Exception e) {
            Log.e(LOG_TAG, "FILE I/O ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /** Metodo de Recuperar
     * */

    public String recuperarDeFichero() {

        boolean hayContenido = false;
        BufferedReader fin;
        StringBuilder contenido = new StringBuilder();

        try {
            if (utilizarMemInterna()) {
                fin = new BufferedReader(
                        new InputStreamReader(openFileInput(obtenerNombreFichero())));
            } else {
                String estadoTarjetaSD = Environment.getExternalStorageState();
                if (estadoTarjetaSD.equals(Environment.MEDIA_MOUNTED)) {
                    String rutaFich = getExternalFilesDir(null) + "/" + obtenerNombreFichero();
                    Log.i(LOG_TAG, "rutaSD=" + rutaFich);
                    fin = new BufferedReader(new FileReader(new File(rutaFich)));
                } else {
                    Log.i(LOG_TAG, "Estado SDcard=" + estadoTarjetaSD);
                    Toast.makeText(this, getString(R.string.txtErrorMemExterna), Toast.LENGTH_SHORT).show();
                    return "";
                }
            }

            String linea = fin.readLine();
            while (linea != null) {
                hayContenido = true;
                contenido.append(linea);
                linea = fin.readLine();
            }
            fin.close();
            Log.i(LOG_TAG, "Fichero leído correctamente");

        } catch (Exception e) {
            Log.e(LOG_TAG, "FILE I/O ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        if (!hayContenido) {
            Toast.makeText(this, R.string.emptyFichero, Toast.LENGTH_SHORT).show();
        }

        return contenido.toString();

    }

}