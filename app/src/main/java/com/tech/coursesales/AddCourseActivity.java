package com.tech.coursesales;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddCourseActivity extends AppCompatActivity {

    private EditText courseName;
    private EditText coursePrice;
    private EditText courseSuit;
    private EditText courseImage;
    private EditText courseLink;
    private EditText courseDesc;
    private MaterialButton addCourseBtn;

    private String courseID;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        initViews();

        addCourseBtn.setOnClickListener(v -> {
            String mCourseName = courseName.getText().toString();
            String mCoursePrice = coursePrice.getText().toString();
            String mCourseSuit = courseSuit.getText().toString();
            String mCourseImage = courseImage.getText().toString();
            String mCourseLink = courseLink.getText().toString();
            String mCourseDesc = courseDesc.getText().toString();
            courseID = mCourseName;

            CourseModel courseModel = new CourseModel(courseID, mCourseName, mCoursePrice, mCourseSuit, mCourseImage, mCourseLink, mCourseDesc);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    databaseReference.child(courseID).setValue(courseModel);
                    Toast.makeText(AddCourseActivity.this, "Course Added", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddCourseActivity.this, MainActivity.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(AddCourseActivity.this, "Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });




        });

    }

    private void initViews(){
        courseName = findViewById(R.id.courseName);
        coursePrice = findViewById(R.id.coursePrice);
        courseSuit = findViewById(R.id.courseSuit);
        courseImage = findViewById(R.id.courseImage);
        courseLink = findViewById(R.id.courseLink);
        courseDesc = findViewById(R.id.courseDescription);
        addCourseBtn = findViewById(R.id.addCourseBtn);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Courses");

    }

}