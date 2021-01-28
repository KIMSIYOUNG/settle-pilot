import React from 'react';
import CustomPagination from "../../util/CustomPagination";
import {PageContainer} from "../../util/CommonStyledComponents";

const VPageContainer = ({values, fetchNextPage}) => {

    return (
        <PageContainer>
            <CustomPagination fetchPage={fetchNextPage} current={values.pageable.pageNumber}
                              total={values.totalPages}/>
        </PageContainer>
    )
}

export default VPageContainer;