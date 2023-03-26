import React from 'react';


function FeaturesFour() {
  // Check that the DOM has loaded before rendering the page so that
  // we don't get a page without the CSS
  return (
      <section className="features features-area-four section-padding wow fadeInUp animate__fast">
        <div className="container">
          <div className="row align-items-center flex-column-reverse flex-lg-row">
            <div className="col-lg-6">
              <div className="features-content features-content-four">
                <h2>Realtime insight about your team</h2>
                <h3>Designed following <span>conversion</span> metrix</h3>
                <p>
                  We're here every step of the way making sure you and your team deliver. We're
                  here every step of the way making sure you and your team deliver
                </p>
              </div>
            </div>
            <div className="col-lg-6">
              <div className="features-image">
                <img src="/assets/images/features/features-4.png" alt="Features Image"/>
                <img src="/assets/images/features/features-object-6.svg" alt="Features Object"
                     className="features-object6"/>
                <img
                    src="/assets/images/features/features-object-7.svg"
                    alt="Features Object"
                    className="features-object7 object-element"
                    data-paroller-factor="0.05"
                    data-paroller-type="foreground"
                    data-paroller-direction="horizontal"
                    data-paroller-transition="transform .2s linear"
                />
                <img
                    src="/assets/images/features/features-object-8.svg"
                    alt="Features Object"
                    className="features-object8 object-element"
                    data-paroller-factor="0.05"
                    data-paroller-type="foreground"
                    data-paroller-transition="transform .2s linear"
                />
                <img
                    src="/assets/images/features/features-object-9.svg"
                    alt="Features Object"
                    className="features-object9 object-element"
                    data-paroller-factor="-0.05"
                    data-paroller-type="foreground"
                    data-paroller-transition="transform .2s linear"
                />
              </div>
            </div>
          </div>
        </div>
      </section>
  );
}

export default FeaturesFour;
