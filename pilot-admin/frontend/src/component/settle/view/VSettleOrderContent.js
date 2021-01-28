import React from 'react';
import {DATE_TIME_CONVERTER, MONEY_DELIMITER, ORDER_STATUS} from "../../../Const";
import {Button, Table} from "react-bootstrap";

const VSettleOrderContent = ({orders, handleOrderShow}) => {
  return (
      <Table striped hover responsive>
          <thead>
          <tr>
              <th>주문 ID</th>
              <th>주문 번호</th>
              <th>총 금액</th>
              <th>주문 상태</th>
              <th>주문 일시</th>
              <th>주문 상세보기</th>
          </tr>
          </thead>
          <tbody>
          {orders && orders.orders.content.map(order => (
              <tr key={order.id}>
                  <td>{order.id}</td>
                  <td>{order.orderNo.substr(13, 20)}</td>
                  <td>{MONEY_DELIMITER(order.amount)}</td>
                  <td>{ORDER_STATUS[order.orderStatus]}</td>
                  <td>{DATE_TIME_CONVERTER(order.orderDateTime)}</td>
                  <td>
                      <Button size={"sm"} variant={"outline-info"}
                              onClick={() => handleOrderShow(order.orderDetails.orderDetails)}>주문상세보기</Button>
                  </td>
              </tr>
          ))}
          </tbody>
      </Table>
  )
};

export default VSettleOrderContent;