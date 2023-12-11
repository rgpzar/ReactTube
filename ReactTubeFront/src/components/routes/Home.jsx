import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate} from "react-router-dom";
import { actions } from "../customHooks/actions";
import Header from "../../widgets/Header";
import { getJwt, getUser } from "../customHooks/useLogin";
import VideoWrapper from "../VideoWrapper";
import styles from "../../resources/css/Home.module.css";


export const Home = () => {
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const current = "home";

    const jwt = useSelector(getJwt) || null;
    const user = useSelector(getUser) || null;

    const [videoList, setVideoList] = useState(null);
    const [searchTerm, setSearchTerm] = useState("");

    console.log(jwt);

    useEffect(() => {

        if (!jwt || !user) {
            dispatch({ type: actions.CHECK_STORAGE });
        }

        const url = "http://localhost:8080/video";

        fetch(url, {
                method: "GET",
                headers: {
                    'Access-Control-Allow-Origin': '*',
                    'Authorization': jwt
                }
            })
                .then(response => response.json())
                .then(data => {
                    data = data.filter(videoDto => {
                        return videoDto.video.title.toLowerCase().includes(searchTerm.toLowerCase());
                    });
                    setVideoList(data);
                })
                .catch(e => {
                    navigate("/logout");
                    console.log(e);
                })
    }, [jwt, navigate, searchTerm, dispatch, user]);


    return (
        <>
            <Header current={current} setSearchTerm={setSearchTerm}/>
            <section>
                <VideoWrapper videoList={videoList} />
            </section>
        </>
    )
}

export default (Home);