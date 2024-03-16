package br.com.alura.ecomart.chatbot.web.controller;

import br.com.alura.ecomart.chatbot.domain.service.ChatService;
import br.com.alura.ecomart.chatbot.web.dto.PerguntaDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

@Controller
@RequestMapping({"/", "chat"})
public class ChatController {

    private static final String PAGINA_CHAT = "chat";
    private ChatService service;

    public ChatController(ChatService service) {
        this.service = service;
    }
    @GetMapping
    public String carregarPaginaChatbot() {
        return PAGINA_CHAT;
    }

    @PostMapping
    @ResponseBody
    public ResponseBodyEmitter responderPergunta(@RequestBody PerguntaDto dto) {

        var fluxo = service.responderPergunta(dto.pergunta());
        var emitter =  new ResponseBodyEmitter();

        fluxo.subscribe(chunk -> {
           var token = chunk.getChoices().get(0).getMessage().getContent();
           if (token != null){
               emitter.send(token);
           }
        }, emitter:: completeWithError, emitter::complete);

        return emitter;
    }

    @GetMapping("limpar")
    public String limparConversa() {
        return PAGINA_CHAT;
    }

}
