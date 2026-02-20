import { useEffect, useState ,navigate } from "react";
import { useNavigate, useParams } from "react-router-dom";
import API from "../../api/axios";
//import "../../styles/teacher.css";
import BackButton from "../../components/BackButton";
import toast from "react-hot-toast";
import "../../styles/teachersubmission.css"
export default function TeacherSubmissions() {

    const navigate=useNavigate();
    const { assignmentId } = useParams();
    const [submissions, setSubmissions] = useState([]);

    useEffect(() => {
        loadSubmissions();
    }, []);

    const loadSubmissions = async () => {
        try {
            const res = await API.get(
                `/api/teacher/submissions/assignment/${assignmentId}`
            );

            setSubmissions(res.data);

        } catch (err) {
            console.error(err);
            toast.error("Failed to load submissions");
        }
    };

   
    const download = async (submissionId) => {

    const response = await API.get(
        `/api/teacher/submissions/${submissionId}/download`,
        { responseType: "blob" }
    );

    
    const blob = new Blob(
        [response.data],
        { type: response.headers["content-type"] }
    );

    const url = window.URL.createObjectURL(blob);

    
    const disposition = response.headers["content-disposition"];

    let fileName = "submission.docx";

    if (disposition && disposition.includes("filename=")) {
        fileName = disposition
            .split("filename=")[1]
            .replace(/"/g, "");
    }

    const link = document.createElement("a");
    link.href = url;
    link.setAttribute("download", fileName);

    document.body.appendChild(link);
    link.click();

    link.remove();
    window.URL.revokeObjectURL(url);
};


    
    const grade = async (submissionId, marks, feedback) => {

       if (!marks || !feedback) {
    toast.error("Enter marks and feedback");
    return;
}


if (marks < 1 || marks >100) {
    toast.error("Marks must be between 1 and 100");
    return;
}


        try {

            await API.post(
                `/api/teacher/evaluations/${submissionId}/grade`,
                {
                    marks,
                    feedback
                }
            );

            toast.success("Evaluation Saved ");

            loadSubmissions();

        } catch (err) {

            console.error(err);

            toast.error(
                err?.response?.data?.message ||
                "Failed to grade submission"
            );
        }
    };

 return (

<div className="
min-h-screen
bg-gradient-to-br
from-indigo-50
via-sky-50
to-cyan-50
px-8
py-10
">

<div className="max-w-7xl mx-auto">


<div className="mb-8 space-y-3">


<button
onClick={() => navigate(-1)}
className="
inline-flex items-center gap-2
px-4 py-2
rounded-lg
bg-white
border border-slate-200
hover:bg-slate-50
shadow-sm
transition
"
>
← Back
</button>



<div>

<h1 className="
text-3xl
font-bold
bg-gradient-to-r
from-indigo-600
via-violet-600
to-cyan-600
bg-clip-text
text-transparent
">
Student Submissions
</h1>

<p className="text-slate-500 text-sm mt-1">
Review, evaluate, and provide feedback to students.
</p>

</div>

</div>





{submissions.length === 0 && (

<div className="
rounded-2xl
p-[1.5px]
bg-gradient-to-r
from-indigo-300
via-sky-300
to-cyan-300
">

<div className="
rounded-2xl
bg-white
p-12
text-center
shadow-md
">

<h3 className="text-xl font-semibold text-slate-700">
No submissions yet
</h3>

<p className="text-slate-500 mt-1">
Student work will appear here.
</p>

</div>
</div>

)}



<div className="grid md:grid-cols-2 xl:grid-cols-3 gap-7">

{submissions.map(sub => (

<SubmissionCard
key={sub.id}
sub={sub}
onDownload={download}
onGrade={grade}
/>

))}

</div>

</div>
</div>
);

 
}




function SubmissionCard({ sub, onDownload, onGrade }) {

const [marks, setMarks] = useState(sub.marks || "");
const [feedback, setFeedback] = useState(sub.feedback || "");

return (

<div className="
group
rounded-xl
p-[1px]
bg-gradient-to-r
from-indigo-300
via-sky-300
to-cyan-300
hover:shadow-lg
transition
">

<div className="
rounded-xl
bg-white
p-4
space-y-3
">


<div className="flex justify-between items-center">

<div>
<h3 className="font-semibold text-slate-800 text-sm">
👨‍🎓 {sub.studentName || "Unknown"}
</h3>

<p className="text-xs text-slate-500">
{new Date(sub.submittedAt).toLocaleString()}
</p>
</div>

{sub.graded && (
<span className="
text-[10px]
font-semibold
px-2 py-[2px]
rounded
bg-emerald-100
text-emerald-600
">
Graded
</span>
)}

</div>




{sub.submissionType === "FILE" && (

<button
onClick={() => onDownload(sub.id)}
className="
w-full
py-1.5
text-sm
rounded-md
font-medium
text-white
bg-gradient-to-r
from-indigo-500
to-violet-500
hover:opacity-90
transition
"
>
Download
</button>

)}

{sub.submissionType === "LINK" && (

<a
href={sub.linkUrl}
target="_blank"
rel="noopener noreferrer"
className="
block
text-center
w-full
py-1.5
text-sm
rounded-md
font-medium
bg-cyan-50
text-cyan-700
hover:bg-cyan-100
transition
"
>
Open Link
</a>

)}



<div className="flex gap-2">

<input
type="number"
placeholder="Marks"
min="1"
max="100"
value={marks}
onChange={(e)=>setMarks(e.target.value)}
className="
flex-1
px-2 py-1.5
text-sm
rounded-md
border border-slate-200
focus:ring-2
focus:ring-indigo-400
outline-none
"
/>

<button
onClick={() =>
onGrade(sub.id, Number(marks), feedback)
}
className="
px-3
text-sm
rounded-md
font-medium
text-white
bg-gradient-to-r
from-emerald-500
to-teal-500
hover:opacity-90
transition
"
>
{sub.graded ? "Update" : "Grade"}
</button>

</div>



<textarea
rows={1}
placeholder="Feedback..."
value={feedback}
onChange={(e)=>setFeedback(e.target.value)}
className="
w-full
px-2 py-1.5
text-sm
rounded-md
border border-slate-200
focus:ring-2
focus:ring-cyan-400
outline-none
resize-none
"
/>

</div>
</div>
);
}


