import axios from 'axios';

import { API_BASE_URL } from '../constants';

class AxiosService {

  constructor() {
    console.log("Axios Service is constructed");
  }

  getRestClient() {
    if (!this.serviceInstance) {
      this.serviceInstance = axios.create({
        baseURL: API_BASE_URL + "/file",
        timeout: 10000,
        headers: {
            'Content-Type': 'application/json'
          },
      });
    }
    return this.serviceInstance;
  }
}

export default (new AxiosService());