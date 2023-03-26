import React from 'react';


function HowItWorks() {
  return (
      <>
        <section className="process-area-3 overflow-hidden pt-140 pb-140" id="how-it-works">
          <div className="container" style={{padding: '15px'}}>
            <div className="process-item-3 pb-105">
              <div className="row">
                <div className="col-12">
                  <h2 className="heading-3 text-center mb-90 wow fadeInUp"
                      data-wow-delay="0.1s">How Kalico Works</h2>
                </div>
              </div>
              <div className="row">
                <div className="col-lg-6 col-md-7">
                  <div className="process-content-3 pt-30 wow fadeInLeft" data-wow-delay="0.2s">
                    <span className="item-number">01</span>
                    <h3 className="item-title">Submit your content</h3>
                    <p className="item-text">
                      Grab a link from YouTube or Instagram Reels (and more options will be available soon). Additionally, you can upload a video or audio file from your computer. We can process anything that has audio of people speaking.
                    </p>
                  </div>
                </div>
                <div className="col-lg-6 col-md-5">
                  <div className="process-images-3 wow fadeInRight" data-wow-delay="0.2s">
                    <img src="/assets/images/landing/undraw_video.svg" alt="How Kalico works" className="shape"/>
                  </div>
                </div>
              </div>
            </div>
            <div className="process-item-3 pb-140">
              <div className="row flex-row-reverse">
                <div className="col-lg-6 col-md-7">
                  <div className="process-content-3 wow fadeInRight" data-wow-delay="0.2s">
                    <span className="item-number">02</span>
                    <h3 className="item-title">Kalico converts your audio or video into an article</h3>
                    <p className="item-text">
                      Our AI utilizes state-of-the-art speech-to-text technology to extract a transcript from which it then creates a high quality article. Typically, it takes 2-4 minutes to process an average-length food recipe video.
                    </p>
                  </div>
                </div>
                <div className="col-lg-6 col-md-5">
                  <div className="process-images-3 wow fadeInLeft" data-wow-delay="0.2s">
                    <img src="/assets/images/landing/undraw_data_processing.svg" alt="How Kalico works" className="shape"/>
                  </div>
                </div>
              </div>
            </div>
            <div className="process-item-3 pb-135">
              <div className="row">
                <div className="col-lg-6 col-md-7">
                  <div className="process-content-3 wow fadeInLeft" data-wow-delay="0.2s">
                    <span className="item-number">03</span>
                    <h3 className="item-title">View publication-ready article</h3>
                    <p className="item-text">
                      The end result is an article that has been beautifully enhanced with rich formatting and structure. You can make any minor adjustments as needed directly in the dashboard, and then copy it into your desired blog or publishing platform.
                    </p>
                  </div>
                </div>
                <div className="col-lg-6 col-md-5">
                  <div className="process-images-3 wow fadeInRight" data-wow-delay="0.2s">
                    <img src="/assets/images/landing/undraw_resume_folder.svg" alt="How Kalico works" className="shape"/>
                  </div>
                </div>
              </div>
            </div>
            <div>
              <img src="/assets/images/process/shape7.svg" alt="Border Shape"
                   className="shape-border"/>
              <span className="point"></span>
            </div>
          </div>
        </section>
        </>
  );
}

export default HowItWorks;
