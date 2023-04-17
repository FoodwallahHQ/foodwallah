import React, {FC, useCallback, useEffect, useState} from "react";
import Card from "@mui/material/Card";
import CardHeader from "@mui/material/CardHeader";
import CardContent from "@mui/material/CardContent";
import TextField from "@mui/material/TextField";
import CardActions from "@mui/material/CardActions";
import Button from "@mui/material/Button";
import CloudUploadIcon from '@mui/icons-material/CloudUpload';
import ContentCopyIcon from '@mui/icons-material/ContentCopy';
import {
  Avatar,
  Box,
  Divider,
  Grid,
  styled,
  Typography,
} from '@mui/material';
import CloudUploadTwoToneIcon from '@mui/icons-material/CloudUploadTwoTone';
import CloseTwoToneIcon from '@mui/icons-material/CloseTwoTone';
import CheckTwoToneIcon from '@mui/icons-material/CheckTwoTone';

import { CircularProgress} from "@mui/material";
import IngredientList from "@/@core/document/editor/IngredientList";
import {Authenticated} from "@/components/auth/Authenticated";
import {
  CmsApi, Ingredient,
  RecipeFull,
  RecipeStep
} from "@/api";
import {useDropzone} from 'react-dropzone';
import {auth} from "@/utils/firebase-setup";
import {headerConfig} from "@/api/headerConfig";
import {showMessageBar} from "@/utils/message";
import {useSnackbar} from "notistack";
import RecipeStepList from "@/@core/document/editor/RecipeStepList";
import {IngredientListData, RecipeStepListData} from "@/@core/utils/types";
import Tabs from "@mui/material/Tabs";
import Tab from "@mui/material/Tab";
import PublishIcon from "@mui/icons-material/Publish";
import DownloadIcon from "@mui/icons-material/Download";

interface TabPanelProps {
  children?: React.ReactNode;
  index: number;
  value: number;
}

const BoxUploadWrapper = styled(Box)(
    ({ theme }) => `
    cursor: pointer;
    height: 220px;
    max-width: 600px;
    margin: 0 auto;
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

function TabPanel(props: TabPanelProps) {
  const { children, value, index, ...other } = props;

  return (
      <div
          role="tabpanel"
          hidden={value !== index}
          id={`simple-tabpanel-${index}`}
          aria-labelledby={`simple-tab-${index}`}
          {...other}
      >
        {value === index && (
            <Box sx={{ p: 3 }}>
              <Typography>{children}</Typography>
            </Box>
        )}
      </div>
  );
}

function a11yProps(index: number) {
  return {
    id: `simple-tab-${index}`,
    'aria-controls': `simple-tabpanel-${index}`,
  };
}

export async function getServerSideProps(context) {
  return {
    props: {
      id: context.query.id
    }
  }
}

export interface DocumentEditorProps {
  id: number
}

const DocumentEditor: FC<DocumentEditorProps> = (props) => {
  const [showProgress, setShowProgress] = useState<boolean>(false);
  const [blogPost, setBlogPost] = useState<RecipeFull>();
  const [tabIndex, setTabIndex] = React.useState(0);
  const [file, setFile] = useState('')
  const [fileExtension, setFileExtension] = useState('')
  const [fileName, setFileName] = useState('')
  const [showFileName, setShowFileName] = useState(false)
  const [uploadedImageUrl, setUploadedImageUrl] = useState('')
  const { enqueueSnackbar } = useSnackbar();

  const onDrop = useCallback((acceptedFiles: File[]) => {
    const conversionFactor = 1024*10
    setFile('')
    setFileExtension('')
    const maxFileSize = 1024*conversionFactor // 10 MB
    if (acceptedFiles && acceptedFiles.length > 0) {
      const f = acceptedFiles[0]
      if (f.size > maxFileSize) {
        const msg = `File size of ${Math.round(f.size/conversionFactor)} MB is too big. Max allowed is 10MB`
        showMessageBar({
          message: msg,
          snack: enqueueSnackbar,
          error: true
        });

      } else {
        convertBase64(f).then(binaryData => {
          setFile(binaryData + '')
          setFileExtension(f.name.split('.').pop())
          setFileName(f.name)
          setShowFileName(true)
        }).catch(e => {
          showMessageBar({
            message: e.message,
            snack: enqueueSnackbar,
            error: true
          });
        })
      }
    }
  }, [])

  const {
    isDragActive,
    isDragAccept,
    isDragReject,
    getRootProps,
    getInputProps
  } = useDropzone({
    accept: {
      'image/png': ['.png'],
      'image/jpeg': ['.jpg', '.jpeg']
    },
    multiple: false,
    onDrop
  });

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

  const handleTabChange = (_event: React.SyntheticEvent, newValue: number) => {
    setTabIndex(newValue);
  };

  const onRecipeStepChange = (items: RecipeStepListData[]): void => {
    const convertToRecipeStep = (item: RecipeStepListData): RecipeStep => {
      return {
        step_number: item.step_number,
        images: item.images?.split("\n"),
        text: item.text,
      }
    }
    blogPost.instructions = items.map(it => convertToRecipeStep(it))
    setBlogPost({...blogPost})
  }

  const onIngredientChange = (items: IngredientListData[]): void => {
    const convertToIngredients = (item: IngredientListData): Ingredient => {
      return {
        amount: item.qt,
        ingredient: item.ingredient,
        units: item.units
      }
    }
    blogPost.ingredients = items.map(it => convertToIngredients(it))
    setBlogPost({...blogPost})
  }

  const handleBlogPostFieldChange = (fieldName: string, event: any): void => {
    event.preventDefault();
    const value = event.target.value;
    if ([
        "title",
        "slug",
        "description",
        "thumbnail",
        "cooking_time"
    ].includes(fieldName)) {
      blogPost.recipe_lite[fieldName] = value
    } else if (["summary", "keywords"].includes(fieldName)) {
      blogPost[fieldName] = value
    } else {
      blogPost[fieldName] = value
    }
    setBlogPost({...blogPost})
  }
  const upload = () => {
    if (file) {
      auth.onAuthStateChanged(user => {
        if (user) {
          user.getIdTokenResult(false)
          .then(tokenResult => {
            new CmsApi(headerConfig(tokenResult.token))
            .uploadImage({
              file: file,
              file_extension: fileExtension
            })
            .then(result => {
              setShowProgress(false)
              if (result.data) {
                setUploadedImageUrl(result.data.url)
                showMessageBar({
                  message: result.data.url ? 'Image Uploaded' : result.data.error,
                  snack: enqueueSnackbar,
                  error: !!result.data.error
                });
              }
            }).catch(e => {
              console.log(e)
              showMessageBar({
                message: e.message,
                snack: enqueueSnackbar,
                error: true
              });
            })
          }).catch(e => console.log(e))
        }
      })
    }
  }
  const publish = () => {
    blogPost.recipe_lite.published = true
    handleSaveBlogPost();
  }
  const unpublish = () => {
    blogPost.recipe_lite.published = false
    handleSaveBlogPost();
  }
  const handleSaveBlogPost = (): void => {
    auth.onAuthStateChanged(user => {
      if (user) {
        user.getIdTokenResult(false)
        .then(tokenResult => {
          const _blogPost = {...blogPost}
          new CmsApi(headerConfig(tokenResult.token))
          .updatePost(_blogPost)
          .then(result => {
            setShowProgress(false)
            if (result.data) {
              showMessageBar({
                message: result.data.status ? 'Updated document' : result.data.error,
                snack: enqueueSnackbar,
                error: !!result.data.error
              });
            }
          }).catch(e =>{
            console.log(e)
            showMessageBar({
              message: e.message,
              snack: enqueueSnackbar,
              error: true
            });
          })
        }).catch(e => console.log(e))
      }
    })
  }

  const getBlogPost = (token: string) => {
    new CmsApi(headerConfig(token))
    .getPostById(props.id)
    .then(result => {
      setShowProgress(false)
      if (result.data) {
        setBlogPost(result.data)
        showMessageBar({
          message: "Received 1 document data",
          snack: enqueueSnackbar,
          error: false
        });
      }
    }).catch(e => {
      showMessageBar({
        message: e.message,
        snack: enqueueSnackbar,
        error: true
      });
    })
  }
  const getPublicationStatus = (): string => {
    return  blogPost?.recipe_lite?.published ? "Current status: Published" : "Current status: Draft"
  }

  const getVideoId = () => {
    if (blogPost?.source) {
      try {
        const tokens = blogPost.source.split("v=")
        return tokens[tokens.length - 1]
      } catch (e) {
        console.log(e)
      }
    }
  }

  const copyToClipboard = (value: string) => {
    if (typeof window !== 'undefined' && navigator.clipboard) {
      navigator.clipboard.writeText(value).then(
          () => {
            showMessageBar({
              message: "Link copied",
              snack: enqueueSnackbar,
              error: false
            });
          },
          () => {
            showMessageBar({
              message: "Failed to copy link :(",
              snack: enqueueSnackbar,
              error: true
            });
          }
      ).catch(e => console.log(e));
    }
  }

  useEffect(() => {
    setShowProgress(true)
    auth.onAuthStateChanged(user => {
      if (user) {
        user.getIdTokenResult(false)
        .then(tokenResult => {
          getBlogPost(tokenResult.token)
        }).catch(e => console.log(e))
      }
    })
  }, [])

  return (
    <Authenticated>
      {showProgress &&
        <Box
          justifyContent="center"
          alignContent="center"
          display="flex"
          sx={{
            mb: '2rem'
          }}>
          <CircularProgress color="primary" />
        </Box>
      }

      {
        blogPost &&
        <>
          <Box sx={{ width: '100%' }}>
            <Box sx={{ borderBottom: 1, borderColor: 'divider', mb: '50px' }}>
              <Tabs value={tabIndex} onChange={handleTabChange} aria-label="recipe-blog-post">
                <Tab label="Post" {...a11yProps(0)} />
                <Tab label="Recipe" {...a11yProps(1)} />
                <Tab label="Ingredients" {...a11yProps(2)} />
                <Tab label="Publish" {...a11yProps(3)} />
                <Tab label="Image Upload Area" {...a11yProps(4)} />
              </Tabs>
            </Box>
            <TabPanel value={tabIndex} index={0}>
              <Card>
                <CardHeader title={blogPost.recipe_lite?.title} titleTypographyProps={{ variant: 'h6' }} />
                <CardContent style={{padding: '0rem', marginLeft: '0rem'}}>
                  <Grid container spacing={5}>
                    <Grid item xs={12} >
                      <Grid container spacing={5} sx={{p: 4}}>
                        <Grid item xs={12} sm={12}>
                          <TextField
                              onChange={(e) => handleBlogPostFieldChange('title', e)}
                              fullWidth label='Title'
                              placeholder='Title'
                              value={blogPost?.recipe_lite?.title ? blogPost?.recipe_lite?.title : ''} />
                        </Grid>
                        <Grid item xs={12}>
                          <TextField
                              onChange={(e) => handleBlogPostFieldChange('keywords', e)}
                              fullWidth
                              label='Keywords'
                              placeholder='Comma-separated list of keywords'
                              value={blogPost.keywords ? blogPost.keywords : ''}
                              sx={{ '& .MuiOutlinedInput-root': { alignItems: 'baseline' } }}
                          />
                        </Grid>
                        <Grid item xs={12}>
                          <TextField
                              onChange={(e) => handleBlogPostFieldChange('thumbnail', e)}
                              fullWidth
                              label='Thumbnail'
                              placeholder='Thumbnail'
                              value={blogPost?.recipe_lite?.thumbnail ? blogPost?.recipe_lite?.thumbnail : ''}
                              sx={{ '& .MuiOutlinedInput-root': { alignItems: 'baseline' } }}
                          />
                        </Grid>
                        <Grid item xs={12}>
                          <TextField
                              onChange={(e) => handleBlogPostFieldChange('slug', e)}
                              fullWidth
                              label='slug'
                              placeholder='Slug'
                              value={blogPost?.recipe_lite?.slug ? blogPost.recipe_lite.slug : ''}
                              sx={{ '& .MuiOutlinedInput-root': { alignItems: 'baseline' } }}
                          />
                        </Grid>
                        <Grid item xs={12}>
                          <TextField
                              onChange={(e) => handleBlogPostFieldChange('description', e)}
                              fullWidth
                              multiline
                              minRows={3}
                              label='Description'
                              value={blogPost?.recipe_lite?.description ? blogPost?.recipe_lite?.description : ''}
                              placeholder='Brief description for SEO'
                              sx={{ '& .MuiOutlinedInput-root': { alignItems: 'baseline' } }}
                          />
                        </Grid>
                        <Grid item xs={12}>
                          <TextField
                              onChange={(e) => handleBlogPostFieldChange('summary', e)}
                              fullWidth
                              multiline
                              minRows={3}
                              label='Body'
                              value={blogPost.summary ? blogPost.summary : ''}
                              placeholder='Blog post body'
                              sx={{ '& .MuiOutlinedInput-root': { alignItems: 'baseline' } }}
                          />
                        </Grid>
                      </Grid>
                    </Grid>
                  </Grid>
                </CardContent>
                <CardActions>
                  <Button
                      onClick={handleSaveBlogPost}
                      size='large'
                      type='submit'
                      sx={{ mr: 2 }}
                      variant='contained'>
                    Save
                  </Button>
                </CardActions>
              </Card>
            </TabPanel>
            <TabPanel value={tabIndex} index={1}>
              <Card>
                <CardHeader title="Step-by-Step Recipe" titleTypographyProps={{ variant: 'h6' }} />

                <Divider sx={{ margin: 0 }} />
                <CardContent>
                  <Grid container spacing={5}>
                    <Grid item xs={12} sm={12}>
                      <TextField
                          onChange={(e) => handleBlogPostFieldChange('cooking_time', e)}
                          fullWidth
                          label={"Cooking Time"}
                          placeholder='Cooking Time (in minutes), e.g. 45'
                          value={blogPost?.recipe_lite?.cooking_time ? blogPost?.recipe_lite?.cooking_time  : ''}
                      />
                    </Grid>
                    <Grid item xs={12}>
                      <RecipeStepList
                          recipe={blogPost?.instructions}
                          onRecipeStepChange={onRecipeStepChange}
                      />
                    </Grid>
                  </Grid>
                </CardContent>

                <CardActions>
                  <Button
                      onClick={handleSaveBlogPost}
                      size='large'
                      type='submit'
                      sx={{ mr: 2 }}
                      variant='contained'>
                    Save
                  </Button>
                </CardActions>
              </Card>
            </TabPanel>
            <TabPanel value={tabIndex} index={2}>
              <Card>
                <CardHeader title="Ingredients" titleTypographyProps={{ variant: 'h6' }} />
                <Divider sx={{ margin: 0 }} />
                <CardContent>
                  <IngredientList
                      ingredients={blogPost?.ingredients}
                      onIngredientChange={onIngredientChange}
                  />
                </CardContent>
                <CardActions>
                  <Button
                      onClick={handleSaveBlogPost}
                      size='large'
                      type='submit'
                      sx={{ mr: 2 }}
                      variant='contained'>
                    Save
                  </Button>
                </CardActions>
              </Card>
            </TabPanel>
            <TabPanel value={tabIndex} index={3}>
              <Grid container>
                <Grid
                    item
                    xs={12}
                    sx={{ paddingTop: ['0 !important', '1.5rem !important'], paddingLeft: ['1.5rem !important', '0 !important'] }}
                >
                  <CardContent
                      sx={{
                        height: '100%',
                        display: 'flex',
                        textAlign: 'center',
                        alignItems: 'center',
                        justifyContent: 'center',
                        backgroundColor: 'action.hover',
                        padding: theme => `${theme.spacing(18, 5, 16)} !important`
                      }}
                  >
                    <Grid container>
                      <Grid item xs={12} sx={{mb: '2rem'}}>
                        <Button
                            onClick={publish}
                            variant="contained"
                            startIcon={<PublishIcon />}
                            className="overview-btn green">
                          Publish
                        </Button>
                      </Grid>
                      <Grid item xs={12}>
                        <Button
                            onClick={unpublish}
                            variant="contained"
                            startIcon={<DownloadIcon/>}
                            className="overview-btn red">
                          Unpublish
                        </Button>
                      </Grid>
                      <Grid item xs={12} sx={{mt: 8}}>
                        <Typography
                            variant='subtitle2'
                            fontSize={12} textAlign="left">
                          {getPublicationStatus()}
                        </Typography>
                      </Grid>
                      <Grid item xs={12} sx={{mt: 4}}>
                        <Typography
                            variant='subtitle2'
                            fontSize={10} textAlign="left">
                          An unpublished post may still show up on the website because of external caching (e.g. CloudFront). The
                          cache must be removed manually for such documents.
                        </Typography>
                      </Grid>
                    </Grid>

                  </CardContent>
                </Grid>
                {/*<Grid item xs={12}>*/}
                {/*  <div className="cms-section-divider"></div>*/}
                {/*  {blogPost.status === 'published' &&*/}
                {/*      <Card>*/}
                {/*        <CardHeader title="Preview Link" titleTypographyProps={{variant: 'h6'}}/>*/}
                {/*        <Divider sx={{margin: 0}}/>*/}
                {/*        <CardContent>*/}
                {/*          <Grid container spacing={5}>*/}
                {/*            <Grid item xs={12}>*/}
                {/*              <Typography variant='body2' sx={{fontWeight: 900, fontSize: '2rem'}}>*/}
                {/*                <a href={blogPost.post_url} target="_blank"*/}
                {/*                   style={{textDecoration: "none", color: "green"}}>*/}
                {/*                  {blogPost.post_url}*/}
                {/*                </a>*/}
                {/*              </Typography>*/}
                {/*            </Grid>*/}
                {/*          </Grid>*/}
                {/*        </CardContent>*/}
                {/*      </Card>*/}
                {/*  }*/}
                {/*</Grid>*/}
              </Grid>
            </TabPanel>
            <TabPanel value={tabIndex} index={4}>
              <Card>
                <CardHeader title="Upload Images" titleTypographyProps={{ variant: 'h2' }} />
                <CardContent style={{padding: '0rem', marginLeft: '0rem'}}>
                  <Grid container spacing={5} sx={{p: 4}}>

                    <Grid
                        item
                        xs={12}
                        sx={{mb: 4}}
                    >
                      <Typography
                          variant='subtitle2'
                          fontSize={14} textAlign="left">
                        Upload images for this recipe. You can take screenshots from the video or upload local images. After uploading,
                        make sure to copy and paste the URL into the correct field in the other tabs.
                      </Typography>
                    </Grid>
                          {
                              getVideoId() &&  <Grid item sm={12} className="iframe-container">
                                <iframe height="315"
                                        src={`https://www.youtube.com/embed/${getVideoId()}`}
                                        title="YouTube video player"
                                        frameBorder="0"
                                        allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
                                        allowFullScreen></iframe>
                              </Grid>
                          }
                          <Grid item xs={12} sx={{mt: 4}}>
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
                                      {'Supported formats: .png, .jpg, .jpeg. Max 10MB'}
                                    </Typography>
                                  </>
                              )}
                            </BoxUploadWrapper>
                          </Grid>
                          {
                              uploadedImageUrl && <Grid item xs={12} sx={{mt: 4, textAlign: 'center'}}>
                                <Button
                                    sx={{width: '48px', pl: '5px', pr: '0px', mr: '20px', height: '38px', display: 'inline-flex'}}
                                    onClick={() => copyToClipboard(uploadedImageUrl)}
                                    variant="contained"
                                    startIcon={<ContentCopyIcon />}
                                    className="overview-btn green"/>
                                <a href={uploadedImageUrl} target="_blank">
                                  <Typography variant='h6' sx={{ fontWeight: 600, mt: '0.5rem', ml: 2, display: 'inline-flex' }}>
                                    {uploadedImageUrl}
                                  </Typography>
                                </a>
                              </Grid>
                          }
                        </Grid>
                  <CardActions>
                    <Button
                        onClick={upload}
                        disabled={!(!!file)}
                        variant="contained"
                        startIcon={<CloudUploadIcon />}
                        className="overview-btn orange">
                      Upload
                    </Button>
                  </CardActions>
                </CardContent>
              </Card>
            </TabPanel>
          </Box>
        </>
      }
    </Authenticated>
  )
}

export default DocumentEditor;
