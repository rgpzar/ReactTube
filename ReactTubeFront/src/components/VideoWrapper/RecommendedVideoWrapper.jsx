/* eslint-disable react/prop-types */
import VideoCard from "../VideoCard/VideoCard.jsx";
import styles from "./VideoWrapper.module.css";
import {useEffect, useState} from "react";
import {useCheckSession} from "../../helpers/customHooks/useCheckSession.js";

const RecommendedVideoWrapper = ({ excludedId }) => {
  const [videoList, setVideoList] = useState([]);

  const {jwt} = useCheckSession();

  //Fetching videos from http:localhost:8080/video
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

        let videos = await response.json();
        console.log(videos);
        videos = videos.filter(videoDto => videoDto.video.id !== excludedId)
        setVideoList(videos);

      } catch (error) {
        console.error('There was a problem with the fetch operation:', error);
      }
    };

    fetchVideos();
  }, [jwt]);

  return (
    <div className={styles.recommended_video_wrapper}>
      {
            Array.isArray(videoList) && videoList.map((videoDto ) => {
                return (
                    <VideoCard key={videoDto.video.id} videoDto={videoDto} />
                )
            })
        }
    </div>
  );
};

export default RecommendedVideoWrapper;
