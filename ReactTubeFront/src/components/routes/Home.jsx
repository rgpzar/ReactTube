import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { actions } from "../customHooks/actions";
import Header from "../../widgets/Header";
import { getJwt, getUser } from "../customHooks/useLogin";
import VideoWrapper from "../VideoWrapper";

import styles from "../../resources/css/Home.module.css";


export const Home = () => {
    const dispatch = useDispatch();
    const current = "home";

    const jwt = useSelector(getJwt);
    const user = useSelector(getUser);

    const [videoList, setVideoList] = useState([]);
    const [searchTerm, setSearchTerm] = useState("");

    useEffect(() => {
        if (!jwt || !user) {
            dispatch({ type: actions.CHECK_STORAGE });
        }
    }, []);

    useEffect(() => {
        const url = "http://localhost:8080/video";

        const fetchVideos = async () => {
            try {
                const response = await fetch(url, {
                    method: "GET",
                    headers: {
                        'Authorization': jwt
                    }
                });

                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }

                const videos = await response.json();
                const filteredVideos = videos.filter(videoDto => 
                    videoDto.video.title.toLowerCase().includes(searchTerm.toLowerCase())
                );
                setVideoList(filteredVideos);

            } catch (error) {
                console.error('There was a problem with the fetch operation:', error);
                // Considera un mejor manejo de errores aquí
            }
        };

        fetchVideos();
    }, [jwt, searchTerm]);

    return (
        <>
            <Header current={current} setSearchTerm={setSearchTerm}/>
            <section>
                <VideoWrapper videoList={videoList} />
            </section>
        </>
    );
};

export default Home;
