import { useState } from "react";
import API from "../../api/axios";
import { useNavigate } from "react-router-dom";
import toast from "react-hot-toast";
import BackButton from "../../components/BackButton";

export default function CreateCourse() {
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");

  const navigate = useNavigate();

  const createCourse = async () => {
    if (!title || !description) {
      toast.error("Please fill all fields");
      return;
    }

    try {
      await API.post("/api/teacher/courses", {
        title,
        description,
      });

      toast.success("Course Created Successfully!");

      navigate("/teacher/manage-courses", { replace: true });
    } catch (err) {
      toast.error("Failed to create course");
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
      <div className="max-w-5xl space-y-10">
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
            Create Course
          </h1>

          <p className="text-slate-600 mt-2">
            Design a new learning experience for your students ✨
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
                "
        >
          <div
            className="
                    rounded-3xl
                    bg-white/75
                    backdrop-blur-xl
                    border border-white/50
                    p-10
                    shadow-xl
                    "
          >
            <form
              onSubmit={(e) => {
                e.preventDefault();
                createCourse();
              }}
              className="space-y-8"
            >
              <div>
                <label
                  className="
                        block
                        text-sm
                        font-semibold
                        text-slate-700
                        mb-2
                        "
                >
                  Course Title
                </label>

                <input
                  placeholder="Microservices Architecture"
                  value={title}
                  onChange={(e) => setTitle(e.target.value)}
                  className="
                        w-full
                        px-5 py-3
                        rounded-xl
                        bg-white/80
                        border border-slate-200
                        focus:outline-none
                        focus:ring-4
                        focus:ring-indigo-200
                        focus:border-indigo-400
                        transition
                        shadow-sm
                        "
                />
              </div>

              <div>
                <label
                  className="
                        block
                        text-sm
                        font-semibold
                        text-slate-700
                        mb-2
                        "
                >
                  Description
                </label>

                <textarea
                  rows={4}
                  placeholder="Learn Kafka, Saga, API Gateway..."
                  value={description}
                  onChange={(e) => setDescription(e.target.value)}
                  className="
                            w-full
                            px-5 py-3
                            rounded-xl
                            bg-white/80
                            border border-slate-200
                            focus:outline-none
                            focus:ring-4
                            focus:ring-cyan-200
                            focus:border-cyan-400
                            transition
                            shadow-sm
                            resize-none
                            "
                />
              </div>

              <button
                type="submit"
                className="
                        w-full
                        py-4
                        rounded-2xl
                        font-bold
                        text-lg
                        text-white
                        bg-gradient-to-r
                        from-indigo-500
                        via-violet-500
                        to-cyan-500
                        hover:scale-[1.02]
                        active:scale-[0.98]
                        transition
                        shadow-lg
                        hover:shadow-2xl
                        "
              >
                🚀 Create Course
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}
