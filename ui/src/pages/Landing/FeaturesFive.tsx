import React from 'react';


function FeaturesFive() {
  // Check that the DOM has loaded before rendering the page so that
  // we don't get a page without the CSS
  return (
      <section className="features-area-five section-padding wow fadeInUp animate__fast">
        <div className="container">
          <div className="row">
            <div className="col-12">
              <div className="section-title-center">
                <h2 className="mt-n4">Move work forward from anywhere</h2>
              </div>
              <div className="feature-image">
                <img src="/assets/images/features/feature-5.png" alt="Features Image"/>
                <img src="/assets/images/features/features-8.png" alt="Features Image"
                     className="features8"/>
                <img src="/assets/images/features/features-9.png" alt="Features Image"
                     className="features9"/>
                <p>
                  We're here every step of the way making sure you and your team deliver. We're
                  here every step of the way making sure you and your team deliver
                </p>
              </div>
            </div>
          </div>
        </div>
      </section>
  );
}

export default FeaturesFive;
