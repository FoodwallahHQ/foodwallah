import React from 'react';
import Hero from "@/pages/Landing/Hero";
import WhyKalico from "@/pages/Landing/WhyKalico";
import FAQ from './FAQ';
import HowItWorks from "@/pages/Landing/HowItWorks";
import VideoDemo from "@/pages/Landing/VideoDemo";

function Landing() {
  return (
      <>
        <main>
          <Hero/>
          <VideoDemo/>
          <WhyKalico/>
          <HowItWorks/>
          <FAQ/>
        </main>
      </>
  );
}

export default Landing;
