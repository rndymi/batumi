package es.upm.miw.bantumi.datos.models;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PuntuacionDAO {

    @Query("SELECT * FROM puntuaciones ORDER BY semillasJugador1 DESC LIMIT 10")
    List<Puntuacion> getTop10();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Puntuacion puntuacion);

}
