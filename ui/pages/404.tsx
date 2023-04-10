import { useEffect } from "react"
import { useRouter } from "next/router"

function Custom404() {
  const router = useRouter()

  useEffect(() => {
    router.replace("/")
    .catch(e => console.log(e))
  })

  return null
}
export default Custom404