import { useState } from "react";
import { registerUser } from "../auth/AuthService";
import { Link, useNavigate } from "react-router-dom";
import toast from "react-hot-toast";

const Register = () => {

  const [name,setName]=useState("");
  const [email,setEmail]=useState("");
  const [password,setPassword]=useState("");
  const [role,setRole]=useState("ROLE_STUDENT");
  const [error,setError]=useState("");

  const navigate = useNavigate();

  const passwordRegex =
/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&]).{8,}$/;

  const handleRegister = async (e) => {

    e.preventDefault();
    setError("");

    if(!name || !email || !password){
      setError("All fields are required");
      return;
    }

    if(!passwordRegex.test(password)){
      setError(
        "Password must be at least 8 characters and include uppercase, lowercase, number, and special character."
      );
      return;
    }

    try{
      await registerUser({name,email,password,role});
      toast.success("Account created successfully 🎉");
      navigate("/login");

    }catch{
      setError("Registration failed");
    }
  };

  return (

<div className="min-h-screen flex items-center justify-center bg-slate-50 px-4">

  <div className="w-full max-w-md bg-white p-8 rounded-2xl shadow-lg border border-slate-200">

    <h1 className="text-3xl font-bold text-slate-800 text-center">
      Create Account
    </h1>

    <p className="text-slate-500 text-center mt-2 mb-6">
      Start your learning journey
    </p>

    {error && (
      <div className="mb-4 bg-red-50 border border-red-200 text-red-700 px-4 py-2 text-sm">
        {error}
      </div>
    )}

    <form onSubmit={handleRegister} className="space-y-5">

      <div>
        <label htmlFor="name" className="block text-sm font-medium text-slate-700 mb-1">
          Full Name
        </label>

        <input
          id="name"
          type="text"
          required
          value={name}
          onChange={(e)=>setName(e.target.value)}
          className="w-full px-4 py-2.5 border border-slate-300 rounded-lg"
        />
      </div>

      <div>
        <label htmlFor="email" className="block text-sm font-medium text-slate-700 mb-1">
          Email
        </label>

        <input
          id="email"
          type="email"
          required
          value={email}
          onChange={(e)=>setEmail(e.target.value)}
          className="w-full px-4 py-2.5 border border-slate-300 rounded-lg"
        />
      </div>

      <div>
        <label htmlFor="password" className="block text-sm font-medium text-slate-700 mb-1">
          Password
        </label>

        <input
          id="password"
          type="password"
          required
          value={password}
          onChange={(e)=>setPassword(e.target.value)}
          className="w-full px-4 py-2.5 border border-slate-300 rounded-lg"
        />

        <p className="text-xs text-slate-500 mt-1">
          Must contain 8+ characters, uppercase, lowercase, number, and special character.
        </p>
      </div>

      <div>
        <label htmlFor="role" className="block text-sm font-medium text-slate-700 mb-1">
          Role
        </label>

        <select
          id="role"
          value={role}
          onChange={(e)=>setRole(e.target.value)}
          className="w-full px-4 py-2.5 border border-slate-300 rounded-lg"
        >
          <option value="ROLE_STUDENT">Student</option>
          <option value="ROLE_TEACHER">Teacher</option>
        </select>
      </div>

      <button className="w-full py-3 bg-indigo-600 text-white rounded-lg">
        Create Account
      </button>

    </form>

    <p className="text-sm text-slate-600 text-center mt-6">
      Already have an account?{" "}
      <Link to="/login" className="text-indigo-600 font-medium hover:underline">
        Sign in
      </Link>
    </p>

  </div>
</div>
  );
};

export default Register;
