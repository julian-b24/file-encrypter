import { Navigate, RouterProvider, createBrowserRouter } from 'react-router-dom'
import Dashboard from '../components/Dashboard'
import Encrypt from '../pages/Encrypt'
import Decrypt from '../pages/Decrypt'

export default function Routes() {

    const router = createBrowserRouter(
        [
            {
                path: "/",
                element: <Dashboard />,
                children: [
                    {
                        path: "*",
                        element: <Navigate to="/" />
                    },
                    {
                        path: "/",
                        element: <Encrypt />
                    },
                    {
                        path: "/decrypt",
                        element: <Decrypt />
                    }
                ]
            }
        ]
    )

    return (
        <RouterProvider router={router} />
    )
}