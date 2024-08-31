package am.run.tracker.api.rest.mapper.run;


import am.run.tracker.api.common.run.FinishRunRequestDto;
import am.run.tracker.api.common.run.StartRunRequestDto;
import am.run.tracker.api.common.run.UpdateRunRequestDto;
import am.run.tracker.core.common.datatypes.run.UpdateRunType;
import am.run.tracker.core.run.FinishRunRequest;
import am.run.tracker.core.run.StartRunRequest;
import am.run.tracker.core.run.UpdateRunRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RunMapper {
    default UpdateRunRequest toUpdateRunRequest(final UpdateRunRequestDto updateRunRequestDto) {
        if (updateRunRequestDto.getType() == UpdateRunType.START) {
            return toStartRunRequestDto((StartRunRequestDto) updateRunRequestDto);
        } else if (updateRunRequestDto.getType() == UpdateRunType.FINISH) {
            return toFinishRunRequestDto((FinishRunRequestDto) updateRunRequestDto);
        } else {
            throw new IllegalArgumentException("Unsupported Run update request type: " + updateRunRequestDto.getType());
        }
    }

    FinishRunRequest toFinishRunRequestDto(FinishRunRequestDto finishRunRequestDto);

    StartRunRequest toStartRunRequestDto(StartRunRequestDto startRunRequestDto);
}
