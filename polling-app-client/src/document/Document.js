import React, { Component } from 'react';

import { Input,Avatar, Button } from 'antd';
import { getAvatarColor } from '../util/Colors';
import { formatDateTime } from '../util/Helpers';

import streamSaver from 'streamsaver'
import { createWriter } from '../util/zip-stream';
import '../util/Blob';

import './Document.css';

const { TextArea } = Input

window.done = false;
window.onunload = () => {
    // writableStream.abort()
    // // also possible to call abort on the writer you got from `getWriter()`
    // writer.abort()
}

window.onbeforeunload = evt => {
    if (!window.done) {
        evt.returnValue = `Are you sure you want to leave?`;
    }
}

class Document extends Component {

    makeZip(event) {
        const fileStream = streamSaver.createWriteStream('archive.zip')
        const file1 = new File(['file1 content'], '/streamsaver-zip-example/file1.txt')
        // File Like object works too
        const file2 = {
          name: '/streamsaver-zip-example/file2.txt',
          stream () {
            // if you want to play it cool and use new api's
            //
            // const { readable, writable } = new TextEncoderStream()
            // writable.write('file2 content')
            // return readable
            return new ReadableStream({
              start (ctrl) {
                ctrl.enqueue(new TextEncoder().encode('file2 generated with readableStream'))
                ctrl.close()
              }
            })
          }
        }
        const blob = new Blob(['support blobs too'])
        const file3 = {
          name: '/streamsaver-zip-example/blob-example.txt',
          stream: () => blob.stream()
        }
        const readableZipStream = new createWriter({
          start (ctrl) {
            ctrl.enqueue(file1)
            ctrl.enqueue(file2)
            ctrl.enqueue(file3)
            ctrl.enqueue({name: '/streamsaver-zip-example/empty folder', directory: true})
            // ctrl.close()
          },
          async pull (ctrl) {
            const url2 = 'https://d8d913s460fub.cloudfront.net/videoserver/cat-test-video-320x240.mp4'
            const url = 'ec2-15-164-218-77.ap-northeast-2.compute.amazonaws.com/images/7e625eca-b53f-5eec-9b83-7f3b3f9960fb-donga-jjs.jpg'
            const res = await fetch(url)
            const stream = () => res.body
            const name = '/streamsaver-zip-example/dongajjs.jpg'
            ctrl.enqueue({ name, stream })

            ctrl.close()
          },
        });
        // more optimized
        if (window.WritableStream && readableZipStream.pipeTo) {
          return readableZipStream.pipeTo(fileStream).then(() => {
            window.done = true;  
            console.log('done writing')
          })
        }
		// less optimized
        window.writer = fileStream.getWriter()
        const reader = readableZipStream.getReader()
        const pump = () => reader.read()
          .then(res => res.done ? window.writer.close() : window.write(res.value).then(pump))
        pump();
    }

    render() {    
        let bgColor = getAvatarColor(this.props.document.name);
        bgColor = (this.props.document.deadmark === 'y') ? bgColor ^ 0xff00ff : bgColor;

        return (
            <div className="document-content">
                <div className="document-header">
                    <div className="document-name">
                        {this.props.document.name}
                    </div>
                    <div className="document-creator-info">
                        <section className="creator-link" to={`/users/${this.props.document.name}`}>
                            <Avatar className="document-creator-avatar" 
                                style={{ backgroundColor: bgColor}} >
                                {this.props.document.name.toUpperCase()}
                            </Avatar>
                            <span className="document-creator-name">
                                {this.props.document.name}
                            </span>
                            <span className="document-creator-username">
                                @{this.props.document.name}
                            </span>
                            <span className="document-creation-date">
                                {formatDateTime(this.props.document.creationDateTime)} 
                            </span>
                        </section>
                    </div>

                    <div className="document-name">
                        <TextArea 
                            style = {{ fontSize: '12px' }} 
                            autosize={{ minRows: 3, maxRows: 10 }} 
                            name = "contents"
                            value = {this.props.document.contents}
                         />
                    </div>
                    <div className="document-name">
                        <TextArea 
                            style = {{ fontSize: '12px' }} 
                            autosize={{ minRows: 3, maxRows: 10 }} 
                            name = "preference"
                            value = {this.props.document.preference}
                        />
                    </div>
                    <div className="document-name">
                        <Button onClick={this.makeZip} document={this.props.document}>Make Zip</Button>
                    </div>
                </div>
            </div>
        );
    }
}

export default Document;