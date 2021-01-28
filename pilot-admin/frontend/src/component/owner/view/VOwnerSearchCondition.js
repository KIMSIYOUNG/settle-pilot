import React from 'react';
import {Button, Form} from "react-bootstrap";
import {ButtonBox, DefaultSearchBarLayout, RightHorizontal} from "../../../util/CommonStyledComponents";

const VOwnerSearchCondition = ({search, onChange, init, onClick}) => {
    return (
        <DefaultSearchBarLayout>
            <Form.Group>
                <Form.Label>업주 번호</Form.Label>
                <Form.Control id={"ownerId"} type={"number"} placeholder="선택사항입니다."
                              value={search.ownerId}
                              onChange={onChange}/>
            </Form.Group>
            <RightHorizontal/>
            <Form.Group>
                <Form.Label>업주 이름</Form.Label>
                <Form.Control id={"name"} type={"text"} placeholder="선택사항입니다."
                              value={search.name}
                              onChange={onChange}/>
            </Form.Group>
            <RightHorizontal/>
            <Form.Group>
                <Form.Label>업주 이메일</Form.Label>
                <Form.Control id={"email"} type={"text"} placeholder="선택사항입니다."
                              value={search.email}
                              onChange={onChange}/>
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
            <ButtonBox>
                <Button variant="outline-danger" onClick={init}>
                    초기화
                </Button>
            </ButtonBox>
            <RightHorizontal/>
            <ButtonBox>
                <Button variant="outline-success" onClick={onClick}>검색</Button>
            </ButtonBox>
        </DefaultSearchBarLayout>

    )
}

export default VOwnerSearchCondition;