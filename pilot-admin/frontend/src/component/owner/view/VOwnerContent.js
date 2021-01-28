import React from 'react';
import {Button, Table} from "react-bootstrap";
import {TableContent} from "../../../util/CommonStyledComponents";
import {OWNER_SETTLE_TYPE} from "../../../Const";

const VOwnerContent = ({owners, handleModifyShow, deleteOwnerApi}) => {
    return (
        <TableContent>
            <Table striped hover responsive>
                <thead>
                <tr>
                    <th>업주 번호</th>
                    <th>업주 이름</th>
                    <th>업주 이메일</th>
                    <th>업주 정산유형</th>
                    <th>업주 수정</th>
                    <th>업주 삭제</th>
                </tr>
                </thead>
                <tbody>
                {owners.owners.content.map(owner => (
                    <tr key={owner.id}>
                        <td>{owner.id}</td>
                        <td>{owner.name}</td>
                        <td>{owner.email}</td>
                        <td>{OWNER_SETTLE_TYPE[owner.settleType]}</td>
                        <td>
                            <Button size={"sm"} variant={"outline-info"}
                                    onClick={() => handleModifyShow(owner)}>업주 수정
                            </Button>
                        </td>
                        <td>
                            <Button size={"sm"} variant={"outline-danger"}
                                    onClick={(event) => deleteOwnerApi(owner.id)}>업주 삭제</Button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </Table>
        </TableContent>
    )
}

export default VOwnerContent;