import { useState } from 'react';
import { useDispatch } from 'react-redux';
import { useNavigate } from 'react-router';
import Login from '../../widgets/Login';
import Register from '../../widgets/Register';
import register from '../helpers/register';
import getAuthToken, { getAuthSession } from '../helpers/login';
import { actions } from '../customHooks/actions';
import styles from '../../resources/css/Authenticate.module.css'; // Importar los estilos CSS Modules

export const Authenticate = () => {
  const [rightPanelActive, setRightPanelActive] = useState(true);
  const [authError, setAuthError] = useState(null);
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const handleLogin = async (formData) => {
    const userDetails = {
      username: formData.username,
      password: formData.password
    };

    const newSession = await getAuthSession(userDetails);

    
    if (newSession) {
      dispatch({ type: actions.STORE_NEW_SESSION, payload: newSession });
      setAuthError(null);
      navigate('/');
    }else{
      setAuthError("Invalid username or password.");
    }
  };

  const handleRegister = async (formData) => {

    const user = {
      email: formData.email,
      username: formData.username,
      password: formData.password,
      role: 'USER',
    };

    const registeredUser = await register(user);
    console.log(registeredUser);
    const newJwt = await getAuthToken({ username: user.username, password: user.password });
    console.log(newJwt)
    dispatch({ type: actions.STORE_NEW_SESSION, payload: {jwt: newJwt, user: registeredUser} });
    navigate('/');
  };

  return (
    <div className={rightPanelActive ? `${styles.container} ${styles.rightPanelActive}` : styles.container}>
      <Register handleRegister={handleRegister} />
      <Login handleLogin={handleLogin} authError={authError}/>
      <div className={styles.overlayContainer}>
        <div className={styles.overlay}>
          <div className={`${styles.overlayPanel} ${styles.overlayLeft}`}>
            <div className={styles.wrapper}>
              <svg>
                <text x="50%" y="50%" dy=".35em" textAnchor="middle">
                  Airport API
                </text>
              </svg>
            </div>
            <h1>Welcome Back!</h1>
            <p>To keep connected with us, please login with your personal info</p>
            <button className={styles.ghost} id="Log In" onClick={() => setRightPanelActive(false)}>
              Log In
            </button>
          </div>
          <div className={`${styles.overlayPanel} ${styles.overlayRight}`}>
            <div className={styles.wrapper}>
              <svg>
                <text x="50%" y="50%" dy=".35em" textAnchor="middle">
                  Airport API
                </text>
              </svg>
            </div>
            <h1>Hello, Friend!</h1>
            <p>Enter your personal details and start your journey with us</p>
            <button className={styles.ghost} id="signUp" onClick={() => setRightPanelActive(true)}>
              Sign Up
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Authenticate;
