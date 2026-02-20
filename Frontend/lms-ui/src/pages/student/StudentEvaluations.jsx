import { useEffect, useState } from "react";
import API from "../../api/axios";
import BackButton from "../../components/BackButton";

export default function StudentEvaluations() {
  const [evaluations, setEvaluations] = useState([]);

  useEffect(() => {
    loadEvaluations();
  }, []);

  const loadEvaluations = async () => {
    const res = await API.get("/api/student/evaluations");

    setEvaluations(res.data);
  };

  const downloadScorecard = async (submissionId) => {
    try {
      const res = await API.get(
        `/api/student/evaluations/${submissionId}/scorecard`,
        {
          responseType: "blob",
        },
      );

      const url = window.URL.createObjectURL(new Blob([res.data]));

      const link = document.createElement("a");
      link.href = url;
      link.download = "scorecard.pdf";
      document.body.appendChild(link);
      link.click();

      link.remove();
      window.URL.revokeObjectURL(url);
    } catch (err) {
      console.error("Download failed", err);
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
            p-8
            "
    >
      <div className="max-w-7xl mx-auto space-y-8">
        <BackButton />

        <div>
          <h1
            className="
                text-3xl
                font-extrabold
                bg-gradient-to-r
                from-indigo-600
                via-cyan-600
                to-violet-600
                bg-clip-text
                text-transparent
                "
          >
            Your Performance
          </h1>

          <p className="text-slate-600">
            Track grades, feedback, and download scorecards.
          </p>
        </div>

        {evaluations.length === 0 ? (
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
                bg-white/80
                backdrop-blur-xl
                p-14
                text-center
                "
            >
              <h3 className="text-xl font-bold text-slate-700">
                No results yet
              </h3>

              <p className="text-slate-500 mt-2">
                Your evaluated assignments will appear here.
              </p>
            </div>
          </div>
        ) : (
          <div className="grid md:grid-cols-2 xl:grid-cols-3 gap-6">
            {evaluations.map((ev) => (
              <div
                key={ev.id}
                className="
                    group
                    rounded-3xl
                    p-[1px]
                    bg-gradient-to-br
                    from-indigo-300
                    via-cyan-300
                    to-violet-300
                    hover:from-indigo-400
                    hover:via-cyan-400
                    hover:to-violet-400
                    transition
                    "
              >
                <div
                  className="
                    rounded-3xl
                    bg-white/80
                    backdrop-blur-xl
                    border border-white/40
                    p-6
                    shadow-md
                    group-hover:-translate-y-1
                    group-hover:shadow-xl
                    transition
                    "
                >
                  <div className="flex justify-between items-start">
                    <div>
                      <p className="text-sm text-slate-500">{ev.courseName}</p>

                      <h2 className="font-bold text-slate-800">
                        {ev.assignmentName}
                      </h2>
                    </div>

                    <div
                      className="
                        px-3 py-1
                        text-xs
                        font-semibold
                        rounded-full
                        bg-emerald-100
                        text-emerald-600
                        "
                    >
                      {ev.grade}
                    </div>
                  </div>

                  <div className="mt-6 text-center">
                    <div
                      className="
                        text-4xl
                        font-extrabold
                        bg-gradient-to-r
                        from-indigo-600
                        to-violet-600
                        bg-clip-text
                        text-transparent
                        "
                    >
                      {ev.marks}
                    </div>

                    <p className="text-sm text-slate-500">Score</p>
                  </div>

                  {ev.feedback && (
                    <div
                      className="
                        mt-5
                        text-sm
                        bg-slate-50
                        border border-slate-200
                        rounded-xl
                        p-3
                        text-slate-600
                        "
                    >
                      💬 {ev.feedback}
                    </div>
                  )}

                  <button
                    onClick={() => downloadScorecard(ev.submissionId)}
                    className="
                        mt-6
                        w-full
                        py-2.5
                        rounded-xl
                        font-semibold
                        text-white
                        bg-gradient-to-r
                        from-indigo-500
                        to-violet-500
                        hover:shadow-lg
                        transition
                        "
                  >
                    Download Scorecard
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
