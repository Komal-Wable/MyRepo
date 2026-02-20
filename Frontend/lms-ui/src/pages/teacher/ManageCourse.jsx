import { useEffect, useState } from "react";
import API from "../../api/axios";
import { useNavigate } from "react-router-dom";

import toast from "react-hot-toast";
import BackButton from "../../components/BackButton";

export default function ManageCourse() {
  const [courses, setCourses] = useState([]);
  const navigate = useNavigate();
  const [open, setOpen] = useState(false);
  const [selectedCourse, setSelectedCourse] = useState(null);

  const handleOpen = (courseId) => {
    setSelectedCourse(courseId);
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
    setSelectedCourse(null);
  };

  useEffect(() => {
    fetchCourses();
  }, []);

  const fetchCourses = async () => {
    const res = await API.get("/api/teacher/courses");
    setCourses(res.data);
  };

  const deleteCourse = async () => {
    try {
      await API.delete(`/api/teacher/courses/${selectedCourse}`);

      toast.success("Course deleted successfully!");

      fetchCourses();
      handleClose();
    } catch (err) {
      console.error(err);
      toast.error("Failed to delete course");
    }
  };

  return (
    <div
      className="
            min-h-screen
            bg-gradient-to-br
            from-[#eef2ff]
            via-[#ecfeff]
            to-[#fdf2f8]
            p-10
            "
    >
      <div className="max-w-7xl mx-auto space-y-10">
        <div className="space-y-4">
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
            Your Courses
          </h1>

          <p className="text-slate-600">Manage your teaching universe 🚀</p>
        </div>

        {courses.length === 0 && (
          <div
            className="
            rounded-3xl
            p-[1px]
            bg-gradient-to-br
            from-indigo-200
            via-cyan-200
            to-violet-200
            "
          >
            <div
              className="
            rounded-3xl
            bg-white/70
            backdrop-blur-xl
            p-16
            text-center
            "
            >
              <h3 className="text-2xl font-bold text-slate-700">
                No courses yet
              </h3>

              <p className="text-slate-500 mt-2">
                Create your first course and start teaching like a pro.
              </p>
            </div>
          </div>
        )}

        <div className="grid sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
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
                hover:scale-[1.03]
                transition
                "
            >
              <div
                className="
                h-full
                rounded-2xl
                bg-white/80
                backdrop-blur-lg
                border border-white/40

                p-5   
                shadow-md
                group-hover:shadow-xl
                transition
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
                  📚
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
                    text-sm
                    mt-1
                    line-clamp-2
                    "
                >
                  {course.description}
                </p>

                <div className="flex gap-2 mt-5">
                  <button
                    onClick={() =>
                      navigate(`/teacher/courses/${course.id}/assignments`)
                    }
                    className="
                    flex-1
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
                    Manage
                  </button>

                  <button
                    onClick={() => handleOpen(course.id)}
                    className="
                        flex-1
                        py-2
                        text-sm
                        rounded-lg
                        font-semibold
                        text-rose-600
                        bg-rose-50
                        hover:bg-rose-100
                        transition
                        "
                  >
                    Delete
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>

      {open && (
        <div
          className="
                fixed inset-0
                flex items-center justify-center
                bg-black/30
                backdrop-blur-md
                z-50
                "
        >
          <div
            className="
                w-full max-w-lg
                rounded-3xl
                p-[1px]
                bg-gradient-to-br
                from-rose-400
                to-red-500
                "
          >
            <div
              className="
                rounded-3xl
                bg-white/80
                backdrop-blur-xl
                p-10
                shadow-2xl
                "
            >
              <h2 className="text-3xl font-bold text-slate-800">
                Delete Course?
              </h2>

              <p className="text-slate-600 mt-3">
                This action cannot be undone and will remove all
                assignments,submissions and evaluations.
              </p>

              <div className="flex justify-end gap-4 mt-10">
                <button
                  onClick={handleClose}
                  className="
                    px-6 py-3
                    rounded-xl
                    border
                    border-slate-300
                    hover:bg-slate-100
                    transition
                    "
                >
                  Cancel
                </button>

                <button
                  onClick={deleteCourse}
                  className="
                    px-6 py-3
                    rounded-xl
                    font-semibold
                    text-white
                    bg-gradient-to-r
                    from-rose-500
                    to-red-600
                    hover:shadow-xl
                    transition
                    "
                >
                  Delete
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
