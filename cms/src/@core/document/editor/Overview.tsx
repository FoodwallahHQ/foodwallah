import React, {FC} from "react";
import CardHeader from "@mui/material/CardHeader";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import Divider from "@mui/material/Divider";
import Grid from "@mui/material/Grid";
import CardContent from "@mui/material/CardContent";
import LooksOneIcon from "@mui/icons-material/LooksOne";
import LooksTwoIcon from "@mui/icons-material/LooksTwo";
import Looks3Icon from "@mui/icons-material/Looks3";
import Looks4Icon from "@mui/icons-material/Looks4";
import Looks5Icon from "@mui/icons-material/Looks5";
import Card from "@mui/material/Card";

const Overview: FC<any> = (_) => {

  return (
      <Card>
        <CardHeader title="Overview" titleTypographyProps={{ variant: 'h6' }} />
        <Box sx={{ml: 4, mr: 4, mb: 4}}>
          <Typography variant='body2'>
            This page consolidates all the resources needed for creating a Foodwallah post. The steps
            below outline the process involved.
          </Typography>
        </Box>
        <Divider sx={{ margin: 0 }} />
        <Grid container spacing={6}>
          <Grid item xs={12} sm={9}>
            <CardContent sx={{ padding: theme => `${theme.spacing(3.25, 5.75, 6.25)} !important` }}>
              <Grid container spacing={4}>
                <Grid item xs={12} sm={12} sx={{mt: 4}}>
                  <Box sx={{ mb: 2, display: 'flex', alignItems: 'center' }}>
                    <LooksOneIcon sx={{ color: 'primary.main', marginRight: 2.75 }} fontSize='small' />
                    <Typography variant='subtitle2'>User submits a URL from their account or an admin submits
                      one through the CMS by clicking on Create a New Post button.</Typography>
                  </Box>
                </Grid>
                <Grid item xs={12} sm={12}>
                  <Box sx={{ mb: 2, display: 'flex', alignItems: 'center' }}>
                    <LooksTwoIcon sx={{ color: 'primary.main', marginRight: 2.75 }} fontSize='small' />
                    <Typography variant='subtitle2'>The server takes the video URL and processes it. It will scrape the video description and title (if available),
                      generate the transcript, extract text overlays from the video, and generate frame-by-frame images to be used in the recipe steps.
                    </Typography>
                  </Box>
                </Grid>
                <Grid item xs={12} sm={12}>
                  <Box sx={{ mb: 2, display: 'flex', alignItems: 'center' }}>
                    <Looks3Icon sx={{ color: 'primary.main', marginRight: 2.75 }} fontSize='small' />
                    <Typography variant='subtitle2'>Fill out Section 2: Proofread the video transcript and break the sentences into separate lines. The transcript
                      will be displayed under the embedded video in the blog. Also edit the description. This will be displayed as a cation on the video.
                    </Typography>
                  </Box>
                </Grid>
                <Grid item xs={12} sm={12}>
                  <Box sx={{ mb: 2, display: 'flex', alignItems: 'center' }}>
                    <Looks4Icon sx={{ color: 'primary.main', marginRight: 2.75 }} fontSize='small' />
                    <Typography variant='subtitle2'>Fill out Section 3. Use the transcript, extracted text-overlays, and scraped
                      description to help you write out step-by-step instructions for the recipe. For each step, select an image
                      to go along with it.
                    </Typography>
                  </Box>
                </Grid>
                <Grid item xs={12} sm={12}>
                  <Box sx={{ mb: 2, display: 'flex', alignItems: 'center' }}>
                    <Looks5Icon sx={{ color: 'primary.main', marginRight: 2.75 }} fontSize='small' />
                    <Typography variant='subtitle2'>Fill out Section 4. The ingredient list is broken apart into
                      quantity and description columns so the amount can be scaled up or down.
                    </Typography>
                  </Box>
                </Grid>
              </Grid>
            </CardContent>
          </Grid>
        </Grid>
      </Card>
  );
}

export default Overview;