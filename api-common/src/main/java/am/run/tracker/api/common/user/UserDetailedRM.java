package am.run.tracker.api.common.user;

import am.run.tracker.api.common.run.RunRM;
import am.run.tracker.core.common.datatypes.user.Gender;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public class UserDetailedRM extends UserRM {

    private Set<RunRM> runs;

    public UserDetailedRM(final UUID id,
                          final String firstName,
                          final String laseName,
                          final LocalDate birthDate,
                          final Gender gender,
                          final Instant created,
                          final Instant updated,
                          final Set<RunRM> runs) {
        super(id, firstName, laseName, birthDate, gender, created, updated);
        this.runs = runs;
    }

    public Set<RunRM> getRuns() {
        return runs;
    }

    public void setRuns(Set<RunRM> runs) {
        this.runs = runs;
    }
}
