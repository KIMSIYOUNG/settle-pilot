import React from 'react';
import {Button, Form} from "react-bootstrap";
import {RightHorizontal} from "../../../util/CommonStyledComponents";
import styled from "styled-components";

const VOrderRegisterDetailForm = ({orderDetails, handleOrderDetailChange, addDetail, removeDetail}) => {
    return (
        <>
            <OrderDetailLayout>
                결제정보
            </OrderDetailLayout>
            {orderDetails && orderDetails.map((orderDetail) =>
                <Form.Row style={{alignItems: "flex-end"}}>
                    <Form.Group>
                        <Form.Label>결제타입</Form.Label>
                        <Form.Control as="select" id="paymentType"
                                      onChange={(event) => handleOrderDetailChange(event, orderDetail.id)}
                                      value={orderDetail.paymentType} required>
                            <option value="CARD">카드</option>
                            <option value="MOBILE">모바일</option>
                            <option value="COUPON">쿠폰</option>
                            <option value="POINT">포인트</option>
                            <option value="EMPTY">없음</option>
                        </Form.Control>
                    </Form.Group>
                    <RightHorizontal/>
                    {orderDetail.paymentType === "COUPON" && (
                        <Form.Group>
                            <Form.Label>결제세부상태</Form.Label>
                            <Form.Control as="select" id="paymentOption"
                                          onChange={(event) => handleOrderDetailChange(event, orderDetail.id)}
                                          value={orderDetail.paymentOption}>
                                <option value="OWNER_COUPON">사장님 쿠폰</option>
                                <option value="BAEMIN_COUPON">배민 쿠폰</option>
                                <option value="EMPTY">없음</option>
                            </Form.Control>
                        </Form.Group>
                    )}
                    <RightHorizontal/>
                    <Form.Group>
                        <Form.Label>금액</Form.Label>
                        <Form.Control type={"number"} id="amount"
                                      onChange={(event) => handleOrderDetailChange(event, orderDetail.id)}
                                      value={orderDetail.amount}/>
                    </Form.Group>
                    <Form.Group>
                        <Button variant={"outline-info"} style={{marginLeft: "10px"}}
                                onClick={addDetail}>추가하기</Button>
                        <Button variant={"outline-danger"} style={{marginLeft: "10px"}}
                                onClick={() => removeDetail(orderDetail.id)}>삭제하기</Button>
                    </Form.Group>
                </Form.Row>
            )}
        </>)
}
export default VOrderRegisterDetailForm;

const OrderDetailLayout = styled.div`
  padding-top: 20px;
  padding-bottom: 25px;
  font-size: 25px;
  font-weight: 500;
`
