package am.run.tracker.core.common.datatypes.user;

import java.time.Instant;
import java.util.Set;

public record UserSearchFilter(
        Set<Gender> genders,
        Instant bornBefore,
        Instant bornAfter
) {
}
