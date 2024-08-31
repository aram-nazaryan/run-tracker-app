package am.run.tracker.api.rest.mapper.user;

import am.run.tracker.api.common.user.UserCreationRequestDto;
import am.run.tracker.api.common.user.UserPatchRequestDto;
import am.run.tracker.core.user.UserCreationRequest;
import am.run.tracker.core.user.UserPatchRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserCreationRequest toUserCreationRequest(UserCreationRequestDto creationRequest);
    UserPatchRequest toUserPatchRequest(UserPatchRequestDto patchRequest);
}
