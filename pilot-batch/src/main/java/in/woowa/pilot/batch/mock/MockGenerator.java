package in.woowa.pilot.batch.mock;

import in.woowa.pilot.core.account.Account;
import in.woowa.pilot.core.account.AccountNumber;
import in.woowa.pilot.core.account.AccountType;
import in.woowa.pilot.core.authority.Authority;
import in.woowa.pilot.core.authority.AuthorityRepository;
import in.woowa.pilot.core.authority.AuthorityStatus;
import in.woowa.pilot.core.member.AuthProvider;
import in.woowa.pilot.core.member.Member;
import in.woowa.pilot.core.member.MemberRepository;
import in.woowa.pilot.core.member.Role;
import in.woowa.pilot.core.order.BusinessNo;
import in.woowa.pilot.core.order.Order;
import in.woowa.pilot.core.order.OrderDetail;
import in.woowa.pilot.core.order.OrderRepository;
import in.woowa.pilot.core.order.OrderStatus;
import in.woowa.pilot.core.order.PaymentOption;
import in.woowa.pilot.core.order.PaymentType;
import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.core.owner.OwnerRepository;
import in.woowa.pilot.core.reward.Reward;
import in.woowa.pilot.core.reward.RewardRepository;
import in.woowa.pilot.core.reward.RewardType;
import in.woowa.pilot.core.settle.SettleType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Component
@Profile(value = "create-mock-data")
public class MockGenerator {
    private final OwnerRepository ownerRepository;
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final RewardRepository rewardRepository;
    private final AuthorityRepository authorityRepository;

    @Transactional
    @PostConstruct
    void generateDummyData() {
        Member member = Member.builder()
                .name("siyoung")
                .email("siyoung@woowahan.com")
                .provider(AuthProvider.GOOGLE)
                .build();
        member.changeRole(Role.ADMIN);

        memberRepository.save(member);

        List<Member> members = memberRepository.saveAll(
                Arrays.asList(
                        createMember("aaj", "hodol@woowahan.com"),
                        createMember("kth", "taehun@woowahan.com"),
                        createMember("pyh", "youngho@woowahan.com"),
                        createMember("jjj", "jaeju@woowahan.com"),
                        createMember("lsb", "sangbo@woowahan.com")
                )
        );
        members.forEach((mem) -> authorityRepository.save(new Authority(mem, Role.ADMIN, AuthorityStatus.PENDING)));

        List<Owner> owners = ownerRepository.saveAll(
                Arrays.asList(
                        createOwner("ksh", "seokani81@woowahan.com", SettleType.DAILY, AccountType.BANK),
                        createOwner("ksh", "sehee.kwon@woowahan.com", SettleType.DAILY, AccountType.PAYPAL),
                        createOwner("kmj", "hywjin@woowahan.com", SettleType.DAILY, AccountType.PAYPAL),
                        createOwner("ksy", "siyoung@woowahan.com", SettleType.DAILY, AccountType.BANK),
                        createOwner("kyb", "kyb0320@woowahan.com", SettleType.DAILY, AccountType.BANK),
                        createOwner("pwb", "wbluke@woowahan.com", SettleType.DAILY, AccountType.BANK),
                        createOwner("ssy", "narastella@woowahan.com", SettleType.WEEK, AccountType.PAYPAL),
                        createOwner("ldw", "dwlee@woowahan.com", SettleType.WEEK, AccountType.PAYPAL),
                        createOwner("lhj", "hojin.lee@woowahan.com", SettleType.WEEK, AccountType.BANK),
                        createOwner("isk", "sklim@woowahan.com", SettleType.MONTH, AccountType.BANK),
                        createOwner("cth", "lannstark@woowahan.com", SettleType.MONTH, AccountType.PAYPAL)
                )
        );

        List<Order> orders = new ArrayList<>();

        for (Owner owner : owners) {
            for (int i = 0; i < 4; i++) {
                for (int SameDayPerOrder = 0; SameDayPerOrder < 30; SameDayPerOrder++) {
                    orders.add(createOrder(owner, SameDayPerOrder, OrderStatus.CANCEL, PaymentType.MOBILE));
                    orders.add(createOrder(owner, SameDayPerOrder, OrderStatus.DELIVERY_CONFIRM, PaymentType.CARD));
                    orders.add(createOrder(owner, SameDayPerOrder, OrderStatus.DELIVERY_CONFIRM, PaymentType.POINT));
                }
            }
        }

        for (Owner owner : owners) {
            for (int i = 0; i < 3; i++) {
                for (int SameDayPerOrder = 31; SameDayPerOrder < 60; SameDayPerOrder++) {
                    orders.add(createOrder(owner, SameDayPerOrder, OrderStatus.ORDER_CONFIRM, PaymentType.CARD));
                    orders.add(createOrder(owner, SameDayPerOrder, OrderStatus.DELIVERY_CONFIRM, PaymentType.CARD));
                }
            }
        }

        for (Owner owner : owners) {
            for (int i = 0; i < 3; i++) {
                for (int SameDayPerOrder = 61; SameDayPerOrder < 70; SameDayPerOrder++) {
                    orders.add(createOrder(owner, SameDayPerOrder, OrderStatus.CANCEL, PaymentType.MOBILE));
                }
            }
        }

        orderRepository.saveAll(orders);

        List<Reward> rewards = new ArrayList<>();
        for (Owner owner : owners) {
            for (int dayIndex = 0; dayIndex < 20; dayIndex++) {
                rewards.add(createReward(owner, dayIndex, RewardType.SYSTEM_ERROR, "System_Error", 10000));
            }

            for (int i = 30; i < 60; i++) {
                rewards.add(createReward(owner, i, RewardType.DELIVERY_ACCIDENT, "accident", 17000));
            }

            for (int i = 60; i < 70; i++) {
                rewards.add(createReward(owner, i, RewardType.ABUSING, "abusing", -5000));
            }

        }
        rewardRepository.saveAll(rewards);
    }

    private Owner createOwner(String name, String email, SettleType type, AccountType accountType) {
        return Owner.builder()
                .name(name)
                .email(email)
                .settleType(type)
                .account(new Account(accountType, new AccountNumber(UUID.randomUUID().toString())))
                .build();
    }


    private Order createOrder(Owner owner, int index, OrderStatus status, PaymentType type) {
        Order order = Order.builder()
                .owner(owner)
                .businessNo(new BusinessNo(LocalDate.now(), UUID.randomUUID().toString()))
                .orderDateTime(LocalDateTime.now().minusDays(index))
                .orderStatus(status)
                .build();
        order.addOrderDetails(createOrderDetails(type));
        return order;
    }

    private List<OrderDetail> createOrderDetails(PaymentType type) {
        return Arrays.asList(
                OrderDetail.builder()
                        .paymentType(PaymentType.COUPON)
                        .paymentOption(PaymentOption.OWNER_COUPON)
                        .amount(BigDecimal.valueOf(3000))
                        .build(),
                OrderDetail.builder()
                        .paymentType(type)
                        .paymentOption(PaymentOption.EMPTY)
                        .amount(BigDecimal.valueOf(14000))
                        .build()
        );
    }

    private Reward createReward(Owner owner, int minusDay, RewardType type, String description, int rewardMoney) {
        return Reward.builder()
                .businessNo(new BusinessNo(LocalDate.now(), UUID.randomUUID().toString()))
                .rewardDateTime(LocalDateTime.now().minusDays(minusDay))
                .rewardType(type)
                .description(description)
                .amount(BigDecimal.valueOf(rewardMoney))
                .owner(owner)
                .build();
    }

    private Member createMember(String name, String email) {
        return Member.builder()
                .name(name)
                .email(email)
                .provider(AuthProvider.GOOGLE)
                .build();
    }
}

