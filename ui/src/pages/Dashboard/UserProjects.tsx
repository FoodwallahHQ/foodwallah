import React, {FC, useEffect, useState} from 'react';
import {observer} from "mobx-react";
import {Box, Chip} from "@mui/material";
import {PATHS} from "@/utils/constants";
import {ProjectApi, UserProjectsResponse} from "@/api";
import {headerConfig} from "@/api/headerConfig";
import { styled } from '@mui/material/styles';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell, { tableCellClasses } from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import {getFormattedDate} from "@/utils/utils";
import {useStore} from "@/hooks/useStore";
import firebase from "firebase/compat";
import User = firebase.User;

const StyledTableCell = styled(TableCell)(({ theme }) => ({
  [`&.${tableCellClasses.head}`]: {
    backgroundColor: theme.palette.warning.light,
    color: theme.palette.common.white,
  },
  [`&.${tableCellClasses.body}`]: {
    fontSize: 14,
  },
}));

const StyledTableRow = styled(TableRow)(({ theme }) => ({
  '&:nth-of-type(odd)': {
    backgroundColor: theme.palette.action.hover,
  },
  // hide last border
  '&:last-child td, &:last-child th': {
    border: 0,
  },
}));



export interface UserProjectProps {
  userProjects?: UserProjectsResponse,
  user: User
}

const UserProjects: FC<UserProjectProps> = observer((props) => {
  const [userProjects, setUserProjects] = useState<UserProjectsResponse | undefined>(undefined)
  const [previews, setPreviews] = useState<{}>({})
  const [urls, setUrls] = useState<string[]>([])
  const [previewsRequested, setPreviewsRequested] = useState<boolean>(false)
  const store = useStore()


  useEffect(() => {
    setUserProjects(props.userProjects)
    if (props.userProjects?.records) {
      const uniqueUrls: string [] = []
      for (let i = 0; i < props.userProjects.records.length; i++) {
        const url = props.userProjects.records[i].content_url
        if (!uniqueUrls.includes(url)) {
          uniqueUrls.push(url)
        }
      }
      setUrls(uniqueUrls)
    }
  }, [props.userProjects])

  useEffect(() => {
    if (urls.length > 0 && !previewsRequested) {
      urls.map(url => getContentPreview(url))
      setPreviewsRequested(true)
    }
  }, [urls])

  const getContentPreview = (url: string) => {
      props?.user?.getIdToken(false)
      .then(tokenResult => {
        const projectApi = new ProjectApi(headerConfig(tokenResult))
        projectApi.getContentPreview(url)
        .then(response => {
          let shortTitle = ''
          if (response.data.title) {
            shortTitle = response.data.title.substring(0, 200) + "..."
          }
          const record = {
            title: shortTitle,
            thumbnail: response.data.thumbnail,
            duration: response.data.duration
          }
          store.sessionDataStore.addContentPreview(url, record)
        }).catch(e => console.log(e))
      }).catch(e => console.log(e))
  }

  useEffect(() => {
    setPreviews(store.sessionDataStore.contentPreview)
  }, [store.sessionDataStore.contentPreview])


  const getProjectUrl = (projectUid: string) => {
    return PATHS.PROJECT + '/' + projectUid + "?editable=false"
  }

  const getUserCount = () => {
    return userProjects?.num_users ? userProjects?.num_users : 0
  }
  return (
      <section>
        <Box sx={{pb: 1, pt: 10}}>
         <Box sx={{
           p: 2
         }}>
           <Chip label={"Total users: " + getUserCount()} color="success" />
         </Box>
        </Box>
        <Box sx={{pb: 15}}>
          <TableContainer component={Paper}>
            <Table sx={{ minWidth: '100%' }} aria-label="customized table">
              <TableHead>
                <TableRow>
                  <StyledTableCell></StyledTableCell>
                  <StyledTableCell>User Name</StyledTableCell>
                  <StyledTableCell align="left">Email</StyledTableCell>
                  <StyledTableCell align="left">Project Name</StyledTableCell>
                  <StyledTableCell align="left">Content Link</StyledTableCell>
                  <StyledTableCell align="left">Account Created On</StyledTableCell>
                  <StyledTableCell align="left">Project Created On</StyledTableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {userProjects?.records?.map((row, index) => (
                    <StyledTableRow key={row.project_id}>
                      <StyledTableCell align="left" sx={{width: '2%'}}>{index + 1}</StyledTableCell>
                      <StyledTableCell component="th" scope="row" sx={{width: '10%'}}>{row.user_full_name}</StyledTableCell>
                      <StyledTableCell align="left" sx={{width: '10%'}}>{row.email}</StyledTableCell>
                      <StyledTableCell align="left" sx={{width: '15%'}}><a href={getProjectUrl(row.project_id)} target="_blank">{row.project_name}</a></StyledTableCell>
                      <StyledTableCell align="left">
                            <Box sx={{display: 'inline-flex'}}>
                            <Box className="content-link-preview">
                              <img src={previews[row.content_url]?.thumbnail} alt={''}/>
                              <h6 style={{marginTop: '15px', fontSize: '14px'}}>{previews[row.content_url]?.title}</h6>
                              <span style={{fontSize: '12px'}}>{previews[row.content_url]?.duration}</span>
                            </Box>
                            </Box>
                        <a href={row.content_url} target="_blank">{row.content_url}</a>
                      </StyledTableCell>
                      <StyledTableCell align="left" sx={{width: '12%'}}>{getFormattedDate(row.registered_on)}</StyledTableCell>
                      <StyledTableCell align="left" sx={{width: '12%'}}>{getFormattedDate(row.project_created_at)}</StyledTableCell>
                    </StyledTableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        </Box>
      </section>
  );
})

export default UserProjects;
