package com.airtribe.learntrack.service;

import java.util.ArrayList;
import java.util.List;
import com.airtribe.learntrack.repository.Repository;
import com.airtribe.learntrack.ui.View;
import com.airtribe.learntrack.utils.Utils;

public class LearnTrackService {

    private List<Integer> inputs = new ArrayList<Integer>();
    private Repository repository = new Repository();
    private StudentService studentService = new StudentService(repository);
    private TrainerService trainerService = new TrainerService(repository);
    private CourseService courseService = new CourseService(repository);
    private BatchService batchService = new BatchService(repository);
    private EnrollmentService enrollmentService = new EnrollmentService(repository);

    // Default constructor
    public LearnTrackService() {
    }

    public void mainMenu() {
    boolean exit = false;

    while (!exit) {
        View.mainMenuView();
        int userInput = Utils.getUserInput(1, 6);
        this.inputs.add(userInput);
        switch (userInput) {
            case 1:
                studentService.menu();
                break;
            case 2:
                trainerService.menu();
                break;
            case 3:
                courseService.menu();
                break;
            case 4:
                batchService.menu();
                break;
            case 5:
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
