/* eslint-disable react/prop-types */
import styles from "./VideoComment.module.css";
import {getUploadDateFormatted} from "../../helpers/videoDatesFormatter.js";

const VideoComment = ({ comment }) => {
    return (
        <div className={styles.comment_box}>
            <div className={styles.comment_card}>
                <p>{comment.message}</p>
                <span>{comment.username} - {getUploadDateFormatted(comment.id.time)} ◷</span>
            </div>
        </div>
    );
};

export default VideoComment;