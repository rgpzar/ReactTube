import AppRouter from './AppRouter';

import useLogin from '../components/customHooks/useLogin';
import { Provider } from 'react-redux';

function App() { 

  const store = useLogin();
  

  return (
    <>
        <Provider store={store}>
          <AppRouter/>
        </Provider>
    </>
  )
}

export default App
