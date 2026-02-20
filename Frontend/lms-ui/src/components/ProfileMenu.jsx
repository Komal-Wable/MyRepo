import { useEffect, useState, useRef } from "react";
import API from "../api/axios";

import "../styles/profile.css";
import { jwtDecode } from "jwt-decode";

export default function ProfileMenu() {
  const [user, setUser] = useState(null);
  const [open, setOpen] = useState(false);

  const menuRef = useRef();

  useEffect(() => {
    const token = localStorage.getItem("token");

    if (!token) return;

    const decoded = jwtDecode(token);

    loadUser(decoded.userId);
  }, []);

  const loadUser = async (id) => {
    try {
      const res = await API.get(`/api/internal/users/${id}`);

      setUser(res.data);
    } catch (err) {
      console.error("User fetch failed");
    }
  };

  useEffect(() => {
    const handler = (e) => {
      if (menuRef.current && !menuRef.current.contains(e.target)) {
        setOpen(false);
      }
    };

    document.addEventListener("mousedown", handler);

    return () => document.removeEventListener("mousedown", handler);
  }, []);

  const logout = () => {
    localStorage.removeItem("token");

    window.location.href = "/login";
  };

  if (!user) return null;

  const initials = user.name
    .split(" ")
    .map((n) => n[0])
    .join("")
    .toUpperCase();

  return (
    <div className="profile-wrapper" ref={menuRef}>
      <div className="avatar" onClick={() => setOpen(!open)}>
        {initials}
      </div>

      {open && (
        <div className="profile-dropdown">
          <div className="profile-header">
            <div className="avatar-large">{initials}</div>

            <div>
              <div className="profile-name">{user.name}</div>

              <div className="profile-email">{user.email}</div>
            </div>
          </div>

          <div className="profile-role">{user.role}</div>

          <button className="logout-btn" onClick={logout}>
            Logout
          </button>
        </div>
      )}
    </div>
  );
}
