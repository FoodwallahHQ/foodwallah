import * as React from 'react';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import {FC, useEffect} from "react";

export interface DialogProps {
  projectId?: string,
  open: boolean,
  onCloseDialog: (doDelete: boolean) => void
}
const ConfirmActionDialog: FC<DialogProps> = (props) => {
  const [open, setOpen] = React.useState(false);
  useEffect(() => {
    setOpen(props.open)
  })

  const handleClose = () => {
    setOpen(false);
    props.onCloseDialog(false)
  };

  const handleConfirmDelete = () => {
    setOpen(false)
    props.onCloseDialog(true)
  }

  return (
      <div>
        <Dialog
            open={open}
            onClose={handleClose}
            aria-labelledby="alert-dialog-title"
            aria-describedby="alert-dialog-description"
        >
          <DialogTitle id="alert-dialog-title">
            {"Are you sure?"}
          </DialogTitle>
          <DialogContent>
            <DialogContentText id="alert-dialog-description">
              Do you really want to delete this project? This process cannot be undone.
            </DialogContentText>
          </DialogContent>
          <DialogActions>
            <Button onClick={handleClose} color={"info"} variant="contained">Cancel</Button>
            <Button onClick={handleConfirmDelete} autoFocus color={"error"} variant="contained">
              Delete
            </Button>
          </DialogActions>
        </Dialog>
      </div>
  );
}
export default ConfirmActionDialog