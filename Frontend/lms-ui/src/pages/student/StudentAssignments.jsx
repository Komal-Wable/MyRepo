import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";

import API from "../../api/axios";

import BackButton from "../../components/BackButton";
import toast from "react-hot-toast";
import Chat from "../../components/ChatComponent";

export default function StudentAssignments() {
  const navigate = useNavigate();

  const { courseId } = useParams();
  const [assignments, setAssignments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const [selectedFiles, setSelectedFiles] = useState({});

  const [linkInputs, setLinkInputs] = useState({});

  const [uploading, setUploading] = useState({});

  useEffect(() => {
    loadAssignments();
  }, [courseId]);

  const loadAssignments = async () => {
    try {
      setLoading(true);
      setError(null);

      const res = await API.get(`/api/student/assignments/course/${courseId}`);

      const processedAssignments = res.data.map((assignment) => {
        let assignmentType =
          assignment.type ||
          assignment.assignmentType ||
          assignment.submissionType;

        if (!assignmentType && assignment.link) {
          assignmentType = "LINK";
        }

        if (!assignmentType) {
          assignmentType = "FILE";
        }

        assignmentType = assignmentType.toUpperCase();

        return {
          ...assignment,
          type: assignmentType,
        };
      });

      const assignmentsWithSubmissionStatus = await Promise.all(
        processedAssignments.map(async (assignment) => {
          try {
            const submissionRes = await API.post(
              "/api/student/submissions/status",
              assignment.id,
              {
                headers: {
                  "Content-Type": "application/json",
                },
              },
            );

            return {
              ...assignment,
              isSubmitted: submissionRes.data || false,
              submissionId: null,
              existingSubmission: null,
            };
          } catch (error) {
            console.log("error", error);
            return {
              ...assignment,
              isSubmitted: false,
              submissionId: null,
              existingSubmission: null,
            };
          }
        }),
      );

      setAssignments(assignmentsWithSubmissionStatus);
    } catch (error) {
      console.error("Error loading assignments:", error);
      setError("Failed to load assignments. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  const handleFileSelect = (assignmentId, file) => {
    setSelectedFiles((prev) => ({
      ...prev,
      [assignmentId]: file,
    }));
  };

  const handleLinkChange = (assignmentId, link) => {
    setLinkInputs((prev) => ({
      ...prev,
      [assignmentId]: link,
    }));
  };

  const submitAssignment = async (assignmentId, type) => {
    const assignment = assignments.find((a) => a.id === assignmentId);
    if (assignment?.isSubmitted) {
      alert("You have already submitted this assignment!");
      return;
    }

    try {
      setUploading((prev) => ({ ...prev, [assignmentId]: true }));

      if (type === "FILE") {
        const file = selectedFiles[assignmentId];
        if (!file) {
          alert("Please select a file to upload");
          setUploading((prev) => ({ ...prev, [assignmentId]: false }));
          return;
        }

        const formData = new FormData();
        formData.append("assignmentId", assignmentId);
        formData.append("file", file);

        await API.post("/api/student/submissions", formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        });

        setSelectedFiles((prev) => {
          const newFiles = { ...prev };
          delete newFiles[assignmentId];
          return newFiles;
        });
      } else if (type === "LINK") {
        const link = linkInputs[assignmentId];
        if (!link || !link.trim()) {
          alert("Please enter a valid link");
          setUploading((prev) => ({ ...prev, [assignmentId]: false }));
          return;
        }

        await API.post(
          "/api/student/submissions/link",
          {
            assignmentId: assignmentId,
            linkUrl: link,
          },
          {
            headers: {
              "Content-Type": "application/json",
            },
          },
        );

        setLinkInputs((prev) => {
          const newLinks = { ...prev };
          delete newLinks[assignmentId];
          return newLinks;
        });
      } else {
        alert("Unknown assignment type");
        setUploading((prev) => ({ ...prev, [assignmentId]: false }));
        return;
      }

      toast.success("Assignment Submitted Successfully! ");

      loadAssignments();
    } catch (error) {
      console.error("Error submitting assignment:", error);

      if (
        error.response?.status === 409 ||
        error.response?.data?.message
          ?.toLowerCase()
          .includes("already submitted") ||
        error.response?.data?.message?.toLowerCase().includes("duplicate")
      ) {
        alert(
          "You have already submitted this assignment. Duplicate submissions are not allowed.",
        );

        loadAssignments();
      } else {
        toast.error("Failed to submit assignment. Please try again.");
      }
    } finally {
      setUploading((prev) => ({ ...prev, [assignmentId]: false }));
    }
  };

  const getAssignmentType = (assignment) => {
    const type = assignment.type;
    if (type) {
      return type.toUpperCase();
    }
    return "FILE";
  };
  const downloadInstruction = async (assignmentId, fileName) => {
    try {
      const res = await API.get(
        `/api/student/assignments/${assignmentId}/instruction`,
        { responseType: "blob" },
      );

      const url = window.URL.createObjectURL(new Blob([res.data]));

      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", fileName);
      document.body.appendChild(link);
      link.click();

      link.remove();
      window.URL.revokeObjectURL(url);
    } catch {
      alert("Failed to download instruction file");
    }
  };

  const downloadExistingSubmission = async (submissionId, assignmentId) => {
    try {
      const res = await API.get(
        `/api/student/submissions/${submissionId}/download`,
        { responseType: "blob" },
      );

      const url = window.URL.createObjectURL(new Blob([res.data]));

      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", `submission_assignment_${assignmentId}`);
      document.body.appendChild(link);
      link.click();

      setTimeout(() => {
        window.URL.revokeObjectURL(url);
        document.body.removeChild(link);
      }, 100);
    } catch {
      alert("Download failed");
    }
  };

  if (loading) {
    return (
      <div style={{ padding: 40, textAlign: "center" }}>
        <h1>Assignments</h1>
        <p>Loading assignments...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div style={{ padding: 40 }}>
        <h1>Assignments</h1>
        <p style={{ color: "red" }}>{error}</p>
        <button
          onClick={loadAssignments}
          style={{
            padding: "8px 16px",
            backgroundColor: "#007bff",
            color: "white",
            border: "none",
            borderRadius: 4,
            cursor: "pointer",
          }}
        >
          Try Again
        </button>
      </div>
    );
  }

  const isDueExpired = (dueDate) => {
    if (!dueDate) return false;

    return new Date(dueDate) < new Date();
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
      <div className="max-w-6xl mx-auto space-y-8">
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
            Assignments
          </h1>

          <p className="text-slate-600">
            Submit your coursework and track deadlines.
          </p>
        </div>

        {assignments.length === 0 ? (
          <div
            className="
                    rounded-2xl
                    bg-white/70
                    backdrop-blur-xl
                    p-12
                    text-center
                    shadow
                    "
          >
            <h2 className="text-xl font-bold text-slate-700">
              No Assignments Yet
            </h2>

            <p className="text-slate-500">
              Your instructor hasn’t added assignments.
            </p>
          </div>
        ) : (
          <div
            className="
                    grid
                    sm:grid-cols-2
                    lg:grid-cols-3
                    gap-6
                    "
          >
            {assignments.map((assignment) => {
              const isExpired = isDueExpired(assignment.dueDate);
              const assignmentId = assignment.id;

              const type = getAssignmentType(assignment);
              const isFileType = type === "FILE";

              const isSubmitted = assignment.isSubmitted;

              const hasFile = !!selectedFiles[assignmentId];
              const linkValue = linkInputs[assignmentId] || "";

              return (
                <div
                  key={assignmentId}
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
                        h-full
                        "
                >
                  <div
                    className="
                            rounded-2xl
                            bg-white/85
                            backdrop-blur-lg
                            border border-white/40
                            p-5
                            shadow-sm
                            group-hover:shadow-xl
                            transition
                            flex flex-col
                            h-full

                            "
                  >
                    <div className="flex justify-between items-start">
                      <div>
                        <h2 className="font-bold text-lg text-slate-800 line-clamp-1">
                          {assignment.title}
                        </h2>

                        <p className="text-sm text-slate-500">
                          📅 {new Date(assignment.dueDate).toLocaleString()}
                        </p>
                      </div>

                      {isSubmitted ? (
                        <span className="px-2 py-1 text-xs rounded-full bg-emerald-100 text-emerald-600">
                          Submitted
                        </span>
                      ) : isExpired ? (
                        <span className="px-2 py-1 text-xs rounded-full bg-rose-100 text-rose-600">
                          Closed
                        </span>
                      ) : (
                        <span className="px-2 py-1 text-xs rounded-full bg-amber-100 text-amber-600">
                          Pending
                        </span>
                      )}
                    </div>

                    {assignment.instructionFileName && (
                      <button
                        onClick={() =>
                          downloadInstruction(
                            assignment.id,
                            assignment.instructionOriginalName,
                          )
                        }
                        className="
                                mt-4
                                text-sm
                                font-semibold
                                text-indigo-600
                                hover:underline
                                "
                      >
                        📥 Download Instructions
                      </button>
                    )}

                    {assignment.instructionPathLink && (
                      <a
                        href={assignment.instructionPathLink}
                        target="_blank"
                        rel="noopener noreferrer"
                        className="
                                mt-2
                                text-sm
                                font-semibold
                                text-cyan-600
                                hover:underline
                                block
                                "
                      >
                        🔗 View Instructions
                      </a>
                    )}

                    {!isSubmitted && !isExpired && (
                      <div className="mt-4 space-y-3">
                        {isFileType ? (
                          <>
                            <input
                              type="file"
                              onChange={(e) =>
                                handleFileSelect(
                                  assignmentId,
                                  e.target.files[0],
                                )
                              }
                              className="
                                    w-full
                                    text-sm
                                    border border-slate-200
                                    rounded-lg
                                    p-2
                                    bg-white
                                    "
                            />

                            <button
                              disabled={!hasFile || uploading[assignmentId]}
                              onClick={() =>
                                submitAssignment(assignmentId, "FILE")
                              }
                              className="
                                    w-full
                                    py-2
                                    rounded-lg
                                    font-semibold
                                    text-white
                                    bg-gradient-to-r
                                    from-indigo-500
                                    to-violet-500
                                    hover:shadow-md
                                    transition
                                    disabled:opacity-60
                                    "
                            >
                              {uploading[assignmentId]
                                ? "Uploading..."
                                : "Submit File"}
                            </button>
                          </>
                        ) : (
                          <>
                            <input
                              type="url"
                              placeholder="Paste submission link"
                              value={linkValue}
                              onChange={(e) =>
                                handleLinkChange(assignmentId, e.target.value)
                              }
                              className="
                                    w-full
                                    px-3 py-2
                                    rounded-lg
                                    border border-slate-200
                                    focus:ring-2
                                    focus:ring-indigo-200
                                    outline-none
                                    "
                            />

                            <button
                              disabled={
                                !linkValue.trim() || uploading[assignmentId]
                              }
                              onClick={() =>
                                submitAssignment(assignmentId, "LINK")
                              }
                              className="
                                        w-full
                                        py-2
                                        rounded-lg
                                        font-semibold
                                        text-white
                                        bg-gradient-to-r
                                        from-indigo-500
                                        to-violet-500
                                        "
                            >
                              Submit Link
                            </button>
                          </>
                        )}

                        <button
                          onClick={() =>
                            navigate(
                              `/student/assignments/${assignmentId}/chat`,
                            )
                          }
                          className="
                                        w-full
                                        py-2
                                        rounded-lg
                                        font-semibold
                                        bg-cyan-50
                                        text-cyan-700
                                        hover:bg-cyan-100
                                        transition
                                        "
                        >
                          💬 Open Discussion
                        </button>
                      </div>
                    )}
                  </div>
                </div>
              );
            })}
          </div>
        )}
      </div>
    </div>
  );
}
