package am.run.tracker.api.common.run;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Malformed request")
public class MalformedRunFinishRequestExceptionDto extends RuntimeException {

    public MalformedRunFinishRequestExceptionDto(@JsonProperty("message") String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
