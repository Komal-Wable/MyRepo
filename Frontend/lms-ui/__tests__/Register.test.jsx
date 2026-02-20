import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { BrowserRouter } from "react-router-dom";
import { describe, test, expect } from "vitest";
import Register from "../src/pages/Register";

describe("Register Page", () => {

  test("renders register form", () => {
    render(
      <BrowserRouter>
        <Register />
      </BrowserRouter>
    );

    expect(
      screen.getByLabelText(/full name/i)
    ).toBeInTheDocument();

    expect(
      screen.getByLabelText(/^email$/i)
    ).toBeInTheDocument();

    expect(
      screen.getByLabelText(/^password$/i)
    ).toBeInTheDocument();

    expect(
      screen.getByRole("button", { name: /create account/i })
    ).toBeInTheDocument();
  });

  test("allows typing in password field", async () => {
    render(
      <BrowserRouter>
        <Register />
      </BrowserRouter>
    );

    const passwordInput = screen.getByLabelText(/^password$/i);

    await userEvent.type(passwordInput, "Password@123");

    expect(passwordInput).toHaveValue("Password@123");
  });

});
