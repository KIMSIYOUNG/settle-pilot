import React, {useState} from 'react';
import DefaultMainLayout from "../../layout/DefaultMainLayout";
import styled from "styled-components";
import {RewardAPI} from "../../util/api/RewardAPI";
import OwnerSearchModal from "../common/OwnerSearchModal";
import VRewardRegisterForm from "./view/VRewardRegisterForm";
import VRewardPeriodRegisterForm from "./view/VRewardPeriodRegisterForm";

const RewardRegister = () => {
    const [singleRewardShow, setSingleRewardShow] = useState(false);
    const [periodRewardShow, setPeriodRewardShow] = useState(false);
    const [showOwnerModal, setShowOwnerModal] = useState(false);
    const [validated, setValidated] = useState(false);
    const [reward, setReward] = useState({
        rewardType: "DELIVERY_ACCIDENT"
    });

    const handleSingleRewardShow = () => {
        setSingleRewardShow(true);
        setPeriodRewardShow(false);

    }
    const handlePeriodRewardShow = () => {
        setSingleRewardShow(false);
        setPeriodRewardShow(true);

    }
    const handleRewardChange = (event) => {
        setReward({...reward, [event.target.id]: event.target.value})

    };
    const handleShowOwnerModal = (owner) => {
        setReward({...reward, ownerId: owner.id,})
        setShowOwnerModal(false);

    }
    const registerReward = async () => {
        try {
            const request = {
                ...reward,
                rewardDateTime: reward.rewardDateTime !== "" ? reward.rewardDateTime + ":00" : null,
            }
            const response = await RewardAPI.post(request)
            if (!response) {
                return;
            }
            if (response.status === 201) {
                alert("등록되었습니다.")
            }
        } catch (e) {
            if (e.response.status === 500) {
                alert("어뷰징은 - 금액만 등록이 가능합니다.")
            }
        }
    }

    const registerPeriodReward = async () => {
        await RewardAPI.postPeriodReward(reward);
    }

    const handleSubmit = (event) => {
        const form = event.currentTarget;
        if (form.checkValidity() === false) {
            event.preventDefault();
            setValidated(true);
        }

        if (singleRewardShow && !periodRewardShow) {
            registerReward();
            return;
        }

        if (periodRewardShow && !singleRewardShow) {
            registerPeriodReward();
            return;
        }
    }


    return (
        <DefaultMainLayout title={"보정금액 등록"}>
            <SelectContainer>
                <SingleReward onClick={handleSingleRewardShow}>단건 등록</SingleReward>
                <PeriodReward onClick={handlePeriodRewardShow}>특정 기간의 주문에 대한 등록</PeriodReward>
            </SelectContainer>

            <OwnerSearchModal show={showOwnerModal}
                              onClose={() => setShowOwnerModal(false)}
                              onSelect={handleShowOwnerModal}/>

            <VRewardRegisterForm singleRewardShow={singleRewardShow}
                                 validated={validated}
                                 onSubmit={handleSubmit}
                                 setShowOwnerModal={setShowOwnerModal}
                                 handleRewardChange={handleRewardChange}
                                 reward={reward}/>

            <VRewardPeriodRegisterForm periodRewardShow={periodRewardShow}
                                       validated={validated}
                                       onSubmit={handleSubmit}
                                       handleRewardChange={handleRewardChange}/>
        </DefaultMainLayout>
    )
}

export default RewardRegister;

const SelectContainer = styled.div`
  margin-bottom: 10px;
  height: 50px;
  width: 800px;
  display: flex;
  flex-direction: row;
`

const SingleReward = styled.div`
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

const PeriodReward = styled.div`
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
