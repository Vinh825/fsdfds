import http from "../axios/axios.jsx";

export const api_getDashboardStats = async () => {
    return http.get(`/stats/dashboard`);
};


