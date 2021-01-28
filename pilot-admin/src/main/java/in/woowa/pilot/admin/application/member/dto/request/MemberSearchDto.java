package in.woowa.pilot.admin.application.member.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberSearchDto {
    private final Long id;
    private final String name;
    private final String email;

    @Builder
    public MemberSearchDto(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
