package am.run.tracker.api.rest.mapper.user;

import am.run.tracker.api.common.user.UserRM;
import am.run.tracker.api.rest.RunTrackerEndpoint;
import am.run.tracker.core.persistence.entities.user.User;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class UserAssembler extends RepresentationModelAssemblerSupport<User, UserRM> {


    public UserAssembler() {
        super(RunTrackerEndpoint.class, UserRM.class);
    }

    @Override
    public UserRM toModel(User entity) {
        return new UserRM(entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getBirthDate(),
                entity.getGender(),
                entity.getCreated(),
                entity.getUpdated());
    }
}
