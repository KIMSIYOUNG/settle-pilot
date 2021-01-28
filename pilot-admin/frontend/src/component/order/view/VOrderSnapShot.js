import React from 'react';
import {Button, Modal, Table} from "react-bootstrap";
import {DATE_TIME_CONVERTER, ORDER_STATUS} from "../../../Const";
import {TableContent} from "../../../util/CommonStyledComponents";

const VOrderSnapShot = ({show, close, orderSnapShots}) => {
    return (
        <Modal show={show} onHide={close}>
            <Modal.Header closeButton>
                <Modal.Title>주문 상태 확인</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <br/>
                <TableContent>
                    <Table striped hover responsive>
                        <thead>
                        <tr>
                            <th>주문 번호</th>
                            <th>주문 상태</th>
                            <th>상태 시점</th>
                        </tr>
                        </thead>
                        <tbody>
                        {orderSnapShots.map(snapshot =>
                            <tr key={snapshot.orderId}>
                                <td>{snapshot.orderId}</td>
                                <td>{ORDER_STATUS[snapshot.orderStatus]}</td>
                                <td>{DATE_TIME_CONVERTER(snapshot.statusAt)}</td>
                            </tr>
                        )}
                        </tbody>
                    </Table>
                </TableContent>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="outline-success" onClick={close}>
                    확인
                </Button>
            </Modal.Footer>
        </Modal>
    )
}

export default VOrderSnapShot;