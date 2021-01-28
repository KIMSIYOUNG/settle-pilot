import React from 'react';
import {Button, Form, Modal} from "react-bootstrap";
import {MONEY_DELIMITER, OPTION, PAYMENT} from "../../../Const";

const VOrderDetailsModal = ({show, setShow, orderDetails}) => {
    return (
        <Modal show={show} onHide={() => setShow(false)}>
            <Modal.Header closeButton>
                <Modal.Title>주문 상세</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                주문에 대한 결제내역입니다.
                <br/><br/>
                {orderDetails.map(od =>
                    <>
                        <Form.Group>
                            <Form.Label type="text">결제정보</Form.Label>
                            <Form.Control id={"paymentType"} value={PAYMENT[od.paymentType]} readOnly={true}/>
                        </Form.Group>

                        <Form.Group>
                            <Form.Label type="text">결제정보상세</Form.Label>
                            <Form.Control id={"paymentOption"} value={OPTION[od.paymentOption]}
                                          readOnly={true}/>
                        </Form.Group>

                        <Form.Group>
                            <Form.Label type="text">금액</Form.Label>
                            <Form.Control id={"amount"} value={MONEY_DELIMITER(od.amount)} readOnly={true}/>
                        </Form.Group>
                        <div style={{height: "2px", backgroundColor: '#E9ECEF', marginBottom: "15px"}}/>
                    </>
                )}
            </Modal.Body>
            <Modal.Footer>
                <Button variant="primary" onClick={() => setShow(false)}>
                    확인
                </Button>
            </Modal.Footer>
        </Modal>
    )
}

export default VOrderDetailsModal;