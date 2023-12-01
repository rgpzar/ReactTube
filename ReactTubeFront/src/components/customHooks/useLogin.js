/* eslint-disable no-case-declarations */
import { configureStore } from "@reduxjs/toolkit";
import { actions } from "./actions";
import { jwtDecode } from "jwt-decode";


const saveSession = (jwt, user) => {
    localStorage.setItem('jwt', 'Bearer ' + jwt);
    localStorage.setItem("user", user);
};

const loginReducer = (state  = getStoredSession(), action) => {
    switch(action.type){
        case actions.CHECK_STORAGE:
            let session = getStoredSession();

            console.log(session);

            if(session){
                return {
                    jwt: session.jwt,
                    user: session.user
                };
            }else{
                return state;
            }

        case actions.STORE_NEW_SESSION:
            console.log(action.payload);


            let jwt = action.payload.jwt;
            let decodedJwt = jwtDecode(jwt);
            let user = JSON.stringify({
                decodedJwt
            });
            
            saveSession(jwt, user);
            
            return {
                jwt: jwt,
                user: user
            };
    }
}

const getStoredSession = () => {
    const storedJwt = localStorage.getItem("jwt"); 
    const storedUser = JSON.parse(localStorage.getItem("user"));

      return {
        jwt: storedJwt,
        user: storedUser
      };
}



export const useLogin = () => {
    return configureStore({
        reducer: loginReducer
    });
}

export const getJwt = (state) => {
    return state.jwt;
}

export const getUser = (state) => {
    return state.user;
}



export default useLogin;