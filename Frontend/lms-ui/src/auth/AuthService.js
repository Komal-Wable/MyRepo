import API from "../api/axios";

export const loginUser = async (data) => {

    const res = await API.post("/api/auth/login", data);

    localStorage.setItem("token", res.data.token);

    return res;
};

export const registerUser = (data) =>
    API.post("/api/auth/register", data);

export const logout = () =>
    localStorage.removeItem("token");
