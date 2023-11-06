package ma.ensa.volley.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.fido.fido2.api.common.RequestOptions;

import java.time.Instant;
import java.util.List;

import ma.ensa.volley.R;
import ma.ensa.volley.beans.Student;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StarViewHolder>{
    private static final String TAG = "StudentAdapter";

    private OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    private List<Student> students;
    private Context context;

    public StudentAdapter( Context context,List<Student> students) {
        this.students = students;
        this.context = context;
    }


    @NonNull
    @Override
    public StarViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(this.context).inflate(R.layout.item_s,
                viewGroup, false);
        return new StarViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StarViewHolder starViewHolder, int i) {

        starViewHolder.phone.setText(students.get(i).getPhone()+"");
        starViewHolder.email.setText(students.get(i).getEmail());
        starViewHolder.name.setText(students.get(i).getName());
        starViewHolder.id.setText(students.get(i).getId()+"");
        starViewHolder.parent.setOnClickListener(view -> {
            if (clickListener != null) {
                clickListener.onItemClick(view, i);
            }
        });



    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public class StarViewHolder extends RecyclerView.ViewHolder {

        TextView email,name,phone,id;
        private ConstraintLayout parent;


        public StarViewHolder(@NonNull View itemView) {
            super(itemView);
            email=itemView.findViewById(R.id.emailitem);
            id=itemView.findViewById(R.id.id);
            name=itemView.findViewById(R.id.nameitem);
            phone=itemView.findViewById(R.id.phoneitem);
            parent =itemView.findViewById(R.id.parent1);

        }
    }
}
