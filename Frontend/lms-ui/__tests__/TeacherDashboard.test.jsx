import { render, screen, waitFor } from "@testing-library/react";
import { describe, test, expect, vi } from "vitest";
import { MemoryRouter } from "react-router-dom";
import TeacherDashboard from "../src/pages/teacher/TeacherDashboard";
import API from "../src/api/axios";

vi.mock("../src/api/axios");

describe("Teacher Dashboard", () => {

  test("loads dashboard stats", async () => {

    API.get.mockResolvedValueOnce({
      data: {
        courses: 3,
        assignments: 10,
        students: 25,
        pendingEvaluations: 4,
        submissionTrend: []
      }
    });

    render(
      <MemoryRouter>
        <TeacherDashboard />
      </MemoryRouter>
    );

    await waitFor(() => {
      expect(screen.getByText("3")).toBeInTheDocument();
      expect(screen.getByText("10")).toBeInTheDocument();
      expect(screen.getByText("25")).toBeInTheDocument();
      expect(screen.getByText("4")).toBeInTheDocument();
    });

  });

});
