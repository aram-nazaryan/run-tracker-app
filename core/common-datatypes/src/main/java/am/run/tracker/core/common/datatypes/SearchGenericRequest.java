package am.run.tracker.core.common.datatypes;

import am.run.tracker.core.common.datatypes.user.PageRequest;
import am.run.tracker.core.common.datatypes.user.SortProperty;

public record SearchGenericRequest<T, E extends SortProperty>(
        String query,
        T filter,
        PageRequest<E> pageRequest
) {
}
