import React from "react";

const BlogHero = () => {
    return (
        <div className="blog-hero-container">
          <div className="container mx-auto px-2 mb-2 clearfix header-text">
            <h1 className="h0 inline-block col-12 sm-width-full py-4 mt-3 header-title" style={{marginTop: '6rem'}}>Blog</h1>
            <div className="clearfix mb-4 py-1">
              <div className="col-4 sm-width-full left border-top-thin">
                <div className="table">

                </div>
                <p className="h4 lh-condensed font-smoothing mt-2 py-1">Official Kalico blog</p>
              </div>
            </div>
          </div>
        </div>
    );
}

export default BlogHero;