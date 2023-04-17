import type { FC, ReactNode } from 'react';
import { useEffect, useState } from 'react';
import { useRouter } from 'next/router';
import {observer} from "mobx-react";
import {useStore} from "@/hooks/useStore";

interface AuthenticatedProps {
  children: ReactNode;
}

export const Authenticated: FC<AuthenticatedProps> = observer((props) => {
  const { children } = props;
  const store = useStore();
  const router = useRouter();

  const [authenticated, setAuthenticated] = useState<boolean>(false)

  const toLoginScreen = () => {
      router.push({
        pathname: '/login'
      }).catch(e => console.log(e))

  }

  useEffect(() => {
    if (store?.sessionDataStore?.user) {
      setAuthenticated(true)
    } else {
      toLoginScreen()
    }
  }, [store?.sessionDataStore?.user])

  if (authenticated) {
    return <>{children}</>;
  } else {
    return null;
  }

});
