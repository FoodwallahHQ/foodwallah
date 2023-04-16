import React, {FC, useEffect, useRef, useState} from "react";
import Grid from "@mui/material/Grid";
import Card from "@mui/material/Card";
import CardHeader from "@mui/material/CardHeader";
import Divider from "@mui/material/Divider";
import CardContent from "@mui/material/CardContent";
import Typography from "@mui/material/Typography";
import TextField from "@mui/material/TextField";
import CardActions from "@mui/material/CardActions";
import Button from "@mui/material/Button";
import Box from "@mui/material/Box";
import {Accordion, AccordionDetails, AccordionSummary, CircularProgress} from "@mui/material";
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import ImageGallery from 'react-image-gallery';
import IngredientList from "@/@core/document/editor/IngredientList";
import {Authenticated} from "@/components/auth/Authenticated";
import ContentCopyIcon from '@mui/icons-material/ContentCopy';
import {
  BlogPost,
  CmsApi,
  CmsVideoContent, Ingredient,
  IngredientContent,
  RecipeContent,
  RecipeStep
} from "@/api";
import {auth} from "@/utils/firebase-setup";
import {headerConfig} from "@/api/headerConfig";
import {showMessageBar} from "@/utils/message";
import {useSnackbar} from "notistack";
import Overview from "@/@core/document/editor/Overview";
import RecipeStepList from "@/@core/document/editor/RecipeStepList";
import {IngredientListData, RecipeStepListData} from "@/@core/utils/types";
import Tabs from "@mui/material/Tabs";
import Tab from "@mui/material/Tab";
import PublishIcon from "@mui/icons-material/Publish";
import DownloadIcon from "@mui/icons-material/Download";
import GifGenerator from "@/@core/document/gif-generator/GifGenerator";

interface TabPanelProps {
  children?: React.ReactNode;
  index: number;
  value: number;
}

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
      docId: context.query.id
    }
  }
}

export interface DocumentEditorProps {
  docId: number
}

interface SampledImage {
  original: string,
  thumbnail: string
}

const DocumentEditor: FC<DocumentEditorProps> = (props) => {
  const imageGalleryRef = useRef(null);
  const [currImageIndex, setCurrImageIndex] = useState<number>(0);
  const [currImageUrl, setCurrImageUrl] = useState<string>("");
  const [showProgress, setShowProgress] = useState<boolean>(false);
  const [blogPost, setBlogPost] = useState<BlogPost>();
  const [videoContent, setVideoContent] = useState<CmsVideoContent>()
  const [recipeContent, setRecipeContent] = useState<RecipeContent>()
  const [ingredientContent, setIngredientContent] = useState<IngredientContent>()
  const [sampledImages, setSampledImages] = useState<SampledImage[]>([])
  const [tabIndex, setTabIndex] = React.useState(1);
  const { enqueueSnackbar } = useSnackbar();

  const handleTabChange = (_event: React.SyntheticEvent, newValue: number) => {
    setTabIndex(newValue);
  };

  const onVideoContentChange = (fieldName: string, event: any): void => {
    event.preventDefault()
    const value = event.target.value;
    videoContent[fieldName] = value
    setVideoContent({...videoContent})
  }

  const onRecipeStepChange = (items: RecipeStepListData[]): void => {
    const convertToRecipeStep = (item: RecipeStepListData): RecipeStep => {
      return {
        step_number: item.step_number,
        image_url: item.image_url,
        description: item.description,
      }
    }
    recipeContent.recipe_steps = items.map(it => convertToRecipeStep(it))
    setRecipeContent({...recipeContent})
  }

  const onIngredientChange = (items: IngredientListData[]): void => {
    const convertToIngredients = (item: IngredientListData): Ingredient => {
      return {
        sort_order: item.id,
        amount: item.qt,
        description: item.description,
        units: item.units
      }
    }
    ingredientContent.ingredients = items.map(it => convertToIngredients(it))
    setIngredientContent({...ingredientContent})
  }

  const onPrimaryThumbnailChange = (event: any): void => {
    event.preventDefault()
    recipeContent.primary_thumbnail = event.target.value
    setRecipeContent({...recipeContent})
  }

  const onImageSlide = (currentIndex: string) => {
    onImageClick(currentIndex)
  }

  const onImageClick = (currentIndex: string) => {
    const index: number = (currentIndex as unknown as number);
    setCurrImageIndex(index);
    setCurrImageUrl(sampledImages[index].original)
  }

  const handleBlogPostFieldChange = (fieldName: string, event: any): void => {
    event.preventDefault();
    const value = event.target.value;
    if (fieldName === 'cuisine') {
      blogPost.recipe.cuisine = value
    } else if (fieldName === 'prep_time') {
      blogPost.recipe.prep_time = value
    } else {
      blogPost[fieldName] = value
    }
    setBlogPost({...blogPost})
  }
  const publish = () => {
    auth.onAuthStateChanged(user => {
      if (user) {
        user.getIdTokenResult(false)
        .then(tokenResult => {
          new CmsApi(headerConfig(tokenResult.token))
          .publishBlogPost({slug: blogPost.slug})
          .then(result => {
            setShowProgress(false)
            if (result.data) {
              showMessageBar({
                message: result.data.status ? "Document publish state has been update" : result.data.error,
                snack: enqueueSnackbar,
                error: !!result.data.error
              });
            }
          }).catch(e => console.log(e))
        }).catch(e => console.log(e))
      }
    })
  }
  const unpublish = () => {
    auth.onAuthStateChanged(user => {
      if (user) {
        user.getIdTokenResult(false)
        .then(tokenResult => {
          new CmsApi(headerConfig(tokenResult.token))
          .unPublishBlogPost({slug: blogPost.slug})
          .then(result => {
            setShowProgress(false)
            if (result.data) {
              showMessageBar({
                message: result.data.status ? "Document publish state has been update" : result.data.error,
                snack: enqueueSnackbar,
                error: !!result.data.error
              });
            }
          }).catch(e => console.log(e))
        }).catch(e => console.log(e))
      }
    })
  }
  const handleSaveBlogPost = (): void => {
    auth.onAuthStateChanged(user => {
      if (user) {
        user.getIdTokenResult(false)
        .then(tokenResult => {
          const _blogPost = {...blogPost}
          _blogPost.video_metadata = null
          new CmsApi(headerConfig(tokenResult.token))
          .upsertBlogPost(_blogPost)
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
  const handleSaveVideoContent = (): void => {
    auth.onAuthStateChanged(user => {
      if (user) {
        user.getIdTokenResult(false)
        .then(tokenResult => {
          new CmsApi(headerConfig(tokenResult.token))
          .updateCmsVideoContent(videoContent)
          .then(result => {
            setShowProgress(false)
            if (result.data) {
              showMessageBar({
                message: 'Updated document',
                snack: enqueueSnackbar,
                error: false
              });
            }
          }).catch(e =>{
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
  const handleSaveRecipeSteps = (): void => {
    auth.onAuthStateChanged(user => {
      if (user) {
        user.getIdTokenResult(false)
        .then(tokenResult => {
          new CmsApi(headerConfig(tokenResult.token))
          .upsertRecipeStep(recipeContent)
          .then(result => {
            if (result.data) {
              showMessageBar({
                message: 'Updated document',
                snack: enqueueSnackbar,
                error: false
              });
            }
          }).catch(e =>{
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
  const handleSaveIngredients = (): void => {
    auth.onAuthStateChanged(user => {
      if (user) {
        user.getIdTokenResult(false)
        .then(tokenResult => {
          new CmsApi(headerConfig(tokenResult.token))
          .upsertIngredients(ingredientContent)
          .then(result => {
            if (result.data) {
              showMessageBar({
                message: 'Updated document',
                snack: enqueueSnackbar,
                error: false
              });
            }
          }).catch(e =>{
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
  const getVideoContent = (docId: number, token: string) => {
    new CmsApi(headerConfig(token))
    .getCmsVideoContent(docId)
    .then(result => {
      if (result.data) {
        setVideoContent(result.data)
      }
    }).catch(e => {
      showMessageBar({
        message: e.message,
        snack: enqueueSnackbar,
        error: true
      });
    })
  }
  const getRecipeContent = (docId: number, token: string) => {
    new CmsApi(headerConfig(token))
    .getRecipeContent(docId)
    .then(result => {
      if (result.data) {
        setRecipeContent(result.data)
      }
    }).catch(e => {
      showMessageBar({
        message: e.message,
        snack: enqueueSnackbar,
        error: true
      });
    })
  }
  const getIngredientList = (docId: number, token: string) => {
    new CmsApi(headerConfig(token))
    .getIngredients(docId)
    .then(result => {
      if (result.data) {
        setIngredientContent(result.data)
      }
    }).catch(e => {
      showMessageBar({
        message: e.message,
        snack: enqueueSnackbar,
        error: true
      });
    })
  }
  const getSampledImages = (docId: number, token: string) => {
    new CmsApi(headerConfig(token))
    .getSampledImages(docId)
    .then(result => {
      if (result.data) {
        setSampledImages(result.data.map(url =>  {
          return {original: url, thumbnail: url}
        }))
        setCurrImageUrl(result.data[0])
      }
    }).catch(e => {
      showMessageBar({
        message: e.message,
        snack: enqueueSnackbar,
        error: true
      });
    })
  }
  const getBlogPost = (token: string) => {
    new CmsApi(headerConfig(token))
    .getPostById(props.docId)
    .then(result => {
      setShowProgress(false)
      if (result.data) {
        setBlogPost(result.data)
        getVideoContent(result.data.doc_id, token)
        getRecipeContent(result.data.doc_id, token)
        getIngredientList(result.data.doc_id, token)
        getSampledImages(result.data.doc_id, token)
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
    return "Current status: " + blogPost.status
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

  const imageIndex = currImageIndex + 1 + "/" + sampledImages.length;

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
              <Tabs value={tabIndex} onChange={handleTabChange} aria-label="basic tabs example">
                <Tab label="Overview" {...a11yProps(0)} />
                <Tab label="Title and Summary" {...a11yProps(1)} />
                <Tab label="Video Content" {...a11yProps(2)} />
                <Tab label="Recipe Steps" {...a11yProps(3)} />
                <Tab label="GIF Generator" {...a11yProps(4)} />
                <Tab label="Ingredients" {...a11yProps(5)} />
                <Tab label="Publish" {...a11yProps(6)} />
              </Tabs>
            </Box>
            <TabPanel value={tabIndex} index={0}>
              <Overview/>
            </TabPanel>
            <TabPanel value={tabIndex} index={1}>
              <Card>
                <CardHeader title={blogPost.title} titleTypographyProps={{ variant: 'h6' }} />
                <CardContent style={{padding: '0rem', marginLeft: '0rem'}}>
                  <Grid container spacing={5}>
                    <Grid item xs={12} >
                      <Grid container spacing={5} sx={{p: 4}}>
                            <Grid item xs={12} sm={12}>
                              <TextField
                                  onChange={(e) => handleBlogPostFieldChange('title', e)}
                                  fullWidth label='Title'
                                  placeholder='Title'
                                  value={blogPost.title ? blogPost.title : ''} />
                            </Grid>
                            <Grid item xs={12}>
                              <TextField
                                  onChange={(e) => handleBlogPostFieldChange('tags', e)}
                                  fullWidth
                                  label='Tags'
                                  placeholder='Semi-colon separated list of tags'
                                  value={blogPost.tags ? blogPost.tags : ''}
                                  sx={{ '& .MuiOutlinedInput-root': { alignItems: 'baseline' } }}
                              />
                            </Grid>
                            <Grid item xs={12}>
                              <TextField
                                  onChange={(e) => handleBlogPostFieldChange('summary', e)}
                                  fullWidth
                                  multiline
                                  minRows={3}
                                  label='Summary'
                                  value={blogPost.summary ? blogPost.summary : ''}
                                  placeholder='Brief description of the post for SEO'
                                  sx={{ '& .MuiOutlinedInput-root': { alignItems: 'baseline' } }}
                              />
                            </Grid>
                            <Grid item xs={12} sm={6}>
                              <TextField
                                  onChange={(e) => handleBlogPostFieldChange('author', e)}
                                  value={blogPost.author ? blogPost.author : ''}
                                  fullWidth
                                  label='Author'
                                  placeholder='Author' />
                            </Grid>
                            <Grid item xs={12} sm={6}>
                              <TextField
                                  onChange={(e) => handleBlogPostFieldChange('cuisine', e)}
                                  value={blogPost?.recipe?.cuisine ? blogPost?.recipe?.cuisine : ''}
                                  fullWidth
                                  label='Cuisine'
                                  placeholder='Cuisine' />
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
            <TabPanel value={tabIndex} index={2}>
              <Card>
                <Divider sx={{ margin: 0 }} />
                <CardContent>
                  <Grid container spacing={5}>
                    <Grid item xs={12}>
                      <Typography variant='body2' sx={{ fontWeight: 600 }}>
                        Video URL
                      </Typography>
                    </Grid>
                    <Grid item xs={12} sm={6}>
                      <TextField
                          disabled
                          value={videoContent?.url ? videoContent?.url : ''}
                          fullWidth
                          placeholder='Video URL' />
                    </Grid>
                    <Grid item xs={12}>
                      <Typography variant='h5' sx={{ fontWeight: 600, mt: '2rem', mb: '0rem', pl: '0rem' }}>
                        Caption
                      </Typography>
                      <Divider sx={{ margin: 0 }} />
                      <Box sx={{mt: '20px'}}>
                      <TextField
                          onChange={(e) => onVideoContentChange('caption', e)}
                          fullWidth
                          multiline
                          minRows={3}
                          label='Caption'
                          value={videoContent?.caption ? videoContent?.caption : ''}
                          placeholder='Caption to be displayed in the blog under the video. Keep it descriptive.'
                          sx={{ '& .MuiOutlinedInput-root': { alignItems: 'baseline' } }}
                      />
                      </Box>
                    </Grid>
                    <Grid item xs={12} >
                      <Typography variant='h5' sx={{ fontWeight: 600, mt: '2rem', mb: '0rem', pl: '0rem' }}>
                        Scraped Titled + Description
                      </Typography>
                      <Divider sx={{ margin: 0 }} />
                      <Box sx={{mt: '20px'}}>
                        <div dangerouslySetInnerHTML={{__html: videoContent?.description }}></div>
                      </Box>
                    </Grid>
                    <Grid item xs={12} >
                      <Typography variant='h5' sx={{ fontWeight: 600, mt: '2rem', mb: '0rem', pl: '0rem' }}>
                        Transcript
                      </Typography>
                      <Divider sx={{ margin: 0 }} />
                      <Box sx={{mt: '20px'}}>
                        <div dangerouslySetInnerHTML={{__html: videoContent?.raw_transcript }}></div>
                      </Box>
                    </Grid>
                    <Grid item xs={12} style={{paddingLeft: '0rem', marginLeft: '0rem'}}>
                      <Accordion style={{marginRight: '-2rem', borderRadius: '0px'}}>
                        <AccordionSummary
                            expandIcon={<ExpandMoreIcon />}
                            aria-controls="panel1a-content"
                            id="panel1a-header"
                            sx={{pr: '2rem', backgroundColor: '#f2f2f27d'}}
                        >
                          <Typography variant='body2' sx={{ fontWeight: 600, mt: '2rem', mb: '1rem', pl: '2rem' }}>
                            On-Screen Text from Image Recognition - Click to Expand
                          </Typography>
                        </AccordionSummary>
                        <AccordionDetails sx={{p: 0}}>
                          <Box sx={{mt: '20px', p: '20px'}}>
                            <div dangerouslySetInnerHTML={{__html: videoContent?.on_screen_text }}></div>
                          </Box>
                        </AccordionDetails>
                      </Accordion>
                    </Grid>

                    {/*<Grid item xs={12} >*/}
                    {/*  <Typography variant='h5' sx={{ fontWeight: 600, mt: '2rem', mb: '0rem', pl: '0rem' }}>*/}
                    {/*    On-Screen Text from Image Recognition*/}
                    {/*  </Typography>*/}
                    {/*  <Divider sx={{ margin: 0 }} />*/}
                    {/*  <Box sx={{mt: '20px'}}>*/}
                    {/*    <div dangerouslySetInnerHTML={{__html: videoContent?.on_screen_text }}></div>*/}
                    {/*  </Box>*/}
                    {/*</Grid>*/}
                  </Grid>
                </CardContent>
                <CardActions>
                  <Button
                      onClick={handleSaveVideoContent}
                      size='large'
                      type='submit' sx={{ mr: 2 }}
                      variant='contained'>
                    Save
                  </Button>
                </CardActions>
              </Card>
            </TabPanel>
            <TabPanel value={tabIndex} index={3}>
              <Card>
                <CardHeader title="Step-by-Step Recipe" titleTypographyProps={{ variant: 'h6' }} />
                {sampledImages.length > 0 &&
                    <Box sx={{ml: 4, mr: 4, mb: 4}}>
                      <Typography variant='subtitle2' fontSize={12}>
                        Sample rate = {recipeContent?.sampling_rate} Total number of images = {sampledImages.length}
                      </Typography>
                    </Box>
                }
                <Divider sx={{ margin: 0 }} />
                <CardContent>
                  {sampledImages.length > 0 &&
                      <ImageGallery
                          onClick={onImageClick}
                          showFullscreenButton={false}
                          items={sampledImages}
                          showIndex={false}
                          ref={imageGalleryRef}
                          onSlide={onImageSlide}
                          style={{overflowX: "scroll"}}/>
                  }
                  <Grid container spacing={5}>
                    {sampledImages.length > 0 &&
                        <>
                          <Grid item xs={12}>
                            <Typography variant='body2' sx={{ fontWeight: 600 }}>
                              Image URLs
                            </Typography>
                          </Grid>
                          <Grid item xs={12} sm={12}>
                            <Typography variant='h6' sx={{ fontWeight: 400, mt: '0.5rem', mb: '0.5rem', pl: '0.5rem', fontSize: '14px' }}>
                              {imageIndex}
                            </Typography>

                            <Typography variant='h5' sx={{ fontWeight: 600, mt: '0.5rem', mb: '0.5rem', pl: '0.5rem' }}>
                              <Button
                                  sx={{width: '48px', pl: '5px', pr: '0px', mr: '20px', height: '38px'}}
                                  onClick={() => copyToClipboard(currImageUrl)}
                                  variant="contained"
                                  startIcon={<ContentCopyIcon />}
                                  className="overview-btn green"/>
                              {currImageUrl}
                            </Typography>
                          </Grid>
                          <Grid item xs={12} sm={12}>
                            <TextField
                                onChange={onPrimaryThumbnailChange}
                                fullWidth
                                placeholder='Primary Thumbnail'
                                value={recipeContent?.primary_thumbnail ? recipeContent?.primary_thumbnail : ''}
                            />
                          </Grid>
                          <Grid item xs={12} sm={12}>
                            <TextField
                                onChange={(e) => handleBlogPostFieldChange('prep_time', e)}
                                fullWidth
                                placeholder='Prep Time, e.g. 45 minutes'
                                value={blogPost?.recipe?.prep_time ? blogPost?.recipe?.prep_time : ''}
                            />
                          </Grid>
                        </>
                    }
                    <Grid item xs={12}>
                      <RecipeStepList
                          recipe={recipeContent?.recipe_steps}
                          onRecipeStepChange={onRecipeStepChange}
                      />
                    </Grid>
                  </Grid>
                </CardContent>

                <CardActions>
                  <Button
                      onClick={handleSaveRecipeSteps}
                      size='large'
                      type='submit'
                      sx={{ mr: 2 }}
                      variant='contained'>
                    Save
                  </Button>
                </CardActions>
              </Card>
            </TabPanel>
            <TabPanel value={tabIndex} index={4}>
              <GifGenerator
                  onCopyToClipboard={copyToClipboard}
                  docId={blogPost?.doc_id}
                  video_url={blogPost?.video_metadata?.cdn_url}/>
            </TabPanel>
            <TabPanel value={tabIndex} index={5}>
              <Card>
                <CardHeader title="Ingredient List" titleTypographyProps={{ variant: 'h6' }} />
                <Divider sx={{ margin: 0 }} />
                <CardContent>
                  <IngredientList
                      ingredients={ingredientContent?.ingredients}
                      onIngredientChange={onIngredientChange}
                  />
                </CardContent>
                <CardActions>
                  <Button
                      onClick={handleSaveIngredients}
                      size='large'
                      type='submit'
                      sx={{ mr: 2 }}
                      variant='contained'>
                    Save
                  </Button>
                </CardActions>
              </Card>
            </TabPanel>
            <TabPanel value={tabIndex} index={6}>
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
                <Grid item xs={12}>
                  <div className="cms-section-divider"></div>
                  {blogPost.status === 'published' &&
                      <Card>
                        <CardHeader title="Preview Link" titleTypographyProps={{variant: 'h6'}}/>
                        <Divider sx={{margin: 0}}/>
                        <CardContent>
                          <Grid container spacing={5}>
                            <Grid item xs={12}>
                              <Typography variant='body2' sx={{fontWeight: 900, fontSize: '2rem'}}>
                                <a href={blogPost.post_url} target="_blank"
                                   style={{textDecoration: "none", color: "green"}}>
                                  {blogPost.post_url}
                                </a>
                              </Typography>
                            </Grid>
                          </Grid>
                        </CardContent>
                      </Card>
                  }
                </Grid>
              </Grid>
            </TabPanel>
          </Box>
        </>
      }
    </Authenticated>
  )
}

export default DocumentEditor;
