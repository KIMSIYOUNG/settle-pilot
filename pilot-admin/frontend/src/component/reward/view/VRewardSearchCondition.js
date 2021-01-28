import React from 'react';
import {Button, Form} from "react-bootstrap";
import {ButtonBox, DefaultSearchBarLayout, RightHorizontal} from "../../../util/CommonStyledComponents";

const VRewardSearchCondition = ({search, setSearch, handleSearchChange, handleOwnerShow, fetchRewardApi}) => {
    return (
        <DefaultSearchBarLayout>
            <RightHorizontal/>
            <Form.Group>
                <Form.Label>업주 이름
                    <Button style={{marginLeft: "90px", paddingTop: "0px", paddingBottom: "0px"}}
                            size={"sm"} variant={"outline-info"}
                            onClick={handleOwnerShow}>검색</Button>
                </Form.Label>
                <Form.Control id={"ownerName"} type={"text"} placeholder="선택사항"
                              onChange={(event) => handleSearchChange(event)}
                              value={search?.ownerName ?? ""}/>
            </Form.Group>
            <RightHorizontal/>

            <Form.Group>
                <Form.Label>보정금액 번호</Form.Label>
                <Form.Control id={"rewardNo"} type={"text"} placeholder="선택사항"
                              onChange={(event) => handleSearchChange(event)}
                              value={search?.rewardNo ?? ""}
                />
            </Form.Group>
            <RightHorizontal/>

            <Form.Group>
                <Form.Label type="text">보정 타입</Form.Label>
                <Form.Control as="select" id="rewardType"
                              onChange={(event) => handleSearchChange(event)}
                              value={search?.rewardType ?? ""}
                >
                    <option value="">전체보기</option>
                    <option value="DELIVERY_ACCIDENT">배달 사고</option>
                    <option value="SYSTEM_ERROR">시스템 오류</option>
                    <option value="ABUSING">어뷰징</option>
                    <option value="ETC">기타</option>
                </Form.Control>
            </Form.Group>
            <RightHorizontal/>

            <ButtonBox>
                <Button variant="outline-danger" onClick={() => setSearch(null)}>초기화</Button>
            </ButtonBox>
            <RightHorizontal/>
            <ButtonBox>
                <Button variant="outline-success" onClick={() => fetchRewardApi()}>검색</Button>
            </ButtonBox>
        </DefaultSearchBarLayout>
    )
}

export default VRewardSearchCondition;