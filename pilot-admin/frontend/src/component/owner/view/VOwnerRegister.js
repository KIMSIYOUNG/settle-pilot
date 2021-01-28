import React from 'react';
import {Button, Form, Modal} from "react-bootstrap";
import styled from "styled-components";
import {RightHorizontal} from "../../../util/CommonStyledComponents";

const VOwnerRegister = ({show, close, onChange, onClick, registerOwnerApi}) => {
    return (
        <OwnerRegisterContainer>
            <Button style={{height: "40px"}} size={"sm"} variant="outline-success" onClick={onClick}>
                업주 등록하기!
            </Button>
            <Modal show={show} onHide={close}>
                <Modal.Header closeButton>
                    <Modal.Title>업주 등록</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    아래의 업주 정보를 입력해주세요.
                    <br/><br/>
                    <Form.Group>
                        <Form.Label>업주 이름</Form.Label>
                        <Form.Control id={"name"} type={"text"} placeholder="필수사항"
                                      onChange={onChange}/>
                    </Form.Group>

                    <br/>
                    <Form.Group>
                        <Form.Label>업주 이메일</Form.Label>
                        <Form.Control id={"email"} type={"text"} placeholder="필수사항"
                                      onChange={onChange}/>
                    </Form.Group>
                    <RightHorizontal/>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={close}>
                        취소
                    </Button>
                    <Button variant="primary" onClick={registerOwnerApi}>
                        등록
                    </Button>
                </Modal.Footer>
            </Modal>
        </OwnerRegisterContainer>
    )
}

export default VOwnerRegister;

const OwnerRegisterContainer = styled.div`
  flex: 4;
  display: flex;
  justify-content: flex-end;
  align-items: center;
`