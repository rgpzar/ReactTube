import { useForm } from "react-hook-form";
import { useSelector, useDispatch } from "react-redux";
import { getJwt } from "./customHooks/useLogin";
import { actions } from "./customHooks/actions";
import { useEffect } from "react";


const UserSettingsForm = () => {
    const { register, handleSubmit, formState: { errors } } = useForm();
    const dispatch = useDispatch();

    const jwt = useSelector(getJwt) || null;

    useEffect(() => {
        if(!jwt){
            dispatch({type: actions.CHECK_STORAGE});
        }
    }, [jwt, dispatch]);

    const onSubmit = (data) => {
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
            })
            .catch(error => {
                // Handle any errors
                console.error(error);
            });
    };

    return (
        <form onSubmit={handleSubmit(onSubmit)}>
            <div>
                <label>Username</label>
                <input {...register("username", { required: true })} />
                {errors.username && <span>This field is required</span>}
            </div>

            <div>
                <label>Email</label>
                <input {...register("email", { required: true, pattern: /^\S+@\S+$/i })} />
                {errors.email && <span>Please enter a valid email address</span>}
            </div>

            <div>
                <label>Password</label>
                <input type="password" {...register("password", { required: true, minLength: 6 })} />
                {errors.password && <span>Password must be at least 6 characters long</span>}
            </div>

            <button type="submit">Submit</button>
        </form>
    );
};

export default UserSettingsForm;