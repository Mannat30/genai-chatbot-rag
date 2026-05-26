package genai_chatbot.chatbot.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import genai_chatbot.chatbot.config.GroqConfig;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class ChatService {

    private final WebClient webClient;
    private final GroqConfig config;

    public ChatService(
            WebClient webClient,
            GroqConfig config){

        this.webClient = webClient;
        this.config = config;
    }

    public String askai(String message){

        try{

            Map<String,Object> requestBody =
                    Map.of(
                            "model","llama-3.3-70b-versatile",
                            "messages",
                            List.of(
                                    Map.of(
                                            "role","user",
                                            "content",message
                                    )
                            )
                    );

            String response =
                    webClient.post()
                            .uri("https://api.groq.com/openai/v1/chat/completions")
                            .header(
                                    "Authorization",
                                    "Bearer " + config.getApiKey()
                            )
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(requestBody)
                            .retrieve()
                            .bodyToMono(String.class)
                            .block();

            ObjectMapper mapper =
                    new ObjectMapper();

            JsonNode json =
                    mapper.readTree(response);

            return json
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

        }
        catch(Exception e){

            return "Error : " + e.getMessage();
        }
    }
}