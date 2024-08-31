package am.run.tracker.core.user;

import am.run.tracker.core.common.datatypes.PageResponse;
import am.run.tracker.core.common.datatypes.SearchGenericRequest;
import am.run.tracker.core.common.datatypes.user.UserSearchFilter;
import am.run.tracker.core.common.datatypes.user.UserSortProperty;
import am.run.tracker.core.persistence.entities.user.User;

import java.util.Optional;
import java.util.UUID;

public interface UserService {

    /**
     * Creates new user from request
     *
     * @param userCreationRequest
     */
    User create(final UserCreationRequest userCreationRequest);

    /**
     * Patches new user with request
     *
     * @param userId
     * @param userPatchRequest
     */
    User patch(final UUID userId, final UserPatchRequest userPatchRequest);

    /**
     * Gets user by ID
     *
     * @param userId
     */
    User get(final UUID userId);

    /**
     * Deletes user by ID
     *
     * @param userId
     */
    User delete(final UUID userId);

    /**
     * Searches for a user by ID
     *
     * @param userId
     */
    Optional<User> find(final UUID userId);

    /**
     * Searches for a user request
     *
     * @param searchGenericRequest
     */
    PageResponse<User> search(SearchGenericRequest<UserSearchFilter, UserSortProperty> searchGenericRequest);
}
