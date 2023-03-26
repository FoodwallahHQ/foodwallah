import React, {FC} from 'react';
import {Box, Grid} from "@mui/material";
import Link from "next/link";
import SummaryComponent from "@/pages/Recipe/SummaryComponent";
import {RecipeLite} from "@/api";
import {getFormattedDate, truncatedDescription} from "@/utils/utils";


export interface ListViewProps {
  posts: RecipeLite[]
}

const ListView: FC<ListViewProps> =  (props) => {
  return (
      <main className="recipe-container">
        <Grid container className="list-items" spacing={3}>
          {
            props.posts?.map((it, index) => {
              const url = '/recipe/' + it.slug
              return (
                  <Grid container spacing={6} className="list-item-card" key={index}>
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
                          <p>{truncatedDescription(it.summary)}</p>
                        </Box>
                        <SummaryComponent steps={it.num_steps}
                                          ingredients={it.num_ingredients}
                                          time={it.cooking_time}/>
                        <Box className="list-view-read-more">
                          <Link href={url}>
                            <button
                                className="read-more-button"
                            >View Recipe</button>
                          </Link>
                        </Box>
                      </Box>

                    </Grid>
                  </Grid>

              )
            })
          }
        </Grid>

      </main>
  );
}

export default ListView;
