import RecentDocuments from "@/@core/document/recent/RecentDocuments";
import { Button} from "@mui/material";
import AddIcon from '@mui/icons-material/Add';
import Box from "@mui/material/Box";
import {Authenticated} from "@/components/auth/Authenticated";
import Grid from "@mui/material/Grid";
import TextField from "@mui/material/TextField";
import CardContent from "@mui/material/CardContent";
import React, {useEffect, useState} from "react";
import CardHeader from "@mui/material/CardHeader";
import CardActions from "@mui/material/CardActions";
import Card from "@mui/material/Card";
import Typography from "@mui/material/Typography";
import {auth} from "@/utils/firebase-setup";
import {BlogApi, BlogPost, CmsApi} from "@/api";
import {headerConfig} from "@/api/headerConfig";
import {showMessageBar} from "@/utils/message";
import {useSnackbar} from "notistack";

const Dashboard = () => {
  const [showVideoUrlCard, setShowVideoUrlCard] = useState<boolean>(false)
  const [submitted, setSubmitted] = useState<boolean>(false)
  const [documents, setDocuments] = useState<BlogPost[]>([])
  const [values, setValues] = useState<string>();
  const { enqueueSnackbar } = useSnackbar();

  const getCleanUrls = (): string[] => {
    return values.split(",")
    .filter(it => it.length > 0)
    .map(it => it.trim())
  }
  const onSubmit = (): void => {
    if (values) {
      const urls = getCleanUrls();
      if (urls && urls.length > 0) {
        auth.onAuthStateChanged(user => {
          if (user) {
            user.getIdTokenResult(false)
            .then(tokenResult => {
              new BlogApi(headerConfig(tokenResult.token))
              .processVideoRecipe(urls)
              .then(result => {
                if (result.data) {
                  const error = result.data.error;
                  const status = result.data.status;
                  showMessageBar({
                    message: error ? error : status,
                    snack: enqueueSnackbar,
                    error: !!error
                  });
                }
              }).catch(e => console.log(e))
            }).catch(e => console.log(e))
          }
        })
      }
    }
    setSubmitted(true)
  }

  const onCreateNewPost = (): void => {
    setShowVideoUrlCard(true)
  }

  const onCancelCreateNewPost = (): void => {
    setShowVideoUrlCard(false)
    setSubmitted(false)
  }

  const handleChange = (event: any) => {
    event.preventDefault();
    setValues(event.target.value)
  }

  useEffect(() => {
    auth.onAuthStateChanged(user => {
      if (user) {
        user.getIdTokenResult(false)
        .then(tokenResult => {
          new CmsApi(headerConfig(tokenResult.token))
          .getAllPosts()
          .then(result => {
            if (result.data) {
              if (result.data.length > 0) {
                setDocuments(result.data)
              }
              showMessageBar({
                message: "Received " + result.data.length + " documents",
                snack: enqueueSnackbar,
                error: false
              });
            }
          }).catch(e => console.log(e))
        }).catch(e => console.log(e))
      }
    })
  }, [])


  return (
    <Authenticated>
      <Box sx={{overflow: 'hidden'}}>
      <Box sx={{
        mb: '2rem'
      }}>

        {
          showVideoUrlCard ?  <Card sx={{maxHeight: '300px', minHeight: '300px'}}>
            <CardHeader title="Video URLs" titleTypographyProps={{ variant: 'h6' }} />
            <CardContent>
              <Grid container spacing={5}>
                <Grid item xs={12}>
                  {submitted ? (
                      <Box sx={{minHeight: '150px'}}>
                        <Typography>Your links have been submitted. After they've been processed, you will see them below as drafts.</Typography>
                      </Box>) :
                      <TextField
                          onChange={handleChange}
                          fullWidth
                          multiline
                          minRows={3}
                          rows={5}
                          label='URLs'
                          placeholder='A comma-separated list of URLs to process'
                          sx={{'& .MuiOutlinedInput-root': {alignItems: 'baseline'}}}
                      />
                  }
                </Grid>
              </Grid>
            </CardContent>
            <CardActions>
              <Button
                  sx={{backgroundColor: '#2fb92f', mr: 2}}
                  onClick={onSubmit}
                  size='large'
                  type='submit'
                  variant='contained'>
                Create
              </Button>
              <Button
                  sx={{backgroundColor: '#e15e5e', mr: 2}}
                  onClick={onCancelCreateNewPost}
                  size='large'
                  type='submit'
                  variant='contained'>
                Cancel
              </Button>
            </CardActions>
          </Card> : <Button
              sx={{backgroundColor: '#ff8b41'}}
              variant="contained"
              endIcon={<AddIcon/>}
              onClick={onCreateNewPost}>
            New Post
          </Button>
        }

      </Box>
      <RecentDocuments documents={documents}/>
      </Box>
  </Authenticated>
  )
}
export default Dashboard;
