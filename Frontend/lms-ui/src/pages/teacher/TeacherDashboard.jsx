import { useEffect, useState } from "react";
import API from "../../api/axios";
import { useNavigate } from "react-router-dom";
import "../../styles/dashboard.css";
import {
    LineChart,
    Line,
    XAxis,
    YAxis,
    Tooltip,
    CartesianGrid,
    ResponsiveContainer
} from "recharts";

export default function TeacherDashboard(){
// const [stats,setStats] = useState({
//     submissionTrend:[]
// });

    const [stats,setStats] = useState({
        courses:0,
        assignments:0,
        students:0,
        pendingEvaluations:0,
        submissionTrend:[]
    });

    const navigate = useNavigate();

    useEffect(()=>{
        fetchStats();
    },[]);

    const fetchStats = async ()=>{

        try{

        

            const res = await API.get(
                `/api/teacher/dashboard`
            );

            setStats(res.data);

        }catch{
            console.log("Failed to load stats");
        }
    };

return (

<div className="
min-h-screen
bg-gradient-to-br
from-[#eef2ff]
via-[#ecfeff]
to-[#fdf2f8]
p-8
">

<div className="max-w-7xl mx-auto space-y-10">


<div>

<h1 className="
text-4xl
font-extrabold
bg-gradient-to-r
from-indigo-600
via-cyan-600
to-violet-600
bg-clip-text
text-transparent
">
Teacher Dashboard
</h1>

<p className="text-slate-600 mt-1">
Welcome back 👋 Monitor your classroom in real time.
</p>

</div>




<div className="grid md:grid-cols-2 lg:grid-cols-4 gap-6">

{[
{
label:"Courses",
value:stats.courses,
icon:"📚",
gradient:"from-indigo-500 to-violet-500"
},
{
label:"Assignments",
value:stats.assignments,
icon:"📝",
gradient:"from-amber-500 to-orange-500"
},
{
label:"Students",
value:stats.students,
icon:"👨‍🎓",
gradient:"from-emerald-500 to-teal-500"
},
{
label:"Pending Reviews",
value:stats.pendingEvaluations,
icon:"⏳",
gradient:"from-cyan-500 to-blue-500"
},
].map((item,i)=>(

<div
key={i}
className="
group
relative
rounded-3xl
p-[1px]
bg-gradient-to-br
from-white/40
to-white/10
backdrop-blur-xl
hover:scale-[1.02]
transition
"
>


<div className="
rounded-3xl
bg-white/75
backdrop-blur-xl
border border-white/40
p-6
shadow-lg
flex flex-col
gap-3
h-[140px]
justify-center
">


<div className={`
w-12 h-12
flex items-center justify-center
rounded-xl
text-xl
text-white
shadow-md
bg-gradient-to-r ${item.gradient}
`}>
{item.icon}
</div>

<p className="text-slate-500 text-sm font-medium whitespace-nowrap">
{item.label}
</p>

<h2 className="
text-3xl
font-extrabold
text-slate-800
">
{item.value}
</h2>

</div>
</div>

))}

</div>






<div className="
rounded-3xl
p-[1px]
bg-gradient-to-br
from-indigo-300
via-cyan-300
to-violet-300
">

<div className="
rounded-3xl
bg-white/80
backdrop-blur-xl
border border-white/40
p-8
shadow-xl
">

<h2 className="
text-xl
font-bold
text-slate-800
mb-6
">
Submission Activity
</h2>

<ResponsiveContainer width="100%" height={320}>
<LineChart data={stats.submissionTrend || []}>
<CartesianGrid strokeDasharray="3 3" />
<XAxis dataKey="day" />
<YAxis />
<Tooltip />

<Line
type="monotone"
dataKey="submissions"
stroke="#6366f1"
strokeWidth={4}
dot={{ r: 4 }}
/>

</LineChart>
</ResponsiveContainer>

</div>
</div>


</div>
</div>
);

}
