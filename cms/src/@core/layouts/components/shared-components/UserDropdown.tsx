// ** React Imports
import {useState, SyntheticEvent, Fragment, useEffect, FC} from 'react'

// ** Next Import
import { useRouter } from 'next/router'

// ** MUI Imports
import Box from '@mui/material/Box'
import Menu from '@mui/material/Menu'
import Badge from '@mui/material/Badge'
import Avatar from '@mui/material/Avatar'
import Divider from '@mui/material/Divider'
import MenuItem from '@mui/material/MenuItem'
import { styled } from '@mui/material/styles'
import Typography from '@mui/material/Typography'

import LogoutVariant from 'mdi-material-ui/LogoutVariant'
import {auth} from "@/utils/firebase-setup";
import {observer} from "mobx-react";
import {useStore} from "@/hooks/useStore";

// ** Styled Components
const BadgeContentSpan = styled('span')(({ theme }) => ({
  width: 8,
  height: 8,
  borderRadius: '50%',
  backgroundColor: theme.palette.success.main,
  boxShadow: `0 0 0 2px ${theme.palette.background.paper}`
}))

export const UserDropdown: FC<any> = observer((_props) => {
  const[avatar, setAvatar] = useState<string>()
  const[fullName, setFullName] = useState<string>()
  const store = useStore()

  // ** States
  const [anchorEl, setAnchorEl] = useState<Element | null>(null)

  // ** Hooks
  const router = useRouter()

  const handleDropdownOpen = (event: SyntheticEvent) => {
    setAnchorEl(event.currentTarget)
  }

  const handleDropdownClose = (url?: string) => {
    if (url) {
      router.push(url)
    }
    setAnchorEl(null)
  }

  useEffect(() => {
    if (store?.sessionDataStore?.user) {
      setAvatar(store.sessionDataStore.user.picture)
      setFullName(store.sessionDataStore.user.name)
    }
  }, [store?.sessionDataStore?.user])

  const logout = () => {
    auth.signOut().then(_ => {
      console.log("Logout success!")
      store.sessionDataStore.setUser(undefined)
      router.push({
        pathname: '/login',
        query: { backTo: router.pathname }
      }).catch(e => console.log(e))
    }).catch(e => console.log(e))
  }

  return (
    <Fragment>
      <Badge
        overlap='circular'
        onClick={handleDropdownOpen}
        sx={{ ml: 2, cursor: 'pointer' }}
        badgeContent={<BadgeContentSpan />}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
      >
        <Avatar
          alt={fullName}
          onClick={handleDropdownOpen}
          sx={{ width: 40, height: 40 }}
          src={avatar}
        />
      </Badge>
      <Menu
        anchorEl={anchorEl}
        open={Boolean(anchorEl)}
        onClose={() => handleDropdownClose()}
        sx={{ '& .MuiMenu-paper': { width: 230, marginTop: 4 } }}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
        transformOrigin={{ vertical: 'top', horizontal: 'right' }}
      >
        <Box sx={{ pt: 2, pb: 3, px: 4 }}>
          <Box sx={{ display: 'flex', alignItems: 'center' }}>
            <Badge
              overlap='circular'
              badgeContent={<BadgeContentSpan />}
              anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
            >
              <Avatar alt={fullName} src={avatar} sx={{ width: '2.5rem', height: '2.5rem' }} />
            </Badge>
            <Box sx={{ display: 'flex', marginLeft: 3, alignItems: 'flex-start', flexDirection: 'column' }}>
              <Typography sx={{ fontWeight: 600 }}>{fullName}</Typography>
              <Typography variant='body2' sx={{ fontSize: '0.8rem', color: 'text.disabled' }}>
                Staff
              </Typography>
            </Box>
          </Box>
        </Box>
        <Divider sx={{ mt: 0, mb: 1 }} />
        {/*<MenuItem sx={{ p: 0 }} onClick={() => handleDropdownClose()}>*/}
        {/*  <Box sx={styles}>*/}
        {/*    <AccountOutline sx={{ marginRight: 2 }} />*/}
        {/*    Profile*/}
        {/*  </Box>*/}
        {/*</MenuItem>*/}
        {/*<MenuItem sx={{ p: 0 }} onClick={() => handleDropdownClose()}>*/}
        {/*  <Box sx={styles}>*/}
        {/*    <EmailOutline sx={{ marginRight: 2 }} />*/}
        {/*    Inbox*/}
        {/*  </Box>*/}
        {/*</MenuItem>*/}
        {/*<MenuItem sx={{ p: 0 }} onClick={() => handleDropdownClose()}>*/}
        {/*  <Box sx={styles}>*/}
        {/*    <MessageOutline sx={{ marginRight: 2 }} />*/}
        {/*    Chat*/}
        {/*  </Box>*/}
        {/*</MenuItem>*/}
        {/*<Divider />*/}
        {/*<MenuItem sx={{ p: 0 }} onClick={() => handleDropdownClose()}>*/}
        {/*  <Box sx={styles}>*/}
        {/*    <CogOutline sx={{ marginRight: 2 }} />*/}
        {/*    Settings*/}
        {/*  </Box>*/}
        {/*</MenuItem>*/}
        {/*<MenuItem sx={{ p: 0 }} onClick={() => handleDropdownClose()}>*/}
        {/*  <Box sx={styles}>*/}
        {/*    <CurrencyUsd sx={{ marginRight: 2 }} />*/}
        {/*    Pricing*/}
        {/*  </Box>*/}
        {/*</MenuItem>*/}
        {/*<MenuItem sx={{ p: 0 }} onClick={() => handleDropdownClose()}>*/}
        {/*  <Box sx={styles}>*/}
        {/*    <HelpCircleOutline sx={{ marginRight: 2 }} />*/}
        {/*    FAQ*/}
        {/*  </Box>*/}
        {/*</MenuItem>*/}
        {/*<Divider />*/}
        <MenuItem sx={{ py: 2 }} onClick={logout}>
          <LogoutVariant sx={{ marginRight: 2, fontSize: '1.375rem', color: 'text.secondary' }} />
          Logout
        </MenuItem>
      </Menu>
    </Fragment>
  )
})

export default UserDropdown
