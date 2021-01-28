package in.woowa.pilot.admin.presentation.owner;

import in.woowa.pilot.admin.application.owner.OwnerService;
import in.woowa.pilot.admin.application.owner.dto.response.OwnerPagedResponsesDto;
import in.woowa.pilot.admin.application.owner.dto.response.OwnerResponseDto;
import in.woowa.pilot.admin.presentation.owner.dto.request.OwnerCreateRequestDto;
import in.woowa.pilot.admin.presentation.owner.dto.request.OwnerSearchRequestDto;
import in.woowa.pilot.admin.presentation.owner.dto.request.OwnerUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;

@Validated
@RequiredArgsConstructor
@RestController
public class OwnerController {
    private final OwnerService ownerService;

    @PostMapping("/api/owners")
    public ResponseEntity<Void> create(@RequestBody @Valid OwnerCreateRequestDto requestDto) {
        OwnerResponseDto response = ownerService.create(requestDto.toServiceDto());

        return ResponseEntity.created(URI.create(String.format("/api/owners/%s", response.getId()))).build();
    }

    @GetMapping("/api/owners/{id}")
    public ResponseEntity<OwnerResponseDto> find(@PathVariable @NotNull Long id) {
        OwnerResponseDto response = ownerService.findById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/owners")
    public ResponseEntity<OwnerPagedResponsesDto> findByCondition(OwnerSearchRequestDto ownerSearchRequestDto, Pageable pageable) {
        OwnerPagedResponsesDto response = ownerService.findByCondition(ownerSearchRequestDto.toAppCondition(), pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/owners/all")
    public ResponseEntity<OwnerPagedResponsesDto> findAll(Pageable pageable) {
        OwnerPagedResponsesDto response = ownerService.findAll(pageable);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/api/owners")
    public ResponseEntity<Void> update(@RequestBody @Valid OwnerUpdateRequestDto updateRequest) {
        OwnerResponseDto response = ownerService.update(updateRequest.toServiceDto());

        return ResponseEntity.created(URI.create(String.format("/api/owners/%s", response.getId()))).build();
    }

    @DeleteMapping("/api/owners/{id}")
    public ResponseEntity<Void> delete(@PathVariable @NotNull Long id) {
        ownerService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
