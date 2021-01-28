import axios from "axios";

axios.interceptors.response.use(
    function (response) {
        return response
    },
    function (error) {
        if (error.response) {
            if (error.response.status === 403) {
                alert("관리지만 사용할 수 있는 기능입니다. 정보보기 옆에 권한신청을 눌러주세요.😭");
                return;
            }
            if (error.response.status === 400) {
                if(error.response.data.errors) {
                    alert(error.response.data.errors[0].reason)
                    return;
                } else {
                    alert(error.response.data.message);
                    return;
                }
            }
            if (error.response.status === 404) {
                alert("존재하지 않는 업주입니다. 혹시 삭제하신건 아닌가요?😭 ");
                return;
            }
            if (error.response.status === 500) {
                alert("서버에서 문제가 생겼어요! 조금만 기다려주세요.")
                return;
            }
        }
        return Promise.reject(error);
    }
);

export default axios;