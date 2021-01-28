import React from 'react';
import {Button} from "react-bootstrap";
import {ButtonBox} from "../../util/CommonStyledComponents";

const VSubmitButton = () => {
    return (
        <ButtonBox>
            <Button type="submit" style={{borderRadius: "20px"}} variant={"outline-success"}>주문 등록</Button>
        </ButtonBox>
    )
}

export default VSubmitButton;