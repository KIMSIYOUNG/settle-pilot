import React, {useEffect, useState} from 'react';
import {OwnerAPI} from "../../util/api/OwnerAPI";
import {Button, Form, Modal, Table} from "react-bootstrap";
import CustomPagination from "../../util/CustomPagination";
import styled from "styled-components";

const OwnerSearchModal = ({show, onClose, onSelect}) => {
    const [owners, setOwners] = useState(null);
    const [search, setSearch] = useState({});

    const handleSearchChange = (event) => {
        setSearch({
                ...search,
                [event.target.id]: event.target.value
            }
        )
    };

    const fetchNextPage = async (nextPage) => {
        const response = await OwnerAPI.getByCondition(search, nextPage, 5);
        setOwners(response);
    }

    useEffect(() => {
        fetchNextPage(0);
    }, [])

    return (
        <Modal show={show} onHide={onClose}>
            <Modal.Header closeButton>
                <Modal.Title>업주정보 검색하기</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form.Group>
                    <Form.Label>업주 이름</Form.Label>
                    <Form.Control id={"name"} type={"text"} placeholder="선택사항입니다."
                                  onChange={handleSearchChange}/>
                </Form.Group>
                <RightHorizontal/>
                <Form.Group>
                    <Form.Label>업주 이메일</Form.Label>
                    <Form.Control id={"email"} type={"text"} placeholder="선택사항입니다."
                                  onChange={handleSearchChange}/>
                </Form.Group>
                <RightHorizontal/>
                <ButtonBox>
                    <Button variant="outline-success" onClick={() => fetchNextPage()}>검색</Button>
                </ButtonBox>

                {owners && <>
                    <Table striped hover responsive>
                        <thead>
                        <tr>
                            <th>업주 번호</th>
                            <th>업주 이름</th>
                            <th>업주 이메일</th>
                        </tr>
                        </thead>
                        <tbody>
                        {owners.owners.content.map(owner => (
                            <tr key={owner.id}
                                onClick={() => onSelect(owner)}
                            >
                                <td>{owner.id}</td>
                                <td>{owner.name}</td>
                                <td>{owner.email}</td>
                            </tr>
                        ))}
                        </tbody>
                    </Table>
                    <PageContainer>
                        <PageNumber>
                            <CustomPagination fetchPage={fetchNextPage} current={owners.owners.pageable.pageNumber}
                                              total={owners.owners.totalPages}/>
                        </PageNumber>
                    </PageContainer>
                </>}
            </Modal.Body>
        </Modal>
    )
}

export default OwnerSearchModal;

const ButtonBox = styled.div`
  display: flex;
  justify-content: flex-end;
  padding-bottom: 20px;
`

const RightHorizontal = styled.div`
  padding-right: 20px;
`


const PageContainer = styled.div`
  flex: 6;
  display: flex;
  padding-top: 10px;
`

const PageNumber = styled.div`
  flex: 5.5;
  display: flex;
  justify-content: center;
  align-items: center;
`