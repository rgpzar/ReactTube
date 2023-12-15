/* eslint-disable react/prop-types */
import { useEffect, useState } from "react";
import styles from "./UserInfo.module.css";
import { useCheckSession } from "../../helpers/customHooks/useCheckSession.js";
import VideoEditWrapper from "../VideoWrapper/VideoEditWrapper.jsx";

const UserInfo = ({ userInfo }) => {
    const { jwt } = useCheckSession();
    const [videoList, setVideoList] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchVideos = async () => {
            setIsLoading(true);
            setError(null);
            try {
                const response = await fetch("http://localhost:8080/video", {
                    method: "GET",
                    headers: { 'Authorization': jwt }
                });

                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }

                const videos = await response.json();
                return videos.filter(videoDto => videoDto.uploadedBy.username === userInfo.username);
            } catch (error) {
                setError('There was a problem with the fetch operation: ' + error.message);
                console.error(error);
            } finally {
                setIsLoading(false);
            }
        };

        fetchVideos().then(videos => {
            if (videos) setVideoList(videos);
        });
    }, [jwt, userInfo]);

    if (isLoading) return <p>Loading videos...</p>;
    if (error) return <p>Error: {error}</p>;

    return (
        <>
            <div className={styles.user_info}>
                <p><span>Username: </span> {userInfo.username}</p>
                <p><span>Email: </span> {userInfo.email}</p>
                <p><span>First Name: </span> {userInfo.firstName}</p>
                <p><span>Last Name: </span> {userInfo.lastName}</p>
                <p><span>Phone Number: </span> {userInfo.phoneNumber}</p>
            </div>

            <h2>Videos uploaded by {userInfo.username}</h2>
            <VideoEditWrapper videoList={videoList}/>
        </>
    );
};

export default UserInfo;


