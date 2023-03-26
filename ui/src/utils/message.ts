import {Slide} from "@mui/material";

export interface ShowMessageBarParams {
  message: string,
  snack: any,
  error?: boolean
}

export const showMessageBar = (params: ShowMessageBarParams) : void => {
  if (params.message) {
    if (params.error) {
      params.snack(params.message, {
        variant: "error",
        anchorOrigin: {
          vertical: 'bottom',
          horizontal: 'right'
        },
        autoHideDuration: 5000,
        TransitionComponent: Slide
      });
    } else {
      params.snack(params.message, {
        variant: "success",
        anchorOrigin: {
          vertical: 'bottom',
          horizontal: 'right'
        },
        autoHideDuration: 5000,
        TransitionComponent: Slide
      });
    }
  }
}