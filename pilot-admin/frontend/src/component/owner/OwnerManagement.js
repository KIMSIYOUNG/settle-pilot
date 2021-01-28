import React, {useEffect, useState} from 'react';
import DefaultMainLayout from "../../layout/DefaultMainLayout";
import {OwnerAPI} from "../../util/api/OwnerAPI";
import {DEFAULT_OWNER_SEARCH} from "../../Const";
import VOwnerSearchCondition from "./view/VOwnerSearchCondition";
import VOwnerUpdateModal from "./view/VOwnerUpdateModal";
import VOwnerContent from "./view/VOwnerContent";
import VPageContainer from "../common/VPageContainer";
import VOwnerRegister from "./view/VOwnerRegister";


const OwnerManagement = () => {
    const [owner, setOwner] = useState({});
    const [owners, setOwners] = useState(null);
    const [search, setSearch] = useState(DEFAULT_OWNER_SEARCH)
    const [register, setRegister] = useState({})
    const [modify, setModify] = useState({})
    const [show, setShow] = useState(false);
    const [modifyShow, setModifyShow] = useState(false);

    const handleModifyShow = (owner) => {
        setModifyShow(true);
        setOwner({...owner})
        setModify({
            id: owner.id,
            name: owner.name,
            email: owner.email,
            settleType: owner.settleType,
        })
    }

    const handleRegisterChange = (event) => {
        setRegister({...register, [event.target.id]: event.target.value})
    }

    const handleSearchChange = (event) => {
        setSearch({...search, [event.target.id]: event.target.value})
    };

    const handleModifyChange = (event) => {
        setModify({...modify, [event.target.id]: event.target.value})
    }

    const registerOwner = async () => {
        await OwnerAPI.post(register)
        fetchNextPage()
        setShow(false);
    }

    const modifyOwner = async () => {
        await OwnerAPI.put({
            id: owner.id,
            name: modify.name,
            email: modify.email,
            settleType: modify.settleType
        });
        fetchNextPage()
        setModifyShow(false)
    }

    const deleteOwner = async (id) => {
        await OwnerAPI.delete(id);
        fetchNextPage()
    }

    const fetchNextPage = async (nextPage) => {
        const response = await OwnerAPI.getByCondition(search, nextPage);
        setOwners(response);
    }

    useEffect(() => {
        fetchNextPage();
    }, [])

    return (
        <DefaultMainLayout title={"업주 등록 및 조회"}>
            {owners && <>
                <VOwnerSearchCondition search={search}
                                       onChange={handleSearchChange}
                                       init={() => setSearch(DEFAULT_OWNER_SEARCH)}
                                       onClick={() => fetchNextPage()}/>

                <VOwnerUpdateModal owner={owner}
                                   show={modifyShow}
                                   close={() => setModifyShow(false)}
                                   onChange={handleModifyChange}
                                   onClick={modifyOwner}
                />

                <VOwnerContent owners={owners}
                               handleModifyShow={handleModifyShow}
                               deleteOwnerApi={deleteOwner}/>

                <VPageContainer values={owners.owners}
                                fetchNextPage={fetchNextPage}/>

                <VOwnerRegister show={show}
                                close={() => setShow(false)}
                                onClick={() => setShow(true)}
                                onChange={handleRegisterChange}
                                registerOwnerApi={registerOwner}/>
            </>}
        </DefaultMainLayout>
    )
}

export default OwnerManagement;