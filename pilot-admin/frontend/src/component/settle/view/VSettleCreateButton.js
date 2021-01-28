import React from 'react';
import {Button} from "react-bootstrap";
import {RightHorizontal} from "../../../util/CommonStyledComponents";

const VSettleCreateButton = ({handleShow, title}) => {
    return (
        <>
            <Button style={{height: "40px"}} size={"sm"} variant="outline-success" onClick={handleShow}>
                {title}
            </Button>
            <RightHorizontal/>
        </>
    )
}

export default VSettleCreateButton;