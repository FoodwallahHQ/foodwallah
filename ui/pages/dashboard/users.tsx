import React, {FC, useEffect, useState} from 'react';
import {ProjectApi, UserProjectsResponse} from "@/api";
import {headerConfig} from "@/api/headerConfig";
import Head from "next/head";
import {ADMIN_EMAIL, INTEGER_32_MAX, PATHS} from "@/utils/constants";
import {useRouter} from "next/router";
import UserProjects from "@/pages/Dashboard/UserProjects";


export interface ProjectIndexProps {
  title?: string,
  description?: string,
  siteImage?: string
}

const UsersIndex: FC<ProjectIndexProps> =  (_props) => {
  const [userProjects, setUserProjects] = useState<UserProjectsResponse | undefined>(undefined)
  const user = null;
  const router = useRouter()

  const fetchProjects = () => {
    user.getIdToken(false)
    .then(tokenResult => {
      const projectApi = new ProjectApi(headerConfig(tokenResult))
      projectApi.getAllUserProjects(0, INTEGER_32_MAX)
      .then(response => {
        if (response.data && response.data.records) {
          setUserProjects(response.data)
        }
      }).catch(e => console.log(e))
    }).catch(e => console.log(e))
  }

  useEffect(() => {
    fetchProjects()
  }, [!userProjects])

  if (user.email !== ADMIN_EMAIL) {
    router.push({
      pathname: PATHS.DASHBOARD
    }).catch(e => console.log(e))
  }

    return (
        <>
          <Head>
            <title>Kalico | Users</title>
          </Head>
            <div className="user-projects-container">
              <UserProjects userProjects={userProjects} user={user}/>
            </div>
        </>
    );
}

export default UsersIndex
