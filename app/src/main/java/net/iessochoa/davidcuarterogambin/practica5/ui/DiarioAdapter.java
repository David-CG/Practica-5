package net.iessochoa.davidcuarterogambin.practica5.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

    @Override
    public void onBindViewHolder(@NonNull DiarioViewHolder holder, int position) {
        if (listaDiario != null){
            final DiaDiario diario = listaDiario.get(position);
            int valor = diario.getValoracionResumida();
            holder.tvResumen.setText(diario.getResumen());
            holder.tvFecha.setText(diario.getFechaFormatoLocal());
            switch (valor){
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
        }else {
            holder.tvResumen.setText("Vacío");
            holder.tvFecha.setText("Vacío");
            holder.ivImagen.setImageResource(R.drawable.neutral);
        }
    }

    @Override
    public int getItemCount() {
        if (listaDiario != null) {
            return  listaDiario.size();
        } else {
            return 0;
        }
    }

    public class DiarioViewHolder extends RecyclerView.ViewHolder {

        private TextView tvResumen;
        private TextView tvFecha;
        private ImageView ivImagen;
        private Button btBorrar;

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
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listenerItem != null)
                        listenerItem.onItemClickItem(listaDiario.get(DiarioViewHolder.this.getAbsoluteAdapterPosition()));
                }
            });
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
