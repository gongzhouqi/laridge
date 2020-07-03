package controllers;

import models.GameLoadModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import user.User;

@Controller
public class GameController {

    @GetMapping("/gameStart")
    @ResponseBody
    public String gameStart() {
        User.getSingleton().letGameStart();
        return "OK";
    }

    @GetMapping("/gameReady")
    @ResponseBody
    public String gameReady() {
        GameLoadModel model = new GameLoadModel();
        User.getSingleton().frontEndGameReady(model);
        return model.getResponse();
    }

    @GetMapping("/waitGameUpdate")
    @ResponseBody
    public SseEmitter gameStream() {
        // 10 hours
        SseEmitter emitter = new SseEmitter(36000000L);
        User.getSingleton().waitOnGame(emitter);
        return emitter;
    }

    @GetMapping("/gameInput")
    @ResponseBody
    public String gameInput(@RequestParam String input) {
        User.getSingleton().inputToGame(input);
        return "OK";
    }
}
