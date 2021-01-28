import React from 'react';
import {Button, Form} from "react-bootstrap";
import {ButtonBox, RightHorizontal} from "../../../util/CommonStyledComponents";
import styled from "styled-components";
import VOrderRegisterDetailForm from "./VOrderRegisterDetailForm";

const VOrderRegisterForm = ({
                                validated,
                                onSubmit,
                                isReOrder,
                                setShowOrderModal,
                                setShowOwnerModal,
                                order,
                                handleOrderChange,
                                handleDateTimeChange,
                                dateTime,
                                orderDetails,
                                handleOrderDetailChange,
                                addDetail,
                                removeDetail
                            }) => {
    return (
        <FormContainer>
            <Form noValidate validated={validated} onSubmit={onSubmit}>
                <Form.Row>
                    {isReOrder ?
                        <>
                            <Form.Group>
                                <Form.Label type="text" style={{color: "red"}}>기존 주문번호(취소)
                                    <Button style={{marginLeft: "50px", paddingTop: "0px", paddingBottom: "0px"}}
                                            size={"sm"} variant={"outline-info"}
                                            onClick={() => setShowOrderModal(true)}>검색</Button>
                                </Form.Label>
                                <Form.Control id={"orderNo"}
                                              value={order.orderNo}
                                              onChange={handleOrderChange}
                                              placeholder={"필수사항"} required/>
                            </Form.Group>
                        </>
                        :
                        <Form.Group>
                            <Form.Label type="number">업주번호
                                <Button style={{marginLeft: "120px", paddingTop: "0px", paddingBottom: "0px"}}
                                        size={"sm"} variant={"outline-info"}
                                        onClick={() => setShowOwnerModal(true)}>검색</Button>
                            </Form.Label>
                            <Form.Control id={"ownerId"}
                                          onChange={handleOrderChange}
                                          value={order.ownerId}
                                          placeholder={"필수사항"} required/>
                        </Form.Group>
                    }
                    <RightHorizontal/>
                    <Form.Group>
                        <Form.Label style={{width: "170px"}}>주문상태</Form.Label>
                        <Form.Control as="select" id="orderStatus" value={order.orderStatus}
                                      onChange={handleOrderChange} required>
                            <option value="ORDER">주문요청</option>
                            <option value="ORDER_CONFIRM">주문완료</option>
                            <option value="DELIVERY">배달중</option>
                            <option value="DELIVERY_CONFIRM">배달완료</option>
                            <option value="CANCEL">배달취소</option>
                        </Form.Control>
                    </Form.Group>
                </Form.Row>
                <Form.Row>
                    <Form.Group>
                        <Form.Label>주문일자</Form.Label>
                        <Form.Control type={"date"} id="date" onChange={handleDateTimeChange} value={dateTime.date}
                                      required/>
                    </Form.Group>
                    <RightHorizontal/>
                    <Form.Group>
                        <Form.Label style={{width: "170px"}}>주문시각</Form.Label>
                        <Form.Control type={"time"} id="time" onChange={handleDateTimeChange} value={dateTime.time}
                                      required/>
                    </Form.Group>
                </Form.Row>

                <VOrderRegisterDetailForm orderDetails={orderDetails}
                                          handleOrderDetailChange={handleOrderDetailChange}
                                          addDetail={addDetail}
                                          removeDetail={removeDetail}/>
                <ButtonBox>
                    <Button type="submit" style={{borderRadius: "20px"}} variant={"outline-success"}>주문
                        등록</Button>
                </ButtonBox>
            </Form>
        </FormContainer>
    )
}

export default VOrderRegisterForm;

const FormContainer = styled.div`
  display: flex;
  width: 100%;
  min-height: 600px;
`
