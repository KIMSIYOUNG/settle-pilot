import React from 'react';
import {Button, Form, Modal} from "react-bootstrap";
import {DATE_TIME_CONVERTER, MONEY_DELIMITER, ORDER_STATUS} from "../../../Const";

const VRewardOrderModal = ({show, close, order}) => {
    return (
        <Modal show={show} onHide={close}>
            <Modal.Header closeButton>
                <Modal.Title>관련 주문 내역</Modal.Title>
            </Modal.Header>
            {order &&
            <Modal.Body>
                보정금액과 관련된 주문에 대한 정보입니다.
                <br/><br/>
                <Form.Group>
                    <Form.Label type="text">주문 번호</Form.Label>
                    <Form.Control id={"nothing"} placeholder={order.orderNo} readOnly={true}/>
                </Form.Group>

                <Form.Group>
                    <Form.Label type="text">주문 금액</Form.Label>
                    <Form.Control id={"nothing"} placeholder={MONEY_DELIMITER(order.amount)} readOnly={true}/>
                </Form.Group>

                <Form.Group>
                    <Form.Label type="text">주문 상태</Form.Label>
                    <Form.Control id={"amount"} placeholder={ORDER_STATUS[order.orderStatus]} readOnly={true}/>
                </Form.Group>

                <Form.Group>
                    <Form.Label type="text">주문 시각</Form.Label>
                    <Form.Control id={"amount"} placeholder={DATE_TIME_CONVERTER(order.orderDateTime)}
                                  readOnly={true}/>
                </Form.Group>
                <div style={{height: "2px", backgroundColor: '#E9ECEF', marginBottom: "15px"}}/>
            </Modal.Body>
            }
            <Modal.Footer>
                <Button variant="primary" onClick={close}>
                    확인
                </Button>
            </Modal.Footer>
        </Modal>
    )
}

export default VRewardOrderModal;