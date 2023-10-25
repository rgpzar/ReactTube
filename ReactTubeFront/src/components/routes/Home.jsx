import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate} from "react-router-dom";
import { Link } from "react-router-dom";
import { actions } from "../customHooks/actions";
import Header from "../../widgets/Header";
import { getJwt, getUser } from "../customHooks/useLogin";

import styles from '../../resources/css/Home.module.css';

export const Home = () => {
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const current = "home";

    dispatch({ type: actions.CHECK_STORAGE });

    const jwt = useSelector(getJwt) || null;
    const user = useSelector(getUser) || null;

    console.log(jwt);

    useEffect(() => {

        if (!jwt || !user) {
            navigate("/auth");
        }
    });


    return (
        <>
            <Header current={current} />
            <section>
                <h1>Welcome to the Airport API {user ? user.username: ""}</h1>
                <p>This API provides access to various functionalities related to airports.</p>

                <h2>Main Features</h2>
                <ul>
                    <li>Retrieve information about airports.</li>
                    <li>API Key based service</li>
                    <li>Detailed documentation available.</li>
                </ul>

                <h2>Dashboard</h2>
                <p>Config your user and get your API Key <Link className={styles.link} to={"/dashboard"}>here</Link>.</p>

                <h2>Documentation</h2>
                <p>For details on how to use the API, click <Link className={styles.link} to={"/docs"}>here</Link>.</p>
            </section>
        </>
    )
}

export default (Home);