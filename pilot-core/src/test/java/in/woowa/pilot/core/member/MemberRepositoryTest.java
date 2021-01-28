package in.woowa.pilot.core.member;

import in.woowa.pilot.core.authority.AuthorityRepository;
import in.woowa.pilot.fixture.RepositoryTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class MemberRepositoryTest extends RepositoryTest {

    private Member member;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    @BeforeEach
    void setUp() {
        member = Member.testBuilder()
                .name("김시영")
                .email(UUID.randomUUID().toString() + "@woowahan.com")
                .provider(AuthProvider.GOOGLE)
                .build();
    }

    @DisplayName("동일한 이메일을 가진 유저는 저장할 수 없다.")
    @Test
    void uniqueEmail() {
        // given
        Member member = Member.builder()
                .name("name")
                .email("same@woowahan.com")
                .provider(AuthProvider.GOOGLE)
                .build();

        Member member1 = Member.builder()
                .name("name1")
                .email("same@woowahan.com")
                .provider(AuthProvider.LOCAL)
                .build();
        // when
        memberRepository.save(member);
        // then
        assertThatThrownBy(() -> memberRepository.save(member1))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("Jpa Auditing 기능이 정상 동작한다.")
    @Test
    void jpaAuditing() {
        // given
        assertThat(member.getCreatedAt()).isNull();
        // when
        Member savedMember = memberRepository.save(member);
        // then
        assertThat(savedMember.getCreatedAt()).isNotNull();
    }

    @DisplayName("저장한 이메일로 조회하는 경우 조회가 성공한다.")
    @Test
    void findByEmail() {
        // given
        Member savedMember = memberRepository.save(member);
        // when
        Optional<Member> findMember = memberRepository.findByEmail(savedMember.getEmail());
        // then
        assertThat(findMember.get()).usingRecursiveComparison().isEqualTo(savedMember);
    }

    @DisplayName("삭제된 회원은 조회할 수 없다.")
    @Test
    void findDeletedMember() {
        // given
        Member savedMember = memberRepository.save(member);
        // when
        savedMember.delete();
        // then
        Optional<Member> findMember = memberRepository.findById(savedMember.getId());
        assertThat(findMember.isPresent()).isFalse();
    }

    @DisplayName("삭제되지 않은 회원만 조회한다.")
    @Test
    void findAll() {
        // given
        Member member = memberRepository.save(memberRepository.save(member(Role.NORMAL, AuthProvider.GOOGLE)));
        Member savedMember = memberRepository.save(memberRepository.save(member(Role.NORMAL, AuthProvider.GOOGLE)));
        Member savedMember2 = memberRepository.save(memberRepository.save(member(Role.NORMAL, AuthProvider.GOOGLE)));
        // when
        member.delete();
        List<Member> members = memberRepository.findAll();
        // then
        assertAll(
                () -> Assertions.assertThat(members).hasSize(2),
                () -> Assertions.assertThat(members).contains(savedMember, savedMember2),
                () -> Assertions.assertThat(members).doesNotContain(member)
        );
    }

    @DisplayName("저장되지 않은 이메일로 조회하는 경우 조회되지 않는다.")
    @Test
    void findByNotExistEmail() {
        // when
        Optional<Member> findMember = memberRepository.findByEmail("NOT_EXIST_EMAIL");
        // then
        assertThat(findMember.isPresent()).isFalse();
    }

    @DisplayName("저장한 이메일 + Provider로 조회하는 경우 조회가 성공한다.")
    @Test
    void findByEmailAndProvider() {
        // given
        Member savedMember = memberRepository.save(member);
        // when
        Optional<Member> findMember = memberRepository.findByEmailAndProvider(savedMember.getEmail(), savedMember.getProvider());
        // then
        assertThat(findMember.get().getId()).isEqualTo(savedMember.getId());
    }

    @DisplayName("저장되지 않은 이메일 + Provider로 조회하는 경우 조회되지 않는다.")
    @Test
    void findByNotExistEmailAndProvider() {
        // given
        Member member = member(Role.NORMAL, AuthProvider.LOCAL);
        Member savedMember = memberRepository.save(member);
        // when
        Optional<Member> findMember = memberRepository.findByEmailAndProvider("NOT_EXIST_MEMBER", savedMember.getProvider());
        Optional<Member> findMember2 = memberRepository.findByEmailAndProvider(savedMember.getEmail(), AuthProvider.GOOGLE);
        // then
        assertThat(findMember.isPresent()).isFalse();
        assertThat(findMember2.isPresent()).isFalse();
    }

    private Member member(Role role, AuthProvider authProvider) {
        Member member = Member.testBuilder()
                .name("김시영")
                .email(UUID.randomUUID().toString() + "@woowahan.com")
                .provider(authProvider)
                .build();
        member.changeRole(role);

        return member;
    }
}