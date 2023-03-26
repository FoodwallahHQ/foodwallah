import React from 'react';
import {PATHS} from "@/utils/constants";
import {Box} from "@mui/material";

const Footer = () => {
  return (
      <footer>
        <Box className="footer-left">
          <a href="/"><p>Home</p></a>
          <a href={PATHS.TOS}>Terms of Service</a>
          <a href={PATHS.PRIVACY}>Privacy Policy</a>
        </Box>
        <Box className="footer-right">
          <p>Copyright © 2023 Kalico</p>
        </Box>
      </footer>
  );
}
export default Footer

