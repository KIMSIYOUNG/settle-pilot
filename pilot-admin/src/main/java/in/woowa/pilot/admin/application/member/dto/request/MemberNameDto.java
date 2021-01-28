package in.woowa.pilot.admin.application.member.dto.request;

import lombok.Getter;

@Getter
public class MemberNameDto {
    private final String name;

    public MemberNameDto(String name) {
        this.name = name;
    }
}
