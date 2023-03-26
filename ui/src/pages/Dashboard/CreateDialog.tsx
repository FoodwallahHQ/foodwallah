import * as React from 'react';
import {FC, useCallback, useEffect, useState} from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import {useDropzone} from 'react-dropzone';
import CloudUploadTwoToneIcon from '@mui/icons-material/CloudUploadTwoTone';
import CloseTwoToneIcon from '@mui/icons-material/CloseTwoTone';
import CheckTwoToneIcon from '@mui/icons-material/CheckTwoTone';
import {
  Avatar,
  Box,
  Divider,
  FormControl, FormControlLabel,
  Grid,
  InputLabel,
  Select,
  styled, Switch,
  Typography,
} from '@mui/material';
import {CreateProjectRequest, KalicoContentType, ProjectApi} from "@/api";
import {toast, TypeOptions} from "react-toastify";
import {headerConfig} from "@/api/headerConfig";
import {auth} from "@/utils/firebase-setup";


const BoxUploadWrapper = styled(Box)(
    ({ theme }) => `
    cursor: pointer;
    height: 120px;
    border-radius: 15px;
    padding: ${theme.spacing(2)};
    background: ${theme.palette.grey["50"]};
    border: 1px dashed ${theme.palette.primary.main};
    outline: none;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    transition: ${theme.transitions.create(['border', 'background'])};

    &:hover {
      background: ${theme.palette.common.white};
      border-color: inherit;
    }
`
);
const AvatarDanger = styled(Avatar)(
    ({ theme }) => `
    background: ${theme.palette.error.light};
    width: ${theme.spacing(7)};
    height: ${theme.spacing(7)};
`
);
const AvatarWrapper = styled(Avatar)(
    ({ theme }) => `
    background: transparent;
    color: ${theme.palette.primary.main};
    width: ${theme.spacing(7)};
`
);

const AvatarSuccess = styled(Avatar)(
    ({ theme }) => `
    background: ${theme.palette.success.light};
    width: ${theme.spacing(7)};
    height: ${theme.spacing(7)};
`
);

export interface CreateDialogProps {
  open: boolean,
  onClose: () => void,
  onSubmit: (data: CreateProjectRequest) => void
}

const CreateDialog: FC<CreateDialogProps> = (props) => {
  const [paraphrase, setParaphrase] = useState(true)
  const [getRawTranscript, setGetRawTranscript] = useState(false)
  const [embedImages, setEmbedImages] = useState(false)
  const [projectName, setProjectName] = useState('')
  const [contentLink, setContentLink] = useState('')
  const [contentType, setContentType] = useState<KalicoContentType>()
  const [file, setFile] = useState('')
  const [fileName, setFileName] = useState('')
  const [fileExtension, setFileExtension] = useState('')
  const [showFileName, setShowFileName] = useState(false)
  const [contentThumbnail, setContentThumbnail] = useState<string | undefined>(undefined)
  const [contentTitle, setContentTitle] = useState<string | undefined>(undefined)
  const [contentDuration, setContentDuration] = useState<string | undefined>(undefined)

  useEffect(() => {
    // Reset the previous state
    setProjectName('')
    setContentType(KalicoContentType.Other)
    setContentLink('')
    setFileName('')
    setFileExtension('')
    setFile('')
    setShowFileName(false)
    setParaphrase(false)
    setEmbedImages(false)
    setGetRawTranscript(false)
  }, [])

  const getContentPreview = (url: string) => {
    if (url && url.includes("http")) {
      auth.onAuthStateChanged(user => {
        if (user) {
          user.getIdToken(false)
          .then(tokenResult => {
            const projectApi = new ProjectApi(headerConfig(tokenResult))
            projectApi.getContentPreview(url)
            .then(response => {
              if (response.data.title) {
                let shortTitle = response.data.title.substring(0, 200) + "..."
                setContentTitle(shortTitle)
              } else {
                setContentTitle('')
              }
              if (response.data.thumbnail) {
                setContentThumbnail(response.data.thumbnail)
              } else {
                setContentThumbnail('')
              }
              if (response.data.duration) {
                setContentDuration(response.data.duration)
              } else {
                setContentDuration('')
              }
            }).catch(e => console.log(e))
          }).catch(e => console.log(e))
        }})
    }
  }

  // const handleParaphrase = (event: any) => {
  //   event.preventDefault()
  //   setParaphrase(event.target.checked)
  // }

  const handleRawTranscript = (event: any) => {
    event.preventDefault()
    setGetRawTranscript(event.target.checked)
  }

  // const handleEmbedImages = (event: any) => {
  //   event.preventDefault()
  //   setEmbedImages(event.target.checked)
  // }

  const handleProjectName = (event: any) => {
    event.preventDefault()
    setProjectName(event.target.value)
  }

  const handleContentLink = (event: any) => {
    event.preventDefault()
    setContentLink(event.target.value)
    getContentPreview(event.target.value)
  }

  const handleContentType = (event: any) => {
    event.preventDefault()
    setContentType(event.target.value)
  }

  const showToast = (msg: string, type: TypeOptions) => {
    toast(msg, {
      type: type,
      position: toast.POSITION.TOP_CENTER
    });
  }


  const onDrop = useCallback((acceptedFiles: File[]) => {
    const conversionFactor = 1024*1024
    const maxFileSize = 1024*conversionFactor // 1 GB
    if (acceptedFiles && acceptedFiles.length > 0) {
      const f = acceptedFiles[0]
      if (f.size > maxFileSize) {
        showToast(`File size of ${Math.round(f.size/conversionFactor)} MB is too big. Max allowed is 1GB`, 'error')
      } else {
        convertBase64(f).then(binaryData => {
          setFile(binaryData + '')
          setFileExtension(f.name.split('.').pop())
          setFileName(f.name)
          setShowFileName(true)
        }).catch(e => {
          showToast(e.message, 'error')
        })
      }
    }
  }, [])

  const convertBase64 = (file) => {
    return new Promise((resolve, reject) => {
      const fileReader = new FileReader();
      fileReader.readAsDataURL(file)
      fileReader.onload = () => {
        resolve(fileReader.result);
      }
      fileReader.onerror = (error) => {
        reject(error);
      }
    })
  }

  const {
    isDragActive,
    isDragAccept,
    isDragReject,
    getRootProps,
    getInputProps
  } = useDropzone({
    accept: {
      'audio/aac': ['.aac'],
      'audio/mpeg': ['.mp3'],
      'audio/wav': ['.wav'],
      'video/mp4': ['.mp4'],
      'video/webm': ['.webm']
    },
    multiple: false,
    onDrop
  });

  const onSubmit = () => {
    props.onSubmit({
      project_name: projectName ? projectName : 'Untitled',
      paraphrase: paraphrase,
      embed_images: embedImages,
      content_link: contentLink,
      content_type: contentType,
      file: file,
      file_extension: fileExtension,
      get_raw_transcript: getRawTranscript
    })
  }
  return (
      <div className="create-project-dialog">
        <Dialog open={props.open} onClose={() => props.onClose()}>
          <DialogTitle sx={{textAlign: 'center', p: 4}}>Submit Your Content</DialogTitle>
          <DialogContent>
              <p style={{fontSize: '12px', textAlign: 'center'}}>
                Supported Platforms
              </p>
            <div style={{display: 'inline-block', width: '100%', margin: '0 auto', textAlign: 'center', padding: '8px'}}>
              <img src="/assets/images/youtube.svg" alt="YouTube" width={128} style={{display: 'inline-block'}}/>
              <img src="/assets/images/instagram.svg" alt="Instagram" width={64} style={{display: 'inline-block'}}/>
            </div>
            <Box p={0} sx={{ml: 3}}>
              <FormControlLabel
                  control={<Switch color="warning" defaultChecked={false} onChange={handleRawTranscript}/>}
                  label={'Get Raw Transcript'}
              />
            </Box>
            {/*<Box p={0}>*/}
            {/*  <FormControlLabel*/}
            {/*      control={<Switch color="warning" defaultChecked={false} onChange={handleParaphrase}/>}*/}
            {/*      label={'Paraphrase'}*/}
            {/*  />*/}
            {/*</Box>*/}
            {/*<Box p={0}>*/}
            {/*  <FormControlLabel*/}
            {/*      disabled*/}
            {/*      control={<Switch color="warning" defaultChecked={false} onChange={handleEmbedImages}/>}*/}
            {/*      label={'Intelligently embed images (coming soon)'}*/}
            {/*  />*/}
            {/*</Box>*/}
            {/*<Divider sx={{width: '100%'}}/>*/}
            <Box p={3}>
              <Grid container spacing={3}>
                <Grid item xs={12}>
                  <TextField
                      InputLabelProps={{
                        style: {background: '#fff', padding: 0},
                        shrink: true,
                      }}
                      onChange={handleProjectName}
                      value={projectName}
                      className="create-dialog-textfield"
                      fullWidth
                      name="project_name"
                      variant="outlined"
                      label={('Project name')}
                      placeholder={('This is my video...')}
                  />
                </Grid>
                <Grid item xs={12}>
                  {/*<p style={{fontSize: '10px', marginBottom: '10px'}}>We currently support only food recipe videos. More coming soon.</p>*/}
                  <FormControl fullWidth variant="outlined">
                    <InputLabel htmlFor="content_type">{('Content Type')}</InputLabel>
                    <Select
                        onChange={handleContentType}
                        required
                        native
                        label={('Content Type')}
                        inputProps={{
                          name: 'content_type'
                        }}
                    >
                      <option aria-label="None" value="" />
                      {/*<option value={KalicoContentType.Diy}>{('DIY')}</option>*/}
                      <option value={KalicoContentType.FoodRecipe}>{('Food Recipe')}</option>
                      {/*<option value={KalicoContentType.Interview}>{('Interview')}</option>*/}
                      {/*<option value={KalicoContentType.Lecture}>{('Lecture')}</option>*/}
                      {/*<option value={KalicoContentType.Podcast}>{('Podcast')}</option>*/}
                      <option value={KalicoContentType.Other}>{('Other')}</option>
                    </Select>
                  </FormControl>
                </Grid>
                <Grid item xs={12}>
                  <TextField
                      key='content_link'
                      InputLabelProps={{
                        style: {background: '#fff'},
                        shrink: true,
                      }}
                      onChange={handleContentLink}
                      value={contentLink}
                      className="create-dialog-textfield"
                      fullWidth
                      name="content_link"
                      variant="outlined"
                      label={('Content Link')}
                      // https://www.youtube.com/watch?v=91BUM3WhCfo&t=6s
                      placeholder={('https://www.youtube.com/watch?v=91BUM...')}
                  />
                </Grid>
                {contentTitle &&
                    <Grid item xs={12} sx={{display: 'inline-flex'}}>
                      {contentThumbnail &&
                          <Box className="content-link-preview">
                            <img src={contentThumbnail} alt={''}/>
                          </Box>
                      }
                      <Box className="content-link-preview">
                        {contentTitle && <h6>{contentTitle}</h6>}
                        {contentDuration && <span>{contentDuration}</span>}
                      </Box>
                    </Grid>
                }
              </Grid>
            </Box>
            <Divider>OR</Divider>
            <Box p={1}>
              <BoxUploadWrapper {...getRootProps()}>
                <input {...getInputProps()} />
                {isDragAccept && (
                    <>
                      <AvatarSuccess variant="rounded">
                        <CheckTwoToneIcon/>
                      </AvatarSuccess>
                      <Typography
                          sx={{
                            mt: 2
                          }}
                      >
                        {('Drop the files to start uploading')}
                      </Typography>
                    </>
                )}
                {isDragReject && (
                    <>
                      <AvatarDanger variant="rounded">
                        <CloseTwoToneIcon />
                      </AvatarDanger>
                      <Typography
                          sx={{
                            mt: 2
                          }}
                      >
                        {('You cannot upload these file types')}
                      </Typography>
                    </>
                )}
                {!isDragActive && (
                    <>
                      <AvatarWrapper variant="rounded">
                        <CloudUploadTwoToneIcon />
                      </AvatarWrapper>
                      <Typography
                          sx={{
                            mt: 1
                          }}
                      >
                        {
                          showFileName ? `${fileName}` : `Drag & drop files here`
                        }
                      </Typography>
                      <Typography
                          sx={{
                            fontSize: '10px',
                            mb: 2
                          }}
                          variant='body1'
                      >
                        {'Supported formats: .aac, .mp3, .wav, .mp4. .webm. Max 100MB'}
                      </Typography>
                    </>
                )}
              </BoxUploadWrapper>
            </Box>
          </DialogContent>
          <DialogActions>
            <Button onClick={() => props.onClose()} variant={'contained'} color={'error'}>Cancel</Button>
            <Button onClick={onSubmit} variant={'contained'} color={'success'}>Create</Button>
          </DialogActions>
        </Dialog>
      </div>
  );
}
export default CreateDialog