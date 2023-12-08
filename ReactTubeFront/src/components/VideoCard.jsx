/* eslint-disable react/prop-types */

import { useEffect, useRef, useState } from "react";
import { useSelector } from "react-redux";
import { Link } from "react-router-dom";
import { getJwt } from "./customHooks/useLogin";

import styles from "../resources/css/VideoCard.module.css";

const VideoCard = ({ videoDto }) => {
  const [img, setImg] = useState(null);

  const jwt = useSelector(getJwt);

  useEffect(() => { 
    console.log(jwt.includes(" "));
    console.log(jwt);

    jwt && fetch("http://localhost:8080/video/getVideoThumbnail/" + videoDto.video.id, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: jwt,
      }
    }).then((response) => response.blob())
    .then((blob) => {
      setImg(URL.createObjectURL(blob));
    });
  }, [videoDto, jwt]);

  const { id, title, uploadDate, durationInSeconds } = videoDto.video;
  const {uploadedBy, videoVisits} = videoDto;

  const getVideoTotalVisits = (videoVisits) => {
    let total = 0;

    videoVisits.forEach(visit => {
      total += visit.timesVisited;
    });

    return total;
  }

  const durationFormatter = (durationInSeconds) => { 
    if(durationInSeconds < 3600){
      return new Date(durationInSeconds * 1000).toISOString().substring(14, 19)
    }else{
      return new Date(durationInSeconds * 1000).toISOString().substring(11, 16)
    }
  };

  return (
    <div className={styles.card}>
      <div className={styles.card_header}>
        <h3>{title}</h3>
      </div>
      <div className={styles.card_body}>
        <div className={styles.video_thumbnail}>
          <Link to={`http://localhost:8080/video/watch/${id}`}>
            <img src={img} alt="Video thumbnail"/>
          </Link>
        </div>
        <p>Uploaded by: {uploadedBy.username}</p>
        <p>Upload date: {uploadDate}</p>
        <p>Duration: {durationFormatter(durationInSeconds)}</p>
        <p>Visits: {
          getVideoTotalVisits(videoVisits)
        }
        </p>
      </div>
    </div>
  );
};

export default VideoCard;
