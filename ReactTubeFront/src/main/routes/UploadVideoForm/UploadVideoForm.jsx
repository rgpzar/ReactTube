// Date: 2021/09/12
import { useForm } from 'react-hook-form';
import Header from '../../../components/Header/Header.jsx';
import {useDropzone} from "react-dropzone";

//Styles
import styles from './UploadVideoForm.module.css';
import {useCheckSession} from "../../../helpers/customHooks/useCheckSession.js";
import {useState} from "react";
import {toast} from "react-toastify";

const UploadVideoForm = () => {
    const {
            register,
            handleSubmit,
            setValue,
            formState: { errors }
    } = useForm();

    const {jwt} = useCheckSession();

    const [selectedFile, setSelectedFile] = useState(null);

    const onDrop = (acceptedFiles) => {
        // Asume que solo se acepta un archivo
        if (acceptedFiles.length) {
            setValue('file', acceptedFiles[0], { shouldValidate: true });
            setSelectedFile(acceptedFiles[0]);
            toast.info(`File ${acceptedFiles[0].name} selected!`);
        }
    };

    const { getRootProps, getInputProps } = useDropzone({
        onDrop,
        accept: 'video/mp4',
        multiple: false
    });

    const onSubmit = (data) => {


        const formData = new FormData();
        if (selectedFile) {
            formData.append('file', selectedFile);
        } else {
            // Manejar el caso en que no se haya seleccionado un archivo
            console.error("No file selected");
            return;
        }

        formData.append('title', data.title);
        formData.append('description', data.description);

        console.log(formData);

        fetch('http://localhost:8080/video/upload', {
            method: 'POST',
            body: formData,
            headers:{
                'Authorization': jwt
            }
        })
            .then((response) => {

                return response.text();
            })
            .then((data) => {
                console.log(data);
                toast.success("Video uploaded successfully!");
                //Clean the form
                setSelectedFile(null);
                setValue('title', '');
                setValue('description', '');
            })
            .catch((error) => {
                console.log(error);
                toast.error("Error uploading video");
            });
    };


    return (
        <>
            <Header current='UploadVideo'/>
            <section>
                <form onSubmit={handleSubmit(onSubmit)} className={styles.upload_video_form}>
                    <div {...getRootProps({className: styles.file_input_container})} >
                        <input {...getInputProps()} />
                        {!selectedFile && <label>Drop a video file here or click to select</label>}
                        {selectedFile && (
                            <label>File selected: {selectedFile.name}</label>
                        )}

                    </div>
                    {errors.file && <span>This field is required</span>}
                    <div className={styles.upload_video_container}>
                        <label htmlFor="title">Title:</label>
                        <input
                            type="text"
                            id="title"
                            name='title'
                            placeholder={'The title for your video...'}
                            {...register('title', {required: true})}
                        />

                    </div>
                    {errors.title && <span>This field is required</span>}
                    <div className={styles.upload_video_container}>
                        <label htmlFor="description">Description:</label>
                        <textarea
                            id="description"
                            name='description'
                            placeholder={'Insert a nice description...'}
                            {...register('description', {required: true})}
                        />

                    </div>
                    {errors.description && <span>This field is required</span>}
                    <button type="submit">Upload</button>
                </form>
            </section>
        </>
    );

};

export default UploadVideoForm;
