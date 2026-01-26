package com.airtribe.learntrack;

import com.airtribe.learntrack.service.LearnTrackService;
import com.airtribe.learntrack.ui.View;

public class Main {
    public static void main(String[] args) {
        View.welcomeView();

        LearnTrackService learnTrack = new LearnTrackService();
        learnTrack.mainMenu();

        View.exitView();
    }
}
