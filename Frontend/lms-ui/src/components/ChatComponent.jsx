import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";
import { useEffect, useState } from "react";
import API from "../api/axios";
import { jwtDecode } from "jwt-decode";

import BackButton from "./BackButton";

export default function Chat({ assignmentId }) {
  const [client, setClient] = useState(null);
  const [messages, setMessages] = useState([]);
  const [text, setText] = useState("");

  useEffect(() => {
    const token = localStorage.getItem("token");

    if (!token) return;

    const decoded = jwtDecode(token);

    const userId = decoded.userId || decoded.sub;
    const role = decoded.role;

    API.get(`/api/messages/${assignmentId}`)
      .then((res) => setMessages(res.data))
      .catch((err) => console.log(err));

    const socket = new SockJS(`http://localhost:8080/api/chat?token=${token}`);

    const stompClient = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,

      onConnect: () => {
        stompClient.subscribe(`/topic/assignment/${assignmentId}`, (msg) => {
          setMessages((prev) => [...prev, JSON.parse(msg.body)]);
        });
      },
    });

    stompClient.activate();
    setClient(stompClient);

    return () => {
      stompClient.deactivate();
    };
  }, [assignmentId]);

  const send = () => {
    if (!text.trim() || !client) return;

    const token = localStorage.getItem("token");
    const decoded = jwtDecode(token);

    client.publish({
      destination: "/app/sendMessage",
      body: JSON.stringify({
        assignmentId,
        senderId: decoded.userId || decoded.sub,
        senderRole: decoded.role,
        content: text,
      }),
    });

    setText("");
  };
  const token = localStorage.getItem("token");

  let currentUserId = null;

  if (token) {
    try {
      currentUserId = currentUserId =
        jwtDecode(token).userId || jwtDecode(token).sub;
    } catch (err) {
      console.log("Invalid token");
    }
  }

  return (
    <div
      className="
        flex flex-col
        h-[520px]
        rounded-2xl
        bg-white
        shadow-inner
        border border-slate-200
        overflow-hidden
        "
    >
      <div
        className="
        flex-1
        overflow-y-auto
        p-5
        space-y-4
        bg-gradient-to-b
        from-slate-50
        to-white
        "
      >
        {messages.map((m, i) => {
          const isMine = m.senderId === currentUserId;

          return (
            <div
              key={i}
              className={`flex${isMine ? "justify-end" : "justify-start"}`}
            >
              <div
                className={`
                  max-w-[70%]
                  px-4 py-2.5
                  rounded-2xl
                  shadow-sm
                  text-sm
                  ${
                    isMine
                      ? "bg-gradient-to-r from-indigo-500 to-purple-500 text-white"
                      : "bg-white border border-slate-200"
                  }
                  `}
              >
                <p
                  className="
                  text-xs
                  mb-1
                  font-semibold
                  opacity-70
                  "
                >
                  {m.senderName}
                </p>

                <p>{m.content}</p>
              </div>
            </div>
          );
        })}
      </div>

      <div
        className="
        border-t
        bg-white
        p-3
        flex gap-2
        "
      >
        <input
          placeholder="Type a message..."
          value={text}
          onChange={(e) => setText(e.target.value)}
          className="
          flex-1
          px-4 py-2
          rounded-xl
          border border-slate-300
          focus:outline-none
          focus:ring-2
          focus:ring-indigo-300
          "
        />

        <button
          onClick={send}
          className="
              px-5
              rounded-xl
              font-semibold
              text-white
              bg-gradient-to-r
              from-indigo-500
              to-purple-500
              hover:scale-105
              active:scale-95
              transition
              "
        >
          Send
        </button>
      </div>
    </div>
  );
}
