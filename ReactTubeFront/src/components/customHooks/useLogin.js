/* eslint-disable no-case-declarations */
import { configureStore } from "@reduxjs/toolkit";
import { actions } from "./actions";
import { jwtDecode } from "jwt-decode";


const saveSession = (jwt, user) => {
    const expirationTime = new Date();
    expirationTime.setTime(expirationTime.getTime() + 30 * 60 * 1000); // 30 minutes

    document.cookie = `jwt=Bearer ${jwt}; expires=${expirationTime.toUTCString()}; path=/`;
    document.cookie = `user=${JSON.stringify(user)}; expires=${expirationTime.toUTCString()}; path=/`;
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


            let jwt = action.payload.jwt;
            console.log(jwt);
            let decodedJwt = jwtDecode(jwt);
            let user = {
                ...decodedJwt
            };
            
            saveSession(jwt, user);

            console.log(user);
            
            return {
                jwt: jwt,
                user: user
            };
    }
}

const getStoredSession = () => {
    const jwtCookie = document.cookie.split('; ').find(row => row.startsWith('jwt='));
    const userCookie = document.cookie.split('; ').find(row => row.startsWith('user='));

    const jwt = jwtCookie ? jwtCookie.split('=')[1] : null;
    const user = userCookie ? JSON.parse(userCookie.split('=')[1]) : null;

    return {
        jwt,
        user
    };
}



export const useLogin = () => {
    return configureStore({
        reducer: loginReducer
    });
}

export const getJwt = (state) => {
    if(state)
    return state.jwt;
}

export const getUser = (state) => {
    if(state)
    return state.user;
}





export default useLogin;