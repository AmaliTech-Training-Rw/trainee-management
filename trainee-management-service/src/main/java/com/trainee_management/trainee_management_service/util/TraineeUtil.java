package com.trainee_management.trainee_management_service.util;

import java.util.ArrayList;
import java.util.List;

public class TraineeUtil {

    public static void addTrainees(List<String> currentTrainees, List<String> newTrainees) {
        if (currentTrainees == null) {
            currentTrainees = new ArrayList<>();
        }
        currentTrainees.addAll(newTrainees);
    }
}
