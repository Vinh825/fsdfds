import http from "../../axios/axios.jsx";

export const api_getAllRoles = async () => {
    return http.get(`/roles`);
};


