import { Button, TextField } from "@mui/material"
import Header from "../components/Header"
import { useState } from "react"
import axios from "../util/axios"

const Encrypt = () => {

    const [file, setFile] = useState('')
    const [password, setPassword] = useState('')

    const handleEncrypt = () => {
        const form = new FormData();
        form.append('file', new Blob([file]))
        form.append('password', password)
        form.append('outputFilePath', 'C:\\Users\\Carlos\\Downloads\\')

        axios.post('/encrypt', form)
        .then((res) => {
            console.log(res)
        })
        .catch((err) => {
            console.log(err)
        })
    }

    return (
        <>
            <Header title={'Cifrar archivo'} />
            <TextField
                id="file"
                type="file"
                label="Archivo"
                variant="outlined"
                margin="normal"
                helperText="Seleccione el archivo a cifrar"
                value={file}
                onChange={(e) => setFile(e.target.value)}
                InputLabelProps={{
                    shrink: true,
                }}
                sx={{
                    width: '100%',
                }}
            />
            <TextField
                id="password"
                type="password"
                label="Contraseña"
                variant="outlined"
                margin="normal"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                helperText="Ingrese la contraseña para cifrar el archivo"
                sx={{
                    width: '100%'
                }}
            />
            <Button
                variant="contained"
                onClick={handleEncrypt}
                disabled={!file || !password}
            >
                Cifrar
            </Button>
        </>
    )
}

export default Encrypt