module.exports = {
  env: {
    REACT_APP_GOOGLE_ANALYTICS: process.env.REACT_APP_GOOGLE_ANALYTICS,
    REACT_APP_API_KEY: process.env.REACT_APP_API_KEY,
    REACT_APP_AUTH_DOMAIN: process.env.REACT_APP_AUTH_DOMAIN,
    REACT_APP_DATABASE_URL: process.env.REACT_APP_DATABASE_URL,
    REACT_APP_PROJECT_ID: process.env.REACT_APP_PROJECT_ID,
    REACT_APP_STORAGE_BUCKET: process.env.REACT_APP_STORAGE_BUCKET,
    REACT_APP_MESSAGING_SENDER_ID: process.env.REACT_APP_MESSAGING_SENDER_ID,
    REACT_APP_MEASUREMENT_ID: process.env.REACT_APP_MEASUREMENT_ID,
    REACT_APP_SERVER_URL: process.env.REACT_APP_SERVER_URL
  },
  typescript: {
    ignoreBuildErrors: true
  },
  async redirects() {
    return [
      {
        source: '/dashboard',
        destination: '/dashboard/projects',
        permanent: true,
      },
      {
        source: '/dashboard/project',
        destination: '/dashboard/projects',
        permanent: true,
      },
    ]
  },
};
