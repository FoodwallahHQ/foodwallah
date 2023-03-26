import React, {FC, useEffect} from 'react';
import {useStore} from "@/hooks/useStore";
import {
  isSignInWithEmailLink,
  signInWithEmailLink,
} from "firebase/auth";
import {auth} from "@/utils/firebase-setup";
import {observer} from "mobx-react";
import {ToastContainer} from "react-toastify";
import {registerUser} from "@/utils/utils";
import {Box, CircularProgress} from "@mui/material";
import {useRouter} from "next/router";
import {PATHS} from "@/utils/constants";


const SignInConfirmation: FC<any> = observer((_props) => {
  const store = useStore()
  const router = useRouter()

  useEffect(() => {
    confirmEmail()
  }, [])

  const confirmEmail = (): void => {
    if (isSignInWithEmailLink(auth, window.location.href)) {
      // Additional state parameters can also be passed via URL.
      // This can be used to continue the user's intended action before triggering
      // the sign-in operation.
      // Get the email if available. This should be available if the user completes
      // the flow on the same device where they started it.
      let email = window.localStorage.getItem('emailForSignIn');
      if (email) {
        // The client SDK will parse the code from the link for you.
        signInWithEmailLink(auth, email, window.location.href)
        .then((_result) => {
          // Clear email from storage.
          window.localStorage.removeItem('emailForSignIn');
          // You can access the new user via result.user
          // Additional user info profile not available via:
          // result.additionalUserInfo.profile == null
          // You can check if the user is new or existing:
          // result.additionalUserInfo.isNewUser
          registerUser(store.sessionDataStore)
        })
        .catch((error) => {
          const errorMessage = error.message;
          if (errorMessage.includes("invalid")) {
            router.push({
              pathname: PATHS.LOGIN,
              query: {confirmationFailed: true}
            }).catch(e => console.log(e))
          }
        });
      }
    }
  }


  return (
    <>
      <section className="sign-up-wrapper overflow-hidden pt-215 pb-165">
        <div className="container">
          <div className="sign-up-box">
                <div className="form-wrapper form-wrapper-signin">
                  <div className="text-center">
                    <img className="sign-in-logo" src="/assets/images/logo.png" alt="logo"/>
                    <span className="sign-in-message">Confirming email address...</span>
                  </div>
                      <form action="#" className="sign-up-form">
                    <div className="row" style={{minHeight: '400px'}}>
                      <div className="col-md-12">
                        <Box sx={{position: 'relative', top: '35%', left: '40%'}}>
                          <CircularProgress />
                        </Box>
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

export default SignInConfirmation;

