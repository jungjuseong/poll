import service from './AxiosService.js';

export class FileService {
    uploadFileToServer(data){
        return service.getRestClient().post('/upload', data);
    }
}