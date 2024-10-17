package com.assessment_management.assessment_management_service.helpers;

import com.assessment_management.assessment_management_service.exception.BadRequestException;
import com.assessment_management.assessment_management_service.model.Cohort;
import com.assessment_management.assessment_management_service.model.Trainee;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

public class AssessmentHelper {

    private static final String TRAINEE_SERVICE_URL = "http://localhost:8093/trainees/all";
    private static final String COHORT_SERVICE_URL = "http://localhost:8093/trainees/cohorts/all";


    public static List<String> fetchAllTraineeIds(RestTemplate restTemplate, HttpHeaders headers) {
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<List<Trainee>> response = restTemplate.exchange(
                TRAINEE_SERVICE_URL,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Trainee>>() {}
        );

        List<Trainee> trainees = response.getBody();
        if (trainees == null || trainees.isEmpty()) {
            throw new BadRequestException("No available trainees found.");
        }

        return trainees.stream().map(Trainee::getId).collect(Collectors.toList());
    }


    public static List<Cohort> fetchAllCohorts(RestTemplate restTemplate) {
        ResponseEntity<List<Cohort>> response = restTemplate.exchange(
                COHORT_SERVICE_URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Cohort>>() {}
        );

        List<Cohort> cohorts = response.getBody();
        validateCohorts(cohorts, "Cohorts");

        return cohorts;
    }

    public static void validateCohorts(List<Cohort> cohorts, String entityName) {
        if (cohorts == null || cohorts.isEmpty()) {
            throw new BadRequestException(entityName + " cannot be null or empty.");
        }
    }


    public static void validateId(String id, String entityName) {
        if (id == null || id.isEmpty()) {
            throw new BadRequestException(entityName + " cannot be null or empty.");
        }
    }
}
