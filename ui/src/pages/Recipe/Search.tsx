import React, {useState} from 'react';
import {Box, CircularProgress} from "@mui/material";
import SearchBar from "material-ui-search-bar";
import {ProjectApi, RecipeApi} from "@/api";
import {useRouter} from "next/router";

function Search() {
  const [value, setValue] = useState("")
  const [showProgress, setShowProgress] = useState(false)
  const [error, setError] = useState('')
  const [status, setStatus] = useState('')
  const [contentThumbnail, setContentThumbnail] = useState<string | undefined>(undefined)
  const [contentTitle, setContentTitle] = useState<string | undefined>(undefined)
  const [contentDuration, setContentDuration] = useState<string | undefined>(undefined)
  const router = useRouter()

  const resetState = (newValue: string, oldValue: string) => {
    if (newValue !== oldValue) {
      setContentTitle(undefined)
      setContentDuration(undefined)
      setContentThumbnail(undefined)
      setError('')
      setStatus('')
    }
  }

  const getContentPreview = (url: string) => {
    if (url && url.includes("http")) {
      new ProjectApi().getContentPreview(url)
      .then(response => {
        if (response.data.title) {
          let shortTitle = response.data.title.substring(0, 200) + "..."
          setContentTitle(shortTitle)
        } else {
          setContentTitle('')
        }
        if (response.data.thumbnail) {
          setContentThumbnail(response.data.thumbnail)
        } else {
          setContentThumbnail('')
        }
        if (response.data.duration) {
          setContentDuration(response.data.duration)
        } else {
          setContentDuration('')
        }
      })
      .catch(e => console.log(e))
    }
  }

  const handleValue = (newValue: string) => {
    resetState(newValue, value)
    setValue(newValue)
    getContentPreview(newValue)
  }

  const handleSubmit = () => {
    setShowProgress(true)
    setError('')
    new RecipeApi()
    .createRecipe({value: value})
    .then(result => {
      setShowProgress(false)
      if (result.data) {
        if (result.data.slug) {
          router.push("/recipe/" + result.data.slug)
          .then(_ => {
            setError('')
          })
          .catch(e => console.log(e))
        } else if (result.data.status) {
          setStatus(result.data.status)
        } else if (result.data.error) {
          setError(result.data.error)
        }
      } else {
        setError("Sorry, something went wrong. Please check your network connection.")
      }
    }).catch(e => console.log(e))
  }
  return (
            <Box className="search-bar-container">
            <SearchBar
                value={value}
                placeholder="https://www.youtube.com/watch?v=mCNH9rn2OS0"
                onChange={handleValue}
                onRequestSearch={handleSubmit}
            />
              {contentTitle &&
                  <Box className="search-content-preview">
                    {contentThumbnail &&
                        <Box className="content-link-preview">
                          <img src={contentThumbnail} alt={''}/>
                        </Box>
                    }
                    <Box className="content-link-preview">
                      {contentTitle && <h6>{contentTitle}</h6>}
                      {contentDuration && <span>{contentDuration}</span>}
                    </Box>
                  </Box>
              }
              {showProgress &&
                  <Box className="search-progress">
                    <Box className="progress-box">
                      <CircularProgress/>
                    </Box>
                    <Box className="text">
                      <span>This will take just a minute</span>
                    </Box>
                  </Box>
              }
              {!status && error && <p className="error">{error}</p>}
              {!error && status && <p className="success">{status}</p>}
            </Box>
  );
}

export default Search;
