import http from "../../axios/axios.jsx";

export const api_createUser = async (
    email,
    firstName,
    lastName,
    gender,
    phoneNumber,
    address,
    dateOfBirth
) => {
    return http.post(`/users/create`, {
        email,
        firstName,
        lastName,
        gender,
        phoneNumber,
        address,
        dateOfBirth
    });
};

