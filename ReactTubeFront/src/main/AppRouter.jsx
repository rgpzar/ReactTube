import { Provider} from "react-redux";

import {BrowserRouter as Router, Routes, Route } from "react-router-dom";

import Home from "../components/routes/Home";
import Dashboard from "../components/routes/Dashboard";
import NotAuthorized from "../components/routes/NotAuthorized";
import { LogOut } from "../components/routes/LogOut";
import useLogin from "../components/customHooks/useLogin.js"
import Documentation from "../components/routes/Documentation";
import { Authenticate } from "../components/routes/Authenticate";

export const AppRouter = () => {
    const store = useLogin();

    return(
        <Provider store={store}>
            <Router>
                <Routes>
                    <Route exact path="/" Component={() => <Home/>}/>
                    <Route path="/dashboard" Component={() => <Dashboard/>}/>
                    <Route path="/auth" Component={() => <Authenticate/>}/>
                    <Route path='/forbidden' Component={() => <NotAuthorized/>}/>
                    <Route path="/logout" Component={() => <LogOut/>}/>
                    <Route path="/docs" Component={() => <Documentation/>} />
                </Routes>
            </Router>
        </Provider>
    );
}

export default AppRouter;