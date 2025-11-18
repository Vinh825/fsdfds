import http from "../../axios/axios.jsx";

export const api_updateRole = async (roleCode, name, description, privileges) => {
    const roleData = {
        name,
        description,
        privileges
    };
    return http.put(`/roles/${roleCode}`, roleData);
};

