import React, {FC, useEffect, useState} from 'react';
import {PATHS} from "@/utils/constants";
import {ProjectApi, ProjectDetail} from "@/api";
import {headerConfig} from "@/api/headerConfig";
import MyProject from "@/pages/Dashboard/MyProject";
import {toast} from "react-toastify";
import {useRouter} from "next/router";
import Head from "next/head";
import {auth} from "@/utils/firebase-setup";

export async function getServerSideProps(context) {
  return {
    props: {
      projectId: context.query.id,
      editable: context.query?.editable ? context.query?.editable : false
    }
  }
}

interface ProjectByIdProps {
  projectId: string,
  editable: boolean
}


const ProjectByIdIndex: FC<ProjectByIdProps> =  (props) => {
  const [project, setProject] = useState<ProjectDetail>()
  const [showProgress, setShowProgress] = useState(true)
  const router = useRouter()

  const fetchProjectById = () => {
    auth.onAuthStateChanged(user => {
      if (user) {
        user.getIdToken(false)
        .then(tokenResult => {
          const projectApi = new ProjectApi(headerConfig(tokenResult))
          projectApi.getProjectById(props.projectId)
          .then(response => {
            if (response.data && response.data) {
              setProject(response.data)
              setShowProgress(false)
            } else {
              toast("Sorry, we could not locate that project", {
                type: 'error',
                position: toast.POSITION.TOP_CENTER
              });
              router.push({
                pathname: PATHS.MY_PROJECTS
              }).then(_ => {
                setShowProgress(false)
              })
              .catch(e => console.log(e))
            }
          }).catch(e => console.log(e))
        }).catch(e => console.log(e))
      }
    })
  }

  useEffect(() => {
    fetchProjectById()
  }, [project])

  return (
      <>
        <Head>
          <title>Kalico | {project?.name}</title>
        </Head>
        <main>
          <section className="container">
            <MyProject project={project} showProgress={showProgress} editable={props.editable} />
          </section>
        </main>

      </>
  );
}

export default ProjectByIdIndex
