import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import API from "../../api/axios";
import { useNavigate } from "react-router-dom";

import toast from "react-hot-toast";
import BackButton from "../../components/BackButton";
export default function CreateAssignment() {
  const { courseId } = useParams();
  const navigate = useNavigate();

  const [assignments, setAssignments] = useState([]);
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [assignmentType, setAssignmentType] = useState("FILE");
  const [instructionPathLink, setInstructionPathLink] = useState("");
  const [file, setFile] = useState(null);
  const [dueDate, setDueDate] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState("");
  const [successMessage, setSuccessMessage] = useState("");

  useEffect(() => {
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    const formatted = tomorrow.toISOString().slice(0, 16);

    setDueDate(formatted);
  }, []);

  const fetchAssignments = async () => {
    setIsLoading(true);
    setError("");

    const token = localStorage.getItem("token");

    try {
      const res = await API.get(
        `/api/teacher/assignments/courses/${courseId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        },
      );

      setAssignments(res.data || []);
    } catch (err) {
      setError(err.response?.data?.message || "Failed to load assignments");
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    if (courseId) {
      fetchAssignments();
    }
  }, [courseId]);

  const createAssignment = async () => {
    if (!title.trim()) {
      setError("Title is required");
      return;
    }

    if (assignmentType === "LINK" && !instructionPathLink.trim()) {
      setError("Link URL is required for LINK type assignments");
      return;
    }

    if (assignmentType === "FILE" && !file) {
      setError("File is required for FILE type assignments");
      return;
    }

    setIsLoading(true);
    setError("");
    setSuccessMessage("");

    const token = localStorage.getItem("token");

    try {
      const assignmentData = {
        title: title.trim(),
        description: description.trim(),
        assignmentType: assignmentType,
        dueDate: dueDate,
        courseId: parseInt(courseId),
      };

      if (assignmentType === "LINK") {
        assignmentData.instructionPathLink = instructionPathLink.trim();
      }

      const res = await API.post("/api/teacher/assignments", assignmentData, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      });

      const assignmentId = res.data.id;

      if (assignmentType === "FILE" && file) {
        const formData = new FormData();
        formData.append("file", file);

        await API.post(
          `/api/teacher/assignments/${assignmentId}/upload`,
          formData,
          {
            headers: {
              Authorization: `Bearer ${token}`,
              "Content-Type": "multipart/form-data",
            },
          },
        );
      }

      setTitle("");
      setDescription("");
      setAssignmentType("FILE");
      setInstructionPathLink("");
      setFile(null);
      const tomorrow = new Date();
      tomorrow.setDate(tomorrow.getDate() + 1);
      setDueDate(tomorrow.toISOString().split("T")[0]);

      toast.success("Assignment created successfully!");
      navigate(`/teacher/courses/${courseId}/assignments`, { replace: true });
    } catch (err) {
      setError(err.response?.data?.message || "Failed to create assignment");
    } finally {
      setIsLoading(false);
    }
  };

  const handleFileChange = (e) => {
    const selectedFile = e.target.files[0];
    if (selectedFile) {
      if (selectedFile.size > 10 * 1024 * 1024) {
        setError("File size exceeds 10MB limit");
        e.target.value = "";
        return;
      }
      setFile(selectedFile);
    }
  };

  const handleResetForm = () => {
    setTitle("");
    setDescription("");
    setAssignmentType("FILE");
    setInstructionPathLink("");
    setFile(null);
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    setDueDate(tomorrow.toISOString().split("T")[0]);
    setError("");
    setSuccessMessage("");
  };

  const deleteAssignment = async (assignmentId) => {
    const confirmDelete = window.confirm(
      "Are you sure you want to delete this assignment?",
    );

    if (!confirmDelete) return;

    const token = localStorage.getItem("token");

    try {
      await API.delete(`/api/teacher/assignments/${assignmentId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      });

      toast.success("Assignment deleted successfully!");
      console.log("navi");
      navigate(`/teacher/courses/${courseId}/assignments`, { replace: true });
    } catch (err) {
      toast.error(err.response?.data?.message || "Failed to delete assignment");
    }
  };

  return (
    <div
      className="
            min-h-screen
            flex justify-center
            items-start
            pt-12
            px-6
            bg-gradient-to-br
            from-indigo-50
            via-sky-50
            to-cyan-50
            "
    >
      <div className="w-full max-w-2xl">
        <div className="mb-6">
          <h1
            className="
            text-4xl
            font-bold
            bg-gradient-to-r
            from-indigo-600
            to-cyan-600
            bg-clip-text
            text-transparent
            "
          >
            Create Assignment
          </h1>

          <p className="text-slate-500 mt-1">
            Design coursework for your students ✨
          </p>
        </div>

        <div
          className="
            p-[1.5px]
            rounded-2xl
            bg-gradient-to-r
            from-indigo-400
            via-sky-400
            to-cyan-400
            shadow-lg
            "
        >
          <div
            className="
                rounded-2xl
                bg-white
                p-7
                space-y-5
                "
          >
            <form
              onSubmit={(e) => {
                e.preventDefault();
                createAssignment();
              }}
              className="space-y-5"
            >
              <input
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                placeholder="Assignment Title"
                className="
                    w-full
                    px-4 py-3
                    rounded-xl
                    border border-slate-200
                    focus:ring-2
                    focus:ring-indigo-400
                    outline-none
                    transition
                    "
              />

              <textarea
                rows={3}
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                placeholder="Explain assignment requirements..."
                className="
                    w-full
                    px-4 py-3
                    rounded-xl
                    border border-slate-200
                    focus:ring-2
                    focus:ring-cyan-400
                    outline-none
                    transition
                    "
              />

              <div className="grid grid-cols-2 gap-3">
                <button
                  type="button"
                  onClick={() => setAssignmentType("FILE")}
                  className={`
                            py-3
                            rounded-xl
                            font-semibold
                            transition
                            ${
                            assignmentType === "FILE"
                                ? "bg-gradient-to-r from-indigo-500 to-violet-500 text-white shadow"
                                : "bg-slate-100 hover:bg-slate-200"
                            }
                            `}
                >
                  📄 File Upload
                </button>

                <button
                  type="button"
                  onClick={() => setAssignmentType("LINK")}
                  className={`
                        py-3
                        rounded-xl
                        font-semibold
                        transition
                        ${
                        assignmentType === "LINK"
                            ? "bg-gradient-to-r from-cyan-500 to-sky-500 text-white shadow"
                            : "bg-slate-100 hover:bg-slate-200"
                        }
                        `}
                >
                  🔗 Link Submission
                </button>
              </div>

              {assignmentType === "LINK" ? (
                <input
                  value={instructionPathLink}
                  onChange={(e) => setInstructionPathLink(e.target.value)}
                  placeholder="Paste instruction URL..."
                  className="
                        w-full
                        px-4 py-3
                        rounded-xl
                        border border-slate-200
                        focus:ring-2
                        focus:ring-cyan-400
                        outline-none
                        "
                />
              ) : (
                <input
                  type="file"
                  onChange={handleFileChange}
                  className="
                        w-full
                        text-sm
                        border border-slate-200
                        p-2
                        rounded-lg
                        bg-slate-50
                        "
                />
              )}

              <input
                type="datetime-local"
                value={dueDate}
                onChange={(e) => setDueDate(e.target.value)}
                className="
                    w-full
                    px-4 py-3
                    rounded-xl
                    border border-slate-200
                    focus:ring-2
                    focus:ring-violet-400
                    outline-none
                    "
              />

              <button
                type="submit"
                disabled={isLoading}
                className="
                        w-full
                        py-3
                        rounded-xl
                        font-semibold
                        text-white

                        bg-gradient-to-r
                        from-indigo-600
                        to-cyan-600

                        hover:opacity-90
                        transition
                        shadow-md
                        "
              >
                {isLoading ? "Creating..." : "Create Assignment"}
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}
