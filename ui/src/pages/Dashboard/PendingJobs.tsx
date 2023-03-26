import {Avatar, Box, Card, LinearProgress, styled, Typography} from '@mui/material';
import CloudSyncIcon from '@mui/icons-material/CloudSync';
import {FC, useEffect, useState} from "react";
import {JobStatus, Project, ProjectApi} from "@/api";
import {headerConfig} from "@/api/headerConfig";
import {auth} from "@/utils/firebase-setup";

const AvatarWrapperError = styled(Avatar)(
  ({ theme }) => `
      width: ${theme.spacing(2)};
      height: ${theme.spacing(2)};
      border-radius: 15px;
      background-color: #fff;
      color:  red;
      margin-bottom: ${theme.spacing(2)};
`
);

export interface PendingJobsProps {
  project?: Project,
  user?: {},
  onRefreshProjectList: () => void
}
const PendingJobs: FC<PendingJobsProps> = (props) => {
  const [percent, setPercent] = useState(0)
  const [estimatedTime, setEstimatedTime] = useState('')
  const [progressMessage, _setProgressMessage] = useState('Processing')
  const [intervalRef, setIntervalRef] = useState<any|undefined>(undefined)
  const [showProgress, _setShowProgress] = useState(true)
  const [failed, setFailed] = useState(false)
  const [reasonFailed, setReasonFailed] = useState('')
  const [pendingJob, setPendingJob] = useState<Project | undefined>(undefined)

  useEffect(() => {
    const interval = setInterval(() => {
      getProgress()
    }, 2000)
    setIntervalRef(interval)

    return () => {
      clearInterval(interval)
    }
  }, [])

  useEffect(() => {
    if (percent === 100 && intervalRef) {
      clearInterval(intervalRef)
    }
  }, [percent])

  useEffect(() => {
    const interval = setInterval(() => {
      props.onRefreshProjectList()
    }, 5000)

    return () => {
      clearInterval(interval)
    }
  }, [])

  const getProgress = () => {
    auth.onAuthStateChanged(user => {
      if (user) {
        user.getIdToken(false)
        .then(tokenResult => {
          const projectApi = new ProjectApi(headerConfig(tokenResult))
          projectApi.getProjectJobStatus("")
          .then(response => {
            if (response.data) {
              if (response.data.project_id) {
                if (response.data.status === JobStatus.Failed) {
                  setFailed(true)
                  setReasonFailed(response.data.message)
                } else {
                  setPercent(response.data.percent_complete)
                  setEstimatedTime(response.data.estimated_time)
                  setFailed(failed)
                  setReasonFailed('')
                }
                setPendingJob({
                  project_name: response.data.project_name,
                  project_uid: response.data.project_id
                })
              }
            }
          }).catch(e => console.log(e))
        }).catch(e => console.log(e))
      }

    })
  }

  if (!pendingJob) {
    return <></>
  }

  return (
    <Card
      sx={{
        p: 2,
        m: 2,
        height: '185px',
        border: '3px solid orange',
        borderRadius: '15px',
        boxShadow: 'none'
      }}
    >
      <AvatarWrapperError>
        <CloudSyncIcon />
      </AvatarWrapperError>
      {pendingJob && showProgress && !failed ? <>
        <Typography
            variant="body1"
            sx={{
              pb: 1,
              display: 'inline-flex'
            }}
        >
          {progressMessage}
        </Typography>
        {' '}
        <Typography
            variant="body1"
            sx={{
              color: 'orange',
              display: 'inline-flex'
            }}
        >
          <strong>{pendingJob.project_name}</strong>
        </Typography>
            <Box pt={2}>
              <LinearProgress value={percent} color="error" variant="determinate" />
            </Box>
            <Box>
              <Typography sx={{fontSize: '12px', pt: 1}}>
                Estimated time: {estimatedTime}
              </Typography>
            </Box>
      </> :
          <>
            {
              failed ? <>
                    <Typography
                        variant="body1"
                        sx={{
                          color: 'inherit',
                          display: 'inline-flex'
                        }}
                    >
                     {pendingJob.project_name + ' has failed'}

                </Typography>
                    <Box>
                      <Typography sx={{fontSize: '14px', pt: 1}} color="error">
                        <strong>{reasonFailed}</strong>
                      </Typography>
                    </Box>


              </> :
                  <Typography
                      variant="body1"
                      sx={{
                        pb: 1,
                        display: 'inline-flex'
                      }}
                  >
                    No pending jobs
                  </Typography>
            }
          </>
      }
    </Card>
  );
}

export default PendingJobs;
