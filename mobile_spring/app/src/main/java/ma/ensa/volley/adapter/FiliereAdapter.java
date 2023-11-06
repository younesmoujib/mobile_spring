package ma.ensa.volley.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ma.ensa.volley.R;
import ma.ensa.volley.beans.Filiere;

public class FiliereAdapter extends RecyclerView.Adapter<FiliereAdapter.ViewHolder> {


    private Context context;
    private List<Filiere> filieres;
    private static final String TAG = "FiliereAdapter";
    private OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public FiliereAdapter(Context context, List<Filiere> filieres) {
        this.context = context;
        this.filieres = filieres;
    }

    @NonNull
    @Override
    public FiliereAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(this.context).inflate(R.layout.item_filiere,
                viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FiliereAdapter.ViewHolder holder, int i) {
        holder.code.setText(filieres.get(i).getCode());
        holder.libelle.setText(filieres.get(i).getLibelle());
        holder.id.setText(filieres.get(i).getId()+"");
        holder.parent.setOnClickListener(view -> {
            if (clickListener != null) {
                clickListener.onItemClick(view, i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return filieres.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView code,id,libelle;
        private ConstraintLayout parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            id=itemView.findViewById(R.id.id3);
            code=itemView.findViewById(R.id.codeitem);
            libelle=itemView.findViewById(R.id.libelleitem);
            parent =itemView.findViewById(R.id.parent3);
        }
    }
}
