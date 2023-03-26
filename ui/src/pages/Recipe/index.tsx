import React, {FC} from 'react';
import Hero from "@/pages/Recipe/Hero";
import Search from "@/pages/Recipe/Search";
import MostRecentRecipes from "@/pages/Recipe/MostRecentRecipes";
import {IndexProps} from "../../../pages";


const RecipeLanding: FC<IndexProps> =  (props) => {
  return (
      <>
        <main>
          <Hero/>
          <Search/>
          {props.posts.length > 0 && <MostRecentRecipes posts={props.posts}/>}
        </main>
      </>
  );
}

export default RecipeLanding;
