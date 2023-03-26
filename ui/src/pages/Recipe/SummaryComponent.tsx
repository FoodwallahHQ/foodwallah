import React, {FC} from 'react';
import {Box} from "@mui/material";


export interface SummaryComponentProps {
  ingredients: number,
  time: number,
  steps: number
}

const SummaryComponent: FC<SummaryComponentProps> =  (props) => {
  return (
          <Box className="summary-item-wrapper">
            <Box className="recipe-summary-item right-border">
              <span className="value h2-text">{props.ingredients}</span>
              <span className="unit">Ingredients</span>
            </Box>
            {/*<Box className="recipe-summary-item right-border">*/}
            {/*  <span className="value h2-text">{props.time}</span>*/}
            {/*  <span className="unit">Minutes</span>*/}
            {/*</Box>*/}
            <Box className="recipe-summary-item">
              <span className="value h2-text">{props.steps}</span>
              <span className="unit">Steps</span>
            </Box>
          </Box>
  );
}

export default SummaryComponent;
