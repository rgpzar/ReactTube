/* eslint-disable react/prop-types */
import { useForm } from 'react-hook-form';
import { useState } from 'react';
import styles from '../../main/routes/Authenticate/Authenticate.module.css';

export const Register = ({ handleRegister }) => {
    const {
        register,
        handleSubmit,
        formState: {errors}
    } = useForm(); 

    const [password, setPassword] = useState("");

    const onPasswordChange = (event) => {
        setPassword(event.target.value);
    }

    const passesMatch = (pass) => {
        return pass === password || "Passwords does not match";
    }

    return (
        <div className={`${styles.formContainer} ${styles.signUpContainer}`}>
            <form onSubmit={handleSubmit((data) => handleRegister(data))}>
                <h1>Create Account</h1>
                <span>or use your email for registration</span>
                {errors.username && <p className={styles.formError}>{errors.username.message}</p>}
                <input type="text" placeholder='Username' {...register("username", {
                    required: "This shouldn't be empty",
                    minLength: {
                        value: 3,
                        message: "Username must have at least 3 characters"
                    }
                })}/>

                {errors.email && <p className={styles.formError}>{errors.email.message}</p>}
                <input type="email" placeholder="Email" {...register("email", {
                    required: "This shouldn't be empty",
                    pattern: {
                        value: /^[\w-.]+@([\w-]+\.)+[\w-]{2,4}$/g,
                        message: "Enter a valid email"
                    }
                })}/>

                {errors.password && <p className={styles.formError}>{errors.password.message}</p>}
                <input type="password" onInput={onPasswordChange} placeholder="Password"{...register("password", {
                    required: "This shouldn't be empty",
                    minLength: {
                        value: 8,
                        message: "Password must have at least 8 characters"
                    },
                    pattern: {
                        value: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$/,
                        message: "Password must contain at least one uppercase letter, one lowercase letter and one number"
                    }
                })}/>

                {errors.passcheck && <p className={styles.formError}>{errors.passcheck.message}</p>}
                <input type='password' placeholder='Repeat password' {...register("passcheck", {
                    required: "This shouldn't be empty",
                    validate: passesMatch
                })}/>

                <input type="submit" value={"Sign Up"}/>
            </form>
        </div>
    );
}

export default Register;