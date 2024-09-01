package am.run.tracker.api.common.user;

import am.run.tracker.api.common.constants.RelationConstants;
import am.run.tracker.core.common.datatypes.user.Gender;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;


@Relation(collectionRelation = RelationConstants.CONTENT_NAME)
public class UserRM extends RepresentationModel<UserRM> {

    private UUID id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Gender gender;
    private Instant created;
    private Instant updated;

    public UserRM() {
    }

    public UserRM(Instant created) {
        this.created = created;
    }

    public UserRM(final UUID id,
                  final String firstName,
                  final String lastName,
                  final LocalDate birthDate,
                  final Gender gender,
                  final Instant created,
                  final Instant updated) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.created = created;
        this.updated = updated;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }


    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getUpdated() {
        return updated;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof UserRM userRM)) return false;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(id, userRM.id)
                .append(firstName, userRM.firstName)
                .append(lastName, userRM.lastName)
                .append(birthDate, userRM.birthDate)
                .append(gender, userRM.gender)
                .append(created, userRM.created)
                .append(updated, userRM.updated)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(id)
                .append(firstName)
                .append(lastName)
                .append(birthDate)
                .append(gender)
                .append(created)
                .append(updated)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("firstName", firstName)
                .append("lastName", lastName)
                .append("birthDate", birthDate)
                .append("gender", gender)
                .append("updated", updated)
                .append("created", created)
                .toString();
    }
}
