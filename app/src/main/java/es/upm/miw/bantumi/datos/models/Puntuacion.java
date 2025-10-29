package es.upm.miw.bantumi.datos.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = Puntuacion.TABLA)
public class Puntuacion {

    static public final String TABLA = "puntuaciones";

    @PrimaryKey(autoGenerate = true)
    protected int uid;

    @NonNull
    protected String nombreJugador;
    protected String fechaHora;
    protected int semillasJugador1;
    protected int semillasJugador2;

    public Puntuacion(@NonNull String nombreJugador, String fechaHora, int semillasJugador1, int semillasJugador2) {
        this.nombreJugador = nombreJugador;
        this.fechaHora = fechaHora;
        this.semillasJugador1 = semillasJugador1;
        this.semillasJugador2 = semillasJugador2;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    @NonNull
    public String getNombreJugador() {
        return nombreJugador;
    }

    public void setNombreJugador(@NonNull String nombreJugador) {
        this.nombreJugador = nombreJugador;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public int getSemillasJugador1() {
        return semillasJugador1;
    }

    public void setSemillasJugador1(int semillasJugador1) {
        this.semillasJugador1 = semillasJugador1;
    }

    public int getSemillasJugador2() {
        return semillasJugador2;
    }

    public void setSemillasJugador2(int semillasJugador2) {
        this.semillasJugador2 = semillasJugador2;
    }

}
