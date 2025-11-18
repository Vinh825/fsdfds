
import Header from "./Header/header/Header.jsx";
import { Outlet } from "react-router-dom";
import './Dashboard.css';

export default function Dashboard() {
    return (
        <div className="dashboard-layout">

            <Header/>

            <main className="dashboard-layout-main">
                <Outlet />
            </main>
        </div>
    );
}