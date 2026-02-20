import { render, screen, waitFor } from "@testing-library/react";
import { describe, test, expect, vi } from "vitest";
import { MemoryRouter } from "react-router-dom";
import Sdashboard from "../src/pages/student/Sdashboard";
import API from "../src/api/axios";

vi.mock("../src/api/axios");

describe("Student Dashboard", () => {

  test("renders student stats from API", async () => {

    API.get.mockResolvedValueOnce({
      data: {
        enrolledCourses: 5,
        pendingAssignments: 2,
        submittedAssignments: 8,
        evaluatedAssignments: 6,
        submissionTrend: []
      }
    });

    render(
      <MemoryRouter>
        <Sdashboard />
      </MemoryRouter>
    );

    await waitFor(() => {
      expect(screen.getByText("5")).toBeInTheDocument();
      expect(screen.getByText("2")).toBeInTheDocument();
      expect(screen.getByText("8")).toBeInTheDocument();
      expect(screen.getByText("6")).toBeInTheDocument();
    });

  });

});
