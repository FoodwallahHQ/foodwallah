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
import {CmsApi, RecipeApi, RecipeLite} from "@/api";
import {headerConfig} from "@/api/headerConfig";
import {showMessageBar} from "@/utils/message";
import {useSnackbar} from "notistack";

const Dashboard = () => {
  const [showVideoUrlCard, setShowVideoUrlCard] = useState<boolean>(false)
  const [submitted, setSubmitted] = useState<boolean>(false)
  const [documents, setDocuments] = useState<RecipeLite[]>([])
  const [url, setUrl] = useState<string>();
  const { enqueueSnackbar } = useSnackbar();

  const onSubmit = (): void => {
    if (url) {
      new RecipeApi()
      .createRecipe({value: url})
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
      })
      .catch(e => console.log(e))
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
    setUrl(event.target.value)
  }

  useEffect(() => {
    auth.onAuthStateChanged(user => {
      if (user) {
        user.getIdTokenResult(false)
        .then(tokenResult => {
          new CmsApi(headerConfig(tokenResult.token))
          .getAllPosts(0, 9999)
          .then(result => {
            if (result.data) {
              if (result.data.records.length > 0) {
                setDocuments(result.data.records)
              }
              showMessageBar({
                message: "Received " + result.data.records.length + " documents",
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
          showVideoUrlCard ?  <Card sx={{maxHeight: '300px', minHeight: '200px'}}>
            <CardHeader title="Submit a Recipe Link from YouTube" titleTypographyProps={{ variant: 'h6' }} />
            <CardContent>
              <Grid container spacing={5}>
                <Grid item xs={12}>
                  {submitted ? (
                      <Box>
                        <Typography>Link submitted. Please refresh the page in a few minutes to see the draft.</Typography>
                      </Box>) :
                      <TextField
                          onChange={handleChange}
                          fullWidth
                          label='Submit a Link'
                          placeholder="https://www.youtube.com/watch?v=mCNH9rn2OS0"
                          sx={{'& .MuiOutlinedInput-root': {alignItems: 'baseline'}}}
                      />
                  }
                </Grid>
              </Grid>
            </CardContent>
            <CardActions>
              {!submitted &&
                  <Button
                      sx={{backgroundColor: '#2fb92f', mr: 2}}
                      onClick={onSubmit}
                      size='large'
                      type='submit'
                      variant='contained'>
                    Submit
                  </Button>
              }
              <Button
                  sx={{backgroundColor: '#e15e5e', mr: 2}}
                  onClick={onCancelCreateNewPost}
                  size='large'
                  type='submit'
                  variant='contained'>
                Close
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
