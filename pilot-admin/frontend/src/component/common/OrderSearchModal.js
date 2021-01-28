import React, {useEffect, useState} from 'react';
import {OrderAPI} from "../../util/api/OrderAPI";
import {Button, Form, Modal, Pagination, Table} from "react-bootstrap";
import {DATE_TIME_CONVERTER, MONEY_DELIMITER, ORDER_STATUS} from "../../Const";
import CustomPagination from "../../util/CustomPagination";
import styled from "styled-components";

const OrderSearchModal = ({show, onClose, onSelect}) => {
    const [orders, setOrders] = useState(null);
    const [search, setSearch] = useState({
        startAt: "",
        endAt: "",
    });

    const handleSearchChange = (event) => {
        setSearch({
            ...search,
            [event.target.id]: event.target.value
        })
    }

    const generateSearchRequest = () => {
        return {
            ...search,
            startAt: search.startAt === "" ? null : search.startAt + ":00",
            endAt: search.endAt === "" ? null : search.endAt + ":00",
        }
    }

    const fetchNextPage = async (nextPage) => {
        const response = await OrderAPI.getByCondition(generateSearchRequest(), nextPage, 5);
        setOrders(response);
    }

    useEffect(() => {
        fetchNextPage(0);
    }, [])

    return (
        <Modal show={show} onHide={onClose} size={"xl"}>
            <Modal.Header closeButton>
                <Modal.Title>주문정보 검색하기</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <div style={{display: "flex"}}>
                    <Form.Group>
                        <Form.Label>업주번호</Form.Label>
                        <Form.Control id={"ownerId"} type={"number"} placeholder="선택사항"
                                      onChange={event => handleSearchChange(event)}
                        />
                    </Form.Group>
                    <RightHorizontal/>

                    <Form.Group>
                        <Form.Label type="text">주문상태</Form.Label>
                        <Form.Control as="select" id="orderStatus" onChange={(event) => handleSearchChange(event)}>
                            <option value="">전체보기</option>
                            <option value="ORDER">주문요청</option>
                            <option value="ORDER_CONFIRM">주문완료</option>
                            <option value="DELIVERY">배달중</option>
                            <option value="DELIVERY_CONFIRM">배달완료</option>
                            <option value="CANCEL">배달취소</option>
                        </Form.Control>
                    </Form.Group>
                    <RightHorizontal/>

                    <Form.Group>
                        <Form.Label>시작일</Form.Label>
                        <Form.Control id={"startAt"} type={"dateTime-local"} placeholder="선택사항"
                                      onChange={(event) => handleSearchChange(event)}/>
                    </Form.Group>
                    <RightHorizontal/>

                    <Form.Group>
                        <Form.Label>종료일</Form.Label>
                        <Form.Control id={"endAt"} type={"dateTime-local"} placeholder="선택사항"
                                      onChange={(event) => handleSearchChange(event)}/>
                    </Form.Group>
                    <RightHorizontal/>
                    <ButtonBox>
                        <Button variant="outline-success"
                                onClick={() => fetchNextPage(0)}>검색</Button>
                    </ButtonBox>
                </div>

                <TableContent>
                    <Table striped hover responsive>
                        <thead>
                        <tr>
                            <th>업주 번호</th>
                            <th>주문 번호</th>
                            <th>주문 금액</th>
                            <th>주문 상태</th>
                            <th>주문 일시</th>
                        </tr>
                        </thead>
                        <tbody>
                        {orders && orders.orders.content.map(order => (
                            <tr key={order.id}
                                onClick={() => onSelect(order)}
                            >
                                <td>{order.owner.id}</td>
                                <td>{order.orderNo.substr(0, 23)}</td>
                                <td>{MONEY_DELIMITER(order.amount)}</td>
                                <td>{ORDER_STATUS[order.orderStatus]}</td>
                                <td>{DATE_TIME_CONVERTER(order.orderDateTime)}</td>
                            </tr>
                        ))}
                        </tbody>
                    </Table>
                </TableContent>
                <PageContainer>
                    {orders && <Pagination>
                        <CustomPagination fetchPage={fetchNextPage} current={orders.orders.pageable.pageNumber}
                                          total={orders.orders.totalPages}/></Pagination>}
                </PageContainer>
            </Modal.Body>
        </Modal>
    )
}

export default OrderSearchModal;

const ButtonBox = styled.div`
  margin-top: 30px;
`

const TableContent = styled.div`
  flex: 10;
`

const PageContainer = styled.div`
  flex: 6;
  display: flex;
  justify-content: center;
  padding-top: 10px;
`

const RightHorizontal = styled.div`
  padding-right: 20px;
`