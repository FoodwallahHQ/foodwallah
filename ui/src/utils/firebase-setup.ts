import { initializeApp } from 'firebase/app';
import { getAuth } from 'firebase/auth';

const config = {
  apiKey: "AIzaSyAKB1lxHNctFnQOHtUdpZqV5bn9gGv1dAU",
  authDomain: "kalico-ai.firebaseapp.com",
  projectId: "kalico-ai",
  storageBucket: "kalico-ai.appspot.com",
  messagingSenderId: "635275971140",
  appId: "1:635275971140:web:28053177fc713f3d88ca11",
  measurementId: "G-XDVFQRG710"
};

// Initialize Firebase
const app = initializeApp(config);
export const auth = getAuth(app);