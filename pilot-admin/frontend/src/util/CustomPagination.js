import {Pagination} from "react-bootstrap";
import React from "react";

const CustomPagination = ({fetchPage, current, total}) => {
    const hasBeforePage = parseInt(current / 5) >= 1;
    const beforeFirstPage = 5 * ((current / 5) - 1)
    const currentFirstPage = 5 * parseInt(current / 5);
    const afterFirstPage = currentFirstPage + 5;
    const hasAfterPage = currentFirstPage + 5 < total;

    const items = [];
    items.push(<Pagination.First onClick={() => fetchPage(0)}/>)
    if (hasBeforePage) {
        items.push(<Pagination.Ellipsis onClick={() => fetchPage(beforeFirstPage)}/>)
    }
    for (let number = currentFirstPage; number < currentFirstPage + 5; number++) {
        if (number === total) {
            break;
        }
        items.push(
            <Pagination.Item key={number} onClick={() => fetchPage(number)} active={number === current}>
                {number + 1}
            </Pagination.Item>)
    }
    if (hasAfterPage) {
        items.push(<Pagination.Ellipsis onClick={() => fetchPage(afterFirstPage)}/>)
    }
    items.push(<Pagination.Last onClick={() => fetchPage(total - 1)}/>)

    return (
        <Pagination>{items}</Pagination>
    )

}

export default CustomPagination;