import React from 'react';

function Progress() {
  return (
      <div id="preloader">
        <div id="ctn-preloader" className="ctn-preloader">
          <div className="round_spinner">
            <div className="spinner"></div>
            <div className="text">
              <img className="mx-auto" src="/assets/images/spinner_logo.svg" alt=""/>
              <h4><span>Foodwallah</span></h4>
            </div>
          </div>
          <h2 className="head">Did You Know?</h2>
          <p></p>
        </div>
      </div>
  );
}

export default Progress;
