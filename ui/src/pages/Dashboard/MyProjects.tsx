import React, {FC, useEffect, useState} from 'react';
import {observer} from "mobx-react";
import {Box, Button, Grid, Typography} from "@mui/material";
import DeleteIcon from '@mui/icons-material/Delete';
import ConfirmActionDialog from "@/pages/Dashboard/ConfirmActionDialog";
import {useRouter} from "next/router";
import {PATHS} from "@/utils/constants";
import PendingJobs from "@/pages/Dashboard/PendingJobs";
import {toast, ToastContainer, TypeOptions} from "react-toastify";
import {CreateProjectRequest, Project, ProjectApi} from "@/api";
import {headerConfig} from "@/api/headerConfig";
import {auth} from "@/utils/firebase-setup";
import CreateDialog from "@/pages/Dashboard/CreateDialog";

export interface MyProjectsProps {
  projects?: Project[]
}

const folderColors = {
  BLUE: 'blue',
  RED: 'red',
  GREEN: 'green',
  YELLOW: 'yellow'
}

const MyProjects: FC<MyProjectsProps> = observer((props) => {
  const router = useRouter()
  const [projects, setProjects] = useState<Project[]>([])
  const [deleteDialogOpen, setDeleteDialogOpen] = useState<boolean>(false)
  const [createDialogOpen, setCreateDialogOpen] = useState<boolean>(false)
  const [projectInProgress, setProjectInProgress] = useState<Project | undefined>(undefined)
  const [pendingDeleteProjectId, setPendingDeleteProjectId] = useState<string | undefined>(undefined)

  useEffect(() => {
    setProjects(props.projects)
  }, [props.projects])

  const onCreate = () => {
    setCreateDialogOpen(true)
  }

  const onSubmitProject = (request: CreateProjectRequest) => {
    auth.onAuthStateChanged(user => {
      if (user) {
        user.getIdToken(false)
        .then(tokenResult => {
          const projectApi = new ProjectApi(headerConfig(tokenResult))
          if (request.file) {
            // If there is a file to upload, show upload progress
            toast.promise(
                projectApi.createProject(request),
                {
                  pending: 'Uploading file...',
                  success: 'Your project is now being processed',
                }
            ).then(result => {
              if (result && result.data) {
                const newProject: Project = {
                  project_name: result.data.project_name,
                  project_uid: result.data.project_id
                }
                setProjectInProgress(newProject)
              }
            })
            .catch(e => {
              const msg = e?.response?.data?.message ? e?.response?.data?.message : 'Something went wrong while uploading your file'
              toast(msg, {
                type: 'error',
                position: toast.POSITION.TOP_CENTER
              });
            })
            setCreateDialogOpen(false)
          } else {
            projectApi.createProject(request)
            .then(response => {
              let msg = response.data.error
              let type: TypeOptions = 'error'
              if (response.data && response.data.project_id) {
                msg = 'Your project is now being processed'
                type = 'success'
              }
              toast(msg, {
                type: type,
                position: toast.POSITION.TOP_CENTER
              });
              setCreateDialogOpen(false)
            }).catch(e => {
              toast(e.message, {
                type: 'error',
                position: toast.POSITION.TOP_CENTER
              });
            })
          }
        }).catch(e => console.log(e))
      }
    })
  }

  const onRefreshProjectList = () => {
    auth.onAuthStateChanged(user => {
      if (user) {
        user.getIdToken(false)
        .then(tokenResult => {
          const projectApi = new ProjectApi(headerConfig(tokenResult))
          projectApi.getAllProjects()
          .then(response => {
            if (response.data && response.data && response.data.records) {
              setProjects([...response.data.records])
            }
          }).catch(e => console.log(e))
        }).catch(e => console.log(e))
      }
    })
  }

  const onOpenProject = (projectId: string) => {
    router.push({
      pathname: PATHS.PROJECT + '/' + projectId,
      query: {editable: true}
    }, undefined, {shallow: true})
    .catch(e => console.log(e))
  }
  const onDeleteProject = (projectId: string) => {
    setDeleteDialogOpen(true)
    setPendingDeleteProjectId(projectId)
  }

  const onCloseDeleteDialog = (doDelete: boolean) => {
    setDeleteDialogOpen(false)
    if (doDelete && pendingDeleteProjectId) {
      auth.onAuthStateChanged(user => {
        if (user) {
          user.getIdToken(false)
          .then(tokenResult => {
            const projectApi = new ProjectApi(headerConfig(tokenResult))
            projectApi.deleteProject(pendingDeleteProjectId)
            .then(response => {
              if (response.data.status) {
                setPendingDeleteProjectId(undefined)
                const filteredProjects = projects.filter(it => it.project_uid !== pendingDeleteProjectId)
                setProjects([...filteredProjects])
              }
            }).catch(e => console.log(e))
          }).catch(e => console.log(e))
        }
      })
    } else {
      setPendingDeleteProjectId(undefined)
    }
  }

  const onCloseCreateDialog = () => {
    setCreateDialogOpen(false)
  }

  const abridgedTitle = (title: string) => {
    const maxLength = 62
    if (title && title.length > maxLength) {
      const prefix = title.slice(0, maxLength)
      return prefix + '...'
    }
    return title;
  }

  const getProjectComponents = () => {
    let prevClassName = ''
    return projects.map((item, index) => {
      const folderClass = getFolderClassName(prevClassName)
      prevClassName = folderClass
      return (
          <Grid item sx={{p: 2}} sm={3} key={index}>
            <div className={folderClass}>
              <Button
                  sx={{width: '30px'}}
                  color="warning"
                  startIcon={<DeleteIcon/>}
                  className="folder-delete-btn"
                  size='large'
                  variant='text'
                  onClick={() => onDeleteProject(item.project_uid)}
              />
              <h6 onClick={() => onOpenProject(item.project_uid)}
                  className="folder-title">{abridgedTitle(item.project_name)}</h6>
            </div>
          </Grid>
      )
    })
  }

  const getFolderClassName = (prevColor: string): string => {
    // Get sequential colors as follows: blue -> red -> green -> yellow
    const name = "my-files-folder "
    let nextColor = folderColors.BLUE
    if (prevColor) {
      if (prevColor.includes(folderColors.BLUE)) {
        nextColor = folderColors.RED
      } else  if (prevColor.includes(folderColors.RED)) {
        nextColor = folderColors.GREEN
      } else  if (prevColor.includes(folderColors.GREEN)) {
        nextColor = folderColors.YELLOW
      } else  if (prevColor.includes(folderColors.YELLOW)) {
        nextColor = folderColors.BLUE
      }
    }
    return name + nextColor
  }

  return (
      <>
      <Grid container className="dashboard-container">
        <Grid item sm={12} md={6}>
          <CreateDialog open={createDialogOpen}
                        onSubmit={onSubmitProject}
                        onClose={onCloseCreateDialog}/>
          <button
              onClick={onCreate}
          >
          <Box className="create-project-btn-box">
            <img src="/assets/images/plus.png" alt="" width={64}/>
            <Typography variant='subtitle2' sx={{ textAlign: 'center', p: 1, fontSize: '12px'}}>Create a new project</Typography>
          </Box>
          </button>
        </Grid>
        <Grid item sm={12} md={6} justifyContent='flex-end' sx={{display: 'flex'}}>
          <Box className="dashboard-pending-jobs">
            <PendingJobs
                project={projectInProgress}
                onRefreshProjectList={onRefreshProjectList}
            />
          </Box>
        </Grid>
        <Grid item sm={12} className="files-section">
          <h3>Your Files</h3>
        </Grid>
        {
          getProjectComponents()
        }
      </Grid>
        <ToastContainer
            style={{width: '100%', maxWidth: '600px'}}
            position="top-center"
            autoClose={5000}
            hideProgressBar
            newestOnTop={false}
            closeOnClick
            rtl={false}
            pauseOnFocusLoss
            draggable
            pauseOnHover
            theme="colored"/>
        <ConfirmActionDialog
            open={deleteDialogOpen}
            onCloseDialog={onCloseDeleteDialog}
        />
        </>
  );
})

export default MyProjects;
