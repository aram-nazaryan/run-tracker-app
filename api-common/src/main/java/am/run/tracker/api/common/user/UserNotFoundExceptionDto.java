package am.run.tracker.api.common.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "User not found")
public class UserNotFoundExceptionDto extends RuntimeException {

    public UserNotFoundExceptionDto(@JsonProperty("message") String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
