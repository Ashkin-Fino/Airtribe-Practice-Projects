package com.airtribe.learntrack;

import com.airtribe.learntrack.service.LearnTrackService;
import com.airtribe.learntrack.ui.View;

public class Main {
    public static void main(String[] args) {
        /*
        This is the main menu of the applicaton. Prints a welcome message
        and then calls the learntrack.mainMenu() method, where the
        application main menu loop runs. After exiting from the main menu
        loop, prints an exit message.
        */
        View.welcomeView();

        LearnTrackService learnTrack = new LearnTrackService();
        learnTrack.mainMenu();

        View.exitView();
    }
}