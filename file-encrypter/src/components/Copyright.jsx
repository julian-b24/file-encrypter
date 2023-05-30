import Typography from '@mui/material/Typography'
import Link from '@mui/material/Link'

export default function Copyright(props) {
    return (
        <Typography variant="body2" color="text.secondary" align="center" {...props}>
            {'Copyright © '}
            <Link color="inherit" href="https://github.com/CarlosJPantoja" target="_blank">
                CarlosJPantoja
            </Link>{', '}
            <Link color="inherit" href="https://github.com/julian-b24" target="_blank">
                julian-b24
            </Link>{', '}
            <Link color="inherit" href="https://github.com/Estebanm1812" target="_blank">
                Estebanm1812
            </Link>{' '}
            {new Date().getFullYear()}
            {'.'}
        </Typography>
    )
}