import axios from "axios";
import { jwtDecode } from "jwt-decode";

const API = axios.create({
    baseURL: "http://localhost:8080", 
});


API.interceptors.request.use((req) => {

    const token = localStorage.getItem("token");

    if (token) {

        
        req.headers.Authorization = `Bearer ${token}`;

        try {

            const decoded = jwtDecode(token);

         
            req.headers["X-User-Id"] =
                decoded.userId || decoded.sub;

        
            req.headers["X-User-Role"] =
                decoded.role;

        } catch (err) {

            console.log("Invalid token");
        }
    }

    return req;
});

export default API;
