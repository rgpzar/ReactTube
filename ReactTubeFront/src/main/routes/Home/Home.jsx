import { useEffect, useState } from "react";
import Header from "../../../components/Header/Header.jsx";
import VideoWrapper from "../../../components/VideoWrapper/VideoWrapper.jsx";

import styles from "./Home.module.css";
import {useCheckSession} from "../../../helpers/customHooks/useCheckSession.js";


export const Home = () => {
    const current = "home";


    const [videoList, setVideoList] = useState([]);
    const [searchTerm, setSearchTerm] = useState("");
    const {jwt} = useCheckSession();

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
                 if(searchTerm === "") {
                    setVideoList(videos);
                    return;
                 }

                const filteredVideos = videos.filter(videoDto =>
                    videoDto.video.title.toLowerCase().includes(searchTerm.toLowerCase())
                );

                 filteredVideos.reverse();
                setVideoList(filteredVideos);

            } catch (error) {
                console.error('There was a problem with the fetch operation:', error);
            }
        };

        jwt && fetchVideos();
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
