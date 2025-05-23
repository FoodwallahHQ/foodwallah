import React from 'react';
import Head from 'next/head';
import {RecipeFull} from "@/api";
import {getFormattedDate} from "@/utils/utils";

export interface StructuredDataProps {
  post: RecipeFull
}

function StructuredData(props: StructuredDataProps) {
  const data = {
    "@context": "https://schema.org/",
    "@type": "Recipe",
    "name": props.post?.recipe_lite?.title,
    "image": [
      props.post?.recipe_lite?.thumbnail,
    ],
    "author": {
      "@type": "Organization",
      "name": "Foodwallah"
    },
    "datePublished": getFormattedDate(props.post?.recipe_lite?.created_at),
    "description": props.post?.recipe_lite?.description,
    // "prepTime": "PT20M",
    // "cookTime": "PT30M",
    // "totalTime": "PT50M",
    "keywords": props.post?.keywords,
    // "recipeYield": "10",
    // "recipeCategory": "Dessert",
    // "recipeCuisine": "American",
    // "nutrition": {
    //   "@type": "NutritionInformation",
    //   "calories": "270 calories"
    // },
    "recipeIngredient": props.post?.ingredients?.map(it => it.ingredient),
    "recipeInstructions": props.post?.instructions?.map(it => {
      return (
          {
            "@type": "HowToStep",
            // "name": "Preheat",
            "text": it,
            // "url": "https://example.com/party-coffee-cake#step1",
            "image": it.images ? it.images[0] : ''
          }
      )
    }),


    // "aggregateRating": {
    //   "@type": "AggregateRating",
    //   "ratingValue": "5",
    //   "ratingCount": "18"
    // },
    // "video": {
    //   "@type": "VideoObject",
    //   "name": "How to make a Party Coffee Cake",
    //   "description": "This is how you make a Party Coffee Cake.",
    //   "thumbnailUrl": [
    //     "https://example.com/photos/1x1/photo.jpg",
    //     "https://example.com/photos/4x3/photo.jpg",
    //     "https://example.com/photos/16x9/photo.jpg"
    //   ],
    //   "contentUrl": "https://www.example.com/video123.mp4",
    //   "embedUrl": "https://www.example.com/videoplayer?video=123",
    //   "uploadDate": "2018-02-05T08:00:00+08:00",
    //   "duration": "PT1M33S",
    //   "interactionStatistic": {
    //     "@type": "InteractionCounter",
    //     "interactionType": { "@type": "WatchAction" },
    //     "userInteractionCount": 2347
    //   },
    //   "expires": "2019-02-05T08:00:00+08:00"
    // }
  }
  return (
      <Head>
        <title>{props.post?.recipe_lite?.title}</title>
          <script
              key="structured-data"
              type="application/ld+json"
              dangerouslySetInnerHTML={{ __html: JSON.stringify(data) }}
          />
      </Head>
  );
}
export default StructuredData;

// const _template = {
//   "@context": "https://schema.org/",
//   "@type": "Recipe",
//   "name": "Party Coffee Cake",
//   "image": [
//     "https://example.com/photos/1x1/photo.jpg",
//     "https://example.com/photos/4x3/photo.jpg",
//     "https://example.com/photos/16x9/photo.jpg"
//   ],
//   "author": {
//     "@type": "Person",
//     "name": "Mary Stone"
//   },
//   "datePublished": "2018-03-10",
//   "description": "This coffee cake is awesome and perfect for parties.",
//   "prepTime": "PT20M",
//   "cookTime": "PT30M",
//   "totalTime": "PT50M",
//   "keywords": "cake for a party, coffee",
//   "recipeYield": "10",
//   "recipeCategory": "Dessert",
//   "recipeCuisine": "American",
//   "nutrition": {
//     "@type": "NutritionInformation",
//     "calories": "270 calories"
//   },
//   "recipeIngredient": [
//     "2 cups of flour",
//     "3/4 cup white sugar",
//     "2 teaspoons baking powder",
//     "1/2 teaspoon salt",
//     "1/2 cup butter",
//     "2 eggs",
//     "3/4 cup milk"
//   ],
//   "recipeInstructions": [
//     {
//       "@type": "HowToStep",
//       "name": "Preheat",
//       "text": "Preheat the oven to 350 degrees F. Grease and flour a 9x9 inch pan.",
//       "url": "https://example.com/party-coffee-cake#step1",
//       "image": "https://example.com/photos/party-coffee-cake/step1.jpg"
//     },
//     {
//       "@type": "HowToStep",
//       "name": "Mix dry ingredients",
//       "text": "In a large bowl, combine flour, sugar, baking powder, and salt.",
//       "url": "https://example.com/party-coffee-cake#step2",
//       "image": "https://example.com/photos/party-coffee-cake/step2.jpg"
//     },
//     {
//       "@type": "HowToStep",
//       "name": "Add wet ingredients",
//       "text": "Mix in the butter, eggs, and milk.",
//       "url": "https://example.com/party-coffee-cake#step3",
//       "image": "https://example.com/photos/party-coffee-cake/step3.jpg"
//     },
//     {
//       "@type": "HowToStep",
//       "name": "Spread into pan",
//       "text": "Spread into the prepared pan.",
//       "url": "https://example.com/party-coffee-cake#step4",
//       "image": "https://example.com/photos/party-coffee-cake/step4.jpg"
//     },
//     {
//       "@type": "HowToStep",
//       "name": "Bake",
//       "text": "Bake for 30 to 35 minutes, or until firm.",
//       "url": "https://example.com/party-coffee-cake#step5",
//       "image": "https://example.com/photos/party-coffee-cake/step5.jpg"
//     },
//     {
//       "@type": "HowToStep",
//       "name": "Enjoy",
//       "text": "Allow to cool and enjoy.",
//       "url": "https://example.com/party-coffee-cake#step6",
//       "image": "https://example.com/photos/party-coffee-cake/step6.jpg"
//     }
//   ],
//   "aggregateRating": {
//     "@type": "AggregateRating",
//     "ratingValue": "5",
//     "ratingCount": "18"
//   },
//   "video": {
//     "@type": "VideoObject",
//     "name": "How to make a Party Coffee Cake",
//     "description": "This is how you make a Party Coffee Cake.",
//     "thumbnailUrl": [
//       "https://example.com/photos/1x1/photo.jpg",
//       "https://example.com/photos/4x3/photo.jpg",
//       "https://example.com/photos/16x9/photo.jpg"
//     ],
//     "contentUrl": "https://www.example.com/video123.mp4",
//     "embedUrl": "https://www.example.com/videoplayer?video=123",
//     "uploadDate": "2018-02-05T08:00:00+08:00",
//     "duration": "PT1M33S",
//     "interactionStatistic": {
//       "@type": "InteractionCounter",
//       "interactionType": { "@type": "WatchAction" },
//       "userInteractionCount": 2347
//     },
//     "expires": "2019-02-05T08:00:00+08:00"
//   }
// }
