package am.run.tracker.api.rest.mapper.run;

import am.run.tracker.api.common.run.PointGeographyDto;
import am.run.tracker.api.common.run.RunRM;
import am.run.tracker.api.rest.RunTrackerEndpoint;
import am.run.tracker.core.persistence.entities.run.Run;
import org.locationtech.jts.geom.Point;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class RunAssembler extends RepresentationModelAssemblerSupport<Run, RunRM> {

    public RunAssembler() {
        super(RunTrackerEndpoint.class, RunRM.class);
    }

    @Override
    public RunRM toModel(Run entity) {

        return new RunRM(entity.getId(),
                entity.getUserId(),
                entity.getStartTime(),
                entity.getFinishTime(),
                toPointGeographyDto(entity.getStartPosition()),
                entity.getFinishPosition() != null ? toPointGeographyDto(entity.getFinishPosition()) : null,
                entity.getDistance(),
                entity.getSpeed(),
                entity.getCreated()
        );
    }

    private PointGeographyDto toPointGeographyDto(final Point point) {
        return new PointGeographyDto(point.getY(), point.getX());
    }
}
