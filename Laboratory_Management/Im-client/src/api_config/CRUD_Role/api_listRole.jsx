import http from "../../axios/axios.jsx";

export const api_listRole = async (params = {}) => {
    return http.get(`/roles/list`, { params });
};

