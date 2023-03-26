import {Editor, Element as SlateElement, Node, Transforms} from "slate";
import {ImageElement, ParagraphElement, TitleElement} from "@/components/SlateEditor/custom-types";
import {ReactEditor, useFocused, useReadOnly, useSelected, useSlateStatic} from "slate-react";
import isUrl from 'is-url'
import imageExtensions from 'image-extensions'
import React, {PropsWithChildren, Ref} from "react";
import { cx, css } from '@emotion/css'
import {Button} from "@mui/material";

interface BaseProps {
  className: string
  [key: string]: unknown
}
type OrNull<T> = T | null

export const withLayout = editor => {
  const { normalizeNode } = editor

  editor.normalizeNode = ([node, path]) => {
    if (path.length === 0) {
      if (editor.children.length <= 1 && Editor.string(editor, [0, 0]) === '') {
        const title: TitleElement = {
          type: 'title',
          children: [{ text: 'Untitled' }],
        }
        Transforms.insertNodes(editor, title, {
          at: path.concat(0),
          select: true,
        })
      }

      if (editor.children.length < 2) {
        const paragraph: ParagraphElement = {
          type: 'paragraph',
          children: [{ text: '' }],
        }
        Transforms.insertNodes(editor, paragraph, { at: path.concat(1) })
      }

      for (const [child, childPath] of Node.children(editor, path)) {
        let type: string
        const slateIndex = childPath[0]
        const enforceType = type => {
          if (SlateElement.isElement(child) && child.type !== type) {
            const newProperties: Partial<SlateElement> = { type }
            Transforms.setNodes<SlateElement>(editor, newProperties, {
              at: childPath,
            })
          }
        }

        switch (slateIndex) {
          case 0:
            type = 'title'
            enforceType(type)
            break
          case 1:
            type = 'paragraph'
            enforceType(type)
            break
          default:
            break
        }
      }
    }

    return normalizeNode([node, path])
  }

  return editor
}

export const insertImage = (editor, url) => {
  const text = { text: '' }
  const image: ImageElement = { type: 'image', url, children: [text] }
  Transforms.insertNodes(editor, image)
}

export const isImageUrl = url => {
  if (!url) return false
  if (!isUrl(url)) return false
  const ext = new URL(url).pathname.split('.').pop()
  return imageExtensions.includes(ext)
}

export const withImages = editor => {
  const { insertData, isVoid } = editor

  editor.isVoid = element => {
    return element.type === 'image' ? true : isVoid(element)
  }

  editor.insertData = data => {
    const text = data.getData('text/plain')
    const { files } = data

    if (files && files.length > 0) {
      for (const file of files) {
        const reader = new FileReader()
        const [mime] = file.type.split('/')

        if (mime === 'image') {
          reader.addEventListener('load', () => {
            const url = reader.result
            insertImage(editor, url)
          })

          reader.readAsDataURL(file)
        }
      }
    } else if (isImageUrl(text)) {
      insertImage(editor, text)
    } else {
      insertData(data)
    }
  }

  return editor
}

export const InsertImageButton = () => {
  const editor = useSlateStatic()
  return (
      <Button
          onMouseDown={event => {
            event.preventDefault()
            const url = window.prompt('Enter the URL of the image:')
            if (url && !isImageUrl(url)) {
              alert('URL is not an image')
              return
            }
            url && insertImage(editor, url)
          }}
      >
        Insert Image
        {/*<Icon>image</Icon>*/}
      </Button>
  )
}

export const Menu = React.forwardRef(
    (
        { className, ...props }: PropsWithChildren<BaseProps>,
        ref: Ref<OrNull<HTMLDivElement>>
    ) => (
        <div
            {...props}
            ref={ref}
            className={cx(
                className,
                css`
          & > * {
            display: inline-block;
          }

          & > * + * {
            margin-left: 15px;
          }
        `
            )}
        />
    )
)

export const Image = ({ attributes, children, element }) => {
  // const editor = useSlateStatic()
  // const path = ReactEditor.findPath(editor, element)

  const selected = useSelected()
  const focused = useFocused()
  return (
      <div {...attributes}>
        {children}
        <div
            contentEditable={false}
            className={css`
          position: relative;
        `}
        >
          <img
              src={element.url}
              className={css`
            display: block;
            max-width: 100%;
            max-height: 20em;
            box-shadow: ${selected && focused ? '0 0 0 3px #B4D5FF' : 'none'};
          `}
          />
          {/*<Button*/}
          {/*    sx={{*/}
          {/*      width: '32px'*/}
          {/*    }}*/}
          {/*    color='warning'*/}
          {/*    size='large'*/}
          {/*    variant='text'*/}
          {/*    startIcon={<DeleteIcon/>}*/}
          {/*    onClick={() => Transforms.removeNodes(editor, { at: path })}*/}
          {/*    className={css`*/}
          {/*  display: ${selected && focused ? 'inline' : 'none'};*/}
          {/*  position: absolute;*/}
          {/*  top: 2px;*/}
          {/*  left: 2px;*/}
          {/*`}*/}
          {/*/>*/}
        </div>
      </div>
  )
}

const CheckListItemElement = ({ attributes, children, element }) => {
  const editor = useSlateStatic()
  const readOnly = useReadOnly()
  const { checked } = element
  return (
      <div
          {...attributes}
          className={css`
        display: flex;
        flex-direction: row;
        align-items: center;
        & + & {
          margin-top: 0;
        }
      `}
      >
      <span
          contentEditable={false}
          className={css`
          margin-right: 0.75em;
        `}
      >
        <input
            type="checkbox"
            checked={checked}
            onChange={event => {
              const path = ReactEditor.findPath(editor, element)
              const newProperties: Partial<SlateElement> = {
                checked: event.target.checked,
              }
              Transforms.setNodes(editor, newProperties, { at: path })
            }}
        />
      </span>
        <span
            contentEditable={!readOnly}
            suppressContentEditableWarning
            className={css`
          flex: 1;
          opacity: ${checked ? 0.666 : 1};
          text-decoration: ${!checked ? 'none' : 'line-through'};
          &:focus {
            outline: none;
          }
        `}
        >
        {children}
      </span>
      </div>
  )
}

export const EditorElement = ({ attributes, children, element }) => {
  const props = { attributes, children, element }
  switch (element.type) {
    case 'title':
      return <h3 {...attributes} className="slate-editor-title">{children}</h3>
    case 'heading':
      return <h5 {...attributes} className="slate-editor-heading">{children}</h5>
    case 'paragraph':
      return <p {...attributes} className="slate-editor-paragraph">{children}</p>
    case 'check-list-item':
      return <CheckListItemElement {...props} />
    case 'image':
      return <Image {...props} />
  }
}