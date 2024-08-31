package am.run.tracker.api.common.user;

import am.run.tracker.core.common.datatypes.user.Gender;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UserCreationRequestDto (
        @NotNull
        @Size(max = 100)
        String firstName,

        @NotNull
        @Size(max = 100)
        String lastName,

        @NotNull
        LocalDate birthDate,

        @NotNull
        Gender gender
) {
}
