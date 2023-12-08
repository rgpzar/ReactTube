/* eslint-disable react/prop-types */
import styles from '../resources/css/Authenticate.module.css';
import { useForm } from 'react-hook-form';

export const Login = ({ handleLogin, authError }) => {
    const {
        register,
        handleSubmit,
        formState: {errors}
    } = useForm();

    return(
        <div className={`${styles.formContainer} ${styles.signInContainer}`} id='login'>
                    <form onSubmit={handleSubmit((data) => handleLogin(data))} method="POST">
                        <h1>Log in</h1>
                        {errors.username && <p className={styles.formError}>{errors.username.message}</p>}
                        <input type="text" placeholder="Username" {...register("username", {
                            required: "This shouldn't be empty"
                        })}/>

                        {errors.password && <p className={styles.formError}>{errors.password.message}</p>}
                        {authError && <p className={styles.formError}>{authError}</p>}
                        <input type="password" placeholder="Password" {...register("password", {
                            required: "This shouldn't be empty"
                        })}/>

                        <a href="#">Forgot your password?</a>
                        <input type="submit" value={"Log In"}></input>
                    </form>
        </div>
    )
}
export default Login;