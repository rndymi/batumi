package es.upm.miw.bantumi.datos.models;

import android.app.Application;

import java.util.List;

public class PuntuacionRepositorio {

    private PuntuacionDAO mPuntuacionDAO;

    private List<Puntuacion> mPuntuaciones;

    public PuntuacionRepositorio(Application application) {
        PuntuacionRoomDatabase db = PuntuacionRoomDatabase.getDatabase(application);
        mPuntuacionDAO = db.puntuacionDAO();
        mPuntuaciones = mPuntuacionDAO.getAll();
    }

    public List<Puntuacion> getAllPuntuaciones() {
        return mPuntuaciones;
    }

    public void insert(Puntuacion puntuacion) {
        mPuntuacionDAO.insert(puntuacion);
    }

}
