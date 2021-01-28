import React from 'react';
import {Form} from "react-bootstrap";
import {MONEY_DELIMITER} from "../../../Const";
import {DefaultSearchBarLayout} from "../../../util/CommonStyledComponents";

const VSettleAmount = ({settleSnapshot}) => {
    return (
        <DefaultSearchBarLayout>
            <Form.Group>
                <Form.Label>배치로 생성된 지급금</Form.Label>
                <Form.Control id={"id"} type={"amount"} value={MONEY_DELIMITER(settleSnapshot.amount)}
                              readOnly={true}/>
            </Form.Group>
        </DefaultSearchBarLayout>
    )
}

export default VSettleAmount;