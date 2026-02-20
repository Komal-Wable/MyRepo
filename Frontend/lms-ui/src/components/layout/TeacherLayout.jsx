import { NavLink } from "react-router-dom";

export default function TeacherLayout({ children }) {
  const base =
    "flex items-center gap-3 px-4 py-3 rounded-xl font-medium transition-all";

  const active =
    "bg-gradient-to-r from-indigo-500 to-violet-500 text-white shadow";

  const inactive = "text-slate-600 hover:bg-slate-100";

  return (
    <div className="min-h-screen flex bg-slate-100">
      <aside
        className="
            w-72
            bg-white
            border-r
            border-slate-200
            p-6
            flex flex-col
            "
      >
        <div className="mb-10">
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

          <p className="text-sm text-slate-600">Teacher Portal</p>
        </div>

        <div className="space-y-2">
          <p className="text-xs font-semibold text-slate-600 uppercase">Menu</p>

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
      </aside>

      <main className="flex-1 p-10">{children}</main>
    </div>
  );
}
