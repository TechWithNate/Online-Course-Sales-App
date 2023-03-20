package com.tech.coursesales;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements CourseAdapter.CourseClickInterface{

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private FloatingActionButton addCourse;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<CourseModel> models;
    private CourseAdapter adapter;
    private RelativeLayout bottomSheet;
    private MaterialToolbar toolbar;
    private FirebaseAuth mAuth;
    private SwipeRefreshLayout swipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new CourseAdapter(this, models, this);
        recyclerView.setAdapter(adapter);
        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddCourseActivity.class));
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.logout:
                        Toast.makeText(MainActivity.this, "Logout", Toast.LENGTH_SHORT).show();
                mAuth.signOut();
                Intent i = new Intent(MainActivity.this, Login.class);
                startActivity(i);
                finish();
                }
                return true;
            }
        });


        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                Collections.shuffle(models);
                adapter.notifyDataSetChanged();
            }
        });

        getAllCourses();

    }

    private void getAllCourses(){
        models.clear();
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                progressBar.setVisibility(View.GONE);
                models.add(snapshot.getValue(CourseModel.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                progressBar.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                progressBar.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews(){
        swipe = findViewById(R.id.rootLayout);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        addCourse = findViewById(R.id.addCourse);
        bottomSheet = findViewById(R.id.bottomSheet);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Courses");
        mAuth = FirebaseAuth.getInstance();
        models = new ArrayList<>();
    }

    @Override
    public void onCourseClick(int position) {
        displayBottomSheet(models.get(position));
    }

    private void displayBottomSheet(CourseModel courseModel){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View layout = LayoutInflater.from(this).inflate(R.layout.bottom_sheet, bottomSheet);
        bottomSheetDialog.setContentView(layout);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.show();

        TextView courseName = layout.findViewById(R.id.courseName);
        TextView courseDesc = layout.findViewById(R.id.courseDescription);
        TextView courseSuit = layout.findViewById(R.id.courseSuit);
        TextView coursePrice = layout.findViewById(R.id.coursePrice);
        ImageView courseImage = layout.findViewById(R.id.courseImageHolder);
        MaterialButton editBtn = layout.findViewById(R.id.editBtn);
        MaterialButton view = layout.findViewById(R.id.viewDetailsBtn);

        courseName.setText(courseModel.getCourseName());
        courseDesc.setText(courseModel.getCourseDesc());
        courseSuit.setText(courseModel.getCourseSuit());
        coursePrice.setText(courseModel.getCoursePrice());
        Glide.with(this)
                .load(courseModel.getCourseImage())
                .into(courseImage);

        editBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EditCourseActivity.class);
            intent.putExtra("courses", courseModel);
            startActivity(intent);
        });

        view.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(courseModel.getCourseLink()));
            startActivity(i);
        });


    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.logout:
//                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
//                mAuth.signOut();
//                Intent i = new Intent(MainActivity.this, Login.class);
//                startActivity(i);
//                finish();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
}