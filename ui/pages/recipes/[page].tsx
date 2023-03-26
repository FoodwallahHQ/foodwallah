import React, {FC, } from 'react';
import {RecipeApi, RecipeLite} from "@/api";
import Head from "next/head";
import ListView from "@/pages/Recipe/ListView";

export async function getServerSideProps(context) {
  const page = context.query.page - 1
  const limit = 9999
  const response = await new RecipeApi().getAllRecipes(page, limit)
  return {
    props: {
      posts: response.data.records
    }
  }
}

interface RecipeListProps {
  posts: RecipeLite[]
}

const RecipeList: FC<RecipeListProps> =  (props) => {
  return (
      <>
        <Head>
          <title>Kalico All Recipes</title>
        </Head>
        <main>
            <ListView posts={props.posts}/>
        </main>
      </>
  );
}

export default RecipeList
