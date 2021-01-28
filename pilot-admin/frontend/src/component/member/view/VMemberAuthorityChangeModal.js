import React from 'react';
import {Button, Form, Modal} from "react-bootstrap";

const VMemberAuthorityChangeModal = ({show, handleShow, onChange, onClose, onClick}) => {

    return (
        <Modal show={show} onHide={handleShow}>
            <Modal.Header closeButton>
                <Modal.Title>회원 권한 수정</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                수정하려는 권한을 누르신 이후, 확인 버튼을 눌러주세요.
                <br/><br/>
                <Form.Group>
                    <Form.Control as="select" id="role" onChange={onChange}>
                        <option value="NORMAL">일반회원으로 변경</option>
                        <option value="ADMIN">관리자로 변경</option>
                    </Form.Control>
                </Form.Group>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="primary" onClick={onClose}>
                    취소
                </Button>
                <Button variant="outline-success" onClick={onClick}>
                    확인
                </Button>
            </Modal.Footer>
        </Modal>
    )
}

export default VMemberAuthorityChangeModal;