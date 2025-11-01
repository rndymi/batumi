package es.upm.miw.bantumi.ui.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import es.upm.miw.bantumi.datos.models.Puntuacion;
import es.upm.miw.bantumi.datos.models.PuntuacionRepositorio;

public class PuntuacionViewModel extends AndroidViewModel {

    private PuntuacionRepositorio mRepository;

    private LiveData<List<Puntuacion>> mAllPuntuaciones;

    public PuntuacionViewModel(Application application) {
        super(application);
        mRepository = new PuntuacionRepositorio(application);
        mAllPuntuaciones = mRepository.getAllPuntuaciones();
    }

    public LiveData<List<Puntuacion>> getAllPuntuaciones() {
        return mAllPuntuaciones;
    }

    public void insert(Puntuacion puntuacion) {
        mRepository.insert(puntuacion);
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }

}
