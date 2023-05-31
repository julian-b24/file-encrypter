import { Button, TextField } from "@mui/material"
import Header from "../components/Header"
import Spinner from "../components/Spinner"
import { useState } from "react"
import axios from "../util/axios"
import Swal from "sweetalert2"
import withReactContent from "sweetalert2-react-content"
import { NoEncryption } from "@mui/icons-material"

const Decrypt = () => {

    const MySwal = withReactContent(Swal)

    const [loaded, setLoaded] = useState(true)

    const [file, setFile] = useState('')
    const [fileName, setFileName] = useState('')
    const [password, setPassword] = useState('')

    const handleFileChange = (e) => {
        setFile(e.target.files[0])
        setFileName(e.target.value)
    }

    const handleDecrypt = () => {
        const form = new FormData();
        form.append('file', new Blob([file]))
        form.append('password', password)

        setLoaded(false)

        axios.post('/decrypt', form)

            .then((response) => {
                MySwal.fire({
                    icon: 'success',
                    title: '¡Listo!',
                    text: 'El archivo se descifró correctamente',
                    confirmButtonText: 'Descargar',
                    confirmButtonColor: '#04b44c',
                    showCancelButton: true,
                    cancelButtonText: 'Volver',
                    cancelButtonColor: '#0464ac'
                }).then((result) => {
                    if (result.isConfirmed) {
                        const element = document.createElement('a')
                        const file = new Blob([atob(response.data)])
                        element.href = URL.createObjectURL(file)
                        element.download = 'decrypted'
                        document.body.appendChild(element)
                        element.click()
                    }
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
                setFileName('')
                setFile('')
                setPassword('')
                setLoaded(true)
            })
    }

    return (
        <>
            <Header title={'Descifrar archivo'} />
            {loaded ?
                <>
                    <TextField
                        id="file"
                        type="file"
                        label="Archivo"
                        variant="outlined"
                        margin="normal"
                        helperText="Seleccione el archivo a cifrar"
                        value={fileName}
                        onChange={handleFileChange}
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
                        onClick={handleDecrypt}
                        disabled={fileName === '' || password === ''}
                    >
                        Descifrar archivo
                    </Button>
                </>
                :
                <Spinner Icon={NoEncryption} />
            }
        </>
    )
}

export default Decrypt