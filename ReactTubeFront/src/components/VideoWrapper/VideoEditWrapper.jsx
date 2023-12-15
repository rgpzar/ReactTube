/* eslint-disable react/prop-types */
import styles from "./VideoWrapper.module.css";
import VideoCardWithEdit from "../VideoCard/VideoCardWithEdit.jsx";

const VideoWrapper = ({ videoList }) => {
    return (
        <div className={styles.video_wrapper}>
            {
                Array.isArray(videoList) && videoList.map((videoDto ) => {
                    return (
                        <VideoCardWithEdit key={videoDto.video.id} videoDto={videoDto} />
                    )
                })
            }
        </div>
    );
};

export default VideoWrapper;
