import React, {FC} from 'react';
import {observer} from "mobx-react";

export interface DashboardProps {

}

const Dashboard: FC<DashboardProps> = observer((_props) => {
  return (
      <section className="faq-area wow fadeInUp animate__fast">
        <div className="container" id="support">
          <div className="row">
            <div className="col-12">
              <div className="section-title-center">
                <h2 className="font-bold mt-n3 mt-md-n4">Frequently asked questions</h2>
                <p>You can find answers to the most common questions people ask us here.</p>
              </div>
            </div>
            <div className="col-12">
              <div className="accordion" id="projectAccordion">
                <div className="accordion-item">
                  <h2 className="accordion-header" id="project-headingOne">
                    <button
                        className="accordion-button collapsed"
                        type="button"
                        data-bs-toggle="collapse"
                        data-bs-target="#project-collapseOne"
                        aria-expanded="false"
                        aria-controls="project-collapseOne"
                    >
                       How does the AI work?
                    </button>
                  </h2>
                  <div id="project-collapseOne" className="accordion-collapse collapse"
                       aria-labelledby="project-headingOne" data-bs-parent="#projectAccordion">
                    <div className="accordion-body">
                      <p>
                        Our AI works by extracting audio and text contained in the video to write the article. This means
                        your video must have spoken audio or an accompanying description. In addition, the AI takes snapshots of meaningful segments
                        from the video and intelligently places them throughout the blog post or article.
                      </p>
                    </div>
                  </div>
                </div>


                <div className="accordion-item">
                  <h2 className="accordion-header" id="project-headingTwo">
                    <button
                        className="accordion-button collapsed"
                        type="button"
                        data-bs-toggle="collapse"
                        data-bs-target="#project-collapseTwo"
                        aria-expanded="false"
                        aria-controls="project-collapseTwo"
                    >
                      What types of links do you support?
                    </button>
                  </h2>
                  <div id="project-collapseTwo" className="accordion-collapse collapse"
                       aria-labelledby="project-headingTwo" data-bs-parent="#projectAccordion">
                    <div className="accordion-body">
                      <p>
                       We support YouTube and Instagram links right now. We will be adding support for
                        more links in the future.
                      </p>
                    </div>
                  </div>
                </div>
                <div className="accordion-item">
                  <h2 className="accordion-header" id="project-headingThree">
                    <button
                        className="accordion-button collapsed"
                        type="button"
                        data-bs-toggle="collapse"
                        data-bs-target="#project-collapseThree"
                        aria-expanded="false"
                        aria-controls="project-collapseThree"
                    >
                      Which languages are supported?
                    </button>
                  </h2>
                  <div id="project-collapseThree" className="accordion-collapse collapse"
                       aria-labelledby="project-headingThree"
                       data-bs-parent="#projectAccordion">
                    <div className="accordion-body">
                      <p>
                        Kalico supports English. We hope to support more languages in the near future.
                      </p>
                    </div>
                  </div>
                </div>
                <div className="accordion-item">
                  <h2 className="accordion-header" id="project-headingFour">
                    <button
                        className="accordion-button collapsed"
                        type="button"
                        data-bs-toggle="collapse"
                        data-bs-target="#project-collapseFour"
                        aria-expanded="false"
                        aria-controls="project-collapseFour"
                    >
                     What kinds of integrations are available?
                    </button>
                  </h2>
                  <div id="project-collapseFour" className="accordion-collapse collapse"
                       aria-labelledby="project-collapseFour"
                       data-bs-parent="#projectAccordion">
                    <div className="accordion-body">
                      <p>
                       We currently support CSV export for WordPress. We will be adding support for other platforms in the future.
                      </p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
  );
})

export default Dashboard;
