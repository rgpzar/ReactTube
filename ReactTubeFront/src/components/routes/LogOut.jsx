import { useEffect } from "react";
import { useNavigate} from "react-router-dom";

export const LogOut = () => {
    const navigate = useNavigate();


    localStorage.removeItem("jwt");
    localStorage.removeItem("user");
    localStorage.removeItem("");

    useEffect(() => {
        navigate("/auth");
    }, []);
}