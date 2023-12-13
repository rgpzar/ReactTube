import { useDispatch, useSelector} from "react-redux";

import { useEffect, useState} from "react";
import { actions } from "../customHooks/actions";

import Header from "../../widgets/Header";
import { getJwt, getUser} from "../customHooks/configureAppStore";
import UserSettingsForm from "../UserSettingsForm";
import UserInfo from "../UserInfo";

import styles from "../../resources/css/Dashboard.module.css";

export const Dashboard = () => {
    const dispatch = useDispatch();
    const current = "dashboard";

    

    const jwt =  useSelector(getJwt);
    const user = useSelector(getUser);

    const [showEditForm, setShowEditForm] = useState(false);
    const [userInfo, setUserInfo] = useState({});


    useEffect(() => {
    
        if(!jwt || !user){
            dispatch({ type: actions.CHECK_STORAGE });
        }

    });

    useEffect(() => {
        if(user){
            setUserInfo(user);
        }
    }, [user]);

    
    return (
        <>
            <Header current={current}/>
            <section>
                <button className={styles.editFormBtn} onClick={() => setShowEditForm(!showEditForm)}>
                    {showEditForm ? "Close" : "Edit"}
                </button>
                {showEditForm && <UserSettingsForm userInfo={userInfo}/>}
                {!showEditForm && <UserInfo userInfo={userInfo}/>}
            </section>
        </>
    );

    
}




export default Dashboard;
