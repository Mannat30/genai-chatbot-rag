package genai_chatbot.chatbot.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import genai_chatbot.chatbot.config.GroqConfig;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ChatService {

    /*
    |--------------------------------------------------------------------------
    | Conversation Memory
    |--------------------------------------------------------------------------
    */
    private final List<Map<String,String>> chatHistory =
            new ArrayList<>();

    /*
    |--------------------------------------------------------------------------
    | Knowledge Base (Chunks)
    |--------------------------------------------------------------------------
    | Small chunks of custom knowledge
    |--------------------------------------------------------------------------
    */
    private final List<String> knowledgeBase =
            List.of(

                    "Java is platform independent because JVM executes bytecode.",

                    "Spring Boot simplifies backend application development.",

                    "OOP has four pillars: Encapsulation, Abstraction, Inheritance, and Polymorphism.",

                    "WebClient is used for external API communication.",

                    "ObjectMapper parses JSON responses."

            );

    /*
    |--------------------------------------------------------------------------
    | WebClient
    |--------------------------------------------------------------------------
    */
    private final WebClient webClient;

    /*
    |--------------------------------------------------------------------------
    | Config Class
    |--------------------------------------------------------------------------
    */
    private final GroqConfig config;

    /*
    |--------------------------------------------------------------------------
    | Constructor Injection
    |--------------------------------------------------------------------------
    */
    public ChatService(
            WebClient webClient,
            GroqConfig config){

        this.webClient = webClient;
        this.config = config;

        /*
        |--------------------------------------------------------------------------
        | System Prompt
        |--------------------------------------------------------------------------
        */
        chatHistory.add(
                Map.of(
                        "role","system",
                        "content",
                        "You are a senior Java, Spring Boot, and GenAI mentor. Explain concepts simply with examples."
                )
        );
    }
    private String retrieveContext(String question){

        question = question.toLowerCase();

        String bestChunk = "";
        int bestScore = 0;

        String[] questionWords =
                question.split("\\s+");

        for(String chunk : knowledgeBase){

            int score = 0;

            String lowerChunk =
                    chunk.toLowerCase();

            for(String word : questionWords){

                if(lowerChunk.contains(word)){
                    score++;
                }
            }

            if(score > bestScore){

                bestScore = score;
                bestChunk = chunk;
            }
        }

        if(bestScore == 0){
            return "No relevant context found.";
        }

        return bestChunk;
    }
    /*
    |--------------------------------------------------------------------------
    | Main AI Method
    |--------------------------------------------------------------------------
    */
    public String askai(String message){

        try{

            /*
            |--------------------------------------------------------------------------
            | Retrieve Relevant Context
            |--------------------------------------------------------------------------
            */
            String retrievedContext =
                    retrieveContext(message);

            /*
            |--------------------------------------------------------------------------
            | Create RAG Prompt
            |--------------------------------------------------------------------------
            */
            String ragPrompt =

                    "Answer only from the given context.\n\n"

                            + "Context:\n"
                            + retrievedContext

                            + "\n\nQuestion:\n"
                            + message;

            /*
            |--------------------------------------------------------------------------
            | Store User Message
            |--------------------------------------------------------------------------
            */
            chatHistory.add(
                    Map.of(
                            "role","user",
                            "content",ragPrompt
                    )
            );

            /*
            |--------------------------------------------------------------------------
            | Request Body
            |--------------------------------------------------------------------------
            */
            Map<String,Object> requestBody =
                    Map.of(
                            "model","llama-3.3-70b-versatile",
                            "messages",chatHistory
                    );

            /*
            |--------------------------------------------------------------------------
            | Send Request To Groq
            |--------------------------------------------------------------------------
            */
            String response =

                    webClient.post()

                            .uri(
                                    "https://api.groq.com/openai/v1/chat/completions"
                            )

                            .header(
                                    "Authorization",
                                    "Bearer " + config.getApiKey()
                            )

                            .contentType(
                                    MediaType.APPLICATION_JSON
                            )

                            .bodyValue(requestBody)

                            .retrieve()

                            .bodyToMono(String.class)

                            .block();

            /*
            |--------------------------------------------------------------------------
            | Parse JSON Response
            |--------------------------------------------------------------------------
            */
            ObjectMapper mapper =
                    new ObjectMapper();

            JsonNode json =
                    mapper.readTree(response);

            /*
            |--------------------------------------------------------------------------
            | Extract AI Response
            |--------------------------------------------------------------------------
            */
            String aiResponse =

                    json.path("choices")
                            .get(0)
                            .path("message")
                            .path("content")
                            .asText();

            /*
            |--------------------------------------------------------------------------
            | Store Assistant Response
            |--------------------------------------------------------------------------
            */
            chatHistory.add(
                    Map.of(
                            "role","assistant",
                            "content",aiResponse
                    )
            );

            /*
            |--------------------------------------------------------------------------
            | Return Final Response
            |--------------------------------------------------------------------------
            */
            return aiResponse;

        }
        catch(Exception e){

            return "Error : " + e.getMessage();
        }
    }
}