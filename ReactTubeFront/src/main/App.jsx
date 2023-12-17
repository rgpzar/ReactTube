import AppRouter from './AppRouter';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

import configureAppStore from '../helpers/configureAppStore.js';
import { Provider } from 'react-redux';

function App() { 

  const store = configureAppStore();
  

  return (
    <>
        <Provider store={store}>
          <AppRouter/>
        </Provider>
        <ToastContainer
            position="bottom-right"
            autoClose={2000}
            hideProgressBar={false}
            newestOnTop={false}
            closeOnClick={true}
            rtl={false}
            pauseOnFocusLoss={true}
            draggable={false}
            pauseOnHover={false}
        />
    </>
  )
}

export default App
