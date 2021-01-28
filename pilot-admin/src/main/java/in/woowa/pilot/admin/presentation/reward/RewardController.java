package in.woowa.pilot.admin.presentation.reward;

import in.woowa.pilot.admin.application.reward.RewardService;
import in.woowa.pilot.admin.application.reward.dto.response.RewardResponseDto;
import in.woowa.pilot.admin.application.reward.dto.response.RewardResponsesDto;
import in.woowa.pilot.admin.presentation.reward.dto.request.*;
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
public class RewardController {
    private final RewardService rewardService;

    @PostMapping("/api/rewards")
    public ResponseEntity<Void> create(@RequestBody @Valid RewardCreateRequestDto requestDto) {
        RewardResponseDto reward = rewardService.create(requestDto.toServiceDto());

        return ResponseEntity.created(URI.create(String.format("/api/rewards/%s", reward.getId()))).build();
    }

    @PostMapping("/api/rewards/period")
    public ResponseEntity<Void> createByPeriod(@RequestBody @Valid RewardPeriodCreateRequestDto requestDto) {
        rewardService.createByPeriod(requestDto.toServiceDto());

        return ResponseEntity.created(URI.create("/api/rewards")).build();
    }

    @PostMapping("/api/rewards/orders")
    public ResponseEntity<Void> createByOrders(@RequestBody @Valid RewardOrdersCreateRequestDto requestDto) {
        rewardService.createByOrders(requestDto.toServiceDto());

        return ResponseEntity.created(URI.create("/api/rewards")).build();
    }

    @GetMapping("/api/rewards/{id}")
    public ResponseEntity<RewardResponseDto> find(@PathVariable @NotNull Long id) {
        RewardResponseDto response = rewardService.fetchById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/rewards/all")
    public ResponseEntity<RewardResponsesDto> findAll(Pageable pageable) {
        RewardResponsesDto response = rewardService.findAll(pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/rewards")
    public ResponseEntity<RewardResponsesDto> fetchByCondition(RewardSearchRequestDto condition, Pageable pageable) {
        RewardResponsesDto response = rewardService.fetchPagedByCondition(condition.toAppCondition(), pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/rewards/settles/{settleId}")
    public ResponseEntity<RewardResponsesDto> fetchBySettleId(@PathVariable @NotNull Long settleId, Pageable pageable) {
        RewardResponsesDto response = rewardService.fetchBySettleId(settleId, pageable);

        return ResponseEntity.ok(response);
    }


    @PutMapping("/api/rewards")
    public ResponseEntity<Void> update(@RequestBody @Valid RewardUpdateRequestDto requestDto) {
        rewardService.update(requestDto.toServiceDto());

        return ResponseEntity.created(URI.create(String.format("/api/rewards/%s", requestDto.getId()))).build();
    }

    @DeleteMapping("/api/rewards/{id}")
    public ResponseEntity<Void> delete(@PathVariable @NotNull Long id) {
        rewardService.delete(id);

        return ResponseEntity.noContent().location(URI.create(String.format("/api/rewards/%s", id))).build();
    }
}
