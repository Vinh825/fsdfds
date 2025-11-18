import http from "../../axios/axios.jsx";

export const api_createRole = async (
    name,
    roleCode,
    description,
    privileges
) => {
    return http.post(`/roles/create`, {
        name,
        roleCode,
        description,
        privileges
    });
};

