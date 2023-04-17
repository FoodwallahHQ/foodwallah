import {autorun, makeAutoObservable} from 'mobx';
import {UserProfile} from "@/api";

export class SessionDataStore {

  cookMode: boolean = false;
  portions: number = 1;
  interval: any | undefined;
  user: UserProfile | undefined;

  constructor() {
    makeAutoObservable(this);
    const storedJson = localStorage.getItem(StoreKey.SESSION_DATA);
    if (storedJson) Object.assign(this, JSON.parse(storedJson));
    autorun(() => {
      localStorage.setItem(StoreKey.SESSION_DATA, JSON.stringify(this))
    })
  }

  setCookMode(cookMode: boolean) {
    this.cookMode = cookMode;
    localStorage.setItem(StoreKey.SESSION_DATA, JSON.stringify(this))
  }

  setPortions(portions: number) {
    this.portions = portions;
    localStorage.setItem(StoreKey.SESSION_DATA, JSON.stringify(this))
  }

  setInterval(interval: any) {
    this.interval = interval;
    localStorage.setItem(StoreKey.SESSION_DATA, JSON.stringify(this))
  }

  setUser(user: UserProfile) {
    this.user = user;
    // console.log("setting user in session storage: ", this.user)
    localStorage.setItem(StoreKey.SESSION_DATA, JSON.stringify(this))
  }

  reset() {
    this.cookMode = false;
    this.portions = 1;
    this.interval = undefined;
    this.user = undefined;
    localStorage.removeItem(StoreKey.SESSION_DATA);
  }
}

export const StoreKey = {
  SESSION_DATA: "SESSION_DATA"
}
