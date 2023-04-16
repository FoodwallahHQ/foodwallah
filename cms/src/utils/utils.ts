import moment from "moment";

export const getFormattedDate = (date: number) => {
  return moment.unix(date).format("llll")
}