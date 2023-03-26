import React from 'react';


function Clients() {
  // Check that the DOM has loaded before rendering the page so that
  // we don't get a page without the CSS
  return (
      <section className="client-area">
        <div className="container">
          <div className="client-wrapper">
            <div className="row">
              <div className="col-12">
                <div className="client-text">
                  <h4 className="mt-n3"><span>3000+ Companies</span> Trust Sturtaplanding to
                    build landing page for their <span>dream product</span></h4>
                </div>
              </div>
            </div>
            <div className="row justify-content-center">
              <div className="w-50 w-md-20">
                <div className="client-image">
                  <img src="/assets/images/brand/brand-1.svg" alt="Brand Image"/>
                </div>
              </div>
              <div className="w-50 w-md-20">
                <div className="client-image">
                  <img src="/assets/images/brand/brand-2.svg" alt="Brand Image"/>
                </div>
              </div>
              <div className="w-50 w-md-20">
                <div className="client-image">
                  <img src="/assets/images/brand/brand-3.svg" alt="Brand Image"/>
                </div>
              </div>
              <div className="w-50 w-md-20">
                <div className="client-image">
                  <img src="/assets/images/brand/brand-4.svg" alt="Brand Image"/>
                </div>
              </div>
              <div className="w-50 w-md-20">
                <div className="client-image">
                  <img src="/assets/images/brand/brand-5.svg" alt="Brand Image"/>
                </div>
              </div>
            </div>
            <div className="client-meta">
              <ul className="client-review-icon">
                <li><i className="fas fa-star"></i></li>
                <li><i className="fas fa-star"></i></li>
                <li><i className="fas fa-star"></i></li>
                <li><i className="fas fa-star"></i></li>
                <li><i className="fas fa-star"></i></li>
              </ul>
              <div className="client-review-number"><span>4.9</span> Stars</div>
              <div className="client-review"><span>5121+</span> Reviews</div>
            </div>
          </div>
        </div>
      </section>
  );
}

export default Clients;
