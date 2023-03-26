import React from 'react';


function FAQ() {
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
                        Our AI uses the audio, on-screen text, and description from the video as a source material to create the article. It does not simply copy the words from the video, but rather adapts them to make the final output more reader-friendly.
                      </p>
                    </div>
                  </div>
                </div>
                <div className="accordion-item">
                  <h2 className="accordion-header" id="project-headingHowLong">
                    <button
                        className="accordion-button collapsed"
                        type="button"
                        data-bs-toggle="collapse"
                        data-bs-target="#project-collapseHowLong"
                        aria-expanded="false"
                        aria-controls="project-collapseHowLong"
                    >
                      How long does it take to convert an audio or video?
                    </button>
                  </h2>
                  <div id="project-collapseHowLong" className="accordion-collapse collapse"
                       aria-labelledby="project-headingHowLong" data-bs-parent="#projectAccordion">
                    <div className="accordion-body">
                      <p>
                       A typical 10-minute food recipe video takes about 4-5 minutes to process. We expect this to fall to 2-3 minutes as we improve our algorithms.
                      </p>
                    </div>
                  </div>
                </div>
                <div className="accordion-item">
                  <h2 className="accordion-header" id="project-length-limit">
                    <button
                        className="accordion-button collapsed"
                        type="button"
                        data-bs-toggle="collapse"
                        data-bs-target="#project-length-limit"
                        aria-expanded="false"
                        aria-controls="project-length-limit"
                    >
                      Is there a limit to the length of the audio or video content?
                    </button>
                  </h2>
                  <div id="project-length-limit" className="accordion-collapse collapse"
                       aria-labelledby="project-length-limit" data-bs-parent="#projectAccordion">
                    <div className="accordion-body">
                      <p>
                        We can handle up to 20-30-minute audio and video files. Kalico is still in beta. We expect to handle much longer content over time.
                      </p>
                    </div>
                  </div>
                </div>
                <div className="accordion-item">
                  <h2 className="accordion-header" id="project-headingOneHalf">
                    <button
                        className="accordion-button collapsed"
                        type="button"
                        data-bs-toggle="collapse"
                        data-bs-target="#project-collapseOneHalf"
                        aria-expanded="false"
                        aria-controls="project-collapseOneHalf"
                    >
                      Is the article plagiarism free?
                    </button>
                  </h2>
                  <div id="project-collapseOneHalf" className="accordion-collapse collapse"
                       aria-labelledby="project-headingOneHalf" data-bs-parent="#projectAccordion">
                    <div className="accordion-body">
                      <p>
                        Our AI ensures 100% plagiarism-free output by using your original content to maintain contextual integrity while significantly improving it, instead of inventing content like other well-known content writing platforms.
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
                      What type of links and file formats do you support?
                    </button>
                  </h2>
                  <div id="project-collapseTwo" className="accordion-collapse collapse"
                       aria-labelledby="project-headingTwo" data-bs-parent="#projectAccordion">
                    <div className="accordion-body">
                      <p>
                       We support YouTube, Instagram Reels, and local MP4, WEBM, MP3, WAV, and AAC file
                       formats. Local uploads are currently limited to 100MB.
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
                      Which languages do you support?
                    </button>
                  </h2>
                  <div id="project-collapseThree" className="accordion-collapse collapse"
                       aria-labelledby="project-headingThree"
                       data-bs-parent="#projectAccordion">
                    <div className="accordion-body">
                      <p>
                        Kalico supports English. We will be adding support for more languages soon.
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
}

export default FAQ;
