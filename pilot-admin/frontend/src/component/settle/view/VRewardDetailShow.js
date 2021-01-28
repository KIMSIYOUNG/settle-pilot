import React, {useEffect, useState} from 'react';
import {Button, Form, Modal, Table} from "react-bootstrap";
import {RewardAPI} from "../../../util/api/RewardAPI";
import CustomPagination from "../../../util/CustomPagination";
import styled from "styled-components";
import {DATE_TIME_CONVERTER, MONEY_DELIMITER, REWARD_TYPE} from "../../../Const";

const VRewardDetailShow = ({settleId}) => {
    const [rewards, setRewards] = useState(null)
    const [reward, setReward] = useState(null)
    const [show, setShow] = useState(false);
    const handleClose = () => setShow(false);
    const handleShow = (event, reward) => {
        setReward(reward)
        setShow(true)
    }

    const fetchSettleRewards = async (nextPage) => {
        const response = await RewardAPI.getBySettleId(settleId, nextPage);
        setRewards(response);
    }

    useEffect(() => {
        fetchSettleRewards(0);
    }, [])


    return (
        <>
            {reward &&
            <Modal show={show} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>보상금액 상세내역</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    보상금액에 대한 설명입니다.
                    <br/><br/>
                    <Form.Group>
                        <Form.Label type="text">보상금액 ID</Form.Label>
                        <Form.Control id={"nothing"} placeholder={reward.id} readOnly={true}/>
                    </Form.Group>

                    <Form.Group>
                        <Form.Label type="text">대상 업주</Form.Label>
                        <Form.Control id={"nothing"} placeholder={reward.owner.name} readOnly={true}/>
                    </Form.Group>

                    <Form.Group>
                        <Form.Label type="text">금액</Form.Label>
                        <Form.Control id={"amount"} placeholder={MONEY_DELIMITER(reward.amount)} readOnly={true}/>
                    </Form.Group>

                    <Form.Group>
                        <Form.Label type="text">타입</Form.Label>
                        <Form.Control id={"rewardType"} placeholder={REWARD_TYPE[reward.rewardType]} readOnly={true}/>
                    </Form.Group>

                    <Form.Group>
                        <Form.Label type="text">상세설명</Form.Label>
                        <Form.Control id={"description"} placeholder={reward.description} readOnly={true}/>
                    </Form.Group>
                    <div style={{height: "2px", backgroundColor: '#E9ECEF', marginBottom: "15px"}}/>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="primary" onClick={handleClose}>
                        돌아가기
                    </Button>
                </Modal.Footer>
            </Modal>}
            {rewards && <>
                <Table striped hover responsive>
                    <thead>
                    <tr>
                        <th>보상금액 ID</th>
                        <th>대상 ID</th>
                        <th>보상금액</th>
                        <th>보상타입</th>
                        <th>보상시기</th>
                        <th>상세보기</th>
                    </tr>
                    </thead>
                    <tbody>
                    {rewards.rewards.content.map(reward => (
                        <tr key={reward.id}>
                            <td>{reward.id}</td>
                            <td>{reward.owner.id}</td>
                            <td>{MONEY_DELIMITER(reward.amount)}</td>
                            <td>{REWARD_TYPE[reward.rewardType]}</td>
                            <td>{DATE_TIME_CONVERTER(reward.rewardDateTime)}</td>
                            <td>
                                <Button size={"sm"} variant={"outline-info"}
                                        onClick={(event) => handleShow(event, reward)}>상세내용</Button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </Table>
                <PageContainer>
                    <CustomPagination fetchPage={fetchSettleRewards} current={rewards.rewards.pageable.pageNumber}
                                      total={rewards.rewards.totalPages}/>
                </PageContainer>
            </>}
        </>
    )
}

export default VRewardDetailShow;

const PageContainer = styled.div`
  display: flex;
  justify-content: center;
  padding-top: 10px;
`
