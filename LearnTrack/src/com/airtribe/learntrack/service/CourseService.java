package com.airtribe.learntrack.service;

import com.airtribe.learntrack.utils.Utils;
import com.airtribe.learntrack.entity.Course;
import com.airtribe.learntrack.ui.View;

public class CourseService {
    
    public CourseService() {
        // Default constructor
    }

    public void menu() {
        boolean back = false;
        // infinite loop to keep the application running
        while (!back) {
            View.courseSubmenuView();
            int userInput = Utils.getUserInput(1, 5);
            switch (userInput) {
                case 1:
                    this.addCourse();
                    break;
                case 2:
                    this.searchCourse();
                    break;
                case 3:
                    this.deactivateCourse();
                    break;
                case 4:
                    this.viewAllCourses();
                    break;
                case 5:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }
    }

    private Course addCourse() {
        return null;
    }

    private Course searchCourse() {
        return null;
    }

    private Course deactivateCourse() {
        return null;
    }

    private Course viewAllCourses() {
        return null;
    }
}
