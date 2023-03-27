import React, {FC} from 'react';
import {RecipeApi, RecipeFull} from "@/api";
import Head from "next/head";
import DetailView from "@/pages/Recipe/DetailView";

export async function getServerSideProps(context) {
  const response = await new RecipeApi().getFullRecipe(context.query.slug)
  return {
    props: {
      post: response.data
    }
  }
}

interface RecipeDetailProps {
  post: RecipeFull
}


const RecipeDetailIndex: FC<RecipeDetailProps> =  (props) => {
  return (
      <>
        <Head>
          <title>{props.post?.recipe_lite?.title}</title>
        </Head>
        <main>
          <DetailView post={props.post}/>
        </main>

      </>
  );
}
export default RecipeDetailIndex
