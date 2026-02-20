import { NavLink, useNavigate } from "react-router-dom";

export default function StudentLayout({ children }) {

    const navigate = useNavigate();

    const base =
        "flex items-center gap-3 px-4 py-3 rounded-xl font-medium transition-all";

    const active =
        "bg-gradient-to-r from-indigo-500 to-violet-500 text-white shadow";

    const inactive =
        "text-slate-600 hover:bg-slate-100";

    const logout = () => {
        localStorage.removeItem("token");
        navigate("/login");
    };

    return (

        <div className="min-h-screen flex bg-slate-100">


            <aside className="
                    w-72
                    bg-white
                    border-r
                    border-slate-200
                    p-6
                    flex flex-col
                    ">


                <div className="mb-10">

                    <h2 className="
                    text-2xl
                    font-extrabold
                    bg-gradient-to-r
                    from-indigo-600
                    to-cyan-600
                    bg-clip-text
                    text-transparent
                    ">
                    SmartAMS
                    </h2>

                    <p className="text-sm text-slate-400">
                        Student Portal
                    </p>

                </div>




                <div className="space-y-2">

                    <p className="text-xs font-semibold text-slate-400 uppercase">
                        Menu
                    </p>

                    <NavLink
                        to="/student"
                        end
                        className={({ isActive }) =>
                            `${base} ${isActive ? active : inactive}`
                        }
                    >
                        📊 Dashboard
                    </NavLink>

                    <NavLink
                        to="/student/courses"
                        className={({ isActive }) =>
                            `${base} ${isActive ? active : inactive}`
                        }
                    >
                        📚 My Courses
                    </NavLink>

                    <NavLink
                        to="/student/evaluations"
                        className={({ isActive }) =>
                            `${base} ${isActive ? active : inactive}`
                        }
                    >
                        🎓 Results
                    </NavLink>

                </div>

            </aside>




            <main className="flex-1 p-10">
                {children}
            </main>

        </div>
    );
}
