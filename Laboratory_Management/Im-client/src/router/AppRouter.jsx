// import {Routes, Route} from "react-router-dom";
// import Login from "../pages/Auth/Login.jsx";
// import PrivateRoute from "../utils/PrivateRoute.jsx";
// import Dashboard from "../components/Dashboard.jsx";
// import Forget from "../pages/Forget/Forget.jsx";
// import {ChangePassword} from "../components/Header/ChangePassword/ChangePassword.jsx";
// import CreateUser from "../pages/Admin/CreateUser/CreateUser.jsx";
//
// export default function AppRouter() {
//     return (
//         <Routes>
//             <Route path="/login" element={<Login/>}/>
//             <Route path="/forget" element={<Forget/>}/>
//             <Route path="/dashboard"
//                 element={
//                     <PrivateRoute>
//                         <Dashboard/>
//                     </PrivateRoute>
//                 }
//             />
//             <Route path="/create"
//                 element={
//                     <PrivateRoute>
//                         <CreateUser/>
//                     </PrivateRoute>
//                 }
//             />
//             <Route path="/changePassword" element={<ChangePassword/>}/>
//         </Routes>
//     );
// }


import { Routes, Route } from "react-router-dom";
import Login from "../pages/Auth/Login.jsx";
import PrivateRoute from "../utils/PrivateRoute.jsx";
import Dashboard from "../components/Dashboard.jsx";
import Forget from "../pages/Forget/Forget.jsx";
import { ChangePassword } from "../components/Header/ChangePassword/ChangePassword.jsx";
import CreateUser from "../pages/Admin/CRUD_User/CreateUser/CreateUser.jsx";
import ListUser from "../pages/Admin/CRUD_User/ListUser/ListUser.jsx";
import DashboardHome from "../components/DashboardHome.jsx";
import CreateRole from "../pages/Admin/CRUD_Role/CreateRole/CreateRole.jsx";
import ListRole from "../pages/Admin/CRUD_Role/ListRole/ListRole.jsx";
import PatientManagementPage from '../pages/patient/PatientManagementPage.jsx';
import PatientDetailPage from '../pages/patient/PatientDetailPage.jsx';

export default function AppRouter() {
    return (
        <Routes>
            <Route path="/login" element={<Login />} />
            <Route path="/forget" element={<Forget />} />
            <Route path="/changePassword" element={<ChangePassword />} />
            <Route
                element={
                    <PrivateRoute>
                        <Dashboard />
                    </PrivateRoute>
                }
            >
                <Route path="/dashboard" element={
                    <PrivateRoute>
                        <DashboardHome />
                    </PrivateRoute>
                } />

                <Route path="/patient-home" element={
                    <PrivateRoute>
                        <PatientManagementPage />
                    </PrivateRoute>
                } />

                <Route path="/patient/:patientId" element={<PatientDetailPage />} />

                <Route path="/create" element={<CreateUser />} />
                <Route path="/users" element={<ListUser />} />

                <Route path="/roles/create" element={<CreateRole />} />
                <Route path="/roles/list" element={<ListRole />} />
            </Route>
        </Routes>
    );
}