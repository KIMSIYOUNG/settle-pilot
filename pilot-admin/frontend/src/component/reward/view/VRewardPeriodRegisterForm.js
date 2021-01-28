import React from 'react';
import {Button, Form} from "react-bootstrap";
import {ButtonBox, RightHorizontal} from "../../../util/CommonStyledComponents";
import styled from "styled-components";

const VRewardPeriodRegisterForm = ({periodRewardShow, validated, onSubmit, handleRewardChange}) => {
    return (
        <>
            {(periodRewardShow) && <>
                <br/>
                ✔️ 시작일 ~ 종료일에 발생한 모든 주문에 대한 보정금액을 생성합니다.
                <br/><br/>
                <FormContainer>

                    <Form noValidate validated={validated} onSubmit={onSubmit}>
                        <Form.Row>
                            <Form.Group>
                                <Form.Label>시작일</Form.Label>
                                <Form.Control type={"dateTime-local"} id="startAt" onChange={handleRewardChange}
                                              required/>
                            </Form.Group>
                            <RightHorizontal/>
                            <Form.Group>
                                <Form.Label>종료일</Form.Label>
                                <Form.Control type={"dateTime-local"} id="endAt" onChange={handleRewardChange}
                                              required/>
                            </Form.Group>
                            <RightHorizontal/>

                            <Form.Group>
                                <Form.Label>보정 예정일</Form.Label>
                                <Form.Control type={"dateTime-local"} id="rewardDateTime"
                                              onChange={handleRewardChange}
                                              required/>
                            </Form.Group>
                        </Form.Row>

                        <Form.Group>
                            <Form.Label style={{width: "170px"}}>보정금액 종류</Form.Label>
                            <Form.Control as="select" id="rewardType" onChange={handleRewardChange} required>
                                <option value="DELIVERY_ACCIDENT">배달 사고</option>
                                <option value="SYSTEM_ERROR">시스템 오류</option>
                                <option value="ABUSING">어뷰징</option>
                                <option value="ETC">기타</option>
                            </Form.Control>
                        </Form.Group>
                        <Form.Row>
                            <Form.Label>보정사유</Form.Label>
                            <Form.Control id="description" as="textarea" rows={6} onChange={handleRewardChange}/>
                        </Form.Row>
                        <ButtonBox>
                            <Button type="submit" style={{borderRadius: "20px"}} variant={"outline-success"}>보정금액
                                등록</Button>
                        </ButtonBox>
                    </Form>
                </FormContainer>
            </>}
        </>
    )
}

export default VRewardPeriodRegisterForm;

const FormContainer = styled.div`
display: flex;
width: 100%;
min-height: 600px;
`
