import { Button, TextField, Typography } from "@mui/material"
import Header from "../components/Header"
import Spinner from "../components/Spinner"
import { useState } from "react"
import axios from "../util/axios"
import Swal from "sweetalert2"
import withReactContent from "sweetalert2-react-content"
import { Https } from "@mui/icons-material"

const Encrypt = () => {

    const MySwal = withReactContent(Swal)

    const [loaded, setLoaded] = useState(true)
    const [encrypted, setEncrypted] = useState(false)

    const [file, setFile] = useState('')
    const [password, setPassword] = useState('')

    const [hash, setHash] = useState('')
    const [encryptedFile, setEncryptedFile] = useState('')

    const handleEncrypt = () => {
        const form = new FormData();

        form.append('file', new Blob([file]))
        form.append('password', password)
        form.append('outputFilePath', '/Users/esteban/Desktop/')

        setLoaded(false)

        axios.post('/encrypt', form)

            .then((response) => {
                setHash(response.data.hashOriginalFile)
                setEncryptedFile(response.data.content)
                setEncrypted(true)
                MySwal.fire({
                    icon: 'success',
                    title: '¡Listo!',
                    text: 'El archivo se cifró correctamente',
                    confirmButtonText: 'Aceptar',
                    confirmButtonColor: '#0464ac'
                })
            })

            .catch((error) => {
                MySwal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: error.response.data.message,
                    confirmButtonText: 'Aceptar',
                    confirmButtonColor: '#0464ac'
                })
            })

            .finally(() => {
                setFile('')
                setPassword('')
                setLoaded(true)
            })
    }

    const handleDownload = () => {
        const element = document.createElement('a')
        const file = new Blob([encryptedFile], { type: 'text/plain' })
        element.href = URL.createObjectURL(file)
        element.download = 'encrypted.cif'
        document.body.appendChild(element)
        element.click()
    }

    return (
        <>
            <Header title={'Cifrar archivo'} />
            {loaded ?
                <>
                    {encrypted ?
                        <>
                        <Typography variant="h6" sx={{my:2}}>
                            Hash del archivo original
                        </Typography>
                        <Typography variant="body1" sx={{mb:2}}>
                            {hash}
                        </Typography>
                        </>
                        :
                        <>
                            <TextField
                                id="file"
                                type="file"
                                label="Archivo"
                                variant="outlined"
                                margin="normal"
                                helperText="Seleccione el archivo a cifrar"
                                value={file}
                                onChange={(e) => setFile(e.target.value)}
                                disabled={encrypted}
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
                                disabled={encrypted}
                                helperText="Ingrese la contraseña para cifrar el archivo"
                                sx={{
                                    width: '100%'
                                }}
                            />
                        </>
                    }
                    <Button
                        variant="contained"
                        onClick={encrypted ? handleDownload : handleEncrypt}
                        disabled={!encrypted && (file === '' || password === '')}
                    >
                        {encrypted ? 'Descargar archivo cifrado' : 'Cifrar archivo'}
                    </Button>
                </>
                :
                <Spinner Icon={Https} />
            }
        </>
    )
}

export default Encrypt