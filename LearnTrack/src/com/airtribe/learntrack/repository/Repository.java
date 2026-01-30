package com.airtribe.learntrack.repository;

import java.util.ArrayList;
import java.util.List;

import com.airtribe.learntrack.entity.Enrollment;
import com.airtribe.learntrack.entity.Batch;
import com.airtribe.learntrack.entity.Course;
import com.airtribe.learntrack.entity.Student;
import com.airtribe.learntrack.entity.Trainer;

public class Repository {
    // Shared lists for storage across services
    public List<Student> students = new ArrayList<>();
    public List<Trainer> trainers = new ArrayList<>();
    public List<Course> courses = new ArrayList<>();
    public List<Batch> batches = new ArrayList<>();
    public List<Enrollment> enrollments = new ArrayList<>();

    public Repository() {
        //default constructory
    }

}
