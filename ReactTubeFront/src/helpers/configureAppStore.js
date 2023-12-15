/* eslint-disable no-case-declarations */
import { configureStore } from "@reduxjs/toolkit";
import { actions } from "./actions.js";
import { jwtDecode } from "jwt-decode";
import thunk from "redux-thunk";
import {sessionCheckerMiddleware} from "./sessionCheckerMiddleware.js";

const saveSession = (jwt, user) => {
    // Considerar el uso de localStorage o sessionStorage
    const expirationTime = new Date();
    expirationTime.setTime(expirationTime.getTime() + 30 * 60 * 1000); // 30 minutes

    document.cookie = `jwt=Bearer ${jwt}; expires=${expirationTime.toUTCString()}; path=/`;
    document.cookie = `user=${JSON.stringify(user)}; expires=${expirationTime.toUTCString()}; path=/`;
};

const getStoredSession = () => {
    const jwtCookie = document.cookie.split('; ').find(row => row.startsWith('jwt='));
    const userCookie = document.cookie.split('; ').find(row => row.startsWith('user='));

    const jwt = jwtCookie ? jwtCookie.split('=')[1] : null;
    const user = userCookie ? JSON.parse(userCookie.split('=')[1]) : null;

    return {
        jwt,
        user
    };
};

const initialState = getStoredSession();

const loginReducer = (state = initialState, action) => {
    switch (action.type) {
        case actions.CHECK_STORAGE:
            const session = getStoredSession();
            if(!session.jwt){
                location.replace('/');
            }
            return session.jwt ? session : state;

        case actions.STORE_NEW_SESSION:
            const jwt = action.payload.jwt;
            const decodedJwt = jwtDecode(jwt);
            const user = { ...decodedJwt };

            saveSession(jwt, user);
            return { jwt: `Bearer ${jwt}`, user };

        case actions.CLEAR_STATE:
            return null;

        default:
            return state;
    }
};

export const configureAppStore = () => {
    return configureStore({
        reducer: loginReducer,
        middleware: (getDefaultMiddleware) => getDefaultMiddleware().concat(thunk, sessionCheckerMiddleware)
    });
};

export const getJwt = (state) => state?.jwt || null;
export const getUser = (state) => state?.user || null;

export default configureAppStore;
