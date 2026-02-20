import { useState } from "react";
import { loginUser } from "../auth/AuthService";
import { useNavigate, Link } from "react-router-dom";
import { jwtDecode } from "jwt-decode";
import toast from "react-hot-toast";

const Login = () => {

  const [email,setEmail] = useState("");
  const [password,setPassword] = useState("");

  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();

    if(!email || !password){
      toast.error("Please fill all fields");
      return;
    }

    try{
      const res = await loginUser({email,password});
      const token = res.data.token;
      const decoded = jwtDecode(token);

      localStorage.setItem("token", token);
      window.dispatchEvent(new Event("storage"));

      if(decoded.role === "ROLE_ADMIN")
        navigate("/admin");
      else if(decoded.role === "ROLE_TEACHER")
        navigate("/teacher");
      else
        navigate("/student");

    }catch{
      toast.error("Invalid credentials");
    }
  };

  return (

<div className="min-h-screen flex items-center justify-center bg-slate-50 px-4">

  <div className="w-full max-w-md bg-white p-8 rounded-2xl shadow-lg border border-slate-200">

    <h1 className="text-3xl font-bold text-slate-800 text-center">
      SmartAMS
    </h1>

    <p className="text-slate-500 text-center mt-2 mb-6">
      Login to your account
    </p>

    <form onSubmit={handleLogin} className="space-y-5">

      <div>
        <label
          htmlFor="email"
          className="block text-sm font-medium text-slate-700 mb-1"
        >
          Email Address
        </label>

        <input
          id="email"
          type="email"
          required
          value={email}
          onChange={(e)=>setEmail(e.target.value)}
          placeholder="you@example.com"
          className="w-full px-4 py-2.5 border border-slate-300 rounded-lg"
        />
      </div>

      <div>
        <label
          htmlFor="password"
          className="block text-sm font-medium text-slate-700 mb-1"
        >
          Password
        </label>

        <input
          id="password"
          type="password"
          required
          value={password}
          onChange={(e)=>setPassword(e.target.value)}
          placeholder="Enter your password"
          className="w-full px-4 py-2.5 border border-slate-300 rounded-lg"
        />
      </div>

      <button className="w-full py-3 bg-indigo-600 text-white rounded-lg">
        Sign In
      </button>

    </form>

    <p className="text-sm text-slate-600 text-center mt-6">
      Don’t have an account?{" "}
      <Link to="/register" className="text-indigo-600 font-medium hover:underline">
        Create one
      </Link>
    </p>

  </div>
</div>
  );
};

export default Login;
