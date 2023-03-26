import React, {useEffect, useRef, useState} from 'react';


function VideoDemo() {
  const demoVideo = 'https://d229njkjc1dgnt.cloudfront.net/video/Kalico_Demo_Project.mp4'
  // const demoVideo = 'https://framerusercontent.com/modules/assets/ZmbqqxSklKO3NmgvrLxv4erd8DM~YsxzbRNmLTHdxAPPyl1NdHBeUhjWmcqbqr164_TGoiA.mp4'
  // const demoVideo = "/assets/videos/Kalico_Demo_Project.mp4";
  const videoRef = useRef<HTMLVideoElement>(null);
  const [showButton, setShowButton] = useState(false)

  const handlePlay = () => {
    if (videoRef?.current) {
      videoRef.current.currentTime = 0.0
      videoRef.current
      .play()
      .then(_ => {
        setShowButton(false)
      })
      .catch(e => console.log(e))
    }
  }

  useEffect(() => {
   if (videoRef?.current &&
       videoRef?.current.ended) {
     setShowButton(true)
   } else {
     setShowButton(false)
   }
  })

  return (
      <section className="video-container">
        <div className="container">
          <div className="row">
            <div className="col-12">
              <div className="video-box-btn-container">
                {showButton && <button className="play-btn" onClick={handlePlay}><i className="fas fa-play"></i></button>}
              <div className="video-player">
                <video
                    webkit-playsinline
                    playsInline
                    autoPlay
                    muted
                    preload="auto"
                    onClick={handlePlay}
                    onEnded={() => setShowButton(true)}
                    loop
                    ref={videoRef}
                    src={demoVideo}
                    />
                </div>
            </div>
            </div>
              </div>
          </div>
      </section>
  );
}

export default VideoDemo;
