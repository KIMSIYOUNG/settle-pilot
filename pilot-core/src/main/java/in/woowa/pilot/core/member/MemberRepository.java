package in.woowa.pilot.core.member;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Transactional(readOnly = true)
    @Override
    @Query("select m " +
            "from Member m " +
            "where m.id = :id " +
            "and m.status = 'ACTIVE'")
    Optional<Member> findById(@Param("id") Long id);

    @Transactional(readOnly = true)
    @Override
    @Query("select m " +
            "from Member m " +
            "where m.status = 'ACTIVE'")
    List<Member> findAll();

    @Transactional(readOnly = true)
    @Query("select m " +
            "from Member m " +
            "where m.status = 'ACTIVE'" +
            "and m.email = :email")
    Optional<Member> findByEmail(@Param("email") String email);

    Optional<Member> findByEmailAndProvider(String email, AuthProvider provider);
}
