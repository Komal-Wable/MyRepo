import { NavLink, useParams, useNavigate } from "react-router-dom";

export default function TeacherSidebar() {
  const { courseId } = useParams();
  const navigate = useNavigate();

  const base =
    "flex items-center gap-3 px-4 py-3 rounded-xl font-medium transition-all";

  const active =
    "bg-gradient-to-r from-indigo-500 to-violet-500 text-white shadow";

  const inactive = "text-slate-600 hover:bg-slate-100";

  return (
    <aside
      className="
        w-72
        bg-white
        border-r
        border-slate-200
        p-6
        flex flex-col
        min-h-screen
        "
    >
      
      <div
        className="mb-10 cursor-pointer"
        onClick={() => navigate("/teacher")}
      >
        <h2
          className="
            text-2xl
            font-extrabold
            bg-gradient-to-r
            from-indigo-600
            to-cyan-600
            bg-clip-text
            text-transparent
            "
        >
          SmartAMS
        </h2>

        <p className="text-sm text-slate-500">Teacher Portal</p>
      </div>

      {!courseId && (
        <div className="space-y-2">
          <p className="text-xs font-semibold text-slate-400 uppercase">Menu</p>

          <NavLink
            to="/teacher"
            end
            className={({ isActive }) =>
              `${base} ${isActive ? active : inactive}`
            }
          >
            📊 Dashboard
          </NavLink>

          <NavLink
            to="/teacher/courses"
            className={({ isActive }) =>
              `${base} ${isActive ? active : inactive}`
            }
          >
            📚 Courses
          </NavLink>

          <NavLink
            to="/teacher/create-course"
            className={({ isActive }) =>
              `${base} ${isActive ? active : inactive}`
            }
          >
            ➕ Create Course
          </NavLink>
        </div>
      )}

      
      {courseId && (
        <div className="space-y-2">
          <button
            onClick={() => navigate("/teacher/courses")}
            className="
            mb-4
            text-sm
            text-indigo-600
            hover:underline
            "
          >
            ← Back to Courses
          </button>

          <p className="text-xs font-semibold text-slate-400 uppercase">
            Course Menu
          </p>

          <NavLink
            to={`/teacher/courses/${courseId}/assignments`}
            className={({ isActive }) =>
              `${base} ${isActive ? active : inactive}`
            }
          >
            📝 Assignments
          </NavLink>

          <NavLink
            to={`/teacher/courses/${courseId}/assignments/create`}
            className={({ isActive }) =>
              `${base} ${isActive ? active : inactive}`
            }
          >
            ➕ Create Assignment
          </NavLink>

          <NavLink
            to={`/teacher/courses/${courseId}/submissions`}
            className={({ isActive }) =>
              `${base} ${isActive ? active : inactive}`
            }
          >
            📥 Submissions
          </NavLink>

          <NavLink
            to={`/teacher/courses/${courseId}/discussion`}
            className={({ isActive }) =>
              `${base} ${isActive ? active : inactive}`
            }
          >
            💬 Discussion
          </NavLink>
        </div>
      )}

    
      <div className="mt-auto border-t pt-6">
        <div className="flex items-center gap-3">
          <div
            className="
            w-10 h-10
            rounded-full
            bg-gradient-to-r
            from-indigo-500
            to-violet-500
            flex items-center justify-center
            text-white font-bold
            "
          >
            T
          </div>

          <div>
            <p className="font-semibold text-slate-700">Teacher</p>

            <p className="text-xs text-slate-500">Online</p>
          </div>
        </div>
      </div>
    </aside>
  );
}
