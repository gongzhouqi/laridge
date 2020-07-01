package controllers;

import models.RoomAccessModel;
import models.RoomWaitModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import user.User;

@Controller
public class RoomController {
    @GetMapping("/createRoom")
    @ResponseBody
    public String createRoom(@RequestParam String roomName, @RequestParam String gameId) {
        User.getSingleton().hostRoom(roomName, gameId);
        return "OK";
    }

    @GetMapping("/closeRoom")
    @ResponseBody
    public String closeRoom() {
        User.getSingleton().closeRoom();
        return "成功关闭房间";
    }

    @GetMapping("/guestRoom")
    @ResponseBody
    public String guestRoom(@RequestParam String roomId, @RequestParam int ip, @RequestParam int port) {
        RoomAccessModel rm = new RoomAccessModel(roomId, ip, port);
        User.getSingleton().guestRoom(rm);
        return rm.getResponse();
    }

    @GetMapping("/waitRoomUpdate")
    @ResponseBody
    public String waitRoomUpdate() {
        RoomWaitModel waiter = new RoomWaitModel();
        User.getSingleton().waitOnRoom(waiter);
        return waiter.getResponse();
    }
}
