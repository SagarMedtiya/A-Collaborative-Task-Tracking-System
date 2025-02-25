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
        OpenAiService service = new OpenAiService(apiKey); // Initialize OpenAI service with API key
        int retryCount = 3; // Number of retries for handling rate-limit errors (HTTP 429)
        int retryDelay = 3000; // Delay (in milliseconds) before retrying after a failure

        for (int i = 0; i < retryCount; i++) {
            try {
                // Build the completion request with prompt, model, and token limit
                CompletionRequest request = CompletionRequest.builder()
                        .prompt(prompt)
                        .model("gpt-3.5-turbo") // Specify the GPT model
                        .maxTokens(100) // Limit the response length
                        .build();

                // Send the request to OpenAI and return the generated text
                return service.createCompletion(request)
                        .getChoices()
                        .get(0)
                        .getText()
                        .trim(); // Remove unnecessary whitespace from the response
            } catch (HttpException e) {
                // Check if the error is due to rate limiting (HTTP 429)
                if (e.code() == 429 && i < retryCount - 1) {
                    try {
                        Thread.sleep(retryDelay); // Wait before retrying
                    } catch (InterruptedException ignored) {
                        // Ignore interruption errors
                    }
                } else {
                    // If it's not a rate-limit error or retries are exhausted, throw an exception
                    throw new RuntimeException("Failed to generate task description: " + e.getMessage(), e);
                }
            } catch (Exception e) {
                // Handle any other errors
                throw new RuntimeException("Failed to generate task description: " + e.getMessage(), e);
            }
        }

        // Return an error message if all retries fail
        return "Error: Too many requests. Please try again later.";
    }
}
