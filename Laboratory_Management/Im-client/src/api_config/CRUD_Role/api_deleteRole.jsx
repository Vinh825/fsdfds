import http from "../../axios/axios.jsx";

export const api_deleteRole = async (roleCode) => {
    return http.delete(`/roles/${roleCode}`);
};

