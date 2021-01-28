import React from 'react';
import {Button, Form, Modal} from "react-bootstrap";

const VOwnerUpdateModal = ({owner, show, close, onChange, onClick}) => {
    return (
        <Modal show={show} onHide={close}>
            <Modal.Header closeButton>
                <Modal.Title>업주 수정하기</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                아래의 업주 정보를 입력해주세요.
                <br/><br/>
                <Form.Group>
                    <Form.Label>업주 번호</Form.Label>
                    <Form.Control id={"id"} type={"number"} value={owner.id} readOnly={true}/>
                </Form.Group>

                <Form.Group>
                    <Form.Label>업주 이름</Form.Label>
                    <Form.Control id={"name"} type={"text"} placeholder={owner.name}
                                  onChange={onChange}/>
                </Form.Group>

                <Form.Group>
                    <Form.Label>업주 이메일</Form.Label>
                    <Form.Control id={"email"} type={"text"} placeholder={owner.email}
                                  onChange={onChange}/>
                </Form.Group>

                <Form.Group>
                    <Form.Label>지급금 유형</Form.Label>
                    <Form.Control id={"settleType"} as="select" placeholder="선택사항"
                                  value={owner.settleType}
                                  onChange={onChange}>
                        <option value="DAILY">일정산</option>
                        <option value="WEEK">주정산</option>
                        <option value="MONTH">월정산</option>
                    </Form.Control>
                </Form.Group>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={close}>
                    취소
                </Button>
                <Button variant="primary" onClick={onClick}>
                    수정
                </Button>
            </Modal.Footer>
        </Modal>
    )
};

export default VOwnerUpdateModal;