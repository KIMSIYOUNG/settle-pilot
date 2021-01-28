import React from 'react';
import {Button, Form, Modal} from "react-bootstrap";

const VOrderStatusUpdate = ({show, close, onChange, onClick}) => {
    return (
        <Modal show={show} onHide={close}>
            <Modal.Header closeButton>
                <Modal.Title>주문 상태 수정</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                주문 상태 수정하기.
                <br/><br/>
                <Form.Group>
                    <Form.Label type="text">주문상태</Form.Label>
                    <Form.Control as="select" id="orderStatus" onChange={(event) => onChange(event)}>
                        <option value="ORDER">주문요청</option>
                        <option value="ORDER_CONFIRM">주문완료</option>
                        <option value="DELIVERY">배달중</option>
                        <option value="DELIVERY_CONFIRM">배달완료</option>
                        <option value="CANCEL">배달취소</option>
                    </Form.Control>
                </Form.Group>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="primary" onClick={close}>
                    취소
                </Button>
                <Button variant="outline-success" onClick={onClick}>
                    수정
                </Button>
            </Modal.Footer>
        </Modal>

    )
}

export default VOrderStatusUpdate;