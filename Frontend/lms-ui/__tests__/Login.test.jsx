import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { BrowserRouter } from "react-router-dom";
import { describe, test, expect } from "vitest";
import Login from "../src/pages/Login";

describe("Login Page", () => {

  test("renders login form", () => {
    render(
      <BrowserRouter>
        <Login />
      </BrowserRouter>
    );

    expect(
      screen.getByLabelText(/email address/i)
    ).toBeInTheDocument();

    expect(
      screen.getByLabelText(/password/i)
    ).toBeInTheDocument();

    expect(
      screen.getByRole("button", { name: /sign in/i })
    ).toBeInTheDocument();
  });

  test("shows required validation on submit", async () => {
    render(
      <BrowserRouter>
        <Login />
      </BrowserRouter>
    );

    await userEvent.click(
      screen.getByRole("button", { name: /sign in/i })
    );

    expect(
      screen.getByLabelText(/email address/i)
    ).toBeRequired();

    expect(
      screen.getByLabelText(/password/i)
    ).toBeRequired();
  });

});
