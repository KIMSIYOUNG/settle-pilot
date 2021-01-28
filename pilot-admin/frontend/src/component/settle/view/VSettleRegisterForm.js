import React from 'react';
import {Button, Form, Modal} from "react-bootstrap";
import {RightHorizontal} from "../../../util/CommonStyledComponents";

const VSettleRegisterForm = ({show, close, onChange, register, registerApi}) => {
    return (
        <Modal show={show} onHide={close}>
            <Modal.Header closeButton>
                <Modal.Title>지급금 생성</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                배치로 생성되지 않은 지급금이나️ 특정 업주에게 지급금을 생성해야하는
                <br/>
                ✔️ 예외적인 경우에만 ✔️ 사용해주세요.
                <br/><br/>
                <Form.Group>
                    <Form.Label>업주 번호</Form.Label>
                    <Form.Control id={"ownerId"} type={"number"} placeholder="필수사항"
                                  onChange={onChange}/>
                </Form.Group>

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

export default VSettleRegisterForm;