import * as React from 'react';
import Button from '@mui/material/Button';
import Avatar from '@mui/material/Avatar';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemAvatar from '@mui/material/ListItemAvatar';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemText from '@mui/material/ListItemText';
import DialogTitle from '@mui/material/DialogTitle';
import Dialog from '@mui/material/Dialog';
import InputIcon from '@mui/icons-material/Input';
import {FC, RefObject} from "react";
// import html2canvas from 'html2canvas';
import { jsPDF } from 'jspdf';


interface ExportTarget {
  platform: string,
  logo: any,
  format: string
}

const platforms: ExportTarget[] = [
  {
    platform: 'WordPress',
    logo: "/assets/images/wordpress.svg",
    format: 'html'
  },
  {
    platform: 'Notion',
    logo: "/assets/images/notion.svg",
    format: 'html'
  },
  {
    platform: 'PDF',
    logo: "/assets/images/pdf.svg",
    format: 'pdf'
  },
  // {
  //   platform: 'Text',
  //   logo: "/assets/images/text.svg",
  //   format: 'txt'
  // }
]

export interface ExportDialogProps {
  editorRef: RefObject<HTMLElement>,
  projectName: string
}
const ExportDialog: FC<ExportDialogProps> = (props) => {
  const [open, setOpen] = React.useState(false);

  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  const handleListItemClick = (value: string) => {
   switch (value) {
     case 'pdf':
       handleDownloadPdf().catch(e => console.log(e))
       break
     case 'html':
       handleDownloadHtml().catch(e => console.log(e))
       break
   }
  };

  const handleDownloadHtml = async () => {
    if (props.editorRef && props.editorRef.current) {
      const element = props.editorRef.current;
      const link = document.createElement("a");
      const file = new Blob([element.innerHTML], {type: 'text/html'})
      link.href = URL.createObjectURL(file);
      link.download = props.projectName + ".html"
      link.click();
      URL.revokeObjectURL(link.href);
    }
  }

  const handleDownloadPdf = async () => {
    if (props.editorRef && props.editorRef.current) {
      const element = props.editorRef.current;
      // const canvas = await html2canvas(element);
      // const data = canvas.toDataURL('image/png');

      const doc = new jsPDF({
        format: 'a4',
        unit: 'px',
      });

      doc.html(element, {
        async callback(doc) {
          await doc.save(props.projectName + '.pdf');
        },
      });


      // const pdf = new jsPDF();
      // const imgProperties = pdf.getImageProperties(data);
      // const pdfWidth = pdf.internal.pageSize.getWidth();
      // const pdfHeight =
      //     (imgProperties.height * pdfWidth) / imgProperties.width;
      //
      // pdf.addImage(data, 'PNG', 0, 0, pdfWidth, pdfHeight);
      // pdf.save(props.projectName + '.pdf');
    }

  };

  return (
      <div>
        <Button
            color="info"
            startIcon={<InputIcon/>}
            onClick={handleClickOpen}
            size='large'
            variant='text'>
          Export
        </Button>
        <Dialog onClose={handleClose} open={open}>
          <DialogTitle sx={{textAlign: 'center', p: 4}}>Select Export Target</DialogTitle>
          <List sx={{ pt: 0 }}>
            {platforms.map((p) => (
                <ListItem  key={p.platform}>
                  <ListItemButton onClick={() => handleListItemClick(p.format)}>
                    <ListItemAvatar>
                      <Avatar sx={{ bgcolor: '#fff', color: '#fff', borderRadius: '5px' }}>
                        <img src={p.logo} alt={p.platform} width={256}/>
                      </Avatar>
                    </ListItemAvatar>
                    <ListItemText primary={p.platform} />
                  </ListItemButton>
                </ListItem>
            ))}
          </List>
        </Dialog>
        {/*<SimpleDialog*/}
        {/*    open={open}*/}
        {/*    onClose={handleClose}*/}
        {/*/>*/}
      </div>
  );
}
export default ExportDialog
