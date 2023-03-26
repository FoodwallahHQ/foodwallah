import { Html, Head, Main, NextScript } from 'next/document';
import Script from "next/script";
import {SITE_IMAGE_URL} from "../src/utils/constants";

export default function Document() {
  const title = 'Kalico AI | Audio and Video Repurposing for Content Creators'
  const description = 'Kalico is an AI assistant that helps you instantly create captivating articles from your videos and podcasts.'
  const siteImage = SITE_IMAGE_URL
  const url = 'https://kalico.ai'
      return (
      <Html lang="en">
        <Head>
          <link rel="apple-touch-icon" sizes="180x180" href="/favicon.ico" />
          <link rel='shortcut icon' href='/favicon.ico' />
          {/*<link rel="manifest" href="/manifest.json" />*/}
          <meta
            name="description"
            content="Kalico"
          />
          <meta name="description" content={description}/>

          {/*<!-- Google / Search Engine Tags -->*/}
          <meta itemProp="name" content={title}/>
          <meta itemProp="description" content={description}/>
          <meta itemProp="image" content={siteImage}/>

          {/*<!-- Facebook Meta Tags -->*/}
          <meta property="og:title" content={title}/>
          <meta property="og:description" content={description}/>
          <meta property="og:image" content={siteImage}/>
          <meta property="og:url" content={url}/>
          <meta property="og:type" content="website"/>

          {/*<!-- Twitter Meta Tags -->*/}
          <meta name="twitter:title" content={title}/>
          <meta name="twitter:description" content={description}/>
          <meta name="twitter:image" content={siteImage}/>
          <meta name="twitter:card" content="summary_large_image"/>
          
          
          
          <meta name="theme-color" content="#fff" />
          <meta name="apple-mobile-web-app-capable" content="yes"/>
          <meta name="mobile-web-app-capable" content="yes"/>

          <link rel="preconnect" href="https://fonts.googleapis.com"/>
          <link rel="preconnect" href="https://fonts.gstatic.com" crossOrigin={"true"}/>
          <link href="https://fonts.googleapis.com/css2?family=Grand+Hotel&family=Karla:wght@300;400;600;800&display=swap" rel="stylesheet"/>
          <link href="https://fonts.googleapis.com/css2?family=Grand+Hotel&family=Karla:wght@300;400;600;800&display=swap" rel="stylesheet"/>





          <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon" />
          {/*Fontawesome CSS*/}
          <link rel="stylesheet" href="/assets/css/fontawesome.min.css" />
          {/* ElegentIcon CSS */}
          <link rel="stylesheet" href="/assets/css/elegant-icons.min.css" />
          {/* Animate CSS */}
          <link rel="stylesheet" href="/assets/css/animate.min.css" />
          {/* Bootstrap CSS */}
          <link rel="stylesheet" href="/assets/css/bootstrap.min.css" />
          {/* SwiperJs CSS */}
          <link rel="stylesheet" href="/assets/css/swiper-bundle.min.css" />
          {/* Fancybox CSS */}
          <link rel="stylesheet" href="/assets/css/jquery.fancybox.min.css" />
          {/* Main CSS */}
          <link rel="stylesheet" href="/assets/css/style.css" />
          {/* Responsive CSS */}
          <link rel="stylesheet" href="/assets/css/responsive.css" />
          <link rel="stylesheet" href="/assets/css/nice-select.css" />
          <link rel="stylesheet" href="/assets/css/slick.css" />
          <link rel="stylesheet" href="/assets/css/slick-theme.css" />


          <script src="/assets/js/plugin/jquery-3.5.0.min.js"/>
          <script src="/assets/js/plugin/popper.min.js"/>
          <script src="/assets/js/plugin/bootstrap.min.js"/>
          {/* TweenMax */}
          <script src="/assets/js/plugin/TweenMax.min.js"/>
          {/* ScrollMagic */}
          <script src="/assets/js/plugin/ScrollMagic.js"/>
          {/* animation.gsap */}
          <Script src="/assets/js/plugin/animation.gsap.js"/>
          {/* debug.addIndicators */}
          <script src="/assets/js/plugin/debug.addIndicators.min.js"/>
          <script src="/assets/js/plugin/squareCountDownClock.js"/>
          <script src="/assets/js/plugin/wow.min.js"/>
          <script src="/assets/js/plugin/jquery.nice-select.min.js"/>
          <script src="/assets/js/plugin/jquery.fancybox.min.js"/>
          <script src="/assets/js/plugin/swiper-bundle.min.js"/>
          <script src="/assets/js/plugin/jquery.waypoints.min.js"/>
          <script src="/assets/js/plugin/jquery.counterup.min.js"/>
          <script src="/assets/js/plugin/jquery.paroller.js"/>
          <script src="/assets/js/script.js"/>
        </Head>
        <body>
          <Main />
          <NextScript />
        </body>
      </Html>
    );
}

