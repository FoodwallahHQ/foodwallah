import React, {FC} from 'react';
import {Container} from "@mui/system";
import {Box} from "@mui/material";
import {urls} from "@/utils/paths";
import Link from "next/link";
import {RecipeLite} from "@/api";
import ListView from "@/pages/Recipe/ListView";

export interface MostRecentRecipesProps {
  posts: RecipeLite[]
}
const MostRecentRecipes: FC<MostRecentRecipesProps> =  (props) => {
  return (
  <Container className="top-recipes">
    <Box className="top-recipes-title">
      <h4>Most Recent Recipes</h4>
    </Box>
    <ListView posts={props.posts}/>
    <Box className="top-recipes-view-all">
      <Link href={urls.getAllRecipes}>
        <button
            className="view-all-button"
        >View All</button>
      </Link>
    </Box>
  </Container>

  );
}

export default MostRecentRecipes;
