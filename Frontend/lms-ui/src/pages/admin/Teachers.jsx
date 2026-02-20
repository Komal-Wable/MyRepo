import { useEffect, useState } from "react";
import API from "../../api/axios";

// import {
//     Container,
//     Card,
//     CardContent,
//     Typography,
//     Button,
//     Grid
// } from "@mui/material";
import toast from "react-hot-toast";

import BackButton from "../../components/BackButton";
// import {
//   Dialog,
//   DialogTitle,
//   DialogContent,
//   DialogContentText,
//   DialogActions,

// } from "@mui/material";

export default function Teachers() {
  const [open, setOpen] = useState(false);
  const [selectedId, setSelectedId] = useState(null);

  const [teachers, setTeachers] = useState([]);
  const handleOpen = (id) => {
    setSelectedId(id);
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
    setSelectedId(null);
  };

  useEffect(() => {
    loadTeachers();
  }, []);

  const loadTeachers = async () => {
    const res = await API.get("/api/admin/teachers");
    setTeachers(res.data);
  };

  const approve = async (id) => {
    await API.put(`/api/admin/teachers/${id}/approve`);

    toast.success("Teacher Approved ");

    loadTeachers();
  };

  const deleteUser = async () => {
    try {
      await API.delete(`/api/admin/users/${selectedId}`);

      toast.success("User Deleted ");

      loadTeachers();

      handleClose();
    } catch {
      toast.error("Failed to delete user");
    }
  };

  return (
    <div className="space-y-8">
      <h1 className="text-3xl font-bold text-slate-800">All Teachers</h1>

      <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
        {teachers.map((t) => (
          <div
            key={t.id}
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
            <h2 className="font-bold text-lg">{t.name}</h2>

            <p className="text-slate-500 text-sm">{t.email}</p>

            <span
              className={`inline-block mt-2 px-3 py-1 text-xs rounded-full
                    ${
                    t.approved ? "bg-emerald-100 text-emerald-600" : "bg-amber-100 text-amber-600"
                    }
                    `}
            >
              {t.approved ? "Approved" : "Pending"}
            </span>

            {!t.approved && (
              <button
                onClick={() => approve(t.id)}
                className="
                        mt-4
                        w-full
                        py-2
                        rounded-lg
                        font-semibold
                        text-white
                        bg-gradient-to-r
                        from-indigo-500
                        to-violet-500
                        hover:shadow-lg
                        transition
                        "
              >
                Approve
              </button>
            )}

            <button
              onClick={() => handleOpen(t.id)}
              className="
                        mt-2
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
