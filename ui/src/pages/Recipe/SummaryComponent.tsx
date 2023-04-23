import React, {FC} from 'react';
import {Box} from "@mui/material";


export interface SummaryComponentProps {
  ingredients: number,
  cookingTime: number,
  prepTime: number,
  steps: number
}

const SummaryComponent: FC<SummaryComponentProps> =  (props) => {
  return (
          <Box className="summary-item-wrapper">
            {/*<Box className="recipe-summary-item right-border">*/}
            {/*  <span className="value h2-text">{props.ingredients}</span>*/}
            {/*  <span className="unit">Ingredients</span>*/}
            {/*</Box>*/}
            <Box className="recipe-summary-item right-border">
              <span className="value h2-text">{props.cookingTime ? props.cookingTime : "15-20"}</span>
              <span className="value p-text">(min)</span>
              <span className="unit">Cooking Time</span>
            </Box>
            <Box className="recipe-summary-item">
              <span className="value h2-text">{props.prepTime ? props.prepTime : "5-10"}</span>
              <span className="value p-text">(min)</span>
              <span className="unit">Prep Time</span>
            </Box>
          </Box>
  );
}

export default SummaryComponent;
