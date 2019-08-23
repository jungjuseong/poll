import React, { Component } from 'react';

import { Input,Avatar, Button } from 'antd';
import { getAvatarColor } from '../util/Colors';
import { formatDateTime } from '../util/Helpers';

import streamSaver from 'streamsaver'
import { createWriter } from '../util/zip-stream';
import '../util/Blob';

import './Document.css';

const { TextArea } = Input

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
            const url = 'https://d8d913s460fub.cloudfront.net/videoserver/cat-test-video-320x240.mp4'
            const res = await fetch(url)
            const stream = () => res.body
            const name = '/streamsaver-zip-example/cat.mp4'
            ctrl.enqueue({ name, stream })
            ctrl.close()
          }
        });
        // more optimized
        if (window.WritableStream && readableZipStream.pipeTo) {
          return readableZipStream.pipeTo(fileStream).then(() => console.log('done writing'))
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