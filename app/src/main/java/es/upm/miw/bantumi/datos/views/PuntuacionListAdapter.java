package es.upm.miw.bantumi.datos.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.upm.miw.bantumi.R;
import es.upm.miw.bantumi.datos.models.Puntuacion;

public class PuntuacionListAdapter extends RecyclerView.Adapter<PuntuacionListAdapter.PuntuacionViewHolder>{

    class PuntuacionViewHolder extends RecyclerView.ViewHolder {
        private final TextView jugadorItemView;
        private final TextView semillasItemView;
        private final TextView fechaItemView;

        private PuntuacionViewHolder(View itemView) {
            super(itemView);
            jugadorItemView = itemView.findViewById(R.id.textJugador);
            semillasItemView = itemView.findViewById(R.id.textSemillas);
            fechaItemView = itemView.findViewById(R.id.textFecha);
        }
    }

    private final LayoutInflater mInflater;
    private List<Puntuacion> mPuntuaciones;

    public PuntuacionListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PuntuacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item_resultados, parent, false);
        return new PuntuacionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PuntuacionViewHolder holder, int position) {
        if (mPuntuaciones != null) {
            Puntuacion actual = mPuntuaciones.get(position);
            holder.jugadorItemView.setText(actual.getNombreJugador());
            holder.semillasItemView.setText("J1: " + actual.getSemillasJugador1() + " | J2: " + actual.getSemillasJugador2());
            holder.fechaItemView.setText(actual.getFechaHora());
        }
    }

    public void setPuntuaciones(List<Puntuacion> puntuaciones) {
        mPuntuaciones = puntuaciones;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (mPuntuaciones == null) ? 0 : mPuntuaciones.size();
    }

}
