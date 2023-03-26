import React from 'react';
import {Container} from "@mui/system";
import {Box, Grid} from "@mui/material";


function Hero() {
  return (
      <Container>
        <Grid container>
          <Grid item sm={12}>
            <Box className="hero">
              <h2>Recipes from YouTube</h2>
              {/*<h3>Instantly get ingredients, recipe steps, and nutrition info from any YouTube food video</h3>*/}
              {/*<h4>Instantly get ingredients, recipe steps, and nutrition info from any YouTube food video</h4>*/}
            </Box>
          </Grid>
        </Grid>

      </Container>
  );
}

export default Hero;
