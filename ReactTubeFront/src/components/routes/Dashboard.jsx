import { useNavigate } from "react-router";
import { Link } from "react-router-dom";

import { useDispatch, useSelector } from "react-redux";

import { useEffect } from "react";
import { actions } from "../customHooks/actions";

import Header from "../../widgets/Header";
import { getJwt, getUser } from "../customHooks/useLogin";

import styles from '../../resources/css/Dashboard.module.css'

export const Dashboard = () => {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const current = "dashboard";

    dispatch({ type: actions.CHECK_STORAGE });

    const jwt = useSelector(getJwt) || null;
    const user = useSelector(getUser) || null;


    //console.log(jwt, user);


    useEffect(() => {
        if (!jwt) {
            navigate("/auth");
        }
    });

    return (
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
                        Api Key: {user.apiKey}
                    </li>
                    <li>
                        Example route: <Link className={styles.link} to={"http://localhost:8080/airport?api_key=" + user.apiKey}>Get all airports</Link> 
                    </li>
                </ul>
            </section>
        </>
    )
}

export default Dashboard;