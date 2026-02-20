import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import API from "../../api/axios";

import BackButton from "../../components/BackButton";
import toast from "react-hot-toast";

export default function StudentDashboard() {
  const [courses, setCourses] = useState([]);
  const [enrollments, setEnrollments] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const navigate = useNavigate();

  useEffect(() => {
    loadCourses();
  }, []);

  const loadCourses = async () => {
    try {
      setLoading(true);
      setError(null);

      const res = await API.get("/api/student/courses");
      setCourses(res.data);
      checkEnrollments(res.data);
    } catch (error) {
      console.error("Error loading courses:", error);
      setError("Failed to load courses. Please try again.");
    } finally {
      setLoading(false);
    }
  };


  const checkEnrollments = async (coursesList) => {
    try {
      const statusMap = {};

      await Promise.all(
        coursesList.map(async (course) => {
          try {
            const res = await API.get(
              `/api/student/courses/${course.id}/enrollment-status`,
            );
            statusMap[course.id] = res.data;
          } catch (error) {
            console.error(
              `Error checking enrollment for course ${course.id}:`,
              error,
            );
            statusMap[course.id] = false;
          }
        }),
      );

      setEnrollments(statusMap);
    } catch (error) {
      console.error("Error checking enrollments:", error);
    }
  };

  const enroll = async (courseId) => {
    try {
      await API.post(`/api/student/courses/${courseId}/enroll`);
      toast.success("Enrolled Successfully!");
      loadCourses();
    } catch (error) {
      console.error("Enrollment error:", error);
      toast.error("Enrollment failed. Please try again.");
    }
  };

  if (loading) {
    return (
      <div style={{ padding: 40, textAlign: "center" }}>
        <h1>Student Dashboard</h1>
        <p>Loading your courses...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div style={{ padding: 40 }}>
        <h1>Student Dashboard</h1>
        <p style={{ color: "red" }}>{error}</p>
        <button
          onClick={loadCourses}
          style={{
            padding: "8px 16px",
            backgroundColor: "#007bff",
            color: "white",
            border: "none",
            borderRadius: 4,
            cursor: "pointer",
          }}
        >
          Try Again
        </button>
      </div>
    );
  }

  return (
    <div
      className="
            min-h-screen
            bg-gradient-to-br
            from-[#eef2ff]
            via-[#ecfeff]
            to-[#fdf2f8]
            p-8
            "
    >
      <div className="max-w-6xl mx-auto space-y-10">
        <div>
          <h1
            className="
                text-4xl
                font-extrabold
                bg-gradient-to-r
                from-indigo-600
                via-cyan-600
                to-violet-600
                bg-clip-text
                text-transparent
                "
          >
            Browse Courses
          </h1>

          <p className="text-slate-600 mt-2">
            Enroll in courses and start learning 🚀
          </p>
        </div>

        {courses.length === 0 && (
          <div
            className="
                rounded-3xl
                p-[1px]
                bg-gradient-to-br
                from-indigo-300
                via-cyan-300
                to-violet-300
                "
          >
            <div
              className="
                rounded-3xl
                bg-white/75
                backdrop-blur-xl
                p-14
                text-center
                "
            >
              <h3 className="text-2xl font-bold text-slate-700">
                No Courses Available
              </h3>

              <p className="text-slate-500 mt-2">
                You are not enrolled in any courses yet.
              </p>
            </div>
          </div>
        )}

        <div
          className="grid sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6"
        >
          {courses.map((course) => (
            <div
              key={course.id}
              className="
                group
                rounded-2xl
                p-[1px]
                bg-gradient-to-br
                from-indigo-300
                via-cyan-300
                to-violet-300
                transition
                hover:scale-[1.02]
                "
            >
              <div
                className="
                    h-full
                    rounded-2xl
                    bg-white/85
                    backdrop-blur-lg
                    border border-white/40

                    p-5   
                    shadow-sm
                    group-hover:shadow-xl
                    transition
                    flex flex-col
                    "
              >
                <div
                  className="
                    w-10 h-10
                    rounded-xl
                    flex items-center justify-center
                    text-lg
                    bg-gradient-to-br
                    from-indigo-500
                    to-cyan-500
                    text-white
                    mb-3
                    "
                >
                  📘
                </div>

                <h2
                  className="
                    text-lg
                    font-bold
                    text-slate-800
                    line-clamp-1
                    "
                >
                  {course.title}
                </h2>

                <p
                  className="
                    text-slate-500
                    mt-1
                    text-sm
                    line-clamp-2
                    flex-grow
                    "
                >
                  {course.description}
                </p>

                <div className="mt-4">
                  {enrollments[course.id] ? (
                    <button
                      onClick={() =>
                        navigate(`/student/courses/${course.id}/assignments`)
                      }
                      className="
                            w-full
                            py-2
                            text-sm
                            rounded-lg
                            font-semibold
                            text-white
                            bg-gradient-to-r
                            from-emerald-500
                            to-teal-500
                            hover:shadow-md
                            transition
                            "
                    >
                      View Assignments
                    </button>
                  ) : (
                    <button
                      onClick={() => enroll(course.id)}
                      className="
                            w-full
                            py-2
                            text-sm
                            rounded-lg
                            font-semibold
                            text-white
                            bg-gradient-to-r
                            from-indigo-500
                            to-violet-500
                            hover:shadow-md
                            transition
                            "
                    >
                      Enroll
                    </button>
                  )}
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
