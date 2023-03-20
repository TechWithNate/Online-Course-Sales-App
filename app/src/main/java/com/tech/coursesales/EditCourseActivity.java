package com.tech.coursesales;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EditCourseActivity extends AppCompatActivity {


    private EditText courseName;
    private EditText coursePrice;
    private EditText courseSuit;
    private EditText courseImage;
    private EditText courseLink;
    private EditText courseDesc;
    private MaterialButton updateCourseBtn;
    private MaterialButton deleteCourseBtn;

    private String courseID;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    CourseModel courseModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);
        initViews();

       courseModel = getIntent().getParcelableExtra("courses");
        if (null != courseModel) {
            courseName.setText(courseModel.getCourseName());
            coursePrice.setText(courseModel.getCoursePrice());
            courseSuit.setText(courseModel.getCourseSuit());
            courseImage.setText(courseModel.getCourseImage());
            courseLink.setText(courseModel.getCourseLink());
            courseDesc.setText(courseModel.getCourseDesc());
            courseID = courseModel.getCourseName();
            Log.e("Course::: ", courseModel.toString());
        }else {
            Toast.makeText(this, "Course Model Is Empty", Toast.LENGTH_SHORT).show();
            Log.e("Course::: ", courseModel.toString());
        }

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Courses").child(courseID);

        updateCourseBtn.setOnClickListener(v -> {
            String mCourseName = courseName.getText().toString();
            String mCoursePrice = coursePrice.getText().toString();
            String mCourseSuit = courseSuit.getText().toString();
            String mCourseImage = courseImage.getText().toString();
            String mCourseLink = courseLink.getText().toString();
            String mCourseDesc = courseDesc.getText().toString();

           Map<String, Object> map = new HashMap<>();
           map.put("courseID", courseID);
           map.put("courseName", mCourseName);
           map.put("coursePrice", mCoursePrice);
           map.put("courseSuit", mCourseSuit);
           map.put("courseImage", mCourseImage);
           map.put("courseLink", mCourseLink);
           map.put("courseDesc", mCourseDesc);
           databaseReference.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {
                   databaseReference.updateChildren(map);
                   Toast.makeText(EditCourseActivity.this, "Course Updated", Toast.LENGTH_SHORT).show();
                   startActivity(new Intent(EditCourseActivity.this, MainActivity.class));
               }

               @Override
               public void onCancelled(@NonNull DatabaseError error) {
                   Toast.makeText(EditCourseActivity.this, "Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
               }
           });
        });


        deleteCourseBtn.setOnClickListener(v -> deleteCourse());

    }

    private void deleteCourse(){
        databaseReference.removeValue();
        Toast.makeText(this, "Course Deleted", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(EditCourseActivity.this, MainActivity.class));
    }

    private void initViews(){
        courseName = findViewById(R.id.courseName);
        coursePrice = findViewById(R.id.coursePrice);
        courseSuit = findViewById(R.id.courseSuit);
        courseImage = findViewById(R.id.courseImage);
        courseLink = findViewById(R.id.courseLink);
        courseDesc = findViewById(R.id.courseDescription);
        updateCourseBtn = findViewById(R.id.updateCourseBtn);
        deleteCourseBtn = findViewById(R.id.deleteCourseBtn);




    }
}