/* eslint-disable react/prop-types */
import { useForm } from "react-hook-form";
import { useSelector, useDispatch } from "react-redux";
import { getJwt } from "../../helpers/configureAppStore.js";
import { actions } from "../../helpers/actions.js";
import { useEffect, useState} from "react";

import styles from "./UserSettingsForm.module.css";


const UserSettingsForm = ({userInfo}) => {
    const { register, handleSubmit, formState: { errors } } = useForm();
    const dispatch = useDispatch();

    
    const [password, setPassword] = useState("");

    const onPasswordChange = (e) => {
        setPassword(e.target.value);
    }

    const passesMatch = (pass) => {
        return pass === password || "Passwords does not match";
    }

    const jwt = useSelector(getJwt) || null;

    useEffect(() => {
        if(!jwt){
            dispatch({type: actions.CHECK_STORAGE});
        }
    }, [jwt, dispatch]);

    const onSubmit = (data) => {
        console.log(data);
        // Perform validation and submit the form data to the backend
        fetch("http://localhost:8080/setting/update", {
            method: "PUT",
            body: JSON.stringify(data),
            headers: {
                "Content-Type": "application/json",
                "Authorization": jwt
            }
        })
            .then(response => response.json())
            .then(result => {
                // Handle the response from the backend
                console.log(result);
                dispatch({type: actions.STORE_NEW_SESSION, payload: result});
            })
            .catch(error => {
                // Handle any errors
                console.error(error);
            });
    };

    return (
        <form onSubmit={handleSubmit(onSubmit)} className={styles.user_settings_form}>
            <div className={`${styles.form_group}`}>
                <label className={styles.form_label}>Username</label>
                <br/>
                <input defaultValue={userInfo.username} placeholder={userInfo.username} className={styles.form_field} {...register("username", { required: true, minLength:3})} />
                {errors.username && <p className={styles.form_error}>This field is required</p>}
            </div>

            <div className={`${styles.form_group}`}>
                <label className={styles.form_label}>Email</label>
                <br/>
                <input defaultValue={userInfo.email} placeholder={userInfo.email} className={styles.form_field} {...register("email", { required: true, pattern: /^\S+@\S+$/i })} />
                {errors.email && <p className={styles.form_error}>Please enter a valid email address</p>}
            </div>

            <div className={`${styles.form_group}`}>
                <label className={styles.form_label}>First Name</label>
                <br/>
                <input className={styles.form_field} placeholder="First Name" defaultValue={userInfo.firstName || ""} type="text" {...register("firstName", { required: false })} />
            </div>

            <div className={`${styles.form_group}`}>
                <label className={styles.form_label}>Last Name</label>
                <br/>
                <input className={styles.form_field} placeholder="Last Name" defaultValue={userInfo.lastName || ""} type="text" {...register("lastName", { required: false })} />
            </div>

            <div className={`${styles.form_group}`}>
                <label className={styles.form_label}>Phone Number</label>
                <br/>
                <input className={styles.form_field} placeholder="Phone Number" defaultValue={userInfo.phoneNumber || ""} type="tel" {...register("phoneNumber", { required: false })} />
            </div>

            <div className={`${styles.form_group} ${styles.password_input}`} id={styles["border"]}>
                <label className={styles.form_label}>Password</label>
                <br/>
                <input className={styles.form_field} onInput={onPasswordChange}  placeholder="Insert your current password or change it" type="password" {...register("password", {
                        required: "This field is required",
                        minLength: {
                            value: 8,
                            message: "Password must be at least 8 characters long"
                        } ,
                        pattern: {
                            value: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$/,
                            message: "Password must contain at least one uppercase letter, one lowercase letter and one number"
                        }
                    })} />
                {errors.password && <p className={styles.form_error}>{errors.password.message}</p>}
            </div>

            <div className={`${styles.form_group} ${styles.password_input}`}>
                <label className={styles.form_label}>Confirm Password</label>
                <br/>
                <input placeholder="Repeat password" className={styles.form_field} type="password" {...register("confirmPassword",
                { required: "This field is required",
                            minLength: {
                                value: 8,
                                message: "Password must be at lest 8 characters long"
                            },
                            validate: passesMatch
                        }
                
                )} />
                {errors.confirmPassword && <p className={styles.form_error}>{errors.confirmPassword.message}</p>}
            </div>

            <button type="submit" className={styles.settings_submit_btn}>Submit</button>
        </form>
    );
};

export default UserSettingsForm;