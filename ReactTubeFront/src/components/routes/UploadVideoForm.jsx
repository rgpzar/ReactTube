// Date: 2021/09/12
import { useForm } from 'react-hook-form';
import Header from '../../widgets/Header';
import { useEffect, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { getJwt } from '../customHooks/useLogin';
import { actions } from '../customHooks/actions';
import { useNavigate } from 'react-router';
import Swal from 'sweetalert2';
import withReactContent from 'sweetalert2-react-content';

const UploadVideoForm = () => {
    const { register, handleSubmit, formState: { errors } } = useForm();
    const jwt = useSelector(getJwt) || null;
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const MySwal = withReactContent(Swal);

    useEffect(() => {
        console.log(jwt);

        if(!jwt){
            dispatch({type: actions.CHECK_STORAGE});
        }
    }, [jwt, navigate, dispatch]);

    const onSubmit = (data) => {


        const formData = new FormData();
        formData.append('file', data.file);
        formData.append('title', data.title);
        formData.append('description', data.description);

        fetch('http://localhost:8080/video/upload', {
            method: 'POST',
            body: formData,
            headers:{
                'Authorization': jwt,
                'content-type': 'multipart/form-data'
            }
        })
            .then((response) => {

                return response.text();
            })
            .then((data) => {
                console.log(data);
                MySwal.fire({
                    title: 'Video uploaded!',
                    icon: 'success',
                });
            })
            .catch((error) => {
                console.log(error);
                MySwal.fire({
                    title: 'Error uploading video!',
                    text: error.message,
                    icon: 'question'               
                });
            });
    };


    return (
        <>
            <Header current='UploadVideo'/>
            <form onSubmit={handleSubmit(onSubmit)}>
                <div>
                    <label htmlFor="file">File:</label>
                    <input
                        type="file"
                        id="file"
                        {...register('file', { required: true })}
                        name='file'
                    />
                    {errors.file && <span>This field is required</span>}
                </div>
                <div>
                    <label htmlFor="title">Title:</label>
                    <input
                        type="text"
                        id="title"
                        name='title'
                        {...register('title', { required: true })}
                    />
                    {errors.title && <span>This field is required</span>}
                </div>
                <div>
                    <label htmlFor="description">Description:</label>
                    <textarea
                        id="description"
                        name='description'
                        {...register('description', { required: true })}
                    />
                    {errors.description && <span>This field is required</span>}
                </div>
                <button type="submit">Upload</button>
            </form>
        </>
    );
    
};

export default UploadVideoForm;