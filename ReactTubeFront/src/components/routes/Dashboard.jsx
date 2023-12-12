import { useDispatch, useSelector} from "react-redux";

import { useEffect} from "react";
import { actions } from "../customHooks/actions";

import Header from "../../widgets/Header";
import { getJwt, getUser} from "../customHooks/useLogin";
import UserSettingsForm from "../UserSettingsForm";

export const Dashboard = () => {
    const dispatch = useDispatch();
    const current = "dashboard";

    

    const jwt =  useSelector(getJwt);
    const user = useSelector(getUser);


    useEffect(() => {
    
        if(!jwt || !user){
            dispatch({ type: actions.CHECK_STORAGE });
        }

    });

    
    return (
        <>
            <Header current={current}/>
            <UserSettingsForm/>
        </>
    );

    
}




export default Dashboard;
