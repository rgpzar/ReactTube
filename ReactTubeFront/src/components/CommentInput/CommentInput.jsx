/* eslint-disable react/prop-types */
import { useState } from 'react';
import styles from "./CommentInput.module.css";
import {useCheckSession} from "../../helpers/customHooks/useCheckSession.js";
import {toast} from "react-toastify";

const CommentInput = ({ videoId, setNewComment}) => {
    const [comment, setComment] = useState('');

    const {jwt} = useCheckSession();
    const handleCommentChange = (e) => {
        setComment(e.target.value);
    };

    const submitComment = () => {
        if(comment === ''){
            return;
        }

        fetch(`http://localhost:8080/video/${videoId}/addComment`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Authorization: jwt
            },
            body: comment
        })
            .then(response => response.json())
            .then(data => {
                console.log('Comment submitted:', data);
                setNewComment(data);
                setComment('');
                toast.success('Comment submitted successfully!');
            })
            .catch(error => {
                console.error('Error submitting comment:', error);
                toast.error('Error submitting comment!');
            });
    };

    return (
        <div className={styles.comment_input_group}>
            <input
                type="text"
                placeholder="Add a public comment..."
                value={comment}
                onChange={handleCommentChange}
                onKeyDown={(e) => {
                    if (e.key === 'Enter') {
                        submitComment();
                    }
                }}
            />
            <button
                id={styles["comment_button"]}
                onClick={submitComment}
            >
                Comment
            </button>
        </div>
    );
}

export default CommentInput;
