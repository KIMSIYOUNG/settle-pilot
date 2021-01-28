import React, {useState} from 'react';
import styled from "styled-components";
import {SIDEBAR_LIST} from "../../Const";
import {Link} from "react-router-dom";

const MainSideBar = () => {
    const [menuList, setMenuList] = useState(SIDEBAR_LIST);

    const changeMenuToggle = (id) => {
        setMenuList(menuList.map(m => m.id === id ? ({...m, toggle: !m.toggle}) : m))
    }

    return (
        <>
            {menuList.map(menu => <MenuList key={menu.id}>
                <MenuHeader onClick={() => changeMenuToggle(menu.id)}>
                    <IconContainer isActive={menu.toggle}>
                        <IconButton url={"arrow.svg"} width="15px" height="15px"/>
                    </IconContainer>
                    {menu.name}
                </MenuHeader>
                {menu.toggle && menu.menuItems.map(menu => <MenuItem key={menu.id} to={menu.path}>
                    <IconContainer isItem>
                        <IconButton url={menu.icon} width="15px" height="15px"/>
                    </IconContainer>
                    {menu.name}
                </MenuItem>)}
            </MenuList>)}
        </>
    )
}

export default MainSideBar;

const IconButton = styled.div`
  background: ${({url}) => "url('/" + url + "')"};
  background-size: 100% 100%;
  ${({width, height}) => `width:${width}; height:${height};`};
`

const MenuList = styled.div`
  display: flex;
  flex-direction: column;
  margin-top: 10px;
  margin-left: 10px;
`

const MenuHeader = styled.div`
  height: 40px;
  display: flex;
  align-items: center;
  padding-left: 10px;

  font-size: 16px;
  font-weight: 700;
  color: #5ebe92;

  &:hover {
    background-color: rgba(0, 0, 0, 0.1);
  }
;
`

const IconContainer = styled.div`
  width: 100px;
  display: flex;
  justify-content: ${({isItem}) => `${isItem && "center"}`};
  ${({isActive}) => isActive && "transform:scaleY(-1)"};
`

const MenuItem = styled(Link)`
  text-decoration: none;
  display: flex;
  padding-left: 10px;
  height: 50px;

  font-size: 15px;
  font-weight: 500;
  color: black;
  align-items: center;

  &:hover {
    background-color: rgba(0, 0, 0, 0.1);
  }
;
`
