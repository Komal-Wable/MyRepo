import { AppBar, Toolbar, Typography, Button, Box } from "@mui/material";
import SchoolIcon from "@mui/icons-material/School";
import { useNavigate } from "react-router-dom";
import ProfileMenu from "./ProfileMenu";

import "../styles/navbar.css";
const Navbar = () => {
  const navigate = useNavigate();

  const logout = () => {
    localStorage.removeItem("token");
    window.dispatchEvent(new Event("storage"));

    navigate("/");
  };

  return (
    <AppBar
      position="sticky"
      elevation={4}
      sx={{
        backdropFilter: "blur(10px)",
        background: "linear-gradient(90deg,#4f46e5,#7c3aed)",
      }}
    >
      <Toolbar sx={{ display: "flex", justifyContent: "space-between" }}>
        <Box
          sx={{
            display: "flex",
            alignItems: "center",
            gap: 1,
            cursor: "pointer",
          }}
          onClick={() => navigate("/")}
        >
          <SchoolIcon sx={{ fontSize: 32 }} />

          <Typography
            variant="h5"
            fontWeight="bold"
            sx={{ letterSpacing: 1 }}
          ></Typography>
        </Box>

        <Box sx={{ display: "flex", alignItems: "center", gap: 2 }}>
          <ProfileMenu />
        </Box>
      </Toolbar>
    </AppBar>
  );
};

export default Navbar;
