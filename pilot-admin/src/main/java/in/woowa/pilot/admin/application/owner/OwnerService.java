package in.woowa.pilot.admin.application.owner;

import in.woowa.pilot.admin.application.owner.dto.request.OwnerCreateDto;
import in.woowa.pilot.admin.application.owner.dto.request.OwnerSearchDto;
import in.woowa.pilot.admin.application.owner.dto.request.OwnerUpdateDto;
import in.woowa.pilot.admin.application.owner.dto.response.OwnerPagedResponsesDto;
import in.woowa.pilot.admin.application.owner.dto.response.OwnerResponseDto;
import in.woowa.pilot.admin.common.exception.ResourceNotFoundException;
import in.woowa.pilot.admin.repository.owner.OwnerCustomRepository;
import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.core.owner.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OwnerService {
    private final OwnerCustomRepository ownerCustomRepository;
    private final OwnerRepository ownerRepository;

    @Transactional
    public OwnerResponseDto create(OwnerCreateDto requestDto) {
        return new OwnerResponseDto(ownerRepository.save(requestDto.toOwner()));
    }

    @Transactional(readOnly = true)
    public OwnerResponseDto findById(Long id) {
        return new OwnerResponseDto(findOwner(id));
    }

    @Transactional(readOnly = true)
    public OwnerPagedResponsesDto findAll(Pageable pageable) {
        Page<Owner> owners = ownerRepository.findAll(pageable);

        return new OwnerPagedResponsesDto(owners);
    }

    @Transactional(readOnly = true)
    public OwnerPagedResponsesDto findByCondition(OwnerSearchDto condition, Pageable pageable) {
        Page<Owner> owners = ownerCustomRepository.fetchPagedByCondition(condition, pageable);

        return new OwnerPagedResponsesDto(owners);
    }

    @Transactional
    public OwnerResponseDto update(OwnerUpdateDto updateRequest) {
        Owner owner = findOwner(updateRequest.getId());
        owner.update(updateRequest.getName(), updateRequest.getEmail(), updateRequest.getSettleType());

        return new OwnerResponseDto(owner);
    }

    @Transactional
    public void delete(Long id) {
        Owner owner = findOwner(id);
        owner.delete();
    }

    private Owner findOwner(Long id) {
        return ownerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("owner", "id", id));
    }
}
