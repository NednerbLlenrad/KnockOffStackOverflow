package learn.slack_overflow.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Objects;

public class SlackQuestion {

    private int slackQuestionId;

    @NotBlank(message = "Title is required.")
    @Size(max = 100, message = "Title must be 100 characters or fewer.")
    private String title;

    @Size(max = 10000, message = "Body must be 10000 characters or fewer.")
    private String body;

    private int slackUserId;
    private int chillPoints;
    private LocalDateTime createdAt;
    private LocalDateTime editedAt;

    public SlackQuestion() {
    }

    public SlackQuestion(int slackQuestionId, String title, String body, int slackUserId, int chillPoints, LocalDateTime createdAt, LocalDateTime editedAt) {
        this.slackQuestionId = slackQuestionId;
        this.title = title;
        this.body = body;
        this.slackUserId = slackUserId;
        this.chillPoints = chillPoints;
        this.createdAt = createdAt;
        this.editedAt = editedAt;
    }

    public int getSlackQuestionId() {
        return slackQuestionId;
    }

    public void setSlackQuestionId(int slackQuestionId) {
        this.slackQuestionId = slackQuestionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getSlackUserId() {
        return slackUserId;
    }

    public void setSlackUserId(int slackUserId) {
        this.slackUserId = slackUserId;
    }

    public int getChillPoints() {
        return chillPoints;
    }

    public void setChillPoints(int chillPoints) {
        this.chillPoints = chillPoints;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getEditedAt() {
        return editedAt;
    }

    public void setEditedAt(LocalDateTime editedAt) {
        this.editedAt = editedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SlackQuestion that = (SlackQuestion) o;
        return slackQuestionId == that.slackQuestionId
                && slackUserId == that.slackUserId
                && chillPoints == that.chillPoints
                && Objects.equals(title, that.title)
                && Objects.equals(body, that.body)
                && Objects.equals(createdAt, that.createdAt)
                && Objects.equals(editedAt, that.editedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(slackQuestionId, title, body, slackUserId, chillPoints, createdAt, editedAt);
    }
}
