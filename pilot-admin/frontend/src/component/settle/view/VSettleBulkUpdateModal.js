import React from 'react';
import {Button, Form, Modal} from "react-bootstrap";
import {RightHorizontal} from "../../../util/CommonStyledComponents";

const VSettleBulkUpdateModal = ({show, close, onChange, onClick}) => {
    return (
        <Modal show={show} onHide={close}>
            <Modal.Header closeButton>
                <Modal.Title>지급금 일괄 완료처리</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                ✔️ 지급금을 일괄적으로 지급완료 처리하는 화면입니다. <br/><br/>
                ✔️ 조건에 해당하는 지급금이 모두 완료처리 됩니다. 주의해주세요!
                <br/><br/>
                <Form.Group>
                    <Form.Label>업주 번호</Form.Label>
                    <Form.Control id={"ownerId"} type={"number"} placeholder="선택사항"
                                  onChange={onChange}/>
                </Form.Group>

                <Form.Group>
                    <Form.Label>지급금 범위 - 시작일</Form.Label>
                    <Form.Control id={"startAt"} type={"date"} placeholder="선택사항"
                                  onChange={onChange}/>
                </Form.Group>

                <Form.Group>
                    <Form.Label>지급금 범위 - 종료일</Form.Label>
                    <Form.Control id={"endAt"} type={"date"} placeholder="선택사항"
                                  onChange={onChange}/>
                </Form.Group>
                <RightHorizontal/>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={close}>
                    돌아가기
                </Button>
                <Button variant="primary" onClick={onClick}>
                    지급완료
                </Button>
            </Modal.Footer>
        </Modal>

    )
}

export default VSettleBulkUpdateModal;