package am.run.tracker.core.user;

import am.run.tracker.core.common.datatypes.user.Gender;

import java.time.LocalDate;

public record UserCreationRequest(
        String firstName,
        String lastName,
        LocalDate birthDate,
        Gender gender
) {
}
