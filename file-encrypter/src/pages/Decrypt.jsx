import { Button, TextField } from "@mui/material"
import Header from "../components/Header"
import Spinner from "../components/Spinner"
import { useState } from "react"
import axios from "../util/axios"
import Swal from "sweetalert2"
import withReactContent from "sweetalert2-react-content"
import { NoEncryption } from "@mui/icons-material"

const Decrypt = () => {

    const type = {
        'txt': 'text/plain',
        'pdf': 'application/pdf',
        'png': 'image/png',
        'jpg': 'image/jpeg',
        'jpeg': 'image/jpeg',
        'gif': 'image/gif',
        'bmp': 'image/bmp',
        'tiff': 'image/tiff',
        'tif': 'image/tiff',
        'svg': 'image/svg+xml',
        'mp3': 'audio/mpeg',
        'wav': 'audio/wav',
        'ogg': 'audio/ogg',
        'mp4': 'video/mp4',
        'webm': 'video/webm',
        'avi': 'video/x-msvideo',
        'mov': 'video/quicktime',
        'wmv': 'video/x-ms-wmv',
        'flv': 'video/x-flv',
        'mkv': 'video/x-matroska',
        'zip': 'application/zip',
        'rar': 'application/x-rar-compressed',
        '7z': 'application/x-7z-compressed',
        'tar': 'application/x-tar',
        'gz': 'application/gzip',
        'exe': 'application/x-msdownload',
        'iso': 'application/x-iso9660-image',
        'doc': 'application/msword',
        'docx': 'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
        'xls': 'application/vnd.ms-excel',
        'xlsx': 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
        'ppt': 'application/vnd.ms-powerpoint',
        'pptx': 'application/vnd.openxmlformats-officedocument.presentationml.presentation',
        'odt': 'application/vnd.oasis.opendocument.text',
        'ods': 'application/vnd.oasis.opendocument.spreadsheet',
        'odp': 'application/vnd.oasis.opendocument.presentation',
        'csv': 'text/csv',
        'ics': 'text/calendar',
        'html': 'text/html',
        'htm': 'text/html',
        'css': 'text/css',
        'js': 'text/javascript',
        'sh': 'application/x-sh',
        'swift': 'text/x-swift',
        'c': 'text/x-c',
        'cpp': 'text/x-c',
        'h': 'text/x-c',
        'hpp': 'text/x-c',
        'java': 'text/x-java-source'
    }

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
                    inputLabel: 'Escriba la extensión del archivo (.txt, .pdf, .etc)',
                    input: 'text',
                    confirmButtonText: 'Descargar',
                    confirmButtonColor: '#04b44c',
                    showCancelButton: true,
                    cancelButtonText: 'Volver',
                    cancelButtonColor: '#0464ac',
                    inputValidator: (value) => {
                        if (!value) {
                            return 'Debe ingresar la extensión del archivo'
                        }
                        if (!value.startsWith('.')) {
                            return 'Debe ingresar una extensión válida'
                        }
                    }
                }).then((result) => {
                    if (result.isConfirmed) {
                        const element = document.createElement('a')
                        element.href = 'data:' + (type[result.value] ? type[result.value] : 'text/plain') + ';base64,' + response.data
                        element.download = 'decrypted' + result.value
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