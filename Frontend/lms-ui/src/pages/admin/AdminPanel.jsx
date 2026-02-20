import { useNavigate } from "react-router-dom";

export default function AdminPanel() {
  const navigate = useNavigate();

  return (
    <div className="space-y-8">
      <div
        className="
            relative
            overflow-hidden
            rounded-2xl
            px-10
            py-7
            bg-gradient-to-r
            from-indigo-600
            via-violet-600
            to-cyan-600
            text-white
            shadow-lg
            "
      >
        <h1 className="text-3xl font-bold">Admin Control Center 👑</h1>

        <p className="mt-1 text-white/80 text-sm max-w-xl">
          Monitor platform activity and keep your LMS running smoothly.
        </p>
      </div>

      <div className="grid md:grid-cols-3 gap-6">
        <div
          className="
            rounded-2xl
            bg-white
            border border-slate-200
            p-5
            shadow-sm
            hover:shadow-xl
            transition
            "
        >
          <div
            className="
                w-10 h-10
                flex items-center justify-center
                rounded-lg
                text-white
                text-lg
                bg-gradient-to-r from-emerald-500 to-teal-500
                "
          >
            🚀
          </div>

          <p className="text-slate-500 text-xs mt-3">Platform Status</p>

          <h2 className="text-lg font-semibold text-slate-800">Operational</h2>
        </div>

        <div
          className="
            rounded-2xl
            bg-white
            border border-slate-200
            p-5
            shadow-sm
            hover:shadow-xl
            transition
            "
        >
          <div
            className="
                w-10 h-10
                flex items-center justify-center
                rounded-lg
                text-white
                text-lg
                bg-gradient-to-r from-indigo-500 to-violet-500
                "
          >
            🔐
          </div>

          <p className="text-slate-500 text-xs mt-3">System Security</p>

          <h2 className="text-lg font-semibold text-slate-800">Protected</h2>
        </div>

        <div
          className="
            rounded-2xl
            bg-white
            border border-slate-200
            p-5
            shadow-sm
            hover:shadow-xl
            transition
            "
        >
          <div
            className="
                w-10 h-10
                flex items-center justify-center
                rounded-lg
                text-white
                text-lg
                bg-gradient-to-r from-cyan-500 to-blue-500
                "
                        >
            🌐
          </div>

          <p className="text-slate-500 text-xs mt-3">Active Environment</p>

          <h2 className="text-lg font-semibold text-slate-800">Production</h2>
        </div>
      </div>

      <div
        className="
        rounded-2xl
        bg-white
        border border-slate-200
        p-6
        shadow-sm
        "
      >
        <h3 className="font-semibold text-slate-800">Welcome Admin 👋</h3>

        <p className="text-sm text-slate-500 mt-1">
          Use the sidebar to manage teachers and students. Keep your learning
          ecosystem efficient and secure.
        </p>
      </div>
    </div>
  );
}
