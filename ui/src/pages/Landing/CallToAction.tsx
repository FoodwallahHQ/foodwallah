import React from 'react';
import {PATHS} from "@/utils/constants";


function CallToAction() {
  // Check that the DOM has loaded before rendering the page so that
  // we don't get a page without the CSS
  return (
      <div className="community-area">
        <div className="container">
          <div className="row">
            <div className="col-12">
              <div className="community-wrapper">
                <div className="community-text">
                  <h3>Try Kalico for free today</h3>
                  <p>No credit card required</p>
                </div>
                <div className="community-social">
                  <div className="community-social-item">
                    <a href={PATHS.DASHBOARD}>
                      <span>Create Account</span>
                    </a>
                  </div>
                  <div className="community-social-item">
                    <a href={PATHS.TWITTER_PAGE}>
                      <span>Contact Us</span>
                    </a>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
  );
}

export default CallToAction;
