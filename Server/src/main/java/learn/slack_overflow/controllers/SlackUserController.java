package learn.slack_overflow.controllers;

import learn.slack_overflow.domain.Result;
import learn.slack_overflow.domain.ResultType;
import learn.slack_overflow.domain.SlackUserService;
import learn.slack_overflow.models.SlackUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/slack-user")
@CrossOrigin(origins = {"http://localhost:5173"})
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
    public ResponseEntity<SlackUser> findById(@PathVariable int slackUserId) {
        SlackUser user = service.findById(slackUserId);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody SlackUser user) {
        Result<SlackUser> result = service.add(user);

        if (!result.isSuccess()) {
            return ErrorResponse.build(result);
        }

        URI location = URI.create("/api/slack-user/" + result.getPayload().getSlackUserId());
        return ResponseEntity.created(location).body(result.getPayload());
    }

    @PutMapping("/{slackUserId}")
    public ResponseEntity<Object> update(@PathVariable int slackUserId, @RequestBody SlackUser user) {
        if (slackUserId != user.getSlackUserId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<SlackUser> result = service.update(user);

        if (!result.isSuccess()) {
            return ErrorResponse.build(result);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{slackUserId}")
    public ResponseEntity<Void> deleteById(@PathVariable int slackUserId) {
        if (service.deleteById(slackUserId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
