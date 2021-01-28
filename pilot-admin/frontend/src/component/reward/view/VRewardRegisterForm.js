import React from 'react';
import {Button, Form} from "react-bootstrap";
import styled from "styled-components";
import {ButtonBox, RightHorizontal} from "../../../util/CommonStyledComponents";

const VRewardRegisterForm = ({
                                 singleRewardShow,
                                 validated,
                                 onSubmit,
                                 setShowOwnerModal,
                                 handleRewardChange,
                                 reward
                             }) => {
    return (
        <>
            {singleRewardShow && <>
                <br/>
                ✔ 특정 업주에게 단일하게 보정금액을 생성하는 것들 의미합니다.
                <br/><br/>
                <FormContainer>
                    <Form noValidate validated={validated} onSubmit={onSubmit}>
                        <Form.Row>
                            <Form.Group>
                                <Form.Label type="number">업주번호
                                    <Button style={{marginLeft: "90px", paddingTop: "0px", paddingBottom: "0px"}}
                                            size={"sm"}
                                            variant={"outline-info"}
                                            onClick={() => setShowOwnerModal(true)}>검색</Button>
                                </Form.Label>
                                <Form.Control id={"ownerId"}
                                              onChange={handleRewardChange}
                                              value={reward.ownerId} required/>
                            </Form.Group>
                            <RightHorizontal/>
                            <Form.Group>
                                <Form.Label style={{width: "170px"}}>보정금액 종류</Form.Label>
                                <Form.Control as="select" id="rewardType" onChange={handleRewardChange} required>
                                    <option value="DELIVERY_ACCIDENT">배달 사고</option>
                                    <option value="SYSTEM_ERROR">시스템 오류</option>
                                    <option value="ABUSING">어뷰징</option>
                                    <option value="ETC">기타</option>
                                </Form.Control>
                            </Form.Group>
                        </Form.Row>
                        <Form.Row>
                            <Form.Group>
                                <Form.Label>보정일자</Form.Label>
                                <Form.Control type={"dateTime-local"} id="rewardDateTime" onChange={handleRewardChange}
                                              required/>
                            </Form.Group>
                        </Form.Row>
                        <Form.Row>
                            <Form.Group>
                                <Form.Label>보정금액</Form.Label>
                                <Form.Control type={"number"} id="amount" onChange={handleRewardChange} required/>
                            </Form.Group>
                        </Form.Row>
                        <Form.Row>
                            <Form.Label>보정사유</Form.Label>
                            <Form.Control id="description" as="textarea" rows={10} onChange={handleRewardChange}/>
                        </Form.Row>
                        <ButtonBox>
                            <Button type="submit" style={{borderRadius: "20px"}} variant={"outline-success"}>보정금액
                                등록</Button>
                        </ButtonBox>
                    </Form>
                </FormContainer>
            </>
            }
        </>
    )
}

export default VRewardRegisterForm;

const FormContainer = styled.div`
  display: flex;
  width: 100%;
  min-height: 600px;
`
