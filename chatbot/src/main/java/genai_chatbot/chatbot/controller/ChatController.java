package genai_chatbot.chatbot.controller;

import genai_chatbot.chatbot.dto.ChatDto;
import genai_chatbot.chatbot.dto.ChatResponse;
import genai_chatbot.chatbot.service.ChatService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatController {
    private final ChatService service;
    public ChatController(ChatService service){
        this.service=service;
    }
    @PostMapping
    public ChatResponse chat(@RequestBody ChatDto chatDto){
        String answer=service.askai(chatDto.getMessage());
        ChatResponse response=new ChatResponse();
        response.setResponse(answer);
        return response;
    }
}
