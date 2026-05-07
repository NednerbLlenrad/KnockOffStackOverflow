package learn.slack_overflow.controllers;

import learn.slack_overflow.domain.SlackUserService;
import learn.slack_overflow.models.SlackUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/slack-user")
public class SlackUserController {

    private final SlackUserService service;

    public SlackUserController(SlackUserService service) {
        this.service = service;
    }

    @GetMapping
    public List<SlackUser> findAll() {
        return service.findAll();
    }

    @GetMapping("/{slackUserId}")
    public SlackUser findById(@PathVariable int slackUserId){
        return service.findById(slackUserId);
    }
}
