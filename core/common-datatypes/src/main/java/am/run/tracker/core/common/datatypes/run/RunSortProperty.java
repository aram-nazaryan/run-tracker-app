package am.run.tracker.core.common.datatypes.run;

import am.run.tracker.core.common.datatypes.user.SortProperty;

public enum RunSortProperty implements SortProperty {
    DISTANCE("distance"),
    SPEED("speed"),
    START_TIME("startTime"),
    FINISH_TIME("finishTime"),
    CREATED("created");

    private final String name;

    RunSortProperty(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
