import { useDispatch, useSelector} from "react-redux";

import { useEffect} from "react";
import { actions } from "../customHooks/actions";

import Header from "../../widgets/Header";
import { getJwt, getUser} from "../customHooks/useLogin";

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

    
    if(user && jwt) return (
        <>
            <Header current={current} />
            <section>
                <ul>
                    <li>
                        Username: {user.username}
                    </li>
                    <li>
                        Email: {user.email}
                    </li>
                    <li>
                        IP: {user.ipAddr}
                    </li>
                </ul>
            </section>
        </>
    )
}

export default Dashboard;
