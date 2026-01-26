package com.airtribe.learntrack.service;

import com.airtribe.learntrack.utils.Utils;
import com.airtribe.learntrack.entity.Enrollment;
import com.airtribe.learntrack.ui.View;

public class EnrollmentService {
    
    public EnrollmentService() {
        // Default constructor
    }

    public void menu() {
        boolean back = false;
        // infinite loop to keep the application running
        while (!back) {
            View.enrollmentSubmenuView();
            int userInput = Utils.getUserInput(1, 5);
            switch (userInput) {
                case 1:
                    this.addEnrollment();
                    break;
                case 2:
                    this.searchEnrollment();
                    break;
                case 3:
                    this.deactivateEnrollment();
                    break;
                case 4:
                    this.viewAllEnrollments();
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

    private Enrollment addEnrollment() {
        return null;
    }

    private Enrollment searchEnrollment() {
        return null;
    }

    private Enrollment deactivateEnrollment() {
        return null;
    }

    private Enrollment viewAllEnrollments() {
        return null;
    }
}
