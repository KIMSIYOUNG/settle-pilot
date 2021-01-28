import React from 'react';
import styled from "styled-components";

const DefaultMainLayout = ({ title, children, width }) => {
    return (
        <SMain>
            <div>
                <MainTitle>
                    {title}
                </MainTitle>
                <MainContent width={width}>
                    {children}
                </MainContent>
            </div>
        </SMain>
    )
}

export default DefaultMainLayout;

const SMain = styled.div`
  flex:5;
  padding-left: 100px;
  padding-top: 40px;
  padding-right: 120px;
  height: 100%;
  overflow: scroll;
  background-color: #f4f2f2;
`


const MainTitle = styled.h1`
  margin-bottom: 50px; 
`

const MainContent = styled.div`
  display:flex;
  flex-direction: column;
  width: ${({ width }) => `${width}`};
`
