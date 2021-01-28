package in.woowa.pilot.admin.presentation.authority;

import in.woowa.pilot.admin.application.authority.AuthorityService;
import in.woowa.pilot.admin.application.authority.dto.AuthorityResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;

@Validated
@RestController
@RequiredArgsConstructor
public class AuthorityController {
    private final AuthorityService authorityService;

    @PostMapping("/api/authorities")
    public ResponseEntity<Void> create(@RequestBody @Valid AuthorityRequestDto request) {
        AuthorityResponseDto response = authorityService.create(request.toServiceDto());

        return ResponseEntity.created(URI.create(String.format("/api/authorities/%s", response.getId()))).build();
    }

    @PatchMapping("/api/authorities/approve/{id}")
    public ResponseEntity<Void> approve(@PathVariable @NotNull Long id) {
        authorityService.approve(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/api/authorities/reject/{id}")
    public ResponseEntity<Void> reject(@PathVariable @NotNull Long id) {
        authorityService.reject(id);

        return ResponseEntity.noContent().build();
    }
}
