import React from 'react';


function Features() {
  return (
      <section className="outstanding-feature-area pt-80 pb-130" id="features">
        <div className="section-title-center wow fadeInUp">
          <h2>Outstanding Features</h2>
        </div>
        <div className="container pt-30">
          <div className="row gy-lg-0 gy-4">
            <div className="col-lg-3 col-md-6 wow fadeInUp" data-wow-delay="0.1s">
              <img src="/assets/images/home_2/out-feature-1.svg" alt="img"/>
              <h5>24/7 Support</h5>
              <p>We'll treat your customers like they're ours · We have an excellent team.</p>
            </div>
            <div className="col-lg-3 col-md-6 wow fadeInUp" data-wow-delay="0.3s">
              <img src="/assets/images/home_2/out-feature-2.svg" alt="img"/>
              <h5>Tested Security</h5>
              <p>The tested Security here. To review and adjust your security settings.</p>
            </div>
            <div className="col-lg-3 col-md-6 wow fadeInUp" data-wow-delay="0.5s">
              <img src="/assets/images/home_2/out-feature-3.svg" alt="img"/>
              <h5>Smart Dashboard</h5>
              <p>The Best Mobile App Awards honors the best mobile apps on iPhone,</p>
            </div>
            <div className="col-lg-3 col-md-6 wow fadeInUp" data-wow-delay="0.7s">
              <img src="/assets/images/home_2/out-feature-4.svg" alt="img"/>
              <h5>XO Award 2021</h5>
              <p>The Best Mobile App Awards honors the best mobile apps on iPhone,</p>
            </div>
          </div>
        </div>
      </section>
  );
}

export default Features;
