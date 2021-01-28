import React from 'react';
import {Button} from "react-bootstrap";
import {RightHorizontal} from "../../../util/CommonStyledComponents";

const VSettleBulkUpdate = ({handleBulkUpdateShow}) => {
    return (
        <>
            <Button style={{height: "40px"}} size={"sm"} variant="outline-primary"
                    onClick={handleBulkUpdateShow}>
                한번에 지급완료 하기
            </Button>
            <RightHorizontal/>
        </>
    )
}

export default VSettleBulkUpdate;