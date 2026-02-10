package com.airtribe.learntrack.repository;

import java.util.ArrayList;
import java.util.List;

import com.airtribe.learntrack.entity.Enrollment;
import com.airtribe.learntrack.entity.Batch;
import com.airtribe.learntrack.entity.Course;
import com.airtribe.learntrack.entity.Student;
import com.airtribe.learntrack.entity.Trainer;

public class Repository {
    /*
    This class contains 5 Array Lists to store the objects of Student, 
    Trainer, Course, Batch and Enrollment. All the lists are private.
    Public getter methods are given to access the list. But only public adder
    methods are given to add new objects to the lists. Replacing entire list
    or deleting elements from the list is not allowed.
    */
    private final List<Student> students = new ArrayList<>();
    private final List<Trainer> trainers = new ArrayList<>();
    private final List<Course> courses = new ArrayList<>();
    private final List<Batch> batches = new ArrayList<>();
    private final List<Enrollment> enrollments = new ArrayList<>();

    public Repository() {
        //default constructor
    }

    /*
    Returns a copy of the list of students. Prevents modification of original
    array, but allows modifying individual student objects.
    */
    public List<Student> getStudents() {
        return new ArrayList<>(this.students);
    }

    /*
    Adds the student object passed as parameter to the 
    List of Students.
    */
    public void addStudent(Student student) {
        this.students.add(student);
    }

    /*
    Returns a copy of the list of Trainers.
    */
    public List<Trainer> getTrainers() {
        return new ArrayList<>(this.trainers);
    }

    /*
    Adds the Trainer object passed as parameter to the 
    List of Trainer.
    */
    public void addTrainer(Trainer trainer) {
        this.trainers.add(trainer);
    }

    /*
    Returns a copy of the list of Batches.
    */
    public List<Batch> getBatches() {
        return new ArrayList<>(this.batches);
    }

    /*
    Adds the Batch object passed as parameter to the 
    List of Batches.
    */
    public void addBatch(Batch batch) {
        this.batches.add(batch);
    }

    /*
    Returns a copy of the list of Courses.
    */
    public List<Course> getCourses() {
        return new ArrayList<>(this.courses);
    }

    /*
    Adds the Course object passed as parameter to the 
    List of Course.
    */
    public void addCourse(Course course) {
        this.courses.add(course);
    }

    /*
    Returns a copy of the list of Enrollments.
    */
    public List<Enrollment> getEnrollments() {
        return new ArrayList<>(this.enrollments);
    }

    /*
    Adds the Enrollment object passed as parameter to the 
    List of Enrollment.
    */
    public void addEnrollment(Enrollment enrollment) {
        this.enrollments.add(enrollment);
    }
}
