import React from 'react';
import {Container} from "@mui/system";
import {Box, Grid} from "@mui/material";


function Hero() {
  return (
      <Container>
        <Grid container>
          <Grid item sm={12}>
            <Box className="hero">
              <h2>All the Best Recipes from YouTube</h2>
              <h6>Turn any YouTube food video into a step-by-step recipe by pasting the link below</h6>
            </Box>
          </Grid>
        </Grid>

      </Container>
  );
}

export default Hero;
