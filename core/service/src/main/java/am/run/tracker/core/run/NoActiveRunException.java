package am.run.tracker.core.run;

import java.util.UUID;


public class NoActiveRunException extends RuntimeException {

    private UUID userId;

    public NoActiveRunException(final UUID userId) {
        super(String.format("User with ID: %s, does not have active run", userId));
        this.userId = userId;

    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
