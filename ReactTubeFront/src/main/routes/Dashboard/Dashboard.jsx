import { useEffect, useState} from "react";
import Header from "../../../components/Header/Header.jsx";
import UserSettingsForm from "../../../components/UserSettingsForm/UserSettingsForm.jsx";
import UserInfo from "../../../components/UserInfo/UserInfo.jsx";

import pencil from "../../../resources/img/pencil.png";
import cross from "../../../resources/img/close(1).png";

import styles from "./Dashboard.module.css";
import {useCheckSession} from "../../../helpers/customHooks/useCheckSession.js";

export const Dashboard = () => {
    const current = "dashboard";


    const [showEditForm, setShowEditForm] = useState(false);
    const [userInfo, setUserInfo] = useState({});

    const {user} = useCheckSession();

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
                    {showEditForm ? <img src={cross}/>: <img src={pencil}/>}
                </button>
                {showEditForm ? <UserSettingsForm userInfo={userInfo}/> : userInfo && <UserInfo userInfo={userInfo}/>}
            </section>
        </>
    );



}




export default Dashboard;
