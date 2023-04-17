import {createContext, FC, ReactNode} from "react";
import PropTypes from "prop-types";
import RootStore from "./RootStore";
import {SessionDataStore} from "./Store";

let store: RootStore

interface RootStoreContextValue {
    sessionDataStore: SessionDataStore,
}

interface RootStoreProviderProps {
    children: ReactNode;
}

export const RootStoreContext = createContext<RootStoreContextValue | undefined>(undefined);

export const RootStoreProvider: FC<RootStoreProviderProps> = (props) => {
    const { children } = props;
    const root = store ?? new RootStore()
    return (
        <RootStoreContext.Provider
            value={root}>
            {children}
        </RootStoreContext.Provider>
    );
}

RootStoreProvider.propTypes = {
    children: PropTypes.node.isRequired
};
