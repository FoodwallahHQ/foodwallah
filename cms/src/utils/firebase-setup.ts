import { initializeApp } from 'firebase/app';
import { getAuth } from 'firebase/auth';

const firebaseConfig = {
  apiKey: "AIzaSyDZ6jaMZ7rf0a8M97EsVHE_QtipgBAgn-U",
  authDomain: "foodwallah-e32d6.firebaseapp.com",
  projectId: "foodwallah-e32d6",
  storageBucket: "foodwallah-e32d6.appspot.com",
  messagingSenderId: "403777416588",
  appId: "1:403777416588:web:0639fae02c2fef9dab3aa9",
  measurementId: "G-JWLGCR92VV"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
export const auth = getAuth(app);