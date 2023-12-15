import { useEffect } from "react";
import { useDispatch } from "react-redux";
import { useNavigate} from "react-router-dom";

export const LogOut = () => {
    const navigate = useNavigate();
    const dispatch = useDispatch();

    
    useEffect(() => {
        document.cookie = "jwt=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
        document.cookie = "user=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";

        dispatch({type: "CLEAR_STATE"});

        navigate("/");
    }, []); // Add an empty dependency array to ensure the effect runs only once.
}