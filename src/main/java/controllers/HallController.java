package controllers;

import games.Games;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import user.User;

@Controller
public class HallController {
    @GetMapping("/getRooms")
    @ResponseBody
    public String getRooms() {
        return "OK" + User.getSingleton().getCurrentRooms();
    }

    @GetMapping("/broadcastRoom")
    @ResponseBody
    public String broadcastRoom() {
        User.getSingleton().broadcastRoom();
        System.out.println("啊啊啊");
        return "广播了一次房间";
    }

    @GetMapping("/gameTypes")
    @ResponseBody
    public String getGames() {
        return Games.getGameInfoJson();
    }

    @GetMapping("/changeName")
    @ResponseBody
    public String changeName(@RequestParam String name) {
        return User.getSingleton().setUsername(name);
    }

}
