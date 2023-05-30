import * as React from "react";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemIcon from "@mui/material/ListItemIcon";
import ListItemText from "@mui/material/ListItemText";
import { Https, NoEncryption } from "@mui/icons-material";
import { Link } from "react-router-dom";

export default function Menu({ mobile, toggleDrawer }) {

  let closeMenu = () => {
    if (mobile) {
      toggleDrawer();
    }
  };

  return (
    <>
      <ListItemButton
        component={Link}
        to="/"
        onClick={() => {
          if (mobile) {
            toggleDrawer();
          }
        }}
      >
        <ListItemIcon>
          <Https />
        </ListItemIcon>
        <ListItemText primary="Cifrar archivo" />
      </ListItemButton>
      <ListItemButton
        component={Link}
        to="/decrypt"
        onClick={closeMenu}
      >
        <ListItemIcon>
          <NoEncryption />
        </ListItemIcon>
        <ListItemText primary="Descifrar archivo" />
      </ListItemButton>
    </>
  )
}
