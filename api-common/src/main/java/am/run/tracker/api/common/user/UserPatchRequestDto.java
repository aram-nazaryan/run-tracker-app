package am.run.tracker.api.common.user;

import am.run.tracker.core.common.datatypes.user.Gender;

import java.time.LocalDate;

public record UserPatchRequestDto (
        String firstName,
        String lastName,
        LocalDate birthDate,
        Gender gender
) {
}
