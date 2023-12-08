/* eslint-disable react/prop-types */
import VideoCard from "./VideoCard";
import styles from "../resources/css/VideoWrapper.module.css";

const VideoWrapper = ({ videoList }) => {
  return (
    <div className={styles.video_wrapper}>
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

export default VideoWrapper;
