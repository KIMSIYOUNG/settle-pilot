import React, {useEffect, useState} from 'react';
import {OrderAPI} from "../../../util/api/OrderAPI";
import DefaultMainLayout from "../../../layout/DefaultMainLayout";
import VRewardDetailShow from "./VRewardDetailShow";
import VOrderDetailsModal from "../../order/view/VOrderDetailsModal";
import VSettleOrderContent from "./VSettleOrderContent";
import VPageContainer from "../../common/VPageContainer";

const VSettleDetail = ({match}) => {
    const {id} = match.params
    const [orders, setOrders] = useState(null)
    const [orderDetails, setOrderDetails] = useState([{
        paymentType: "",
        paymentOption: "",
        amount: "",
    }])
    const [orderShow, setOrderShow] = useState(false);

    const handleOrderShow = (orderDetails) => {
        setOrderShow(true);
        setOrderDetails([...orderDetails])
    }

    const fetchPage = async (nextPage) => {
        const response = await OrderAPI.getBySettleId(id, nextPage);
        setOrders(response);
    }

    useEffect(() => {
        fetchPage();
    }, [])


    return (
        <DefaultMainLayout title={"지급금 상세보기"}>
            {orders && <>
                <VOrderDetailsModal show={orderShow}
                                    setShow={setOrderShow}
                                    orderDetails={orderDetails}/>

                <VSettleOrderContent orders={orders}
                                     handleOrderShow={handleOrderShow}/>

                <VPageContainer values={orders.orders}
                                fetchNextPage={fetchPage}/>

                <div style={{paddingBottom: "200px"}}/>
                <VRewardDetailShow settleId={id}/>
            </>}
        </DefaultMainLayout>
    )
};

export default VSettleDetail;