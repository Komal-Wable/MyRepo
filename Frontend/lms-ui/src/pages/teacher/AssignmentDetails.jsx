import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import API from "../../api/axios";

import Chat from "../../components/ChatComponent";
import BackButton from "../../components/BackButton";
//import BackButton from "../../components/BackButton";
export default function AssignmentDetails() {
  const { assignmentId } = useParams();
  const [assignment, setAssignment] = useState(null);

  useEffect(() => {
    API.get(`/api/teacher/assignments/${assignmentId}`).then((res) =>
      setAssignment(res.data),
    );
  }, [assignmentId]);

  if (!assignment) return <p>Loading...</p>;

  return (
    <div
      className="
            min-h-screen
            bg-gradient-to-br
            from-indigo-50
            via-cyan-50
            to-violet-50
            py-10
            px-6
            flex justify-center
            "
    >
      <div className="w-full max-w-5xl space-y-6">
        <div className="flex items-center">
          <BackButton />
        </div>

        <div
          className="
                rounded-3xl
                p-[1px]
                bg-gradient-to-br
                from-indigo-400
                via-cyan-400
                to-violet-400
                shadow-xl
                "
        >
          <div
            className="rounded-3xl
                        bg-white/80
                        backdrop-blur-xl
                        border border-white/40
                        p-8
                        "
          >
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
              {assignment.title}
            </h1>

            <p className="text-slate-600 mt-3 leading-relaxed">
              {assignment.description}
            </p>

            <div
              className="
                    mt-5
                    inline-block
                    px-4 py-2
                    rounded-xl
                    bg-indigo-50
                    text-indigo-700
                    font-semibold
                    "
            >
              📅 Due: {new Date(assignment.dueDate).toLocaleDateString()}
            </div>
          </div>
        </div>

        <div
          className="
                    rounded-3xl
                    p-[1px]
                    bg-gradient-to-br
                    from-cyan-400
                    via-indigo-400
                    to-violet-400
                    shadow-xl
                    "
        >
          <div
            className="
                    rounded-3xl
                    bg-white/85
                    backdrop-blur-xl
                    border border-white/40
                    p-6
                    "
          >
            <h2
              className="
                    text-2xl
                    font-bold
                    mb-4
                    bg-gradient-to-r
                    from-indigo-600
                    to-cyan-600
                    bg-clip-text
                    text-transparent
                    "
            >
              💬 Assignment Discussion
            </h2>

            <Chat assignmentId={assignmentId} />
          </div>
        </div>
      </div>
    </div>
  );
}
