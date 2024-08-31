package am.run.tracker.core.common.datatypes;

import am.run.tracker.core.common.datatypes.user.PageRequest;
import am.run.tracker.core.common.datatypes.user.SortProperty;

import java.util.List;

public record PageResponse<T> (
        long totalElements,
        int totalPages,
        int number,
        int size,
        SortProperty sortProperty,
        PageRequest.SortDirection sortDirection,
        List<T> content
)  {
    public boolean hasNext() {
        return number() + 1 < totalPages();
    }

    public boolean hasPrevious() {
        return number() > 0;
    }
}
