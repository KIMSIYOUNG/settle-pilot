package in.woowa.pilot.admin.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    BUSINESS(500, "서버에서 문제가 생겼어요. 조금만 기다려주세요."),
    UNEXPECTED(500, "서버에서 알 수 없는 예외가 발생했어요. 조금만 기다려주세요."),

    NOT_SUPPORTED_MEDIA_TYPE(400, "지원하지 않는 MediaType입니다. Json으로 통신해주세요."),
    INVALID_VALIDATE(400, "입력이 이상해요."),
    UN_AUTHENTICATE(403, "인증이 잘못되었어요."),

    RESOURCE_NOT_FOUND(404, "해당 리소스가 존재하지 않아요!"),
    RESOURCE_DUPLICATE(400, "이미 존재하는 리소스입니다."),
    RESOURCE_INVALID(400, "잘못된 리소스 입니다!"),

    UN_AUTHORIZED(401, "권한이 없습니다.");

    private final int status;
    private final String code;

}
