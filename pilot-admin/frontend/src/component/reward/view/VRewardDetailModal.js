import React from 'react';
import {Button, Form, Modal} from "react-bootstrap";
import {MONEY_DELIMITER} from "../../../Const";

const VRewardDetailModal = ({show, close, reward, handleRewardUpdateChange, patchRewardApi}) => {
    return (
        <Modal show={show} onHide={close}>
            <Modal.Header closeButton>
                <Modal.Title>보정금액 상세내역</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                보정금액에 대한 설명입니다.
                <br/><br/>
                <Form.Group>
                    <Form.Label type="text">보정금액 번호</Form.Label>
                    <Form.Control id={"nothing"} placeholder={reward.rewardNo} readOnly={true}/>
                </Form.Group>

                <Form.Group>
                    <Form.Label type="text">대상 업주</Form.Label>
                    <Form.Control id={"nothing"} placeholder={reward.ownerName} readOnly={true}/>
                </Form.Group>

                <Form.Group>
                    <Form.Label type="text">금액</Form.Label>
                    <Form.Control id={"amount"} placeholder={MONEY_DELIMITER(reward.amount)}
                                  onChange={event => handleRewardUpdateChange(event)}/>
                </Form.Group>

                <Form.Group>
                    <Form.Label type="text">타입</Form.Label>
                    <Form.Control id={"rewardType"} as={"select"}
                                  onChange={event => handleRewardUpdateChange(event)}>
                        <option value="DELIVERY_ACCIDENT">배달 사고</option>
                        <option value="ABUSING">어뷰징</option>
                        <option value="SYSTEM_ERROR">시스템 오류</option>
                        <option value="ETC">기타</option>
                    </Form.Control>
                </Form.Group>

                <Form.Group>
                    <Form.Label type="text">상세설명</Form.Label>
                    <Form.Control id={"description"} placeholder={reward.description}
                                  onChange={event => handleRewardUpdateChange(event)}/>
                </Form.Group>
                <div style={{height: "2px", backgroundColor: '#E9ECEF', marginBottom: "15px"}}/>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="primary" onClick={close}>
                    돌아가기
                </Button>
                <Button variant="outline-success" onClick={patchRewardApi}>
                    수정
                </Button>
            </Modal.Footer>
        </Modal>
    )
}

export default VRewardDetailModal;