package com.airtribe.TaskTrackingAndManagement.service;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.adapter.rxjava2.HttpException;


@Service
public class AIService {

    @Value("${openai.api.key}")
    private String apiKey;

    public String generateTaskDescription(String prompt) {
        OpenAiService service = new OpenAiService(apiKey);
        int retryCount = 3; // Number of retries for rate-limiting errors
        int retryDelay = 3000; // Delay between retries in milliseconds

        for (int i = 0; i < retryCount; i++) {
            try {
                // Build the completion request
                CompletionRequest request = CompletionRequest.builder()
                        .prompt(prompt)
                        .model("gpt-3.5-turbo") // Use the correct model
                        .maxTokens(100) // Limit the response length
                        .build();

                // Send the request and return the generated text
                return service.createCompletion(request)
                        .getChoices()
                        .get(0)
                        .getText()
                        .trim(); // Trim whitespace from the response
            } catch (HttpException e) {
                // Handle rate-limiting errors (HTTP 429)
                if (e.code() == 429 && i < retryCount - 1) {
                    try {
                        Thread.sleep(retryDelay); // Wait before retrying
                    } catch (InterruptedException ignored) {
                        // Ignore interruption
                    }
                } else {
                    // Re-throw the exception if it's not a rate-limiting error or retries are exhausted
                    throw new RuntimeException("Failed to generate task description: " + e.getMessage(), e);
                }
            } catch (Exception e) {
                // Handle other exceptions
                throw new RuntimeException("Failed to generate task description: " + e.getMessage(), e);
            }
        }

        // Return an error message if all retries fail
        return "Error: Too many requests. Please try again later.";
    }
}
