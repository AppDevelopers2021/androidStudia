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

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private ArrayList<Todo> arrayList;
    private Context context;
    private String subject;

    public CustomAdapter(ArrayList<Todo> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.content.setText(arrayList.get(position).getContent());
        holder.subject.setText(arrayList.get(position).getSubject());

        if (arrayList.get(position).getSubject().toString() == "가정") {
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.constraintLayout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.rectanglered) );
            } else {
                holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectanglered));
            }
        }
        if (arrayList.get(position).getSubject().toString() == "과학") {
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.constraintLayout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.rectangleblue) );
            } else {
                holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectangleblue));
            }
        }
        if (arrayList.get(position).getSubject().toString() == "국어") {
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.constraintLayout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.rectanglelightblue) );
            } else {
                holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectanglelightblue));
            }
        }
        if (arrayList.get(position).getSubject().toString() == "기술") {
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.constraintLayout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.rectanglegreen) );
            } else {
                holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectanglegreen));
            }
        }
        if (arrayList.get(position).getSubject().toString() == "도덕") {
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.constraintLayout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.rectanglepurple) );
            } else {
                holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectanglepurple));
            }
        }
        if (arrayList.get(position).getSubject().toString() == "독서") {
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.constraintLayout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.rectangleyellow) );
            } else {
                holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectangleyellow));
            }
        }
        if (arrayList.get(position).getSubject().toString() == "미술") {
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.constraintLayout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.rectanglelightgreen) );
            } else {
                holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectanglelightgreen));
            }
        }
        if (arrayList.get(position).getSubject().toString() == "보건") {
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.constraintLayout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.rectanglegray) );
            } else {
                holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectanglegray));
            }
        }
        if (arrayList.get(position).getSubject().toString() == "사회") {
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.constraintLayout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.rectangleblue) );
            } else {
                holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectangleblue));
            }
        }
        if (arrayList.get(position).getSubject().toString() == "수학") {
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.constraintLayout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.rectanglegreen) );
            } else {
                holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectanglegreen));
            }
        }
        if (arrayList.get(position).getSubject().toString() == "영어") {
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.constraintLayout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.rectangleyellow) );
            } else {
                holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectangleyellow));
            }
        }
        if (arrayList.get(position).getSubject().toString() == "음악") {
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.constraintLayout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.rectanglegreen) );
            } else {
                holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectanglegreen));
            }
        }
        if (arrayList.get(position).getSubject().toString() == "정보") {
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.constraintLayout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.rectanglered) );
            } else {
                holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectanglered));
            }
        }
        if (arrayList.get(position).getSubject().toString() == "진로") {
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.constraintLayout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.rectanglegray) );
            } else {
                holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectanglegray));
            }
        }
        if (arrayList.get(position).getSubject().toString() == "창체") {
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.constraintLayout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.rectanglepurple) );
            } else {
                holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectanglepurple));
            }
        }
        if (arrayList.get(position).getSubject().toString() == "체육") {
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.constraintLayout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.rectanglelightgreen) );
            } else {
                holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectanglelightgreen));
            }
        }
        if (arrayList.get(position).getSubject().toString() == "환경") {
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.constraintLayout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.rectanglelightblue) );
            } else {
                holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectanglelightblue));
            }
        }
        if (arrayList.get(position).getSubject().toString() == "자율") {
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.constraintLayout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.rectanglered) );
            } else {
                holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectanglered));
            }
        }
        if (arrayList.get(position).getSubject().toString() == "기타") {
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.constraintLayout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.rectanglegray) );
            } else {
                holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectanglegray));
            }
        }



        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //Delete
                remove(holder.getAdapterPosition());
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
            this.constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.layout);
        }
    }

    public void remove(int position) {
        try {
            arrayList.remove(position);
            notifyItemRemoved(position);
        }
        catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
    }
}
