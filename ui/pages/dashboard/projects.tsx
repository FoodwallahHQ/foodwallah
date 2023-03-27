import React, {FC, useEffect, useState} from 'react';
import MyProjects from "@/pages/Dashboard/MyProjects";
import {Project, ProjectApi} from "@/api";
import {headerConfig} from "@/api/headerConfig";
import Head from "next/head";
import {auth} from "@/utils/firebase-setup";


export interface ProjectIndexProps {
  title?: string,
  description?: string,
  siteImage?: string
}

const ProjectsIndex: FC<ProjectIndexProps> =  (_props) => {
  const [projects, setProjects] = useState<Project[]>([])

  const fetchProjects = () => {
    auth.onAuthStateChanged(user => {
      if (user) {
        user.getIdToken(false)
        .then(tokenResult => {
          const projectApi = new ProjectApi(headerConfig(tokenResult))
          projectApi.getAllProjects()
          .then(response => {
            if (response.data && response.data.records) {
              setProjects(response.data.records)
            }
          }).catch(e => console.log(e))
        }).catch(e => console.log(e))
      }
    })
  }

  useEffect(() => {
    fetchProjects()
  }, [!projects])

    return (
        <>
          <Head>
            <title>Foodwallah | My Projects</title>
          </Head>
          <main>
            <section className="container">
              <MyProjects projects={projects}/>
            </section>
          </main>
        </>
    );
}

export default ProjectsIndex
