import { AppBar, Typography, Box } from '@mui/material'

function Header({ title }) {
    return (
        <Box sx={{ flexGrow: 1 }}>
            <AppBar position="static" sx={{ mb: 2, p: 1 }}>
                <Typography variant="h5" component="div" align='center' sx={{ flexGrow: 20 }}>
                    {title}
                </Typography>
            </AppBar>
        </Box>
    )
}

export default Header