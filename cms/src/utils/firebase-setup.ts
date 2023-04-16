import { initializeApp } from 'firebase/app';
import { getAuth } from 'firebase/auth';

const devConfig = {
  apiKey: 'AIzaSyBPK8HxA8Ul-RDAVcrxdDsFL44mn4aUkR8',
  authDomain: 'foodwallah-dev.firebaseapp.com',
  projectId: 'foodwallah-dev',
  storageBucket: 'foodwallah-dev.appspot.com',
  messagingSenderId: '668448388734',
  appId: '1:668448388734:web:d80ccea21f8c2309318a2f',
  measurementId: 'G-BRLSYWQK53'
}


const prodConfig = {
  apiKey: 'AIzaSyBPK8HxA8Ul-RDAVcrxdDsFL44mn4aUkR8',
  authDomain: 'foodwallah-dev.firebaseapp.com',
  projectId: 'foodwallah-dev',
  storageBucket: 'foodwallah-dev.appspot.com',
  messagingSenderId: '668448388734',
  appId: '1:668448388734:web:d80ccea21f8c2309318a2f',
  measurementId: 'G-BRLSYWQK53'
}

// Initialize Firebase
const app = initializeApp(process.env.NODE_ENV === "production" ? prodConfig : devConfig);
export const auth = getAuth(app);