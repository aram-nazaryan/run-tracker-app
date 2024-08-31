package am.run.tracker.core.persistence.repositories.user;

import am.run.tracker.core.common.datatypes.user.Gender;
import am.run.tracker.core.persistence.entities.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {

    @EntityGraph(
            attributePaths = {"runs"},
            type = EntityGraph.EntityGraphType.LOAD
    )
    Optional<User> getUserByIdAndDeletedIsNull(UUID userId);

    @Query("""
            select u
            from User u 
            where (:query is null or lower(u.firstName) like lower(concat(:query, '%')) or lower(u.lastName) like lower(concat(:query, '%')))
            and (:#{#publishedStates == null} = true or (u.gender in :genders))
            and (coalesce(:bornBefore) is null or u.birthDate <= :bornBefore)
            and (coalesce(:bornAfter) is null or u.birthDate >= :bornAfter)
            and u.deleted is null
            """)
    Page<User> search(@Param("query") String query,
                      @Param("genders") Set<Gender> genders,
                      @Param("bornBefore") Instant bornBefore,
                      @Param("bornAfter") Instant bornAfter,
                      PageRequest page);
}
