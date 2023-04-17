import * as React from 'react';
import {FC} from 'react';
import Paper from '@mui/material/Paper';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TablePagination from '@mui/material/TablePagination';
import TableRow from '@mui/material/TableRow';
import Chip from "@mui/material/Chip";
import {RecipeLite} from "@/api";
import {getFormattedDate} from "@/utils/utils";

interface Column {
  id: 'id' | 'title' | 'created_at' | 'published';
  label: string;
  minWidth?: number;
  align?: 'right';
  format?: (value: number) => string;
}

const columns: readonly Column[] = [
  { id: 'id', label: 'ID', minWidth: 100 },
  { id: 'title', label: 'Title', minWidth: 170 },
  { id: 'created_at', label: 'Created', minWidth: 170 },
  { id: 'published', label: 'Published', minWidth: 170 },
];

export interface RecentDocumentsProps {
  documents?: RecipeLite[]
}

const RecentDocuments: FC<RecentDocumentsProps> = (props) => {
  const [page, setPage] = React.useState(0);
  const [rowsPerPage, setRowsPerPage] = React.useState(10);

  const handleChangePage = (_event: unknown, newPage: number) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event: React.ChangeEvent<HTMLInputElement>) => {
    setRowsPerPage(+event.target.value);
    setPage(0);
  };

  const getStatusColor = (published: boolean): "error" | "success" => {
    return published ? "success" : "error"
  }

  const openEditor = (docId: number): void => {
    window.open('/posts/' + docId, '_blank');
  }


  return (
    <Paper sx={{ width: '100%', overflow: 'hidden' }}>
      <TableContainer sx={{ maxHeight: '80vh' }}>
        <Table stickyHeader aria-label="sticky table">
          <TableHead>
            <TableRow>
              {columns.map((column) => (
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
            {props.documents
              .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
              .map((row) => {
                return (
                  <TableRow hover key={row.slug} sx={{height: '4rem', cursor: 'pointer'}}
                            onClick={() => openEditor(row.id)}>
                    {columns.map((column) => {
                      let value = row[column.id];
                      if(column.id === "published") {
                        // @ts-ignore
                        return (
                          <TableCell key={column.id}>
                            <Chip
                              label={value}
                              color={getStatusColor(value as boolean)}
                              sx={{
                                height: 24,
                                fontSize: '0.75rem',
                                textTransform: 'capitalize',
                                '& .MuiChip-label': { fontWeight: 500 }
                              }}
                            />
                          </TableCell>
                        )
                      }
                      if (column.id === 'created_at') {
                        value = getFormattedDate(value as number)
                      }
                      return (
                        <TableCell key={column.id} align={column.align}>
                          {value}
                        </TableCell>
                      )
                    })}
                  </TableRow>
                );
              })}
          </TableBody>
        </Table>
      </TableContainer>
      <TablePagination
        rowsPerPageOptions={[10, 25, 100]}
        component="div"
        count={props.documents.length}
        rowsPerPage={rowsPerPage}
        page={page}
        onPageChange={handleChangePage}
        onRowsPerPageChange={handleChangeRowsPerPage}
      />
    </Paper>
  );
}

export default RecentDocuments;
