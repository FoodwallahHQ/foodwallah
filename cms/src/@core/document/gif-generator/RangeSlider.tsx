import React, {FC, useEffect, useState} from 'react';
import Slider from '@material-ui/core/Slider';
import {withStyles} from "@material-ui/core";
import {Box, Typography} from "@mui/material";

const AirbnbSlider = withStyles({
  root: {
    color: '#3a8589',
    height: 3,
    padding: '13px 0',
  },
  thumb: {
    height: 36,
    width: 36,
    backgroundColor: '#fff',
    border: '1px solid currentColor',
    marginTop: -15,
    marginLeft: -13,
    boxShadow: '#ebebeb 0 2px 2px',
    '&:focus, &:hover, &$active': {
      boxShadow: '#ccc 0 2px 3px 1px',
    },
    '& .bar': {
      // display: inline-block !important;
      height: 9,
      width: 1,
      backgroundColor: 'currentColor',
      marginLeft: 1,
      marginRight: 1,
    },
  },
  active: {},
  track: {
    height: 6,
  },
  rail: {
    color: '#d8d8d8',
    opacity: 1,
    height: 6,
  },
})(Slider);

function AirbnbThumbComponent(props: any) {
  return (
      <span {...props}>
      <span className="bar" />
      <span className="bar" />
      <span className="bar" />
    </span>
  );
}

interface RangeSliderProps {
  duration: number,
  max: number,
  min: number,
  onChange: (_value: number[]) => void
}

const RangeSlider: FC<RangeSliderProps> = (props) => {
  const [duration, setDuration] = useState<number>(0)
  const handleChange = (_event: any, newValue: number[]) => {
    props.onChange(newValue)
  };


  useEffect(() => {
    setDuration(props.duration)
  }, [props.duration])

  return (
      <Box className="custom-range-slider" sx={{ml: 10}}>
        <Box sx={{mb: 5}}>
          <Typography>
            Start: {props.min}
          </Typography>
          <Typography>
            End: {props.max}
          </Typography>
        </Box>

        <AirbnbSlider
            ThumbComponent={AirbnbThumbComponent}
            onChange={handleChange}
            max={duration}
            defaultValue={[0, 20]}
        />
      </Box>
  );
}
export default RangeSlider;
