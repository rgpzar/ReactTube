import { useEffect, useState } from "react";
import Header from "../../../components/Header/Header.jsx";
import UserSettingsForm from "../../../components/UserSettingsForm/UserSettingsForm.jsx";
import UserInfo from "../../../components/UserInfo/UserInfo.jsx";

import pencil from "../../../resources/img/pencil.png";
import cross from "../../../resources/img/close(1).png";

import styles from "./Dashboard.module.css";
import { useCheckSession } from "../../../helpers/customHooks/useCheckSession.js";

export const Dashboard = () => {
    const current = "dashboard";

    const [showEditForm, setShowEditForm] = useState(false);
    const [userInfo, setUserInfo] = useState({});
    const [loading, setLoading] = useState(true); // Nuevo estado para controlar la carga

    const { user } = useCheckSession();

    useEffect(() => {
        if (user) {
            setUserInfo(user);
            setLoading(false); // Datos cargados, establecer loading en false
        } else {
            setLoading(true); // Si no hay usuario, mostrar el estado de carga
        }
    }, [user]);

    if (loading) {
        return (
            <>
                <Header current={current} />
                <section>
                    <div>Loading...</div>
                </section>
            </>
        );
    }

    return (
        <>
            <Header current={current} />
            <section>
                <button className={styles.editFormBtn} onClick={() => setShowEditForm(!showEditForm)}>
                    {showEditForm ? <img src={cross} alt="Cerrar"/> : <img src={pencil} alt="Editar"/>}
                </button>
                {showEditForm ? <UserSettingsForm userInfo={userInfo}/> : userInfo && <UserInfo userInfo={userInfo}/>}
            </section>
        </>
    );
}

export default Dashboard;
