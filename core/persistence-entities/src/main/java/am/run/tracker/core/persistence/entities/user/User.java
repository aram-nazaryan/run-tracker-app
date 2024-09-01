package am.run.tracker.core.persistence.entities.user;

import am.run.tracker.core.common.datatypes.user.Gender;
import am.run.tracker.core.persistence.entities.run.Run;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "_user")
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    @Type(PostgreSQLEnumType.class)
    private Gender gender;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Where(clause = "deleted is null")
    private Set<Run> runs;

    @CreatedDate
    @Column(name = "created", updatable = false, nullable = false)
    private Instant created;

    @LastModifiedDate
    @Column(name = "updated", nullable = false)
    private Instant updated;

    @Column(name = "deleted")
    private Instant deleted;

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

    public Set<Run> getRuns() {
        return runs;
    }

    public void setRuns(Set<Run> runs) {
        this.runs = runs;
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

    public Instant getDeleted() {
        return deleted;
    }

    public void setDeleted(Instant deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof User user)) return false;

        return new EqualsBuilder()
                .append(id, user.id)
                .append(firstName, user.firstName)
                .append(lastName, user.lastName)
                .append(birthDate, user.birthDate)
                .append(gender, user.gender)
                .append(runs, user.runs)
                .append(created, user.created)
                .append(updated, user.updated)
                .append(deleted, user.deleted)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(firstName)
                .append(lastName)
                .append(birthDate)
                .append(gender)
                .append(runs)
                .append(created)
                .append(updated)
                .append(deleted)
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
                .append("runs", runs)
                .append("created", created)
                .append("updated", updated)
                .append("deleted", deleted)
                .toString();
    }
}
