import {autorun, makeAutoObservable} from 'mobx';

export class SessionDataStore {

  contentPreview: Map<string, {}> = new Map<string, {}>()
  user: any | undefined = undefined

  constructor() {
    makeAutoObservable(this);
    const storedJson = localStorage.getItem(StoreKey.SESSION_DATA);
    if (storedJson) Object.assign(this, JSON.parse(storedJson));
    autorun(() => {
      localStorage.setItem(StoreKey.SESSION_DATA, JSON.stringify(this))
    })
  }

  addContentPreview(key: string, data: {}) {
    const content = {...this.contentPreview}
    content[key] = data
    this.contentPreview = content
    localStorage.setItem(StoreKey.SESSION_DATA, JSON.stringify(this))
  }

  setUser(userObj: any) {
    this.user = userObj
    localStorage.setItem(StoreKey.SESSION_DATA, JSON.stringify(this))
  }

  reset() {
    this.contentPreview.clear()
    this.user = undefined
    localStorage.removeItem(StoreKey.SESSION_DATA);
  }
}

export const StoreKey = {
  SESSION_DATA: "SESSION_DATA"
}
