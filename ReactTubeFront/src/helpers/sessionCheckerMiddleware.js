import {jwtDecode} from 'jwt-decode';
import {actions} from "./actions.js";

export const sessionCheckerMiddleware = store => next => action => {
    const result = next(action);
    const state = store.getState();

    if (state.jwt) {
        const decodedJwt = jwtDecode(state.jwt.replace('Bearer ', ''));
        const isExpired = new Date(decodedJwt.exp * 1000) < new Date();

        if (isExpired) {
            store.dispatch({ type: actions.CLEAR_STATE });
            location.replace('/');
        }else{
            console.log("Session is valid");
        }
    }

    return result;
};
