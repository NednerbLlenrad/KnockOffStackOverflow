package learn.slack_overflow.models;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.Objects;

public class SlackUser {

    private int slackUserId;

    @NotBlank(message = "Username is required.")
    @Size(max = 20, message = "Username must be 20 characters or fewer.")
    private String username;

    @NotBlank(message = "Password hash is required.")
    @Size(max = 2048, message = "Password hash must be 2048 characters or fewer.")
    private String passwordHash;

    @NotBlank(message = "Email is required.")
    @Email(message = "Email must be valid.")
    @Size(max = 50, message = "Email must be 50 characters or fewer.")
    private String email;

    private int chillPoints;
    private LocalDateTime createdAt;
    private LocalDateTime editedAt;

    public SlackUser() {
    }

    public SlackUser(int slackUserId, String username, String passwordHash, String email, int chillPoints, LocalDateTime createdAt, LocalDateTime editedAt) {
        this.slackUserId = slackUserId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.chillPoints = chillPoints;
        this.createdAt = createdAt;
        this.editedAt = editedAt;
    }

    public int getSlackUserId() {
        return slackUserId;
    }

    public void setSlackUserId(int slackUserId) {
        this.slackUserId = slackUserId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        SlackUser slackUser = (SlackUser) o;
        return slackUserId == slackUser.slackUserId
                && chillPoints == slackUser.chillPoints
                && Objects.equals(username, slackUser.username)
                && Objects.equals(email, slackUser.email)
                && Objects.equals(createdAt, slackUser.createdAt)
                && Objects.equals(editedAt, slackUser.editedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(slackUserId, username, email, chillPoints, createdAt, editedAt);
    }
}
