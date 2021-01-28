import React, {useCallback, useState} from 'react';
import DefaultMainLayout from "../../layout/DefaultMainLayout";
import styled from "styled-components";
import {OrderAPI} from "../../util/api/OrderAPI";
import OwnerSearchModal from "../common/OwnerSearchModal";
import OrderSearchModal from "../common/OrderSearchModal";
import {DEFAULT_DATE_TIME, DEFAULT_ORDER_REGISTER} from "../../Const";
import VOrderRegisterForm from "./view/VOrderRegisterForm";

const OrderRegister = () => {
    const [newOrder, setNewOrder] = useState(false);
    const [reOrder, setReOrder] = useState(false);
    const [nextId, setNextId] = useState(1)
    const [validated, setValidated] = useState(false);
    const [order, setOrder] = useState(DEFAULT_ORDER_REGISTER);
    const [orderDetails, setOrderDetails] = useState([{
        id: nextId,
        paymentType: "COUPON",
        paymentOption: "BAEMIN_COUPON",
        amount: 3_000,
    }]);
    const [dateTime, setDateTime] = useState(DEFAULT_DATE_TIME);
    const [showOwnerModal, setShowOwnerModal] = useState(false);
    const [showOrderModal, setShowOrderModal] = useState(false);

    const handleReOrder = () => {
        setReOrder(true);
        setNewOrder(false)
        alert("기존 주문이 취소되고, 새로운 주문이 생성됩니다.")
    }

    const handleNewOrder = () => {
        setReOrder(false);
        setNewOrder(true);
    }

    const handleOrderChange = (event) => {
        setOrder({...order, [event.target.id]: event.target.value})
    };

    const handleOrderDetailChange = (event, id) => {
        const newOrderDetails = orderDetails.map(
            o => o.id === id ? ({...o, [event.target.id]: event.target.value}) : o)

        setOrderDetails(newOrderDetails)
    };

    const handleDateTimeChange = (event) => {
        setDateTime({...dateTime, [event.target.id]: event.target.value})
    };

    const handleShowOwnerModal = (owner) => {
        setOrder({...order, ownerId: owner.id,})
        setShowOwnerModal(false)
    }

    const handleShowOrderModal = (selectedOrder) => {
        setOrder({...order, orderNo: selectedOrder.orderNo,})
        setShowOrderModal(false)
    }

    const addEmptyOrderDetail = () => {
        setNextId(nextId + 1);

        setOrderDetails([...orderDetails, {
            id: nextId + 1,
            paymentType: "CARD",
            paymentOption: "EMPTY",
            amount: 0
        }
        ])
    }

    const register = useCallback(
        async () => {
            const response = await OrderAPI.post({
                ...order,
                orderDateTime: dateTimeFormatter(),
                orderDetails: orderDetails.map((od) => {
                    delete od.id
                    return od
                }),
            });
            return response;
        }
        , [order, orderDetails, dateTime])

    const registerReOrder = async () => {
        const data = {
            orderNo: order.orderNo,
            orderDetails: orderDetails.map((od) => {
                delete od.id
                return od
            }),
            orderStatus: order.orderStatus,
            orderDateTime: dateTimeFormatter(),
        };

        await OrderAPI.reOrder(data)
    }

    const dateTimeFormatter = () => {
        return dateTime.date + "T" + dateTime.time + ":00"
    };

    const removeOrderDetail = (id) => {
        if (orderDetails.length === 1) {
            alert("하나이상의 결제수단을 등록해주세요!")
            return;
        }
        setOrderDetails(orderDetails.filter((orderDetail) => orderDetail.id !== id))
    }

    const handleSubmit = (event) => {
        const form = event.currentTarget;
        if (form.checkValidity() === false) {
            event.preventDefault();
            event.stopPropagation();
        } else {
            if (reOrder) {
                registerReOrder();
                return;
            }
            register();
            setOrder(DEFAULT_ORDER_REGISTER);
            setDateTime(DEFAULT_DATE_TIME);
        }
        setValidated(true);
    }

    return (
        <DefaultMainLayout title={"주문 등록"}>
            <OwnerSearchModal show={showOwnerModal}
                              onClose={() => setShowOwnerModal(false)}
                              onSelect={handleShowOwnerModal}/>
            <OrderSearchModal show={showOrderModal}
                              onClose={() => setShowOrderModal(false)}
                              onSelect={handleShowOrderModal}/>

            <SelectContainer>
                <NewOrder onClick={() => handleNewOrder()}>새로운 주문 생성</NewOrder>
                <ReOrder onClick={() => handleReOrder()}>주문취소 및 재생성</ReOrder>
            </SelectContainer>

            {(newOrder || reOrder) &&
            <VOrderRegisterForm order={order} dateTime={dateTime} orderDetails={orderDetails}
                                isReOrder={reOrder} validated={validated}
                                setShowOrderModal={setShowOrderModal}
                                setShowOwnerModal={setShowOwnerModal}
                                addDetail={addEmptyOrderDetail} removeDetail={removeOrderDetail}
                                handleDateTimeChange={handleDateTimeChange}
                                handleOrderChange={handleOrderChange}
                                handleOrderDetailChange={handleOrderDetailChange}
                                onSubmit={handleSubmit}/>
            }
        </DefaultMainLayout>
    )
}

export default OrderRegister;

const SelectContainer = styled.div`
  margin-bottom: 40px;
  height: 50px;
  width: 800px;
  display: flex;
  flex-direction: row;
`

const NewOrder = styled.div`
  justify-content: center;
  margin-right: 10px;
  display: flex;
  flex: 1;
  font-weight: 500;
  font-size: 18px;
  background-color: #ece6df;
  border-radius: 20px;

  &:hover {
    background-color: rgba(0, 0, 0, 0.1);
  }
`

const ReOrder = styled.div`
  justify-content: center;
  display: flex;
  margin-left: 10px;
  flex: 1;
  font-weight: 500;
  font-size: 18px;
  background-color: #f4e5d3;
  border-radius: 20px;

  &:hover {
    background-color: rgba(0, 0, 0, 0.1);
  }
`