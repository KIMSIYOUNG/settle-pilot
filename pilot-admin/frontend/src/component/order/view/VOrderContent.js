import React from 'react';
import {Button, Table} from "react-bootstrap";
import {DATE_TIME_CONVERTER, MONEY_DELIMITER, ORDER_STATUS} from "../../../Const";
import {TableContent} from "../../../util/CommonStyledComponents";

const VOrderContent = ({
                           orders,
                           handleOrderDetailShow,
                           handleOrderSnapShotShow,
                           handleOrderStatusShow,
                           deleteOrderApi
                       }) => {

    return (
        <TableContent>
            <Table striped hover responsive>
                <thead>
                <tr>
                    <th>업주 번호</th>
                    <th>주문 번호</th>
                    <th>주문 금액</th>
                    <th>주문 상태</th>
                    <th>주문 일시</th>
                    <th>결제 내역</th>
                    <th>주문 상태</th>
                    <th>주문 상태변경</th>
                    <th>주문 삭제</th>
                </tr>
                </thead>
                <tbody>
                {orders && orders.orders.content.map(order => (
                    <tr key={order.id}>
                        <td>{order.owner.id}</td>
                        <td>{order.orderNo.substr(0, 23)}</td>
                        <td>{MONEY_DELIMITER(order.amount)}</td>
                        <td>{ORDER_STATUS[order.orderStatus]}</td>
                        <td>{DATE_TIME_CONVERTER(order.orderDateTime)}</td>
                        <td><Button size={"sm"} variant={"outline-info"}
                                    onClick={() => handleOrderDetailShow(order.orderDetails.orderDetails)}>결제
                            내역</Button>
                        </td>
                        <td><Button size={"sm"} variant={"outline-info"}
                                    onClick={() => handleOrderSnapShotShow(order.orderSnapShots.snapshots)}>주문
                            상태</Button>
                        </td>
                        <td><Button size={"sm"} variant={"outline-success"}
                                    onClick={() => handleOrderStatusShow(order)}>주문 상태변경</Button></td>
                        <td><Button size={"sm"} variant={"outline-danger"}
                                    onClick={(event) => deleteOrderApi(event, order.id)}>주문 삭제</Button></td>
                    </tr>
                ))}
                </tbody>
            </Table>
        </TableContent>
    )
}

export default VOrderContent;