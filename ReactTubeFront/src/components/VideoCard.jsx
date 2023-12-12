/* eslint-disable react/prop-types */

import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { Link } from "react-router-dom";
import { getJwt } from "./customHooks/useLogin";
import { useNavigate } from "react-router";

import styles from "../resources/css/VideoCard.module.css";
import { formatDistance } from "date-fns";

const VideoCard = ({ videoDto }) => {
  const [img, setImg] = useState(null);
  const jwt = useSelector(getJwt);
  const navigate = useNavigate();

  useEffect(() => {
    if (jwt) {
      fetch(`http://localhost:8080/video/getVideoThumbnail/${videoDto.video.id}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: jwt,
        }
      })
      .then((response) => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.blob();
      })
      .then((blob) => {
        setImg(URL.createObjectURL(blob));
      })
      .catch((error) => {
        console.log(error);
        // Considera mostrar un mensaje de error o una imagen de placeholder
      });
    }
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

  const getUploadDateFormated = (uploadDate) => {


    const from = new Date(uploadDate);
    const to = new Date();

    console.log(from);

    const distance = formatDistance(from, to, {addSuffix: true});

    const distanceArray = distance.split("");
    distanceArray[0] = distanceArray[0].toUpperCase();

    return distanceArray.join("");
  };

  return (
    <div className={styles.card}>
      <div className={styles.card_header}>
        <h3>{title}</h3>
      </div>
      <div className={styles.card_body}>
        <div className={styles.video_thumbnail}>
          <Link to={`http://localhost:8080/video/watch/${id}`}>
            {img && <img src={img} alt="Video thumbnail"/>}
            <span>{durationFormatter(durationInSeconds)}</span>
          </Link>
        </div>
          <p>
            {uploadedBy.username} ‒ {getVideoTotalVisits(videoVisits)}  {(getVideoTotalVisits(videoVisits) > 1) || (getVideoTotalVisits(videoVisits) == 0)  ? 'visualizations' : 'visualization'}
          </p>
          <p>
            {getUploadDateFormated(uploadDate)} ◷ 
          </p>
      </div>
    </div>
  );
};

export default VideoCard;
