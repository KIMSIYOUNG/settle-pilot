import React from 'react';
import {Button, Form} from "react-bootstrap";
import OwnerSearchModal from "../../common/OwnerSearchModal";
import {ButtonBox, DefaultSearchBarLayout, RightHorizontal} from "../../../util/CommonStyledComponents";

const VOrderSearchCondition = ({
                                   search, init, onClick, onChange,
                                   showOwnerModal, setShowOwnerModal,
                                   selectOwnerModal
                               }) => {

    return (
        <DefaultSearchBarLayout>
            <OwnerSearchModal show={showOwnerModal} onClose={() => setShowOwnerModal(false)}
                              onSelect={selectOwnerModal}/>

            <Form.Group>
                <Form.Label>업주 번호
                    <Button style={{marginLeft: "90px", paddingTop: "0px", paddingBottom: "0px"}} size={"sm"}
                            variant={"outline-info"}
                            onClick={() => setShowOwnerModal(true)}>검색</Button>
                </Form.Label>
                <Form.Control id={"ownerId"} type={"number"} placeholder="선택사항"
                              value={search?.ownerId ?? ""}
                              onChange={(event) => onChange(event)}
                />
            </Form.Group>
            <RightHorizontal/>
            <Form.Group>
                <Form.Label>주문번호</Form.Label>
                <Form.Control id={"orderNo"} type={"text"} placeholder="선택사항" value={search.orderNo}
                              onChange={(event) => onChange(event)}/>
            </Form.Group>
            <RightHorizontal/>

            <Form.Group>
                <Form.Label type="text">주문상태</Form.Label>
                <Form.Control as="select" id="orderStatus" value={search.orderStatus}
                              onChange={(event) => onChange(event)}>
                    <option value="">전체보기</option>
                    <option value="ORDER">주문요청</option>
                    <option value="ORDER_CONFIRM">주문완료</option>
                    <option value="DELIVERY">배달중</option>
                    <option value="DELIVERY_CONFIRM">배달완료</option>
                    <option value="CANCEL">배달취소</option>
                </Form.Control>
            </Form.Group>
            <RightHorizontal/>

            <Form.Group>
                <Form.Label>시작일</Form.Label>
                <Form.Control id={"startAt"} type={"dateTime-local"} placeholder="선택사항" value={search.startAt}
                              onChange={(event) => onChange(event)}/>
            </Form.Group>
            <RightHorizontal/>

            <Form.Group>
                <Form.Label>종료일</Form.Label>
                <Form.Control id={"endAt"} type={"dateTime-local"} placeholder="선택사항" value={search.endAt}
                              onChange={(event) => onChange(event)}/>
            </Form.Group>
            <RightHorizontal/>
            <ButtonBox>
                <Button variant="outline-danger" onClick={init}>초기화</Button>
            </ButtonBox>
            <RightHorizontal/>
            <ButtonBox>
                <Button variant="outline-success" onClick={onClick}>검색</Button>
            </ButtonBox>
        </DefaultSearchBarLayout>
    )
}

export default VOrderSearchCondition;