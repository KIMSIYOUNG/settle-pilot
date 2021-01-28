import {createGlobalStyle} from "styled-components";
import {React, useEffect, useState} from 'react';
import {BrowserRouter as Router, Redirect, Route, Switch,} from "react-router-dom";
import Login from "./component/login/Login";
import OAuth2RedirectHandler from "./component/login/OAuth2RedirectHandler";
import {DEFAULT_MEMBER} from "./Const";
import MainHeader from "./component/common/MainHeader";
import MainSideBar from "./component/common/MainSideBar";
import SideBarLayout from "./layout/SideBarLayout";
import {MemberAPI} from "./util/api/MemberAPI";
import OwnerManagement from "./component/owner/OwnerManagement";
import OrderRegister from "./component/order/OrderRegister";
import OrderSearch from "./component/order/OrderSearch";
import RewardSearch from "./component/reward/RewardSearch";
import SettleSearch from "./component/settle/SettleManament";
import VSettleDetail from "./component/settle/view/VSettleDetail";
import RewardRegister from "./component/reward/RewardRegister";
import MemberSearch from "./component/member/MemberSearch";


const App = () => {
    const [member, setMember] = useState(DEFAULT_MEMBER);

    useEffect(() => {
        const fetchMember = async () => {
            const response = await MemberAPI.get()
            setMember(response)
        }
        fetchMember();
    }, [])

    return (

        <Router>
            {member.id === null ? <> <Redirect to={"/login"}/> <Switch>
                    <Route exact path="/login">
                        <Login/>
                    </Route>
                    <Route exact path="/oauth2/redirect">
                        <OAuth2RedirectHandler/>
                    </Route>
                </Switch> </> :
                <><Redirect to={"/"}/>
                    <SideBarLayout header={<MainHeader member={member} changeMember={setMember}/>}
                                   sideBar={<MainSideBar/>}>
                        <Switch>
                            <Route exact path="/owners/search">
                                <OwnerManagement/>
                            </Route>
                            <Route exact path="/orders/register">
                                <OrderRegister/>
                            </Route>
                            <Route exact path="/orders/search">
                                <OrderSearch/>
                            </Route>
                            <Route exact path="/settles/search">
                                <SettleSearch/>
                            </Route>
                            <Route exact path="/rewards/register">
                                <RewardRegister/>
                            </Route>
                            <Route exact path="/settle/details/:id" component={VSettleDetail}/>
                            <Route exact path="/rewards/search">
                                <RewardSearch/>
                            </Route>
                            {member.role === "ADMIN" ? <Route exact path="/members/search">
                                <MemberSearch currentMember={member}/>
                            </Route> : null}
                        </Switch>
                    </SideBarLayout>
                </>
            }
            <GlobalStyle/>
        </Router>
    )
}

export default App;

const GlobalStyle = createGlobalStyle`
  html, body {
    margin: 0;
    padding: 0;
  }

  * {
    box-sizing: border - box;
  }
`
