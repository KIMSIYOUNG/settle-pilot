import React from 'react';
import {Button, Table} from "react-bootstrap";
import {DATE_TIME_CONVERTER, MONEY_DELIMITER, REWARD_TYPE} from "../../../Const";
import {TableContent} from "../../../util/CommonStyledComponents";

const VRewardContent = ({rewards, handleOrderShow, handleDetailShow, deleteRewardApi}) => {
    return (
        <TableContent>
            <Table striped hover responsive>
                <thead>
                <tr>
                    <th>업주 이름</th>
                    <th>보정금액 번호</th>
                    <th>보정 금액</th>
                    <th>보정 타입</th>
                    <th>보정 시기</th>
                    <th>관련 주문</th>
                    <th>상세보기/수정</th>
                    <th>보정 금액 삭제</th>
                </tr>
                </thead>
                <tbody>
                {rewards && rewards.rewards.content.map(reward => (
                    <tr key={reward.id}>
                        <td>{reward.owner.name}</td>
                        <td>{reward.rewardNo.substr(0, 18)}</td>
                        <td>{MONEY_DELIMITER(reward.amount)}</td>
                        <td>{REWARD_TYPE[reward.rewardType]}</td>
                        <td>{DATE_TIME_CONVERTER(reward.rewardDateTime)}</td>
                        {reward.order ?
                            <td>
                                <Button size={"sm"} variant={"outline-info"}
                                        onClick={(event) => handleOrderShow(event, reward)}>관련 주문 확인</Button>
                            </td> :
                            <td>
                                <Button size={"sm"} variant={"secondary"}>관련주문 없음</Button>
                            </td>
                        }
                        <td>
                            <Button size={"sm"} variant={"outline-info"}
                                    onClick={(event) => handleDetailShow(event, reward)}>상세보기/수정</Button>
                        </td>
                        <td>
                            <Button size={"sm"} variant={"outline-danger"}
                                    onClick={(event) => deleteRewardApi(event, reward.id)}>보정금액 삭제</Button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </Table>
        </TableContent>
    )
}

export default VRewardContent;