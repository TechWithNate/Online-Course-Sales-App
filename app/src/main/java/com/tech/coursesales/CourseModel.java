package com.tech.coursesales;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.EditText;

import androidx.annotation.NonNull;

public class CourseModel implements Parcelable {
    private String courseName;
    private String coursePrice;
    private String courseSuit;
    private String courseImage;
    private String courseLink;
    private String courseDesc;
    private String courseID;

    public CourseModel() {
    }

    public CourseModel(String courseID, String courseName, String coursePrice, String courseSuit, String courseImage, String courseLink, String courseDesc) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.coursePrice = coursePrice;
        this.courseSuit = courseSuit;
        this.courseImage = courseImage;
        this.courseLink = courseLink;
        this.courseDesc = courseDesc;
    }


    protected CourseModel(Parcel in) {
        courseName = in.readString();
        coursePrice = in.readString();
        courseSuit = in.readString();
        courseImage = in.readString();
        courseLink = in.readString();
        courseDesc = in.readString();
    }

    public static final Creator<CourseModel> CREATOR = new Creator<CourseModel>() {
        @Override
        public CourseModel createFromParcel(Parcel in) {
            return new CourseModel(in);
        }

        @Override
        public CourseModel[] newArray(int size) {
            return new CourseModel[size];
        }
    };

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCoursePrice() {
        return coursePrice;
    }

    public void setCoursePrice(String coursePrice) {
        this.coursePrice = coursePrice;
    }

    public String getCourseSuit() {
        return courseSuit;
    }

    public void setCourseSuit(String courseSuit) {
        this.courseSuit = courseSuit;
    }

    public String getCourseImage() {
        return courseImage;
    }

    public void setCourseImage(String courseImage) {
        this.courseImage = courseImage;
    }

    public String getCourseLink() {
        return courseLink;
    }

    public void setCourseLink(String courseLink) {
        this.courseLink = courseLink;
    }

    public String getCourseDesc() {
        return courseDesc;
    }

    public void setCourseDesc(String courseDesc) {
        this.courseDesc = courseDesc;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(courseName);
        dest.writeString(coursePrice);
        dest.writeString(courseSuit);
        dest.writeString(courseImage);
        dest.writeString(courseLink);
        dest.writeString(courseDesc);
    }
}
