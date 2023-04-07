import {SeoApi} from "@/api";

function SiteMap() {}

export async function getServerSideProps({ res }) {
  // We make an API call to gather the URLs for our site
  const response = await new SeoApi().getSitemap()
  res.setHeader('Content-Type', 'text/xml');
  // we send the XML to the browser
  console.log(response.data)
  res.write(response.data);
  res.end();

  return {
    props: {},
  };
}

export default SiteMap;

