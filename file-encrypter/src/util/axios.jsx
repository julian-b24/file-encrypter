import instance from 'axios'

const axios = instance.create({ baseURL: 'http://localhost:8182/api' })

export default axios