package com.airtribe.learntrack.service;

import java.util.ArrayList;
import java.util.List;

import com.airtribe.learntrack.ui.View;
import com.airtribe.learntrack.utils.Utils;

public class LearnTrackService {

    private List<Integer> inputs = new ArrayList<Integer>();

    // Default constructor
    public LearnTrackService() {
    }

    public void mainMenu() {
        boolean exit = false;
        // infinite loop to keep the application running
        while (!exit) {
            View.mainMenuView();
            int userInput = Utils.getUserInput(1, 6);
            this.inputs.add(userInput);
            switch (userInput) {
                case 1:
                    StudentService studentService = new StudentService();
                    studentService.menu();
                    break;
                case 2:
                    TrainerService trainerService = new TrainerService();
                    trainerService.menu();
                    break;
                case 3:
                    CourseService courseService = new CourseService();
                    courseService.menu();
                    break;
                case 4:
                    BatchService batchService = new BatchService();
                    batchService.menu();
                    break;
                case 5:
                    EnrollmentService enrollmentService = new EnrollmentService();
                    enrollmentService.menu();
                    break;
                case 6:
                    System.out.println("Exiting Application...");
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }
    }
}
