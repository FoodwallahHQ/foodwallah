import {TableFooter} from "@mui/material";
import * as React from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import DeleteIcon from '@mui/icons-material/Delete';
import Grid from "@mui/material/Grid";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";
import AddIcon from "@mui/icons-material/Add";
import {FC} from "react";
import {DynamicTableColumn, DynamicTableData} from "@/@core/utils/types";
import TextField from "@mui/material/TextField";

export interface DynamicTableProps {
  columns: readonly DynamicTableColumn[],
  data: DynamicTableData[];
  onAddItem: () => void,
  onRemoveItem: () => void,
  onInputChange: (number, string, any) => void
}

const DynamicTable: FC<DynamicTableProps> = (props) => {

  return (
      <TableContainer sx={{ maxHeight: '80vh' }}>
        <Table stickyHeader aria-label="sticky table">
          <TableHead>
            <TableRow>
              {props.columns.map((column) => (
                <TableCell
                  key={column.id}
                  align={column.align}
                  style={{ minWidth: column.minWidth }}
                >
                  {column.label}
                </TableCell>
              ))}
            </TableRow>
          </TableHead>
          <TableBody>
            {props?.data?.map((row) => {
                return (
                  <TableRow hover key={row.id} sx={{height: '4rem'}}>
                    {props.columns.map((column) => {
                      const value = row[column.id];
                      const cellKey = row.id + "_" + column.id;
                      let editable = true;
                      let width = '20px';
                      let multiline = false;
                      let placeholder = '';
                      if (column.id === 'step_number') {
                        editable = false;
                      } else if (column.id === 'image_url') {
                        width = '50%'
                        multiline = true;
                        placeholder = 'Copy and paste image links here (one per line)'
                      } else if (column.id === 'description') {
                        width = '50%'
                        multiline = true;
                      }
                      return (
                        <TableCell
                            sx={{width: width}}
                            key={cellKey}
                            align={column.align}
                        >
                          {column.id === 'step_number' ? <>{value}</> :
                              <TextField
                                  placeholder={placeholder}
                                  disabled={!editable}
                                  onChange={(e) => props.onInputChange(row.id, column.id, e)}
                                  fullWidth
                                  multiline={multiline}
                                  maxRows={10}
                                  minRows={3}
                                  value={value}
                                  sx={{'& .MuiOutlinedInput-root': {alignItems: 'baseline'}}}
                              />}

                        </TableCell>
                      )
                    })}
                  </TableRow>
                );
              })}
          </TableBody>
          <TableFooter>
            <TableRow>
              <TableCell colSpan={4}>
                <Grid container spacing={4}>
                  <Grid item lg={6}>
                    <Typography>
                      <Button
                        style={{color: 'white'}}
                        variant="contained"
                        color="success"
                        onClick={props.onAddItem}
                        startIcon={<AddIcon />}
                      >
                        Add Row
                      </Button>
                    </Typography>
                  </Grid>
                  <Grid item lg={6}>
                    <Typography>
                      <Button
                        disabled={props?.data?.length == 0}
                        style={{color: 'white'}}
                        variant="contained"
                        color="error"
                        onClick={props.onRemoveItem}
                        startIcon={<DeleteIcon />}
                      >
                        Remove Row
                      </Button>
                    </Typography>
                  </Grid>
                </Grid>
              </TableCell>
            </TableRow>
          </TableFooter>
        </Table>
      </TableContainer>
  );
}
export default DynamicTable;

