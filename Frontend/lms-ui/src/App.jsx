import { BrowserRouter, Routes, Route,Navigate } from "react-router-dom";

import Login from "./pages/Login";
import Register from "./pages/Register";
import { useEffect, useState } from "react";
import CourseAssignments from "./pages/teacher/CourseAssignments";
//import StudentResults from "./pages/student/StudentResults";
import StudentEvaluations from "./pages/student/StudentEvaluations";
import AssignmentDetails from "./pages/teacher/AssignmentDetails";

import TeacherDashboard from "./pages/teacher/TeacherDashboard";
import StudentDashboard from "./pages/student/StudentDashboard";
import StudentAssignments from "./pages/student/StudentAssignments";
import ProtectedRoute from "./routes/ProtectedRoute";
import TeacherSubmissions from "./pages/teacher/TeacherSubmissions";
import AdminPanel from "./pages/admin/AdminPanel";
import Teachers from "./pages/admin/Teachers";
import Students from "./pages/admin/Students";
import Navbar from "./components/Navbar";
import { Toaster } from "react-hot-toast";
import ManageCourse from "./pages/teacher/ManageCourse";
import Sdashboard from "./pages/student/Sdashboard";
import CreateCourse from "./pages/teacher/CreateCourse";
import CreateAssignment from "./pages/teacher/CreateAssignment";
import AssignmentDiscussion from "./pages/student/AssignmentDiscussion";
import TeacherLayout from "./components/layout/TeacherLayout";
import StudentLayout from "./components/layout/StudentLayout";
import AdminLayout from "./components/layout/AdminLayout";
function App() {
const [token, setToken] = useState(
    localStorage.getItem("token")
  );


  useEffect(() => {

    const syncToken = () => {
      setToken(localStorage.getItem("token"));
    };

    window.addEventListener("storage", syncToken);

    return () =>
      window.removeEventListener("storage", syncToken);

  }, []);

  
  return (
  
    <BrowserRouter>

      <Toaster
    position="top-right"
    reverseOrder={false}
    toastOptions={{
      duration: 3000,
      style: {
        borderRadius: "12px",
        background: "#111827",
        color: "#fff",
        fontSize: "14px",
      }
    }}
  />
      {token && <Navbar/>}
      <Routes>

       <Route path="/" element={<Login />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
<Route path="/admin" element={
  <AdminLayout><AdminPanel /> </AdminLayout>} />


<Route path="/admin/teachers" element={
  
  <AdminLayout><Teachers /> </AdminLayout>} />

  
<Route path="/admin/students" element={ <AdminLayout><Students /> </AdminLayout>} />

        {/* <Route
          path="/admin"
          element={
            <ProtectedRoute role="ROLE_ADMIN">
              <AdminDashboard />
            </ProtectedRoute>
          }
        /> */}
<Route
 path="/teacher/assignments/:assignmentId/submissions"
 element={
 <TeacherLayout>
 
 <TeacherSubmissions />
 
 </TeacherLayout>}
/>
<Route
  path="/teacher"
  element={<Navigate to="/teacher/dashboard" replace />}
/>


       <Route
  path="/teacher/dashboard"
  element={
    <TeacherLayout>
      <TeacherDashboard/>
    </TeacherLayout>
  }
/>

 <Route
   path="/teacher/courses"
   element={
     <TeacherLayout>
      <ProtectedRoute role="ROLE_TEACHER">
         <ManageCourse/>
      </ProtectedRoute>
      </TeacherLayout>
   }
/>
<Route path="/teacher/create-course"
 
element={  <TeacherLayout> <CreateCourse/>   </TeacherLayout>}

/>
<Route
   path="/teacher/manage-courses"
   element={<ManageCourse/>}
/>

<Route
 path="/student/assignments/:assignmentId/chat"
 element={
 <StudentLayout>
 
 <AssignmentDiscussion/>
 
 </StudentLayout>}
/>


<Route
  path="/teacher/courses/:courseId/assignments"
  element={
    <TeacherLayout>
    <ProtectedRoute role="ROLE_TEACHER">
      <CourseAssignments />
    </ProtectedRoute>
    </TeacherLayout>
  }
/>


   <Route path="/teacher/assignments/:assignmentId"
          element={
           <TeacherLayout>
           <ProtectedRoute role="ROLE_TEACHER">
          <AssignmentDetails/>
           </ProtectedRoute>
          </TeacherLayout>
          }
          />


<Route
   path="/teacher/assignments/:assignmentId/discussion"
   element={
   
           <TeacherLayout>
   
   
   <AssignmentDiscussion/>
   
   </TeacherLayout>
   }
/>


<Route
  path="/teacher/courses/:courseId/assignments/create"
  element={
    <TeacherLayout>
    <ProtectedRoute role="ROLE_TEACHER">
      <CreateAssignment />
    </ProtectedRoute>
    </TeacherLayout>
  }
/>
{/* <Route
 path="/student/results"
 element={<StudentResults />}
/> */}
<Route
 path="/student/evaluations"
 element={
   <StudentLayout>
 <StudentEvaluations />
 </StudentLayout>}
/>

<Route
   path="/student/courses/:courseId/assignments"
   element={
    <StudentLayout>
   
   <StudentAssignments />
   
   </StudentLayout>}
/>




        <Route
          path="/student"
          element={
            <StudentLayout>
            <ProtectedRoute role="ROLE_STUDENT">
              <Sdashboard />
            </ProtectedRoute>
            </StudentLayout>
          }
        />

         <Route
          path="/student/courses"
          element={
 <StudentLayout>

            <ProtectedRoute role="ROLE_STUDENT">
              <StudentDashboard/>
            </ProtectedRoute>
            </StudentLayout>
          }
        />

      </Routes>
    </BrowserRouter>
  );
}

export default App;
