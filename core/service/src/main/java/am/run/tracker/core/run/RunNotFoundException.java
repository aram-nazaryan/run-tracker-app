package am.run.tracker.core.run;

import java.util.UUID;


public class RunNotFoundException extends RuntimeException {

    private UUID runId;

    public RunNotFoundException(final UUID runId) {
        super(String.format("Run with ID: %s, not found", runId));
        this.runId = runId;

    }

    public UUID getRunId() {
        return runId;
    }

    public void setRunId(UUID runId) {
        this.runId = runId;
    }
}
