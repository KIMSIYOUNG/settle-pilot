import React from 'react';
import {Button, Form} from "react-bootstrap";
import {ButtonBox, DefaultSearchBarLayout, RightHorizontal} from "../../../util/CommonStyledComponents";

const VMemberSearchCondition = ({onChange, onClick}) => {
    return (
        <DefaultSearchBarLayout>
            <Form.Group>
                <Form.Label>회원 번호</Form.Label>
                <Form.Control id={"id"} type={"number"} placeholder="선택사항"
                              onChange={onChange}/>
            </Form.Group>
            <RightHorizontal/>
            <Form.Group>
                <Form.Label>회원 이름</Form.Label>
                <Form.Control id={"name"} type={"text"} placeholder="선택사항"
                              onChange={onChange}/>
            </Form.Group>
            <RightHorizontal/>

            <Form.Group>
                <Form.Label>회원 이메일</Form.Label>
                <Form.Control id={"email"} type={"text"} placeholder="선택사항"
                              onChange={onChange}/>
            </Form.Group>
            <RightHorizontal/>
            <ButtonBox>
                <Button variant="outline-success" onClick={() => onClick()}>검색</Button>
            </ButtonBox>
        </DefaultSearchBarLayout>
    )
}

export default VMemberSearchCondition;