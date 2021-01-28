import React from 'react';
import {Button, Form} from "react-bootstrap";
import {ButtonBox, DefaultSearchBarLayout, RightHorizontal} from "../../../util/CommonStyledComponents";

const VSettleSearchCondition = ({search, setSearch, onChange, fetchPageApi, setOwnerShow}) => {
    return (
        <DefaultSearchBarLayout>
            <Form.Group>
                <Form.Label>업주 번호
                    <Button style={{marginLeft: "90px", paddingTop: "0px", paddingBottom: "0px"}}
                            size={"sm"} variant={"outline-info"}
                            onClick={() => setOwnerShow(true)}>검색</Button>
                </Form.Label>
                <Form.Control id={"ownerId"} type={"number"} placeholder="선택사항"
                              value={search?.ownerId ?? ""}
                              onChange={(event) => onChange(event)}/>
            </Form.Group>
            <RightHorizontal/>

            <Form.Group>
                <Form.Label>지급 상태</Form.Label>
                <Form.Control id={"settleStatus"} as="select" placeholder="선택사항"
                              value={search?.settleStatus ?? ""}
                              onChange={(event) => onChange(event)}>
                    <option value="">전체</option>
                    <option value="COMPLETED">지급완료</option>
                    <option value="CREATED">미지급</option>
                </Form.Control>
            </Form.Group>
            <RightHorizontal/>

            <Form.Group>
                <Form.Label>지급금 유형</Form.Label>
                <Form.Control id={"settleType"} as="select" placeholder="선택사항"
                              value={search?.settleType ?? ""}
                              onChange={(event) => onChange(event)}>
                    <option value="">전체</option>
                    <option value="DAILY">일정산</option>
                    <option value="WEEK">주정산</option>
                    <option value="MONTH">월정산</option>
                </Form.Control>
            </Form.Group>
            <RightHorizontal/>

            <Form.Group>
                <Form.Label>시작일</Form.Label>
                <Form.Control id={"startAt"} type={"date"} placeholder="선택사항"
                              value={search?.startAt ?? ""}
                              onChange={(event) => onChange(event)}/>
            </Form.Group>
            <RightHorizontal/>
            <Form.Group>
                <Form.Label>종료일</Form.Label>
                <Form.Control id={"endAt"} type={"date"} placeholder="선택사항"
                              value={search?.endAt ?? ""}
                              onChange={(event) => onChange(event)}/>
            </Form.Group>
            <RightHorizontal/>

            <ButtonBox>
                <Button variant="outline-danger" onClick={() => setSearch({startAt: "", endAt: "",})}>초기화</Button>
            </ButtonBox>
            <RightHorizontal/>
            <ButtonBox>
                <Button variant="outline-success" onClick={() => fetchPageApi()}>검색</Button>
            </ButtonBox>
        </DefaultSearchBarLayout>

    )
}

export default VSettleSearchCondition;