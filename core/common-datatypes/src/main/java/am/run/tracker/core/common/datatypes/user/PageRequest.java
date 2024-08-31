package am.run.tracker.core.common.datatypes.user;


public record PageRequest<T extends SortProperty> (
        int page,
        int size,
        T sortProperty,
        SortDirection sortDirection
) {
    public enum SortDirection {
        ASC, DESC
    }
}
