package am.run.tracker.api.rest.mapper.user;

import am.run.tracker.api.common.user.UserDetailedRM;
import am.run.tracker.api.rest.RunTrackerEndpoint;
import am.run.tracker.api.rest.mapper.run.RunAssembler;
import am.run.tracker.core.persistence.entities.user.User;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserDetailedAssembler extends RepresentationModelAssemblerSupport<User, UserDetailedRM> {

    private final RunAssembler runAssembler;

    public UserDetailedAssembler(final RunAssembler runAssembler) {
        super(RunTrackerEndpoint.class, UserDetailedRM.class);
        this.runAssembler = runAssembler;
    }

    @Override
    public UserDetailedRM toModel(User entity) {
        return new UserDetailedRM(entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getBirthDate(),
                entity.getGender(),
                entity.getCreated(),
                entity.getUpdated(),
                entity.getRuns() != null ? entity.getRuns().stream().map(runAssembler::toModel).collect(Collectors.toSet()) : null);
    }
}
