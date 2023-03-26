import React, {FC} from 'react';
import Head from "next/head";
import RecipeLanding from "@/pages/Recipe";
import {RecipeApi, RecipeLite} from "@/api";


export async function getServerSideProps(_context) {
  const page = 0
  const limit = 9
  const response = await new RecipeApi().getMostRecentRecipes(page, limit)
  return {
    props: {
      posts: response.data.records
    }
  }
}

export interface IndexProps {
  posts: RecipeLite[]
}
const Index: FC<IndexProps> =  (props) => {
  return (
    <>
      <Head>
        <title>Kalico | Recipe from YouTube</title>
      </Head>
      <RecipeLanding posts={props.posts}/>
    </>
  );
}
export default Index
