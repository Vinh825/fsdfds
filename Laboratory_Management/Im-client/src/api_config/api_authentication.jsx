
import http from "../axios/axios";

export const login = async ({username, password}) => {
    const res = await http.post("/auth/token", {username, password});

    return res.data.result;
};

export const logout = async ({token}) => {
    await http.post(
        "/auth/logout",
        {token},
        {headers: {Authorization: `Bearer ${token}`}}
    );
};