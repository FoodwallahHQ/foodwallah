import React from 'react';


function Testimonials() {
  // Check that the DOM has loaded before rendering the page so that
  // we don't get a page without the CSS
  return (
      <section className="testimonial-area testimonial bg-black wow fadeInUp animate__fast">
        <div id="fixedWrapper" className="pt-md-4">
          <div className="container">
            <div className="row">
              <div className="col-12">
                <div className="testimonial-title text-center">
                  <h2 className="text-white mt-n3">Choose a workflow or make your own</h2>
                </div>
              </div>
            </div>
          </div>
          <div className="container">
            <div id="scroll-container">
              <div className="row">
                <div className="col-12 max-width">
                  <div className="testimonial-wrapper">
                    <div className="row align-items-center">
                      <div className="col-7 col-md-7">
                        <div className="testimonial-content">
                          <h3 className="mt-n4">Plan & Create</h3>
                          <span> Elit sed sapien viverra eleifend malesuada purus pellentesque ut eget. </span>
                          <p>Ligula ridiculus lacinia mattis non volutpat. Pellentesque id
                            aenean diam aenean tincidunt non fermentum nunc mattis.</p>
                          <div className="testimonial-review">
                            <i className="fas fa-quote-left"></i>
                            <p>
                              Startup Landing is a great theme. Ligula ridiculus lacinia mattis
                              non volutpat.
                              <span>Ligula ridiculus lacinia</span>
                            </p>
                            <div className="testimonial-author">
                              <div className="testimonial-author-image">
                                <img src="/assets/images/testimonial/testimonial-1.jpg"
                                     alt="Testimonial"/>
                              </div>
                              <div className="testimonial-author-name">
                                <h4>Jesus Requena</h4>
                                <span>Support Engineer, Aliexpress</span>
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                      <div className="col-5 col-md-5">
                        <div className="testimonial-right-image">
                          <img src="/assets/images/testimonial/testimonial.png" alt=""/>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <div className="col-12 max-width">
                  <div className="testimonial-wrapper">
                    <div className="row align-items-center">
                      <div className="col-7 col-md-7">
                        <div className="testimonial-content">
                          <h3 className="mt-n4">Plan & Create</h3>
                          <span>Elit sed sapien viverra eleifend malesuada purus pellentesque ut eget.</span>
                          <p>Ligula ridiculus lacinia mattis non volutpat. Pellentesque id
                            aenean diam aenean tincidunt non fermentum nunc mattis.</p>
                          <div className="testimonial-review">
                            <i className="fas fa-quote-left"></i>
                            <p>
                              Startup Landing is a great theme. Ligula ridiculus lacinia mattis
                              non volutpat.
                              <span>Ligula ridiculus lacinia</span>
                            </p>
                            <div className="testimonial-author">
                              <div className="testimonial-author-image">
                                <img src="/assets/images/testimonial/testimonial-1.jpg"
                                     alt="Testimonial"/>
                              </div>
                              <div className="testimonial-author-name">
                                <h4>Jesus Requena</h4>
                                <span>Support Engineer, Aliexpress</span>
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                      <div className="col-5 col-md-5">
                        <div className="testimonial-right-image">
                          <img src="/assets/images/testimonial/testimonial.png" alt=""/>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <div className="col-12 max-width">
                  <div className="testimonial-wrapper">
                    <div className="row align-items-center">
                      <div className="col-7 col-md-7">
                        <div className="testimonial-content">
                          <h3 className="mt-n4">Plan & Create</h3>
                          <span>Elit sed sapien viverra eleifend malesuada purus pellentesque ut eget.</span>
                          <p>Ligula ridiculus lacinia mattis non volutpat. Pellentesque id
                            aenean diam aenean tincidunt non fermentum nunc mattis.</p>
                          <div className="testimonial-review">
                            <i className="fas fa-quote-left"></i>
                            <p>
                              Startup Landing is a great theme. Ligula ridiculus lacinia mattis
                              non volutpat.
                              <span>Ligula ridiculus lacinia</span>
                            </p>
                            <div className="testimonial-author">
                              <div className="testimonial-author-image">
                                <img src="/assets/images/testimonial/testimonial-1.jpg"
                                     alt="Testimonial"/>
                              </div>
                              <div className="testimonial-author-name">
                                <h4>Jesus Requena</h4>
                                <span>Support Engineer, Aliexpress</span>
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                      <div className="col-5 col-md-5">
                        <div className="testimonial-right-image">
                          <img src="/assets/images/testimonial/testimonial.png" alt=""/>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
  );
}

export default Testimonials;
