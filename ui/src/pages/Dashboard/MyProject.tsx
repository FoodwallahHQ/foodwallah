import React, {FC, RefObject, useState} from 'react';
import {observer} from "mobx-react";
import {Grid} from "@mui/material";
import ForcedLayoutEditor from "@/components/SlateEditor/ForcedLayoutEditor";
import {ProjectDetail} from "@/api";
import {CenterAlignedProgress} from "@/utils/utils";

export interface MyProjectsProps {
  project: ProjectDetail,
  showProgress: boolean,
  editable?: boolean
}

const MyProject: FC<MyProjectsProps> = observer((props) => {
  const [_editorRef, setEditorRef] = useState<RefObject<HTMLElement> | undefined>(undefined)

  const handleSetEditorRef = (ref: RefObject<HTMLElement>) => {
    setEditorRef(ref)
  }

  return (
      <Grid container className="dashboard-container editor">
        <Grid item sm={12} sx={{width: '100%'}}>
          {
            props.showProgress ? <CenterAlignedProgress/> :
                <>
                  {/*<ExportDialog editorRef={editorRef} projectName={props.project?.name}/>*/}
                  <ForcedLayoutEditor
                      editable={props.editable}
                      setEditorRef={handleSetEditorRef}
                      project={props.project}
                  />
                </>
          }
        </Grid>
      </Grid>
  );
})

export default MyProject;
