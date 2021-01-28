import React, {useEffect, useState} from 'react';
import {OrderAPI} from "../../util/api/OrderAPI";
import DefaultMainLayout from "../../layout/DefaultMainLayout";
import {DEFAULT_ORDER_SEARCH} from "../../Const";
import VOrderDetailsModal from "./view/VOrderDetailsModal";
import VOrderSearchCondition from "./view/VOrderSearchCondition";
import VOrderSnapShot from "./view/VOrderSnapShot";
import VOrderStatusUpdate from "./view/VOrderStatusUpdate";
import VOrderContent from "./view/VOrderContent";
import VPageContainer from "../common/VPageContainer";

const OrderSearch = () => {
    const [orders, setOrders] = useState(null);
    const [orderDetails, setOrderDetails] = useState([])
    const [search, setSearch] = useState(DEFAULT_ORDER_SEARCH)
    const [orderSnapShots, setOrderSnapShots] = useState([]);
    const [orderStatus, setOrderStatus] = useState({});
    const [show, setShow] = useState(false);
    const [orderStatusShow, setOrderStatusShow] = useState(false);
    const [orderSnapShotShow, setOrderSnapShotShow] = useState(false);
    const [showOwnerModal, setShowOwnerModal] = useState(false);

    const generateSearchRequest = () => {
        return {
            ...search,
            startAt: search.startAt !== "" ? search.startAt + ":00" : null,
            endAt: search.endAt !== "" ? search.endAt + ":00" : null
        }
    }

    const handleShow = (orderDetails) => {
        setShow(true);
        setOrderDetails([...orderDetails])
    }

    const handleOrderSnapShotShow = (orderSnapShots) => {
        setOrderSnapShotShow(true);
        setOrderSnapShots([...orderSnapShots])
    }

    const handleOrderStatusShow = (order) => {
        setOrderStatusShow(true);
        setOrderStatus({
            id: order.id,
            orderStatus: order.orderStatus,
        })
    }

    const handleOrderChange = (event) => {
        setOrderStatus({...orderStatus, [event.target.id]: event.target.value});
    }

    const handleOwnerShowModal = (owner) => {
        setSearch({...search, ownerId: owner.id,})
        setShowOwnerModal(false);
    }

    const handleSearchChange = (event) => {
        setSearch({...search, [event.target.id]: event.target.value})
    };

    const fetchByCondition = async () => {
        const response = await OrderAPI.getByCondition(generateSearchRequest())
        setOrders(response)
    }

    const fetchNextPage = async (nextPage) => {
        const response = await OrderAPI.getByCondition(generateSearchRequest(), nextPage);
        setOrders(response);
    }

    const fetchOrderStatus = async () => {
        try {
            await OrderAPI.patchOrderStatus({
                id: orderStatus.id,
                status: orderStatus.orderStatus
            });
        } catch (ex) {
            alert("배달완료/취소 된 주문은 수정할 수 없어요.😭")
        }
        fetchNextPage();
        setOrderStatusShow(false);
    }

    const deleteOrder = async (event, id) => {
        await OrderAPI.delete(id);
        fetchNextPage();
    }

    useEffect(() => {
        fetchNextPage();
    }, [])

    return (
        <DefaultMainLayout title={"주문 조회"}>
            🙏 주문금액은 총 주문된 금액이 아닌, 정산 대금을 계산할 때 사용되는 금액입니다.(예를 들어 사장님 쿠폰은 금액에서 제외됨)
            <br/><br/>
            {orders && <>
                <VOrderSearchCondition search={search}
                                       onChange={handleSearchChange}
                                       onClick={fetchByCondition}
                                       init={() => setSearch(DEFAULT_ORDER_SEARCH)}
                                       selectOwnerModal={handleOwnerShowModal}
                                       showOwnerModal={showOwnerModal}
                                       setShowOwnerModal={setShowOwnerModal}/>

                <VOrderDetailsModal show={show}
                                    setShow={setShow}
                                    orderDetails={orderDetails}/>

                <VOrderSnapShot show={orderSnapShotShow}
                                close={() => setOrderSnapShotShow(false)}
                                orderSnapShots={orderSnapShots}/>

                <VOrderStatusUpdate show={orderStatusShow}
                                    close={() => setOrderStatusShow(false)}
                                    onChange={handleOrderChange}
                                    onClick={fetchOrderStatus}/>

                <VOrderContent orders={orders}
                               handleOrderDetailShow={handleShow}
                               handleOrderSnapShotShow={handleOrderSnapShotShow}
                               handleOrderStatusShow={handleOrderStatusShow}
                               deleteOrderApi={deleteOrder}/>

                <VPageContainer values={orders.orders}
                                fetchNextPage={fetchNextPage}/>
            </>}
        </DefaultMainLayout>
    )
}


export default OrderSearch;