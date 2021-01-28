package in.woowa.pilot.admin.application.owner.dto.response;

import in.woowa.pilot.admin.common.CustomPageImpl;
import in.woowa.pilot.core.owner.Owner;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class OwnerPagedResponsesDto {
    private CustomPageImpl<OwnerResponseDto> owners;

    public OwnerPagedResponsesDto(Page<Owner> owners) {
        List<OwnerResponseDto> content = owners.getContent().stream()
                .map(OwnerResponseDto::new)
                .collect(Collectors.toList());

        this.owners = new CustomPageImpl<>(content, owners.getPageable(), owners.getTotalElements());
    }
}
