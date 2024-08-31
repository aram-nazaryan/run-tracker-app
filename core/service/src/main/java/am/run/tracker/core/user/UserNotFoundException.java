package am.run.tracker.core.user;

import java.util.UUID;


public class UserNotFoundException extends RuntimeException {

    private UUID userId;

    public UserNotFoundException(final UUID userId) {
        super(String.format("User with ID: %s, not found", userId));
        this.userId = userId;

    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
