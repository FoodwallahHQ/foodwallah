import {SessionDataStore} from "./Store";

export default class RootStore {
    sessionDataStore: SessionDataStore

    constructor() {
        // Wait for the window to be available before instantiating the stores
        if (typeof window !== 'undefined') {
            this.sessionDataStore = new SessionDataStore();
        }
    }
}
