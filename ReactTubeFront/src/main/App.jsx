import AppRouter from './AppRouter';

import configureAppStore from '../components/customHooks/configureAppStore';
import { Provider } from 'react-redux';

function App() { 

  const store = configureAppStore();
  

  return (
    <>
        <Provider store={store}>
          <AppRouter/>
        </Provider>
    </>
  )
}

export default App
