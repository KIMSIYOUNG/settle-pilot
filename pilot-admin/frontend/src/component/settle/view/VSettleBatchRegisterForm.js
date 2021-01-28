import React from 'react';
import {Button, Form, Modal} from "react-bootstrap";
import {RightHorizontal} from "../../../util/CommonStyledComponents";

const VSettleBatchRegisterForm = ({show, close, onChange, register, registerApi}) => {
    return (
        <Modal show={show} onHide={close}>
            <Modal.Header closeButton>
                <Modal.Title>지급금 생성</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                ✔️ 업주별 지급금 유형에 따라 특정기간의 모든 업주에게 지급금을 생성하는 방식입니다. 현재 배치로 전환중에 있습니다.
                
                <br/><br/>

                <Form.Group>
                    <Form.Label>지급금 유형</Form.Label>
                    <Form.Control id={"settleType"} as="select" placeholder="선택사항"
                                  onChange={onChange}>
                        <option value="">전체</option>
                        <option value="DAILY">일정산</option>
                        <option value="WEEK">주정산</option>
                        <option value="MONTH">월정산</option>
                    </Form.Control>
                </Form.Group>

                {register.settleType === "DAILY" &&
                <Form.Group>
                    <Form.Label>날짜를 선택하세요.</Form.Label>
                    <Form.Control id={"criteriaDate"} type={"date"} placeholder="필수사항"
                                  onChange={onChange}/>
                </Form.Group>
                }

                {register.settleType === "WEEK" &&
                <Form.Group>
                    <Form.Group>
                        <Form.Label>정산 받을 주의, 아무 날짜를 선택해주세요.</Form.Label>
                        <Form.Control id={"criteriaDate"} type={"date"} placeholder="필수사항"
                                      onChange={onChange}/>
                    </Form.Group>
                </Form.Group>
                }

                {register.settleType === "MONTH" &&
                <Form.Group>
                    <Form.Label>정산 받을 월의, 아무 날짜를 선택해주세요.</Form.Label>
                    <Form.Control id={"criteriaDate"} type={"date"} placeholder="필수사항"
                                  onChange={onChange}/>
                </Form.Group>
                }
                <RightHorizontal/>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={close}>
                    취소
                </Button>
                <Button variant="primary" onClick={registerApi}>
                    등록
                </Button>
            </Modal.Footer>
        </Modal>
    )
}

export default VSettleBatchRegisterForm;