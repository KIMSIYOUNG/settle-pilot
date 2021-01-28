import React, {useState} from 'react';
import styled from "styled-components";

const SideBarLayout = ({header, sideBar, children}) => {
    const [sidebarToggle, setSidebarToggle] = useState(true);

    const toggleSideBar = () => {
        setSidebarToggle(!sidebarToggle)
    }

    return (
        <SideBarLayoutContainer>
            <SHeader>
                <MenuContainer onClick={() => toggleSideBar()}>
                    <IconButton url={"menu-icon.svg"} width="24px" height="24px"/>
                </MenuContainer>
                {header}
            </SHeader>
            <SBody>
                {sidebarToggle && <SideBar>
                    {sideBar}
                </SideBar>}
                {children}
            </SBody>
        </SideBarLayoutContainer>
    )
}

export default SideBarLayout;

const SHeader = styled.div`
  display: flex;
  height: 80px;
  background-color: #6adaa7;
`

const SBody = styled.div`
  background-color: white;
  height: calc(100% - 80px);
  display: flex;
`

const SideBarLayoutContainer = styled.div`
  width: 100vw;
  height: 100vh;

  background-color: white;
`

const SideBar = styled.div`
  min-width: 300px;
  flex: 1;
  background-color: white;
  border-right: 1px solid #ddd;
  display: flex;
  flex-direction: column;
  overflow: scroll;
  height: 100%;
`

const MenuContainer = styled.div`
  width: 80px;
  height: 80px;
  display: flex;
  justify-content: center;
  align-items: center;

  &:hover {
    background-color: rgba(0, 0, 0, 0.1);
  }
`

const IconButton = styled.div`
  background: ${({url}) => "url('/" + url + "')"};
  background-size: 100% 100%;
  ${({width, height}) => `width:${width}; height:${height};`};
`

