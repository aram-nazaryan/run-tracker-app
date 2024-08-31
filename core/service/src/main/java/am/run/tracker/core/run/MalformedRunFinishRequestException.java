package am.run.tracker.core.run;

import java.time.Instant;


public class MalformedRunFinishRequestException extends RuntimeException {

    private Instant startTime;
    private Instant finishTime;

    public MalformedRunFinishRequestException(final Instant startTime, final Instant finishTime) {
        super(String.format("Run end time: %s, is before start time: %s", finishTime, startTime));
        this.startTime = startTime;
        this.finishTime = finishTime;

    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Instant finishTime) {
        this.finishTime = finishTime;
    }
}
