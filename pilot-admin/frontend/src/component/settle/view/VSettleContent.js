import React from 'react';
import {Button, Table} from "react-bootstrap";
import {DATE_TIME_CONVERTER, MONEY_DELIMITER, SETTLE_STATUS, SETTLE_TYPE} from "../../../Const";
import {TableContent} from "../../../util/CommonStyledComponents";

const VSettleContent = ({settles, redirectSettleDetail, completeSettle}) => {
    return (
        <TableContent>
            <Table striped hover responsive>
                <thead>
                <tr>
                    <th>지급 상태</th>
                    <th>업주번호</th>
                    <th>지급금 번호</th>
                    <th>지급금액</th>
                    <th>지급금 유형</th>
                    <th>거래기간</th>
                    <th>지급금 예정일</th>
                    <th>지급 완료일</th>
                    <th>상세보기</th>
                    <th>지급완료처리</th>
                </tr>
                </thead>
                <tbody>
                {settles && settles.settles.content.map(settle => (
                    <tr key={settle.id}>
                        {settle.settleStatus === "CREATED"
                            ? <td style={{
                                display: "flex",
                                backgroundColor: "#bceed8",
                                borderRadius: 100,
                                justifyContent: "center"
                            }}>{SETTLE_STATUS[settle.settleStatus]}</td>
                            : <td style={{
                                display: "flex",
                                justifyContent: "center"
                            }}>{SETTLE_STATUS[settle.settleStatus]}</td>
                        }
                        <td>{settle.ownerId}</td>
                        <td>{settle.businessNo.businessNo.substr(0, 18)}</td>
                        <td>{MONEY_DELIMITER(settle.amount)}</td>
                        <td>{SETTLE_TYPE[settle.settleType]}</td>
                        <td>{DATE_TIME_CONVERTER(settle.transactionStartAt, true)} ~ {DATE_TIME_CONVERTER(settle.transactionEndAt, true)}</td>
                        <td>{DATE_TIME_CONVERTER(settle.settleScheduleDate, true)}</td>
                        {settle.settleCompleteDate
                            ? <td>{DATE_TIME_CONVERTER(settle.settleCompleteDate, true)}</td>
                            : <td>미지급 상태</td>
                        }
                        <td>
                            <Button size={"sm"} variant={"outline-info"}
                                    onClick={() => redirectSettleDetail(settle.id)}>지급금 상세보기
                            </Button>
                        </td>
                        <td>
                            <Button size={"sm"} variant={"outline-success"}
                                    onClick={() => completeSettle(settle.id)}>지급완료처리
                            </Button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </Table>
        </TableContent>
    )
}

export default VSettleContent;