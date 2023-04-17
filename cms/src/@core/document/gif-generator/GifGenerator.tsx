import React, {FC, useEffect, useRef, useState} from "react";
import Card from "@mui/material/Card";
import Hls from "hls.js";
import CardHeader from "@mui/material/CardHeader";
import {Box, Button, CircularProgress, Divider, Grid, Typography} from "@mui/material";
import RangeSlider from "@/@core/document/gif-generator/RangeSlider";
import {auth} from "@/utils/firebase-setup";
import {CmsApi} from "@/api";
import {headerConfig} from "@/api/headerConfig";
import {showMessageBar} from "@/utils/message";
import {useSnackbar} from "notistack";
import ContentCopyIcon from "@mui/icons-material/ContentCopy";

interface GifGeneratorProps {
  video_url?: string
  docId?: number
  onCopyToClipboard: (_value: string) => void
}

const GifGenerator: FC<GifGeneratorProps> = (props) => {
  const [minValue, setMinValue] = useState(0);
  const [maxValue, setMaxValue] = useState(0);
  const [canPlay, setCanPlay] = useState<boolean>(false)
  const [isPlaying, setIsPlaying] = useState<boolean>(false)
  const [fetchingGif, setFetchingGif] = useState<boolean>(false)
  const [duration, setDuration] = useState<number>(0)
  const [gifUrl, setGifUrl] = useState<string>('')
  const videoRef = useRef<HTMLVideoElement>(null);
  const { enqueueSnackbar } = useSnackbar();
  const SMOOTHING_FACTOR = 10;

  const getRoundedNumber = (value: number) => {
    const res = Math.round(value * 10) / 10
    if (videoRef?.current && res > videoRef?.current.duration) {
      return videoRef?.current.duration
    }
    return res
  }

  const generateGif = () => {
    setFetchingGif(true)
    auth.onAuthStateChanged(user => {
      if (user) {
        user.getIdTokenResult(false)
        .then(tokenResult => {
          new CmsApi(headerConfig(tokenResult.token))
          .generateGif({
            doc_id: props.docId,
            start: getRoundedNumber(minValue/SMOOTHING_FACTOR),
            end: getRoundedNumber(maxValue/SMOOTHING_FACTOR)
          })
          .then(result => {
            setFetchingGif(false)
            if (result.data) {
              setGifUrl(result.data.url)
              showMessageBar({
                message: result.data.url ? 'GIF generated' : result.data.error,
                snack: enqueueSnackbar,
                error: !!result.data.error
              });
            }
          }).catch(e =>{
            console.log(e)
            showMessageBar({
              message: e.message,
              snack: enqueueSnackbar,
              error: true
            });
          })
        }).catch(e => console.log(e))
      }
    })
  }

  const deleteGif = () => {
    auth.onAuthStateChanged(user => {
      if (user) {
        user.getIdTokenResult(false)
        .then(tokenResult => {
          new CmsApi(headerConfig(tokenResult.token))
          .deleteGif(gifUrl)
          .then(result => {
            setGifUrl('')
            if (result.data) {
              showMessageBar({
                message: result.data.status ? 'GIF deleted' : result.data.error,
                snack: enqueueSnackbar,
                error: !!result.data.error
              });
            }
          }).catch(e =>{
            console.log(e)
            showMessageBar({
              message: e.message,
              snack: enqueueSnackbar,
              error: true
            });
          })
        }).catch(e => console.log(e))
      }
    })
  }

  const handleRangeChange = (value: number[]) => {
    if (value && value.length == 2) {
      setMinValue(value[0])
      setMaxValue(value[1])
      if (videoRef) {
        play()
      }
    }
  }
  const onCanPlay = () => {
    setCanPlay(true)
    setDuration(videoRef?.current.duration * SMOOTHING_FACTOR)
  }

  const conditionalPlay = () => {
    if (isPlaying) {
      pause()
    } else {
      play(true)
    }
  }

  const play = (conditional?: boolean) => {
    videoRef.current.muted = false;
    if (!conditional) {
      videoRef.current.currentTime = maxValue/SMOOTHING_FACTOR
    }
    videoRef?.current?.play()
    .then(_ => setIsPlaying(true))
    .catch(e => console.log(e))
  }

  const pause = () => {
    videoRef?.current?.pause();
    setIsPlaying(false)
  }

  const initHlsPlayer = (): void => {
    const video = videoRef.current
    // Pause the video after loading so it gets played by the intersection observer
    try {
      if (Hls.isSupported()) {
        const hls = new Hls();
        hls.loadSource(props?.video_url);
        hls.attachMedia(video);
        hls.on(Hls.Events.MANIFEST_PARSED, function () {
          pause()
        });
      }
          // hls.js is not supported on platforms that do not have Media Source Extensions (MSE) enabled.
          // When the browser has built-in HLS support (check using `canPlayType`), we can provide an HLS manifest (i.e. .m3u8 URL) directly to the video element throught the `src` property.
      // This is using the built-in support of the plain video element, without using hls.js.
      else if (video.canPlayType('application/vnd.apple.mpegurl')) {
        video.src = props?.video_url;
        video.addEventListener('canplay', function () {
          pause()
        });
      }
    } catch (e) {
      console.log(e)
    }
  }

  useEffect(() => {
    initHlsPlayer()
    play()
  }, [videoRef.current])

  return (
      <Card>
        <CardHeader title="Generate GIFs" titleTypographyProps={{ variant: 'h6' }} />
        <Box sx={{ml: 4, mr: 4, mb: 4}}>
          <Typography variant='body2'>
            Generate GIFs here. If you don't like the GIF, discard it and try again.
          </Typography>
        </Box>
        <Divider sx={{ margin: 0 }} />

        <Grid container spacing={3}>
          <Grid item sm={12} md={6} sx={{background: '#fdfdfd'}}>
            <Box className="video-column">
              <div className="video-detail">
                <video
                    webkit-playsinline={"true"}
                    playsInline={true}
                    autoPlay={true}
                    muted={true}
                    preload="auto"
                    id="gif-video"
                    className="video__player_detail"
                    onClick={conditionalPlay}
                    onCanPlay={onCanPlay}
                    loop
                    ref={videoRef}
                    src={props?.video_url}
                    style={{display: canPlay ? "inline-flex": 'none'}}
                />
              </div>

              <Box className='multi-range-slider-container'>
                <RangeSlider
                    max={getRoundedNumber(maxValue/SMOOTHING_FACTOR)}
                    min={getRoundedNumber(minValue/SMOOTHING_FACTOR)}
                    duration={duration}
                    onChange={handleRangeChange}/>
              </Box>
            </Box>
            {
              canPlay &&
                  <Box className="generate-gif-button">
                    <Button variant="contained" color="success" onClick={generateGif}>
                      Generate GIF
                    </Button>
                  </Box>
            }

          </Grid>
          {canPlay &&
              <Grid item sm={12} md={6} sx={{background: '#fffcf9'}}>
                <Box sx={{p: 2}}>
                  <Typography variant='body2'>
                    Preview
                  </Typography>
                </Box>

                {fetchingGif &&
                    <Box sx={{height: '400px', mt: '25%', ml: '25%'}}>
                      <CircularProgress/>
                    </Box>
                }
                {
                    !fetchingGif && gifUrl &&
                    <>
                      <Box className='generated-gif-box' sx={{height: '550px'}}>
                        <img src={gifUrl}/>
                      </Box>
                      <Box sx={{mt: 32}}>
                        <Typography variant='body2' sx={{
                          textAlign: 'center',
                          wordBreak: 'break-word',
                          mr: 5,
                          fontWeight: 300,
                          mt: '0.5rem',
                          mb: '0.5rem',
                          pl: '0.5rem',
                        }}>
                          <Button
                              sx={{width: '48px', pl: '5px', pr: '0px', mr: '20px', height: '38px'}}
                              onClick={() => props.onCopyToClipboard(gifUrl)}
                              variant="contained"
                              startIcon={<ContentCopyIcon/>}
                              className="overview-btn green"/>
                          {gifUrl}
                        </Typography>
                        <Box className="discard-gif-button">
                          <Button
                              className="overview-btn red"
                              variant="contained"
                              color="warning"
                              onClick={deleteGif}>
                            Discard GIF
                          </Button>
                        </Box>
                      </Box>
                    </>
                }
              </Grid>
          }
        </Grid>

      </Card>
  );
}

export default GifGenerator;