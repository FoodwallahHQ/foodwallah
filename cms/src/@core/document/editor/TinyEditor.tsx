import React, {FC} from 'react';
import { Editor } from '@tinymce/tinymce-react';

export interface EditorProps {
  onEditorChange: (editorContent: string) => void
  editorContent?: string
}
const TinyEditor: FC<EditorProps> = (props) => {
  return (
    <Editor
      apiKey=""
      onEditorChange={props.onEditorChange}
      value={props.editorContent}
      init={{
        skin: "oxide",
        resize: false,
        branding: false,
        statusbar: false,
        // menubar: 'file edit view insert format tools',
        menubar: 'edit insert format tools',
        // toolbar: 'undo redo | bold italic underline strikethrough | fontselect fontsizeselect formatselect | alignleft aligncenter alignright alignjustify | outdent indent |  numlist bullist | forecolor backcolor removeformat | fullscreen print | ltr rtl',
        plugins: [
          // 'export',
          // 'preview',
          // 'searchreplace',
          // 'fullscreen',
          'link',
          'insertdatetime',
          'advlist',
          'lists',
          'wordcount',
          // 'quickbars'
        ],
        // removed_menuitems: 'newdocument',
        // quickbars_selection_toolbar: 'bold italic | quicklink h2 h3 blockquote quickimage quicktable',
        // noneditable_noneditable_class: 'mceNonEditable',
        // content_css: 'fw-tiny-editor',
        // contextmenu: 'link image imagetools',
        templates: [
          // { title: 'New Table', description: 'creates a new table', content: '<div class="mceTmpl"><table width="98%%"  border="0" cellspacing="0" cellpadding="0"><tr><th scope="col"> </th><th scope="col"> </th></tr><tr><td> </td><td> </td></tr></table></div>' },
          // { title: 'Starting my story', description: 'A cure for writers block', content: 'Once upon a time...' },
          // { title: 'New list with dates', description: 'New List with dates', content: '<div class="mceTmpl"><span class="cdate">cdate</span><br /><span class="mdate">mdate</span><h2>My List</h2><ul><li></li><li></li></ul></div>' }
        ],
        // template_cdate_format: '[Date Created (CDATE): %m/%d/%Y : %H:%M:%S]',
        // template_mdate_format: '[Date Modified (MDATE): %m/%d/%Y : %H:%M:%S]',
        // toolbar_sticky: true
      }}
    />
  );
}
export default TinyEditor;
