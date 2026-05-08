package learn.slack_overflow.controllers;

import learn.slack_overflow.domain.Result;
import learn.slack_overflow.domain.SlackQuestionService;
import learn.slack_overflow.models.SlackQuestion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/slack-question")
@CrossOrigin(origins = {"http://localhost:5173"})
public class SlackQuestionController {

    private final SlackQuestionService service;

    public SlackQuestionController(SlackQuestionService service) {
        this.service = service;
    }

    @GetMapping
    public List<SlackQuestion> findAll() {
        return service.findAll();
    }

    @GetMapping("/user/{slackUserId}")
    public List<SlackQuestion> findByUserId(@PathVariable int slackUserId) {
        return service.findByUserId(slackUserId);
    }

    @GetMapping("/{slackQuestionId}")
    public ResponseEntity<SlackQuestion> findById(@PathVariable int slackQuestionId) {
        SlackQuestion question = service.findById(slackQuestionId);

        if (question == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(question);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody SlackQuestion question) {
        Result<SlackQuestion> result = service.add(question);

        if (!result.isSuccess()) {
            return ErrorResponse.build(result);
        }

        URI location = URI.create("/api/slack-question/" + result.getPayload().getSlackQuestionId());
        return ResponseEntity.created(location).body(result.getPayload());
    }

    @PutMapping("/{slackQuestionId}")
    public ResponseEntity<Object> update(@PathVariable int slackQuestionId, @RequestBody SlackQuestion question) {
        if (slackQuestionId != question.getSlackQuestionId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<SlackQuestion> result = service.update(question);

        if (!result.isSuccess()) {
            return ErrorResponse.build(result);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{slackQuestionId}")
    public ResponseEntity<Void> deleteById(@PathVariable int slackQuestionId) {
        if (service.deleteById(slackQuestionId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
