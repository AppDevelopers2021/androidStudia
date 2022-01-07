package app.web.studia_kr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private ArrayList<Todo> arrayList;
    private Context context;

    public CustomAdapter(ArrayList<Todo> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        context = parent.getContext();

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

        holder.content.setText(arrayList.get(position).getContent());
        holder.subject.setText(arrayList.get(position).getSubject());

        if (arrayList.get(position).getSubject().equals("가정"))
            holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectangleblue));
        if (arrayList.get(position).getSubject().equals("과학"))
            holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectanglegray));
        if (arrayList.get(position).getSubject().equals("국어"))
            holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectanglegreen));
        if (arrayList.get(position).getSubject().equals("기술"))
            holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectanglelightblue));
        if (arrayList.get(position).getSubject().equals("도덕"))
            holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectanglelightgreen));
        if (arrayList.get(position).getSubject().equals("독서"))
            holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectanglepurple));
        if (arrayList.get(position).getSubject().equals("미술"))
            holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectanglered));
        if (arrayList.get(position).getSubject().equals("보건"))
            holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectangleyellow));
        if (arrayList.get(position).getSubject().equals("사회"))
            holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectangleblue));
        if (arrayList.get(position).getSubject().equals("수학"))
            holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectanglegray));
        if (arrayList.get(position).getSubject().equals("영어"))
            holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectanglegreen));
        if (arrayList.get(position).getSubject().equals("음악"))
            holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectanglelightblue));
        if (arrayList.get(position).getSubject().equals("정보"))
            holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectanglelightgreen));
        if (arrayList.get(position).getSubject().equals("진로"))
            holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectanglepurple));
        if (arrayList.get(position).getSubject().equals("창체"))
            holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectanglered));
        if (arrayList.get(position).getSubject().equals("체육"))
            holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectangleyellow));
        if (arrayList.get(position).getSubject().equals("환경"))
            holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectangleblue));
        if (arrayList.get(position).getSubject().equals("자율"))
            holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectanglegray));
        if (arrayList.get(position).getSubject().equals("기타"))
            holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectanglegreen));

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //Delete holder
                int pos = holder.getAdapterPosition();

                try{
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    database.getReference().child("calendar").child(arrayList.get(pos).getUid())
                            .child(arrayList.get(pos).getDate()).child("note")
                            .child(arrayList.get(pos).getNumber()).removeValue();

                    arrayList.remove(pos);
                    notifyItemRemoved(pos);
                }
                catch(IndexOutOfBoundsException e){
                    e.printStackTrace();
                }

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView subject;
        TextView content;
        ConstraintLayout constraintLayout;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            this.subject = itemView.findViewById(R.id.tvSubject);
            this.content = itemView.findViewById(R.id.tvContent);
            this.constraintLayout = itemView.findViewById(R.id.layout);
        }
    }
}
