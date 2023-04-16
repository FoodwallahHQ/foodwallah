import React, {FC, useEffect, useState} from "react";
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
import { CircularProgress} from "@mui/material";
import IngredientList from "@/@core/document/editor/IngredientList";
import {Authenticated} from "@/components/auth/Authenticated";
import {
  CmsApi, Ingredient,
  RecipeFull,
  RecipeStep
} from "@/api";
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
  const { enqueueSnackbar } = useSnackbar();

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

  // const copyToClipboard = (value: string) => {
  //   if (typeof window !== 'undefined' && navigator.clipboard) {
  //     navigator.clipboard.writeText(value).then(
  //         () => {
  //           showMessageBar({
  //             message: "Link copied",
  //             snack: enqueueSnackbar,
  //             error: false
  //           });
  //         },
  //         () => {
  //           showMessageBar({
  //             message: "Failed to copy link :(",
  //             snack: enqueueSnackbar,
  //             error: true
  //           });
  //         }
  //     ).catch(e => console.log(e));
  //   }
  // }

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
          </Box>
        </>
      }
    </Authenticated>
  )
}

export default DocumentEditor;
