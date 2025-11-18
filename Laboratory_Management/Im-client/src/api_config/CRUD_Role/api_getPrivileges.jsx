import http from "../../axios/axios.jsx";

export const api_getPrivileges = async () => {
    return http.get(`/roles/privileges`);
};

