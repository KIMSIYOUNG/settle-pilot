import React from 'react';
import {Button, Table} from "react-bootstrap";
import {AUTH} from "../../../Const";
import {TableContent} from "../../../util/CommonStyledComponents";

const VMemberContent = ({members, currentMember, fetchApproval, fetchReject, deleteMember, handleShow}) => {
    return (
        <TableContent>
            <Table hover responsive>
                <thead>
                <tr>
                    <th>회원 번호</th>
                    <th>회원 이름</th>
                    <th>회원 이메일</th>
                    <th>현재 권한</th>
                    <th>권한변경</th>
                    <th>요청권한</th>
                    <th>권한승인</th>
                    <th>권한거절</th>
                    <th>회원삭제</th>
                </tr>
                </thead>
                <tbody>
                {members && members.members.content
                    .filter(member => {
                        return member.memberId !== currentMember.id
                    })
                    .map(member => (
                        <tr key={member.memberId}>
                            <td>{member.memberId}</td>
                            <td>{member.name}</td>
                            <td>{member.email}</td>
                            <td>{AUTH[member.role]}</td>
                            <td><Button size={"sm"} variant={"outline-info"}
                                        onClick={() => handleShow(member)}>권한변경</Button></td>
                            {member.authorityId && <>
                                <td>{AUTH[member.target]}</td>
                                <td><Button size={"sm"} variant={"outline-success"}
                                            onClick={() => fetchApproval(member)}>승인</Button></td>
                                <td><Button size={"sm"} variant={"outline-danger"}
                                            onClick={() => fetchReject(member)}>거절</Button></td>
                                <td><Button size={"sm"} variant={"outline-danger"}
                                            onClick={() => deleteMember(member)}>회원삭제</Button></td>
                            </>
                            }
                        </tr>
                    ))}
                </tbody>
            </Table>
        </TableContent>
    )
}

export default VMemberContent;