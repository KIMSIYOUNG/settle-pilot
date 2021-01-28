import React, {useState} from 'react';
import styled from "styled-components";
import {useHistory} from "react-router-dom";
import {AUTH, DEFAULT_MEMBER, PROVIDER} from "../../Const";
import {Button as ReactButton, Form, Modal} from "react-bootstrap";
import {MemberAPI} from "../../util/api/MemberAPI";
import {AuthorityAPI} from "../../util/api/AuthorityAPI";


const MainHeader = ({member, changeMember}) => {
    const history = useHistory();
    const [show, setShow] = useState(false);
    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    const changeRole = async () => {
        try {
            if (window.confirm("관리자 권한을 신청하시겠습니까?")) {
                await AuthorityAPI.post({
                    memberId: member.id,
                    role: "ADMIN"
                })
                alert("요청하였습니다. 조금만 기다려주세요.")
            }
        } catch (e) {
            alert("요청에 실패하였습니다.")
        }
    }

    const logOut = () => {
        history.push("/login")
        changeMember(DEFAULT_MEMBER);
    }

    const deleteCurrentMember = async () => {
        await MemberAPI.deleteByCurrentMember().then(() => {
            history.push("/login")
            changeMember(DEFAULT_MEMBER);
        })
    }

    return (
        <>
            <Modal show={show} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>나의 정보 보기</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <div style={{display: "flex", justifyContent: "center"}}>
                        개인정보는 수정이 불가능합니다.
                    </div>
                    <br/>
                    <Form.Group>
                        <Form.Label>이름</Form.Label>
                        <Form.Control id={"name"} type={"text"} placeholder={member.name} readOnly={true}/>
                    </Form.Group>
                    <Form.Group>
                        <Form.Label>이메일</Form.Label>
                        <Form.Control id={"email"} type={"text"} placeholder={member.email} readOnly={true}/>
                    </Form.Group>
                    <Form.Group>
                        <Form.Label>인증기관</Form.Label>
                        <Form.Control id={"nothing"} type={"text"} placeholder={PROVIDER[member.provider]}
                                      readOnly={true}/>
                    </Form.Group>
                    <Form.Group>
                        <Form.Label>권한</Form.Label>
                        <Form.Control id={"nothing"} type={"text"} placeholder={AUTH[member.role]}
                                      readOnly={true}/>
                    </Form.Group>
                </Modal.Body>
                <Modal.Footer>
                    <ReactButton variant="outline-danger" onClick={deleteCurrentMember}>
                        회원탈퇴
                    </ReactButton>
                    <ReactButton variant="outline-secondary" onClick={handleClose}>
                        취소
                    </ReactButton>
                </Modal.Footer>
            </Modal>

            <HeaderTitle>
                정산어드민
            </HeaderTitle>
            <Information>
                <Greeting>
                    <GreetingMessage> {member.role === 'NORMAL' ? '우아한형제들' : '관리자'} {member.name}님
                        반갑습니다.</GreetingMessage>
                </Greeting>
                <Button onClick={handleShow}>정보보기</Button>
                {member.role === "NORMAL" &&
                <>
                    <Divider/>
                    <Button onClick={changeRole}>권한요청</Button>
                </>}
                <Divider/>
                <Button onClick={logOut}>로그아웃</Button>

            </Information>
        </>
    )
}

export default MainHeader;

const HeaderTitle = styled.div`
  flex: 1;
  display: flex;
  align-items: center;
  font-size: 22px;
  font-weight: 400;
  color: white;
  padding-left: 15px;
`

const Information = styled.div`
  display: flex;
  align-items: center;
  min-width: 400px;
  justify-content: flex-end;
  padding: 15px;
`

const Greeting = styled.div`
  display: flex;
  font-size: 16px;
  font-weight: 500;
  color: white;
  margin-right: 30px;
  align-items: center;
`

const GreetingMessage = styled.div`
  font-size: 16px;
  font-weight: 400;
  color: white;
  margin-right: 30px;
  margin-left: 10px;
`

const Button = styled.div`
  height: 50px;
  width: 50px;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 14px;
  font-weight: 400;
  color: white;

  &:hover {
    background-color: rgba(0, 0, 0, 0.1);
  }
`

const Divider = styled.div`
  margin-right: 10px;
`
