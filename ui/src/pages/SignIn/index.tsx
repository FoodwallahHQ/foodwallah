import React, {FC, useEffect, useState} from 'react';
import {useStore} from "@/hooks/useStore";
import {
  GoogleAuthProvider,
  indexedDBLocalPersistence, sendSignInLinkToEmail,
  setPersistence,
  signInWithPopup, TwitterAuthProvider
} from "firebase/auth";
import {auth} from "@/utils/firebase-setup";
import firebase from "firebase/compat";
import AuthProvider = firebase.auth.AuthProvider;
import {observer} from "mobx-react";
import {
  GoogleLoginButton,
  TwitterLoginButton
} from "react-social-login-buttons";
import {toast, ToastContainer} from "react-toastify";
import {registerUser} from "@/utils/utils";
import {PATHS} from "@/utils/constants";

export interface SignInProps {
  confirmationFailed?: boolean
}

const actionCodeSettings = {
  // URL you want to redirect back to. The domain (www.example.com) for this
  // URL must be in the authorized domains list in the Firebase Console.
  url: process.env.NODE_ENV === 'production' ? 'https://kalico.ai' + PATHS.EMAIL_CONFIRMATION : 'http://localhost:3000' + PATHS.EMAIL_CONFIRMATION,
  // This must be true.
  handleCodeInApp: true
};

const SignIn: FC<SignInProps> = observer((props) => {
  const store = useStore()
  const [error, setError] = useState<string | undefined>(undefined)
  const [email, setEmail] = useState<string | undefined>(undefined)

  const handleEmail = (event: any): void => {
    event.preventDefault()
    setEmail(event.target.value)
  }

  useEffect(() => {
    if (props.confirmationFailed) {
      toast("Invalid confirmation link", {
        type: 'error',
        position: toast.POSITION.TOP_CENTER
      });
    }
  }, [])

  const signInWithEmail = (): void => {
    setPersistence(auth, indexedDBLocalPersistence)
    .then(() => {
      sendSignInLinkToEmail(auth, email, actionCodeSettings)
      .then(() => {
        window.localStorage.setItem('emailForSignIn', email);
        toast("Please check your email and follow the instructions to login", {
          type: 'success',
          position: toast.POSITION.TOP_CENTER
        });
      })
      .catch((error) => {
        const errorMessage = error.message;
        console.log(errorMessage)
      });
    })
  }

  /**
   * Sign in user using Google login pop screen. After login, persist the user
   * object in indexDB so that the session is not terminated if the user refreshes
   * the page or closes the browser.
   */
  const firebaseSignIn = (provider: AuthProvider) => {
    setPersistence(auth, indexedDBLocalPersistence)
    .then(() => {
      // In local persistence will be applied to the signed in Google user
      return signInWithPopup(auth, provider)
      .then((result) => {
        if (result.user) {
          registerUser(store.sessionDataStore)
        }
      }).catch(e => {
        setError(e.message)
      });
    })
    .catch((e) => {
      console.log(e)
    });
  }

  useEffect(() => {
    if (error) {
      toast(error, {
        type: 'error',
        position: toast.POSITION.TOP_CENTER
      });
      setError(undefined)
    }
  }, [error])

  return (
    <>
      <section className="sign-up-wrapper overflow-hidden pt-215 pb-165">
        <div className="container">
          <div className="sign-up-box">
                <div className="form-wrapper form-wrapper-signin">
                  <div className="text-center">
                    <img className="sign-in-logo" src="/assets/images/logo.png" alt="logo"/>
                    <span className="sign-in-message">Sign in with your social accounts</span>
                    <ul className="create-with-list mb-35">
                      <li>
                        <GoogleLoginButton  onClick={() => firebaseSignIn(new GoogleAuthProvider())}/>
                      </li>
                      {/*<li>*/}
                      {/*  <AppleLoginButton onClick={() => firebaseSignIn(new OAuthProvider('apple.com'))}/>*/}
                      {/*</li>*/}
                      <li>
                        <TwitterLoginButton onClick={() => firebaseSignIn(new TwitterAuthProvider())}/>
                      </li>
                      {/*<li>*/}
                      {/*  <FacebookLoginButton onClick={() => firebaseSignIn(new FacebookAuthProvider())}/>*/}
                      {/*</li>*/}
                    </ul>
                    <span className="divider">or</span>
                  </div>
                      <form action="#" className="sign-up-form">
                    <div className="row">
                      <div className="col-md-12">
                        <input type="email" placeholder="Email address" onChange={handleEmail} style={{textTransform: 'none'}}/>
                      </div>
                      {/*<div className="col-md-12 mt-20">*/}
                      {/*  <input type="password" placeholder="Password*"/>*/}
                      {/*</div>*/}
                      {/*<div className="col-6">*/}
                      {/*  <input type="checkbox" id="remember"/>*/}
                      {/*  <label className="check-text" htmlFor="remember"> Remember me </label>*/}
                      {/*</div>*/}
                      {/*<div className="col-6">*/}
                      {/*  <a className="forgot-link float-end" href="#">Forgot Password?</a>*/}
                      {/*</div>*/}
                      <div className="col-md-12 mt-30">
                        <input className="btn btn-red" value="Sign In" onClick={signInWithEmail}/>
                      </div>
                    </div>
                  </form>
                </div>
          </div>
        </div>
      </section>
      <ToastContainer
          style={{width: '100%', maxWidth: '600px'}}
          position="top-center"
          autoClose={5000}
          hideProgressBar
          newestOnTop={false}
          closeOnClick
          rtl={false}
          pauseOnFocusLoss
          draggable
          pauseOnHover
          theme="colored"/>
    </>
  );
})

export default SignIn;

