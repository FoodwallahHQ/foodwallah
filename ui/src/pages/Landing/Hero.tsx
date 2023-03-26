import React from 'react';
import Link from "next/link";
import {PATHS} from "@/utils/constants";


function Hero() {
  // Check that the DOM has loaded before rendering the page so that
  // we don't get a page without the CSS
  return (
      <section className="banner-area-4 pb-60">
        <div className="container">
          <div className="row flex-column-reverse flex-md-row align-items-center">
            <div className="col-md-12">
              <div className="banner-left text-center">
                <h2>
                  Instantly create captivating articles from your
                  <span style={{color: '#ee8e3b'}}>
                    {' '}videos
                  </span>
                  {' '}and
                  <span style={{color: '#ee8e3b'}}>
                    {' '} podcasts
                  </span>
                </h2>
                {/*<span className="banner-para">Maximize the reach of your videos and podcasts by turning them into engaging and shareable articles instantly.</span>*/}
                <div className="hero-cta">
                  {/*<div className="right-nav">*/}
                    {/*<a href="#" className="language-bar mr-50"><span className="active">En.</span><span>Ru</span></a>*/}
                    <Link className="btn btn-red" href={PATHS.DASHBOARD}>Get Started for Free</Link>
                    {/*<Link className="btn btn-red" href={PATHS.SIGN_UP}>Sign Up</Link>*/}
                  {/*</div>*/}
                {/*<button  className="btn btn-red">Get Started for Free</button>*/}

                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
  );
}

export default Hero;
