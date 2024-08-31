package am.run.tracker.core.user;

import am.run.tracker.core.common.datatypes.PageResponse;
import am.run.tracker.core.common.datatypes.SearchGenericRequest;
import am.run.tracker.core.common.datatypes.user.UserSearchFilter;
import am.run.tracker.core.common.datatypes.user.UserSortProperty;
import am.run.tracker.core.persistence.entities.user.User;
import am.run.tracker.core.persistence.repositories.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    public UserServiceImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public User create(UserCreationRequest userCreationRequest) {
        Assert.notNull(userCreationRequest, "Creation request must not be null!");
        Assert.notNull(userCreationRequest.firstName(), "First name must not be null!");
        Assert.notNull(userCreationRequest.lastName(), "Last name must not be null!");
        Assert.notNull(userCreationRequest.birthDate(), "Date of birth must not be null!");
        Assert.notNull(userCreationRequest.gender(), "Gender must not be null!");
        logger.trace("Creating user from request: {}", userCreationRequest);
        final User newUser = new User();
        newUser.setFirstName(userCreationRequest.firstName());
        newUser.setLastName(userCreationRequest.lastName());
        newUser.setBirthDate(userCreationRequest.birthDate());
        newUser.setGender(userCreationRequest.gender());

        final User saved = userRepository.save(newUser);
        logger.debug("Done creating user with ID: {}", saved.getId());
        return saved;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public User patch(final UUID userId, final UserPatchRequest userPatchRequest) {
        logger.trace("Patching user ID: {}, with request: {}", userId, userPatchRequest);
        final User user = get(userId);
        if (userPatchRequest.firstName() != null) {
            user.setFirstName(userPatchRequest.firstName());
        }
        if (userPatchRequest.lastName() != null) {
            user.setLastName(userPatchRequest.lastName());
        }
        if (userPatchRequest.gender() != null) {
            user.setGender(userPatchRequest.gender());
        }
        if (userPatchRequest.birthDate() != null) {
            user.setBirthDate(userPatchRequest.birthDate());
        }
        final User saved = userRepository.save(user);
        logger.debug("Done patching user ID: {}, with request: {}", saved.getId(), userPatchRequest);
        return saved;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public User get(final UUID userId) {
        logger.trace("Getting user by ID: {}", userId);
        final User user = userRepository.getUserByIdAndDeletedIsNull(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        logger.debug("Done getting user by ID: {}", user.getId());
        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public User delete(final UUID userId) {
        logger.trace("Deleting user by ID: {}", userId);
        final User user = get(userId);
        user.setDeleted(Instant.now());
        final User deletedUser = userRepository.save(user);
        logger.trace("Done deleting user by ID: {}", userId);
        return deletedUser;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<User> find(final UUID userId) {
        logger.trace("Finding user by ID: {}", userId);
        Optional<User> optionalUser = userRepository.getUserByIdAndDeletedIsNull(userId);
        logger.trace("Done searching for user by ID: {}", userId);
        return optionalUser;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponse<User> search(SearchGenericRequest<UserSearchFilter, UserSortProperty> searchGenericRequest) {
        logger.trace("Getting user by request: {}", searchGenericRequest);
        final Page<User> users = userRepository.search(searchGenericRequest.query(),
                searchGenericRequest.filter().genders(),
                searchGenericRequest.filter().bornBefore(),
                searchGenericRequest.filter().bornAfter(),
                PageRequest.of(searchGenericRequest.pageRequest().page(),
                        searchGenericRequest.pageRequest().size(),
                        Sort.Direction.valueOf(searchGenericRequest.pageRequest().sortDirection().name()),
                        searchGenericRequest.pageRequest().sortProperty().getName())
        );
        logger.trace("Done getting user by request: {}", searchGenericRequest);
        return new PageResponse<>(
                users.getTotalElements(),
                users.getTotalPages(),
                users.getNumber(),
                users.getSize(),
                searchGenericRequest.pageRequest().sortProperty(),
                searchGenericRequest.pageRequest().sortDirection(),
                users.getContent()
        );
    }
}
