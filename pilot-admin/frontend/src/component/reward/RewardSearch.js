import React, {useEffect, useState} from 'react';
import DefaultMainLayout from "../../layout/DefaultMainLayout";
import {RewardAPI} from "../../util/api/RewardAPI";
import {REWARD_TYPE} from "../../Const";
import OwnerSearchModal from "../common/OwnerSearchModal";
import VRewardSearchCondition from "./view/VRewardSearchCondition";
import VRewardDetailModal from "./view/VRewardDetailModal";
import VRewardOrderModal from "./view/VRewardOrderModal";
import VRewardContent from "./view/VRewardContent";
import VPageContainer from "../common/VPageContainer";

const RewardSearch = () => {
    const [rewards, setRewards] = useState(null);
    const [reward, setReward] = useState({
        id: "",
        amount: "",
        ownerName: "",
        rewardType: "",
        description: "",
    })
    const [order, setOrder] = useState(null);
    const [rewardUpdate, setRewardUpdate] = useState(null);
    const [search, setSearch] = useState(null);
    const [showDetail, setShowDetail] = useState(false);
    const [orderShow, setOrderShow] = useState(false);
    const [ownerShow, setOwnerShow] = useState(false);

    const handleDetailShow = (event, reward) => {
        setShowDetail(true);
        setReward({
            id: reward.id,
            rewardNo: reward.rewardNo,
            amount: reward.amount,
            ownerName: reward.owner.name,
            rewardType: REWARD_TYPE[reward.rewardType],
            description: reward.description,
        })
        setRewardUpdate({
            amount: reward.amount,
            rewardType: reward.rewardType,
            description: reward.description,
        })
    }

    const handleRewardUpdateChange = (event) => {
        setRewardUpdate({...rewardUpdate, [event.target.id]: event.target.value});
    }

    const handleSearchChange = (event) => {
        setSearch({...search, [event.target.id]: event.target.value});
    };

    const handleOrderShow = (event, reward) => {
        setOrderShow(true);
        setOrder({
            ...order,
            orderNo: reward.order.orderNo,
            amount: reward.order.amount,
            orderDateTime: reward.order.orderDateTime,
            orderStatus: reward.order.orderStatus,
        });
    }

    const fetchReward = async (nextPage) => {
        const response = await RewardAPI.getByCondition(search, nextPage);
        setRewards(response);
    }

    const deleteReward = async (event, id) => {
        await RewardAPI.delete(id);
        fetchReward()
    }

    const handleSelect = (owner) => {
        setSearch({...search, ownerName: owner.name,})
        setOwnerShow(false);
    }

    const patchReward = async () => {
        try {
            await RewardAPI.put({
                ...rewardUpdate,
                id: reward.id,
                rewardType: rewardUpdate.rewardType,
            });
        } catch (e) {
            alert("어뷰징 금액은 -만 가능합니다.")
        }
        fetchReward()
        setShowDetail(false)
    }

    useEffect(() => {
        fetchReward();
    }, [])

    return (
        <DefaultMainLayout title={"보정금액 조회"}>
            {rewards && <>
                <OwnerSearchModal show={ownerShow}
                                  onClose={() => setOwnerShow(false)}
                                  onSelect={handleSelect}/>

                <VRewardSearchCondition search={search}
                                        setSearch={setSearch}
                                        handleSearchChange={handleSearchChange}
                                        handleOwnerShow={() => setOwnerShow(true)}
                                        fetchRewardApi={fetchReward}/>

                <VRewardDetailModal show={showDetail}
                                    close={() => setShowDetail(false)}
                                    handleRewardUpdateChange={handleRewardUpdateChange}
                                    reward={reward}
                                    patchRewardApi={patchReward}/>

                <VRewardOrderModal show={orderShow}
                                   close={() => setOrderShow(false)}
                                   order={order}/>

                <VRewardContent handleDetailShow={handleDetailShow}
                                handleOrderShow={handleOrderShow}
                                rewards={rewards}
                                deleteRewardApi={deleteReward}/>

                <VPageContainer values={rewards.rewards}
                                fetchNextPage={fetchReward}/>
            </>}
        </DefaultMainLayout>
    )
}


export default RewardSearch;