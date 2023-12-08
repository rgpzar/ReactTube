import { useEffect, useState } from 'react';
import { useDispatch } from 'react-redux';
import { getJwt, getUser } from '../customHooks/useLogin';
import { useNavigate } from 'react-router';
import Login from '../../widgets/Login';
import Register from '../../widgets/Register';
import register from '../helpers/register';
import { getAuthSession } from '../helpers/login';
import { actions } from '../customHooks/actions';
import { useSelector } from 'react-redux';

import styles from '../../resources/css/Authenticate.module.css'; // Importar los estilos CSS Modules

/**
 * Authenticate component.
 * 
 * @returns {JSX.Element} The Authenticate component.
 */
/**
 * Authenticate component.
 * 
 * @returns {JSX.Element} The Authenticate component.
 */
const Authenticate = () => {
  const [rightPanelActive, setRightPanelActive] = useState(true);
  const [authError, setAuthError] = useState(null);
  const [transitionBtnText, setTransitionBtnText] = useState("Register");
  const navigate = useNavigate();
  const dispatch = useDispatch();

  dispatch({ type: actions.CHECK_STORAGE });

  const jwt = useSelector(getJwt);
  const user = useSelector(getUser);

  useEffect(() => {
    jwt && user && navigate('/home');
  });

  /**
   * Handles the screen transition between Login and Register in mobile phones.
   */
  const handleScreenTransition = () => {
    setTransitionBtnText(prevText => prevText === "Login" ? "Register" : "Login");
  }

  /**
   * Handles the login process.
   * 
   * @param {Object} formData - The form data containing the username and password.
   * @returns {Promise<void>} - A promise that resolves when the login process is complete.
   */
  const handleLogin = async (formData) => {
    const userDetails = {
      username: formData.username,
      password: formData.password
    };

    const newSession = await getAuthSession(userDetails);

    if (newSession) {
      dispatch({ type: actions.STORE_NEW_SESSION, payload: newSession });
      setAuthError(null);
      navigate('/home');
    } else {
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
    const newJwt = await getAuthSession({ username: user.username, password: user.password });
    console.log(newJwt)
    dispatch({ type: actions.STORE_NEW_SESSION, payload: newJwt });
    navigate('/home');
  };

  return (
    <div className={`${styles.container} ${rightPanelActive ? styles.rightPanelActive : ''}`} id='register'>
      <Register handleRegister={handleRegister} />
      <Login handleLogin={handleLogin} authError={authError} />
      <div className={styles.overlayContainer}>
        <div className={styles.overlay}>
          <div className={`${styles.overlayPanel} ${styles.overlayLeft}`}>
            <div className={styles.wrapper}>
              <svg>
                <text x="50%" y="50%" dy=".35em" textAnchor="middle">
                  React-Tube
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
                  React-Tube
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
      <a href={`#${transitionBtnText.toLowerCase()}`} className={styles.screen_transition}>
        <button onClick={handleScreenTransition}>{transitionBtnText === "Login" ? 'Register' : "Login"}</button>
      </a>
      <div className={`${styles.wrapper} ${styles.mobile_title}`}>
        <svg>
          <text x="50%" y="50%" dy=".35em" textAnchor="middle">
            React-Tube
          </text>
        </svg>
      </div>
    </div>
  );
};

export default Authenticate;

