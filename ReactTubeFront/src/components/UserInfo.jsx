/* eslint-disable react/prop-types */
import { useEffect, useState} from "react";
import { useDispatch, useSelector } from "react-redux";
import { getJwt } from "./customHooks/configureAppStore";
import { actions } from "./customHooks/actions";
import VideoWrapper from "./VideoWrapper";

const UserInfo = ({userInfo}) => {
    const dispatch = useDispatch();

    const jwt = useSelector(getJwt);
    const [videoList, setVideoList] = useState([]);

    useEffect(() => {
        if(!jwt){
            dispatch({type: actions.CHECK_STORAGE});
        }
    }, [jwt, dispatch]);

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
                console.log(videos);
                const filteredVideos = videos.filter(videoDto => 
                    videoDto.uploadedBy.username == userInfo.username
                );
                setVideoList(filteredVideos);

            } catch (error) {
                console.error('There was a problem with the fetch operation:', error);
                // Considera un mejor manejo de errores aquí
            }
        };

        fetchVideos();
    }, [jwt, userInfo]);

    return (
        <>
            <div className="userInfo">
                <p>Username: {userInfo.username}</p>
                <p>Email: {userInfo.email}</p>
            </div>


            {videoList.length > 0 && <h2>Videos uploaded by {userInfo.username}</h2>}
            {videoList.length > 0 && <VideoWrapper videoList={videoList}/>}
        </>
    );
};

export default UserInfo;