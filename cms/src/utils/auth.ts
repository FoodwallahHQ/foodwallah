import {auth} from "@/utils/firebase-setup";
import {SessionDataStore} from "@/store/Store";

export const setAuthState = (store: SessionDataStore, action: (boolean) => void) => {
  auth.onAuthStateChanged(user => {
    if (user) {
      action(true)
      // @ts-ignore
      store.setUser(user)
    } else {
      action(false)
      store.setUser(undefined)
    }
  })
}