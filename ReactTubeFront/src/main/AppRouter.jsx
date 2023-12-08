import {BrowserRouter as Router, Routes, Route } from "react-router-dom";

import Home from "../components/routes/Home";
import Dashboard from "../components/routes/Dashboard";
import NotAuthorized from "../components/routes/NotAuthorized";
import { LogOut } from "../components/routes/LogOut";
import Documentation from "../components/routes/Documentation";
import Authenticate from "../components/routes/Authenticate";

export const AppRouter = () => {



    return(
        <Router>
            <Routes>
                <Route path="/home" Component={() => <Home/>}/>
                <Route path="/dashboard" Component={() => <Dashboard/>}/>
                <Route path='/forbidden' Component={() => <NotAuthorized/>}/>
                <Route path="/logout" Component={() => <LogOut/>}/>
                <Route path="/docs" Component={() => <Documentation/>} />
                <Route exact path="/" Component={() => <Authenticate/>}/>
            </Routes>
            
        </Router>
    );
}

export default AppRouter;