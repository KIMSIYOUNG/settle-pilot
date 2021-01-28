package in.woowa.pilot.core.authority;

import in.woowa.pilot.core.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Optional<Authority> findByMember(Member findMember);
}
