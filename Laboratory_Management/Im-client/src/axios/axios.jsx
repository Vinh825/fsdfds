import axios from "axios";

const http = axios.create({ baseURL: "http://localhost:8080/identity" });

http.interceptors.request.use((config) => {
  const token = sessionStorage.getItem("token");
    console.log("Adding auth header, token exists:", !!token);
    if (token) config.headers["Authorization"] = `Bearer ${token}`;
    return config;
});



http.interceptors.response.use(
  (res) => res,
  (error) => {
    const status = error?.response?.status;
    const url = error?.config?.url || "";
    return Promise.reject(error);
  }
);

export default http;
