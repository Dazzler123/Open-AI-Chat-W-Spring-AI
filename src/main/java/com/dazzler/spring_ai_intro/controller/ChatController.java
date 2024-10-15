package com.dazzler.spring_ai_intro.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Controller
public class ChatController {

    @Value("${openai.api.key}")
    private String openAiApiKey;

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/completions";

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/prompt")
    public String prompt(@RequestParam String message, Model model) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + openAiApiKey);

        String jsonBody = "{\n" +
                "  \"model\": \"gpt-3.5-turbo\",\n" +
                "  \"messages\": [{\"role\": \"user\", \"content\": \"" + message + "\"}],\n" +
                "  \"max_tokens\": 100\n" +
                "}";

        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        try {
            String response = restTemplate.exchange(
                    OPENAI_API_URL,
                    HttpMethod.POST,
                    entity,
                    String.class
            ).getBody();

            model.addAttribute("response", response);
        } catch (RestClientException e) {
            model.addAttribute("error", "Error calling OpenAI API: " + e.getMessage());
        }

        return "index";
    }

}
