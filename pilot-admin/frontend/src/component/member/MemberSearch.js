import React, {useEffect, useState} from 'react';
import DefaultMainLayout from "../../layout/DefaultMainLayout";
import {MemberAPI} from "../../util/api/MemberAPI";
import {AuthorityAPI} from "../../util/api/AuthorityAPI";
import VMemberAuthorityChangeModal from "./view/VMemberAuthorityChangeModal";
import VMemberSearchCondition from "./view/VMemberSearchCondition";
import VMemberContent from "./view/VMemberContent";
import VPageContainer from "../common/VPageContainer";

const MemberSearch = ({currentMember}) => {
    const [members, setMembers] = useState(null);
    const [search, setSearch] = useState(null);
    const [role, setRole] = useState(null);
    const [show, setShow] = useState(false);

    const handleShow = (member) => {
        setShow(true);
        if (member) {
            setRole({
                memberId: member.memberId,
                role: "NORMAL",
            })
        }
    }

    const handleSearchChange = (event) => {
        setSearch({...search, [event.target.id]: event.target.value})
    };

    const handleRoleChange = (event) => {
        setRole({...role, [event.target.id]: event.target.value})
    }

    const getMember = async (nextPage = 0) => {
        const response = await MemberAPI.getAll(search, nextPage);
        setMembers(response);
    }

    const fetchApproval = async (member) => {
        await AuthorityAPI.approve(member.authorityId);
        getMember();
    }

    const fetchReject = async (member) => {
        await AuthorityAPI.reject(member.authorityId);
        getMember();
    }

    const fetchChangeRole = async () => {
        await MemberAPI.patchRole(role).then();
        getMember();
        setShow(false);
    }

    const deleteMember = async (member) => {
        await MemberAPI.deleteByAdmin(member.memberId);
        getMember();
    }


    useEffect(() => {
        getMember();
    }, [])

    return (
        <DefaultMainLayout title={"회원 조회"}>
            {members && <>
                <VMemberAuthorityChangeModal show={show}
                                             handleShow={handleShow}
                                             onChange={handleRoleChange}
                                             onClose={() => setShow(false)}
                                             onClick={fetchChangeRole}/>

                <VMemberSearchCondition onChange={handleSearchChange}
                                        onClick={getMember}/>

                <VMemberContent members={members} currentMember={currentMember}
                                handleShow={handleShow}
                                fetchApproval={fetchApproval}
                                fetchReject={fetchReject}
                                deleteMember={deleteMember}/>

                <VPageContainer values={members.members} fetchNextPage={getMember}/>
            </>}
        </DefaultMainLayout>
    )
}

export default MemberSearch;