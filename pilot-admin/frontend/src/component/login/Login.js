import {React} from 'react';
import styled from "styled-components";
import {GOOGLE_AUTH_URL} from "../../Const";

const Login = () => {
    return (
        <>
            <BackgroundImage url={"wave.png"} width="55%" height="100%"/>
            <LoginContainer>
                <ImageContainer url={"people.svg"}/>
                <LoginContent>
                    <Title>
                        미니 정산 어드민
                    </Title>
                    <LoginButton href={GOOGLE_AUTH_URL}>
                        GOOGLE LOGIN
                    </LoginButton>
                </LoginContent>
            </LoginContainer>
        </>
    )
}

export default Login

const BackgroundImage = styled.div`
  background: ${({url}) => "url('/" + url + "')"};
  background-size: 100% 100%;
  ${({width, height}) => `width:${width}; height:${height};`};

  position: fixed;
  bottom: 0;
  left: 0;
  height: 100%;
  z-index: -1;
`

const LoginContainer = styled.div`
  flex: 1;
  width: 100vw;
  height: 100vh;
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  grid-gap: 10rem;
  padding: 0 2rem;
`

const ImageContainer = styled.div`
  background: ${({url}) => "url('/" + url + "')"};
  background-size: 100% 100%;

  display: flex;
  justify-content: flex-end;
  align-items: center;
`

const LoginContent = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
  margin-right: 200px;
`

const Title = styled.div`
  font-size: 30px;
  font-weight: 1000;
  color: #2c4557;
  margin: 10px 0;
`

const LoginButton = styled.a`
  text-decoration: none;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 55%;
  height: 70px;
  border-radius: 40px;
  background-image: linear-gradient(to right, #86dec1, #47d3a4, #32be8f);
  background-size: 200%;
  font-size: 1.2rem;
  color: #fff;

  margin: 1rem 0;
  cursor: pointer;
  transition: .5s;

  :hover {
    background-position: right;
  }
`