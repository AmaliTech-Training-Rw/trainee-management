package com.assessment_management.assessment_management_service.service;

import com.assessment_management.assessment_management_service.model.Answer;
import com.assessment_management.assessment_management_service.model.AnswerSubmission;
import com.assessment_management.assessment_management_service.model.Assessment; // Import your Assessment model
import com.assessment_management.assessment_management_service.model.Question;
import com.assessment_management.assessment_management_service.repository.AssessmentRepository; // Import your Assessment repository
import com.assessment_management.assessment_management_service.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnswerService {

    @Autowired
    private AssessmentRepository assessmentRepository; // Dependency for Assessment repository

    @Autowired
    private QuestionRepository questionRepository;

    public String calculateScore(String assessmentId, AnswerSubmission submission) {
        int score = 0;
        List<Answer> answers = submission.getAnswers();

        // Fetch assessment by ID
        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new RuntimeException("Assessment not found"));

        // Get questions from the assessment
        List<Question> questions = assessment.getQuestions(); // Assuming your Assessment model has a method getQuestions()

        // Create a map for quick lookup
        Map<String, Question> questionsMap = new HashMap<>();
        for (Question question : questions) {
            questionsMap.put(question.getId(), question);
        }

        // Validate answers and calculate score
        for (Answer answer : answers) {
            Question question = questionsMap.get(answer.getQuestionId());
            if (question != null && question.getCorrectAnswer().equals(answer.getUserAnswer())) {
                score += 10; // Each correct answer is worth 10 points
            }
        }

        // Calculate the total possible score
        int totalPossibleScore = questions.size() * 10;

        // Constructing score message
        String scores = score + "/" + totalPossibleScore;
        return "{ \"scores\": \"" + scores + "\" }"; // Returning the score in JSON format
    }
}
