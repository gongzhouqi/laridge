package controllers;

import models.GameLoadModel;
import models.GameWaitModel;
import models.RoomWaitModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import user.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        SseEmitter emitter = new SseEmitter(28000L);
        User.getSingleton().waitOnGame(emitter);
        return emitter;
    }
}
