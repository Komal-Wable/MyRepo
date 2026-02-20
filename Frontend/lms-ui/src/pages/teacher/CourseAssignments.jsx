import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import API from "../../api/axios";
import { useNavigate } from "react-router-dom";
//import "../../styles/teacher.css";
//import "../../styles/courseAssignments.css"
import toast from "react-hot-toast";
import BackButton from "../../components/BackButton";
export default function CourseAssignments() {
  const { courseId } = useParams();
  const navigate = useNavigate();
  const [openDrawer, setOpenDrawer] = useState(false);

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

      
      await fetchAssignments();
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

      
      fetchAssignments();
    } catch (err) {
      toast.error(err.response?.data?.message || "Failed to delete assignment");
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
        py-10
        px-6
        "
    >
      <div className="max-w-6xl mx-auto space-y-8">
        <div>
          <BackButton />
        </div>

        <div className="flex flex-col gap-3">
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
            Assignments
          </h1>

          <p className="text-slate-600">
            Manage coursework and track submissions effortlessly.
          </p>

          <button
            onClick={() =>
              navigate(`/teacher/courses/${courseId}/assignments/create`)
            }
            className="
                w-fit
                mt-2
                px-5 py-2.5
                rounded-xl
                font-semibold
                text-white
                bg-gradient-to-r
                from-indigo-500
                via-violet-500
                to-cyan-500
                hover:scale-105
                active:scale-95
                transition
                shadow-md
                hover:shadow-lg
                "
          >
            + Create Assignment
          </button>
        </div>

        {assignments.length === 0 && (
          <div
            className="
                rounded-2xl
                p-[1px]
                bg-gradient-to-br
                from-indigo-300
                via-cyan-300
                to-violet-300
                "
          >
            <div
              className="
                rounded-2xl
                bg-white/80
                backdrop-blur-xl
                p-12
                text-center
                "
            >
              <h3 className="text-xl font-bold text-slate-700">
                No assignments yet
              </h3>

              <p className="text-slate-500 mt-1">
                Create one to start collecting submissions.
              </p>
            </div>
          </div>
        )}

        <div
          className="
            grid
            sm:grid-cols-2
            lg:grid-cols-3
            gap-6
            "
        >
          {assignments.map((a) => (
            <div
              key={a.id}
              className="
                group
                rounded-2xl
                p-[1px]
                bg-gradient-to-br
                from-indigo-300
                via-cyan-300
                to-violet-300
                hover:scale-[1.02]
                transition
                "
            >
              <div
                className="
                    h-full
                    rounded-2xl
                    bg-white/85
                    backdrop-blur-lg
                    border border-white/40
                    p-5
                    shadow-sm
                    group-hover:shadow-xl
                    transition
                    "
              >
                <span
                  className={`px-2 py-1 text-xs font-semibold rounded-full ${a.assignmentType === "FILE"? "bg-emerald-100 text-emerald-600" : "bg-sky-100 text-sky-600"}`}
                >
                  {a.assignmentType}
                </span>

                <p className="text-slate-700 text-sm font-medium mt-3 line-clamp-2">
                  {a.description}
                </p>

                <p className="mt-2 text-xs text-slate-500">
                  📅 Due: {new Date(a.dueDate).toLocaleDateString()}
                </p>

                <div className="flex flex-col gap-2 mt-4">
                  <button
                    onClick={() =>
                      navigate(`/teacher/assignments/${a.id}/submissions`)
                    }
                    className="
                        w-full
                        py-2
                        rounded-lg
                        text-sm
                        font-semibold
                        text-white
                        bg-gradient-to-r
                        from-indigo-500
                        to-violet-500
                        hover:shadow-md
                        transition
                        "
                  >
                    View Submissions
                  </button>

                  <button
                    onClick={() =>
                      navigate(`/teacher/assignments/${a.id}/discussion`)
                    }
                    className="
                        w-full
                        py-2
                        rounded-lg
                        text-sm
                        font-semibold
                        bg-cyan-50
                        text-cyan-700
                        hover:bg-cyan-100
                        transition
                        "
                  >
                    💬 Discussion
                  </button>

                  <button
                    onClick={() => deleteAssignment(a.id)}
                    className="
                        w-full
                        py-2
                        rounded-lg
                        text-sm
                        font-semibold
                        bg-rose-50
                        text-rose-600
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
    </div>
  );
}
