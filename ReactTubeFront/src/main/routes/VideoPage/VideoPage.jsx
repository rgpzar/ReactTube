import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import Header from "../../../components/Header/Header.jsx";
import RecommendedVideoWrapper from "../../../components/VideoWrapper/RecommendedVideoWrapper.jsx";
import VideoComment from "../../../components/VideoComment/VideoComment.jsx";
import { useCheckSession } from "../../../helpers/customHooks/useCheckSession.js";
import {getUploadDateFormatted, getVideoTotalVisits} from "../../../helpers/videoDatesFormatter.js";

import styles from "./VideoPage.module.css";
import CommentInput from "../../../components/CommentInput/CommentInput.jsx";

const VideoPage = () => {
  const { id } = useParams();
  const [videoUrl, setVideoUrl] = useState("");
  const [videoInfo, setVideoInfo] = useState({});
  const [newComment, setNewComment] = useState("");
  const [loadingVideoUrl, setLoadingVideoUrl] = useState(true);
  const [loadingVideoInfo, setLoadingVideoInfo] = useState(true);

  const { jwt } = useCheckSession();

  useEffect(() => {
    if (jwt) {
      setLoadingVideoUrl(true);
      fetch(`http://localhost:8080/video/client/watch/${id}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: jwt,
        },
      })
          .then((response) => response.text())
          .then((videoUrl) => {
            setVideoUrl(videoUrl);
          })
          .catch((error) => {
            console.error("Error fetching video:", error);
          })
          .finally(() => setLoadingVideoUrl(false));
    }
  }, [id, jwt]);

  useEffect(() => {
    if (jwt && id) {
      setNewComment("");
      setLoadingVideoInfo(true);
      fetch(`http://localhost:8080/video/${id}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: jwt,
        },
      })
          .then((response) => response.json())
          .then((data) => {
            setVideoInfo(data);
          })
          .catch((error) => {
            console.error("Error fetching additional data:", error);
          })
          .finally(() => setLoadingVideoInfo(false));
    }
  }, [id, jwt]);

  const { video, uploadedBy, videoComments, videoVisits } = videoInfo;

  if (loadingVideoUrl || loadingVideoInfo) {
    return <div>Loading...</div>;
  }

  if(newComment){
    videoComments.unshift(newComment);
  }



  return (
      <>
        <Header />
        <section className={styles.video_page_container}>
          <div className={styles.video_section}>
            <div className={styles.video_box}>
              {!loadingVideoUrl && videoUrl &&(
                  <video controls autoPlay>
                    <source src={videoUrl} type="video/mp4" />
                    Your browser does not support the video tag.
                  </video>
              )}
              <h2 id={styles["video_title"]}>{video.title}</h2>
              <div className={styles.video_info}>
                <h4>Uploaded by - {uploadedBy.username}</h4>
              </div>
              <div className={styles.video_description}>
                <p>
                  {getVideoTotalVisits(videoVisits)}  {(getVideoTotalVisits(videoVisits) > 1) || (getVideoTotalVisits(videoVisits) == 0)  ? 'visualizations' : 'visualization'}
                  - {getUploadDateFormatted(video.uploadDate)} ◷
                </p>
                {video.description}
              </div>
            </div>

            <div className={styles.comment_container}>
              <CommentInput videoId={video.id} setNewComment={setNewComment}/>

              {
                  videoComments && videoComments.map(comment => {
                    return (
                        <VideoComment key={comment.id.time} comment={comment}/>
                    );
                  })
              }
            </div>
          </div>
          <div className={styles.recommended_video_section}>
            <h2>Recommended videos</h2>
            <RecommendedVideoWrapper excludedId={video.id}/>
          </div>
        </section>
      </>
  );
};

export default VideoPage;




