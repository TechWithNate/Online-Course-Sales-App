package com.tech.coursesales;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    private Context context;
    private ArrayList<CourseModel> models;
    int lasPos = -1;
    private CourseClickInterface courseClickInterface;

    public CourseAdapter(Context context, ArrayList<CourseModel> models, CourseClickInterface courseClickInterface) {
        this.context = context;
        this.models = models;
        this.courseClickInterface = courseClickInterface;
    }

    @NonNull
    @Override
    public CourseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        CourseModel model = models.get(position);
        holder.courseName.setText(model.getCourseName());
        holder.coursePrice.setText("GHC. "+model.getCoursePrice());
        Glide.with(context)
                .load(model.getCourseImage())
                .into(holder.courseImageHolder);

        setAnimation(holder.itemView, position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseClickInterface.onCourseClick(position);
            }
        });



    }

    private void setAnimation(View itemView, int position){
        if (position > lasPos){
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            itemView.setAnimation(animation);
            lasPos = position;
        }
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView courseImageHolder;
        private TextView courseName;
        private TextView coursePrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            courseImageHolder = itemView.findViewById(R.id.courseImageHolder);
            courseName = itemView.findViewById(R.id.courseName);
            coursePrice = itemView.findViewById(R.id.coursePrice);

        }
    }

    public interface CourseClickInterface{
        void onCourseClick(int position);
    }

}
