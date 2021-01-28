import React from 'react';
import {Button} from "react-bootstrap";
import {RightHorizontal} from "../../../util/CommonStyledComponents";

const VSettleCurrentPageComplete = ({updateCurrentPageSettleComplete}) => {
    return (
        <>
            <Button style={{height: "40px"}} size={"sm"} variant="outline-primary"
                    onClick={updateCurrentPageSettleComplete}>
                현재페이지 지급완료 하기
            </Button>
            <RightHorizontal/>
        </>
    )
}

export default VSettleCurrentPageComplete;