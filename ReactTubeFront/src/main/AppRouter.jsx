import {BrowserRouter as Router, Routes, Route } from "react-router-dom";

import Home from "./routes/Home/Home.jsx";
import Dashboard from "./routes/Dashboard/Dashboard.jsx";
import { LogOut } from "./routes/LogOut/LogOut.jsx";
import Authenticate from "./routes/Authenticate/Authenticate.jsx";
import UploadVideoForm from "./routes/UploadVideoForm/UploadVideoForm.jsx";
import VideoPage from "./routes/VideoPage/VideoPage.jsx";
import VideoEditPage from "./routes/VideoPage/VideoEditPage.jsx";

export const AppRouter = () => {



    return(
        <Router>
            <Routes>
                <Route path="/home" Component={() => <Home/>}/>
                <Route path="/dashboard" Component={() => <Dashboard/>}/>
                <Route path="/logout" Component={() => <LogOut/>}/>
                <Route path="/uploadVideo" Component={() => <UploadVideoForm/>}/>
                <Route path="watch/:id" Component={() => <VideoPage/>}/>
                <Route path="edit/:id" Component={() => <VideoEditPage/>}/>
                <Route exact path="/" Component={() => <Authenticate/>}/>
            </Routes>
            
        </Router>
    );
}

export default AppRouter;