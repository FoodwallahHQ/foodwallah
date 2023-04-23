import React, {FC} from 'react';
import {Box, Divider, Grid} from "@mui/material";
import Link from "next/link";
import SummaryComponent from "@/pages/Recipe/SummaryComponent";
import {RecipeLite} from "@/api";
import {getFormattedDate, truncatedDescription} from "@/utils/utils";
import { AdditionalInfo } from '@/types/types';


export interface ListViewProps {
  posts: RecipeLite[]
}



const ListView: FC<ListViewProps> =  (props) => {
  return (
      <main className="recipe-container">
        <Box className="list-items">
          {
            props.posts?.map((it, index) => {
              const url = '/recipe/' + it.slug
              const additionalInfo: AdditionalInfo = JSON.parse(it.additional_info ? it.additional_info : "{}")
              return (
                  <Grid container className="list-item-card" key={index} spacing={2}>
                    <Grid item md={6} sm={12} className="center">
                      <Box>
                        <a href={url} aria-hidden="true">
                          <img src={it.thumbnail} alt={it.title}/>
                        </a>
                      </Box>
                    </Grid>
                    <Grid item md={6} sm={12} className="middle">
                      <Box>
                        <Box className="entry-header">
                          <p className="entry-meta">
                            <time className="entry-time">{getFormattedDate(it.created_at)}</time>
                          </p>
                          <h3 className="entry-title"><a
                              href={url}>{it.title}</a></h3>
                          <p>{truncatedDescription(it.description)}</p>
                        </Box>
                        <SummaryComponent steps={it.num_steps}
                                          ingredients={it.num_ingredients}
                                          prepTime={additionalInfo.prep_time}
                                          cookingTime={additionalInfo.cooking_time}/>
                        <Box className="list-view-read-more">
                          <Link href={url}>
                            <button
                                className="read-more-button"
                            >View Recipe</button>
                          </Link>
                        </Box>
                      </Box>
                      {index < props.posts.length - 1 && <Divider>{'<>'}</Divider>}
                    </Grid>
                  </Grid>
              )
            })
          }
        </Box>

      </main>
  );
}

export default ListView;
