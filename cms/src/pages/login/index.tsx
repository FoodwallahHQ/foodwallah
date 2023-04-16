// ** React Imports
import {ReactNode, useEffect} from 'react'

// ** MUI Components
import Box from '@mui/material/Box'
import Button from '@mui/material/Button'
import Typography from '@mui/material/Typography'
import CardContent from '@mui/material/CardContent'
import { styled } from '@mui/material/styles'
import MuiCard, { CardProps } from '@mui/material/Card'

// ** Icons Imports
import Google from 'mdi-material-ui/Google'

// ** Configs
import themeConfig from '@/configs/themeConfig'

// ** Layout Import
import BlankLayout from '@/@core/layouts/BlankLayout'

// ** Demo Imports
import FooterIllustrationsV1 from '@/views/pages/auth/FooterIllustration'
import {useStore} from "@/hooks/useStore";
import {
  GoogleAuthProvider,
  indexedDBLocalPersistence,
  setPersistence,
  signInWithPopup
} from "firebase/auth";
import {auth} from "@/utils/firebase-setup";
import {UserApi} from "@/api";
import {headerConfig} from "@/api/headerConfig";
import {useRouter} from "next/router";
import {showMessageBar} from "@/utils/message";
import {useSnackbar} from "notistack";
import {observer} from "mobx-react";


// ** Styled Components
const Card = styled(MuiCard)<CardProps>(({ theme }) => ({
  [theme.breakpoints.up('sm')]: { width: '28rem' }
}))

const LoginPage = () => {
  const store = useStore()
  const router = useRouter()
  const { enqueueSnackbar } = useSnackbar();

  useEffect(() => {
    if (store?.sessionDataStore?.user) {
      router.push({
        pathname: '/',
      }).catch(e => console.log(e))
    }
  })


  /**
   * Sign in user using Google login pop screen. After login, persist the user
   * object in indexDB so that the session is not terminated if the user refreshes
   * the page or closes the browser.
   */
  const signInWithGoogle = () => {
    setPersistence(auth, indexedDBLocalPersistence)
    .then(() => {
      // In local persistence will be applied to the signed in Google user
      return signInWithPopup(auth, new GoogleAuthProvider())
      .then((result) => {
        if (result.user) {
          registerUser()
        } else {
          showMessageBar({
            message: "Not Authorized",
            snack: enqueueSnackbar,
            error: true
          });
        }
      }).catch(e => console.log(e));
    })
    .catch((error) => {
      showMessageBar({
        message: error.message,
        snack: enqueueSnackbar,
        error: true
      });
    });
  }


  /**
   * Check if the current user is registered. If not, register them. Once
   * registration has been verified or completed, disable the progress indicator and
   * route the user to the dashboard.
   *
   */
  const registerUser = () => {
    auth.onAuthStateChanged(user => {
      if (user) {
        user.getIdTokenResult(false)
        .then(tokenResult => {
          new UserApi(headerConfig(tokenResult.token))
          .getUserprofile()
          .then(result => {
            if (result.data.profile) {
              store.sessionDataStore.setUser(result.data.profile)
            } else {
              showMessageBar({
                message: result.data.error,
                snack: enqueueSnackbar,
                error: true
              });
            }
          }).catch(e => console.log(e))
        }).catch(e => console.log(e))
      }
    })
  }

  return (
    <Box className='content-center'>
      <Card sx={{ zIndex: 1, height: '400px' }}>
        <CardContent sx={{ padding: theme => `${theme.spacing(12, 9, 7)} !important` }}>
          <Box sx={{ mb: 8, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
            <img src="/images/favicon.ico"/>
            <Typography
              variant='h6'
              sx={{
                ml: 3,
                lineHeight: 1,
                fontWeight: 600,
                fontSize: '1.5rem !important'
              }}
            >
              {themeConfig.templateName}
            </Typography>
          </Box>
          <Box sx={{ mb: 6, textAlign: 'center' }}>
            <Typography variant='body2'>Please sign-in to manage Foodwallah content</Typography>
          </Box>
          <form noValidate autoComplete='off' onSubmit={e => e.preventDefault()}>
            <Button
              startIcon={<Google/>}
              fullWidth
              size='large'
              variant='contained'
              sx={{ marginBottom: 2, marginTop: 6 }}
              onClick={signInWithGoogle}
            >
              Login with Google
            </Button>
          </form>
        </CardContent>
      </Card>
      <FooterIllustrationsV1 />
    </Box>
  )
}

LoginPage.getLayout = (page: ReactNode) => <BlankLayout>{page}</BlankLayout>

export default observer(LoginPage)
