import { useEffect, useState } from "react";
import API from "../../api/axios";
import { Container, Card, CardContent, Typography, Grid } from "@mui/material";
import "../../styles/admin.css";
import BackButton from "../../components/BackButton";
export default function Students() {

    const [students, setStudents] = useState([]);

    useEffect(() => {
        loadStudents();
    }, []);

    const loadStudents = async () => {
        const res = await API.get("/api/admin/students");
        setStudents(res.data);
    };

   return (<div className="list-container">

<div className="page-header">
              <BackButton/>
           </div>
  <h1 className="list-title">Students</h1>

  <div className="user-grid">

    {students.map(s => (

      <div key={s.id} className="user-card">

        <div className="user-name">{s.name}</div>

        <div className="user-email">{s.email}</div>

      </div>

    ))}

  </div>

</div>
)
}
