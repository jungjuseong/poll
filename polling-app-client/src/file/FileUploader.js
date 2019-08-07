import React, { Component } from 'react';
import { FileService } from './FileService.js';

export class FileUploader extends Component {
    constructor() {
        super();
        this.fileService = new FileService();
    }

    handleUploadFile = (event) => {
        
        //using File API to get chosen file
        let source = event.target.files[0];
        
        console.log("Uploading file", source);

        const data = new FormData();

        data.append('file', source);
        data.append('name', 'my_file');
        data.append('description', 'this file is uploaded by IRON');

        //calling async Promise and handling response or error situation
        this.fileService.uploadFileToServer(data).then((response) => {
            console.log("File " + source.name + " is uploaded");
            console.log(response.request.response);
        }).catch(function (error) {
            console.log(error);
            if (error.response) {
                //HTTP error happened
                console.log("Upload error. HTTP error/status code=",error.response.status);
            } else {
                //some other error happened
               console.log("Upload error. HTTP error/status code=",error.message);
            }
        });
    };

    render() {
        return (
            <div>
                <input type="file" onChange={this.handleUploadFile} />
            </div>
        )
    };
}