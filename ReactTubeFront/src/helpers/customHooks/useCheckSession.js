import { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { actions } from '../actions.js';
import { getJwt, getUser } from '../configureAppStore.js';

export const useCheckSession = () => {
    const dispatch = useDispatch();
    const jwt = useSelector(getJwt);
    const user = useSelector(getUser);

    useEffect(() => {
        if (!jwt || !user) {
            dispatch({ type: actions.CHECK_STORAGE });
        }
    }, [jwt, user, dispatch]);

    return { jwt, user }; // Devolver jwt y user
};