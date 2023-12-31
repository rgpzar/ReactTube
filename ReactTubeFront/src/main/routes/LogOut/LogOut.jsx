import { useEffect } from "react";
import { useDispatch } from "react-redux";
import { useNavigate} from "react-router-dom";
import {toast} from "react-toastify";

export const LogOut = () => {
    const navigate = useNavigate();
    const dispatch = useDispatch();

    
    useEffect(() => {
        document.cookie = "jwt=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
        document.cookie = "user=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";

        dispatch({type: "CLEAR_STATE"});

        setTimeout(() => {
            toast.info("Logging out...");
        }, 1000);


        navigate("/");
    }, []); // Add an empty dependency array to ensure the effect runs only once.
}