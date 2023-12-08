import { useEffect } from "react";
import { useNavigate} from "react-router-dom";

export const LogOut = () => {
    const navigate = useNavigate();

    document.cookie = "jwt=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
    document.cookie = "user=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";

    useEffect(() => {
        navigate("/");
    }, []);
}