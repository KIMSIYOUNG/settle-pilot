import React from "react";
import {Redirect, useLocation} from "react-router-dom";

const OAuth2RedirectHandler = () => {
    const location = useLocation();

    return (
        <Redirect
            to={{
                pathname: "/",
                state: {
                    from: location,
                },
            }}
        />
    );
};

export default OAuth2RedirectHandler;
