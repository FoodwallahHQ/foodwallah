import React, {FC} from 'react';
import {Box, Divider, Grid} from "@mui/material";
import RestaurantMenuIcon from '@mui/icons-material/RestaurantMenu';
import SummaryComponent from "@/pages/Recipe/SummaryComponent";
import {Ingredient, RecipeFull} from "@/api";
import StructuredData from "@/components/StructuredData";
import {getFormattedDate} from "@/utils/utils";
import {AdditionalInfo} from "@/types/types";


export interface DetailViewProps {
  post: RecipeFull
}

const DetailView: FC<DetailViewProps> =  (props) => {
  const url = props.post?.source
  let videoId;
  if (url) {
    try {
      const tokens = url.split("v=")
      videoId = tokens[tokens.length - 1]
    } catch (e) {
      console.log(e)
    }
  }
  const buildIngredient = (value: Ingredient): string => {
    if (value?.amount && value?.units && value?.ingredient) {
      return `${value.amount} ${value.units} of ${value.ingredient}`
    }
    return value?.ingredient
  }
  const prevUrl = "/recipe/" + props.post?.prev?.slug
  const nextUrl = "/recipe/" + props.post?.next?.slug
  const additionalInfo: AdditionalInfo = JSON.parse(props.post?.recipe_lite?.additional_info ? props.post?.recipe_lite?.additional_info : "{}")
  return (
      <>
        <StructuredData post={props.post}/>
      <article className="recipe-container recipe-detail">
        <Grid container className="recipe-detail-top">
          <Grid item md={6} sm={12}>
            <Box className="primary-thumbnail">
              <img src={props.post?.recipe_lite?.thumbnail} alt={props.post?.recipe_lite?.title}/>
            </Box>
          </Grid>
          <Grid item md={6} sm={12} sx={{p: 3}}>
            <Box className="title">
              <h3 >{props.post?.recipe_lite?.title}</h3>
            </Box>
            <SummaryComponent steps={props.post?.recipe_lite?.num_steps}
                              ingredients={props.post?.recipe_lite?.num_ingredients}
                              prepTime={additionalInfo.prep_time}
                              cookingTime={additionalInfo.cooking_time}/>
          </Grid>
        </Grid>
        <Divider><RestaurantMenuIcon/></Divider>
        {videoId &&
            <Grid container>
              <Grid item sm={12} className="iframe-container">
                <iframe height="315"
                    // src={props.post?.source}
                    // src="https://www.youtube.com/watch?v=PVycYj_PKgk"
                        src={`https://www.youtube.com/embed/${videoId}`}
                        title="YouTube video player"
                        frameBorder="0"
                        allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
                        allowFullScreen></iframe>
              </Grid>
            </Grid>
        }

        <Box className="recipe-detail-bottom">
          <Box>
            {
              props.post?.summary?.split("\n")
              .filter(it => it.trim())
              .map((text, index) => <div className="recipe-body" ><span key={index}>{text}</span></div>)
            }
          </Box>
          <Box className="ingredients">
            <h3>Ingredients</h3>
            <ul>
              {
                props.post?.ingredients?.map((it, index) => {
                  return <li key={"ingredient-" + index}><span>{buildIngredient(it)}</span></li>
                })
              }
            </ul>
          </Box>
          <Box className="instructions">
            <h3>Steps</h3>
            <ul>
              {
                props.post?.instructions?.map((it, index) => {
                  return (
                      <li key={"step-" + index}>
                        {it.images && it.images.filter(it => it.trim())
                        .map((imageUrl, imageIndex) => {
                          return (
                              <Box className="recipe-image" key={index + "-" + imageIndex}>
                              <img src={imageUrl} alt={it.text}/>
                            </Box>
                          )
                        })}
                        <span>{it.text}</span>
                    </li>
                  )
                })
              }
            </ul>
          </Box>
          {/*{props.post?.source &&*/}
          {/*    <Box className="citation">*/}
          {/*      <p>Disclaimer: This recipe was written using automated means. Please let us know if you have comments or concerns.</p>*/}
          {/*      {!videoId && <p>Source: {props.post?.source}</p>}*/}
          {/*    </Box>*/}
          {/*}*/}
        </Box>

        {(props.post?.prev || props.post?.next) &&
            <Box className="other-recent-recipes">
              <h3>Recent Recipes</h3>
              <Grid container className="grid-items prev-next" spacing={6}>
                {props.post?.prev &&
                    <Grid item sm={12}>
                      <p>Previous</p>
                      <article
                          className="grid-item-card"
                          aria-label={props.post?.prev?.title}>
                        <a href={prevUrl} className="alignnone" aria-hidden="true">
                          <img width="600" height="850"
                               src={props.post?.prev?.thumbnail}
                               alt={props.post?.prev?.title}/>
                        </a>
                        <Box className="entry-header">
                          <span className="entry-meta">
                            <time
                                className="entry-time">{getFormattedDate(props.post?.prev?.created_at)}</time>
                          </span>
                          <h2 className="entry-title"><a
                              href={prevUrl}>{props.post?.prev?.title}</a></h2>
                        </Box>
                      </article>
                    </Grid>
                }
                {props.post?.next &&
                    <Grid item sm={12}>
                      <p>Next</p>
                      <article
                          className="grid-item-card"
                          aria-label={props.post?.next?.title}>
                        <a href={nextUrl} className="alignnone" aria-hidden="true">
                          <img width="600" height="850"
                               src={props.post?.next?.thumbnail}
                               alt={props.post?.next?.title}/>
                        </a>
                        <Box className="entry-header" sx={{maxWidth: '600px'}}>
                          <span className="entry-meta">
                            <time
                                className="entry-time">{getFormattedDate(props.post?.next?.created_at)}</time>
                          </span>
                          <h2 className="entry-title"><a
                              href={nextUrl}>{props.post?.next?.title}</a></h2>
                        </Box>
                      </article>
                    </Grid>
                }
              </Grid>
            </Box>
        }
      </article>

      </>
  );
}

export default DetailView;
