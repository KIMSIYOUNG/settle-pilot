import React, {useEffect, useState} from 'react';
import DefaultMainLayout from "../../layout/DefaultMainLayout";
import styled from "styled-components";
import {SettleAPI} from "../../util/api/SettleAPI";
import {useHistory} from "react-router-dom";
import OwnerSearchModal from "../common/OwnerSearchModal";
import VSettleAmount from "./view/VSettleAmount";
import VSettleSearchCondition from "./view/VSettleSearchCondition";
import VSettleContent from "./view/VSettleContent";
import VPageContainer from "../common/VPageContainer";
import VSettleBulkUpdate from "./view/VSettleBulkUpdate";
import VSettleCurrentPageComplete from "./view/VSettleCurrentPageComplete";
import VSettleCreateButton from "./view/VSettleCreateButton";
import VSettleBulkUpdateModal from "./view/VSettleBulkUpdateModal";
import VSettleRegisterForm from "./view/VSettleRegisterForm";
import {DEFAULT_SETTLE_AMOUNT_PERIOD} from "../../Const";
import VSettleBatchRegisterForm from "./view/VSettleBatchRegisterForm";


const SettleSearch = () => {
    const history = useHistory();
    const [settleSnapshot, setSettleSnapshot] = useState({
        amount: "",
        startAt: "",
        endAt: "",
    })
    const [settles, setSettles] = useState(null);
    const [search, setSearch] = useState({
        startAt: "",
        endAt: "",
    });
    const [register, setRegister] = useState({})
    const [bulkUpdate, setBulkUpdate] = useState({})
    const [show, setShow] = useState(false);
    const [batchShow, setBatchShow] = useState(false);
    const [bulkUpdateShow, setBulkUpdateShow] = useState(false);
    const [ownerShow, setOwnerShow] = useState(false);

    const generateSearchRequest = () => {
        return {
            ...search,
            startAt: search.startAt === "" ? null : search.startAt + "T00:00:00",
            endAt: search.endAt === "" ? null : search.endAt + "T00:00:00",
        }
    }
    const handleBulkUpdateShow = () => {
        setBulkUpdateShow(true)
        setBulkUpdate({})

    }
    const handleRegisterChange = (event) => {
        setRegister({...register, [event.target.id]: event.target.value})
    }
    const handleSearchChange = (event) => {
        setSearch({...search, [event.target.id]: event.target.value})

    };
    const handleBulkUpdateChange = (event) => {
        setBulkUpdate({...bulkUpdate, [event.target.id]: event.target.value})
    };

    const handleOwnerSearchShow = (owner) => {
        setSearch({...search, ownerId: owner.id})
        setOwnerShow(false);

    }

    const redirectSettleDetail = (id) => {
        history.push(`/settle/details/${id}`)
    }
    const fetchAmount = async () => {
        const response = await SettleAPI.getAmount(DEFAULT_SETTLE_AMOUNT_PERIOD);
        console.log(response)
        setSettleSnapshot(response)
    }

    const registerSettle = async () => {
        await SettleAPI.post({
            ...register,
            startAt: register.startAt !== "" ? register.startAt + ":00" : null,
            endAt: register.endAt !== "" ? register.endAt + ":00" : null
        })
        fetchPage();
        setRegister({});
        setShow(false);
    }

    const registerBatch = async () => {
        await SettleAPI.registerBatch({...register})
        setRegister({});
        setBatchShow(false);
        fetchPage();
    }

    const fetchPage = async (nextPage) => {
        const response = await SettleAPI.getByCondition(generateSearchRequest(), nextPage);
        fetchAmount(generateSearchRequest())
        setSettles(response);

    }

    const completeSettle = async (id) => {
        await SettleAPI.patchStatus({
            id: id,
            settleStatus: "COMPLETED",
        })
        fetchPage(settles.settles.pageable.pageNumber);
    }

    const patchBulkUpdate = async () => {
        let message;
        message = bulkUpdate.ownerId ? `업주 번호 ${bulkUpdate.ownerId}번에 대해 ` : "모든 업주에 대해";
        message = bulkUpdate.startAt ? `${message} ${bulkUpdate.startAt}부터 ` : `${message} 과거 모든 지급금부터 `;
        message = bulkUpdate.endAt ? `${message} ${bulkUpdate.endAt}까지 모든 지급금을 완료처리 하시겠습니까? ` : `${message} 현재까지의 모든 지급금을 완료처리 하시겠습니까?`;

        if (window.confirm(message)) {
            await SettleAPI.updateBulkWithCondition({
                ownerId: bulkUpdate.ownerId,
                startAt: bulkUpdate.startAt ? bulkUpdate.startAt + "T00:00:00" : null,
                endAt: bulkUpdate.endAt ? bulkUpdate.endAt + "T00:00:00" : null
            })
            setBulkUpdate({});
            fetchPage(settles.settles.pageable.pageNumber)
        }
        setBulkUpdateShow(false);
    }

    const updateCurrentPageSettleComplete = async () => {
        if (window.confirm("현재 페이지에 있는 지급금을 모두 완료처리 하시겠습니까?")) {
            const ids = settles.settles.content.map(settle => settle.id);
            await SettleAPI.updateBulkWithCondition({
                settleIds: ids
            })
            fetchPage(settles.settles.pageable.pageNumber)
        }
    }

    useEffect(() => {
        fetchPage();
    }, [])

    return (
        <DefaultMainLayout title={"지급금 등록 및 조회"}>
            {settles && <>
                <OwnerSearchModal show={ownerShow}
                                  onClose={() => setOwnerShow(false)}
                                  onSelect={handleOwnerSearchShow}/>
                <VSettleAmount settleSnapshot={settleSnapshot}/>
                <VSettleSearchCondition search={search}
                                        setSearch={setSearch}
                                        onChange={handleSearchChange}
                                        setOwnerShow={setOwnerShow}
                                        fetchPageApi={fetchPage}/>
                <VSettleContent settles={settles}
                                redirectSettleDetail={redirectSettleDetail}
                                completeSettle={completeSettle}/>
                <VPageContainer values={settles.settles}
                                fetchNextPage={fetchPage}/>

                <SettleRegisterContainer>
                    <VSettleBulkUpdate handleBulkUpdateShow={handleBulkUpdateShow}/>
                    <VSettleCurrentPageComplete updateCurrentPageSettleComplete={updateCurrentPageSettleComplete}/>
                    <VSettleCreateButton handleShow={() => setShow(true)} title={"업주별 지급금 생성"}/>
                    <VSettleCreateButton handleShow={() => setBatchShow(true)} title={"기간별 지급금 생성"}/>
                    <VSettleBulkUpdateModal show={bulkUpdateShow}
                                            close={() => setBulkUpdateShow(false)}
                                            onChange={handleBulkUpdateChange}
                                            onClick={patchBulkUpdate}/>
                    <VSettleRegisterForm show={show}
                                         close={() => setShow(false)}
                                         onChange={handleRegisterChange}
                                         register={register}
                                         registerApi={registerSettle}/>
                    <VSettleBatchRegisterForm show={batchShow}
                                              close={() => setBatchShow(false)}
                                              onChange={handleRegisterChange}
                                              register={register}
                                              registerApi={registerBatch}/>
                </SettleRegisterContainer>
            </>}
        </DefaultMainLayout>
    )
}

export default SettleSearch;

const SettleRegisterContainer = styled.div`
  flex: 4;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding-bottom: 10px;
`