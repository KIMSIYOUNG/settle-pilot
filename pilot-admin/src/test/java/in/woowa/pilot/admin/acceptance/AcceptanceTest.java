package in.woowa.pilot.admin.acceptance;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.woowa.pilot.admin.application.IntegrationTest;
import in.woowa.pilot.admin.application.member.dto.response.MemberResponseDto;
import in.woowa.pilot.admin.security.user.UserPrincipal;
import in.woowa.pilot.core.member.Member;
import in.woowa.pilot.core.member.MemberRepository;
import in.woowa.pilot.core.order.OrderRepository;
import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.core.owner.OwnerRepository;
import in.woowa.pilot.core.reward.RewardRepository;
import in.woowa.pilot.fixture.member.MemberFixture;
import in.woowa.pilot.fixture.order.OrderFixture;
import in.woowa.pilot.fixture.owner.OwnerFixture;
import in.woowa.pilot.fixture.reward.RewardFixture;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureMockMvc
public class AcceptanceTest extends IntegrationTest {
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    RewardRepository rewardRepository;

    @Autowired
    OwnerRepository ownerRepository;

    @BeforeEach
    void setUp(WebApplicationContext context) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(springSecurity())
                .alwaysDo(print())
                .build();
    }

    protected Runnable 일반회원세션() {
        Member member = memberRepository.save(MemberFixture.createNormalWithoutId());
        MemberResponseDto webMemberResponseDto = new MemberResponseDto(member);

        UserPrincipal userPrincipal = UserPrincipal.create(webMemberResponseDto);
        Authentication authentication = new OAuth2AuthenticationToken(
                userPrincipal,
                userPrincipal.getAuthorities(),
                webMemberResponseDto.getProvider().name()
        );

        SecurityContextHolder.clearContext();
        return () -> SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    protected Runnable 관리자회원세션() {
        Member member = memberRepository.save(MemberFixture.createAdminMember());
        MemberResponseDto webMemberResponseDto = new MemberResponseDto(member);

        UserPrincipal userPrincipal = UserPrincipal.create(webMemberResponseDto);
        Authentication authentication = new OAuth2AuthenticationToken(
                userPrincipal,
                userPrincipal.getAuthorities(),
                webMemberResponseDto.getProvider().name()
        );

        SecurityContextHolder.clearContext();
        return () -> SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    protected Owner 업주생성() {
        return ownerRepository.save(OwnerFixture.createWithoutId());
    }

    protected void 보상금액생성(Owner owner, int count, LocalDateTime dateTime) {
        IntStream.range(0, count)
                .forEach((i) -> rewardRepository.save(RewardFixture.createWithoutId(owner, BigDecimal.valueOf(30000), dateTime)));
    }

    protected void 주문생성(Owner owner, int count, LocalDateTime dateTime) {
        IntStream.range(0, count)
                .forEach((i) -> orderRepository.save(OrderFixture.createWithoutId(owner, 2, dateTime)));
    }

    protected String getPathVariableBy(String location) {
        return "/" + getIdBy(location);
    }

    protected Long getIdBy(String location) {
        return Long.parseLong(location.substring(location.lastIndexOf("/") + 1));
    }
}
