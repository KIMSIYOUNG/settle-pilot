package in.woowa.pilot.admin.presentation.settle;

import in.woowa.pilot.admin.application.settle.SettleService;
import in.woowa.pilot.admin.application.settle.dto.response.SettleAmountResponseDto;
import in.woowa.pilot.admin.application.settle.dto.response.SettleResponseDto;
import in.woowa.pilot.admin.application.settle.dto.response.SettleResponsesDto;
import in.woowa.pilot.admin.presentation.settle.dto.request.*;
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
public class SettleController {
    private final SettleService settleService;

    @PostMapping("/api/settles")
    public ResponseEntity<Void> createInCaseSettle(@RequestBody @Valid SettleInCaseCreateRequestDto requestDto) {
        SettleResponseDto response = settleService.createInCaseSettle(requestDto.toServiceDto());

        return ResponseEntity.created(URI.create(String.format("/api/settles/%s", response.getId()))).build();
    }

    @PostMapping("/api/settles/batch")
    public ResponseEntity<Void> createBatchSettle(@RequestBody @Valid SettleRegularCreateRequestDto requestDto) {
        settleService.createRegularSettle(requestDto.toServiceDto());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/settles")
    public ResponseEntity<SettleResponsesDto> fetchByOwnerAndCondition(SettleSearchRequestDto condition, Pageable pageable) {
        SettleResponsesDto response = settleService.fetchPagedByCondition(condition.toAppCondition(), pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/settles/amount")
    public ResponseEntity<SettleAmountResponseDto> fetchAmountByCondition(SettleSnapShotSearchRequestDto request) {
        SettleAmountResponseDto response = settleService.fetchAmountByCondition(request.toAppCondition());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/settles/all")
    public ResponseEntity<SettleResponsesDto> findAll(Pageable pageable) {
        SettleResponsesDto response = settleService.fetchPagedAll(pageable);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/api/settles")
    public ResponseEntity<Void> updateSettleStatus(@RequestBody @Valid SettleUpdateRequestDto request) {
        settleService.updateSettleStatus(request.toServiceDto());

        return ResponseEntity.noContent().location(URI.create(String.format("/api/settles/%s", request.getId()))).build();
    }

    @PatchMapping("/api/settles/bulk")
    public ResponseEntity<Void> updateSettleBulk(@RequestBody @Valid SettleCompleteRequestDto condition) {
        settleService.bulkCompleteSettleStatus(condition.toAppCondition());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/api/settles/{id}")
    public ResponseEntity<Void> updateSettleStatus(@PathVariable @NotNull Long id) {
        settleService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
