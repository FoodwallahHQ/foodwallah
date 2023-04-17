import moment from "moment";
import {Box, CircularProgress} from "@mui/material";
import React from "react";
import {SessionDataStore, StoreKey} from "@/store/Store";
import {auth} from "@/utils/firebase-setup";
import {UserApi} from "@/api";
import {headerConfig} from "@/api/headerConfig";

export const getFormattedDate = (date: number) => {
  return moment.unix(date).format("ll")
}

export const truncatedDescription = (text: string) => {
  const MAX_LENGTH = 215
  if (text && text.length > 215) {
    return text.slice(0, MAX_LENGTH) + "..."
  }
  return text
}

export const sessionIdSet = (req): boolean => {
  const cookies = {};
  req.headers.cookie.split(';').forEach(function(cookie) {
    const parts = cookie.match(/(.*?)=(.*)$/)
    cookies[parts[1].trim()] = (parts[2] || '').trim();
  });
   return !!cookies['sessionId'];

}

export const CenterAlignedProgress = () => {
  return (
      <Box className='center-aligned-progress'>
        <CircularProgress />
      </Box>
  )
}

export const userSessionFound = () => {
  const storedJson = localStorage.getItem(StoreKey.SESSION_DATA);
  if (storedJson) {
    if (JSON.parse(storedJson).user?.firebase_id) {
      return true;
    }
  }
  return false;
}

/**
 * Check if the current user is registered. If not, register them. Once
 * registration has been verified or completed, disable the progress indicator and
 * route the user to the dashboard.
 *
 */
export const registerUser = (store: SessionDataStore) => {
  auth.onAuthStateChanged(user => {
    if (user) {
      user.getIdTokenResult(false)
      .then(tokenResult => {
        new UserApi(headerConfig(tokenResult.token))
        .getUserprofile()
        .then(result => {
          if (result.data.profile) {
            store?.setUser(result.data.profile)
          }
        }).catch(e => console.log(e))
      }).catch(e => console.log(e))
    }
  })
}