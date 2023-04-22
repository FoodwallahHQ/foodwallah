import React, {FC} from "react";
import Box from "@mui/material/Box";
import Accordion from '@mui/material/Accordion';
import AccordionSummary from '@mui/material/AccordionSummary';
import AccordionDetails from '@mui/material/AccordionDetails';
import Typography from '@mui/material/Typography';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';

export interface RawTranscriptProps {
  transcript?: string
}

const RawTranscript: FC<RawTranscriptProps> = (props) => {

  return (
      <Box sx={{mb: 5, mt: 2}}>
        <Accordion>
          <AccordionSummary
              expandIcon={<ExpandMoreIcon />}
              aria-controls="panel1a-content"
              id="panel1a-header"
          >
            <Typography>Raw Transcript</Typography>
          </AccordionSummary>
          <AccordionDetails>
            <Typography>
              {props.transcript}
            </Typography>
          </AccordionDetails>
        </Accordion>
      </Box>
  );
}

export default RawTranscript;