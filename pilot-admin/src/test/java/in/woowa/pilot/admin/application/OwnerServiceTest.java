package in.woowa.pilot.admin.application;

import in.woowa.pilot.admin.application.owner.OwnerService;
import in.woowa.pilot.admin.application.owner.dto.request.OwnerCreateDto;
import in.woowa.pilot.admin.application.owner.dto.request.OwnerSearchDto;
import in.woowa.pilot.admin.application.owner.dto.response.OwnerResponseDto;
import in.woowa.pilot.admin.common.exception.ResourceNotFoundException;
import in.woowa.pilot.admin.util.Owners;
import in.woowa.pilot.core.settle.SettleType;
import in.woowa.pilot.fixture.BaseFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class OwnerServiceTest extends IntegrationTest {
    private OwnerCreateDto createRequestDto;

    @Autowired
    OwnerService ownerService;

    @BeforeEach
    void setUp() {
        createRequestDto = Owners.createRequestDto();
    }

    @DisplayName("업주를 정상적으로 생성할 수 있다.")
    @Test
    void create() {
        // given
        OwnerResponseDto responseDto = ownerService.create(createRequestDto);
        // then
        assertAll(
                () -> assertThat(responseDto.getId()).isNotNull(),
                () -> assertThat(responseDto.getSettleType()).isEqualTo(createRequestDto.getSettleType()),
                () -> assertThat(responseDto.getAccount().getAccountType()).isEqualTo(createRequestDto.getAccountDto().getAccountType()),
                () -> assertThat(responseDto.getAccount().getAccountNumber()).isEqualTo(createRequestDto.getAccountDto().getAccountNumber().getAccountNumber())
        );
    }

    @DisplayName("저장된 업주를 ID로 찾아올 수 있다.")
    @Test
    void findById() {
        // given
        OwnerResponseDto responseDto = ownerService.create(createRequestDto);
        // when
        OwnerResponseDto findOwner = ownerService.findById(responseDto.getId());
        // then
        assertThat(responseDto).isEqualToIgnoringGivenFields(findOwner, "account");
        assertThat(responseDto.getAccount()).usingRecursiveComparison().isEqualTo(findOwner.getAccount());
    }

    @DisplayName("저장된 업주를 모두 찾아올 수 있다.")
    @Test
    void findAll() {
        // given
        OwnerResponseDto expected = ownerService.create(createRequestDto);
        ownerService.create(Owners.createRequestDto());
        ownerService.create(Owners.createRequestDto());
        ownerService.create(Owners.createRequestDto());
        // when
        Page<OwnerResponseDto> owners = ownerService.findAll(PageRequest.of(0, 2)).getOwners();
        // then
        assertAll(
                () -> assertThat(owners.getTotalElements()).isEqualTo(4),
                () -> assertThat(owners.getTotalPages()).isEqualTo(2),
                () -> assertThat(owners.getContent()).hasSize(2),
                () -> assertThat(owners.getContent().get(0)).usingRecursiveComparison().isEqualTo(expected)
        );
    }

    @DisplayName("이름 기준 - 저장된 업주를 조건에 맞게 조회할 수 있다.")
    @Test
    void findByConditionByName() {
        // given
        ownerService.create(Owners.createRequestDto("KIM", "KIM@GOOGLE.COM"));
        ownerService.create(Owners.createRequestDto("KIM", "KIM2@GOOGLE.COM"));
        ownerService.create(Owners.createRequestDto("KIM", "KIMU@GOOGLE.com"));
        ownerService.create(Owners.createRequestDto("LEE", "LEE@NAVER.COM"));
        ownerService.create(Owners.createRequestDto("HONG", "HONG@NAVER.COM"));
        // when
        OwnerSearchDto condition = new OwnerSearchDto(null, "KIM", null, null);
        Page<OwnerResponseDto> owners = ownerService.findByCondition(condition, BaseFixture.DEFAULT_PAGEABLE).getOwners();
        // then
        assertAll(
                () -> assertThat(owners.getTotalElements()).isEqualTo(3),
                () -> assertThat(owners.getContent().get(0))
                        .isEqualToIgnoringGivenFields(owners.getContent().get(1), "id", "email", "account")
                        .isEqualToIgnoringGivenFields(owners.getContent().get(2), "id", "email", "account"),
                () -> assertThat(owners.getContent().get(0).getName()).isEqualTo("KIM")
        );
    }

    @DisplayName("이메일 기준 - 저장된 업주를 조건에 맞게 조회할 수 있다.")
    @Test
    void findByConditionByEmail() {
        // given
        ownerService.create(Owners.createRequestDto("KIM", "KIM@GOOGLE.COM"));
        ownerService.create(Owners.createRequestDto("KIM", "KIM2@GOOGLE.COM"));
        ownerService.create(Owners.createRequestDto("KIM", "KIMU@GOOGLE.com"));
        ownerService.create(Owners.createRequestDto("LEE", "LEE@NAVER.COM"));
        ownerService.create(Owners.createRequestDto("HONG", "HONG@NAVER.COM"));
        // when
        OwnerSearchDto condition = new OwnerSearchDto(null, null, "KIM@GOOGLE.COM", null);
        Page<OwnerResponseDto> owners = ownerService.findByCondition(condition, BaseFixture.DEFAULT_PAGEABLE).getOwners();
        // then
        assertAll(
                () -> assertThat(owners.getTotalElements()).isEqualTo(1),
                () -> assertThat(owners.getContent().get(0).getEmail()).isEqualTo("KIM@GOOGLE.COM")
        );
    }

    @DisplayName("지급금 유형 기준 - 저장된 업주를 조건에 맞게 조회할 수 있다.")
    @Test
    void findByConditionBySettleType() {
        // given
        ownerService.create(Owners.createRequestDto("KIM", "KIM@GOOGLE.COM"));
        ownerService.create(Owners.createRequestDto("KIM", "KIM2@GOOGLE.COM"));
        ownerService.create(Owners.createRequestDto("KIM", "KIMU@GOOGLE.com"));
        ownerService.create(Owners.createRequestDto("LEE", "LEE@NAVER.COM"));
        ownerService.create(Owners.createRequestDto("HONG", "HONG@NAVER.COM"));
        // when
        OwnerSearchDto condition = new OwnerSearchDto(null, null, null, SettleType.DAILY);
        Page<OwnerResponseDto> owners = ownerService.findByCondition(condition, BaseFixture.DEFAULT_PAGEABLE).getOwners();
        // then
        assertThat(owners.getTotalElements()).isEqualTo(5);
    }

    @DisplayName("업주의 이름과 이메일을 변경할 수 있다.")
    @Test
    void update() {
        // given
        OwnerResponseDto responseDto = ownerService.create(createRequestDto);
        // when
        OwnerResponseDto updatedOwner = ownerService.update(Owners.createUpdateRequest(responseDto.getId()));
        // then
        assertAll(
                () -> assertThat(updatedOwner.getName()).isEqualTo(Owners.CHANGED_NAME),
                () -> assertThat(updatedOwner.getEmail()).isEqualTo(Owners.CHANGED_EMAIL)
        );
    }

    @DisplayName("업주를 삭제할 수 있고, 삭제된 회원은 조회되지 않는다.")
    @Test
    void delete() {
        // given
        OwnerResponseDto responseDto = ownerService.create(createRequestDto);
        // when
        ownerService.delete(responseDto.getId());
        // then
        assertThatThrownBy(() -> ownerService.findById(responseDto.getId()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("owner not found with id : '%s'", responseDto.getId());
    }
}