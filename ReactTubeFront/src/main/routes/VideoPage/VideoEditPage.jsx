import { useForm } from 'react-hook-form';
import { useCheckSession } from "../../../helpers/customHooks/useCheckSession.js";
import { useParams } from "react-router-dom";
import Header from "../../../components/Header/Header.jsx";
import {useEffect, useState} from "react";
import {toast} from "react-toastify";

import styles from "./VideoPage.module.css";
import {getUploadDateFormatted, getVideoTotalVisits} from "../../../helpers/videoDatesFormatter.js";

const VideoEditPage = () => {
    const { id } = useParams();
    const { register, handleSubmit, formState: { errors } } = useForm(); // Cambio aquí
    const { jwt } = useCheckSession();
    const [videoUrl, setVideoUrl] = useState("");
    const [loadingVideoUrl, setLoadingVideoUrl] = useState(true);
    const [videoInfo, setVideoInfo] = useState({});
    const [loadingVideoInfo, setLoadingVideoInfo] = useState(true);

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
                    toast.error("Error fetching video");
                })
                .finally(() => setLoadingVideoUrl(false));
        }
    }, [id, jwt]);

    useEffect(() => {
        if (jwt && id) {
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
                    toast.error("Error fetching additional data");
                })
                .finally(() => setLoadingVideoInfo(false));
        }
    }, [id, jwt]);


    const onSubmit = (data) => {

        console.log(data);
        // Send data to the server
        fetch(`http://localhost:8080/video/${id}`, {
            method: 'PUT',
            body: JSON.stringify(data),
            headers: {
                "Content-Type": "application/json",
                'Authorization': jwt
            },
        })
            .then((response) => {
                return response.json();
            })
            .then((result) => {
                // Handle the response
                console.log(result);
                toast.success("Video edited successfully");
                //wait 2 seconds before redirecting
                setTimeout(() => {
                    location.replace(`/dashboard`)
                }, 2000);
            })
            .catch((error) => {
                // Handle errors
                console.error(error);
                toast.error("Error editing video");
            });
    };

    const onDelete = () => {
        // Send data to the server
        fetch(`http://localhost:8080/video/${id}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': jwt
            },
        })
            .then((response) => {

                return response.json();
            })
            .then((result) => {
                // Handle the response
                console.log(result);
                toast.success("Video deleted successfully");
                setTimeout(() => {
                    location.replace(`/dashboard`)
                }, 2000);
            })
            .catch((error) => {
                // Handle errors
                console.error(error);
                toast.error("Error deleting video");
            });
    };

    const confirmDelete = () => {
        const confirm = window.confirm('Are you sure you want to delete this video?');
        if(confirm){
            onDelete();
        }
    }

    const { video, uploadedBy, videoVisits } = videoInfo;


    if (loadingVideoUrl || loadingVideoInfo) {
        return <div>Loading...</div>;
    }

    return (
        <>
            <Header/>
            <section>
                <div className={styles.video_section_edit}>
                    <div className={styles.video_box_edit}>
                        {!loadingVideoUrl && videoUrl && (
                            <video controls>
                                <source src={videoUrl} type="video/mp4"/>
                                Your browser does not support the video tag.
                            </video>
                        )}
                        <div className={styles.video_info_edit}>
                            <div className={styles.video_info_edit_info_box}>
                                <h2 id={styles["video_title"]}>{video.title}</h2>
                                <h4>Uploaded by - {uploadedBy.username}</h4>
                            </div>
                            <div className={styles.video_description_edit}>
                                <p>
                                {getVideoTotalVisits(videoVisits)} {(getVideoTotalVisits(videoVisits) > 1) || (getVideoTotalVisits(videoVisits) == 0) ? 'visualizations' : 'visualization'}
                                    - {getUploadDateFormatted(video.uploadDate)} ◷
                                </p>
                                {video.description}
                            </div>
                            <div className={styles.separation_border}></div>
                            <form onSubmit={handleSubmit(onSubmit)} className={styles.video_edit_form}>
                                <label htmlFor="title">Title:</label>
                                <input
                                    type="text"
                                    id="title"
                                    name="title"
                                    defaultValue={video.title}
                                    placeholder={video.title}
                                    {...register("title", {required: true, minLength: 4})}
                                />
                                {errors.title && <span>Title is required and must have at least 4 characters</span>}

                                <label htmlFor="description">Description:</label>
                                <input
                                    type="text"
                                    id="description"
                                    name="description"
                                    defaultValue={video.description}
                                    placeholder={video.description}
                                    {...register("description", {required: true, minLength: 4})}
                                />
                                {errors.description &&
                                    <span>Description is required and must have at least 4 characters</span>}

                                <div className={styles.video_edit_form_btn_container}>
                                    <button type="submit">Edit</button>
                                    <button className={styles.delete_btn} type='button' onClick={confirmDelete}>Delete</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>

            </section>
        </>
    );
};

export default VideoEditPage;

