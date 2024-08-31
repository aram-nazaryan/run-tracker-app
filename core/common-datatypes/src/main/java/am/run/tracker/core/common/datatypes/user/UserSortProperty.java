package am.run.tracker.core.common.datatypes.user;

public enum UserSortProperty implements SortProperty {
    FIRST_NAME("firstName"),
    LAST_NAME("lastName"),
    BIRTH_DATE("birthDate"),
    CREATED("created");

    private final String name;

    UserSortProperty(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
