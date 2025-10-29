package es.upm.miw.bantumi.datos.models;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Puntuacion.class}, version = 1)
public abstract class PuntuacionRepositorio extends RoomDatabase {

    public static final String BASE_DATOS = Puntuacion.TABLA + ".db";

    public abstract PuntuacionDAO puntuacionDAO();

}
