package controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import user.User;

@Controller
public class InitController {

    @GetMapping("/init")
    @ResponseBody
    public String init() {
        return User.getSingleton().toString();
    }
}
