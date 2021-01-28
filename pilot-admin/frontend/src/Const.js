export const API_BASE_URL = `http://pilot-admin.ap-northeast-2.elasticbeanstalk.com`;
export const OAUTH2_REDIRECT_URI = `pilot-admin.ap-northeast-2.elasticbeanstalk.com/oauth2/redirect`;
export const GOOGLE_AUTH_URL = `${API_BASE_URL}/oauth2/authorize/google?redirect_uri=${OAUTH2_REDIRECT_URI}`;


export const DATE_TIME_CONVERTER = (dateTime, onlyDate = false) => {
    const dateAndTime = dateTime.split("T");
    const date = dateAndTime[0].split("-");
    const time = dateAndTime[1].split(":");
    if (onlyDate) {
        return date[0] + "년 " + date[1] + "월 " + date[2] + "일 ";
    }
    return date[0] + "년 " + date[1] + "월 " + date[2] + "일 " + time[0] + "시 " + time[1] + "분 " + time[0] + "초";
}

export const MONEY_DELIMITER = (input) => {
    return input.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") + "원";
}

export const DEFAULT_ORDER_SEARCH = {
    ownerId: null,
    orderNo: "",
    startAt: "",
    endAt: "",
    orderStatus: "",
}

export const formatDate = (date) => {
    let d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();

    if (month.length < 2)
        month = '0' + month;
    if (day.length < 2)
        day = '0' + day;

    return [year, month, day].join('-');
};

const now = new Date();
let before = new Date();
before.setDate(before.getDate() - 30);


export const DEFAULT_SETTLE_AMOUNT_PERIOD = {
    startAt: formatDate(before) + "T00:00:00",
    endAt: formatDate(now) + "T00:00:00"
}

export const DEFAULT_ORDER_REGISTER = {
    orderStatus: "ORDER_CONFIRM",
}

export const DEFAULT_DATE_TIME = {
    date: "",
    time: "",
}

export const DEFAULT_OWNER_SEARCH = {
    ownerId: "",
    name: "",
    email: "",
    settleType: "",
}

export const AUTH = {
    ADMIN: "관리자",
    NORMAL: "일반회원",
}

export const PROVIDER = {
    GOOGLE: "구글",
    KAKAO: "카카오",
    GITHUB: "깃허브",
    NAVER: "네이버",
    FACEBOOK: "페이스북"
}

export const DEFAULT_MEMBER = {
    id: null,
    name: "",
    email: "",
    provider: "",
    role: "",
}

export const OWNER_SETTLE_TYPE = {
    MONTH: "월정산",
    WEEK: "주정산",
    DAILY: "일정산"
}

export const ORDER_STATUS = {
    ORDER: "주문생성",
    ORDER_CONFIRM: "주문완료",
    DELIVERY: "배달중",
    DELIVERY_CONFIRM: "배달완료",
    CANCEL: "배달취소"
}

export const PAYMENT = {
    CARD: "카드 주문",
    MOBILE: "모바일 주문",
    COUPON: "쿠폰 사용",
    POINT: "포인트 사용",
    EMPTY: "없음"
}

export const OPTION = {
    BAEMIN_COUPON: "배달의민족 발행 쿠폰",
    OWNER_COUPON: "업주님 발행 쿠폰",
    EMPTY: "없음"
}

export const REWARD_TYPE = {
    ABUSING: "어뷰징",
    SYSTEM_ERROR: "시스템 오류",
    DELIVERY_ACCIDENT: "배달 사고",
    ETC: "기타",
}

export const SETTLE_STATUS = {
    CREATED: "미지급",
    COMPLETED: "지급완료",
    ALL: null,
}

export const SETTLE_TYPE = {
    DAILY: "일정산",
    WEEK: "주정산",
    MONTH: "월정산",
    "": "전체",
}

export const SIDEBAR_LIST = [
    {
        id: 1,
        toggle: true,
        name: "업주 관리",
        menuItems: [
            {
                id: 1,
                path: '/owners/search',
                icon: "search.svg",
                name: "업주 등록 및 조회",
            }
        ]
    },
    {
        id: 2,
        toggle: true,
        name: "주문 관리",
        menuItems: [
            {
                id: 2,
                icon: "add.svg",
                name: "주문 등록",
                path: "/orders/register"
            },
            {
                id: 3,
                icon: "search.svg",
                name: "주문 조회",
                path: "/orders/search"
            }
        ]
    },
    {
        id: 3,
        toggle: true,
        name: "보정금액 관리",
        menuItems: [
            {
                id: 4,
                path: '/rewards/register',
                icon: "add.svg",
                name: "보정금액 등록",
            },
            {
                id: 5,
                path: '/rewards/search',
                icon: "search.svg",
                name: "보정금액 조회",
            }
        ]
    },
    {
        id: 4,
        toggle: true,
        name: "지급금 관리",
        menuItems: [
            {
                id: 6,
                path: '/settles/search',
                icon: "search.svg",
                name: "지급금 등록 및 조회",
            }
        ]
    },
    {
        id: 5,
        toggle: true,
        name: "회원 관리",
        menuItems: [
            {
                id: 7,
                path: '/members/search',
                icon: "add.svg",
                name: "회원 조회",
            },
        ]
    },
]
