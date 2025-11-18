import http from "../../axios/axios.jsx";

export const api_listUser = async (params = {}) => {
    return http.get(`/users`, { params });
};
