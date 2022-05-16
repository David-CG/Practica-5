package net.iessochoa.davidcuarterogambin.practica5.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.iessochoa.davidcuarterogambin.practica5.R;
import net.iessochoa.davidcuarterogambin.practica5.model.DiaDiario;

import java.util.List;

public class DiarioAdapter extends RecyclerView.Adapter<DiarioAdapter.DiarioViewHolder> {

    private List<DiaDiario> listaDiario;
    private OnItemClickBorrarListener listenerBorrar;
    private OnItemClickItemListener listenerItem;

    @SuppressLint("NotifyDataSetChanged")
    public void setListaTareas(List<DiaDiario> diaDiarios) {
        listaDiario = diaDiarios;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DiarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dia, parent, false);
        return new DiarioViewHolder(itemView);
    }

    // asigna los image views de humor a la puntuación del día
    @Override
    public void onBindViewHolder(@NonNull DiarioViewHolder holder, int position) {
        if (listaDiario != null) {
            final DiaDiario diario = listaDiario.get(position);
            int valor = diario.getValoracionResumida();
            holder.tvResumen.setText(diario.getResumen());
            //holder.tvFecha.setText(diario.getFechaFormatoLocal()); // hace que la app de error y se cierre
            switch (valor) {
                case 1:
                    holder.ivImagen.setImageResource(R.drawable.sad);
                    break;
                case 2:
                    holder.ivImagen.setImageResource(R.drawable.neutral);
                    break;
                case 3:
                    holder.ivImagen.setImageResource(R.drawable.smile);
                    break;
            }
        }
    }

    // cantidad de dias en la lista
    @Override
    public int getItemCount() {
        if (listaDiario != null) {
            return listaDiario.size();
        } else {
            return 0;
        }
    }

    // asigna las acciones de borrar y editar al hacer click en el item o el botón de eliminar
    public class DiarioViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvResumen;
        private final TextView tvFecha;
        private final ImageView ivImagen;
        private final Button btBorrar;

        public DiarioViewHolder(@NonNull View itemView) {
            super(itemView);
            tvResumen = itemView.findViewById(R.id.tvResumenDiaItem);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            ivImagen = itemView.findViewById(R.id.imageView);
            btBorrar = itemView.findViewById(R.id.btBorrar);

            btBorrar.setOnClickListener(view -> {
                if (listenerBorrar != null) {
                    listenerBorrar.onItemClickBorrar(listaDiario.get(DiarioViewHolder.this.getAbsoluteAdapterPosition()));
                }
            });
            itemView.setOnClickListener(view -> {
                if (listenerItem != null)
                    listenerItem.onItemClickItem(listaDiario.get(DiarioViewHolder.this.getAbsoluteAdapterPosition()));
            });
        }

        public DiaDiario getDia() {
            return listaDiario.get(DiarioViewHolder.this.getBindingAdapterPosition());
        }
    }

    public interface OnItemClickBorrarListener {
        void onItemClickBorrar(DiaDiario diaDiario);
    }

    public interface OnItemClickItemListener {
        void onItemClickItem(DiaDiario diaDiario);
    }

    public void setOnClickBorrarListener(OnItemClickBorrarListener listener) {
        this.listenerBorrar = listener;
    }

    public void setOnClickItemListener(OnItemClickItemListener listener) {
        this.listenerItem = listener;
    }
}
