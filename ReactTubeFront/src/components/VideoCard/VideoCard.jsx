/* eslint-disable react/prop-types */

import { useEffect, useState } from "react";
import { useSelector, useDispatch } from "react-redux";
import { Link } from "react-router-dom";
import { getJwt } from "../../helpers/configureAppStore.js";
import { actions } from "../../helpers/actions.js";
import videoPlaceholder from "../../resources/img/video-placeholder.jpg";

import styles from "./VideoCard.module.css";

import { durationFormatter, getUploadDateFormatted, getVideoTotalVisits } from "../../helpers/videoDatesFormatter.js";

const VideoCard = ({ videoDto }) => {
  const [img, setImg] = useState(null);
  const jwt = useSelector(getJwt);
  const dispatch = useDispatch();

  useEffect(() => {
    if (!jwt) {
        dispatch({ type: actions.CHECK_STORAGE });
    }
  }, [jwt, dispatch]);


  useEffect(() => {

    jwt && fetch(`http://localhost:8080/video/getVideoThumbnail/${videoDto.video.id}`, {
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
        setImg(videoPlaceholder); // Set a placeholder image
      });

  }, [videoDto, jwt]);

  const { id, title, uploadDate, durationInSeconds } = videoDto.video;
  const {uploadedBy, videoVisits} = videoDto;

  

  return (
    <div className={styles.card}>
      <div className={styles.card_header}>
        <h3>{title}</h3>
      </div>
      <div className={styles.card_body}>
        <div className={styles.video_thumbnail}>
          <Link to={`/watch/${id}`}>
            {img && <img src={img} alt="Video thumbnail"/>}
            <span>{durationFormatter(durationInSeconds)}</span>
          </Link>
        </div>
          <p>
            {uploadedBy.username} ‒ {getVideoTotalVisits(videoVisits)}  {(getVideoTotalVisits(videoVisits) > 1) || (getVideoTotalVisits(videoVisits) == 0)  ? 'visualizations' : 'visualization'}
          </p>
          <p>
            {getUploadDateFormatted(uploadDate)} ◷
          </p>
      </div>
    </div>
  );
};

export default VideoCard;
