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
import ma.ensa.volley.beans.Role;

public class RoleAdpater extends RecyclerView.Adapter<RoleAdpater.RoleViewHolder> {


    private static final String TAG = "RoleAdapter";

    private OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    private Context context;
    private List<Role> roles;

    public RoleAdpater(Context context, List<Role> roles) {
        this.context = context;
        this.roles = roles;
    }

    @NonNull
    @Override
    public RoleAdpater.RoleViewHolder onCreateViewHolder(@NonNull ViewGroup  viewGroup, int viewType) {
        View v = LayoutInflater.from(this.context).inflate(R.layout.item_role,
                viewGroup, false);
        return new RoleAdpater.RoleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RoleAdpater.RoleViewHolder holder, int i) {
        holder.name.setText(roles.get(i).getName());
        holder.id.setText(roles.get(i).getId()+"");
        holder.parent.setOnClickListener(view -> {
            if (clickListener != null) {
                clickListener.onItemClick(view, i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return roles.size();
    }

    public class RoleViewHolder extends RecyclerView.ViewHolder {

        TextView name,id;
        private ConstraintLayout parent;

        public RoleViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.itemname1);
            id=itemView.findViewById(R.id.id1);
            parent =itemView.findViewById(R.id.parent2);

        }
    }
}

