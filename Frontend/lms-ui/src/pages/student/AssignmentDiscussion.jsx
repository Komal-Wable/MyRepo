import { useParams } from "react-router-dom";
import Chat from "../../components/ChatComponent";

import BackButton from "../../components/BackButton";

export default function AssignmentDiscussion() {
  const { assignmentId } = useParams();

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
      <div className="max-w-5xl mx-auto space-y-6">
        <div>
          <BackButton />
        </div>

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
            Assignment Discussion
          </h1>

          <p className="text-slate-600">
            Ask questions, clarify doubts, and collaborate.
          </p>
        </div>

        <div
          className="
                rounded-3xl
                p-[1px]
                bg-gradient-to-br
                from-indigo-300
                via-cyan-300
                to-violet-300
                shadow-xl
                "
        >
          <div
            className="
                rounded-3xl
                bg-white/85
                backdrop-blur-xl
                border border-white/40
                p-5
                "
          >
            <Chat assignmentId={assignmentId} />
          </div>
        </div>
      </div>
    </div>
  );
}
