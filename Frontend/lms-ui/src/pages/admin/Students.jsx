import { useEffect, useState } from "react";
import API from "../../api/axios";
import { Container, Card, CardContent, Typography, Grid } from "@mui/material";
import "../../styles/admin.css";
import toast from "react-hot-toast";
// import {
//   Dialog,
//   DialogTitle,
//   DialogContent,
//   DialogContentText,
//   DialogActions,
//   Button,
// } from "@mui/material";

import BackButton from "../../components/BackButton";
export default function Students() {
  const [open, setOpen] = useState(false);
  const [selectedId, setSelectedId] = useState(null);

  const [students, setStudents] = useState([]);

  useEffect(() => {
    loadStudents();
  }, []);
  const handleOpen = (id) => {
    setSelectedId(id);
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
    setSelectedId(null);
  };

  const loadStudents = async () => {
    const res = await API.get("/api/admin/students");
    setStudents(res.data);
  };
  const deleteStudent = async () => {
    try {
      await API.delete(`/api/admin/users/${selectedId}`);

      toast.success("Student deleted ");

      loadStudents();
      handleClose();
    } catch {
      toast.error("Failed to delete student");
    }
  };

  return (
    <div className="space-y-8">
      <h1 className="text-3xl font-bold text-slate-800">All Students</h1>

      <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
        {students.map((s) => (
          <div
            key={s.id}
            className="
                rounded-2xl
                bg-white
                border border-slate-200
                p-6
                shadow-sm
                hover:shadow-xl
                transition
                "
          >
            <h2 className="font-bold text-lg">{s.name}</h2>

            <p className="text-slate-500 text-sm">{s.email}</p>

            <button
              onClick={() => handleOpen(s.id)}
              className="
                    mt-4
                    w-full
                    py-2
                    rounded-lg
                    bg-rose-50
                    text-rose-600
                    hover:bg-rose-100
                    transition
                    "
            >
              Delete
            </button>
          </div>
        ))}
      </div>
    </div>
  );
}
