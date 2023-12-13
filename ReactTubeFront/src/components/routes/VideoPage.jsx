import { useState, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useParams } from "react-router-dom";
import { getJwt } from "../customHooks/configureAppStore";
import { actions } from "../customHooks/actions";
import Header from "../../widgets/Header";

const VideoPage = () => {
  const { id } = useParams();
  const [videoUrl, setVideoUrl] = useState("");
  const [loading, setLoading] = useState(true);

  const dispatch = useDispatch();
  const jwt = useSelector(getJwt);

  useEffect(() => {
    if (!jwt) {
      dispatch({ type: actions.CHECK_STORAGE });
    }
  }, [jwt, dispatch]);

  useEffect(() => {
    jwt &&
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
          setLoading(false);
        })
        .catch((error) => {
          console.error("Error fetching video:", error);
          setLoading(false);
        });

    return () => {
      if (videoUrl) {
        setVideoUrl(null);
      }
    };
  }, [id]);

  if (loading) {
    return <div>Loading...</div>;
  }

  return (
    <>
      <Header />
      <section>
        {!loading && videoUrl &&(
          <video width="800" height="450" controls>
            <source src={videoUrl} type="video/mp4" />
            Your browser does not support the video tag.
          </video>
        )}
        <button>Like</button>
        {/* Aquí iría el componente de comentarios */}
        {/* Aquí iría el componente de videos recomendados */}
      </section>
    </>
  );
};

export default VideoPage;
